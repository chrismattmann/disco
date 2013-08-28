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
import java.util.List;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// DISCO imports
import edu.usc.softarch.disco.performance.PerformanceProfile;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Reads a {@link PerformanceProfile} from an XML file.
 * </p>.
 */
public final class PerformanceProfileReader {

  /* file filter for xml files */
  private static FileFilter XML_FILE_FILTER = new FileFilter() {

    public boolean accept(File file) {
      return file.isFile() && file.getName().endsWith(".xml");
    }

  };

  private PerformanceProfileReader() throws InstantiationException {
    throw new InstantiationException("Don't construct readers!");
  }

  public static PerformanceProfile readPerformanceProfile(File file) {
    Document docRoot = XMLUtils.getDocumentRoot(file.getAbsolutePath());
    Element docElement = docRoot.getDocumentElement();
    return XmlStructFactory.getPerformanceProfile(docElement);
  }

  public static List<PerformanceProfile> readPerformanceProfiles(File dir)
      throws Exception {
    List<PerformanceProfile> profs = null;

    File[] performanceFiles = dir.listFiles(XML_FILE_FILTER);

    if (performanceFiles != null && performanceFiles.length > 0) {
      profs = new Vector<PerformanceProfile>(performanceFiles.length);
      for (int i = 0; i < performanceFiles.length; i++) {
        PerformanceProfile prof = readPerformanceProfile(performanceFiles[i]);
        profs.add(prof);
      }
    }

    return profs;
  }
}
