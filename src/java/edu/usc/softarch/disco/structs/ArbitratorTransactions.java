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
public class ArbitratorTransactions {
    
    private String nesting = null;
    
    private String awareness = null;
    
    private String isolation = null;
    
    public ArbitratorTransactions(){
    	
    }

	/**
	 * @return the awareness
	 */
	public String getAwareness() {
		return awareness;
	}

	/**
	 * @param awareness the awareness to set
	 */
	public void setAwareness(String awareness) {
		this.awareness = awareness;
	}

	/**
	 * @return the isolation
	 */
	public String getIsolation() {
		return isolation;
	}

	/**
	 * @param isolation the isolation to set
	 */
	public void setIsolation(String isolation) {
		this.isolation = isolation;
	}

	/**
	 * @return the nesting
	 */
	public String getNesting() {
		return nesting;
	}

	/**
	 * @param nesting the nesting to set
	 */
	public void setNesting(String nesting) {
		this.nesting = nesting;
	}
    
    
}
