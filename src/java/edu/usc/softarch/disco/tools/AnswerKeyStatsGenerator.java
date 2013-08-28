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

package edu.usc.softarch.disco.tools;

// JDK imports
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

// DISCO imports
import edu.usc.softarch.disco.structs.AnswerKey;
import edu.usc.softarch.disco.util.AnswerKeyReader;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Describe your class here
 * </p>.
 */
public final class AnswerKeyStatsGenerator {

  private static final Map<String, List<String>> familyMap = new HashMap<String, List<String>>();

  private static final String[] gridConns = { "GridFTP", "bbFTP" };

  private static final String[] p2pConns = { "Bittorrent" };

  private static final String[] csConns = { "RMI", "CORBA", "SOAP",
      "HTTP/REST", "FTP", "UFTP", "Commercial UDP Technology", "SCP" };

  private static final String[] eventConns = { "GLIDE", "Sienna" };

  static {
    familyMap.put("Grid", Arrays.asList(gridConns));
    familyMap.put("Client/Server", Arrays.asList(csConns));
    familyMap.put("P2P", Arrays.asList(p2pConns));
    familyMap.put("Event", Arrays.asList(eventConns));
  }

  private static final Map<String, Integer> familyAppCount = new HashMap<String, Integer>();

  private static final Map<String, Integer> familyInappCount = new HashMap<String, Integer>();

  private static final Map<String, Integer> connAppCount = new HashMap<String, Integer>();

  private static final Map<String, Integer> connInappCount = new HashMap<String, Integer>();

  private static int totalAppropriate = 0;

  private static int totalInappropriate = 0;

  /* answer key file filter */
  public static final FileFilter ANSWER_KEY_FILTER = new FileFilter() {
    public boolean accept(File f) {
      return f.isFile() && f.getName().endsWith(".xml");
    }

  };

  public AnswerKeyStatsGenerator() {
    initCounts();
  }

  public void generateStats(File answerKeyDir) {
    File[] answerKeyFiles = answerKeyDir.listFiles(ANSWER_KEY_FILTER);

    if (answerKeyFiles != null && answerKeyFiles.length > 0) {
      for (int i = 0; i < answerKeyFiles.length; i++) {
        AnswerKey key = AnswerKeyReader.readAnswerKeyFile(answerKeyFiles[i]);
        processAndCountSet(key.getAppropriate(), this.familyAppCount, true);
        this.totalAppropriate += key.getAppropriate().size();
        processAndCountSet(key.getInappropriate(), this.familyInappCount, false);
        this.totalInappropriate += key.getInappropriate().size();
      }
    }

    System.out
        .println("Total appropriate connectors: " + this.totalAppropriate);
    System.out.println("Total inappropriate connectors: "
        + this.totalInappropriate);

    System.out.println("Famliy,Appropriate,Inappropriate");
    double totalP2PApp = (this.familyAppCount.get("P2P") * 1.0)
        / (this.totalAppropriate * 1.0);
    double totalP2PInapp = (this.familyInappCount.get("P2P") * 1.0)
        / (this.totalInappropriate * 1.0);
    System.out.println("P2P," + totalP2PApp + "," + totalP2PInapp);

    double totalCSApp = (this.familyAppCount.get("Client/Server") * 1.0)
        / (this.totalAppropriate * 1.0);
    double totalCSInapp = (this.familyAppCount.get("Client/Server") * 1.0)
        / (this.totalInappropriate * 1.0);
    System.out.println("Client/Server," + totalCSApp + "," + totalCSInapp);

    double totalEventApp = (this.familyAppCount.get("Event") * 1.0)
        / (this.totalAppropriate * 1.0);
    double totalEventInapp = (this.familyInappCount.get("Event") * 1.0)
        / (this.totalInappropriate * 1.0);
    System.out.println("Event," + totalEventApp + "," + totalEventInapp);

    double totalGridApp = (this.familyAppCount.get("Grid") * 1.0)
        / (this.totalAppropriate * 1.0);
    double totalGridInapp = (this.familyInappCount.get("Grid") * 1.0)
        / (this.totalInappropriate * 1.0);
    System.out.println("Grid," + totalGridApp + "," + totalGridInapp);

    System.out.println("");
    System.out.println("");
    System.out.println("Connector Name,Appropriate,Inappropriate");
    for (int i = 0; i < gridConns.length; i++) {
      System.out.println(gridConns[i] + "," + getPct(gridConns[i], true) + ","
          + getPct(gridConns[i], false));
    }

    for (int i = 0; i < eventConns.length; i++) {
      System.out.println(eventConns[i] + "," + getPct(eventConns[i], true)
          + "," + getPct(eventConns[i], false));
    }

    for (int i = 0; i < csConns.length; i++) {
      System.out.println(csConns[i] + "," + getPct(csConns[i], true) + ","
          + getPct(csConns[i], false));
    }

    for (int i = 0; i < p2pConns.length; i++) {
      System.out.println(p2pConns[i] + "," + getPct(p2pConns[i], true) + ","
          + getPct(p2pConns[i], false));
    }

  }

  public static void main(String[] args) {
    String answerKeyDirPath = null;
    String usage = "AnswerKeyStatsGenerator --answerKeyDirPath <answer key dir path>\n";

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--answerKeyDirPath")) {
        answerKeyDirPath = args[++i];
      }
    }

    if (answerKeyDirPath == null) {
      System.err.println(usage);
      System.exit(1);
    }

    AnswerKeyStatsGenerator generator = new AnswerKeyStatsGenerator();
    generator.generateStats(new File(answerKeyDirPath));
  }

  private double getPct(String connName, boolean app) {
    double cnt = -1.0;
    double denom = -1.0;

    if (app) {
      cnt = 1.0 * this.connAppCount.get(connName);
      denom = 1.0 * this.totalAppropriate;
    } else {
      cnt = 1.0 * this.connInappCount.get(connName);
      denom = 1.0 * this.totalInappropriate;
    }

    return cnt / denom;
  }

  private void initCounts() {
    initFamilyCount(familyAppCount);
    initFamilyCount(familyInappCount);
    initConnCount(connAppCount);
    initConnCount(connInappCount);
  }

  private void initFamilyCount(Map<String, Integer> countMap) {
    countMap.put("Grid", 0);
    countMap.put("Client/Server", 0);
    countMap.put("Event", 0);
    countMap.put("P2P", 0);
  }

  private void initConnCount(Map<String, Integer> countMap) {
    for (int i = 0; i < gridConns.length; i++) {
      countMap.put(gridConns[i], 0);
    }

    for (int i = 0; i < p2pConns.length; i++) {
      countMap.put(p2pConns[i], 0);
    }

    for (int i = 0; i < csConns.length; i++) {
      countMap.put(csConns[i], 0);
    }

    for (int i = 0; i < eventConns.length; i++) {
      countMap.put(eventConns[i], 0);
    }
  }

  private void processAndCountSet(Set<String> conns,
      Map<String, Integer> famCount, boolean app) {
    if (conns.size() > 0) {
      for (Iterator<String> i = conns.iterator(); i.hasNext();) {
        String connName = i.next();
        if (app) {
          this.connAppCount.put(connName, this.connAppCount.get(connName) + 1);
        } else {
          if (this.connInappCount.get(connName) == null) {
            System.out.println("No inapp count for: [" + connName + "]");
          }
          this.connInappCount.put(connName,
              this.connInappCount.get(connName) + 1);
        }
        String family = getFamily(connName);
        int count = famCount.get(family);
        count++;
        famCount.put(family, count);
      }
    }
  }

  private String getFamily(String connName) {
    // check P2P
    if (inFamily(familyMap.get("P2P"), connName)) {
      return "P2P";
    } else if (inFamily(familyMap.get("Client/Server"), connName)) {
      return "Client/Server";
    } else if (inFamily(familyMap.get("Event"), connName)) {
      return "Event";
    } else if (inFamily(familyMap.get("Grid"), connName)) {
      return "Grid";
    } else
      return "N/A";
  }

  private boolean inFamily(List<String> familyConns, String conn) {
    return familyConns.contains(conn);
  }

}
