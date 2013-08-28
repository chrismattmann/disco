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
import java.util.Vector;

//Junit imports
import junit.framework.TestCase;

/**
 * @author mattmann
 * @version $Revision$
 *
 * <p>Test Case for the Bayes Inferencer.</p>.
 */
public class TestBayesInferencer extends TestCase {

	public void testBayesTheorem(){
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

		prior.setStatements(statementList);

		RandomVariable observedY = new RandomVariable();
		observedY.setName("Y");
		observedY.setValue("1");

		BayesianInferencer inferencer = new BayesianInferencer();
		ProbabilityDistribution posterior = inferencer.applyBayesTheorem(prior, 
				observedY, tab);
		assertNotNull(posterior);
		assertEquals(3, posterior.getStatements().size());
		
		for(Iterator i = posterior.getStatements().iterator(); i.hasNext(); ){
			DiscreteStatement statement = (DiscreteStatement)i.next();
			if(statement.getVariable().getName().equals("theta") && 
					statement.getVariable().getValue().equals("0")){
				assertEquals(0.7117400419287212, statement.getProbability(), 0.0F);
			}
			else if(statement.getVariable().getName().equals("theta") &&
					statement.getVariable().getValue().equals("1")){
				assertEquals(0.09958071278825996, statement.getProbability(), 0.0F);
			}
			else if(statement.getVariable().getName().equals("theta") && 
					statement.getVariable().getValue().equals("2")){
				assertEquals(0.18867924528301888, statement.getProbability(), 0.0F);
			}
			
			
		}
		
	}
}
