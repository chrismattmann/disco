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

//JDK imports
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Describe your class here
 * </p>.
 */
public class BayesianInferencer {
	
	/* our log stream */
	private static final Logger LOG = Logger.getLogger(BayesianInferencer.class.getName());

	public BayesianInferencer() {
	}

	// the goal of this component is to take
	// a prior probability distribution, along with
	// a new piece of sample evidence, along with
	// a set of conditional probabilities e.g.,
	// P(y = 1 | theta = 0) = 0.07
	// P(y = 1 | theta = 1) = 0.95
	// P(y = 1 | theta = 2) = 0.90
	// and to then apply bayesTheorem to update the prior
	// distribution to a new distribution

	public ProbabilityDistribution applyBayesTheorem(
			ProbabilityDistribution prior, RandomVariable observed,
			ConditionalProbabilityTable condProbTable) {

		// to apply Bayes theorem, we need to iterate through
		// the prior distribution, and update the probabilities as
		// we encounter them, using the conditional probabilities

		// for each Statement in the ProbabilityDistribution, updaate
		// its associated probability according to the following 
		// formula
		
		// P(priorVar=statementVarVal|observedVar=observedVal) = 
		// 
		// P(observedVar=observedVal|priorVal=statementVarVal) * P(priorVal=statementVarVal)
		// ----------------------------------------------------------------------------------
		// Sum over all priorVal P(observedVal|priorVal) * P(priorVal)
		
		// first thing to calculate is denominator
		
		double bayesDenom = getBayesDenom(prior, observed, condProbTable);
		
		ProbabilityDistribution postDistribution = null;
		// now for each value in the prior distribution, get its probablity
		// and then get its conditional probability, and update it accordingly
		if(prior.getStatements() != null && prior.getStatements().size() > 0){
			postDistribution = new ProbabilityDistribution();
			//copy through
			postDistribution.setVarName(prior.getVarName());
			boolean allStatementsExist = BayesUtil.allStatementsExist(prior.getStatements(), 
					(List)condProbTable.getConditionalProbabilities(observed));
			
			for(Iterator i = prior.getStatements().iterator(); i.hasNext(); ){
			   DiscreteStatement statement = (DiscreteStatement)i.next();
			   
			   double prob = 0.0F;
			   
			   if(condProbTable.getConditionalProbabilities(observed) != null && allStatementsExist){
				   double statementCondProb = getObservedPriorProb(condProbTable.getConditionalProbabilities(observed), statement);
				   double bayesNumerator = statement.getProbability() * statementCondProb;
				   prob = bayesNumerator / bayesDenom;
				   LOG.log(Level.FINE, "Probability updated: new value: ["+prob+"]");
			   }
			   else{
				   // just copy through old one if there is no conditional probability data
				   LOG.log(Level.FINE, "Keeping old probability: ["+statement.getProbability()+"]");
				   prob = statement.getProbability();
			   }
			  
			   DiscreteStatement postStatement = new DiscreteStatement();
			   postStatement.setVariable(statement.getVariable());
			   postStatement.setProbability(prob);
			   postDistribution.getStatements().add(postStatement);
			}			
		}
		
		return postDistribution;
		
	}
	
	private double getBayesDenom(ProbabilityDistribution prior, RandomVariable observed, 
			ConditionalProbabilityTable condProbTable){
		
		double bayesDenomProb = 0.0F;
		
		if(prior.getStatements() != null && prior.getStatements().size() > 0){
			for(Iterator i = prior.getStatements().iterator(); i.hasNext(); ){
				DiscreteStatement statement = (DiscreteStatement)i.next();
				
				double pPriorVal = statement.getProbability();
				
				// need to look up P(observedVal|priorVal)
				List condProbs = condProbTable.getConditionalProbabilities(observed);
				
				double pObsPrior = getObservedPriorProb(condProbs, statement);
				bayesDenomProb+=(pPriorVal * pObsPrior);
			}
		}
		
		return bayesDenomProb;
	}
	
	private double getObservedPriorProb(List condProbs, DiscreteStatement statement){
		double obsPriorProb = 0.0F;
		
		
		if(condProbs != null && condProbs.size() > 0){
			for(Iterator i = condProbs.iterator(); i.hasNext(); ){
				ConditionalProbability cProb = (ConditionalProbability)i.next();
				
				if(cProb.getInterestVar().equals(statement.getVariable())){
					obsPriorProb = cProb.getProbability();
					break;
				}
			}
		}
		
		return obsPriorProb;
	}
	
	

}
