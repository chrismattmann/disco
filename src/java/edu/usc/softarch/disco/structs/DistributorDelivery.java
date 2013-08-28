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

import java.util.List;
import java.util.Vector;

/**
 * @author mattmann
 * @version $Revision$
 *
 * <p>Describe your class here</p>.
 */
public class DistributorDelivery {
	
	private String type = null;
	
	private List semantics = null;
	
	private List mechanisms = null;
	
	public DistributorDelivery(){
		semantics = new Vector();
		mechanisms = new Vector();
	}

	/**
	 * @return the mechanisms
	 */
	public List getMechanisms() {
		return mechanisms;
	}

	/**
	 * @param mechanisms the mechanisms to set
	 */
	public void setMechanisms(List mechanisms) {
		this.mechanisms = mechanisms;
	}

	/**
	 * @return the semantics
	 */
	public List getSemantics() {
		return semantics;
	}

	/**
	 * @param semantics the semantics to set
	 */
	public void setSemantics(List semantics) {
		this.semantics = semantics;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	

}
