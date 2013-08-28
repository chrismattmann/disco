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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// Junit imports
import junit.framework.TestCase;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Test Case for the Val Range Reader
 * </p>.
 */
public class TestValRangeReader extends TestCase {

	public void testReadValRange() {
		Map valRange = ValRangeReader.readValRangeFile(new File(
				"src/testdata/valRange.xml"));

		assertNotNull(valRange);

		assertNotNull(valRange.get("data_access_locality"));

		List vals = (List) valRange.get("data_access_locality");
		assertEquals(vals.size(), 2);

		boolean gotProcess = false, gotGlobal = false;

		for (Iterator i = vals.iterator(); i.hasNext();) {
			String val = (String) i.next();
			if (val.equals("Process")) {
				gotProcess = true;
			} else if (val.equals("Global")) {
				gotGlobal = true;
			}
		}

		if (!gotProcess || !gotGlobal) {
			fail("Process and Global were not both present in the val range for data_access_locality!");
		}
	}

}
