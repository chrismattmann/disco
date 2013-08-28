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
 * <p>A class to represent the probability that a
 * {@link RandomVariable} is in fact a certain discrete
 * value. </p>.
 */
public class DiscreteStatement implements Statement {
	
	private RandomVariable randomVariable = null;
	
	private double probability = 0.0F;
	
	public DiscreteStatement(){}
	
	public DiscreteStatement(String name, String val, double prob){
		randomVariable = new RandomVariable();
		this.randomVariable.setName(name);
		this.randomVariable.setValue(val);
		this.probability = prob;
	}

	/* (non-Javadoc)
	 * @see edu.usc.softarch.disco.selection.bayes.Statement#getProbability()
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

	/* (non-Javadoc)
	 * @see edu.usc.softarch.disco.selection.bayes.Statement#getVariable()
	 */
	public RandomVariable getVariable() {
		return randomVariable;
	}

	/* (non-Javadoc)
	 * @see edu.usc.softarch.disco.selection.bayes.Statement#setVariable(edu.usc.softarch.disco.selection.bayes.RandomVariable)
	 */
	public void setVariable(RandomVariable var) {
		this.randomVariable = var;
		
	}
	
	public int hashCode() {
		return this.randomVariable.hashCode()
				+ new Double(this.probability).hashCode();
	}

	public boolean equals(Object o) {
		DiscreteStatement otherStatement = (DiscreteStatement) o;

		return this.randomVariable.equals(otherStatement.getVariable())
				&& this.probability == otherStatement.getProbability();

	}
	
	public String toString(){
		StringBuffer rStr = new StringBuffer();
		rStr.append("P(");
		rStr.append(randomVariable);
		rStr.append(") = ");
		rStr.append(this.probability);
		return rStr.toString();
	}

}
