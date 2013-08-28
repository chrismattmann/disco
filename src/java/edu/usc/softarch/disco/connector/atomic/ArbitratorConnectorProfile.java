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

//DISCO imports
import edu.usc.softarch.disco.structs.ArbitratorConcurrency;
import edu.usc.softarch.disco.structs.ArbitratorSecurity;
import edu.usc.softarch.disco.structs.ArbitratorTransactions;

/**
 * @author mattmann
 * @version $Revision$
 *
 * <p>Arbitrator Atomic Connector Profile</p>.
 */
public class ArbitratorConnectorProfile implements AtomicConnectorProfile {

	private String faultHandling = null;
	
	private ArbitratorConcurrency concurrency = null;
	
	private ArbitratorTransactions transactions = null;
	
	private String scheduling = null;
	
	private ArbitratorSecurity security = null;
	
    public static final String ARBITRATOR_CONNECTOR_TYPE = "arbitrator";
	
	/**
	 * 
	 */
	public ArbitratorConnectorProfile() {
		concurrency = new ArbitratorConcurrency();
		security = new ArbitratorSecurity();
		transactions = new ArbitratorTransactions();
		
	}

	/* (non-Javadoc)
	 * @see edu.usc.softarch.disco.structs.AtomicConnectorProfile#getType()
	 */
	public String getType() {
		return ARBITRATOR_CONNECTOR_TYPE;
	}

	/**
	 * @return the concurrency
	 */
	public ArbitratorConcurrency getConcurrency() {
		return concurrency;
	}

	/**
	 * @param concurrency the concurrency to set
	 */
	public void setConcurrency(ArbitratorConcurrency concurrency) {
		this.concurrency = concurrency;
	}

	/**
	 * @return the faultHandling
	 */
	public String getFaultHandling() {
		return faultHandling;
	}

	/**
	 * @param faultHandling the faultHandling to set
	 */
	public void setFaultHandling(String faultHandling) {
		this.faultHandling = faultHandling;
	}

	/**
	 * @return the scheduling
	 */
	public String getScheduling() {
		return scheduling;
	}

	/**
	 * @param scheduling the scheduling to set
	 */
	public void setScheduling(String scheduling) {
		this.scheduling = scheduling;
	}

	/**
	 * @return the security
	 */
	public ArbitratorSecurity getSecurity() {
		return security;
	}

	/**
	 * @param security the security to set
	 */
	public void setSecurity(ArbitratorSecurity security) {
		this.security = security;
	}

	/**
	 * @return the transactions
	 */
	public ArbitratorTransactions getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions the transactions to set
	 */
	public void setTransactions(ArbitratorTransactions transactions) {
		this.transactions = transactions;
	}

}
