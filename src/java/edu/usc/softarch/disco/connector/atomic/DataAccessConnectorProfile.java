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

// JDK imports
import java.util.List;
import java.util.Vector;

// DISCO imports
import edu.usc.softarch.disco.structs.DataAccessAvailability;
import edu.usc.softarch.disco.structs.DataAccessCardinality;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Metadata information for the Data Access Atomic Connector type
 * </p>.
 */
public class DataAccessConnectorProfile implements AtomicConnectorProfile {
	/* const string */
	private static final String DATA_ACCESS_TYPE = "dataaccess";

	private String locality = null;

	private String lifecycle = null;

	private List accesses = null;

	private DataAccessAvailability availabilities = null;

	private boolean privateAccessibility = false;

	private boolean publicAccessibility = false;

	private DataAccessCardinality cardinality = null;

	public DataAccessConnectorProfile() {
		accesses = new Vector();
		availabilities = new DataAccessAvailability();
		cardinality = new DataAccessCardinality();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.usc.softarch.disco.structs.AtomicConnectorProfile#getType()
	 */
	public String getType() {
		return DATA_ACCESS_TYPE;
	}

	public List getAccesses() {
		return accesses;
	}

	public void setAccesses(List accesses) {
		this.accesses = accesses;
	}

	public DataAccessAvailability getAvailabilities() {
		return availabilities;
	}

	public void setAvailabilities(DataAccessAvailability availabilities) {
		this.availabilities = availabilities;
	}

	public DataAccessCardinality getCardinality() {
		return cardinality;
	}

	public void setCardinality(DataAccessCardinality cardinality) {
		this.cardinality = cardinality;
	}

	public String getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(String lifecycle) {
		this.lifecycle = lifecycle;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public boolean isPublicAccessibility() {
		return publicAccessibility;
	}

	public boolean isPrivateAccessibility() {
		return privateAccessibility;
	}

	public void setPrivateAccessibility(boolean privateAccessibility) {
		this.privateAccessibility = privateAccessibility;
	}

	public void setPublicAccessibility(boolean publicAccessibility) {
		this.publicAccessibility = publicAccessibility;
	}

}
