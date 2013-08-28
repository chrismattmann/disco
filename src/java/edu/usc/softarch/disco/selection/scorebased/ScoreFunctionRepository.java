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

package edu.usc.softarch.disco.selection.scorebased;

// JDK imports
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

// DISCO imports
import edu.usc.softarch.disco.selection.ConnectorPerformanceScore;
import edu.usc.softarch.disco.structs.DistributionScenario;

/**
 * @author rezam
 * @version $Revision$
 * 
 * <p>
 * A repository of {@link ScoreFunction}s for the {@link ScoreBasedSelector}.
 * </p>.
 */
public class ScoreFunctionRepository {

  private HashMap<String, ScoreFunction> scoreFunctions = null;

  public ScoreFunctionRepository() {
    scoreFunctions = new HashMap<String, ScoreFunction>();
  }

  public void updatePerformanceScoreMap(
      HashMap<String, ConnectorPerformanceScore> performanceMap,
      DistributionScenario scenario) {
    Set entries = scoreFunctions.entrySet();
    Iterator iter = entries.iterator();
    while (iter.hasNext()) {
      Map.Entry entry = (Map.Entry) iter.next();
      ScoreFunction scoreFunction = (ScoreFunction) entry.getValue();

      /* use the function's knowledge to update the connector performance map */
      scoreFunction.updatePerformanceScoreMap(performanceMap, scenario);
    }
  }

  public void addScoreFunction(ScoreFunction scoreFunction) {
    String key = scoreFunction.getId();
    scoreFunctions.put(key, scoreFunction);
  }

}
