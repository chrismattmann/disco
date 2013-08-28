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

package edu.usc.softarch.disco.selection.optimizing;

// JDK imports
import java.util.HashMap;

// DISCO imports
import edu.usc.softarch.disco.connector.dcp.DistributionConnectorProfile;

/**
 * @author rezam
 * @version $Revision$
 * 
 * <p>
 * A repository of {@link CompetencyFunction}s for the
 * {@link OptimizingSelector}.
 * </p>.
 */
public class CompetencyFunctionRepository {

  private HashMap<String, CompetencyFunction> competencyFunctions = null;

  public CompetencyFunctionRepository() {
    competencyFunctions = new HashMap<String, CompetencyFunction>();
  }

  public void addCompetencyFunction(CompetencyFunction competencyFunction) {
    String key = competencyFunction.getId();
    competencyFunctions.put(key, competencyFunction);
  }

  public boolean hasFunctionsForVar(DistributionConnectorProfile dcp,
      String varx) {
    CompetencyFunction func;
    func = getCompetencyFunction(dcp, varx, CompetencyFunction.VAR_CONSISTENCY);
    if (func != null)
      return true;
    func = getCompetencyFunction(dcp, varx,
        CompetencyFunction.VAR_DEPENDABILITY);
    if (func != null)
      return true;
    func = getCompetencyFunction(dcp, varx, CompetencyFunction.VAR_EFFICIENCY);
    if (func != null)
      return true;
    func = getCompetencyFunction(dcp, varx, CompetencyFunction.VAR_SCALABILITY);
    if (func != null)
      return true;
    return false;
  }

  public CompetencyFunction getCompetencyFunction(
      DistributionConnectorProfile dcp, String varx, String vary) {
    CompetencyFunction func = null;
    String id = CompetencyFunction.getIdForParams(dcp, varx, vary);
    if (competencyFunctions.containsKey(id)) {
      func = competencyFunctions.get(id);
    }
    return func;
  }

  public int size() {
    return this.competencyFunctions.keySet().size();
  }

}
