/* Copyright (c) 2007 University of Southern California.
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

//JDK imports
import java.io.File;

//DISCO imports
import edu.usc.softarch.disco.performance.PerformanceProfile;
import edu.usc.softarch.disco.performance.RawPerformanceData;

//Junit imports
import junit.framework.TestCase;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Test suite for {@link PerformanceProfileReader} class.
 * </p>.
 */
public class TestPerformanceProfileReader extends TestCase {

  private static File performanceProfileFile = new File(
      "./src/testdata/exPerformanceProfile.xml");

  private static final String expectedScenName = "scenario1.xml";

  private static final long expectedEffBps = 20000L;

  private static final long expectedEffMem = 10485760L;

  private static final long expectedConsBytesSent = 104857600000L;

  private static final int expectedNumFaults = 0;

  private static final String expectedNumUsers = "1";

  private static final String expectedTotalVolume = "104857600000";

  private static final String expectedUserTypes = "4";
  
  private static final String scenarioId = "scenario1.xml";

  public TestPerformanceProfileReader() {
  }

  public void testReadProfile() {
    PerformanceProfile prof = PerformanceProfileReader
        .readPerformanceProfile(performanceProfileFile);
    assertNotNull(prof);
  }

  public void testReadConnectorName() {
    PerformanceProfile prof = PerformanceProfileReader
        .readPerformanceProfile(performanceProfileFile);
    assertEquals("FTP", prof.getConnector());
  }

  public void testReadScenarios() {
    PerformanceProfile prof = PerformanceProfileReader
        .readPerformanceProfile(performanceProfileFile);

    assertNotNull(prof.getRawData());
    assertEquals(1, prof.getRawData().size());
  }

  public void testReadRawData() {
    PerformanceProfile prof = PerformanceProfileReader
        .readPerformanceProfile(performanceProfileFile);
    RawPerformanceData scenData = prof.getRawData().get(scenarioId);
    assertNotNull(scenData);
    assertNotNull(scenData.getScenario());
    assertEquals(expectedScenName, scenData.getScenario().getName());
    assertEquals(expectedEffBps, scenData.getConnBps());
    assertEquals(expectedEffMem, scenData.getConnAvgMem());
    assertEquals(expectedConsBytesSent, scenData.getConnBytesActualTransfer());
    assertEquals(expectedNumFaults, scenData.getConnNumFaults());
    assertEquals(expectedNumUsers, scenData.getScenario().getNumUsers()
        .getValue());
    assertEquals(expectedUserTypes, scenData.getScenario().getNumUserTypes()
        .getValue());
    assertEquals(expectedTotalVolume, scenData.getScenario().getTotalVolume()
        .getValue());

  }

}
