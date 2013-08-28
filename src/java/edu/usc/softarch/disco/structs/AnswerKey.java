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

package edu.usc.softarch.disco.structs;

// JDK imports
import java.util.Set;
import java.util.HashSet;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A key provided by a domain expert for a particular
 * {@link DistributionScenario}, grouping connectors as either appropriate or
 * inappropriate for the {@link DistributionScenario}
 * </p>.
 */
public class AnswerKey {

  /* the identifier pointing to the scenario that this is the answer key for. */
  private String scenarioId = null;

  private Set<String> appropriate;

  private Set<String> inappropriate;

  public AnswerKey() {
    appropriate = new HashSet<String>();
    inappropriate = new HashSet<String>();
  }

  /**
   * @param scenarioId
   * @param appropriate
   * @param inappropriate
   */
  public AnswerKey(String scenarioId, Set<String> appropriate,
      Set<String> inappropriate) {
    super();
    this.scenarioId = scenarioId;
    this.appropriate = appropriate;
    this.inappropriate = inappropriate;
  }

  /**
   * @return the appropriate
   */
  public Set<String> getAppropriate() {
    return appropriate;
  }

  /**
   * @param appropriate
   *          the appropriate to set
   */
  public void setAppropriate(Set<String> appropriate) {
    this.appropriate = appropriate;
  }

  /**
   * @return the inappropriate
   */
  public Set<String> getInappropriate() {
    return inappropriate;
  }

  /**
   * @param inappropriate
   *          the inappropriate to set
   */
  public void setInappropriate(Set<String> inappropriate) {
    this.inappropriate = inappropriate;
  }

  /**
   * @return the scenarioId
   */
  public String getScenarioId() {
    return scenarioId;
  }

  /**
   * @param scenarioId
   *          the scenarioId to set
   */
  public void setScenarioId(String scenarioId) {
    this.scenarioId = scenarioId;
  }

}
