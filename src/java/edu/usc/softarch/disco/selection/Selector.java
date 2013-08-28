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

package edu.usc.softarch.disco.selection;

// JDK imports
import java.util.List;

// DISCO imports
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.util.Configurable;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * The interface for Connector Selection. Any class that implements this
 * interface is responsible for taking in two forms of information:
 * 
 * <ul>
 * <li>A Distribution Scenario specification - this provides all of the
 * information about data distribution requirements that the connectors to be
 * selected should satisfy.</li>
 * <li>A Set of Distribution Connector Profiles (DCPs) - these provide
 * connector metadata information that is to be used to identify whether or not
 * the connectors are the appropriate ones to select for the given Distribution
 * Scenario specification.</li>
 * </ul>
 * </p>.
 */
public interface Selector extends Configurable{

	public List selectConnectors(DistributionScenario scenario, List dcpProfiles);

}
