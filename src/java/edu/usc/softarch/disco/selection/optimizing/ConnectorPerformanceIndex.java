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

package edu.usc.softarch.disco.selection.optimizing;

// DISCO imports
import edu.usc.softarch.disco.selection.ConnectorRank;

/**
 * @author rezam
 * @version $Revision$
 * 
 * <p>
 * A Connector Performance Score.
 * </p>.
 */
public class ConnectorPerformanceIndex {

  private float indexConsistency = 0;

  private float indexDependability = 0;

  private float indexEfficiency = 0;

  private float indexScalability = 0;

  private float overallIndex = 0;

  /* the name of the connector */
  private String connectorName = null;

  public ConnectorPerformanceIndex(String name) {
    this.connectorName = name;
  }

  public ConnectorPerformanceIndex(String name, float overallIndex) {
    this.connectorName = name;
    this.overallIndex = overallIndex;
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
  public ConnectorRank getRank() {
    ConnectorRank rank = new ConnectorRank(connectorName, overallIndex);
    return rank;
  }

  public String toString() {
    StringBuffer rStr = new StringBuffer();
    rStr.append(this.connectorName);
    rStr.append("=");
    rStr.append(this.indexConsistency + "," + indexDependability + ","
        + indexEfficiency + "," + indexScalability);
    return rStr.toString();
  }
}
