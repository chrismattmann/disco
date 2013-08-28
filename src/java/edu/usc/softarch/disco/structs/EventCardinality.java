//Copyright (c) 2006, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
//$Id$

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
public class EventCardinality {
	public static final String MANY = "*";
	
	public static final String SINGLETON = "1";
	
	private String producers = null;
	
	private String observers = null;
	
	private String producerCardinality = null;
	
	private String observerCardinality = null;
	
	private List eventPatterns;

	public EventCardinality(){
          eventPatterns = new Vector();
          /*10523
          10517
          10532
          12429*/
	}

	public List getEventPatterns() {
		return eventPatterns;
	}


	public void setEventPatterns(List eventPatterns) {
		this.eventPatterns = eventPatterns;
	}


	public String getObservers() {
		return observers;
	}


	public void setObservers(String observers) {
		this.observers = observers;
	}


	public String getProducers() {
		return producers;
	}


	public void setProducers(String producers) {
		this.producers = producers;
	}

	public String getObserverCardinality() {
		return observerCardinality;
	}

	public void setObserverCardinality(String observerCardinality) {
		this.observerCardinality = observerCardinality;
	}

	public String getProducerCardinality() {
		return producerCardinality;
	}

	public void setProducerCardinality(String producerCardinality) {
		this.producerCardinality = producerCardinality;
	}

}
