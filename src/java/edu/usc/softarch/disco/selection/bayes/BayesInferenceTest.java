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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Describe your class here
 * </p>.
 */
public class BayesInferenceTest {

	public BayesInferenceTest() {

	}

	public ProbabilityDistribution doTest() {
		// first set up the conditional probability table

		ConditionalProbabilityTable tab = new ConditionalProbabilityTable();
		BayesUtil.addConditionalProbability(tab, "Y", "1", "theta", "0", 0.07);
		BayesUtil.addConditionalProbability(tab, "Y", "1", "theta", "1", 0.95);
		BayesUtil.addConditionalProbability(tab, "Y", "1", "theta", "2", 0.90);

		ProbabilityDistribution prior = new ProbabilityDistribution();
		prior.setVarName("theta");

		List statementList = new Vector();
		statementList.add(new DiscreteStatement("theta", "0", 0.97));
		statementList.add(new DiscreteStatement("theta", "1", 0.01));
		statementList.add(new DiscreteStatement("theta", "2", 0.02));
		
		System.out.println("Prior Distribution of theta");
		System.out.println("===========================");
		for(Iterator i = statementList.iterator(); i.hasNext(); ){
			DiscreteStatement statement = (DiscreteStatement)i.next();
			System.out.println(statement);
		}

		prior.setStatements(statementList);

		RandomVariable observedY = new RandomVariable();
		observedY.setName("Y");
		observedY.setValue("1");

		BayesianInferencer inferencer = new BayesianInferencer();
		return inferencer.applyBayesTheorem(prior, observedY, tab);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BayesInferenceTest test = new BayesInferenceTest();
		ProbabilityDistribution dist = test.doTest();
		
		

		if (dist != null) {
			System.out.println("Post Probability Distribution for ["
					+ dist.getVarName() + "]");
			System.out.println("=============================");

			if (dist.getStatements() != null && dist.getStatements().size() > 0) {
				for (Iterator i = dist.getStatements().iterator(); i.hasNext();) {
					DiscreteStatement statement = (DiscreteStatement) i.next();

					System.out.println("P(" + statement.getVariable()
							+ "|Y=1) = " + statement.getProbability());
				}
			}

		}
	}

}
