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
 */

package edu.usc.softarch.disco.selection.scorebased;

// JDK imports
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

// DISCO imports
import edu.usc.softarch.disco.selection.ConnectorPerformanceScore;
import edu.usc.softarch.disco.structs.DistributionScenario;

/**
 * @author rezam
 * @version $Revision$
 * 
 * <p>
 * A Function that describes how to score different scenario constraints affect
 * a connector's score.
 * </p>.
 */
public class ScoreFunction {

  /* our log stream */
  private static Logger LOG = Logger.getLogger(ScoreFunction.class.getName());

  private String dcp = null;

  private String varX = null;

  private String varY = null;

  private int varXType = 0;

  private int varXFormat = 0;

  private Map<String, Float> points = null;

  public ScoreFunction(String dcp, String varX, String varXTypeId,
      String varXFormatId, String varY) {
    this.dcp = dcp;
    this.varX = varX;
    this.varY = varY;

    this.varXType = 0;
    if (varXTypeId.equals(ID_TYPE_DISCRETE)) {
      this.varXType = TYPE_DISCRETE;
    } else if (varXTypeId.equals(ID_TYPE_CONTINUOUS_APPROXIMATED)) {
      this.varXType = TYPE_CONTINUOUS_APPROXIMATED;
    } else {
      LOG.log(Level.WARNING, "Unknown score function variable type: ["
          + varXTypeId + "]");
    }

    this.varXFormat = 0;
    if (varXFormatId.equals(ID_FORMAT_NUMERIC)) {
      this.varXFormat = FORMAT_NUMERIC;
    } else if (varXFormatId.equals(ID_FORMAT_STRING)) {
      this.varXFormat = FORMAT_STRING;
    } else {
      LOG.log(Level.WARNING, "Unknown score function variable format: ["
          + varXFormatId + "]");
    }

    points = new HashMap<String, Float>();
  }

  public void updatePerformanceScoreMap(
      HashMap<String, ConnectorPerformanceScore> performanceMap,
      DistributionScenario scenario) {
    ConnectorPerformanceScore connectorPerformanceScore = performanceMap
        .get(dcp);
    if (connectorPerformanceScore != null) {
      String scenarioValue = extractScenarioValue(scenario, varX);
      float scoreIncrement = getValueForPoint(scenarioValue);
      updatePeformanceScore(connectorPerformanceScore, scoreIncrement);
    } else {
      // do some LOGGING
    }
  }

  private String extractScenarioValue(DistributionScenario scenario,
      String attribute) {
    String value = "";
    if (attribute.equals(VAR_TOTAL_VOLUME)) {
      String volumeHumanReadable = scenario.getTotalVolume().getValue();
      value = parseVolume(volumeHumanReadable);
    } else if (attribute.equals(VAR_NUMBER_OF_INTERVALS)) {
      value = String.valueOf(scenario.getDelivSchedule().getNumberOfIntervals()
          .getValue());
    } else if (attribute.equals(VAR_VOLUME_PER_INTERVAL)) {
      String volumeHumanReadable = scenario.getDelivSchedule()
          .getVolumePerInterval().getValue();
      value = parseVolume(volumeHumanReadable);
    } else if (attribute.equals(VAR_TIMING_OF_INTERVAL)) {
      // TODO: parse the TimeContract data
    } else if (attribute.equals(VAR_GEOGRAPHIC_DISTRIBUTION)) {
      if (scenario.getGeoDistribution() != null) {
        value = (String) scenario.getGeoDistribution().get(0);
      }
    } else if (attribute.equals(VAR_NUMBER_OF_USERS)) {
      value = scenario.getNumUsers().getValue();
    } else if (attribute.equals(VAR_NUMBER_OF_USER_TYPES)) {
      value = scenario.getNumUserTypes().getValue();
    } else if (attribute.equals(VAR_NUMBER_OF_DATA_TYPES)) {
      value = String.valueOf(scenario.getDataTypes().size());
    } else {
      // do some LOGGING
    }
    return value;
  }

  private void updatePeformanceScore(
      ConnectorPerformanceScore connectorPerformanceScore, float increment) {
    if (varY.equals(VAR_CONSISTENCY)) {
      connectorPerformanceScore.updateScoreConsistency(increment);
    } else if (varY.equals(VAR_DEPENDABILITY)) {
      connectorPerformanceScore.updateScoreDependability(increment);
    } else if (varY.equals(VAR_EFFICIENCY)) {
      connectorPerformanceScore.updateScoreEfficiency(increment);
    } else if (varY.equals(VAR_SCALABILITY)) {
      connectorPerformanceScore.updateScoreScalability(increment);
    } else {
      // do some LOGGING
    }
  }

  private String parseVolume(String humanReadable) {
    // this method converts a human readable volume into a number
    // representing the volume in mega bytes.
    // an input of "1GB" or "1 GB" gives "1000"
    String result = null;
    if (humanReadable == null) {
      LOG.log(Level.FINE, "humanReadable volume is null: must be a range");
      return null;
    }
    if (humanReadable.endsWith("MB")) {
      result = humanReadable.substring(0, humanReadable.length() - 2).trim();
    } else if (humanReadable.endsWith("GB")) {
      result = humanReadable.substring(0, humanReadable.length() - 2).trim()
          + "000";
    } else if (humanReadable.endsWith("TB")) {
      result = humanReadable.substring(0, humanReadable.length() - 2).trim()
          + "000000";
    } else if (humanReadable.endsWith("PB")) {
      result = humanReadable.substring(0, humanReadable.length() - 2).trim()
          + "000000000";
    } else {
      // do some LOGGING
    }
    return result;
  }

  public void addPoint(String xValue, String yValue) {
    points.put(xValue, Float.valueOf(yValue));
  }

  public float getValueForPoint(String pointX) {
    if (pointX == null || (pointX != null && pointX.equals("null"))
        || (pointX != null && pointX.equals(""))) {
      return 0.0f;
    }

    float retval = 0;
    if (points.containsKey(pointX)) {
      return points.get(pointX);
    } else {
      if (varXFormat == FORMAT_NUMERIC) {
        float xValue = Float.parseFloat(pointX);

        float leftX = 0, rightX = 0;
        float leftY = 0, rightY = 0;
        boolean gotLeft = false, gotRight = false;

        float floatValue = 0;
        Set entries = points.entrySet();
        Iterator iter = entries.iterator();
        while (iter.hasNext()) {
          Map.Entry entry = (Map.Entry) iter.next();
          floatValue = Float.parseFloat((String) entry.getKey());
          if (!gotLeft && floatValue < xValue) {
            gotLeft = true;
            leftX = floatValue;
            leftY = ((Float) entry.getValue()).floatValue();
          } else if (gotLeft && floatValue < xValue && floatValue > leftX) {
            leftX = floatValue;
            leftY = ((Float) entry.getValue()).floatValue();
          }
          if (!gotRight && floatValue > xValue) {
            gotRight = true;
            rightX = floatValue;
            rightY = ((Float) entry.getValue()).floatValue();
          } else if (gotRight && floatValue > xValue && floatValue < rightX) {
            rightX = floatValue;
            rightY = ((Float) entry.getValue()).floatValue();
          }
        }

        if (!gotLeft && gotRight) {
          retval = rightY;
        } else if (gotLeft && !gotRight) {
          retval = leftY;
        } else if (gotLeft && gotRight) {
          retval = (rightY - leftY) / (rightX - leftX) * (xValue - leftX);
        }
      } else {
        throw new RuntimeException("Cannot approximate x: ["+pointX+"] " +
        "format: [" + varXFormat + "]: function: [connector: ["+this.dcp+"]: varX: ["+this.varX+"]: " +
        "varY: ["+this.varY+"]]");
      }
    }

    return retval;
  }

  public float getValueForRange(String valueX1, String valueX2) {
    // this method will be used when we have values of type range (10GB - 20GB)
    // in the distribution scenario
    float retval = 0;
    return retval;
  }

  /**
   * @return the associated DCP
   */
  public String getDcp() {
    return this.dcp;
  }

  /**
   * @param dcp
   *          the DCP to set
   */
  public void setDcp(String dcp) {
    this.dcp = dcp;
  }

  /**
   * @return the X var
   */
  public String getVarX() {
    return this.varX;
  }

  /**
   * @param varX
   *          the X var to set
   */
  public void setVarX(String varX) {
    this.varX = varX;
  }

  /**
   * @return the X var type
   */
  public int getVarXType() {
    return this.varXType;
  }

  /**
   * @param varXType
   *          the X var type to set
   */
  public void setVarXType(int varXType) {
    this.varXType = varXType;
  }

  /**
   * @return the X var format
   */
  public int getVarXFormat() {
    return this.varXFormat;
  }

  /**
   * @param varXFormat
   *          the X var format to set
   */
  public void setVarXFormat(int varXFormat) {
    this.varXFormat = varXFormat;
  }

  /**
   * @return the Y var
   */
  public String getVarY() {
    return this.varY;
  }

  /**
   * @param varY
   *          the Y var to set
   */
  public void setVarY(String varY) {
    this.varY = varY;
  }

  /**
   * @return the function id
   */
  public String getId() {
    return this.dcp + "-" + this.varX + "-" + this.varY;
  }

  public static final int TYPE_DISCRETE = 1;

  public static final int TYPE_CONTINUOUS_APPROXIMATED = 2;

  public static final String ID_TYPE_DISCRETE = "discrete";

  public static final String ID_TYPE_CONTINUOUS_APPROXIMATED = "continuous-approximated";

  public static final int FORMAT_NUMERIC = 1;

  public static final int FORMAT_STRING = 2;

  public static final String ID_FORMAT_NUMERIC = "numeric";

  public static final String ID_FORMAT_STRING = "string";

  // TODO: we need to take care of multi-valued attributes as well
  public static final String VAR_TOTAL_VOLUME = "total_volume";

  public static final String VAR_NUMBER_OF_INTERVALS = "number_of_intervals";

  public static final String VAR_VOLUME_PER_INTERVAL = "volume_per_interval";

  public static final String VAR_TIMING_OF_INTERVAL = "timing_of_interval";

  public static final String VAR_GEOGRAPHIC_DISTRIBUTION = "geographic_distribution";

  public static final String VAR_NUMBER_OF_USERS = "number_of_users";

  public static final String VAR_NUMBER_OF_USER_TYPES = "number_of_user_types";

  // TODO: also need to have functions which map scores by existence of specific
  // user types
  public static final String VAR_NUMBER_OF_DATA_TYPES = "number_of_data_types";

  // TODO: also need to have functions wcich map scores by existence of specific
  // data types

  public static final String VAR_CONSISTENCY = "consistency";

  public static final String VAR_DEPENDABILITY = "dependability";

  public static final String VAR_EFFICIENCY = "efficiency";

  public static final String VAR_SCALABILITY = "scalability";
}
