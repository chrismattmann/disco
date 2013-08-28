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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// DISCO imports
import edu.usc.softarch.disco.selection.scorebased.ScoreFunction;
import edu.usc.softarch.disco.selection.scorebased.ScoreFunctionRepository;

/**
 * @author rezam
 * @version $Revision$
 * 
 * <p>
 * Score Function XML file Reader.
 * </p>.
 */
public final class ScoreFunctionReader {

	/* file filter for xml files */
	private static FileFilter XML_FILE_FILTER = new FileFilter() {

		public boolean accept(File file) {
			return file.isFile() && file.getName().endsWith(".xml");
		}

	};
	
	/* file filter for directories */
    private static FileFilter DIR_FILE_FILTER = new FileFilter() {
        public boolean accept(File file) {
            return file.isDirectory();
        }
    };

	private ScoreFunctionReader() throws InstantiationException {
		throw new InstantiationException("Don't construct reader objects!");
	}

	public static ScoreFunction parseScoreFunction(File file){
		Document docRoot = XMLUtils.getDocumentRoot(file.getAbsolutePath());
		Element docElement = docRoot.getDocumentElement();
		return XmlStructFactory.getScoreFunction(docElement);
	}

	public static ScoreFunctionRepository parseScoreFunctions(File dir) {
		ScoreFunctionRepository repo = new ScoreFunctionRepository();

		File[] functionDirs = dir.listFiles(DIR_FILE_FILTER);

		if (functionDirs != null && functionDirs.length > 0) {
			for (int i = 0; i < functionDirs.length; i++) {
				parseScoreFunctionsInDir(repo, functionDirs[i]);
			}
		}

		return repo;
	}
	
	private static void parseScoreFunctionsInDir(ScoreFunctionRepository repo, File dir) {
		File[] functionFiles = dir.listFiles(XML_FILE_FILTER);
		if (functionFiles != null && functionFiles.length > 0) {
			for (int i = 0; i < functionFiles.length; i++) {
				ScoreFunction scoreFunction = ScoreFunctionReader
						.parseScoreFunction(functionFiles[i]);
				repo.addScoreFunction(scoreFunction);
			}
		}

	}

}
