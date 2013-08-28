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

// DISCO imports
import edu.usc.softarch.disco.structs.DistributionScenario;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Raw performance data associated with a connector for a particular
 * {@link DistributionScenario}
 * </p>.
 */
public class RawPerformanceData {

  /* the connector that this raw data is assoc. with */
  private String connName = null;

  /* the scenario in which this raw data came from */
  private DistributionScenario scenario = null;

  /* efficiency */
  private long connBps = 0L;

  private long connAvgMem = 0L;

  /* consistency */
  private long connBytesActualTransfer = 0L;

  /* dependability */
  private int connNumFaults = 0;

  /**
   * @param connName
   * @param scenario
   * @param connBps
   * @param connAvgMem
   * @param connBytesActualTransfer
   * @param connNumFaults
   */
  public RawPerformanceData(String connName, DistributionScenario scenario,
      long connBps, long connAvgMem, long connBytesActualTransfer,
      int connNumFaults) {
    super();
    this.connName = connName;
    this.scenario = scenario;
    this.connBps = connBps;
    this.connAvgMem = connAvgMem;
    this.connBytesActualTransfer = connBytesActualTransfer;
    this.connNumFaults = connNumFaults;
  }

  public RawPerformanceData() {

  }

  /**
   * @return the connAvgMem
   */
  public long getConnAvgMem() {
    return connAvgMem;
  }

  /**
   * @param connAvgMem
   *          the connAvgMem to set
   */
  public void setConnAvgMem(long connAvgMem) {
    this.connAvgMem = connAvgMem;
  }

  /**
   * @return the connBps
   */
  public long getConnBps() {
    return connBps;
  }

  /**
   * @param connBps
   *          the connBps to set
   */
  public void setConnBps(long connBps) {
    this.connBps = connBps;
  }

  /**
   * @return the connBytesActualTransfer
   */
  public long getConnBytesActualTransfer() {
    return connBytesActualTransfer;
  }

  /**
   * @param connBytesActualTransfer
   *          the connBytesActualTransfer to set
   */
  public void setConnBytesActualTransfer(long connBytesActualTransfer) {
    this.connBytesActualTransfer = connBytesActualTransfer;
  }

  /**
   * @return the connName
   */
  public String getConnName() {
    return connName;
  }

  /**
   * @param connName
   *          the connName to set
   */
  public void setConnName(String connName) {
    this.connName = connName;
  }

  /**
   * @return the connNumFaults
   */
  public int getConnNumFaults() {
    return connNumFaults;
  }

  /**
   * @param connNumFaults
   *          the connNumFaults to set
   */
  public void setConnNumFaults(int connNumFaults) {
    this.connNumFaults = connNumFaults;
  }

  /**
   * @return the scenario
   */
  public DistributionScenario getScenario() {
    return scenario;
  }

  /**
   * @param scenario
   *          the scenario to set
   */
  public void setScenario(DistributionScenario scenario) {
    this.scenario = scenario;
  }

}
