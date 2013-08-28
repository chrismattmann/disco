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
 * <p>
 * Describe your class here
 * </p>.
 */
public class RangedValue {

  private String minValue;

  private String maxValue;

  private boolean minInclusive;

  private boolean maxInclusive;

  /**
   * Default Constructor.
   * 
   */
  public RangedValue() {
  }

  /**
   * Constructs a new RangedValue.
   * 
   * @param min
   *          Min value in the range (use null if open ended)
   * @param max
   *          Max value in the range (use null if open ended)
   * @param minIncl
   *          The minimum value is included in the range.
   * @param maxIncl
   *          The maximum value is included in the range.
   */
  public RangedValue(String min, String max, boolean minIncl, boolean maxIncl) {
    this.minValue = min;
    this.minInclusive = minIncl;
    this.maxValue = max;
    this.maxInclusive = maxIncl;
  }

  /**
   * @return the maxInclusive
   */
  public boolean isMaxInclusive() {
    return maxInclusive;
  }

  /**
   * @param maxInclusive
   *          the maxInclusive to set
   */
  public void setMaxInclusive(boolean maxInclusive) {
    this.maxInclusive = maxInclusive;
  }

  /**
   * @return the maxValue
   */
  public String getMaxValue() {
    return maxValue;
  }

  /**
   * @param maxValue
   *          the maxValue to set
   */
  public void setMaxValue(String maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * @return the minInclusive
   */
  public boolean isMinInclusive() {
    return minInclusive;
  }

  /**
   * @param minInclusive
   *          the minInclusive to set
   */
  public void setMinInclusive(boolean minInclusive) {
    this.minInclusive = minInclusive;
  }

  /**
   * @return the minValue
   */
  public String getMinValue() {
    return minValue;
  }

  /**
   * @param minValue
   *          the minValue to set
   */
  public void setMinValue(String minValue) {
    this.minValue = minValue;
  }

}
