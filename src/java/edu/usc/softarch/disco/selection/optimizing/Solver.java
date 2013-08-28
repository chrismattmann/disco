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

package edu.usc.softarch.disco.selection.optimizing;

/**
 * 
 * @author rezam
 * @version $Revision$
 * 
 * <p>
 * Describe your class here
 * </p>.
 */
public class Solver {

  private final int VARS = 10; /* max # of variables */

  private final double RHO_BEGIN = 0.5; /* stepsize geometric shrink */

  private final double EPSMIN = 1E-7; /* ending value of stepsize */

  private final int IMAX = 5000; /* max # of iterations */

  /* global variables */
  private int funevals = 0;

  double f(double[] x, int n) {
    double termConsistency = coefConsistency;
    double termDependability = coefDependability;
    double termEfficiency = coefEfficiency;
    double termScalability = coefScalability;
    for (int i = 0; i < n; i++) {
      if (functionsConsistency[i] != null)
        termConsistency *= functionsConsistency[i]
            .getValueForPointLogarithmic((float) x[i]);
      if (functionsDependability[i] != null)
        termDependability *= functionsDependability[i]
            .getValueForPointLogarithmic((float) x[i]);
      if (functionsEfficiency[i] != null)
        termEfficiency *= functionsEfficiency[i]
            .getValueForPointLogarithmic((float) x[i]);
      if (functionsScalability[i] != null)
        termScalability *= functionsScalability[i]
            .getValueForPointLogarithmic((float) x[i]);
    }
    return termConsistency + termDependability + termEfficiency
        + termScalability;
  }

  /* given a point, look for a better one nearby, one coord at a time */
  double best_nearby(double[] delta, double[] point, double prevbest, int nvars) {
    double[] z = new double[VARS];
    double maxf, ftmp;
    int i;
    maxf = prevbest;
    for (i = 0; i < nvars; i++)
      z[i] = point[i];
    for (i = 0; i < nvars; i++) {
      z[i] = point[i] + delta[i];
      ftmp = f(z, nvars);
      if (ftmp > maxf)
        maxf = ftmp;
      else {
        delta[i] = 0.0 - delta[i];
        z[i] = point[i] + delta[i];
        ftmp = f(z, nvars);
        if (ftmp > maxf)
          maxf = ftmp;
        else
          z[i] = point[i];
      }
    }
    for (i = 0; i < nvars; i++)
      point[i] = z[i];
    return (maxf);
  }

  int hooke(int nvars, double[] startpt, double[] endpt, double rho,
      double epsilon, int itermax) {
    double[] delta = new double[VARS];
    double newf, fbefore, steplength, tmp;
    double[] xbefore = new double[VARS], newx = new double[VARS];
    int i, j, keep;
    int iters, iadj;
    for (i = 0; i < nvars; i++) {
      newx[i] = xbefore[i] = startpt[i];
      delta[i] = Math.abs(startpt[i] * rho);
      if (delta[i] == 0.0)
        delta[i] = rho;
    }
    iadj = 0;
    steplength = rho;
    iters = 0;
    fbefore = f(newx, nvars);
    newf = fbefore;
    while ((iters < itermax) && (steplength > epsilon)) {
      iters++;
      iadj++;
      for (j = 0; j < nvars; j++)
        /* find best new point, one coord at a time */
        for (i = 0; i < nvars; i++) {
          newx[i] = xbefore[i];
        }
      newf = best_nearby(delta, newx, fbefore, nvars);
      /* if we made some improvements, pursue that direction */
      keep = 1;
      while ((newf < fbefore) && (keep == 1)) {
        iadj = 0;
        for (i = 0; i < nvars; i++) {
          /* firstly, arrange the sign of delta[] */
          if (newx[i] <= xbefore[i])
            delta[i] = 0.0 - Math.abs(delta[i]);
          else
            delta[i] = Math.abs(delta[i]);
          /* now, move further in this direction */
          tmp = xbefore[i];
          xbefore[i] = newx[i];
          newx[i] = newx[i] + newx[i] - tmp;
        }
        fbefore = newf;
        newf = best_nearby(delta, newx, fbefore, nvars);
        /* if the further (optimistic) move was bad.... */
        if (newf >= fbefore)
          break;
        /* make sure that the differences between the new */
        /* and the old points are due to actual */
        /* displacements; beware of roundoff errors that */
        /* might cause newf < fbefore */
        keep = 0;
        for (i = 0; i < nvars; i++) {
          keep = 1;
          if (Math.abs(newx[i] - xbefore[i]) > (0.5 * Math.abs(delta[i])))
            break;
          else
            keep = 0;
        }
      }
      if ((steplength >= epsilon) && (newf >= fbefore)) {
        steplength = steplength * rho;
        for (i = 0; i < nvars; i++) {
          delta[i] *= rho;
        }
      }
    }
    for (i = 0; i < nvars; i++)
      endpt[i] = xbefore[i];
    return (iters);
  }

  double solve() {
    int itermax;
    double rho, epsilon;
    int i, jj;

    itermax = IMAX;
    rho = RHO_BEGIN;
    epsilon = EPSMIN;
    jj = hooke(nvars, startpt, endpt, rho, epsilon, itermax);

    return jj;
  }

  Solver(int nvars, double[] startpt, double[] endpt,
      CompetencyFunction[] functionsConsistency,
      CompetencyFunction[] functionsDependability,
      CompetencyFunction[] functionsEfficiency,
      CompetencyFunction[] functionsScalability, double coefConsistency,
      double coefDependability, double coefEfficiency, double coefScalability) {
    this.nvars = nvars;
    this.startpt = startpt;
    this.endpt = endpt;
    this.functionsConsistency = functionsConsistency;
    this.functionsDependability = functionsDependability;
    this.functionsEfficiency = functionsEfficiency;
    this.functionsScalability = functionsScalability;
    this.coefConsistency = coefConsistency;
    this.coefDependability = coefDependability;
    this.coefEfficiency = coefEfficiency;
    this.coefScalability = coefScalability;
  }

  int nvars;

  double[] startpt;

  double[] endpt;

  CompetencyFunction[] functionsConsistency;

  CompetencyFunction[] functionsDependability;

  CompetencyFunction[] functionsEfficiency;

  CompetencyFunction[] functionsScalability;

  double coefConsistency, coefDependability, coefEfficiency, coefScalability;
}
