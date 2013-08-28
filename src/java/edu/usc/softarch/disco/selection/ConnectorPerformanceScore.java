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

// DISCO imports
import edu.usc.softarch.disco.selection.scorebased.ObjectiveFunction;

/**
 * @author rezam
 * @version $Revision$
 * 
 * <p>
 * A Connector Performance Score.
 * </p>.
 */
public class ConnectorPerformanceScore {

  private float scoreConsistency = 0;

  private float scoreDependability = 0;

  private float scoreEfficiency = 0;

  private float scoreScalability = 0;

  /* the name of the connector */
  private String connectorName = null;

  public ConnectorPerformanceScore(String name) {
    this.connectorName = name;
  }

  /**
   * @return the connectorName
   */
  public String getConnectorName() {
    return connectorName;
  }

  /**
   * @param connectorName
   *          the connectorName to set
   */
  public void setConnectorName(String connectorName) {
    this.connectorName = connectorName;
  }

  /**
   * @return the rank
   */
  public ConnectorRank getRank(ObjectiveFunction objectiveFunction) {
    float totalScore = objectiveFunction.calculateTotalScore(scoreConsistency,
        scoreDependability, scoreEfficiency, scoreScalability);
    ConnectorRank rank = new ConnectorRank(connectorName, totalScore);
    return rank;
  }

  public void updateScoreConsistency(float increment) {
    this.scoreConsistency += increment;
  }

  public void updateScoreDependability(float increment) {
    this.scoreDependability += increment;
  }

  public void updateScoreEfficiency(float increment) {
    this.scoreEfficiency += increment;
  }

  public void updateScoreScalability(float increment) {
    this.scoreScalability += increment;
  }

  public String toString() {
    StringBuffer rStr = new StringBuffer();
    rStr.append(this.connectorName);
    rStr.append("=");
    rStr.append(this.scoreConsistency + "," + scoreDependability + ","
        + scoreEfficiency + "," + scoreScalability);
    return rStr.toString();
  }
}
