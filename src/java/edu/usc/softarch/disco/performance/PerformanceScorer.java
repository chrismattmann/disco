/* Copyright (c) 2007 University of Southern California.
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

package edu.usc.softarch.disco.performance;

// JDK imports
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.structs.ScenarioValue;
import edu.usc.softarch.disco.util.DistributionScenarioReader;
import edu.usc.softarch.disco.util.PerformanceProfileReader;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A utility class for computing performance scores for distribution connector
 * {@link PerformanceProfile}s.
 * </p>.
 */
public final class PerformanceScorer {

  private List<PerformanceProfile> kb;

  /* our log stream */
  private static final Logger LOG = Logger.getLogger(PerformanceScorer.class
      .getName());

  public PerformanceScorer(List<PerformanceProfile> performanceKb) {
    this.kb = performanceKb;
  }

  /**
   * Computes an efficiency score for the associated connector, and
   * {@link DistributionScenario} in which the raw data was gathered. Implements
   * the equation <code>e(scenario)</code> on pg. 80 of Mattmann's thesis.
   * 
   * @param s
   *          The {@link DistributionScenario} to compute effiency for.
   * @param connectorName
   *          The name of the DistributionConnector whose
   *          {@link PerformanceProfile} will be examined.
   * 
   * @return The value of <code>e(scenario)</code>, in other words, the
   *         efficiency score for the connector for this particular scenario.
   */
  public double computeEfficiency(DistributionScenario s, String connectorName) {
    double finalScore = computeUnNormalizedEfficiency(s, connectorName);
    LOG.log(Level.FINE, "Unnormalized eff score: [" + connectorName
        + "]: Score: " + "[" + finalScore + "]: Scenario: [" + s.getName()
        + "]");
    // normalize score
    double maxEffScore = computeMaxEfficiencyScore(connectorName);
    LOG.log(Level.FINE, "max eff: [" + maxEffScore + "]");
    finalScore += (10.0 - maxEffScore);

    return finalScore;
  }

  /**
   * Computes an consistency score for the associated connector, and
   * {@link DistributionScenario} in which the raw data was gathered. Implements
   * the equation <code>c(scenario)</code> on pg. 80 of Mattmann's thesis.
   * 
   * @param s
   *          The {@link DistributionScenario} to compute effiency for.
   * @param connectorName
   *          The name of the DistributionConnector whose
   *          {@link PerformanceProfile} will be examined.
   * 
   * @return The value of <code>c(scenario)</code>, in other words, the
   *         consistency score for the connector for this particular scenario.
   */
  public double computeConsistency(DistributionScenario s, String connectorName) {
    double finalScore = computeUnNormalizedConsistency(s, connectorName);
    LOG.log(Level.FINE, "Unnormalized cons score: [" + connectorName
        + "]: Score: [" + finalScore + "]: Scenario: [" + s.getName() + "]");
    // normalize score
    double maxConsScore = computeMaxConsistencyScore(connectorName);
    LOG.log(Level.FINE, "max cons: [" + maxConsScore + "]");
    finalScore += (10.0 - maxConsScore);

    return finalScore;
  }

  /**
   * 
   * @param s
   * @param connectorName
   * @return
   */
  public double computeDependability(DistributionScenario s,
      String connectorName) {
    double finalScore = computeUnNormalizedDependability(s, connectorName);
    LOG.log(Level.FINE, "Unnormalized dep score: [" + connectorName
        + "]: Score: [" + finalScore + "]: Scenario: [" + s.getName() + "]");

    // normalize score
    double maxDepScore = computeMaxDependabilityScore(connectorName);
    LOG.log(Level.FINE, "max dep: [" + maxDepScore + "]");
    finalScore += (10.0 - maxDepScore);

    return finalScore;
  }

  /**
   * 
   * @param s
   * @param connectorName
   * @return
   */
  public double computeScalability(DistributionScenario s, String connectorName) {
    double finalScore = computeUnNormalizedScalability(s, connectorName);
    LOG.log(Level.FINE, "Unnormalized scal score: [" + connectorName
        + "]: Score: [" + finalScore + "]: Scenario: [" + s.getName() + "]");

    // normalize score
    double maxScalScore = computeMaxScalabilityScore(connectorName);
    LOG.log(Level.FINE, "max scal: [" + maxScalScore + "]");
    finalScore += (10.0 - maxScalScore);

    return finalScore;
  }

  private double computeUnNormalizedScalability(DistributionScenario s,
      String connectorName) {
    PerformanceProfile prof = getConnectorPerformanceProfile(connectorName);
    RawPerformanceData data = getRawPerformanceData(prof, s);
    if (data == null)
      return getAvgScalability(connectorName);

    double totalTotalVolume = getTotalScenarioTotalVolume(data.getConnName());
    double totalNumUsers = getTotalScenarioNumUsers(data.getConnName());
    double totalNumUserTypes = getTotalScenarioNumUserTypes(data.getConnName());

    double totalVolumeScen = cleanValue(data.getScenario().getTotalVolume());
    double numUsersScen = cleanValue(data.getScenario().getNumUsers());
    double numUserTypesScen = cleanValue(data.getScenario().getNumUserTypes());

    double totalVolumeScore = -1.0;
    if (totalTotalVolume == 0.0) {
      totalVolumeScore = 0.0;
    } else
      totalVolumeScore = totalVolumeScen / totalTotalVolume;

    double numUsersScore = -1.0;
    if (totalNumUsers == 0.0) {
      numUsersScore = 0.0;
    } else
      numUsersScore = numUsersScen / totalNumUsers;

    double userTypesScore = -1.0;
    if (totalNumUserTypes == 0.0) {
      userTypesScore = 0.0;
    } else
      userTypesScore = numUserTypesScen / totalNumUserTypes;

    double finalScore = Math.min(1.0,
        (totalVolumeScore + numUsersScore + userTypesScore)) * 10.0;
    return finalScore;
  }

  private double computeUnNormalizedDependability(DistributionScenario s,
      String connectorName) {
    PerformanceProfile prof = getConnectorPerformanceProfile(connectorName);
    RawPerformanceData data = getRawPerformanceData(prof, s);
    if (data == null)
      return getAvgDependability(connectorName);
    int totalNumFaults = getTotalScenarioNumFaults(data.getConnName());

    double depScore = -1.0;
    if (totalNumFaults == 0) {
      depScore = 1.0;
    } else
      depScore = (1.0 - ((data.getConnNumFaults() * 1.0) / (1.0 * totalNumFaults)));

    double finalScore = depScore * 10.0;
    return finalScore;
  }

  private double computeUnNormalizedConsistency(DistributionScenario s,
      String connectorName) {
    PerformanceProfile prof = getConnectorPerformanceProfile(connectorName);
    RawPerformanceData data = getRawPerformanceData(prof, s);
    if (data == null)
      return getAvgConsistency(connectorName);

    double totalConsistencyBytes = getTotalScenarioConsistencyBytes(data
        .getConnName());
    LOG.log(Level.FINEST, "Conn: [" + connectorName
        + "]: Total consistency bytes: [" + totalConsistencyBytes
        + "]: scenario: [" + s.getName() + "]");

    LOG
        .log(Level.FINEST, "cons_scenario = ["
            + data.getConnBytesActualTransfer() + "/" + totalConsistencyBytes
            + "]");
    double consistencyScore = data.getConnBytesActualTransfer()
        / totalConsistencyBytes;
    double finalScore = consistencyScore * 10.0;

    return finalScore;
  }

  private double computeUnNormalizedEfficiency(DistributionScenario s,
      String connectorName) {
    PerformanceProfile prof = getConnectorPerformanceProfile(connectorName);
    RawPerformanceData data = getRawPerformanceData(prof, s);
    if (data == null)
      return getAvgEfficiency(connectorName);
    double totalScenarioBps = getTotalScenarioEfficiencyBps(data.getConnName());
    LOG.log(Level.FINEST, "bps_score=(" + data.getConnBps() + " / "
        + totalScenarioBps + ")");
    double bpsScore = data.getConnBps() / totalScenarioBps;
    LOG.log(Level.FINEST, "=[" + bpsScore + "]");

    double totalAvgMem = getTotalScenarioAvgMem(data.getConnName());
    LOG.log(Level.FINEST, "totalAvgMem_score=(1.0-(" + data.getConnAvgMem()
        + " / " + totalAvgMem + "))");
    double avgMemScore = 1.0 - (data.getConnAvgMem() / totalAvgMem);
    LOG.log(Level.FINEST, "=[" + avgMemScore + "]");

    double finalScore = Math.min(1.0, (bpsScore + avgMemScore)) * 10.0;
    return finalScore;
  }

  private double computeMaxEfficiencyScore(String connName) {
    double max = Double.MIN_VALUE;
    PerformanceProfile prof = getConnectorPerformanceProfile(connName);
    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenId = i.next();
      RawPerformanceData data = prof.getRawData().get(scenId);
      double eff = computeUnNormalizedEfficiency(data.getScenario(), connName);
      if (eff > max) {
        LOG.log(Level.FINEST, "max eff: scenario: [" + scenId + "]: value: ["
            + eff + "]");
        max = eff;
      }

    }

    return max;
  }

  private double computeMaxConsistencyScore(String connName) {
    double max = Double.MIN_VALUE;
    PerformanceProfile prof = getConnectorPerformanceProfile(connName);
    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenId = i.next();
      RawPerformanceData data = prof.getRawData().get(scenId);
      double cons = computeUnNormalizedConsistency(data.getScenario(), connName);
      if (cons > max) {
        max = cons;
      }

    }

    return max;
  }

  private double computeMaxDependabilityScore(String connName) {
    double max = Double.MIN_VALUE;
    PerformanceProfile prof = getConnectorPerformanceProfile(connName);
    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenId = i.next();
      RawPerformanceData data = prof.getRawData().get(scenId);
      double dep = computeUnNormalizedDependability(data.getScenario(),
          connName);
      if (dep > max) {
        max = dep;
      }

    }

    return max;
  }

  private double computeMaxScalabilityScore(String connName) {
    double max = Double.MIN_VALUE;
    PerformanceProfile prof = getConnectorPerformanceProfile(connName);
    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenId = i.next();
      RawPerformanceData data = prof.getRawData().get(scenId);
      double scal = computeUnNormalizedScalability(data.getScenario(), connName);
      if (scal > max) {
        max = scal;
      }

    }

    return max;
  }

  private double getTotalScenarioTotalVolume(String connName) {
    double total = 0.0d;
    PerformanceProfile prof = getConnectorPerformanceProfile(connName);

    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenarioId = i.next();
      RawPerformanceData data = prof.getRawData().get(scenarioId);
      total += cleanValue(data.getScenario().getTotalVolume());
    }

    return total;
  }

  private double getTotalScenarioNumUsers(String connName) {
    double total = 0;
    PerformanceProfile prof = getConnectorPerformanceProfile(connName);

    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenarioId = i.next();
      RawPerformanceData data = prof.getRawData().get(scenarioId);
      total += cleanValue(data.getScenario().getNumUsers());
    }

    return total;
  }

  private double getTotalScenarioNumUserTypes(String connName) {
    double total = 0;
    PerformanceProfile prof = getConnectorPerformanceProfile(connName);

    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenarioId = i.next();
      RawPerformanceData data = prof.getRawData().get(scenarioId);
      total += cleanValue(data.getScenario().getNumUserTypes());
    }

    return total;
  }

  private int getTotalScenarioNumFaults(String connName) {
    int total = 0;
    PerformanceProfile prof = getConnectorPerformanceProfile(connName);

    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenarioId = i.next();
      RawPerformanceData data = prof.getRawData().get(scenarioId);
      total += data.getConnNumFaults();
    }

    return total;
  }

  private double getTotalScenarioConsistencyBytes(String connName) {
    double total = 0.0d;

    PerformanceProfile prof = getConnectorPerformanceProfile(connName);

    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenarioId = i.next();
      RawPerformanceData data = prof.getRawData().get(scenarioId);
      total += data.getConnBytesActualTransfer();
    }

    return total;
  }

  private double getTotalScenarioAvgMem(String connName) {
    double total = 0.0d;
    PerformanceProfile prof = getConnectorPerformanceProfile(connName);

    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenarioId = i.next();
      RawPerformanceData data = prof.getRawData().get(scenarioId);
      total += data.getConnAvgMem();
    }

    return total;
  }

  private double getTotalScenarioEfficiencyBps(String connName) {
    double total = 0.0d;
    PerformanceProfile prof = getConnectorPerformanceProfile(connName);

    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenarioId = i.next();
      RawPerformanceData data = prof.getRawData().get(scenarioId);
      total += data.getConnBps();
    }

    return total;
  }

  private double getAvgDependability(String connName) {
    PerformanceProfile prof = getConnectorPerformanceProfile(connName);
    double total = 0.0;
    int numData = 0;

    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenarioId = i.next();
      DistributionScenario s = new DistributionScenario();
      s.setName(scenarioId);
      total += computeUnNormalizedDependability(s, connName);
      numData++;
    }

    return (total / (1.0 * numData));
  }

  private double getAvgConsistency(String connName) {
    PerformanceProfile prof = getConnectorPerformanceProfile(connName);
    double total = 0.0;
    int numData = 0;

    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenarioId = i.next();
      DistributionScenario s = new DistributionScenario();
      s.setName(scenarioId);
      total += computeUnNormalizedConsistency(s, connName);
      numData++;
    }

    return (total / (1.0 * numData));
  }

  private double getAvgScalability(String connName) {
    PerformanceProfile prof = getConnectorPerformanceProfile(connName);
    double total = 0.0;
    int numData = 0;

    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenarioId = i.next();
      DistributionScenario s = new DistributionScenario();
      s.setName(scenarioId);
      total += computeUnNormalizedScalability(s, connName);
      numData++;
    }

    return (total / (1.0 * numData));
  }

  private double getAvgEfficiency(String connName) {
    PerformanceProfile prof = getConnectorPerformanceProfile(connName);
    double total = 0.0;
    int numData = 0;

    for (Iterator<String> i = prof.getRawData().keySet().iterator(); i
        .hasNext();) {
      String scenarioId = i.next();
      DistributionScenario s = new DistributionScenario();
      s.setName(scenarioId);
      total += computeUnNormalizedEfficiency(s, connName);
      numData++;
    }

    return (total / (1.0 * numData));
  }

  private RawPerformanceData getRawPerformanceData(PerformanceProfile prof,
      DistributionScenario scenario) {
    return prof.getRawData().get(scenario.getName());
  }

  private PerformanceProfile getConnectorPerformanceProfile(String connName) {
    for (Iterator<PerformanceProfile> i = kb.iterator(); i.hasNext();) {
      PerformanceProfile prof = i.next();
      if (prof.getConnector().equals(connName)) {
        return prof;
      }

    }

    return null;
  }

  private double cleanValue(ScenarioValue val) {
    if (val.getValue() == null
        || (val.getValue() != null && val.getValue().equals(""))) {
      return 0.0;
    } else
      return Double.valueOf(val.getValue()).doubleValue();
  }

  public static void main(String[] args) throws Exception {
    String performanceKbPath = null, connName = null, scenarioFilePath = null;
    String usage = "PerformanceScorer --kbPath <path> --conn <connector name> --scenario <scenarioFilePath>\n";

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--kbPath")) {
        performanceKbPath = args[++i];
      } else if (args[i].equals("--conn")) {
        connName = args[++i];
      } else if (args[i].equals("--scenario")) {
        scenarioFilePath = args[++i];
      }
    }

    if (performanceKbPath == null || connName == null
        || scenarioFilePath == null) {
      System.err.println(usage);
      System.exit(1);
    }

    PerformanceScorer scorer = new PerformanceScorer(PerformanceProfileReader
        .readPerformanceProfiles(new File(performanceKbPath)));
    DistributionScenario s = DistributionScenarioReader.readScenario(new File(
        scenarioFilePath));
    System.out.println("Consistency: ["
        + scorer.computeConsistency(s, connName) + "]");
    System.out.println("Efficiency: [" + scorer.computeEfficiency(s, connName)
        + "]");
    System.out.println("Scalability: ["
        + scorer.computeScalability(s, connName) + "]");
    System.out.println("Dependability: ["
        + scorer.computeDependability(s, connName) + "]");
  }

}
