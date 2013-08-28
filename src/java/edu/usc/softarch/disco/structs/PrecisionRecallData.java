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

package edu.usc.softarch.disco.structs;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Data to store true positives, true negatives, false positives, and false
 * negatives, for use in precision recall analysis.
 * </p>.
 */
public class PrecisionRecallData {

  private int TP = 0;

  private int TN = 0;

  private int FP = 0;

  private int FN = 0;

  public PrecisionRecallData() {

  }

  /**
   * @param tp
   * @param tn
   * @param fp
   * @param fn
   */
  public PrecisionRecallData(int tp, int tn, int fp, int fn) {
    super();
    TP = tp;
    TN = tn;
    FP = fp;
    FN = fn;
  }

  /**
   * @return the fN
   */
  public int getFN() {
    return FN;
  }

  /**
   * @param fn
   *          the fN to set
   */
  public void setFN(int fn) {
    FN = fn;
  }

  /**
   * @return the fP
   */
  public int getFP() {
    return FP;
  }

  /**
   * @param fp
   *          the fP to set
   */
  public void setFP(int fp) {
    FP = fp;
  }

  /**
   * @return the tN
   */
  public int getTN() {
    return TN;
  }

  /**
   * @param tn
   *          the tN to set
   */
  public void setTN(int tn) {
    TN = tn;
  }

  /**
   * @return the tP
   */
  public int getTP() {
    return TP;
  }

  /**
   * @param tp
   *          the tP to set
   */
  public void setTP(int tp) {
    TP = tp;
  }

}
