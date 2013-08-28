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
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Converts the output of a {@link ScenarioRunner} for
 * a {@link Selector} into an AnswerKey.
 * </p>.
 */
public class ScenarioRunnerOutputConverter {

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

  public void writeOutputFile(Map answers, File f) throws Exception {
    PrintWriter pw = new PrintWriter(new FileWriter(f));

    for (Iterator i = answers.keySet().iterator(); i.hasNext();) {
      String scenario = (String) i.next();
      String connector = (String) answers.get(scenario);

      pw.println(scenario + ":" + connector);
    }

    if (pw != null) {
      try {
        pw.close();
      } catch (Exception ignore) {
      }
      pw = null;
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    String scenarioOutputFile = null, answerKeyOutputFile = null;

    String usage = "ScenarioRunnerOutputConverter [options]\n"
        + "--scenarioOutputFile <file>\n" + "--answerKeyOutputFile <file>\n";

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--scenarioOutputFile")) {
        scenarioOutputFile = args[++i];
      } else if (args[i].equals("--answerKeyOutputFile")) {
        answerKeyOutputFile = args[++i];
      }
    }

    if (scenarioOutputFile == null || answerKeyOutputFile == null) {
      System.err.println(usage);
      System.exit(1);
    }

    ScenarioRunnerOutputConverter converter = new ScenarioRunnerOutputConverter();
    Map answers = converter.parseSelectionAlgorithmOutput(new File(
        scenarioOutputFile));
    converter.writeOutputFile(answers, new File(answerKeyOutputFile));

  }

}
