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

package edu.usc.softarch.disco.util;

// JDK imports
import java.io.File;
import java.util.List;

// DISCO imports
import edu.usc.softarch.disco.selection.bayes.ConditionalProbability;
import edu.usc.softarch.disco.selection.bayes.ConditionalProbabilityTable;
import edu.usc.softarch.disco.selection.bayes.RandomVariable;

// Junit imports
import junit.framework.TestCase;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Tests the BayesianDomainProfileReader
 * </p>.
 */
public class TestBayesianDomainProfileReader extends TestCase {

	private static final String TOTAL_VOLUME = "TotalVolume";

	public void testDomainProfReader() {
		ConditionalProbabilityTable tab = BayesianDomainProfileReader
				.readProfile(new File("src/testdata/testBayesProfile.xml"));

		assertNotNull(tab);

		RandomVariable var = new RandomVariable();
		var.setName(TOTAL_VOLUME);
		var.setValue("100 GB");

		List cProbs = tab.getConditionalProbabilities(var);
		assertNotNull(cProbs);
		
		ConditionalProbability cProb = (ConditionalProbability)cProbs.get(0);
		assertEquals(cProb.getInterestVar().getName(), "data_access_locality");
		assertEquals(cProb.getInterestVar().getValue(), "Global");
		assertEquals(cProb.getProbability(), 0.80, 0.00);
		
	}

}
