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

package edu.usc.softarch.disco.selection.bayes;

// JDK imports
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

// DISCO imports
import edu.usc.softarch.disco.connector.dcp.DistributionConnectorProfile;
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.structs.PerformanceRequirements;
import edu.usc.softarch.disco.structs.ScenarioValue;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A Set of utility methods to perform Bayesian inference.
 * </p>.
 */
public final class BayesUtil {

  /* our log stream */
  private static final Logger LOG = Logger.getLogger(BayesUtil.class.getName());

  private BayesUtil() throws InstantiationException {
    throw new InstantiationException("Don't construct utility classes!");
  }

  public static void addConditionalProbability(ConditionalProbabilityTable tab,
      String obsVarName, String obsValue, String randName, String randVal,
      double prob) {
    ConditionalProbability cProb = new ConditionalProbability();
    RandomVariable var = new RandomVariable();
    var.setName(randName);
    var.setValue(randVal);

    RandomVariable obsVar = new RandomVariable();
    obsVar.setName(obsVarName);
    obsVar.setValue(obsValue);
    cProb.setSampleStatisticObservedValue(obsVar);
    cProb.setProbability(prob);
    cProb.setInterestVar(var);
    tab.addConditionalProbability(obsVar, cProb);
  }

  public static DiscreteStatement getMaxStatement(String attrName, 
     List attrVals, List statementList){
    DiscreteStatement maxStatement = null;
    double max = Double.MIN_VALUE;
    
    if(attrVals == null){
      // just return a statement with probability 1.0
      // this won't affect anything
      DiscreteStatement statement = new DiscreteStatement(""
          ,"", 1.0f);
      return statement;
    }
    
    for(Iterator i = attrVals.iterator(); i.hasNext(); ){
      String attrVal = (String)i.next();
      DiscreteStatement statement = getStatement(statementList,
          attrName, attrVal);
      if(statement == null){
        LOG.log(Level.WARNING, "search for statement returned null: " +
        "statement list: ["+statementList+"], statement: attrName=["+attrName
        +"],attrVal=["+attrVal+"]");
      }
      
      if(statement.getProbability() > max){
        maxStatement = statement;
        max = statement.getProbability();
      }
    }
    
    return maxStatement;    
 }

  public static DiscreteStatement getStatement(List statementList,
      String varName, String varValue) {

    DiscreteStatement statement = null;

    if (statementList != null && statementList.size() > 0) {
      for (Iterator i = statementList.iterator(); i.hasNext();) {
        DiscreteStatement stmt = (DiscreteStatement) i.next();
        if (stmt.getVariable().getName().equals(varName)
            && stmt.getVariable().getValue().equals(varValue)) {
          statement = stmt;
        }
      }
    }

    return statement;
  }

  public static double getProbFromPriorDistribution(
      DistributionConnectorProfile profile, ProbabilityDistribution prior) {
    double prob = 0.0;
    if (prior.getStatements() != null && prior.getStatements().size() > 0) {
      for (Iterator i = prior.getStatements().iterator(); i.hasNext();) {
        DiscreteStatement statement = (DiscreteStatement) i.next();
        if (statement.getVariable().getValue().equals(
            profile.getConnectorName())) {
          prob = statement.getProbability();
        }
      }
    }

    return prob;
  }

  public static boolean allStatementsExist(List statements, List condProbs) {
    if (statements == null || (statements != null && statements.size() == 0)) {
      return false;
    }

    for (Iterator i = statements.iterator(); i.hasNext();) {
      DiscreteStatement statement = (DiscreteStatement) i.next();
      if (!statementExists(statement, condProbs)) {
        return false;
      }
    }

    return true;
  }

  public static boolean statementExists(DiscreteStatement statement,
      List condProbs) {
    if (condProbs == null || (condProbs != null && condProbs.size() == 0)) {
      return false;
    }

    for (Iterator i = condProbs.iterator(); i.hasNext();) {
      ConditionalProbability condProb = (ConditionalProbability) i.next();
      RandomVariable interestVar = condProb.getInterestVar();
      if (interestVar.equals(statement.getVariable())) {
        return true;
      }
    }

    return false;
  }

  public static void normalizeProbabilityDistribution(
      ProbabilityDistribution dist) {
    double denom = sumProbabilities(dist);

    if (dist != null && dist.getStatements() != null
        && dist.getStatements().size() > 0) {
      for (Iterator i = dist.getStatements().iterator(); i.hasNext();) {
        DiscreteStatement statement = (DiscreteStatement) i.next();
        double normProb = statement.getProbability() / denom;
        statement.setProbability(normProb);
      }
    }

  }

  private static double sumProbabilities(ProbabilityDistribution dist) {
    double sum = 0.0d;

    if (dist == null || (dist != null && dist.getStatements().size() == 0)) {
      return sum;
    }

    for (Iterator i = dist.getStatements().iterator(); i.hasNext();) {
      DiscreteStatement statement = (DiscreteStatement) i.next();
      sum += statement.getProbability();
    }

    return sum;
  }
  

  public static List getObservationsFromDistributionScenario(
      DistributionScenario scenario) {
    List observations = new Vector();
    observations.addAll(getObservedVarsFromScenarioValue("TotalVolume",
        scenario.getTotalVolume()));
    observations.addAll(getObservedVarsFromScenarioValue("NumUsers", scenario
        .getNumUsers()));
    observations.addAll(getObservedVarsFromScenarioValue(
        "DeliverySchedule.VolumePerInterval", scenario.getDelivSchedule()
            .getVolumePerInterval()));
    observations.addAll(getObservedVarsFromScenarioValue(
        "DeliverySchedule.NumberOfIntervals", scenario.getDelivSchedule()
            .getNumberOfIntervals()));
    observations.addAll(getObservedVarsFromScenarioValue("NumUserTypes",
        scenario.getNumUserTypes()));

    observations.addAll(getObservedVarsFromPerformanceReqs(scenario
        .getPerformanceReqs()));
    observations.addAll(getObservedVarsFromValList("AccessPolicies", scenario
        .getAccessPolicies()));
    observations.addAll(getObservedVarsFromValList("GeographicDistribution",
        scenario.getGeoDistribution()));
    observations.addAll(getObservedVarsFromValList("DataTypes", scenario
        .getDataTypes()));

    return observations;

  }

  private static List getObservedVarsFromValList(String varName, List vals) {
    List obs = new Vector();
    if (vals != null && vals.size() > 0) {
      for (Iterator i = vals.iterator(); i.hasNext();) {
        String valStr = (String) i.next();
        obs.add(getRandomVariable(varName, valStr));
      }
    }

    return obs;
  }

  private static List getObservedVarsFromPerformanceReqs(PerformanceRequirements reqs) {
    List obs = new Vector();
    if(reqs.getConsistency() != null){
      obs.add(getRandomVariable("PerformanceReqs.Consistency", reqs
          .getConsistency()));      
    }
    if(reqs.getEfficiency() != null){
      obs.add(getRandomVariable("PerformanceReqs.Efficiency", reqs
          .getEfficiency()));      
    }
    if(reqs.getDependability() != null){
      obs.add(getRandomVariable("PerformanceReqs.Dependability", reqs
          .getDependability()));      
    }
    
    if(reqs.getScalability() != null){
      obs.add(getRandomVariable("PerformanceReqs.Scalability", reqs
          .getScalability()));      
    }

    return obs;
  }

  public static RandomVariable getRandomVariable(String name, String val) {
    RandomVariable var = new RandomVariable();
    var.setName(name);
    var.setValue(val);
    return var;
  }

  private static List getObservedVarsFromScenarioValue(String valName,
      ScenarioValue value) {
    List obs = new Vector();

    if(value == null || (value != null && value.getRangeVal() == null 
        && value.getValue() == null) ||
        (value != null && value.getRangeVal() != null &&
            value.getValue() != null && value.getValue().
            equals(""))) return obs;
    
    if (value.isRangeValue()) {
      RandomVariable lowerBoundVar = new RandomVariable();
      lowerBoundVar.setName(valName);
      String valLow = getValStr(value.getRangeVal().getMinValue(), "lower",
          value.getRangeVal().isMinInclusive());
      lowerBoundVar.setValue(valLow);

      RandomVariable upperBoundVar = new RandomVariable();
      upperBoundVar.setName(valName);
      String valHigh = getValStr(value.getRangeVal().getMaxValue(), "upper",
          value.getRangeVal().isMaxInclusive());
      upperBoundVar.setValue(valHigh);

      if(value.getRangeVal().getMinValue() != null 
          && !value.getRangeVal().getMinValue()
          .equals("")){
        obs.add(lowerBoundVar);        
      }
      
      if(value.getRangeVal().getMaxValue() != null
          && !value.getRangeVal().getMaxValue()
          .equals("")){
        obs.add(upperBoundVar);        
      }
    } else {
      RandomVariable scenarioVar = new RandomVariable();
      scenarioVar.setName(valName);
      scenarioVar.setValue(value.getValue());
      obs.add(scenarioVar);
    }

    return obs;
  }

  private static String getValStr(String val, String bounds, boolean inclusive) {
    String op = null;

    if (bounds.equals("lower")) {
      if (inclusive) {
        op = "geq";
      } else {
        op = "gt";
      }
    } else {
      if (inclusive) {
        op = "leq";
      } else {
        op = "lt";
      }
    }

    return op + " " + val;
  }

}
