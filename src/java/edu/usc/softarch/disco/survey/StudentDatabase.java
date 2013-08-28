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

package edu.usc.softarch.disco.survey;

// JDK imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * The Student Database for the Connector Survey given in Cs578: Software
 * Architectures class in Spring 2007 for HW #5.
 * </p>.
 */
public class StudentDatabase {

  private List<StudentRecord> db = null;

  private final static int FIRST_NAME_IDX = 0;

  private final static int LAST_NAME_IDX = 1;

  private final static int STATUS_IDX = 5;

  private final static int SCENARIO_SET_IDX = 7;

  public final static String CLASS_DEN = "DEN";

  public final static String CLASS_ONCAMPUS = "On Campus";

  /* our log stream */
  private static Logger LOG = Logger.getLogger(StudentDatabase.class.getName());

  public StudentDatabase(String sdbFilePath) {
    parseSdbFile(new File(sdbFilePath));
  }

  public List<StudentRecord> getRecords() {
    return this.db;
  }

  public StudentRecord getRecordByFirstAndLastName(String firstName,
      String lastName) {
    if (this.db != null && this.db.size() > 0) {
      for (Iterator<StudentRecord> i = this.db.iterator(); i.hasNext();) {
        StudentRecord rec = i.next();
        if (rec.getLastName().equalsIgnoreCase(lastName)
            && rec.getFirstName().equalsIgnoreCase(firstName)) {
          return rec;
        }
      }
    }

    return null;
  }
  
  public List<StudentRecord> getRecordByLastName(String lastName) {
    List<StudentRecord> recs = new Vector<StudentRecord>();
    if (this.db != null && this.db.size() > 0) {
      for (Iterator<StudentRecord> i = this.db.iterator(); i.hasNext();) {
        StudentRecord rec = i.next();
        if (rec.getLastName().equalsIgnoreCase(lastName)) {
          recs.add(rec);
        }
      }
    }

    return recs;
  }

  public int getNumStudents() {
    if (this.db != null) {
      return this.db.size();
    } else
      return 0;
  }

  public static void main(String[] args) throws Exception {
    String usage = "StudentDatabase <path to db file>\n";
    String dbFilePath = null;

    if (args.length != 1) {
      System.err.println(usage);
      System.exit(1);
    }

    dbFilePath = args[0];

    StudentDatabase db = new StudentDatabase(dbFilePath);
    System.out.println("Student DB: [" + dbFilePath + "]");
    System.out.println("Num Record: [" + db.getNumStudents() + "]");

    if (db.getNumStudents() > 0) {
      for (Iterator<StudentRecord> i = db.getRecords().iterator(); i.hasNext();) {
        StudentRecord rec = i.next();
        System.out.println("Name: [" + rec.getLastName() + ","
            + rec.getFirstName() + "]|Classification: [" + rec.getStatus()
            + "]|Scenario Set: [" + rec.getScenarioSet()
            + "]|Starting Scenario: [" + rec.getStartingScenario() + "]");
      }
    }
  }

  private void parseSdbFile(File dbFile) {
    // to parse the file just open it up
    // then read each line, and split on commas

    BufferedReader br = null;
    int lineNo = 1;
    String line = null;

    try {
      br = new BufferedReader(new FileReader(dbFile));
      this.db = new Vector<StudentRecord>();

      while ((line = br.readLine()) != null) {
        String[] dbToks = line.split(",");
        StudentRecord rec = new StudentRecord();
        rec.setFirstName(dbToks[FIRST_NAME_IDX].trim());
        rec.setLastName(dbToks[LAST_NAME_IDX].trim());
        rec.setScenarioSet(Integer.parseInt(dbToks[SCENARIO_SET_IDX]));
        rec.setStatus(!dbToks[STATUS_IDX].equals("") ? dbToks[STATUS_IDX].trim()
            : CLASS_ONCAMPUS);
        this.db.add(rec);
        lineNo++;
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING, "Student database file not found: ["
          + dbFile.getAbsolutePath() + "]");
    } catch (IOException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING, "Unable to parse line: [" + lineNo
          + "]: attempting to continue parsing.");
    } catch (Exception e) {
      e.printStackTrace();
      LOG.log(Level.WARNING, "Error parsing line: [" + lineNo + "]: Line: ["
          + line + "]");
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (Exception ignore) {
        }

        br = null;
      }
    }

  }

}
