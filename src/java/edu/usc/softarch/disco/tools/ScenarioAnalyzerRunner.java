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
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// DISCO imports
import edu.usc.softarch.disco.analysis.StatsGenerator;
import edu.usc.softarch.disco.structs.AnswerKey;
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.structs.PrecisionRecallData;
import edu.usc.softarch.disco.util.AnswerKeyReader;
import edu.usc.softarch.disco.util.DistributionScenarioReader;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Runs an {@link Analyzer} an a set of {@link DistributionScenario}s
 * specified by respective directories containing XML files for the
 * scenarios, and for the corresponding {@link AnswerKey}s.
 * </p>.
 */
public final class ScenarioAnalyzerRunner {

  private static final FileFilter XML_FILE_FILTER = new FileFilter() {
    public boolean accept(File file) {
      return file.isFile() && file.getName().endsWith(".xml");
    }

  };

  private ScenarioAnalyzer analyzer = null;

  private Map<String, DistributionScenario> scenarioMap = null;

  private Map<String, AnswerKey> answerKeyMap = null;

  public ScenarioAnalyzerRunner(String answerKeyPath, String scenarioPath,
      String propFilePath, String dcpPath, String selectorClass,
      String analyzerClass) throws InstantiationException {
    analyzer = new ScenarioAnalyzer(dcpPath, propFilePath, selectorClass,
        analyzerClass);
    scenarioMap = buildScenarioMap(scenarioPath);
    answerKeyMap = buildAnswerKeyMap(answerKeyPath);
  }

  public void doAnalysis() {
    PrecisionRecallData totalData = new PrecisionRecallData();
    if (scenarioMap.keySet() != null && scenarioMap.keySet().size() > 0) {
      for (Iterator<String> i = scenarioMap.keySet().iterator(); i.hasNext();) {
        String scenarioId = i.next();
        System.out.println("Analyzing scenario: [" + scenarioId + "]");
        PrecisionRecallData data = analyzer.analyze(
            scenarioMap.get(scenarioId), answerKeyMap.get(scenarioId));
        totalData.setFN(totalData.getFN() + data.getFN());
        totalData.setTN(totalData.getTN() + data.getTN());
        totalData.setFP(totalData.getFP() + data.getFP());
        totalData.setTP(totalData.getTP() + data.getTP());
      }

      System.out.println("Summary=====================");
      System.out.println("TP: " + totalData.getTP());
      System.out.println("TN: " + totalData.getTN());
      System.out.println("FP: " + totalData.getFP());
      System.out.println("FN: " + totalData.getFN());

      System.out.println("Precision: "
          + StatsGenerator.computePrecision(totalData));
      System.out.println("Accuracy: "
          + StatsGenerator.computeAccuracy(totalData));
      System.out.println("Recall: " + StatsGenerator.computeRecall(totalData));
      System.out.println("Error Rate: "
          + StatsGenerator.computeErrorRate(totalData));

    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    String dcpPath = null, propFilePath = null, selectorClass = null, analyzerClass = null, answerKeyDirPath = null;
    String scenarioDirPath = null;

    String usage = "ScenarioAnalyzerRunner [options]\n"
        + "--dcpPath <path/to/dcps>\n" + "--propFile <path/to/prop/file>\n"
        + "--selector <class name of selector>\n"
        + "--analyzer <class name of analyzer>\n"
        + "--answerKeyDirPath </path/to/expert/answer/keys>\n"
        + "--scenarioDirPath </path/to/scenarios>\n";

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--dcpPath")) {
        dcpPath = args[++i];
      } else if (args[i].equals("--propFile")) {
        propFilePath = args[++i];
      } else if (args[i].equals("--selector")) {
        selectorClass = args[++i];
      } else if (args[i].equals("--analyzer")) {
        analyzerClass = args[++i];
      } else if (args[i].equals("--answerKeyDirPath")) {
        answerKeyDirPath = args[++i];
      } else if (args[i].equals("--scenarioDirPath")) {
        scenarioDirPath = args[++i];
      }
    }

    if (dcpPath == null || propFilePath == null || selectorClass == null
        || analyzerClass == null || answerKeyDirPath == null
        || scenarioDirPath == null) {
      System.err.println(usage);
      System.exit(1);
    }

    ScenarioAnalyzerRunner runner = new ScenarioAnalyzerRunner(
        answerKeyDirPath, scenarioDirPath, propFilePath, dcpPath,
        selectorClass, analyzerClass);
    runner.doAnalysis();

  }

  private Map<String, AnswerKey> buildAnswerKeyMap(String answerKeyPath) {
    File answerKeyDir = new File(answerKeyPath);
    File[] answerKeyFiles = answerKeyDir.listFiles(XML_FILE_FILTER);
    Map<String, AnswerKey> keys = new HashMap<String, AnswerKey>();
    AnswerKey key;
    for (int i = 0; i < answerKeyFiles.length; i++) {
      key = AnswerKeyReader.readAnswerKeyFile(answerKeyFiles[i]);
      keys.put(key.getScenarioId(), key);
    }

    return keys;
  }

  private Map<String, DistributionScenario> buildScenarioMap(String scenarioPath) {
    File scenarioDir = new File(scenarioPath);
    File[] scenarioFiles = scenarioDir.listFiles(XML_FILE_FILTER);
    Map<String, DistributionScenario> scenarios = new HashMap<String, DistributionScenario>();
    DistributionScenario s;
    for (int i = 0; i < scenarioFiles.length; i++) {
      s = DistributionScenarioReader.readScenario(scenarioFiles[i]);
      s.setName(scenarioFiles[i].toString());
      scenarios.put(scenarioFiles[i].getName(), s);
    }

    return scenarios;
  }

}
