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
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Reads a value range for all attributes defined in a valRange.xml
 * file.
 * </p>.
 */
public final class ValRangeReader {

	private ValRangeReader() throws InstantiationException {
		throw new InstantiationException("Don't construct readers!");
	}

	public static Map readValRangeFile(File file) {
		Document docRoot = XMLUtils.getDocumentRoot(file.getAbsolutePath());
		Element docElement = docRoot.getDocumentElement();
		return XmlStructFactory.getValRange(docElement);
	}

}
