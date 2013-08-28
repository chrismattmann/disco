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

/**
 * @author mattmann
 * @version $Revision$
 *
 * <p>Describe your class here</p>.
 */
public class PerformanceRequirements {

	private String consistency = null;
	
	private String efficiency = null;
	
	private String dependability = null;
	
	private String scalability = null;
	
	public PerformanceRequirements(){
	}

  /**
   * @return the consistency
   */
  public String getConsistency() {
    return consistency;
  }

  /**
   * @param consistency the consistency to set
   */
  public void setConsistency(String consistency) {
    this.consistency = consistency;
  }

  /**
   * @return the dependability
   */
  public String getDependability() {
    return dependability;
  }

  /**
   * @param dependability the dependability to set
   */
  public void setDependability(String dependability) {
    this.dependability = dependability;
  }

  /**
   * @return the efficiency
   */
  public String getEfficiency() {
    return efficiency;
  }

  /**
   * @param efficiency the efficiency to set
   */
  public void setEfficiency(String efficiency) {
    this.efficiency = efficiency;
  }

  /**
   * @return the scalability
   */
  public String getScalability() {
    return scalability;
  }

  /**
   * @param scalability the scalability to set
   */
  public void setScalability(String scalability) {
    this.scalability = scalability;
  }
  
}
