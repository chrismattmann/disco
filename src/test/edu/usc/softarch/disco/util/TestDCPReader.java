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

package edu.usc.softarch.disco.util;

// JDK imports
import java.io.File;

// Junit imports
import junit.framework.TestCase;

// DISCO imports
import edu.usc.softarch.disco.connector.dcp.ClientServerDistributionConnectorProfile;
import edu.usc.softarch.disco.connector.dcp.EventDistributionConnectorProfile;
import edu.usc.softarch.disco.connector.dcp.GridDistributionConnectorProfile;
import edu.usc.softarch.disco.connector.dcp.P2PDistributionConnectorProfile;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Tests out the DCP Reader.
 * </p>.
 */
public class TestDCPReader extends TestCase {

	public TestDCPReader() {
	}

	protected void setUp() {

	}

	public void testReadEventDcp() {
		EventDistributionConnectorProfile eventProfile = null;

		try {
			eventProfile = (EventDistributionConnectorProfile) DCPReader
					.parseDCP(new File("src/dcp/glide.xml"));
		} catch (Exception e) {
			fail(e.getMessage());
		}

		assertNotNull(eventProfile);
		assertEquals("Producers: ["
				+ eventProfile.getEventProfile().getCardinality()
						.getProducers() + "] not equal to Components",
				"Components", eventProfile.getEventProfile().getCardinality()
						.getProducers());

		assertNotNull(eventProfile.getEventProfile().getCardinality()
				.getEventPatterns());
		assertEquals("The number of event patterns: ["
				+ eventProfile.getEventProfile().getCardinality()
						.getEventPatterns().size() + "] is not equal to 5", 5,
				eventProfile.getEventProfile().getCardinality()
						.getEventPatterns().size());

		assertEquals("The event incoming priority: ["
				+ eventProfile.getEventProfile().getPriority().getIncoming()
				+ "] is " + "not equal to consumers", "consumers", eventProfile
				.getEventProfile().getPriority().getIncoming());

	}

	public void testReadP2PDcp() {
		P2PDistributionConnectorProfile p2pProfile = null;

		try {
			p2pProfile = (P2PDistributionConnectorProfile) DCPReader
					.parseDCP(new File("src/dcp/bittorrent.xml"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		assertNotNull(p2pProfile);

		assertEquals("The locality: ["
				+ p2pProfile.getDataAccessProfile().getLocality() + "] "
				+ "is not equal to Process", "Process", p2pProfile
				.getDataAccessProfile().getLocality());

	}

	public void testReadGridDcp() {
		GridDistributionConnectorProfile gridProfile = null;

		try {
			gridProfile = (GridDistributionConnectorProfile) DCPReader
					.parseDCP(new File("src/dcp/gridftp.xml"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		assertNotNull(gridProfile);
		assertEquals("The param return value: ["
				+ gridProfile.getProcedureCallProfile().getParams()
						.getReturnValue() + "] "
				+ "is not equal to the expected value of [GridFTP Message]",
				"GridFTP Message", gridProfile.getProcedureCallProfile()
						.getParams().getReturnValue());
	}

	public void testReadClientServerDcp() {
		ClientServerDistributionConnectorProfile csProfile = null;

		try {
			csProfile = (ClientServerDistributionConnectorProfile) DCPReader
					.parseDCP(new File("./src/dcp/rmi.xml"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		assertNotNull(csProfile);
		String dataAccessAvailability = "Session-Based";

		assertEquals("The availability: ["
				+ csProfile.getDataAccessProfile().getAvailabilities()
						.getAvailTransient() + "] "
				+ "is not equal to the expected value of ["
				+ dataAccessAvailability + "]", dataAccessAvailability,
				csProfile.getDataAccessProfile().getAvailabilities()
						.getAvailTransient());

	}
}
