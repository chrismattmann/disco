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
import edu.usc.softarch.disco.structs.StreamCardinality;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Metadata information for the Stream Atomic Connector type
 * </p>.
 */
public class StreamConnectorProfile implements AtomicConnectorProfile {

	private List deliveries = null;

	private String bounds = null;

	private String buffering = null;

	private String throughput = null;

	private String state = null;

	private String identity = null;

	private List localities = null;

	private String synchronicity = null;

	private List formats = null;

	private StreamCardinality cardinality = null;

	public static final String STREAM_TYPE = "stream";

	public StreamConnectorProfile() {
		deliveries = new Vector();
		localities = new Vector();
		formats = new Vector();
		cardinality = new StreamCardinality();
	}

	/**
	 * @return the bounds
	 */
	public String getBounds() {
		return bounds;
	}

	/**
	 * @param bounds
	 *            the bounds to set
	 */
	public void setBounds(String bounds) {
		this.bounds = bounds;
	}

	/**
	 * @return the buffering
	 */
	public String getBuffering() {
		return buffering;
	}

	/**
	 * @param buffering
	 *            the buffering to set
	 */
	public void setBuffering(String buffering) {
		this.buffering = buffering;
	}

	/**
	 * @return the cardinality
	 */
	public StreamCardinality getCardinality() {
		return cardinality;
	}

	/**
	 * @param cardinality
	 *            the cardinality to set
	 */
	public void setCardinality(StreamCardinality cardinality) {
		this.cardinality = cardinality;
	}

	/**
	 * @return the deliveries
	 */
	public List getDeliveries() {
		return deliveries;
	}

	/**
	 * @param deliveries
	 *            the deliveries to set
	 */
	public void setDeliveries(List deliveries) {
		this.deliveries = deliveries;
	}

	/**
	 * @return the formats
	 */
	public List getFormats() {
		return formats;
	}

	/**
	 * @param formats
	 *            the formats to set
	 */
	public void setFormats(List formats) {
		this.formats = formats;
	}

	/**
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * @param identity
	 *            the identity to set
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/**
	 * @return the localities
	 */
	public List getLocalities() {
		return localities;
	}

	/**
	 * @param localities
	 *            the localities to set
	 */
	public void setLocalities(List localities) {
		this.localities = localities;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the synchronicity
	 */
	public String getSynchronicity() {
		return synchronicity;
	}

	/**
	 * @param synchronicity
	 *            the synchronicity to set
	 */
	public void setSynchronicity(String synchronicity) {
		this.synchronicity = synchronicity;
	}

	/**
	 * @return the throughput
	 */
	public String getThroughput() {
		return throughput;
	}

	/**
	 * @param throughput
	 *            the throughput to set
	 */
	public void setThroughput(String throughput) {
		this.throughput = throughput;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.usc.softarch.disco.structs.AtomicConnectorProfile#getType()
	 */
	public String getType() {
		return STREAM_TYPE;
	}

}
