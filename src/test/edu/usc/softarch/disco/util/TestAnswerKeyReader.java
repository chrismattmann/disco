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

//DISCO imports
import edu.usc.softarch.disco.structs.AnswerKey;

// Junit imports
import junit.framework.TestCase;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Test Case for the {@link AnswerKeyReader}.
 * </p>.
 */
public class TestAnswerKeyReader extends TestCase {

 public void testReadAnswerKey() {
   AnswerKey key = AnswerKeyReader.readAnswerKeyFile(new File("./src/testdata/exAnswerKey.xml"));
   assertNotNull(key);
   
   assertNotNull(key.getAppropriate());
   assertNotNull(key.getInappropriate());
   assertTrue(key.getAppropriate().contains("Bittorrent"));
   assertTrue(key.getInappropriate().contains("SCP"));
   
 }

}