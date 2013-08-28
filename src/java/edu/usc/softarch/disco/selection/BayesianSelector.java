/* Copyright (c) 2006 University of Southern California.
 * All rights reserved.                                            
 *                                                                
 * Redistribution and use in source and binary forms are permitted
 * provided that the above copyright notice and this paragraph are
 * duplicated in all such forms and that any documentation, 
 * advertising materials, and other materials related to such 
 * distribution and use acknowledge that the software was 
 * developed by the Software Architecture Research Group at the 
 * University of Southern Calfornia.  The name of the University may 
 * not be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 * 
 * Any questions, comments, or or corrections should be mailed to the author 
 * of this code, Chris Mattmann, at: mattmann@usc.edu
 *
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE.
 * 
 */

package edu.usc.softarch.disco.selection;

// JDK imports
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

// DISCO imports
import edu.usc.softarch.disco.connector.atomic.DataAccessConnectorProfile;
import edu.usc.softarch.disco.connector.atomic.DistributorConnectorProfile;
import edu.usc.softarch.disco.connector.atomic.StreamConnectorProfile;
import edu.usc.softarch.disco.connector.dcp.DistributionConnectorProfile;
import edu.usc.softarch.disco.selection.bayes.BayesUtil;
import edu.usc.softarch.disco.selection.bayes.BayesianInferencer;
import edu.usc.softarch.disco.selection.bayes.ConditionalProbabilityTable;
import edu.usc.softarch.disco.selection.bayes.DiscreteStatement;
import edu.usc.softarch.disco.selection.bayes.ProbabilityDistribution;
import edu.usc.softarch.disco.selection.bayes.RandomVariable;
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.util.BayesianDomainProfileReader;
import edu.usc.softarch.disco.util.DCPReader;
import edu.usc.softarch.disco.util.DistributionScenarioReader;
import edu.usc.softarch.disco.util.ValRangeReader;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A connector selector that uses Discrete Bayesian inference on the underlying
 * connector metadata to determine the most appropriate ranked set of connectors
 * for a particular distribution scenario.
 * </p>.
 */
public class BayesianSelector implements Selector{

  /* our log stream */
  private static final Logger LOG = Logger.getLogger(BayesianSelector.class
      .getName());

  /* the variable name for connector selection */
  private static final String CONNECTOR_VAR = "c";

  /* our bayesian inferencer */
  private static final BayesianInferencer inferencer = new BayesianInferencer();

  /* our conditional probability table */
  private static ConditionalProbabilityTable cProbTab = null;

  /* our value range for our dimensions */
  private static Map valRange = null;

  public BayesianSelector(){}
  
  public BayesianSelector(String domainProfileFile, String valRangeFile) {
    cProbTab = BayesianDomainProfileReader.readProfile(new File(
        domainProfileFile));
    valRange = ValRangeReader.readValRangeFile(new File(valRangeFile));
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.usc.softarch.disco.selection.Selector#selectConnectors(edu.usc.softarch.disco.
   *      structs.DistributionScenario, java.util.List)
   */
  public List selectConnectors(DistributionScenario scenario, List dcpProfiles) {
    // The distribution scenario tells us what evidence is present
    // right now we'll focus on a few pieces:
    // TotalVolume, and NumUsers and DeliverySchedule.VolumePerInterval,
    // DeliverySchedule.NumIntervals

    // for each "fact" present in the distribution scenario, we need to generate
    // a list of "observed" random variables

    List observedVars = BayesUtil.getObservationsFromDistributionScenario(scenario);

    // for each observed random variable, we refine our original probability
    // distribution, which ranks all connectors equally
    ProbabilityDistribution prior = getInitialProbabilities(dcpProfiles);

    LOG.log(Level.FINE, "Initial Connector ranks: " + prior.getStatements());

    ProbabilityDistribution posterior = applyBayesBasedOnConnectorMetadata(
        dcpProfiles, observedVars, cProbTab);

    // now normalize the probs
    BayesUtil.normalizeProbabilityDistribution(posterior);

    return toRankList(posterior);
  }

  private ProbabilityDistribution applyBayesBasedOnConnectorMetadata(
      List dcpProfiles, List observedVars,
      ConditionalProbabilityTable condProbTable) {

    // the goal here is to loop through the connector profiles
    // for each profile
    // apply bayes theorem to the profile metadata

    ProbabilityDistribution posterior = null;
    if (dcpProfiles != null && dcpProfiles.size() > 0) {
      posterior = new ProbabilityDistribution();
      posterior.setVarName(CONNECTOR_VAR);
      for (Iterator i = dcpProfiles.iterator(); i.hasNext();) {
        DistributionConnectorProfile profile = (DistributionConnectorProfile) i
            .next();
        double prob = getConnectorProbability(profile, observedVars,
            condProbTable);
        posterior.getStatements().add(
            new DiscreteStatement(CONNECTOR_VAR, profile.getConnectorName(),
                prob));
      }

    }

    return posterior;
  }

  private double getConnectorProbability(DistributionConnectorProfile profile,
      List observedVars, ConditionalProbabilityTable condProbTable) {
    // okay the goal here is the following
    // for each attribute in the connector metadata
    // across all 4 atomic connector metadata
    // form a probability distribution akin to
    // P(attribute=profile.getAttrVal()) = prior distribution val
    // then apply bayes theorem based on the observed Vars

    // once bayes theorem has been applied on all observedVars for
    // the particular metadata attribute, multiply that final value for that
    // attribute
    // by the prior final value for the prior attribute
    // repeat this process for all attributes in the connector metadata
    // the final probability is the prob for this connector

    double dataAccessProb = getDataAccessProb(profile, observedVars,
        condProbTable);
    double streamProb = getStreamProb(profile, observedVars, condProbTable);
    double distributorProb = getDistributorProb(profile, observedVars,
        condProbTable);

    LOG.log(Level.FINER, "Connector: "+profile.getConnectorName()+": " +
    "DA Prob: "+dataAccessProb+": stream Prob: "+streamProb+": distributorProb: "+distributorProb);

    return dataAccessProb * streamProb * distributorProb;

  }

  private double getDistributorProb(DistributionConnectorProfile profile,
      List observedVars, ConditionalProbabilityTable condProbTable) {
    DistributorConnectorProfile distProf = profile.getDistributorProfile();

    double distProb = 1.0d;

    // do the probability distribution for distributor_routing_membership
    ProbabilityDistribution probDistRoutingMembership = applyBayesTheoremToAttribute(
        "distributor_routing_membership", observedVars, condProbTable,
        getValueRange("distributor_routing_membership"));

    distProb *= BayesUtil
        .getStatement(probDistRoutingMembership.getStatements(),
            "distributor_routing_membership",
            distProf.getRouting().getMembership()).getProbability();

    // do the probability distribution for distributor_naming_type
    ProbabilityDistribution probDistNamingType = applyBayesTheoremToAttribute(
        "distributor_naming_type", observedVars, condProbTable, 
        getValueRange("distributor_naming_type"));

    distProb *= BayesUtil.getStatement(probDistNamingType.getStatements(),
        "distributor_naming_type", distProf.getNaming().getType())
        .getProbability();

    // do the probability distribution for distributor_routing_type
    ProbabilityDistribution probDistRoutingType = applyBayesTheoremToAttribute(
        "distributor_routing_type", observedVars, condProbTable, 
        getValueRange("distributor_routing_type"));
    distProb *= BayesUtil.getStatement(probDistRoutingType.getStatements(),
        "distributor_routing_type", distProf.getRouting().getType())
        .getProbability();
    
    ProbabilityDistribution probDistNamingStructure = applyBayesTheoremToAttribute(
        "distributor_naming_structures", observedVars, condProbTable,
        getValueRange("distributor_naming_structures"));
    distProb *= BayesUtil.getMaxStatement("distributor_naming_structures", 
        distProf.getNaming().getStructures(), probDistNamingStructure.getStatements())
        .getProbability();

    ProbabilityDistribution probDistDeliverySemantics = applyBayesTheoremToAttribute(
        "distributor_delivery_semantics", observedVars, condProbTable,
        getValueRange("distributor_delivery_semantics"));
    distProb *= BayesUtil.getMaxStatement("distributor_delivery_semantics",
        distProf.getDelivery().getSemantics(), probDistDeliverySemantics.getStatements())
        .getProbability();
    
    ProbabilityDistribution probDistDeliveryMechanism = applyBayesTheoremToAttribute(
        "distributor_delivery_mechanisms", observedVars, condProbTable,
        getValueRange("distributor_delivery_mechanisms"));
    
    distProb *= BayesUtil.getMaxStatement("distributor_delivery_mechanisms", 
        distProf.getDelivery().getMechanisms(), probDistDeliveryMechanism.getStatements())
        .getProbability();
    
    

    return distProb;
  }

  private double getStreamProb(DistributionConnectorProfile profile,
      List observedVars, ConditionalProbabilityTable condProbTable) {
    StreamConnectorProfile streamProf = profile.getStreamProfile();

    double streamProb = 1.0d;

    // do the probability distribution for stream_bounds
    ProbabilityDistribution probStreamBounds = applyBayesTheoremToAttribute(
        "stream_bounds", observedVars, condProbTable,
        getValueRange("stream_bounds"));
    streamProb *= BayesUtil.getStatement(probStreamBounds.getStatements(),
        "stream_bounds", streamProf.getBounds()).getProbability();

    // do the probability distribution for stream_cardinality_senders
    ProbabilityDistribution probStreamCardinalitySenders = applyBayesTheoremToAttribute(
        "stream_cardinality_senders", observedVars, condProbTable,
        getValueRange("stream_cardinality_senders"));
    streamProb *= BayesUtil.getStatement(
        probStreamCardinalitySenders.getStatements(),
        "stream_cardinality_senders",
        streamProf.getCardinality().getSenderCardinality()).getProbability();

    // do the probability distribution for stream_buffering
    ProbabilityDistribution probStreamBuffering = applyBayesTheoremToAttribute(
        "stream_buffering", observedVars,
        condProbTable, getValueRange("stream_buffering"));
    streamProb *= BayesUtil.getStatement(probStreamBuffering.getStatements(),
        "stream_buffering", streamProf.getBuffering()).getProbability();

    // do the probability distribution for stream_state
    ProbabilityDistribution probStreamState = applyBayesTheoremToAttribute(
        "stream_state", observedVars, condProbTable,
        getValueRange("stream_state"));
    streamProb *= BayesUtil.getStatement(probStreamState.getStatements(),
        "stream_state", streamProf.getState()).getProbability();

    // do the probability distribution for stream_cardinality_receivers
    ProbabilityDistribution probStreamCardinalityRecv = applyBayesTheoremToAttribute(
        "stream_cardinality_receivers", observedVars, condProbTable,
        getValueRange("stream_cardinality_receivers"));
    streamProb *= BayesUtil.getStatement(
        probStreamCardinalityRecv.getStatements(),
        "stream_cardinality_receivers",
        streamProf.getCardinality().getReceiverCardinality()).getProbability();

    // do the probability distribution for stream_synchronicity
    ProbabilityDistribution probStreamSynchronicity = applyBayesTheoremToAttribute(
        "stream_synchronicity", observedVars,
        condProbTable, getValueRange("stream_synchronicity"));
    streamProb *= BayesUtil.getStatement(
        probStreamSynchronicity.getStatements(), "stream_synchronicity",
        streamProf.getSynchronicity()).getProbability();
    
    ProbabilityDistribution probStreamDeliveries = applyBayesTheoremToAttribute(
        "stream_deliveries", observedVars,
        condProbTable, getValueRange("stream_deliveries"));
    streamProb *= BayesUtil.getMaxStatement("stream_deliveries", 
        streamProf.getDeliveries(), probStreamDeliveries.getStatements()).getProbability();
    
    ProbabilityDistribution probStreamLocalities = applyBayesTheoremToAttribute(
        "stream_localities", observedVars,
        condProbTable, getValueRange("stream_localities"));
    streamProb *= BayesUtil.getMaxStatement("stream_localities",
        streamProf.getLocalities(), probStreamLocalities.getStatements()).getProbability();
    
    
    ProbabilityDistribution probStreamFormats = applyBayesTheoremToAttribute(
        "stream_formats", observedVars,
        condProbTable, getValueRange("stream_formats"));
    streamProb *= BayesUtil.getMaxStatement("stream_formats",
        streamProf.getFormats(), probStreamFormats.getStatements()).getProbability();
    
    return streamProb;
  }

  private double getDataAccessProb(DistributionConnectorProfile profile,
      List observedVars, ConditionalProbabilityTable condProbTable) {

    DataAccessConnectorProfile dataAccessProf = profile.getDataAccessProfile();

    double dataAccessProb = 1.0d;
    // do the probability distribution for data_access_locality
    ProbabilityDistribution probDataAccesLocality = applyBayesTheoremToAttribute(
        "data_access_locality", observedVars,
        condProbTable, getValueRange("data_access_locality"));
    dataAccessProb *= BayesUtil.getStatement(
        probDataAccesLocality.getStatements(), "data_access_locality",
        dataAccessProf.getLocality()).getProbability();

    // do the probability distribution for data_access_transient_availability
    ProbabilityDistribution probDataAccessTransientAvailability = applyBayesTheoremToAttribute(
        "data_access_transient_availability", observedVars,
        condProbTable, getValueRange("data_access_transient_availability"));
    dataAccessProb *= BayesUtil.getStatement(
        probDataAccessTransientAvailability.getStatements(),
        "data_access_transient_availability",
        dataAccessProf.getAvailabilities().getAvailTransient())
        .getProbability();

    // do the probability distribution for data_access_cardinality_receivers
    ProbabilityDistribution probDataAccessCardinalityReceivers = applyBayesTheoremToAttribute(
        "data_access_cardinality_receivers", observedVars, condProbTable,
        getValueRange("data_access_cardinality_receivers"));
    dataAccessProb *= BayesUtil.getStatement(
        probDataAccessCardinalityReceivers.getStatements(),
        "data_access_cardinality_receivers",
        dataAccessProf.getCardinality().getReceiverCardinality())
        .getProbability();

    ProbabilityDistribution probDataAccessCardinalitySenders = applyBayesTheoremToAttribute(
        "data_access_cardinality_senders", observedVars, condProbTable,
        getValueRange("data_access_cardinality_senders"));
    dataAccessProb *= BayesUtil.getStatement(
        probDataAccessCardinalitySenders.getStatements(),
        "data_access_cardinality_senders",
        dataAccessProf.getCardinality().getSenderCardinality())
        .getProbability();
    
    ProbabilityDistribution probDataAccessAccesses = applyBayesTheoremToAttribute(
        "data_access_accesses", observedVars, condProbTable,
        getValueRange("data_access_accesses"));
    dataAccessProb *= BayesUtil.getMaxStatement("data_access_accesses", 
        dataAccessProf.getAccesses(), probDataAccessAccesses.getStatements())
        .getProbability();
    
    ProbabilityDistribution probDataAccessTransPersist = applyBayesTheoremToAttribute(
        "data_access_persistance", observedVars, condProbTable,
        getValueRange("data_access_persistance"));
    dataAccessProb *= BayesUtil.getMaxStatement("data_access_persistance",
        dataAccessProf.getAvailabilities().getPersistences(), probDataAccessTransPersist
        .getStatements()).getProbability();

    return dataAccessProb;
  }

  private ProbabilityDistribution applyBayesTheoremToAttribute(String attrName,
      List observedVars, ConditionalProbabilityTable condProbTable, List valRange) {
    ProbabilityDistribution prior = new ProbabilityDistribution();
    prior.setVarName(attrName);

    // how many values are there?
    int totalVals = valRange.size();

    // well se say that all other choices including our choice are equally
    // probable

    double priorProb = (1.0) / (totalVals * 1.0);

    for (Iterator i = valRange.iterator(); i.hasNext();) {
      String val = (String) i.next();
      prior.getStatements()
          .add(new DiscreteStatement(attrName, val, priorProb));
    }

    LOG.log(Level.FINE, "Iteratively applying Bayes Theorem to connector "
        + "attr: [" + attrName + "]: Initial prob: " + prior);

    if (observedVars != null && observedVars.size() > 0) {
      for (Iterator i = observedVars.iterator(); i.hasNext();) {
        RandomVariable obsVar = (RandomVariable) i.next();

        LOG
            .log(Level.FINE, "Applying Bayes theorem: obs var: [" + obsVar
                + "]");
        prior = inferencer.applyBayesTheorem(prior, obsVar, condProbTable);
        LOG.log(Level.FINE, "Posterior dist for [" + attrName + "]: " + prior);
      }

    }

    return prior;
  }

  private List getValueRange(String attrName) {
    return (List) valRange.get(attrName);
  }

  public static void main(String[] args) throws Exception {
    String dcpDir = null, domainProfFilePath = null, valRangeFilePath = null, scenarioFile = null;
    String usage = "BayesianSelector --dcpDir <path> --domainProf <path> "
        + "--valRangeFile <path> --scenario <path>\n";

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--dcpDir")) {
        dcpDir = args[++i];
      } else if (args[i].equals("--domainProf")) {
        domainProfFilePath = args[++i];
      } else if (args[i].equals("--valRangeFile")) {
        valRangeFilePath = args[++i];
      } else if (args[i].equals("--scenario")) {
        scenarioFile = args[++i];
      }
    }

    if (dcpDir == null || domainProfFilePath == null
        || valRangeFilePath == null || scenarioFile == null) {
      System.err.println(usage);
      System.exit(1);
    }

    BayesianSelector selector = new BayesianSelector(domainProfFilePath,
        valRangeFilePath);
    DistributionScenario scenario = DistributionScenarioReader
        .readScenario(new File(scenarioFile));

    List ranks = selector.selectConnectors(scenario, DCPReader
        .parseDCPs(new File(dcpDir)));

    LOG.log(Level.INFO, "Found: [" + ranks.size()
        + "] connectors: displaying in order of preference");

    for (Iterator i = ranks.iterator(); i.hasNext();) {
      ConnectorRank rank = (ConnectorRank) i.next();
      LOG.log(Level.INFO, "Connector: [" + rank.getConnectorName()
          + "]: Rank: [" + rank.getRank() + "]");
    }
  }

  private List toRankList(ProbabilityDistribution posterior) {
    List rankList = new Vector();
    if (posterior != null) {
      for (Iterator i = posterior.getStatements().iterator(); i.hasNext();) {
        DiscreteStatement statement = (DiscreteStatement) i.next();
        ConnectorRank rank = new ConnectorRank(statement.getVariable()
            .getValue(), statement.getProbability());
        rankList.add(rank);
      }
    }

    rankConnectorList(rankList);

    return rankList;
  }

  private ProbabilityDistribution getInitialProbabilities(List dcpProfiles) {
    ProbabilityDistribution dist = new ProbabilityDistribution();
    dist.setVarName(CONNECTOR_VAR);

    if (dcpProfiles != null && dcpProfiles.size() > 0) {
      double initProb = 100.0 / (double) dcpProfiles.size();

      for (Iterator i = dcpProfiles.iterator(); i.hasNext();) {
        DistributionConnectorProfile profile = (DistributionConnectorProfile) i
            .next();
        dist.getStatements().add(
            new DiscreteStatement(CONNECTOR_VAR, profile.getConnectorName(),
                initProb));
      }
    }

    return dist;
  }

  private void rankConnectorList(List connectors) {
    if (connectors != null && connectors.size() > 0) {
      Collections.sort(connectors, new Comparator() {

        public int compare(Object arg0, Object arg1) {
          ConnectorRank rank1 = (ConnectorRank) arg0;
          ConnectorRank rank2 = (ConnectorRank) arg1;

          if (rank1.getRank() > rank2.getRank()) {
            return -1;
          } else if (rank1.getRank() == rank2.getRank()) {
            return 0;
          } else
            return 1;
        }

      });
    }
  }

  /* (non-Javadoc)
   * @see edu.usc.softarch.disco.util.Configurable#configure(java.util.Properties)
   */
  public void configure(Properties props) {
    // need the domainProfPath and the valRangePath
    String domainProfPath = props.getProperty("domainProfile.path");
    String valRangePath = props.getProperty("valRange.path");
    
    if(domainProfPath != null && valRangePath != null){
      this.valRange = ValRangeReader.readValRangeFile(new File(valRangePath));
      this.cProbTab = BayesianDomainProfileReader.readProfile(new File(domainProfPath));      
    }
    
  }

}
