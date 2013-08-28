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

package edu.usc.softarch.disco.analysis;

//JDK imports
import java.util.HashSet;
import java.util.Set;

//DISCO imports
import edu.usc.softarch.disco.selection.ConnectorRank;

//Junit imports
import junit.framework.TestCase;

/**
 * @author mattmann
 * @version $Revision$
 *
 * <p>Test out the {@link StatsGenerator}</p>.
 */
public class TestStatsGenerator extends TestCase {
  
  private static double expectedDistSetOne = 0.03956850796450915;
  
  private static double expectedDistSetTwo = 0.16777851006526995;
  
  public void testComputeAverageDist(){
    ConnectorRank rank1 = new ConnectorRank("HTTP/REST", 0.09943080425600441);
    ConnectorRank rank2 = new ConnectorRank("SOAP", 0.09919102812267519);
    ConnectorRank rank3 = new ConnectorRank("bbFTP", 0.09685866227594184);
    ConnectorRank rank4 = new ConnectorRank("Sienna", 0.09574969765929416);
    ConnectorRank rank5 = new ConnectorRank("GridFTP", 0.09307128526955323);
    ConnectorRank rank6 = new ConnectorRank("GLIDE",0.09182336347602806);
    ConnectorRank rank7 = new ConnectorRank("FTP", 0.09009861148376429);
    ConnectorRank rank8 = new ConnectorRank("SCP", 0.08877984275045353);
    ConnectorRank rank9 = new ConnectorRank("Bittorrent", 0.0810334301588259);
    ConnectorRank rank10 = new ConnectorRank("RMI", 0.060796871114639064);
    ConnectorRank rank11 = new ConnectorRank("CORBA", 0.05214040975134733);
    ConnectorRank rank12 = new ConnectorRank("UFTP", 0.051025993681473016);
    ConnectorRank rank13 = new ConnectorRank("Commercial UDP Technology",0.0);
    
    Set<ConnectorRank> set1 = new HashSet<ConnectorRank>();
    set1.add(rank9);
    set1.add(rank12);
    set1.add(rank11);
    set1.add(rank10);
    
    Set<ConnectorRank> set2 = new HashSet<ConnectorRank>();
    set2.add(rank1);
    set2.add(rank2);
    set2.add(rank3);
    set2.add(rank4);
    set2.add(rank5);
    set2.add(rank6);
    set2.add(rank7);
    set2.add(rank8);
    set2.add(rank13);
    
    double setOneAvgDist = StatsGenerator.computeAverageDist(set1);
    double setTwoAvgDist = StatsGenerator.computeAverageDist(set2);
    
    assertEquals(expectedDistSetOne, setOneAvgDist, 0.2F);
    assertEquals(expectedDistSetTwo, setTwoAvgDist, 0.2F);
    
  }

}
