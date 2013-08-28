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

package edu.usc.softarch.disco.tools;

// JDK imports
import java.io.File;
import java.io.FileFilter;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

// DISCO imports
import edu.usc.softarch.disco.selection.ConnectorRank;
import edu.usc.softarch.disco.selection.Selector;
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.structs.DistributionScenarioResult;
import edu.usc.softarch.disco.util.DCPReader;
import edu.usc.softarch.disco.util.DistributionScenarioReader;
import edu.usc.softarch.disco.util.GenericStructFactory;
import edu.usc.softarch.disco.connector.dcp.DistributionConnectorProfile;

/**
 * @author mattmann
 * @author Trevor Johns &gt;{@link <a href="mailto:tjohns@usc.edu">tjohns@usc.edu</a>}&lt;
 * @version $Revision$
 * 
 * <p>
 * Runs a {@link Selector} on a set of {@link DistributionScenario}s that
 * reside in a particular directory.
 * </p>.
 */
public final class ScenarioRunner {

  private static final FileFilter XML_FILE_FILTER = new FileFilter() {
    public boolean accept(File file) {
      return file.isFile() && file.getName().endsWith(".xml");
    }

  };

  /* our log stream */
  private static final Logger LOG = Logger.getLogger(ScenarioRunner.class
      .getName());

  private ScenarioRunner() throws InstantiationException {
    throw new InstantiationException("Don't construct tools!");
  }
  
  /**
   * Run a scenario with the given command line parameters.
   *
   * @param scenarios A list of distribution scenarios that should be tested against.
   * @param dcpProfiles A list of connector profiles that will be run against the given scenarios.
   * @param props Application configuration file.
   * @param selectorClass The class which will decide which connector to use.
   * @param topN The number of scores to print, or -1 if unlimited.
   * @param printScores Whether to print scores or not.
   * @return A list of {@link ConnectorRank} objects for each scenario if success, otherwise null.
   */
  public static List<DistributionScenarioResult> runScenarios(String scenarioFileDirPath, String dcpPath,
  String propFilePath, String selectorClass) throws Exception {
      // Parse command line parameters
      File scenarioDir = new File(scenarioFileDirPath);

      if (!scenarioDir.isDirectory()) {
          LOG.log(Level.SEVERE,
              "Must specify a valid directory containing scenario xml files: "
              + "directory: [" + scenarioFileDirPath + "] not directory!");
          return null;
      }

      List dcpProfiles = DCPReader.parseDCPs(new File(dcpPath));

      Properties props = new Properties();
      props.load(new File(propFilePath).toURL().openStream());
      
      File[] scenarioFiles = scenarioDir.listFiles(XML_FILE_FILTER);
      LinkedList <DistributionScenario> scenarios = new LinkedList<DistributionScenario>();
      DistributionScenario s;
      for (int i = 0; i < scenarioFiles.length; i++) {
          s = DistributionScenarioReader.readScenario(scenarioFiles[i]);
          s.setName(scenarioFiles[i].toString());
          scenarios.add(s);
      }
      
      // Pass the command line arguments on to the real runScenarios() for processing
      List<DistributionScenarioResult> results = runScenarios(scenarios, dcpProfiles, props, selectorClass);
      
      return results;
  }
  
  /**
   * Run a scenario with the given parameters.
   *
   * @param scenarios A list of distribution scenarios that should be tested against.
   * @param dcpProfiles A list of connector profiles that will be run against the given scenarios.
   * @param props Application configuration file.
   * @param selectorClass The class which will decide which connector to use.
   * @param topN The number of scores to print, or -1 if unlimited.
   * @param printScores Whether to print scores or not.
   * @return A list of {@link ConnectorRank} objects for each scenario. 
   */
  public static List<DistributionScenarioResult> runScenarios(List<DistributionScenario> scenarios, List<DistributionConnectorProfile> dcpProfiles,
  Properties props, String selectorClass) throws Exception {
      Selector selector = GenericStructFactory.getSelectorFromClassName(selectorClass);
      selector.configure(props);

      // Iterate through all provided scenarios, calling selector.selectConnectors() on
      // each and outputting the results to a new LinkedList.
      LinkedList<DistributionScenarioResult> results = new LinkedList<DistributionScenarioResult>();
      for (Iterator<DistributionScenario> i = scenarios.iterator(); i.hasNext(); ) {
          DistributionScenario scenario = i.next();
          
          // Run scenario
          List rankList = selector.selectConnectors(scenario, dcpProfiles);
          
          // Add results to results table
          results.add(new DistributionScenarioResult(scenario, rankList));
      }
      
      return results;
  }
    
  public static void main(String[] args) throws Exception {
    String scenarioDirPath = null, dcpDirPath = null, propFilePath = null, selectorClass = null;
    int topN = -1;
    boolean printScores=true;

    String usage = "ScenarioRunner [options]\n" + "--scenarioDirPath <path>\n"
        + "--dcpDir <path>\n" + "--propFile <path>\n"
        + "--selector <class name>\n" + "[--topN <num>]\n"
        + "[--scoresoff]\n";

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--scenarioDirPath")) {
        scenarioDirPath = args[++i];
      } else if (args[i].equals("--dcpDir")) {
        dcpDirPath = args[++i];
      } else if (args[i].equals("--propFile")) {
        propFilePath = args[++i];
      } else if (args[i].equals("--selector")) {
        selectorClass = args[++i];
      } else if (args[i].equals("--topN")) {
        topN = Integer.parseInt(args[++i]);
      }
      else if(args[i].equals("--scoresoff")){
        printScores=false;
      }
    }

    if (scenarioDirPath == null) {
        System.err.println("Error: Scenario directory not specified.");
    }
    if (dcpDirPath == null) {
        System.err.println("Error: DCP directory not specified.");
    }
    if (propFilePath == null) {
        System.err.println("Error: Properties file not specified.");
    }
    if (selectorClass == null) {
        System.err.println("Error: Selector class not specified.");
    }

    if (scenarioDirPath == null || dcpDirPath == null || propFilePath == null
        || selectorClass == null) {
      System.err.println("\n" + usage);
      System.exit(1);
    }

    List<DistributionScenarioResult> results = ScenarioRunner.runScenarios(scenarioDirPath, 
        dcpDirPath, propFilePath, selectorClass);
        
    // Print the results
    DistributionScenarioResult.printScenarioResults(results, topN, printScores);
  }
}
