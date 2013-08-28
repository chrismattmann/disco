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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A test tool to meaasure precision and recall
 * for the {@link Selector}s.
 * </p>.
 */
public class PrecisionRecallTester {

  public Map getScenarioAnswers(File file) throws Exception {
    BufferedReader br = new BufferedReader(new FileReader(file));

    String line = null;
    HashMap answers = new HashMap();
    while ((line = br.readLine()) != null) {
      if (!line.equals("")) {
        String[] toks = line.split(":");
        String scenarioName = toks[0];
        String connectorName = toks[1].trim();
        answers.put(scenarioName, connectorName);
      }
    }

    if (br != null) {
      try {
        br.close();
      } catch (Exception ignore) {
      }
      br = null;
    }
    return answers;
  }

  public Map parseSelectionAlgorithmOutput(File outputFile) throws Exception {
    BufferedReader br = new BufferedReader(new FileReader(outputFile));

    String line = null;
    TreeMap algoOutput = new TreeMap();

    while ((line = br.readLine()) != null) {
      if (line.startsWith("Scenario")) {
        // we've got a scenario, let's parse it:
        String[] toks = line.split("\\[");
        String[] toks2 = toks[1].split("\\]");
        String scenarioName = toks2[0];

        // now read the next liine: that's the connector choice
        line = br.readLine();
        String[] toks3 = line.split("\\[");
        String[] toks4 = toks3[1].split("\\]");
        String connectorName = toks4[0].trim();
        algoOutput.put(scenarioName, connectorName);
      }
    }

    if (br != null) {
      try {
        br.close();
      } catch (Exception ignore) {
      }
      br = null;
    }

    return algoOutput;

  }

  public void compare(Map answers, Map results) {
    int numRight = 0;
    int totalResults = 0;

    if (results != null) {
      for (Iterator i = results.keySet().iterator(); i.hasNext();) {
        String scenario = (String) i.next();
        String connector = (String) results.get(scenario);

        String rightConnector = (String) answers.get(scenario);
        if (connector.equals(rightConnector)) {
          numRight++;
        } else {
          System.out.println("Wrong answer: scenario: ["+scenario+"]: picked [" + connector
              + "] but right answer was: [" + rightConnector + "]");
        }
        totalResults++;
      }

      double pctRight = (1.0 * numRight) / (1.0 * totalResults);
      System.out.println("------------------------");
      System.out.println("Num Correct: "+numRight);
      System.out.println("Total Questions: "+totalResults);
      System.out.println("Pct Correct: " + pctRight);
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    String scenarioAnswerFile = null, algoOutputFile = null;
    String usage = "PrecisionRecallTester [options]\n"
        + "--scenarioAnswerFile <file>\n" + "--algoOutputFile <file>\n";

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--scenarioAnswerFile")) {
        scenarioAnswerFile = args[++i];
      } else if (args[i].equals("--algoOutputFile")) {
        algoOutputFile = args[++i];
      }
    }

    if (scenarioAnswerFile == null || algoOutputFile == null) {
      System.err.println(usage);
      System.exit(1);
    }

    PrecisionRecallTester tester = new PrecisionRecallTester();
    Map answers = tester.getScenarioAnswers(new File(scenarioAnswerFile));
    Map selectionOutput = tester.parseSelectionAlgorithmOutput(new File(
        algoOutputFile));
    tester.compare(answers, selectionOutput);
  }

}
