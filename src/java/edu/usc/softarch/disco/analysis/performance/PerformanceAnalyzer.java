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

package edu.usc.softarch.disco.analysis.performance;

//JDK imports
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

//DISCO imports
import edu.usc.softarch.disco.analysis.Analyzer;
import edu.usc.softarch.disco.performance.PerformanceProfile;
import edu.usc.softarch.disco.performance.PerformanceScorer;
import edu.usc.softarch.disco.structs.AnswerKey;
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.structs.DistributionScenarioResult;
import edu.usc.softarch.disco.structs.PerformanceRequirements;
import edu.usc.softarch.disco.util.GenericStructFactory;
import edu.usc.softarch.disco.util.PerformanceProfileReader;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * An {@link Analzyer} that uses {@link PerformanceProfile}s to revise
 * {@link DistributionConnectorProfile} groupings based on actual performance
 * data captured in them.
 * </p>.
 */
public class PerformanceAnalyzer implements Analyzer {

  /* the regular analyzer that we'll use and augment the results from */
  private Analyzer analyzer = null;

  /* our performance profile kb */
  private List<PerformanceProfile> profKb = null;
  
  /* our performance scorer */
  private PerformanceScorer scorer = null;
  
  /* allowed number of perf req failures */
  private int numFailures = -1;

  public PerformanceAnalyzer() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.usc.softarch.disco.analysis.Analyzer#analyzeScenarioResult(edu.usc.softarch.disco.structs.DistributionScenarioResult)
   */
  public AnswerKey analyzeScenarioResult(DistributionScenarioResult result) {
    // get initial answer key
    AnswerKey origKey = new AnswerKey();
    origKey = this.analyzer.analyzeScenarioResult(result);

      // now filter the key based on performance scores
      String [] connNames = origKey.getAppropriate().toArray(new String[]{""});
      for (int i=0; i < connNames.length; i++) {
        String connName = connNames[i];
        PerformanceProfile prof = getConnectorPerformanceProfile(connName);
        if (!checkPerformance(result.getScenario(), prof)) {
          origKey.getAppropriate().remove(connName);
          origKey.getInappropriate().add(connName);
        }
      }      

    return origKey;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.usc.softarch.disco.util.Configurable#configure(java.util.Properties)
   */
  public void configure(Properties props) {
    String analyzerClassName = props.getProperty("performance.analyzer.class");
    this.analyzer = GenericStructFactory
        .getAnalyzerFromClassName(analyzerClassName);
    try {
      this.profKb = PerformanceProfileReader.readPerformanceProfiles(new File(
          props.getProperty("performance.kb.path")));
      scorer = new PerformanceScorer(profKb);
      numFailures = Integer.parseInt(props.getProperty("performance.req.failures.max"));
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private boolean checkPerformance(DistributionScenario s,
      PerformanceProfile prof) {
    int numFalse = 0;
    PerformanceRequirements reqs = s.getPerformanceReqs();
    if (scorer.computeConsistency(s, prof.getConnector()) < Double.valueOf(reqs.getConsistency())
        .doubleValue()) {
      numFalse++;
    } else if (scorer.computeDependability(s, prof.getConnector()) < Double
        .valueOf(reqs.getDependability()).doubleValue()) {
      numFalse++;
    } else if (scorer.computeEfficiency(s, prof.getConnector()) < Double.valueOf(reqs.getEfficiency())
        .doubleValue()) {
      numFalse++;
    } else if (scorer.computeScalability(s, prof.getConnector()) < Double.valueOf(reqs.getScalability())
        .doubleValue()) {
      numFalse++;
    } 
    
    if(numFalse > this.numFailures){
      return false;
    }
    else return true;
  }

  private PerformanceProfile getConnectorPerformanceProfile(String connName) {
    for (Iterator<PerformanceProfile> i = profKb.iterator(); i.hasNext();) {
      PerformanceProfile prof = i.next();
      if (prof.getConnector().equals(connName)) {
        return prof;
      }

    }

    return null;
  }

}
