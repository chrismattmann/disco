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

package edu.usc.softarch.disco.tools;

// JDK imports
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

// DISCO imports
import edu.usc.softarch.disco.connector.dcp.DistributionConnectorProfile;
import edu.usc.softarch.disco.selection.Selector;
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.util.DCPReader;
import edu.usc.softarch.disco.util.DistributionScenarioReader;
import edu.usc.softarch.disco.util.GenericStructFactory;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A generic benchmarking tool to measure the amount of time taken (in
 * miliseconds) for a particular {@link Selector} to make a connector selection
 * on a given {@link DistributionScenario}.
 * </p>.
 */
public class SelectorTestRunner {

  private Selector selector = null;

  private DistributionScenario scenario = null;

  private List<DistributionConnectorProfile> dcps = null;

  public SelectorTestRunner(String selectorClassName, String dcpPath,
      String scenarioFilePath, String propertyFilePath)
      throws InstantiationException {
    this.selector = GenericStructFactory
        .getSelectorFromClassName(selectorClassName);
    Properties props = new Properties();
    try {
      props.load(new FileInputStream(new File(propertyFilePath)));
    } catch (Exception e) {
      throw new InstantiationException(e.getMessage());
    }
    this.selector.configure(props);

    try {
      dcps = DCPReader.parseDCPs(new File(dcpPath));
    } catch (Exception e) {
      throw new InstantiationException(e.getMessage());
    }

    this.scenario = DistributionScenarioReader.readScenario(new File(
        scenarioFilePath));
  }

  public long run(int num) throws Exception {

    long totalTime = 0L;

    for (int i = 0; i < num; i++) {
      long timeBefore = System.currentTimeMillis();
      // run the algorithm
      selector.selectConnectors(scenario, dcps);
      long timeAfter = System.currentTimeMillis();
      totalTime += (timeAfter - timeBefore);
    }

    return totalTime;
  }

  /**
   * @return the dcps
   */
  public List<DistributionConnectorProfile> getDcps() {
    return dcps;
  }

  /**
   * @param dcps
   *          the dcps to set
   */
  public void setDcps(List<DistributionConnectorProfile> dcps) {
    this.dcps = dcps;
  }

  /**
   * @return the scenario
   */
  public DistributionScenario getScenario() {
    return scenario;
  }

  /**
   * @param scenario
   *          the scenario to set
   */
  public void setScenario(DistributionScenario scenario) {
    this.scenario = scenario;
  }

  /**
   * @return the selector
   */
  public Selector getSelector() {
    return selector;
  }

  /**
   * @param selector
   *          the selector to set
   */
  public void setSelector(Selector selector) {
    this.selector = selector;
  }

  public static void main(String[] args) throws Exception {
    String propFilePath = null, scenarioPath = null, selectorClassName = null;
    String dcpDirPath = null;
    int numRuns = 0;

    String usage = "SelectorTestRunner [options]\n"
        + "--selector <class name>\n" + "--props <path>\n"
        + "--scenarioPath <path>\n" + "--dcpDir <path>\n" + "--numRuns <num>\n";

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--props")) {
        propFilePath = args[++i];
      } else if (args[i].equals("--selector")) {
        selectorClassName = args[++i];
      } else if (args[i].equals("--scenarioPath")) {
        scenarioPath = args[++i];
      } else if (args[i].equals("--dcpDir")) {
        dcpDirPath = args[++i];
      } else if (args[i].equals("--numRuns")) {
        numRuns = Integer.parseInt(args[++i]);
      }
    }

    if (propFilePath == null || selectorClassName == null
        || scenarioPath == null || dcpDirPath == null || numRuns == 0) {
      System.err.println(usage);
      System.exit(1);
    }

    SelectorTestRunner runner = new SelectorTestRunner(selectorClassName,
        dcpDirPath, scenarioPath, propFilePath);

    long totalMilis = runner.run(numRuns);
    System.out.println("Num Runs: [" + numRuns + "] | Total Time: ["
        + totalMilis + "]");
  }

}
