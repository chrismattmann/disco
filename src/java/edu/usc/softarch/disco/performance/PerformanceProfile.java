/* Copyright (c) 2007 University of Southern California.
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

package edu.usc.softarch.disco.performance;

//JDK imports
import java.util.HashMap;
import java.util.Map;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A performance profile for a distribution connector. The values stored in
 * these profiles are based on equations provided at the end of Chapter 5 in
 * Chris Mattmann's thesis.
 * </p>.
 */
public class PerformanceProfile {

  private Map<String, RawPerformanceData> rawData;

  private String connector;

  public PerformanceProfile() {
    rawData = new HashMap<String, RawPerformanceData>();
  }

  /**
   * @param connector
   * @param rawData
   */
  public PerformanceProfile(String connector, 
      Map<String, RawPerformanceData> rawData) {
    super();
    this.connector = connector;
    this.rawData = rawData;
  }

  /**
   * @return the connector
   */
  public String getConnector() {
    return connector;
  }

  /**
   * @param connector
   *          the connector to set
   */
  public void setConnector(String connector) {
    this.connector = connector;
  }

  /**
   * @return the rawData
   */
  public Map<String, RawPerformanceData> getRawData() {
    return rawData;
  }

  /**
   * @param rawData the rawData to set
   */
  public void setRawData(Map<String, RawPerformanceData> rawData) {
    this.rawData = rawData;
  }

}
