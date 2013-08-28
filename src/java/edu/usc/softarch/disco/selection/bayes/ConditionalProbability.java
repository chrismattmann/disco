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

package edu.usc.softarch.disco.selection.bayes;

/**
 * @author mattmann
 * @version $Revision$
 *
 * <p>Describe your class here</p>.
 */
public class ConditionalProbability {
	
	private RandomVariable sampleStatisticObservedValue = null;
	
	private RandomVariable interestVar = null;
	
	private double probability = 0.0F;
	
	public ConditionalProbability(){
		sampleStatisticObservedValue = new RandomVariable();
		interestVar = new RandomVariable();
	}

	/**
	 * @return the sampleStatisticObservedValue
	 */
	public RandomVariable getSampleStatisticObservedValue() {
		return sampleStatisticObservedValue;
	}

	/**
	 * @param sampleStatisticObservedValue the sampleStatisticObservedValue to set
	 */
	public void setSampleStatisticObservedValue(RandomVariable sampleStatisticObservedValue) {
		this.sampleStatisticObservedValue = sampleStatisticObservedValue;
	}
	
	public String toString() {
		StringBuffer rStr = new StringBuffer("P(");
		rStr.append(this.sampleStatisticObservedValue);
		rStr.append("|");
		if(this.interestVar != null){
			rStr.append(interestVar);
			rStr.append(") = "+this.probability);
		}
		
		return rStr.toString();
	}

	/**
	 * @return the interestVar
	 */
	public RandomVariable getInterestVar() {
		return interestVar;
	}

	/**
	 * @param interestVar the interestVar to set
	 */
	public void setInterestVar(RandomVariable interestVar) {
		this.interestVar = interestVar;
	}

	/**
	 * @return the probability
	 */
	public double getProbability() {
		return probability;
	}

	/**
	 * @param probability the probability to set
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	
}
