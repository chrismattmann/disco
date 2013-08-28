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

//JDK imports
import java.util.List;
import java.util.Vector;

/**
 * @author mattmann
 * @version $Revision$
 *
 * <p>Arbirator Security information.</p>.
 */
public class ArbitratorSecurity {
	
	private String authentication = null;
	private List authorizations = null;
	private List privacies = null;
	private String integrity = null;
	private String durability = null;
	private boolean required = false;
  
	public ArbitratorSecurity(){
		authorizations = new Vector();
		privacies = new Vector();
	}

	/**
	 * @return the authentication
	 */
	public String getAuthentication() {
		return authentication;
	}

	/**
	 * @param authentication the authentication to set
	 */
	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	/**
	 * @return the authorizations
	 */
	public List getAuthorizations() {
		return authorizations;
	}

	/**
	 * @param authorizations the authorizations to set
	 */
	public void setAuthorizations(List authorizations) {
		this.authorizations = authorizations;
	}

	/**
	 * @return the durability
	 */
	public String getDurability() {
		return durability;
	}

	/**
	 * @param durability the durability to set
	 */
	public void setDurability(String durability) {
		this.durability = durability;
	}

	/**
	 * @return the integrity
	 */
	public String getIntegrity() {
		return integrity;
	}

	/**
	 * @param integrity the integrity to set
	 */
	public void setIntegrity(String integrity) {
		this.integrity = integrity;
	}

	/**
	 * @return the privacies
	 */
	public List getPrivacies() {
		return privacies;
	}

	/**
	 * @param privacies the privacies to set
	 */
	public void setPrivacies(List privacies) {
		this.privacies = privacies;
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	
}