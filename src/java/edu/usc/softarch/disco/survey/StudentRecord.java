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

package edu.usc.softarch.disco.survey;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A record from the {@link StudentDatabase}.
 * </p>.
 */
public class StudentRecord {

  private String firstName = null;

  private String lastName = null;

  private String status = null;

  private int scenarioSet = -1;

  private final static int scenarioMultiplier = 10;

  public StudentRecord() {

  }

  /**
   * @param firstName
   * @param lastName
   * @param status
   * @param scenarioSet
   */
  public StudentRecord(String firstName, String lastName, String status,
      int scenarioSet) {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
    this.status = status;
    this.scenarioSet = scenarioSet;
  }

  /**
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName
   *          the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName
   *          the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the scenarioSet
   */
  public int getScenarioSet() {
    return scenarioSet;
  }

  /**
   * @param scenarioSet
   *          the scenarioSet to set
   */
  public void setScenarioSet(int scenarioSet) {
    this.scenarioSet = scenarioSet;
  }

  /**
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param status
   *          the status to set
   */
  public void setStatus(String status) {
    this.status = status;
  }

  public int getStartingScenario() {
    return (((this.scenarioSet - 1) * scenarioMultiplier) + 1);
  }

}
