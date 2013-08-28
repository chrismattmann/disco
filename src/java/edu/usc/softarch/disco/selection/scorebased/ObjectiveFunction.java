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

package edu.usc.softarch.disco.selection.scorebased;

/**
 * @author rezam
 * @version $Revision$
 * 
 * <p>
 * A data structure representing the objective function for a score-based
 * connector selection
 * </p>.
 */
public class ObjectiveFunction {

  private float coefConsistency = 0;

  private float coefEfficiency = 0;

  private float coefDependability = 0;

  private float coefScalability = 0;

  public ObjectiveFunction(float coefConsistency, float coefEfficiency,
      float coefDependability, float coefScalability) {
    this.coefConsistency = coefConsistency;
    this.coefDependability = coefDependability;
    this.coefEfficiency = coefEfficiency;
    this.coefScalability = coefScalability;
  }

  public float calculateTotalScore(float scoreConsistency,
      float scoreEfficiency, float scoreDependability, float scoreScalability) {

    float totalScore = coefConsistency * scoreConsistency + coefDependability
        * scoreDependability + coefEfficiency * scoreEfficiency
        + coefScalability * scoreScalability;
    return totalScore;
  }

}
