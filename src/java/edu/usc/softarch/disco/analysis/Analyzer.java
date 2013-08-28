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

package edu.usc.softarch.disco.analysis;

// DISCO imports
import edu.usc.softarch.disco.structs.AnswerKey;
import edu.usc.softarch.disco.structs.DistributionScenarioResult;
import edu.usc.softarch.disco.util.Configurable;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * An interface that performs analysis on a {@link DistributionScenarioResult}.
 * The analysis generates precision/recall information, including error rate and
 * accuracy.
 * </p>.
 */
public interface Analyzer extends Configurable{

  /**
   * Generate a set of good connectors and a set of bad connectors for the
   * {@link DistributionScenario} provided with the
   * {@link DistributionScenarioResult} parameter.
   * 
   * @param result
   *          The result of connector selection, a {@link DistributionScenario},
   *          and a {@link List} of {@link ConnectorRank}s.
   * @return An {@link AnswerKey} containing a set of good connectors, and a set
   *         of bad connectors, for the given scenario.
   */
  public AnswerKey analyzeScenarioResult(DistributionScenarioResult result);

}
