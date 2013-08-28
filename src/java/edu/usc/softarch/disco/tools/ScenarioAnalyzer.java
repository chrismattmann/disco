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
import java.util.List;
import java.util.Properties;

// DISCO imports
import edu.usc.softarch.disco.analysis.Analyzer;
import edu.usc.softarch.disco.analysis.StatsGenerator;
import edu.usc.softarch.disco.connector.dcp.DistributionConnectorProfile;
import edu.usc.softarch.disco.selection.ConnectorRank;
import edu.usc.softarch.disco.selection.Selector;
import edu.usc.softarch.disco.structs.AnswerKey;
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.structs.DistributionScenarioResult;
import edu.usc.softarch.disco.structs.PrecisionRecallData;
import edu.usc.softarch.disco.util.AnswerKeyReader;
import edu.usc.softarch.disco.util.DCPReader;
import edu.usc.softarch.disco.util.DistributionScenarioReader;
import edu.usc.softarch.disco.util.GenericStructFactory;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A tool to run a scenario through connector selection, and then analysis
 * afterwards.
 * </p>.
 */
public final class ScenarioAnalyzer {

  private List<DistributionConnectorProfile> dcps = null;

  private Selector selector = null;

  private Analyzer analyzer = null;

  public ScenarioAnalyzer(String dcpPath, String propFilePath,
      String selectorClassName, String analyzerClassName)
      throws InstantiationException {
    try {
      dcps = DCPReader.parseDCPs(new File(dcpPath));
      selector = GenericStructFactory
          .getSelectorFromClassName(selectorClassName);
      analyzer = GenericStructFactory
          .getAnalyzerFromClassName(analyzerClassName);
      Properties props = new Properties();
      props.load(new File(propFilePath).toURL().openStream());
      selector.configure(props);
      analyzer.configure(props);
    } catch (Exception e) {
      throw new InstantiationException(e.getMessage());
    }
  }

  public PrecisionRecallData analyze(DistributionScenario scenario, AnswerKey expertKey) {
    List<ConnectorRank> rankList = selector.selectConnectors(scenario, dcps);
    AnswerKey generated = analyzer
        .analyzeScenarioResult(new DistributionScenarioResult(scenario,
            rankList));
    PrecisionRecallData data = StatsGenerator.precisionRecallAnalysis(
        generated, expertKey);

    System.out.println("Summary:=================");
    System.out.println("Expert Set: Good: [" + expertKey.getAppropriate()
        + "]|Bad: [" + expertKey.getInappropriate() + "]");
    System.out.println("Generated Set: Good: [" + generated.getAppropriate()
        + "]|Bad: [" + generated.getInappropriate() + "]");
    System.out.println("TP: " + data.getTP());
    System.out.println("TN: " + data.getTN());
    System.out.println("FP: " + data.getFP());
    System.out.println("FN: " + data.getFN());

    System.out.println("Precision: " + StatsGenerator.computePrecision(data));
    System.out.println("Accuracy: " + StatsGenerator.computeAccuracy(data));
    System.out.println("Recall: " + StatsGenerator.computeRecall(data));
    System.out.println("Error Rate: " + StatsGenerator.computeErrorRate(data));
    
    return data;
  }

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    String dcpPath = null, propFilePath = null, selectorClass = null, analyzerClass = null, answerKeyPath = null;
    String scenarioPath = null;

    String usage = "ScenarioAnalyzer [options]\n"
        + "--dcpPath <path/to/dcps>\n" + "--propFile <path/to/prop/file>\n"
        + "--selector <class name of selector>\n"
        + "--analyzer <class name of analyzer>\n"
        + "--answerKey </path/to/expert/answer/key>\n"
        + "--scenario </path/to/scenario>\n";

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--dcpPath")) {
        dcpPath = args[++i];
      } else if (args[i].equals("--propFile")) {
        propFilePath = args[++i];
      } else if (args[i].equals("--selector")) {
        selectorClass = args[++i];
      } else if (args[i].equals("--analyzer")) {
        analyzerClass = args[++i];
      } else if (args[i].equals("--answerKey")) {
        answerKeyPath = args[++i];
      } else if (args[i].equals("--scenario")) {
        scenarioPath = args[++i];
      }
    }

    if (dcpPath == null || propFilePath == null || selectorClass == null
        || analyzerClass == null || answerKeyPath == null
        || scenarioPath == null) {
      System.err.println(usage);
      System.exit(1);
    }

    ScenarioAnalyzer analysis = new ScenarioAnalyzer(dcpPath, propFilePath,
        selectorClass, analyzerClass);
    AnswerKey expertKey = AnswerKeyReader.readAnswerKeyFile(new File(
        answerKeyPath));
    analysis.analyze(DistributionScenarioReader.readScenario(new File(
        scenarioPath)), expertKey);

  }

}
