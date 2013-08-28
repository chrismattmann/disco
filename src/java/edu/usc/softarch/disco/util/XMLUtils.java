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

//JDK imports
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * @author mattmann
 * @version $Revision$
 *
 * <p>A suite of utility functions for managing XML documents</p>.
 */
public final class XMLUtils {
	
	/* our log stream */
	private static Logger LOG = Logger.getLogger(XMLUtils.class.getName());

	private XMLUtils() throws InstantiationException{
		throw new InstantiationException("Don't construct utility objects!");
	}
	
    /**
     * <p>
     * This method writes a DOM document to a file
     * </p>.
     * 
     * @param doc
     *            The DOM document to write.
     * @param filename
     *            The filename to write the DOM document to.
     */
    public static void writeXmlFile(Document doc, String filename) throws Exception{
        // Prepare the output file
        File file = new File(filename);
        Result result = new StreamResult(file);
        transform(doc, result);

    }

    public static void writeXmlToStream(Document doc, OutputStream stream) throws Exception{
        Result result = new StreamResult(stream);
        transform(doc, result);
    }

    private static void transform(Document doc, Result result) throws Exception{
        try {
            // Prepare the DOM document for writing
            Source source = new DOMSource(doc);

            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance()
                    .newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
        	e.printStackTrace();
        	throw e;
        } catch (TransformerException e) {
        	e.printStackTrace();
        	throw e;
        }
          catch(Exception e){
        	  e.printStackTrace();
        	  throw e;
          }

    }

    public static Document getDocumentRoot(String xmlFile) {
		InputStream is = null;

		try {
			is = new File(xmlFile).toURL().openStream();
			return getDocumentRoot(is);
		} catch (Exception e) {
			LOG.log(Level.WARNING, "IOException " +
					"opening xmlFile: [" + xmlFile
					+ "]: Message: " + e.getMessage());
			return null;
		}

	}

	public static Document getDocumentRoot(InputStream is) {
		// open up the XML file
		DocumentBuilderFactory factory = null;
		DocumentBuilder parser = null;
		Document document = null;
		InputSource inputSource = null;

		inputSource = new InputSource(is);

		try {
			factory = DocumentBuilderFactory.newInstance();
			parser = factory.newDocumentBuilder();
			document = parser.parse(inputSource);
		} catch (Exception e) {
			LOG.log(Level.WARNING, "Unable to parse xml stream"
					+ ": Reason is [" + e + "]");
			return null;
		}

		return document;
	}
	
	public static Element addNode(Document doc, Node parent, String name) {
		Element child = doc.createElement(name);
		parent.appendChild(child);
		return child;
	}

	public static void addNode(Document doc, Node parent, String name,
			String text) {
		Element child = doc.createElement(name);
		child.appendChild(doc.createTextNode(text));
		parent.appendChild(child);
	}

	public static void addNode(Document doc, Node parent, String ns,
			String name, String text, Map NS_MAP) {
		Element child = doc.createElementNS((String) NS_MAP.get(ns), ns + ":"
				+ name);
		child.appendChild(doc.createTextNode(text));
		parent.appendChild(child);
	}

	public static void addAttribute(Document doc, Element node, String name,
			String value) {
		Attr attribute = doc.createAttribute(name);
		attribute.setValue(value);
		node.getAttributes().setNamedItem(attribute);
	}

}
