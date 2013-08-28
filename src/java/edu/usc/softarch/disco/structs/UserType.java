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
public class UserType {
	
	public static final int CONSUMER = -9383939;
	
	public static final int PRODUCER = -9388888;
	
	private boolean automated = false;
	
	private int type = -1;
	
	private long numInType = 0L;
	
	public UserType(){
		
	}

	/**
	 * @return the automated
	 */
	public boolean isAutomated() {
		return automated;
	}

	/**
	 * @param automated the automated to set
	 */
	public void setAutomated(boolean automated) {
		this.automated = automated;
	}

	/**
	 * @return the numInType
	 */
	public long getNumInType() {
		return numInType;
	}

	/**
	 * @param numInType the numInType to set
	 */
	public void setNumInType(long numInType) {
		this.numInType = numInType;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	

}
