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
 * <p>
 * Describe your class here
 * </p>.
 */
public class RandomVariable {

	private String name = null;

	private String value = null;

	public RandomVariable() {
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public boolean equals(Object obj) {
		RandomVariable otherRandom = (RandomVariable) obj;

  if(otherRandom == null || (otherRandom != null && 
      (otherRandom.getName() == null || 
          otherRandom.getValue() == null))){
    return false;
  }
  
		if (this.name.equals(otherRandom.getName())
				&& this.value.equals(otherRandom.getValue())) {
			return true;
		} else
			return false;
	}
	
	public int hashCode(){
		return this.name.hashCode()+this.value.hashCode();
	}
	
	public String toString(){
		StringBuffer rStr = new StringBuffer();
		rStr.append(this.getName());
		rStr.append("=");
		rStr.append(this.getValue());
		return rStr.toString();	
	}

}
