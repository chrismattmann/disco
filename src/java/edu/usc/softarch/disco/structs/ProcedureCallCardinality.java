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
public class ProcedureCallCardinality {
	
	private String senders = null;
	
	private String receivers = null;
	
	public ProcedureCallCardinality(){
		
	}

	/**
	 * @return the receivers
	 */
	public String getReceivers() {
		return receivers;
	}

	/**
	 * @param receivers the receivers to set
	 */
	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}

	/**
	 * @return the senders
	 */
	public String getSenders() {
		return senders;
	}

	/**
	 * @param senders the senders to set
	 */
	public void setSenders(String senders) {
		this.senders = senders;
	}
	
	

}
