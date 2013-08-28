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
import edu.usc.softarch.disco.selection.scorebased.ScoreFunctionRepository;
import edu.usc.softarch.disco.selection.scorebased.ObjectiveFunction;
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.util.DCPReader;
import edu.usc.softarch.disco.util.DistributionScenarioReader;
import edu.usc.softarch.disco.util.ObjectiveFunctionReader;
import edu.usc.softarch.disco.util.ScoreFunctionReader;
import edu.usc.softarch.disco.util.ValRangeReader;

/**
 * @author rezam
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A connector selector that uses Score-based functional addition and
 * multiplication on the underlying connector metadata to determine the most
 * appropriate ranked set of connectors for a particular distribution scenario.
 * </p>.
 */
public class ScoreBasedSelector implements Selector{

  /* our log stream */
  private static final Logger LOG = Logger.getLogger(ScoreBasedSelector.class
      .getName());

  /* our value range for our dimensions */
  private static Map valRange = null;

  /* score function manager object */
  private static ScoreFunctionRepository scoreFunctionRepo = null;

  /* objective function object */
  private ObjectiveFunction objectiveFunction = null;

  public ScoreBasedSelector(){}
  
  public ScoreBasedSelector(String scoreFunctionDirPath, String valRangeFile) {
    scoreFunctionRepo = ScoreFunctionReader.parseScoreFunctions(new File(
        scoreFunctionDirPath));
    valRange = ValRangeReader.readValRangeFile(new File(valRangeFile));
  }

  public void setObjectiveFunction(ObjectiveFunction inObjectiveFunction) {
    objectiveFunction = inObjectiveFunction;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.usc.softarch.disco.selection.Selector#selectConnectors(edu.usc.softarch.disco.
   *      structs.DistributionScenario, java.util.List)
   */
  public List selectConnectors(DistributionScenario scenario, List dcpProfiles) {

    HashMap<String, ConnectorPerformanceScore> connectorPerformanceScoreMap = new HashMap<String, ConnectorPerformanceScore>();
    /* populate connectorRanks map with all available connectors */
    for (Iterator i = dcpProfiles.iterator(); i.hasNext();) {
      DistributionConnectorProfile dcp = (DistributionConnectorProfile) i
          .next();
      ConnectorPerformanceScore connectorPerformance = new ConnectorPerformanceScore(
          dcp.getConnectorName());
      connectorPerformanceScoreMap.put(dcp.getConnectorName(),
          connectorPerformance);
    }

    /*
     * iterate over score functions and update connector performance objects in
     * the map
     */
    scoreFunctionRepo.updatePerformanceScoreMap(connectorPerformanceScoreMap,
        scenario);
    
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
      this.objectiveFunction = new ObjectiveFunction(consistency, efficiency,
          dependability, scalability);

    }

    List connectorRanks = toRankList(connectorPerformanceScoreMap);
    return normalizeRanks(connectorRanks);
  }

  public static void main(String[] args) throws Exception {
    String dcpDir = null, scoreFunctionDirPath = null, valRangeFilePath = null, scenarioFile = null, objectiveFile = null;
    String usage = "ScoreBasedSelector --dcpDir <path> --scoreFunctionDir <path> "
        + "--valRangeFile <path> --scenario <path> --objective <path>\n";

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--dcpDir")) {
        dcpDir = args[++i];
      } else if (args[i].equals("--scoreFunctionDir")) {
        scoreFunctionDirPath = args[++i];
      } else if (args[i].equals("--valRangeFile")) {
        valRangeFilePath = args[++i];
      } else if (args[i].equals("--scenario")) {
        scenarioFile = args[++i];
      } else if (args[i].equals("--objective")) {
        objectiveFile = args[++i];
      }
    }

    if (dcpDir == null || scoreFunctionDirPath == null
        || valRangeFilePath == null || scenarioFile == null
        || objectiveFile == null) {
      System.err.println(usage);
      System.exit(1);
    }

    ScoreBasedSelector selector = new ScoreBasedSelector(scoreFunctionDirPath,
        valRangeFilePath);
    DistributionScenario scenario = DistributionScenarioReader
        .readScenario(new File(scenarioFile));
    ObjectiveFunction objective = ObjectiveFunctionReader
        .readObjectiveFunction(new File(objectiveFile));

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

  private List toRankList(
      HashMap<String, ConnectorPerformanceScore> connectorPerformanceScoreMap) {
    List rankList = new Vector();
    if (connectorPerformanceScoreMap != null) {
      for (Iterator i = connectorPerformanceScoreMap.entrySet().iterator(); i
          .hasNext();) {
        ConnectorPerformanceScore connectorPerformance = (ConnectorPerformanceScore) ((Map.Entry) i
            .next()).getValue();
        ConnectorRank rank = connectorPerformance.getRank(objectiveFunction);
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
    String scoreFuncPath = props.getProperty("scoreFunction.dir.path");
    String valRangePath = props.getProperty("valRange.path");

    if (objectiveFuncPath != null && scoreFuncPath != null
        && valRangePath != null) {
      this.objectiveFunction = ObjectiveFunctionReader
          .readObjectiveFunction(new File(objectiveFuncPath));
      this.valRange = ValRangeReader.readValRangeFile(new File(valRangePath));
      this.scoreFunctionRepo = ScoreFunctionReader
          .parseScoreFunctions(new File(scoreFuncPath));
    }

  }
  
  private List normalizeRanks(List ranks) {
    if (ranks == null || (ranks != null && ranks.size() == 0)) {
      return null;
    }
    List newRanks = new Vector(ranks.size());

    double lowNum = findLow(ranks);
    
    if(lowNum < 0d){
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

}
