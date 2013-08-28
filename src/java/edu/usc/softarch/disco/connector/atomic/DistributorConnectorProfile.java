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
import edu.usc.softarch.disco.structs.DistributorDelivery;
import edu.usc.softarch.disco.structs.DistributorNaming;
import edu.usc.softarch.disco.structs.DistributorRouting;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Distributor Metadata information for the Distributor Atomic Connector type.
 * </p>.
 */
public class DistributorConnectorProfile implements AtomicConnectorProfile {
	public static final String DISTRIBUTOR_TYPE = "distributor";

	private DistributorNaming naming = null;

	private DistributorDelivery delivery = null;

	private DistributorRouting routing = null;

	public DistributorConnectorProfile() {
		naming = new DistributorNaming();
		delivery = new DistributorDelivery();
		routing = new DistributorRouting();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.usc.softarch.disco.structs.AtomicConnectorProfile#getType()
	 */
	public String getType() {
		// TODO Auto-generated method stub
		return DISTRIBUTOR_TYPE;
	}

	/**
	 * @return the delivery
	 */
	public DistributorDelivery getDelivery() {
		return delivery;
	}

	/**
	 * @param delivery
	 *            the delivery to set
	 */
	public void setDelivery(DistributorDelivery delivery) {
		this.delivery = delivery;
	}

	/**
	 * @return the naming
	 */
	public DistributorNaming getNaming() {
		return naming;
	}

	/**
	 * @param naming
	 *            the naming to set
	 */
	public void setNaming(DistributorNaming naming) {
		this.naming = naming;
	}

	/**
	 * @return the routing
	 */
	public DistributorRouting getRouting() {
		return routing;
	}

	/**
	 * @param routing
	 *            the routing to set
	 */
	public void setRouting(DistributorRouting routing) {
		this.routing = routing;
	}

}
