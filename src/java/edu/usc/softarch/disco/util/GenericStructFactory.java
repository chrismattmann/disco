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
import java.util.logging.Level;
import java.util.logging.Logger;

// DISCO imports
import edu.usc.softarch.disco.analysis.Analyzer;
import edu.usc.softarch.disco.selection.Selector;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Describe your class here
 * </p>.
 */
public final class GenericStructFactory {

  /* our log stream */
  private final static Logger LOG = Logger.getLogger(GenericStructFactory.class
      .getName());

  private GenericStructFactory() throws InstantiationException {
    throw new InstantiationException("Don't construct factory classes!");
  }

  public static Selector getSelectorFromClassName(String className) {
    try {
      Class selectorClass = Class.forName(className);
      return (Selector) selectorClass.newInstance();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "ClassNotFoundException when loading selector class " + className
              + " Message: " + e.getMessage());
    } catch (InstantiationException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "InstantiationException when loading selector class " + className
              + " Message: " + e.getMessage());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "IllegalAccessException when loading selector class " + className
              + " Message: " + e.getMessage());
    }

    return null;
  }

  public static Analyzer getAnalyzerFromClassName(String className) {
    try {
      Class analyzerClass = Class.forName(className);
      return (Analyzer) analyzerClass.newInstance();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "ClassNotFoundException when loading analyzer class " + className
              + " Message: " + e.getMessage());
    } catch (InstantiationException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "InstantiationException when loading analyzer class " + className
              + " Message: " + e.getMessage());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "IllegalAccessException when loading analyzer class " + className
              + " Message: " + e.getMessage());
    }

    return null;
  }
}
