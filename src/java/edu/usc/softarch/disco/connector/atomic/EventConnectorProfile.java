//Copyright (c) 2006, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
//$Id$

package edu.usc.softarch.disco.connector.atomic;

//DISCO imports
import edu.usc.softarch.disco.structs.EventCardinality;
import edu.usc.softarch.disco.structs.Mode;
import edu.usc.softarch.disco.structs.Priority;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Metadata information for the Event Atomic Connector type
 * </p>.
 */
public class EventConnectorProfile implements AtomicConnectorProfile {
	private static final String EVENT_TYPE = "event";

	private EventCardinality cardinality = null;

	private String delivery = null;

	private Priority priority = null;

	private String synchronicity = null;

	private String notification = null;

	private String causality = null;

	private Mode mode = null;

	public EventConnectorProfile() {
		cardinality = new EventCardinality();
		priority = new Priority();
		mode = new Mode();
	}

	public EventCardinality getCardinality() {
		return cardinality;
	}

	public void setCardinality(EventCardinality cardinality) {
		this.cardinality = cardinality;
	}

	public String getCausality() {
		return causality;
	}

	public void setCausality(String causality) {
		this.causality = causality;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public String getSynchronicity() {
		return synchronicity;
	}

	public void setSynchronicity(String synchronicity) {
		this.synchronicity = synchronicity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.usc.softarch.disco.structs.AtomicConnector#getType()
	 */
	public String getType() {
		return EVENT_TYPE;
	}

}
