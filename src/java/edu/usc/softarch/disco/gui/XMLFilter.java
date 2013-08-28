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

package edu.usc.softarch.disco.gui;

import java.io.File;
import java.io.FileFilter;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * XML file filter singleton.
 * This singleton is a filter used to filter results, either in a Swing 
 * file chooser dialog or when generating a list of File objects.
 *
 * When applied, this filter will select only files with a '.xml'
 * file extension.
 *
 * @author Trevor Johns &lt;<a href="mailto:tjohns@usc.edu">tjohns@usc.edu</a>&gt;
 * @version $Rev: 128 $
 */
public class XMLFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter
{
    /** Singleton instance. */
    private static XMLFilter ref;
    
    /**
     * Class constructor.
     * Declared private to prevent manual instantiation.
     * Use {@link #getXMLFilter()} instead.
     */
    private XMLFilter ()
    {
        // Intentionally left blank to enforce singleton rules
    }
    
    /**
     * Public singleton constructor.
     * Use this static method to obtain an instance of the 
     * singleton associated with this class.
     *
     * WARNING: This is not thread safe.
     */
    public static XMLFilter getXMLFilter ()
    {
      if (ref == null)
          // it's ok, we can call this constructor
          ref = new XMLFilter();		
      return ref;
    }
    
    /**
     * Clone protection method.
     * Overrides {@link Object#clone()} to make sure that a seperate 
     * instance of the singleton cannot be created.
     */
    public Object clone() throws CloneNotSupportedException
    {
      throw new CloneNotSupportedException(); 
    }
    
    /**
     * Tests a file to see if it should be filtered or not.
     * 
     * @param f The file to be tested.
     * @return true if the file's extension is '.xml'
     */ 
    public boolean accept(File f)
    {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals("xml")) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * Returns a description of the filter. This result is usually displayed in a 
     * file chooser drop down menu to select what types of files to display.
     *
     * @return The description of the filter.
     */
    public String getDescription ()
    {
        return "XML Files";
    }
    
    /**
     * Private method to deterine the extension of a file.
     *
     * @param f The file to inspect.
     * @return The file extension of the inspected file, not including the leading period.
     */
    private static String getExtension (File f)
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
