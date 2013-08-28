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

// JDK imports
import java.util.List;
import java.util.Vector;

/**
 * @author mattmann
 * @version $Revision$
 *
 * <p>Describe your class here</p>.
 */
public class DistributionScenario {
	
	private String name = null;
	
	private ScenarioValue totalVolume = null;
	
	private DeliverySchedule delivSchedule = null;
	
	private List accessPolicies = null;
	
	private List geoDistribution = null;
	
	private PerformanceRequirements performanceReqs = null;
	
	private ScenarioValue numUsers = null;
	
	private ScenarioValue numUserTypes = null;
	
	private List dataTypes = null;
	
	public DistributionScenario(){
		this.delivSchedule = new DeliverySchedule();
		this.accessPolicies = new Vector();
		this.geoDistribution = new Vector();
		this.performanceReqs = new PerformanceRequirements();
		this.dataTypes = new Vector();
		this.totalVolume = new ScenarioValue();
		this.numUsers = new ScenarioValue();
		this.numUserTypes = new ScenarioValue();
	}

	/**
	 * @return the accessPolicies
	 */
	public List getAccessPolicies() {
		return accessPolicies;
	}

	/**
	 * @param accessPolicies the accessPolicies to set
	 */
	public void setAccessPolicies(List accessPolicies) {
		this.accessPolicies = accessPolicies;
	}

	/**
	 * @return the dataTypes
	 */
	public List getDataTypes() {
		return dataTypes;
	}

	/**
	 * @param dataTypes the dataTypes to set
	 */
	public void setDataTypes(List dataTypes) {
		this.dataTypes = dataTypes;
	}

	/**
	 * @return the delivSchedule
	 */
	public DeliverySchedule getDelivSchedule() {
		return delivSchedule;
	}

	/**
	 * @param delivSchedule the delivSchedule to set
	 */
	public void setDelivSchedule(DeliverySchedule delivSchedule) {
		this.delivSchedule = delivSchedule;
	}

	/**
	 * @return the geoDistribution
	 */
	public List getGeoDistribution() {
		return geoDistribution;
	}

	/**
	 * @param geoDistribution the geoDistribution to set
	 */
	public void setGeoDistribution(List geoDistribution) {
		this.geoDistribution = geoDistribution;
	}

	/**
	 * @return the performanceReqs
	 */
	public PerformanceRequirements getPerformanceReqs() {
		return performanceReqs;
	}

	/**
	 * @param performanceReqs the performanceReqs to set
	 */
	public void setPerformanceReqs(PerformanceRequirements performanceReqs) {
		this.performanceReqs = performanceReqs;
	}

  /**
   * @return the numUsers
   */
  public ScenarioValue getNumUsers() {
    return numUsers;
  }

  /**
   * @param numUsers the numUsers to set
   */
  public void setNumUsers(ScenarioValue numUsers) {
    this.numUsers = numUsers;
  }

  /**
   * @return the totalVolume
   */
  public ScenarioValue getTotalVolume() {
    return totalVolume;
  }

  /**
   * @param totalVolume the totalVolume to set
   */
  public void setTotalVolume(ScenarioValue totalVolume) {
    this.totalVolume = totalVolume;
  }

  /**
   * @return the numUserTypes
   */
  public ScenarioValue getNumUserTypes() {
    return numUserTypes;
  }

  /**
   * @param numUserTypes the numUserTypes to set
   */
  public void setNumUserTypes(ScenarioValue numUserTypes) {
    this.numUserTypes = numUserTypes;
  }

    /**
      * @return the name (or filename if read from disk) of the scenario
      */
    public String getName() {
      return name;
    }

    /**
      * @param n the name (or filename if read from disk) of the scenario
      */
    public void setName(String n) {
      name = n;
    }

    /**
      * @return the name (or filename if read from disk) of the scenario
      */
    public String toString() {
      if (name != null)
          return name;
      else return super.toString();
    }

}
