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

//JDK imports
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//DISCO imports
import edu.usc.softarch.disco.connector.dcp.DistributionConnectorProfile;

/**
 * @author rezam
 * @version $Revision$
 * 
 * <p>
 * A data structure representing the objective function for a index-based
 * connector selection
 * </p>.
 */
public class OptimizingObjectiveFunction {
  
  /* our log stream */
  private final static Logger LOG = Logger.getLogger(OptimizingObjectiveFunction
      .class.getName());

  private float coefConsistency = 0;

  private float coefEfficiency = 0;

  private float coefDependability = 0;

  private float coefScalability = 0;

  public OptimizingObjectiveFunction(float coefConsistency,
      float coefEfficiency, float coefDependability, float coefScalability) {
    this.coefConsistency = coefConsistency;
    this.coefDependability = coefDependability;
    this.coefEfficiency = coefEfficiency;
    this.coefScalability = coefScalability;
  }

  public float calculateOverallIndex(float indexConsistency,
      float indexEfficiency, float indexDependability, float indexScalability) {
    float overallIndex = coefConsistency * indexConsistency + coefDependability
        * indexDependability + coefEfficiency * indexEfficiency
        + coefScalability * indexScalability;
    return overallIndex;
  }

  public ConnectorPerformanceIndex optimizeObjective(
      List<String> freeVars, DistributionConnectorProfile dcp,
      CompetencyFunctionRepository repo, float baseCoefConsistency,
      float baseCoefEfficiency, float baseCoefDependability,
      float baseCoefScalability) {
    float optimizedIndex = 0;

    int nvar = freeVars.size();
    double[] startpt = new double[nvar];
    double[] endpt = new double[nvar];
    CompetencyFunction[] functionsConsistency = new CompetencyFunction[nvar];
    CompetencyFunction[] functionsDependability = new CompetencyFunction[nvar];
    CompetencyFunction[] functionsEfficiency = new CompetencyFunction[nvar];
    CompetencyFunction[] functionsScalability = new CompetencyFunction[nvar];

    // populate startpt
    float mulCoef = baseCoefConsistency * coefConsistency;
    String varForStartPt = CompetencyFunction.VAR_CONSISTENCY;
    float tmpCoef = baseCoefDependability * coefDependability;
    if (tmpCoef > mulCoef) {
      mulCoef = tmpCoef;
      varForStartPt = CompetencyFunction.VAR_DEPENDABILITY;
    }
    tmpCoef = baseCoefEfficiency * coefEfficiency;
    if (tmpCoef > mulCoef) {
      mulCoef = tmpCoef;
      varForStartPt = CompetencyFunction.VAR_EFFICIENCY;
    }
    tmpCoef = baseCoefScalability * coefScalability;
    if (tmpCoef > mulCoef) {
      mulCoef = tmpCoef;
      varForStartPt = CompetencyFunction.VAR_SCALABILITY;
    }

    for (int i = 0; i < nvar; i++) {
      CompetencyFunction func = repo.getCompetencyFunction(dcp,
          freeVars.get(i), varForStartPt);
      
      if(func == null){
        LOG.log(Level.WARNING, "No competency function present for: DCP: ["+dcp.getConnectorName()+"]: " +
        "var: ["+freeVars.get(i)+"] start pt var: ["+varForStartPt+"]");
        startpt[i] = 0;
      }
      else startpt[i] = func.getXofMaximum();

      // convert to logarithmic scale
      if (startpt[i] == 0) {
        startpt[i] = -100;
      } else {
        startpt[i] = Math.log10(startpt[i]);
      }
    }

    // populate function vectors
    for (int i = 0; i < nvar; i++) {
      functionsConsistency[i] = repo.getCompetencyFunction(dcp,
          freeVars.get(i), CompetencyFunction.VAR_CONSISTENCY);
      functionsDependability[i] = repo.getCompetencyFunction(dcp, freeVars
          .get(i), CompetencyFunction.VAR_DEPENDABILITY);
      functionsEfficiency[i] = repo.getCompetencyFunction(dcp, freeVars.get(i),
          CompetencyFunction.VAR_EFFICIENCY);
      functionsScalability[i] = repo.getCompetencyFunction(dcp,
          freeVars.get(i), CompetencyFunction.VAR_SCALABILITY);
    }

    Solver solver = new Solver(nvar, startpt, endpt, functionsConsistency,
        functionsDependability, functionsEfficiency, functionsEfficiency,
        baseCoefConsistency * coefConsistency, baseCoefDependability
            * coefDependability, baseCoefEfficiency * coefEfficiency,
        baseCoefScalability * coefScalability);

    LOG.log(Level.FINE, "Optimizing for " + dcp);
    optimizedIndex = (float) solver.solve();
    LOG.log(Level.FINE, "Optimum value " + optimizedIndex + " achieved on:");
    for (int i = 0; i < nvar; i++)
      LOG.log(Level.FINE, freeVars.get(i) + " = " + Math.pow(10, endpt[i]));

    ConnectorPerformanceIndex connectorPerformance = new ConnectorPerformanceIndex(
        dcp.getConnectorName(), optimizedIndex);
    return connectorPerformance;
  }

}
