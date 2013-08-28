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
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// DISCO imports
import edu.usc.softarch.disco.connector.dcp.DistributionConnectorProfile;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Distribution Connector Profile XML file Reader.
 * </p>.
 */
public final class DCPReader {
	/* our log stream */
	private static Logger LOG = Logger.getLogger(DCPReader.class.getName());

	/* file filter for xml files */
	private static FileFilter XML_FILE_FILTER = new FileFilter() {

		public boolean accept(File file) {
			return file.isFile() && file.getName().endsWith(".xml");
		}

	};

	private DCPReader() throws InstantiationException {
		throw new InstantiationException("Don't construct reader objects!");
	}
	
	public static DistributionConnectorProfile parseDCP(InputStream is) throws Exception{

		Document doc = XMLUtils.getDocumentRoot(is);
		Element rootElem = doc.getDocumentElement();

		if (rootElem.getAttribute("class").equals("Event")) {
			return XmlStructFactory
					.getEventDistributionConnectorProfile(rootElem);
		} else if (rootElem.getAttribute("class").equals("P2P")) {
			return XmlStructFactory
					.getP2PDistributionConnectorProfile(rootElem);
		} else if (rootElem.getAttribute("class").equals("grid")) {
			return XmlStructFactory
					.getGridDistributionConnectorProfile(rootElem);
		} else if (rootElem.getAttribute("class").equals("Client/Server")) {
			return XmlStructFactory
					.getClientServerDistributionConnectorProfile(rootElem);
		} else {
			LOG.log(Level.WARNING, "Unknown DCP class: ["
					+ rootElem.getAttribute("class") + "]");
			return null;
		}		
	}

	public static DistributionConnectorProfile parseDCP(File file)
			throws Exception {
		return parseDCP(new FileInputStream(file));
	}

	public static List parseDCPs(File dir) throws Exception {
		List dcps = null;

		File[] dcpFiles = dir.listFiles(XML_FILE_FILTER);

		if (dcpFiles != null && dcpFiles.length > 0) {
			dcps = new Vector(dcpFiles.length);
			for (int i = 0; i < dcpFiles.length; i++) {
				DistributionConnectorProfile dcp = DCPReader
						.parseDCP(dcpFiles[i]);
				dcps.add(dcp);
			}
		}

		return dcps;
	}

}
