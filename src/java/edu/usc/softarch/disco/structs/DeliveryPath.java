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
public class DeliveryPath {
	
	private boolean staticPath = false;
	
	private boolean dynamicPath = false;
	
	private boolean cachedPath = false;
	
	public DeliveryPath(){}

	/**
	 * @return the cachedPath
	 */
	public boolean isCachedPath() {
		return cachedPath;
	}

	/**
	 * @param cachedPath the cachedPath to set
	 */
	public void setCachedPath(boolean cachedPath) {
		this.cachedPath = cachedPath;
	}

	/**
	 * @return the dynamicPath
	 */
	public boolean isDynamicPath() {
		return dynamicPath;
	}

	/**
	 * @param dynamicPath the dynamicPath to set
	 */
	public void setDynamicPath(boolean dynamicPath) {
		this.dynamicPath = dynamicPath;
	}

	/**
	 * @return the staticPath
	 */
	public boolean isStaticPath() {
		return staticPath;
	}

	/**
	 * @param staticPath the staticPath to set
	 */
	public void setStaticPath(boolean staticPath) {
		this.staticPath = staticPath;
	}
	
	

}
