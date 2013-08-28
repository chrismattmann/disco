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

/**
 * @author mattmann
 * @version $Revision$
 *
 * <p>Describe your class here</p>.
 */
public class DeliverySchedule {
	
	private ScenarioValue numberOfIntervals = null;
	
	private ScenarioValue volumePerInterval = null;
	
	private TimeContract intervalTiming = null;
	
	public DeliverySchedule(){
		intervalTiming = new TimeContract();
	}

	/**
	 * @return the intervalTiming
	 */
	public TimeContract getIntervalTiming() {
		return intervalTiming;
	}

	/**
	 * @param intervalTiming the intervalTiming to set
	 */
	public void setIntervalTiming(TimeContract intervalTiming) {
		this.intervalTiming = intervalTiming;
	}

  /**
   * @return the numberOfIntervals
   */
  public ScenarioValue getNumberOfIntervals() {
    return numberOfIntervals;
  }

  /**
   * @param numberOfIntervals the numberOfIntervals to set
   */
  public void setNumberOfIntervals(ScenarioValue numberOfIntervals) {
    this.numberOfIntervals = numberOfIntervals;
  }

  /**
   * @return the volumePerInterval
   */
  public ScenarioValue getVolumePerInterval() {
    return volumePerInterval;
  }

  /**
   * @param volumePerInterval the volumePerInterval to set
   */
  public void setVolumePerInterval(ScenarioValue volumePerInterval) {
    this.volumePerInterval = volumePerInterval;
  }

	
	

}
