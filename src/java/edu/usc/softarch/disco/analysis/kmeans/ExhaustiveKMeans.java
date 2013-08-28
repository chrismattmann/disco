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

package edu.usc.softarch.disco.analysis.kmeans;

// JDK imports
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

// DISCO imports
import edu.usc.softarch.disco.analysis.Analyzer;
import edu.usc.softarch.disco.analysis.StatsGenerator;
import edu.usc.softarch.disco.selection.ConnectorRank;
import edu.usc.softarch.disco.structs.AnswerKey;
import edu.usc.softarch.disco.structs.DistributionScenarioResult;

/**
 * @author mattmann
 * @author woollard
 * @version $Revision$
 * 
 * <p>
 * Describe your class here
 * </p>.
 */
public class ExhaustiveKMeans implements Analyzer {

  public ExhaustiveKMeans() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.usc.softarch.disco.analysis.Analyzer#analyzeScenarioResult(edu.usc.softarch.disco.structs.DistributionScenarioResult)
   */
  public AnswerKey analyzeScenarioResult(DistributionScenarioResult result) {
    // iterate over all combinations of connectors in 2 sets to find set with
    // the min
    // distance of each c from the centroid of the set

    // that min dist set combination = the good set?
    // other

    int numConnectors = result.getRankList().size();
    int connNum = new Double(Math.pow(2.0, (numConnectors * 1.0))).intValue();
    System.out.println("Initial rank list: " + result.getRankList());
    String topRankedConn = ((ConnectorRank)result.getRankList().get(0)).getConnectorName();

    ConnectorGroup lowGroup = null;

    double MIN_DISTANCE = Double.MAX_VALUE;

    Map<String, ConnectorRank> connectorMap = new HashMap<String, ConnectorRank>();
    for (int i = 0; i < numConnectors; i++) {
      connectorMap.put(String.valueOf(i), (ConnectorRank) result.getRankList()
          .get(i));
    }

    // only instantiate once
    // clear out after each iteration
    // if it's lower average distance from mean
    // then copy into lowest distance
    Set<ConnectorRank> connSet1 = new HashSet<ConnectorRank>();
    Set<ConnectorRank> connSet2 = new HashSet<ConnectorRank>();

    for (int i = 0; i < connNum; i++) {
      String byteStr = Integer.toBinaryString(i);
      char[] byteStrCharArray = byteStr.toCharArray();
      char[] groupDescCharArray = pad(byteStrCharArray, numConnectors);

      for (int j = 0; j < groupDescCharArray.length; j++) {
        if (groupDescCharArray[j] == '1') {
          connSet1.add(connectorMap.get(String.valueOf(j)));
        } else if (groupDescCharArray[j] == '0') {
          connSet2.add(connectorMap.get(String.valueOf(j)));
        }
      }

      double averageDistG1 = StatsGenerator.computeAverageDist(connSet1);
      double averageDistG2 = StatsGenerator.computeAverageDist(connSet2);

      if (averageDistG1 + averageDistG2 < MIN_DISTANCE) {
        /*System.out.println("New min distance: ["
            + (averageDistG1 + averageDistG2) + "] sets: [set1: " + connSet1
            + ",distance: " + averageDistG1 + "], [set2: " + connSet2
            + ",distance: " + averageDistG2 + "]");*/
        ConnectorSet cSet1 = new ConnectorSet(connSet1, averageDistG1);
        ConnectorSet cSet2 = new ConnectorSet(connSet2, averageDistG2);
        lowGroup = new ConnectorGroup(cSet1, cSet2);
        MIN_DISTANCE = averageDistG1 + averageDistG2;
      }

      connSet1.clear();
      connSet2.clear();
    }

    if (lowGroup != null) {
      return getAnswerKey(lowGroup, result.getScenario().getName(), topRankedConn);

    } else
      return null;

  }

  private AnswerKey getAnswerKey(ConnectorGroup g, String scenarioId, String topConnName) {
    Set<String> appropriate = null;
    Set<String> inappropriate = null;

    /*if (g.getSet1().getDist() < g.getSet2().getDist()) {
      appropriate = getConnectorNameSet(g.getSet1().getConnectors());
      inappropriate = getConnectorNameSet(g.getSet2().getConnectors());
    } else {
      appropriate = getConnectorNameSet(g.getSet2().getConnectors());
      inappropriate = getConnectorNameSet(g.getSet1().getConnectors());
    }*/
    Set<String> g1NameSet = getConnectorNameSet(g.getSet1().getConnectors());
    Set<String> g2NameSet = getConnectorNameSet(g.getSet2().getConnectors());
    
    if(g1NameSet.contains(topConnName)){
      appropriate = g1NameSet;
      inappropriate = g2NameSet;
    }
    else if(g2NameSet.contains(topConnName)){
      appropriate = g2NameSet;
      inappropriate = g1NameSet;
    }

    AnswerKey retKey = new AnswerKey();
    retKey.setAppropriate(appropriate);
    retKey.setInappropriate(inappropriate);
    retKey.setScenarioId(scenarioId);
    return retKey;
  }

  private Set<String> getConnectorNameSet(Set<ConnectorRank> rankSet) {
    Set<String> set = new HashSet<String>();
    for (Iterator<ConnectorRank> i = rankSet.iterator(); i.hasNext();) {
      ConnectorRank rank = i.next();
      set.add(rank.getConnectorName());
    }

    return set;
  }

  private char[] pad(char[] orig, int numConnectors) {
    int padChars = numConnectors - orig.length;
    String connectorGroupStr = "";
    for (int i = 0; i < padChars; i++) {
      connectorGroupStr += "0";
    }

    connectorGroupStr += new String(orig);
    return connectorGroupStr.toCharArray();
  }

  private class ConnectorGroup {
    private ConnectorSet set1;

    private ConnectorSet set2;

    public ConnectorGroup() {
      set1 = null;
      set2 = null;
    }

    /**
     * @param set1
     * @param set2
     */
    public ConnectorGroup(ConnectorSet set1, ConnectorSet set2) {
      this.set1 = new ConnectorSet();
      this.set2 = new ConnectorSet();

      for (Iterator<ConnectorRank> i = set1.getConnectors().iterator(); i
          .hasNext();) {
        ConnectorRank r = i.next();
        ConnectorRank newR = new ConnectorRank();
        newR.setConnectorName(r.getConnectorName());
        newR.setRank(r.getRank());
        this.set1.getConnectors().add(newR);
      }
      this.set1.setDist(set1.getDist());

      for (Iterator<ConnectorRank> i = set2.getConnectors().iterator(); i
          .hasNext();) {
        ConnectorRank r = i.next();
        ConnectorRank newR = new ConnectorRank();
        newR.setConnectorName(r.getConnectorName());
        newR.setRank(r.getRank());
        this.set2.getConnectors().add(newR);
      }

      this.set2.setDist(set2.getDist());

    }

    /**
     * @return the set1
     */
    public ConnectorSet getSet1() {
      return set1;
    }

    /**
     * @param set1
     *          the set1 to set
     */
    public void setSet1(ConnectorSet set1) {
      this.set1 = set1;
    }

    /**
     * @return the set2
     */
    public ConnectorSet getSet2() {
      return set2;
    }

    /**
     * @param set2
     *          the set2 to set
     */
    public void setSet2(ConnectorSet set2) {
      this.set2 = set2;
    }

  }

  private class ConnectorSet {

    private Set<ConnectorRank> connectors;

    private double dist;

    public ConnectorSet() {
      connectors = new HashSet<ConnectorRank>();
      dist = 0.0;
    }

    /**
     * @param connectors
     * @param dist
     */
    public ConnectorSet(Set<ConnectorRank> connectors, double dist) {
      super();
      this.connectors = connectors;
      this.dist = dist;
    }

    /**
     * @return the connectors
     */
    public Set<ConnectorRank> getConnectors() {
      return connectors;
    }

    /**
     * @param connectors
     *          the connectors to set
     */
    public void setConnectors(Set<ConnectorRank> connectors) {
      this.connectors = connectors;
    }

    /**
     * @return the dist
     */
    public double getDist() {
      return dist;
    }

    /**
     * @param dist
     *          the dist to set
     */
    public void setDist(double dist) {
      this.dist = dist;
    }

  }

  /* (non-Javadoc)
   * @see edu.usc.softarch.disco.util.Configurable#configure(java.util.Properties)
   */
  public void configure(Properties props) {
    // TODO: flow through props: at current time, there are none
    
  }

}
