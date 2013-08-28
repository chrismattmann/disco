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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

// DISCO imports
import edu.usc.softarch.disco.connector.dcp.DistributionConnectorProfile;
import edu.usc.softarch.disco.selection.optimizing.CompetencyFunction;
import edu.usc.softarch.disco.selection.optimizing.CompetencyFunctionRepository;
import edu.usc.softarch.disco.selection.optimizing.OptimizingObjectiveFunction;
import edu.usc.softarch.disco.selection.optimizing.ConnectorPerformanceIndex;
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.util.DCPReader;
import edu.usc.softarch.disco.util.DistributionScenarioReader;
import edu.usc.softarch.disco.util.OptimizingObjectiveFunctionReader;
import edu.usc.softarch.disco.util.CompetencyFunctionReader;
import edu.usc.softarch.disco.util.ValRangeReader;

/**
 * @author rezam
 * @version $Revision$
 * 
 * <p>
 * A connector selector using nonlinear optimization
 * </p>.
 */
public class OptimizingSelector implements Selector {

  /* our log stream */
  private static final Logger LOG = Logger.getLogger(OptimizingSelector.class
      .getName());

  /* our value range for our dimensions */
  private static Map valRange = null;

  /* competency function manager object */
  private static CompetencyFunctionRepository competencyFunctionRepo = null;

  /* objective function object */
  private OptimizingObjectiveFunction objectiveFunction = null;

  public OptimizingSelector() {
  }

  public OptimizingSelector(String competencyFunctionDirPath,
      String valRangeFile) {
    competencyFunctionRepo = CompetencyFunctionReader
        .parseCompetencyFunctions(new File(competencyFunctionDirPath));
    valRange = ValRangeReader.readValRangeFile(new File(valRangeFile));
  }

  public void setObjectiveFunction(
      OptimizingObjectiveFunction inObjectiveFunction) {
    objectiveFunction = inObjectiveFunction;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.usc.softarch.disco.selection.Selector#selectConnectors(edu.usc.softarch.disco.
   *      structs.DistributionScenario, java.util.List)
   */
  public List selectConnectors(DistributionScenario scenario, List dcpProfiles) {

    // if there is a fully defined objective function in the scenario,
    // override the specified objective function
    if (scenario.getPerformanceReqs() != null
        && scenario.getPerformanceReqs().getConsistency() != null
        && scenario.getPerformanceReqs().getDependability() != null
        && scenario.getPerformanceReqs().getEfficiency() != null
        && scenario.getPerformanceReqs().getScalability() != null) {
      float consistency = Float.parseFloat(scenario.getPerformanceReqs()
          .getConsistency());
      float dependability = Float.parseFloat(scenario.getPerformanceReqs()
          .getDependability());
      float efficiency = Float.parseFloat(scenario.getPerformanceReqs()
          .getEfficiency());
      float scalability = Float.parseFloat(scenario.getPerformanceReqs()
          .getScalability());

      LOG.log(Level.FINE, "Overriding objective function with: [c="
          + consistency + "," + "d=" + dependability + ",e=" + efficiency
          + ",s=" + scalability + "]");
      this.objectiveFunction = new OptimizingObjectiveFunction(consistency,
          efficiency, dependability, scalability);

    }

    HashMap<String, ConnectorPerformanceIndex> connectorPerformanceScoreMap = new HashMap<String, ConnectorPerformanceIndex>();
    for (Iterator i = dcpProfiles.iterator(); i.hasNext();) {
      DistributionConnectorProfile dcp = (DistributionConnectorProfile) i
          .next();
      ConnectorPerformanceIndex connectorPerformance = optimizeConnectorPerformance(
          dcp, competencyFunctionRepo, scenario, objectiveFunction);
      connectorPerformanceScoreMap.put(dcp.getConnectorName(),
          connectorPerformance);
    }

    List connectorRanks = toRankList(connectorPerformanceScoreMap);
    LOG.log(Level.FINER,"Connector ranks before normalization: "
        + connectorRanks);
    return normalizeRanks(connectorRanks);
  }

  private static ConnectorPerformanceIndex optimizeConnectorPerformance(
      DistributionConnectorProfile dcp, CompetencyFunctionRepository repo,
      DistributionScenario scenario,
      OptimizingObjectiveFunction objectiveFunction) {

    List<String> freeVars = new ArrayList<String>();

    PrecisionPerformanceReqs reqs = new OptimizingSelector().new PrecisionPerformanceReqs(
        1.0f, 1.0f, 1.0f, 1.0f);
    multiplyByCompetencyFunc(repo, dcp, CompetencyFunction.VAR_TOTAL_VOLUME,
        CompetencyFunction.parseVolume(scenario.getTotalVolume().getValue()),
        freeVars, scenario, reqs);

    multiplyByCompetencyFunc(repo, dcp,
        CompetencyFunction.VAR_NUMBER_OF_INTERVALS, String.valueOf(scenario
            .getDelivSchedule().getNumberOfIntervals().getValue()), freeVars,
        scenario, reqs);

    multiplyByCompetencyFunc(repo, dcp,
        CompetencyFunction.VAR_VOLUME_PER_INTERVAL, CompetencyFunction
            .parseVolume(scenario.getDelivSchedule().getVolumePerInterval()
                .getValue()), freeVars, scenario, reqs);

    multiplyByCompetencyFunc(repo, dcp, CompetencyFunction.VAR_NUMBER_OF_USERS,
        scenario.getNumUsers().getValue(), freeVars, scenario, reqs);

    multiplyByCompetencyFunc(repo, dcp,
        CompetencyFunction.VAR_NUMBER_OF_USER_TYPES, scenario.getNumUserTypes()
            .getValue(), freeVars, scenario, reqs);

    multiplyByCompetencyFunc(repo, dcp,
        CompetencyFunction.VAR_NUMBER_OF_DATA_TYPES, scenario.getDataTypes() != null ? 
            String.valueOf(scenario.getDataTypes().size()):null, freeVars, scenario, reqs);

    // TODO:
    // handle VAR_TIMING_OF_INTERVAL
    // handle VAR_GEOGRAPHIC_DISTRIBUTION (discrete X values)

    ConnectorPerformanceIndex connectorPerformance = null;
    if (freeVars.size() == 0) {
      float overallIndex = objectiveFunction.calculateOverallIndex(reqs
          .getConsistency(), reqs.getEfficiency(), reqs.getDependability(),
          reqs.getScalability());
      connectorPerformance = new ConnectorPerformanceIndex(dcp
          .getConnectorName(), overallIndex);
    } else {
      connectorPerformance = objectiveFunction.optimizeObjective(freeVars, dcp,
          repo, reqs.getConsistency(), reqs.getEfficiency(), reqs
              .getDependability(), reqs.getScalability());
    }
    return connectorPerformance;
  }

  public static void main(String[] args) throws Exception {
    String dcpDir = null, competencyFunctionDirPath = null, valRangeFilePath = null, scenarioFile = null, objectiveFile = null;
    String usage = "OptimizingSelector --dcpDir <path> --competencyFunctionDir <path> "
        + "--valRangeFile <path> --scenario <path> --objective <path>\n";

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--dcpDir")) {
        dcpDir = args[++i];
      } else if (args[i].equals("--competencyFunctionDir")) {
        competencyFunctionDirPath = args[++i];
      } else if (args[i].equals("--valRangeFile")) {
        valRangeFilePath = args[++i];
      } else if (args[i].equals("--scenario")) {
        scenarioFile = args[++i];
      } else if (args[i].equals("--objective")) {
        objectiveFile = args[++i];
      }
    }

    if (dcpDir == null || competencyFunctionDirPath == null
        || valRangeFilePath == null || scenarioFile == null
        || objectiveFile == null) {
      System.err.println(usage);
      System.exit(1);
    }

    OptimizingSelector selector = new OptimizingSelector(
        competencyFunctionDirPath, valRangeFilePath);
    DistributionScenario scenario = DistributionScenarioReader
        .readScenario(new File(scenarioFile));
    OptimizingObjectiveFunction objective = OptimizingObjectiveFunctionReader
        .readOptimizingObjectiveFunction(new File(objectiveFile));

    selector.setObjectiveFunction(objective);

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

  private static void multiplyByCompetencyFunc(
      CompetencyFunctionRepository repo, DistributionConnectorProfile dcp,
      String varX, String value, List<String> freeVars,
      DistributionScenario scenario, PrecisionPerformanceReqs reqs) {
    boolean hasFunctions = repo.hasFunctionsForVar(dcp, varX);
    CompetencyFunction func = null;

    if (hasFunctions) {
      if (value == null) {
        freeVars.add(varX);
      } else {
        func = repo.getCompetencyFunction(dcp, varX,
            CompetencyFunction.VAR_CONSISTENCY);
        if (func != null)
          reqs.setConsistency(reqs.getConsistency()
              * func.getValueForPoint(value));
        func = repo.getCompetencyFunction(dcp, varX,
            CompetencyFunction.VAR_DEPENDABILITY);
        if (func != null)
          reqs.setDependability(reqs.getDependability()
              * func.getValueForPoint(value));
        func = repo.getCompetencyFunction(dcp, varX,
            CompetencyFunction.VAR_EFFICIENCY);
        if (func != null)
          reqs.setEfficiency(reqs.getEfficiency()
              * func.getValueForPoint(value));
        func = repo.getCompetencyFunction(dcp, varX,
            CompetencyFunction.VAR_SCALABILITY);
        if (func != null)
          reqs.setScalability(reqs.getScalability()
              * func.getValueForPoint(value));
        
        LOG.log(Level.FINE, "Update: DCP: ["
          + dcp.getConnectorName() + "]: varX: [" + varX + "], varY: [" + value
          + "]: "+reqs);
      }
    } else {
      LOG.log(Level.FINE, "No functions present for: DCP: ["
          + dcp.getConnectorName() + "]: varX: [" + varX + "], varY: [" + value
          + "]");
    }

  }

  private List toRankList(
      HashMap<String, ConnectorPerformanceIndex> connectorPerformanceScoreMap) {
    List rankList = new Vector();
    if (connectorPerformanceScoreMap != null) {
      for (Iterator i = connectorPerformanceScoreMap.entrySet().iterator(); i
          .hasNext();) {
        ConnectorPerformanceIndex connectorPerformance = (ConnectorPerformanceIndex) ((Map.Entry) i
            .next()).getValue();
        ConnectorRank rank = connectorPerformance.getRank();
        rankList.add(rank);
      }
    }

    rankConnectorList(rankList);
    return rankList;
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

  /*
   * (non-Javadoc)
   * 
   * @see edu.usc.softarch.disco.util.Configurable#configure(java.util.Properties)
   */
  public void configure(Properties props) {
    // need objective function path
    // need score function dir path
    // need valRangePath
    String objectiveFuncPath = props.getProperty("objectiveFunction.path");
    String competencyFuncPath = props.getProperty("optimizing.dir.path");
    String valRangePath = props.getProperty("valRange.path");

    if (objectiveFuncPath != null && competencyFuncPath != null
        && valRangePath != null) {
      this.objectiveFunction = OptimizingObjectiveFunctionReader
          .readOptimizingObjectiveFunction(new File(objectiveFuncPath));
      this.valRange = ValRangeReader.readValRangeFile(new File(valRangePath));
      this.competencyFunctionRepo = CompetencyFunctionReader
          .parseCompetencyFunctions(new File(competencyFuncPath));
    }

  }

  private List normalizeRanks(List ranks) {
    if (ranks == null || (ranks != null && ranks.size() == 0)) {
      return null;
    }
    List newRanks = new Vector(ranks.size());

    double lowNum = findLow(ranks);

    if (lowNum < 0d) {
      shiftPositive(ranks, Math.abs(lowNum));
    }
    double denom = sumExistingRanks(ranks);

    for (Iterator i = ranks.iterator(); i.hasNext();) {
      ConnectorRank rank = (ConnectorRank) i.next();
      ConnectorRank newRank = new ConnectorRank();
      newRank.setConnectorName(rank.getConnectorName());
      double newRankVal = rank.getRank() / denom;
      newRank.setRank(newRankVal);
      newRanks.add(newRank);
    }

    return newRanks;
  }

  private void shiftPositive(List ranks, double increment) {
    if (ranks != null && ranks.size() > 0) {
      for (Iterator i = ranks.iterator(); i.hasNext();) {
        ConnectorRank rank = (ConnectorRank) i.next();
        rank.setRank(rank.getRank() + increment);
      }
    }
  }

  private double findLow(List ranks) {
    if (ranks == null || (ranks != null && ranks.size() == 0)) {
      return Double.MAX_VALUE;
    }

    double min = Double.MAX_VALUE;

    for (Iterator i = ranks.iterator(); i.hasNext();) {
      ConnectorRank rank = (ConnectorRank) i.next();
      if (rank.getRank() < min) {
        min = rank.getRank();
      }
    }

    return min;
  }

  private double sumExistingRanks(List ranks) {
    double sum = 0.0d;

    if (ranks != null && ranks.size() > 0) {
      for (Iterator i = ranks.iterator(); i.hasNext();) {
        ConnectorRank rank = (ConnectorRank) i.next();
        sum += rank.getRank();
      }
    }

    return sum;
  }

  class PrecisionPerformanceReqs {
    private float consistency;

    private float dependability;

    private float scalability;

    private float efficiency;

    /**
     * @param consistency
     * @param dependability
     * @param scalability
     * @param efficiency
     */
    public PrecisionPerformanceReqs(float consistency, float dependability,
        float scalability, float efficiency) {
      super();
      this.consistency = consistency;
      this.dependability = dependability;
      this.scalability = scalability;
      this.efficiency = efficiency;
    }

    /**
     * @return the consistency
     */
    public float getConsistency() {
      return consistency;
    }

    /**
     * @param consistency
     *          the consistency to set
     */
    public void setConsistency(float consistency) {
      this.consistency = consistency;
    }

    /**
     * @return the dependability
     */
    public float getDependability() {
      return dependability;
    }

    /**
     * @param dependability
     *          the dependability to set
     */
    public void setDependability(float dependability) {
      this.dependability = dependability;
    }

    /**
     * @return the efficiency
     */
    public float getEfficiency() {
      return efficiency;
    }

    /**
     * @param efficiency
     *          the efficiency to set
     */
    public void setEfficiency(float efficiency) {
      this.efficiency = efficiency;
    }

    /**
     * @return the scalability
     */
    public float getScalability() {
      return scalability;
    }

    /**
     * @param scalability
     *          the scalability to set
     */
    public void setScalability(float scalability) {
      this.scalability = scalability;
    }

    public String toString() {
      return "consistency=" + this.consistency + ",scalability="
          + this.scalability + ",efficiency=" + this.efficiency
          + ",dependability=" + this.dependability;
    }

  }

}
