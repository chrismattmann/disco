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

package edu.usc.softarch.disco.connector.dcp;

// DISCO imports
import edu.usc.softarch.disco.connector.atomic.DataAccessConnectorProfile;
import edu.usc.softarch.disco.connector.atomic.DistributorConnectorProfile;
import edu.usc.softarch.disco.connector.atomic.ProcedureCallConnectorProfile;
import edu.usc.softarch.disco.connector.atomic.StreamConnectorProfile;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Client/Server family of data distribution connector profile.
 * </p>
 */
public class ClientServerDistributionConnectorProfile implements
		DistributionConnectorProfile {

	private String connectorName = null;

	private ProcedureCallConnectorProfile procProfile = null;

	private DataAccessConnectorProfile dataAccessProfile = null;

	private StreamConnectorProfile streamProfile = null;

	private DistributorConnectorProfile distributorProfile = null;

	public ClientServerDistributionConnectorProfile(String name) {
		this.connectorName = name;
		this.procProfile = new ProcedureCallConnectorProfile();
		this.dataAccessProfile = new DataAccessConnectorProfile();
		this.streamProfile = new StreamConnectorProfile();
		this.distributorProfile = new DistributorConnectorProfile();

	}

	/**
	 * @return the connectorName
	 */
	public String getConnectorName() {
		return connectorName;
	}

	/**
	 * @param connectorName
	 *            the connectorName to set
	 */
	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

	/**
	 * @return the dataAccessProfile
	 */
	public DataAccessConnectorProfile getDataAccessProfile() {
		return dataAccessProfile;
	}

	/**
	 * @param dataAccessProfile
	 *            the dataAccessProfile to set
	 */
	public void setDataAccessProfile(
			DataAccessConnectorProfile dataAccessProfile) {
		this.dataAccessProfile = dataAccessProfile;
	}

	/**
	 * @return the distributorProfile
	 */
	public DistributorConnectorProfile getDistributorProfile() {
		return distributorProfile;
	}

	/**
	 * @param distributorProfile
	 *            the distributorProfile to set
	 */
	public void setDistributorProfile(
			DistributorConnectorProfile distributorProfile) {
		this.distributorProfile = distributorProfile;
	}

	/**
	 * @return the arbProfile
	 */
	public ProcedureCallConnectorProfile getProcedureCallProfile() {
		return procProfile;
	}

	/**
	 * @param arbProfile
	 *            the arbProfile to set
	 */
	public void setProcedureCallProfile(
			ProcedureCallConnectorProfile procProfile) {
		this.procProfile = procProfile;
	}

	/**
	 * @return the streamProfile
	 */
	public StreamConnectorProfile getStreamProfile() {
		return streamProfile;
	}

	/**
	 * @param streamProfile
	 *            the streamProfile to set
	 */
	public void setStreamProfile(StreamConnectorProfile streamProfile) {
		this.streamProfile = streamProfile;
	}

	/* (non-Javadoc)
	 * @see edu.usc.softarch.disco.connector.dcp.DistributionConnectorProfile#getType()
	 */
	public String getType() {
		return CS_TYPE;
	}

}
