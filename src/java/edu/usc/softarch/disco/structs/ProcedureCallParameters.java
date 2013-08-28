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

//JDK imports
import java.util.List;
import java.util.Vector;

/**
 * @author mattmann
 * @version $Revision$
 *
 * <p>Describe your class here</p>.
 */
public class ProcedureCallParameters {
	
	private List dataTransferMethods = null;
	
	private String semantics = null;
	
	private String returnValue = null;
	
	private String invocationRecord = null;
	
	public ProcedureCallParameters(){
		this.dataTransferMethods = new Vector();
	}

	/**
	 * @return the dataTransferMethods
	 */
	public List getDataTransferMethods() {
		return dataTransferMethods;
	}

	/**
	 * @param dataTransferMethods the dataTransferMethods to set
	 */
	public void setDataTransferMethods(List dataTransferMethods) {
		this.dataTransferMethods = dataTransferMethods;
	}

	/**
	 * @return the invocationRecord
	 */
	public String getInvocationRecord() {
		return invocationRecord;
	}

	/**
	 * @param invocationRecord the invocationRecord to set
	 */
	public void setInvocationRecord(String invocationRecord) {
		this.invocationRecord = invocationRecord;
	}

	/**
	 * @return the returnValue
	 */
	public String getReturnValue() {
		return returnValue;
	}

	/**
	 * @param returnValue the returnValue to set
	 */
	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	/**
	 * @return the semantics
	 */
	public String getSemantics() {
		return semantics;
	}

	/**
	 * @param semantics the semantics to set
	 */
	public void setSemantics(String semantics) {
		this.semantics = semantics;
	}
	
	

}
