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

/**
 * @author mattmann
 * @version $Revision$
 *
 * <p>Describe your class here</p>.
 */
public class ConnectorRank {
	
	// ranks are between 0 and 1, exclusive
	private double rank = 0.0F;
	
	/* the name of the connector */
	private String connectorName = null;

	public ConnectorRank(){}
	
	public ConnectorRank(String name, double rank){
		this.connectorName = name;
		this.rank = rank;		
	}

	/**
	 * @return the connectorName
	 */
	public String getConnectorName() {
		return connectorName;
	}

	/**
	 * @param connectorName the connectorName to set
	 */
	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

	/**
	 * @return the rank
	 */
	public double getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(double rank) {
		this.rank = rank;
	}
	
	public String toString(){
		StringBuffer rStr = new StringBuffer();
		rStr.append(this.connectorName);
		rStr.append("=");
		rStr.append(this.rank);		
		return rStr.toString();
	}
}
