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
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// APACHE imports
import org.apache.commons.io.FileUtils;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A processing program to iterate over all the 578 hw #5 data from Spring 2007,
 * and tally up connectors, counts, and scenario ids, and record the information
 * in two {@link ConnectorScenarioCountMap}s, one for the appropriate
 * connectors, and one for the inappropriate.
 * </p>.
 */
public class PartTwoProcessor {

  private static final FileFilter dirFilter = new FileFilter() {

    public boolean accept(File f) {
      return f.isDirectory();
    }
  };

  private static final FileFilter txtFileFilter = new FileFilter() {

    public boolean accept(File f) {
      return f.isFile() && f.getName().endsWith(".txt");
    }

  };

  private ConnectorScenarioCountMap appropriateCntMap = null;

  private ConnectorScenarioCountMap inappropriateCntMap = null;

  private int numScenarios = 30;

  private StudentDatabase studentDb = null;

  private int numRejected = 0;

  private int numAccepted = 0;

  private static final String[] connNames = { "Bittorrent", "HTTP/REST", "FTP",
      "SCP", "RMI", "CORBA", "SOAP", "UFTP", "CUDP", "GLIDE", "Siena",
      "GridFTP", "bbFTP" };

  private static final String SCENARIO = "S";

  private static final String APPROPRIATE_KEY = "Appropriate";

  private static final String INAPPROPRIATE_KEY = "Inappropriate";

  private static final String RATIONALE_TERMINATOR = "Rationale";

  private static final String RATIONALE_TERMINATOR_OTHER = "Ratinale";

  /* our log stream */
  private static Logger LOG = Logger
      .getLogger(PartTwoProcessor.class.getName());

  public PartTwoProcessor(int numScenarios, String sdbFilePath) {
    this.numScenarios = numScenarios;
    studentDb = new StudentDatabase(sdbFilePath);
    appropriateCntMap = new ConnectorScenarioCountMap();
    inappropriateCntMap = new ConnectorScenarioCountMap();
    initCountMaps();
  }

  public void processStudentHws(File rootDir) {
    // use dirFilter and iterate over all student dirs
    // when in each dir, use the txtFileFilter to
    // grab the .txt version of their homework

    // grab student database record and cross reference
    // student name (identified by their directory name)

    // Open up the file
    // Skip to the part that begins with "Part II:"
    // start processing from there
    // Look for Appropriate: Conn1,Conn2,Conn3
    // Look for Inappropriate: Conn1,Conn2,Conn3

    // make sure that there are 10 scenarios
    // scenario # start = studentRec.getStartScenarioNum

    File[] studentDirs = rootDir.listFiles(dirFilter);

    if (studentDirs != null && studentDirs.length > 0) {
      LOG.log(Level.INFO, "Processing: [" + studentDirs.length
          + "] student directories");

      for (int i = 0; i < studentDirs.length; i++) {
        String studentName = studentDirs[i].getName();

        // look up their record in the student database
        StudentRecord rec = getStudentRec(studentName);
        if (rec == null) {
          LOG.log(Level.WARNING, "Skipping student: [" + studentName
              + "]: Unable to obtain student record.");
          numRejected++;
          continue;
        } else {
          LOG.log(Level.INFO, "Processing student: [" + studentName + "]");

          // list the .txt files, make sure that there is only 1
          File[] hwFiles = studentDirs[i].listFiles(txtFileFilter);

          if (hwFiles == null || (hwFiles != null && hwFiles.length != 1)) {
            LOG.log(Level.WARNING, "No hw files or > 1 present for student: ["
                + studentName + "]: Num files: ["
                + ((hwFiles == null) ? 0 : hwFiles.length)
                + "]: Skipping processing");
            numRejected++;
            continue;
          }

          // Open the file and process it
          processHwFile(hwFiles[0], rec);
        }

      }

    }

  }

  public static void main(String[] args) throws Exception {
    String usage = "PartTwoProcessor <num scenarios> <sdb path> <root hw dir>\n";
    int numScenarios = -1;
    String sdbFilePath = null, rootHwDir = null;

    if (args.length != 3) {
      System.err.println(usage);
      System.exit(1);
    }

    numScenarios = Integer.parseInt(args[0]);
    sdbFilePath = args[1];
    rootHwDir = args[2];

    PartTwoProcessor processor = new PartTwoProcessor(numScenarios,
        sdbFilePath);
    processor.processStudentHws(new File(rootHwDir));
    System.out.println("APPROPRIATE");
    processor.dumpToExcel(processor.getAppropriateCntMap());
    System.out.println("INAPPROPRIATE");
    processor.dumpToExcel(processor.getInappropriateCntMap());

    System.out.println("Summary:==============");
    System.out.println("Num Records Accepted: [" + processor.getNumAccepted()
        + "]");
    System.out.println("Num Records Rejected: [" + processor.getNumRejected()
        + "]");

  }

  public void dumpToExcel(ConnectorScenarioCountMap data) {
    System.out.print(",");
    for (int i = 0; i < connNames.length; i++) {
      System.out.print(connNames[i] + ",");
    }
    System.out.println("");

    // sort the scenarios
    List<String> scenarios = data.getScenarios();

    Collections.sort(scenarios, new Comparator<String>() {

      public int compare(String o1, String o2) {
        int intO1 = Integer.parseInt(o1.substring(1));
        int intO2 = Integer.parseInt(o2.substring(1));

        if (intO1 < intO2) {
          return -1;
        } else if (intO1 == intO2) {
          return 0;
        } else {
          return 1;
        }
      }

    });

    for (Iterator<String> i = scenarios.iterator(); i.hasNext();) {
      String scenarioId = i.next();
      System.out.print(scenarioId + ",");

      for (int j = 0; j < connNames.length; j++) {
        System.out.print(data.getCount(scenarioId, connNames[j]) + ",");
      }

      System.out.println(",");
    }

  }

  private void processHwFile(File file, StudentRecord rec) {
    String fileStr = null;

    try {
      fileStr = FileUtils.readFileToString(file, null);
    } catch (IOException e) {
      LOG.log(Level.WARNING, "Unable to read file: [" + file
          + "] into string: message: " + e.getMessage());
      numRejected++;
      return;
    }

    LOG.log(Level.INFO, "Processing hw file: [" + file.getAbsolutePath() + "]");

    // here is the strategy
    // start from the beginning of the string, and then do the following
    // find APPROPRIATE_KEY, first index of
    // appropriate set = first index of APPROPRIATE_KEY to first index of
    // INAPPROPRIATE_KEY
    // then from APPROPRIATE_KEY on, find INAPPROPRIATE_KEY
    // inappropriate set = first index of inappropriate_key to first index of
    // rationatle terminators

    // first chomp off everyting up to Part II
    int partTwoIdx = fileStr.indexOf("Part II") != -1 ? fileStr
        .indexOf("Part II") : fileStr.indexOf("Part 2");
    if (partTwoIdx != -1) {
      fileStr = fileStr.substring(partTwoIdx);
    }

    int current = 0;
    String startStr = fileStr;
    int cnt = 0;
    while (true) {
      String examineStr = startStr.substring(current);
      // System.out.println("Examine str: "+examineStr);
      LOG.log(Level.FINEST, "Current: " + current);
      int appKeyIdx = examineStr.indexOf(APPROPRIATE_KEY);
      int inappKeyIdx = examineStr.indexOf(INAPPROPRIATE_KEY, appKeyIdx);

      if (appKeyIdx == -1 || inappKeyIdx == -1) {
        break;
      }

      int ratTermIdx = examineStr.indexOf(RATIONALE_TERMINATOR, inappKeyIdx);
      if (ratTermIdx == -1) {
        ratTermIdx = examineStr
            .indexOf(RATIONALE_TERMINATOR_OTHER, inappKeyIdx);
      }

      LOG.log(Level.FINEST, "appKeyIdx: [" + appKeyIdx + "]: inappKeyIdx: ["
          + inappKeyIdx + "]: ratTermIdx: [" + ratTermIdx + "]");

      if (ratTermIdx < inappKeyIdx) {
        break;
      }

      String rawAppConnStr = examineStr.substring(appKeyIdx, inappKeyIdx);
      String rawInappConnStr = examineStr.substring(inappKeyIdx, ratTermIdx);

      LOG.log(Level.FINEST, "Raw Data: App: [" + rawAppConnStr + "]: Inapp: ["
          + rawInappConnStr + "]");

      String cleanAppConnStr = guessConns(rawAppConnStr);
      String cleanInappConnStr = guessConns(rawInappConnStr);
      int scenarioNum = rec.getStartingScenario() + cnt;

      LOG.log(Level.INFO, "App Data: [" + cleanAppConnStr + "]: Inapp Data: ["
          + cleanInappConnStr + "]: Scenario: [" + scenarioNum + "]");

      // add it to the overall data map
      flushData(cleanAppConnStr, cleanInappConnStr, scenarioNum);

      current = ratTermIdx;
      startStr = examineStr;
      cnt++;
    }

    numAccepted++;

  }

  private String guessConns(String rawData) {
    String connStr = "";
    String upperRawData = rawData.toUpperCase();

    for (int i = 0; i < connNames.length; i++) {
      if (upperRawData.indexOf(connNames[i].toUpperCase()) != -1) {
        connStr += connNames[i] + ",";
      }
    }

    if (!connStr.equals("")) {
      connStr = connStr.substring(0, connStr.length() - 1);
    }

    return connStr;
  }

  private void flushData(String app, String inapp, int scenNum) {
    flushConnSet(app, scenNum, APPROPRIATE_KEY);
    flushConnSet(inapp, scenNum, INAPPROPRIATE_KEY);
  }

  private void flushConnSet(String rawData, int scenNum, String key) {

    if (key.equals(APPROPRIATE_KEY)) {
      String[] conns = rawData.split(",");
      if (conns != null && conns.length > 0) {
        for (int i = 0; i < conns.length; i++) {
          this.appropriateCntMap.incrementCount(SCENARIO + scenNum, conns[i]);
        }
      }
    } else {
      String[] conns = rawData.split(",");
      if (conns != null && conns.length > 0) {
        for (int i = 0; i < conns.length; i++) {
          this.inappropriateCntMap.incrementCount(SCENARIO + scenNum, conns[i]);
        }
      }
    }
  }

  private StudentRecord getStudentRec(String fullName) {
    // first try to split the fullName and then search on it
    // firstname lastname
    String[] nameToks = fullName.split(" ");
    String firstName = nameToks[0];
    String lastName = nameToks[1];

    StudentRecord rec = this.studentDb.getRecordByFirstAndLastName(firstName,
        lastName);

    if (rec == null) {
      LOG.log(Level.WARNING,
          "Unable to retrieve student record by first and last name. Trying last name: ["
              + lastName + "] only");

      List<StudentRecord> recs = this.studentDb.getRecordByLastName(lastName);
      if (recs == null)
        return null;
      if (recs.size() != 1) {
        LOG.log(Level.WARNING, "Unable to uniquely identify student: ["
            + fullName + "]: Num records returned: [" + recs.size() + "]");
        return null;
      } else
        return recs.get(0);
    } else
      return rec;
  }

  private void initCountMaps() {
    List<String> connList = Arrays.asList(connNames);
    for (int i = 0; i < this.numScenarios; i++) {
      appropriateCntMap.initScenario(SCENARIO + (i + 1), connList);
      inappropriateCntMap.initScenario(SCENARIO + (i + 1), connList);
    }

  }

  /**
   * @return the appropriateCntMap
   */
  public ConnectorScenarioCountMap getAppropriateCntMap() {
    return appropriateCntMap;
  }

  /**
   * @param appropriateCntMap
   *          the appropriateCntMap to set
   */
  public void setAppropriateCntMap(ConnectorScenarioCountMap appropriateCntMap) {
    this.appropriateCntMap = appropriateCntMap;
  }

  /**
   * @return the inappropriateCntMap
   */
  public ConnectorScenarioCountMap getInappropriateCntMap() {
    return inappropriateCntMap;
  }

  /**
   * @param inappropriateCntMap
   *          the inappropriateCntMap to set
   */
  public void setInappropriateCntMap(
      ConnectorScenarioCountMap inappropriateCntMap) {
    this.inappropriateCntMap = inappropriateCntMap;
  }

  /**
   * @return the numAccepted
   */
  public int getNumAccepted() {
    return numAccepted;
  }

  /**
   * @param numAccepted
   *          the numAccepted to set
   */
  public void setNumAccepted(int numAccepted) {
    this.numAccepted = numAccepted;
  }

  /**
   * @return the numRejected
   */
  public int getNumRejected() {
    return numRejected;
  }

  /**
   * @param numRejected
   *          the numRejected to set
   */
  public void setNumRejected(int numRejected) {
    this.numRejected = numRejected;
  }
}
