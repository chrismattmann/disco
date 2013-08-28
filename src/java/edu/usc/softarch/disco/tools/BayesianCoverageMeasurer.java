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

//JDK imports
import java.io.File;
import java.util.Iterator;
import java.util.List;

//DISCO imports
import edu.usc.softarch.disco.connector.dcp.DistributionConnectorProfile;
import edu.usc.softarch.disco.selection.bayes.BayesUtil;
import edu.usc.softarch.disco.selection.bayes.ConditionalProbability;
import edu.usc.softarch.disco.selection.bayes.ConditionalProbabilityTable;
import edu.usc.softarch.disco.selection.bayes.RandomVariable;
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.util.BayesianDomainProfileReader;
import edu.usc.softarch.disco.util.DCPReader;
import edu.usc.softarch.disco.util.DistributionScenarioReader;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Measures the coverage of a set of observed variables
 * against a given BayesianDomainProfile.
 * </p>.
 */
public class BayesianCoverageMeasurer {

  public void measureCoverage(ConditionalProbabilityTable cTab,
      DistributionScenario scenario, DistributionConnectorProfile prof) {

    List observedVars = BayesUtil
        .getObservationsFromDistributionScenario(scenario);

    System.out.println("DCP: ["+prof.getConnectorName()+"]");
    int totalHits = 0;
    int totalObsVar = 0;
    double probSum=0.0;
    for (Iterator i = observedVars.iterator(); i.hasNext();) {
      RandomVariable obsVar = (RandomVariable) i.next();
      System.out.println("Obs var: ["+obsVar+"]");

      List cProbs = cTab.getConditionalProbabilities(obsVar);

      if (cProbs != null && cProbs.size() > 0) {
        totalHits++;
        probSum+=countProbs("data_access_locality", prof.getDataAccessProfile().getLocality(),
           cProbs, obsVar);
        probSum+=countProbs("data_access_transient_availability", prof.getDataAccessProfile()
           .getAvailabilities().getAvailTransient(), cProbs, obsVar);
        probSum+=countProbs("data_access_cardinality_senders", prof.getDataAccessProfile().getCardinality()
            .getSenderCardinality(), cProbs, obsVar);
        probSum+=countProbs("data_access_cardinality_receivers", prof.getDataAccessProfile().getCardinality()
            .getReceiverCardinality(), cProbs, obsVar);
        probSum+=countProbs("stream_bounds", prof.getStreamProfile().getBounds(), cProbs, obsVar);
      }
      
      totalObsVar++;
    }
    
    System.out.println("Summary:");
    System.out.println("===================");
    System.out.println("Total Hits: "+totalHits);
    System.out.println("Total Observed Variables: "+totalObsVar);
    System.out.println("Coverage Pct: "+((1.0*totalHits) / (1.0*totalObsVar)));
    System.out.println("Probability Sum: "+probSum);
    

  }

  private double getProb(RandomVariable interestVar, List cProbs) {
    //System.out.println("Searching cProbs: ["+cProbs+"]");
    //System.out.println("Looking for: "+interestVar);
    for (Iterator i = cProbs.iterator(); i.hasNext();) {
      ConditionalProbability cProb = (ConditionalProbability) i.next();
      if (cProb.getInterestVar().equals(interestVar)) {
        return cProb.getProbability();
      }
    }

    return -1.0f;
  }
  
  private double countProbs(String propName, String propValue, List cProbs, RandomVariable obsVar){
    double probIncrement = -1.0d;
    RandomVariable rVar = BayesUtil.getRandomVariable(
        propName, propValue);
    double condProb = getProb(rVar, cProbs);
    
    if(condProb != -1.0d){
      System.out.println(propName+": DCP value: ["+propValue+"]: " +
          ": observedVar: ["+obsVar+"]: prob: [" + condProb + "]");
          probIncrement = condProb;          
    }
    else{
      probIncrement = 0.0d;
    }
    
    return probIncrement;
  }

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    String domainProf = null, scenarioFile = null, dcpFile = null;

    String usage = "BayesianCoverageMeasurer [options]\n"
        + "--domainProf <file>\n" + "--scenarioFile <file>\n"
        + "--dcp <file>\n";

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--domainProf")) {
        domainProf = args[++i];
      } else if (args[i].equals("--scenarioFile")) {
        scenarioFile = args[++i];
      } else if (args[i].equals("--dcp")) {
        dcpFile = args[++i];
      }
    }

    if (domainProf == null || scenarioFile == null || dcpFile == null) {
      System.err.println(usage);
      System.exit(1);
    }

    BayesianCoverageMeasurer measurer = new BayesianCoverageMeasurer();
    measurer.measureCoverage(BayesianDomainProfileReader.readProfile(new File(
        domainProf)), DistributionScenarioReader.readScenario(new File(
        scenarioFile)), DCPReader.parseDCP(new File(dcpFile)));
  }

}
