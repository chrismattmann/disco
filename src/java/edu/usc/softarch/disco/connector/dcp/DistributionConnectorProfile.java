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

package edu.usc.softarch.disco.connector.dcp;

import edu.usc.softarch.disco.connector.atomic.DataAccessConnectorProfile;
import edu.usc.softarch.disco.connector.atomic.DistributorConnectorProfile;
import edu.usc.softarch.disco.connector.atomic.StreamConnectorProfile;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A Distribution Connector Profile (DCP) for use in the DISCO framework.
 * </p>.
 */
public interface DistributionConnectorProfile {
	
	public static final String P2P_TYPE = "P2P";
	
	public static final String GRID_TYPE = "grid";
	
	public static final String CS_TYPE = "Client/Server";
	
	public static final String EVENT_TYPE = "Event";
	
	public String getType();
	
	public String getConnectorName();
	
	public DataAccessConnectorProfile getDataAccessProfile();
	
	public StreamConnectorProfile getStreamProfile();
	
	public DistributorConnectorProfile getDistributorProfile();

}
