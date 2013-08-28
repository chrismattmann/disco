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

package edu.usc.softarch.disco.selection;

// JDK imports
import java.io.File;
import java.util.List;

// DISCO imports
import edu.usc.softarch.disco.selection.bayes.BayesUtil;
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.util.DistributionScenarioReader;

// Junit imports
import junit.framework.TestCase;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Test suite for the {@link BayesianSelector}.
 * </p>.
 */
public class TestBayesianSelector extends TestCase {
  private BayesianSelector selector = null;

  public TestBayesianSelector() {
    selector = new BayesianSelector("./src/bayesian/bayesianDomainProfile.xml",
        "./src/conf/valRange.xml");

  }

  public void testGetObservedVariablesFromScenario() {
    DistributionScenario scenario = DistributionScenarioReader
        .readScenario(new File("./src/examples/exDistScenario.xml"));
    List obsVars = BayesUtil.getObservationsFromDistributionScenario(scenario);
    assertNotNull(obsVars);
    assertEquals(obsVars.size(), 4);

  }

}
