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

package edu.usc.softarch.disco.examples;

// JDK imports
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

// DISCO imports
import edu.usc.softarch.disco.analysis.StatsGenerator;
import edu.usc.softarch.disco.selection.ConnectorRank;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Example use of DISCO API to compute distances that could be used in the
 * {@link ExhaustiveKMeans} {@link Analyzer}.
 * </p>.
 */
public class ComputeDistanceExample {

  private static final Set<String> c1 = new HashSet<String>(Arrays
      .asList(new String[] { "A", "B", "C" }));

  private static final Set<String> c2 = new HashSet<String>(Arrays
      .asList(new String[] { "A", "B" }));

  private static final Set<String> c3 = new HashSet<String>(Arrays
      .asList(new String[] { "A", "C" }));

  private static final Set<String> c4 = new HashSet<String>(Arrays
      .asList(new String[] { "B", "C" }));

  private static final Set<String> c5 = new HashSet<String>(Arrays
      .asList(new String[] { "A" }));

  private static final Set<String> c6 = new HashSet<String>(Arrays
      .asList(new String[] { "B" }));

  private static final Set<String> c7 = new HashSet<String>(Arrays
      .asList(new String[] { "C" }));

  private static final Set<ConnectorRank> BLANK_SET = new HashSet<ConnectorRank>();

  public static void main(String[] args) {

    // build up first rank list
    List<ConnectorRank> rankList1 = new Vector<ConnectorRank>();

    rankList1.add(new ConnectorRank("A", 0.20991));
    rankList1.add(new ConnectorRank("B", 0.20991));
    rankList1.add(new ConnectorRank("C", 0.15001));

    // build up second rank list
    List<ConnectorRank> rankList2 = new Vector<ConnectorRank>();
    rankList2.add(new ConnectorRank("A", 0.25991));
    rankList2.add(new ConnectorRank("B", 0.22991));
    rankList2.add(new ConnectorRank("C", 0.15001));

    System.out.println(" ,Appropriate,Avg D1,Inappropriate,Avg D2,AvgD1+AvgD2");
    printDistances(rankList1, 1);
    printDistances(rankList2, 2);
  }

  private static void printDistances(List<ConnectorRank> rankList, int num) {
    // c1, BLANK_SET
    // c2, c7
    // c3, c6
    // c4, c5
    // c5, c4
    // c6, c3
    // c7, c2
    // BLANK_SET, c1
    System.out.println("Rank List " + num + ", , , , , , ");

    printAppInappSet(getRankSetFromConnSetSpec(c1, rankList), BLANK_SET);
    printAppInappSet(getRankSetFromConnSetSpec(c2, rankList),
        getRankSetFromConnSetSpec(c7, rankList));
    printAppInappSet(getRankSetFromConnSetSpec(c3, rankList),
        getRankSetFromConnSetSpec(c6, rankList));
    printAppInappSet(getRankSetFromConnSetSpec(c4, rankList),
        getRankSetFromConnSetSpec(c5, rankList));
    printAppInappSet(getRankSetFromConnSetSpec(c5, rankList),
        getRankSetFromConnSetSpec(c4, rankList));
    printAppInappSet(getRankSetFromConnSetSpec(c6, rankList),
        getRankSetFromConnSetSpec(c3, rankList));
    printAppInappSet(getRankSetFromConnSetSpec(c7, rankList),
        getRankSetFromConnSetSpec(c2, rankList));
    printAppInappSet(BLANK_SET, getRankSetFromConnSetSpec(c1, rankList));

  }

  private static void printAppInappSet(Set<ConnectorRank> s1,
      Set<ConnectorRank> s2) {
    double s1avgDist = StatsGenerator.computeAverageDist(s1);
    double s2avgDist = StatsGenerator.computeAverageDist(s2);

    System.out.println(" ," + connRankSetToString(s1) + "," + s1avgDist + ","
        + connRankSetToString(s2) + "," + s2avgDist + ","
        + (s1avgDist + s2avgDist));
  }

  private static Set<ConnectorRank> getRankSetFromConnSetSpec(
      Set<String> connSetSpec, List<ConnectorRank> rankList) {
    Set<ConnectorRank> rankSet = new HashSet<ConnectorRank>();
    for (Iterator<String> i = connSetSpec.iterator(); i.hasNext();) {
      String connName = i.next();
      rankSet.add(getRankFromConnName(connName, rankList));
    }

    return rankSet;

  }

  private static ConnectorRank getRankFromConnName(String connName,
      List<ConnectorRank> rankList) {
    if (rankList == null || (rankList != null && rankList.size() == 0)) {
      return null;
    }

    for (Iterator<ConnectorRank> i = rankList.iterator(); i.hasNext();) {
      ConnectorRank rank = i.next();
      if (rank.getConnectorName().equals(connName)) {
        return rank;
      }
    }

    return null;
  }

  private static String connRankSetToString(Set<ConnectorRank> set) {
    if (set == null || (set != null && set.size() == 0)) {
      return "[]";
    }

    StringBuffer rStrBuf = new StringBuffer();
    rStrBuf.append("[");

    for (Iterator<ConnectorRank> i = set.iterator(); i.hasNext();) {
      ConnectorRank r = i.next();
      rStrBuf.append(r.getConnectorName());
      rStrBuf.append("|");
    }

    rStrBuf.deleteCharAt(rStrBuf.length() - 1);
    rStrBuf.append("]");
    return rStrBuf.toString();
  }

}
