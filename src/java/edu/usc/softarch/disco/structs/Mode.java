//Copyright (c) 2006, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
//$Id$

package edu.usc.softarch.disco.structs;

import java.util.List;
import java.util.Vector;

/**
 * @author mattmann
 * @version $Revision$
 *
 * <p>Describe your class here</p>.
 */
public class Mode {
	private String type = null;
	
	private List softwares = null;
	
	public Mode(){
		softwares = new Vector();
	}

	public List getSoftwares() {
		return softwares;
	}

	public void setSoftwares(List softwares) {
		this.softwares = softwares;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
