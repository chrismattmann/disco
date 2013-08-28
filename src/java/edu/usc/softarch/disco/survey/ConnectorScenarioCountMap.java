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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A front-end interface to a {@link Map} of String scenarioIds=>{@link Map} of
 * String connector Names and their counts. This is the data structure that is
 * used to analyze Part II of Mattmann's connector survey.
 * </p>.
 */
public class ConnectorScenarioCountMap {

  /* a map of String scenarioName */
  private Map<String, Map<String, Integer>> scenConnMap;

  public ConnectorScenarioCountMap() {
    scenConnMap = new HashMap<String, Map<String, Integer>>();
  }

  public void initScenario(String scenario, List<String> conns) {
    if (conns != null && conns.size() > 0) {
      HashMap<String, Integer> connCountMap = new HashMap<String, Integer>();
      for (Iterator<String> i = conns.iterator(); i.hasNext();) {
        String connName = i.next();
        connCountMap.put(connName, 0);
      }

      this.scenConnMap.put(scenario, connCountMap);
    }
  }

  public void incrementCount(String scenario, String connName) {
    Map<String, Integer> connCountMap = this.scenConnMap.get(scenario);
    if (connCountMap != null) {
      if (connCountMap.containsKey(connName)) {
        int cnt = connCountMap.get(connName);
        cnt++;
        connCountMap.put(connName, cnt);
      }
    }

  }

  public List<String> getScenarios() {
    return Arrays
        .asList(this.scenConnMap.keySet().toArray(new String[] { "" }));
  }

  public void decrementCount(String scenario, String connName) {
    Map<String, Integer> connCountMap = this.scenConnMap.get(scenario);
    if (connCountMap != null) {
      if (connCountMap.containsKey(connName)) {
        int cnt = connCountMap.get(connName);
        cnt--;
        connCountMap.put(connName, cnt);
      }
    }
  }

  public int getCount(String scenario, String connName) {
    Map<String, Integer> connCountMap = this.scenConnMap.get(scenario);
    if (connCountMap != null) {
      if (connCountMap.containsKey(connName)) {
        int cnt = connCountMap.get(connName);
        return cnt;
      } else
        return 0;
    } else
      return 0;
  }

  public void setCount(String scenario, String connName, int cnt) {
    Map<String, Integer> connCountMap = this.scenConnMap.get(scenario);
    if (connCountMap != null) {
      connCountMap.put(connName, cnt);
    }
  }

}
