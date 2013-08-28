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

package edu.usc.softarch.disco.connector.atomic;

// DISCO imports
import edu.usc.softarch.disco.structs.ProcedureCallAccessibility;
import edu.usc.softarch.disco.structs.ProcedureCallCardinality;
import edu.usc.softarch.disco.structs.ProcedureCallEntryPoint;
import edu.usc.softarch.disco.structs.ProcedureCallInvocation;
import edu.usc.softarch.disco.structs.ProcedureCallParameters;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Metadata information for the Procedure Call Atomic Connector type
 * </p>.
 */
public class ProcedureCallConnectorProfile implements AtomicConnectorProfile {

	private String PROC_TYPE = "procedurecall";

	private ProcedureCallParameters params = null;

	private ProcedureCallEntryPoint entryPoint = null;

	private ProcedureCallInvocation invocation = null;

	private ProcedureCallCardinality cardinality = null;

	private ProcedureCallAccessibility accessibility = null;
	
	private String synchronicity = null;

	public ProcedureCallConnectorProfile() {
		this.params = new ProcedureCallParameters();
		this.entryPoint = new ProcedureCallEntryPoint();
		this.cardinality = new ProcedureCallCardinality();
		this.accessibility = new ProcedureCallAccessibility();
		this.invocation = new ProcedureCallInvocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.usc.softarch.disco.structs.AtomicConnectorProfile#getType()
	 */
	public String getType() {
		return PROC_TYPE;
	}

	/**
	 * @return the accessibility
	 */
	public ProcedureCallAccessibility getAccessibility() {
		return accessibility;
	}

	/**
	 * @param accessibility
	 *            the accessibility to set
	 */
	public void setAccessibility(ProcedureCallAccessibility accessibility) {
		this.accessibility = accessibility;
	}

	/**
	 * @return the cardinality
	 */
	public ProcedureCallCardinality getCardinality() {
		return cardinality;
	}

	/**
	 * @param cardinality
	 *            the cardinality to set
	 */
	public void setCardinality(ProcedureCallCardinality cardinality) {
		this.cardinality = cardinality;
	}

	/**
	 * @return the entryPoint
	 */
	public ProcedureCallEntryPoint getEntryPoint() {
		return entryPoint;
	}

	/**
	 * @param entryPoint
	 *            the entryPoint to set
	 */
	public void setEntryPoint(ProcedureCallEntryPoint entryPoint) {
		this.entryPoint = entryPoint;
	}

	/**
	 * @return the invocation
	 */
	public ProcedureCallInvocation getInvocation() {
		return invocation;
	}

	/**
	 * @param invocation
	 *            the invocation to set
	 */
	public void setInvocation(ProcedureCallInvocation invocation) {
		this.invocation = invocation;
	}

	/**
	 * @return the params
	 */
	public ProcedureCallParameters getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(ProcedureCallParameters params) {
		this.params = params;
	}

	/**
	 * @return the synchronicity
	 */
	public String getSynchronicity() {
		return synchronicity;
	}

	/**
	 * @param synchronicity the synchronicity to set
	 */
	public void setSynchronicity(String synchronicity) {
		this.synchronicity = synchronicity;
	}

}
