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

// JDK imports
import java.util.Iterator;
import java.util.Set;

// DISCO imports
import edu.usc.softarch.disco.selection.ConnectorRank;
import edu.usc.softarch.disco.structs.AnswerKey;
import edu.usc.softarch.disco.structs.PrecisionRecallData;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A Utility class to perform statistics related to Precision and Recall
 * measurement.
 * </p>.
 */
public final class StatsGenerator {

  public static double computeRecall(PrecisionRecallData data) {
    return (1.0 * data.getTP()) / (1.0 * (data.getTP() + data.getFN()));

  }

  public static double computeErrorRate(PrecisionRecallData data) {
    return (1.0 * (data.getFP() + data.getFN()))
        / (1.0 * (data.getFN() + data.getFP() + data.getTN() + data.getTP()));
  }

  public static double computePrecision(PrecisionRecallData data) {
    // TODO: remove this stub
    return (1.0 * data.getTP()) / (1.0 * (data.getTP() + data.getFP()));
  }

  public static double computeAccuracy(PrecisionRecallData data) {
    return (1.0 * (data.getTP() + data.getTN()))
        / (1.0 * (data.getFN() + data.getFP() + data.getTN() + data.getTP()));
  }

  /**
   * Generate precision/recall data for the given generated key, and expert key.
   * 
   * @param generated
   *          The {@link AnswerKey} generated as a result of an {@link Analyzer}.
   * 
   * @param expert
   *          An expert {@link AnswerKey} for the particular
   *          {@link DistributionScenario} being analyzed.
   * @return {@link PrecisionRecallData}.
   */
  public static PrecisionRecallData precisionRecallAnalysis(
      AnswerKey generated, AnswerKey expert) {

    int truePositives = 0, trueNegatives = 0, falsePositives = 0, falseNegatives = 0;

    if (expert.getAppropriate() != null && expert.getAppropriate().size() > 0) {
      for (Iterator<String> i = generated.getAppropriate().iterator(); i
          .hasNext();) {
        String connName = i.next();
        if (expert.getAppropriate().contains(connName)) {
          truePositives++;
        } else {
          falsePositives++;
        }
      }
    } else {
      for (Iterator<String> i = generated.getAppropriate().iterator(); i
          .hasNext();) {
        String connName = i.next();
        falsePositives++;
      }
    }

    if (expert.getInappropriate() != null
        && expert.getInappropriate().size() > 0) {
      for (Iterator<String> i = generated.getInappropriate().iterator(); i
          .hasNext();) {
        String connName = i.next();
        if (expert.getInappropriate().contains(connName)) {
          trueNegatives++;
        } else {
          falseNegatives++;
        }
      }
    } else {
      for (Iterator<String> i = generated.getInappropriate().iterator(); i
          .hasNext();) {
        String connName = i.next();
        falseNegatives++;
      }
    }

    return new PrecisionRecallData(truePositives, trueNegatives,
        falsePositives, falseNegatives);

  }
  
  public static double computeAverageDist(Set<ConnectorRank> connSet) {
    if (connSet == null || (connSet != null && connSet.size() == 0))
      return 0.0;

    // first compute set average
    double setAvg = 0.0;
    double avgDist = 0.0;

    for (Iterator<ConnectorRank> i = connSet.iterator(); i.hasNext();) {
      ConnectorRank rank = i.next();
      setAvg += rank.getRank();
    }

    setAvg /= (connSet.size() * 1.0);
    
    //System.out.println("Mean: = "+setAvg);

    // compute average dist
    for (Iterator<ConnectorRank> i = connSet.iterator(); i.hasNext();) {
      ConnectorRank rank = i.next();
      avgDist += Math.abs((rank.getRank() - setAvg));
    }

    return avgDist;
  }
}
