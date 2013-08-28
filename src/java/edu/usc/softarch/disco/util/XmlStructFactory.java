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

package edu.usc.softarch.disco.util;

// JDK imports
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

// DISCO imports
import edu.usc.softarch.disco.connector.atomic.ArbitratorConnectorProfile;
import edu.usc.softarch.disco.connector.atomic.DataAccessConnectorProfile;
import edu.usc.softarch.disco.connector.atomic.DistributorConnectorProfile;
import edu.usc.softarch.disco.connector.atomic.EventConnectorProfile;
import edu.usc.softarch.disco.connector.atomic.ProcedureCallConnectorProfile;
import edu.usc.softarch.disco.connector.atomic.StreamConnectorProfile;
import edu.usc.softarch.disco.connector.dcp.ClientServerDistributionConnectorProfile;
import edu.usc.softarch.disco.connector.dcp.DistributionConnectorProfile;
import edu.usc.softarch.disco.connector.dcp.EventDistributionConnectorProfile;
import edu.usc.softarch.disco.connector.dcp.GridDistributionConnectorProfile;
import edu.usc.softarch.disco.connector.dcp.P2PDistributionConnectorProfile;
import edu.usc.softarch.disco.performance.PerformanceProfile;
import edu.usc.softarch.disco.performance.RawPerformanceData;
import edu.usc.softarch.disco.selection.bayes.BayesUtil;
import edu.usc.softarch.disco.selection.bayes.ConditionalProbabilityTable;
import edu.usc.softarch.disco.selection.bayes.RandomVariable;
import edu.usc.softarch.disco.selection.optimizing.CompetencyFunction;
import edu.usc.softarch.disco.selection.optimizing.OptimizingObjectiveFunction;
import edu.usc.softarch.disco.selection.scorebased.ScoreFunction;
import edu.usc.softarch.disco.selection.scorebased.ObjectiveFunction;
import edu.usc.softarch.disco.structs.AnswerKey;
import edu.usc.softarch.disco.structs.ArbitratorConcurrency;
import edu.usc.softarch.disco.structs.ArbitratorSecurity;
import edu.usc.softarch.disco.structs.ArbitratorTransactions;
import edu.usc.softarch.disco.structs.DataAccessAvailability;
import edu.usc.softarch.disco.structs.DataAccessCardinality;
import edu.usc.softarch.disco.structs.DeliveryPath;
import edu.usc.softarch.disco.structs.DeliverySchedule;
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.structs.DistributorDelivery;
import edu.usc.softarch.disco.structs.DistributorNaming;
import edu.usc.softarch.disco.structs.DistributorRouting;
import edu.usc.softarch.disco.structs.EventCardinality;
import edu.usc.softarch.disco.structs.Mode;
import edu.usc.softarch.disco.structs.PerformanceRequirements;
import edu.usc.softarch.disco.structs.Priority;
import edu.usc.softarch.disco.structs.ProcedureCallAccessibility;
import edu.usc.softarch.disco.structs.ProcedureCallCardinality;
import edu.usc.softarch.disco.structs.ProcedureCallEntryPoint;
import edu.usc.softarch.disco.structs.ProcedureCallInvocation;
import edu.usc.softarch.disco.structs.ProcedureCallParameters;
import edu.usc.softarch.disco.structs.RangedValue;
import edu.usc.softarch.disco.structs.ScenarioValue;
import edu.usc.softarch.disco.structs.StreamCardinality;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A Factory class for constructing Disco objects from XML nodes.
 * </p>.
 */
public final class XmlStructFactory {

  /* our log stream */
  private static Logger LOG = Logger
      .getLogger(XmlStructFactory.class.getName());

  private XmlStructFactory() throws InstantiationException {
    throw new InstantiationException("Don't construct factory objects!");
  }

  public static void writeDcpXmlFile(String filePath,
      DistributionConnectorProfile prof) {
    try {
      XMLUtils.writeXmlFile(getDCPDocument(prof), filePath);
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Error writing DCP: type: [" + prof.getType()
          + "]: Message: " + e.getMessage());
    }
  }

  public static void writeDcpXmlFileToStream(OutputStream os,
      DistributionConnectorProfile prof) {
    try {
      XMLUtils.writeXmlToStream(getDCPDocument(prof), os);
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Error writing DCP: type: [" + prof.getType()
          + "]: Message: " + e.getMessage());
    }
  }

  public static AnswerKey getAnswerKey(Node rootNode) {
    Element rootElem = (Element) rootNode;
    AnswerKey key = new AnswerKey();

    Element scenarioElem = getFirstElement("scenario", rootElem);
    String scenarioId = scenarioElem.getAttribute("id");

    Set<String> appropriate = getAppropriateSet(rootElem);
    Set<String> inappropriate = getInAppropriateSet(rootElem);

    key.setAppropriate(appropriate);
    key.setInappropriate(inappropriate);
    key.setScenarioId(scenarioId);
    return key;
  }

  public static PerformanceProfile getPerformanceProfile(Node rootNode) {
    Element rootElem = (Element) rootNode;

    PerformanceProfile prof = new PerformanceProfile();
    prof.setConnector(rootElem.getAttribute("connector"));
    Element scenariosElem = getFirstElement("scenarios", rootElem);

    if (scenariosElem == null)
      throw new RuntimeException("Must define scenarios element!");

    prof.setRawData(getRawDataList(scenariosElem, prof.getConnector()));

    return prof;

  }

  public static ScoreFunction getScoreFunction(Node rootNode) {
    Element rootElem = (Element) rootNode;

    Element metaDataNode = getFirstElement("metadata", rootElem);
    Element varXNode = getFirstElement("var_x", metaDataNode);
    Element varYNode = getFirstElement("var_y", metaDataNode);

    String dcpText = getElementText("dcp", metaDataNode);
    String varXId = varXNode.getAttribute("id");
    String varYId = varYNode.getAttribute("id");
    String varXType = varXNode.getAttribute("type");
    String varXFormat = varXNode.getAttribute("format");

    ScoreFunction scoreFunction = new ScoreFunction(dcpText, varXId, varXType,
        varXFormat, varYId);

    Element pointsNode = getFirstElement("points", rootElem);
    NodeList pointElemTags = pointsNode.getElementsByTagName("point");

    if (pointElemTags != null && pointElemTags.getLength() > 0) {
      for (int i = 0; i < pointElemTags.getLength(); i++) {
        Element pointElemTag = (Element) pointElemTags.item(i);
        String xVal = pointElemTag.getAttribute("x");
        String yVal = pointElemTag.getAttribute("y");

        if (xVal != null && yVal != null) {
          scoreFunction.addPoint(xVal, yVal);
        }
      }
    }

    return scoreFunction;
  }

  public static CompetencyFunction getCompetencyFunction(Node rootNode) {
    Element rootElem = (Element) rootNode;

    Element metaDataNode = getFirstElement("metadata", rootElem);
    Element varXNode = getFirstElement("var_x", metaDataNode);
    Element varYNode = getFirstElement("var_y", metaDataNode);

    String dcpText = getElementText("dcp", metaDataNode);
    String varXId = varXNode.getAttribute("id");
    String varYId = varYNode.getAttribute("id");
    String varXType = varXNode.getAttribute("type");
    String varXFormat = varXNode.getAttribute("format");

    CompetencyFunction competencyFunction = new CompetencyFunction(dcpText,
        varXId, varXType, varXFormat, varYId);

    Element pointsNode = getFirstElement("points", rootElem);
    NodeList pointElemTags = pointsNode.getElementsByTagName("point");

    if (pointElemTags != null && pointElemTags.getLength() > 0) {
      for (int i = 0; i < pointElemTags.getLength(); i++) {
        Element pointElemTag = (Element) pointElemTags.item(i);
        String xVal = pointElemTag.getAttribute("x");
        String yVal = pointElemTag.getAttribute("y");

        if (xVal != null && yVal != null) {
          competencyFunction.addPoint(xVal, yVal);
        }
      }
    }

    return competencyFunction;
  }

  public static ObjectiveFunction readObjectiveFunction(Node rootNode) {
    Element rootElem = (Element) rootNode;

    Float coefConsistency = Float.valueOf(getElementText("coefConsistency",
        rootElem));
    Float coefDependability = Float.valueOf(getElementText("coefDependability",
        rootElem));
    Float coefEfficiency = Float.valueOf(getElementText("coefEfficiency",
        rootElem));
    Float coefScalability = Float.valueOf(getElementText("coefScalability",
        rootElem));

    ObjectiveFunction objectiveFunction = new ObjectiveFunction(
        coefConsistency, coefDependability, coefEfficiency, coefScalability);

    return objectiveFunction;
  }

  public static OptimizingObjectiveFunction readOptimizingObjectiveFunction(
      Node rootNode) {
    Element rootElem = (Element) rootNode;

    Float coefConsistency = Float.valueOf(getElementText("coefConsistency",
        rootElem));
    Float coefDependability = Float.valueOf(getElementText("coefDependability",
        rootElem));
    Float coefEfficiency = Float.valueOf(getElementText("coefEfficiency",
        rootElem));
    Float coefScalability = Float.valueOf(getElementText("coefScalability",
        rootElem));

    OptimizingObjectiveFunction objectiveFunction = new OptimizingObjectiveFunction(
        coefConsistency, coefDependability, coefEfficiency, coefScalability);

    return objectiveFunction;
  }

  public static ClientServerDistributionConnectorProfile getClientServerDistributionConnectorProfile(
      Node rootNode) {
    Element rootElem = (Element) rootNode;
    ClientServerDistributionConnectorProfile profile = new ClientServerDistributionConnectorProfile(
        rootElem.getAttribute("name"));

    Element dataAccessNode = getFirstElement("dataaccess", rootElem);
    profile.setDataAccessProfile(getDataAccessConnectorProfile(dataAccessNode));
    Element streamNode = getFirstElement("stream", rootElem);
    profile.setStreamProfile(getStreamConnectorProfile(streamNode));
    Element procNode = getFirstElement("procedurecall", rootElem);
    profile.setProcedureCallProfile(getProcedureCallConnectorProfile(procNode));

    Element distributorNode = getFirstElement("distributor", rootElem);
    profile
        .setDistributorProfile(getDistributorConnectorProfile(distributorNode));

    return profile;

  }

  public static GridDistributionConnectorProfile getGridDistributionConnectorProfile(
      Node rootNode) {
    Element rootElem = (Element) rootNode;
    GridDistributionConnectorProfile profile = new GridDistributionConnectorProfile(
        rootElem.getAttribute("name"));

    Element dataAccessNode = getFirstElement("dataaccess", rootElem);
    profile.setDataAccessProfile(getDataAccessConnectorProfile(dataAccessNode));
    Element streamNode = getFirstElement("stream", rootElem);
    profile.setStreamProfile(getStreamConnectorProfile(streamNode));
    Element procNode = getFirstElement("procedurecall", rootElem);
    profile.setProcedureCallProfile(getProcedureCallConnectorProfile(procNode));

    Element distributorNode = getFirstElement("distributor", rootElem);
    profile
        .setDistributorProfile(getDistributorConnectorProfile(distributorNode));

    return profile;
  }

  public static P2PDistributionConnectorProfile getP2PDistributionConnectorProfile(
      Node rootNode) {
    Element rootElem = (Element) rootNode;
    P2PDistributionConnectorProfile profile = new P2PDistributionConnectorProfile(
        rootElem.getAttribute("name"));

    Element dataAccessNode = getFirstElement("dataaccess", rootElem);
    profile.setDataAccessProfile(getDataAccessConnectorProfile(dataAccessNode));
    Element streamNode = getFirstElement("stream", rootElem);
    profile.setStreamProfile(getStreamConnectorProfile(streamNode));
    Element arbitratorNode = getFirstElement("arbitrator", rootElem);
    profile.setArbitratorProfile(getArbitratorConnectorProfile(arbitratorNode));
    Element distributorNode = getFirstElement("distributor", rootElem);
    profile
        .setDistributorProfile(getDistributorConnectorProfile(distributorNode));

    return profile;
  }

  public static EventDistributionConnectorProfile getEventDistributionConnectorProfile(
      Node rootNode) {
    Element rootElem = (Element) rootNode;
    EventDistributionConnectorProfile profile = new EventDistributionConnectorProfile(
        rootElem.getAttribute("name"));

    Element eventNode = getFirstElement("event", rootElem);
    profile.setEventProfile(getEventConnectorProfile(eventNode));
    Element dataAccessNode = getFirstElement("dataaccess", rootElem);
    profile.setDataAccessProfile(getDataAccessConnectorProfile(dataAccessNode));
    Element streamNode = getFirstElement("stream", rootElem);
    profile.setStreamProfile(getStreamConnectorProfile(streamNode));
    Element distributorNode = getFirstElement("distributor", rootElem);
    profile
        .setDistributorProfile(getDistributorConnectorProfile(distributorNode));

    return profile;
  }

  public static ProcedureCallConnectorProfile getProcedureCallConnectorProfile(
      Node procNode) {
    Element procElem = (Element) procNode;
    ProcedureCallConnectorProfile profile = new ProcedureCallConnectorProfile();

    profile.setInvocation(getProcedureCallInvocation(procElem));
    profile.setAccessibility(getProcedureCallAccessibility(procElem));
    profile.setCardinality(getProcedureCallCardinality(procElem));
    profile.setEntryPoint(getProcedureCallEntryPoint(procElem));
    profile.setParams(getProcedureCallParams(procElem));
    profile.setSynchronicity(getElementText("synchronicity", procElem));

    return profile;
  }

  public static ArbitratorConnectorProfile getArbitratorConnectorProfile(
      Node arbNode) {
    Element arbElem = (Element) arbNode;
    ArbitratorConnectorProfile profile = new ArbitratorConnectorProfile();

    String faultHandling = getElementText("faulthandling", arbElem);
    String scheduling = getElementText("scheduling", arbElem);

    profile.setFaultHandling(faultHandling);
    profile.setScheduling(scheduling);
    profile.setTransactions(getArbitratorTransactions(getFirstElement(
        "transactions", (Element) arbElem)));
    profile.setConcurrency(getArbitratorConcurrency(getFirstElement(
        "concurrency", (Element) arbElem)));
    profile.setSecurity(getArbitratorSecurity(getFirstElement("security",
        (Element) arbElem)));

    return profile;
  }

  public static EventConnectorProfile getEventConnectorProfile(Node eventNode) {
    Element eventNodeElem = (Element) eventNode;
    EventConnectorProfile profile = new EventConnectorProfile();

    String delivery = getElementText("delivery", eventNodeElem);
    String notification = getElementText("notification", eventNodeElem);
    String synchronicity = getElementText("synchronicity", eventNodeElem);
    String causality = getElementText("causality", eventNodeElem);

    profile.setDelivery(delivery);
    profile.setNotification(notification);
    profile.setSynchronicity(synchronicity);
    profile.setCausality(causality);
    profile.setMode(getMode(getFirstElement("mode", (Element) eventNode)));
    profile.setCardinality(getEventCardinality(getFirstElement("cardinality",
        (Element) eventNode)));
    profile.setPriority(getPriority(getFirstElement("priority",
        (Element) eventNode)));

    return profile;
  }

  public static DataAccessConnectorProfile getDataAccessConnectorProfile(
      Node dataAccessNode) {
    Element dataAccessElem = (Element) dataAccessNode;
    DataAccessConnectorProfile profile = new DataAccessConnectorProfile();

    profile.setLocality(getElementText("locality", dataAccessElem));

    Element accessibilityElem = getFirstElement("accessibility", dataAccessElem);
    if (accessibilityElem.getAttribute("private").equals("true")) {
      profile.setPrivateAccessibility(true);
    }

    if (accessibilityElem.getAttribute("public").equals("true")) {
      profile.setPublicAccessibility(true);
    }

    profile.setAccesses(getAccesses(dataAccessElem));
    profile.setAvailabilities(getDataAccessAvailability(dataAccessElem));
    profile.setLifecycle(getElementText("lifecycle", dataAccessElem));
    profile.setCardinality(getDataAccessCardinality(dataAccessElem));

    return profile;

  }

  public static StreamConnectorProfile getStreamConnectorProfile(Node streamNode) {
    Element streamElem = (Element) streamNode;
    StreamConnectorProfile profile = new StreamConnectorProfile();

    profile.setDeliveries(getDeliveries(streamElem));
    profile.setBounds(getElementText("bounds", streamElem));
    profile.setBuffering(getElementText("buffering", streamElem));
    profile.setThroughput(getElementText("throughput", streamElem));
    profile.setState(getElementText("state", streamElem));
    profile.setIdentity(getElementText("identity", streamElem));
    profile.setLocalities(getLocalities(streamElem));
    profile.setSynchronicity(getElementText("synchronicity", streamElem));
    profile.setFormats(getFormats(streamElem));
    profile.setCardinality(getStreamCardinality(streamElem));

    return profile;
  }

  public static DistributorConnectorProfile getDistributorConnectorProfile(
      Node distributorNode) {
    Element distributorElem = (Element) distributorNode;
    DistributorConnectorProfile profile = new DistributorConnectorProfile();

    profile.setDelivery(getDistributorDelivery(distributorElem));
    profile.setNaming(getDistributorNaming(distributorElem));
    profile.setRouting(getDistributorRouting(distributorElem));

    return profile;
  }

  public static DistributionScenario getDistributionScenario(
      Node distScenarioNode) {
    Element distScenarioElem = (Element) distScenarioNode;

    // eight key dimensions w00t!
    DistributionScenario scenario = new DistributionScenario();
    scenario.setTotalVolume(getScenarioValue(getFirstElement("totalVolume",
        distScenarioElem)));
    scenario.setNumUsers(getScenarioValue(getFirstElement("numUsers",
        distScenarioElem)));
    scenario.setNumUserTypes(getScenarioValue(getFirstElement("userTypes",
        distScenarioElem)));
    DeliverySchedule schedule = getDeliverySchedule(getFirstElement(
        "deliverySchedule", distScenarioElem));
    scenario.setDelivSchedule(schedule);
    scenario.setGeoDistribution(getSimpleList(distScenarioElem,
        "geographicDistribution", "type"));
    scenario.setAccessPolicies(getSimpleList(distScenarioElem,
        "accessPolicies", "policy"));
    scenario.setDataTypes(getSimpleList(distScenarioElem, "dataTypes", "type"));
    if (getFirstElement("performancereqs", distScenarioElem) != null) {
      scenario.setPerformanceReqs(getPerformanceRequirements(getFirstElement(
          "performancereqs", distScenarioElem)));
    }

    return scenario;
  }

  private static Map<String, RawPerformanceData> getRawDataList(Node scenariosNode, String connName) {
    Element scenariosElement = (Element) scenariosNode;
    Map<String, RawPerformanceData> dataMap = new HashMap<String, RawPerformanceData>();

    NodeList scenarioNodeList = scenariosElement
        .getElementsByTagName("scenario");
    if (scenarioNodeList != null && scenarioNodeList.getLength() > 0) {
      for (int i = 0; i < scenarioNodeList.getLength(); i++) {
        Element scenarioElem = (Element)scenarioNodeList.item(i);
        String scenarioId = scenarioElem.getAttribute("id");
        RawPerformanceData data = getRawData(scenarioNodeList.item(i));
        data.setConnName(connName);
        dataMap.put(scenarioId, data);
      }
    }
    return dataMap;
  }

  private static RawPerformanceData getRawData(Node scenarioNode) {
    Element scenarioElem = (Element) scenarioNode;
    RawPerformanceData data = new RawPerformanceData();

    DistributionScenario scen = new DistributionScenario();
    scen.setName(scenarioElem.getAttribute("id"));
    data.setScenario(scen);

    // efficiency
    Element effElem = getFirstElement("efficiency", scenarioElem);
    data.setConnBps(Long.valueOf(effElem.getAttribute("bps")).longValue());
    data
        .setConnAvgMem(Long.valueOf(effElem.getAttribute("avgMem")).longValue());

    // consistency
    Element consElem = getFirstElement("consistency", scenarioElem);
    data.setConnBytesActualTransfer(Long.valueOf(
        consElem.getAttribute("bytesSent")).longValue());

    // dependability
    Element depElem = getFirstElement("dependability", scenarioElem);
    data.setConnNumFaults(Integer.valueOf(depElem.getAttribute("numFaults"))
        .intValue());

    // scalability
    Element scalElem = getFirstElement("scalability", scenarioElem);
    data.getScenario().getNumUsers()
        .setValue(scalElem.getAttribute("numUsers"));
    data.getScenario().getTotalVolume().setValue(
        scalElem.getAttribute("totalVolume"));
    data.getScenario().getNumUserTypes().setValue(
        scalElem.getAttribute("userTypes"));

    return data;

  }

  private static PerformanceRequirements getPerformanceRequirements(Node reqNode) {
    Element reqElem = (Element) reqNode;
    PerformanceRequirements reqs = new PerformanceRequirements();
    reqs.setConsistency(getElementText("consistency", reqElem));
    reqs.setDependability(getElementText("dependability", reqElem));
    reqs.setEfficiency(getElementText("efficiency", reqElem));
    reqs.setScalability(getElementText("scalability", reqElem));

    return reqs;
  }

  private static ScenarioValue getScenarioValue(Element elem) {
    ScenarioValue val = new ScenarioValue();
    if (elem == null) {
      return val;
    }

    if (elem.getAttribute("valtype") != null
        && elem.getAttribute("valtype").equals("range")) {
      RangedValue rVal = getRangedValue(elem);
      val.setRangeVal(rVal);
    } else {
      val.setValue(getSimpleElementText(elem));
    }

    return val;
  }

  private static Set<String> getAppropriateSet(Node answerKeyNode) {
    Element answerKeyElem = (Element) answerKeyNode;
    return getConnectorSet(getFirstElement("appropriate", answerKeyElem));
  }

  private static Set<String> getInAppropriateSet(Node answerKeyNode) {
    Element answerKeyElem = (Element) answerKeyNode;
    return getConnectorSet(getFirstElement("inappropriate", answerKeyElem));
  }

  private static Set<String> getConnectorSet(Node setNode) {
    Element setElem = (Element) setNode;
    Set<String> set = new TreeSet<String>();

    NodeList connectorNodes = setElem.getElementsByTagName("connector");

    if (connectorNodes != null && connectorNodes.getLength() > 0) {
      for (int i = 0; i < connectorNodes.getLength(); i++) {
        Element connectorElem = (Element) connectorNodes.item(i);
        set.add(connectorElem.getAttribute("id"));
      }
    }

    return set;
  }

  private static RangedValue getRangedValue(Node dimensionNode) {
    Element dimensionElem = (Element) dimensionNode;

    Element rangeElem = getFirstElement("range", dimensionElem);

    RangedValue rVal = null;

    if (rangeElem != null) {
      rVal = new RangedValue();

      Element lowerBoundElem = getFirstElement("lowerbound", rangeElem);
      Element upperBoundElem = getFirstElement("upperbound", rangeElem);

      if (lowerBoundElem == null && upperBoundElem == null) {
        throw new RuntimeException("There must be at least an upperbound "
            + "or a lower bound on a range");
      }

      if (lowerBoundElem != null) {
        boolean inclusive = Boolean.valueOf(lowerBoundElem
            .getAttribute("inclusive"));
        rVal.setMinInclusive(inclusive);
        rVal.setMinValue(getSimpleElementText(lowerBoundElem));
      }

      if (upperBoundElem != null) {
        boolean inclusive = Boolean.valueOf(upperBoundElem
            .getAttribute("inclusive"));
        rVal.setMaxInclusive(inclusive);
        rVal.setMaxValue(getSimpleElementText(upperBoundElem));
      }
    }

    return rVal;

  }

  public static Map getValRange(Node valRangeNode) {
    Element valRangeElem = (Element) valRangeNode;
    HashMap valRange = new HashMap();

    NodeList attrElemTags = valRangeElem.getElementsByTagName("attribute");

    if (attrElemTags != null && attrElemTags.getLength() > 0) {
      for (int i = 0; i < attrElemTags.getLength(); i++) {
        Element attrElemTag = (Element) attrElemTags.item(i);
        String attrName = attrElemTag.getAttribute("name");
        List attrValRange = getValRangeList(attrElemTag);

        if (attrName != null && attrValRange != null) {
          valRange.put(attrName, attrValRange);
        }
      }
    }

    return valRange;

  }

  public static ConditionalProbabilityTable getConditionalProbabilityTable(
      Node probNode) {
    Element probElement = (Element) probNode;

    ConditionalProbabilityTable tab = null;

    NodeList condProbElems = probElement
        .getElementsByTagName("conditionalProb");

    if (condProbElems != null && condProbElems.getLength() > 0) {
      tab = new ConditionalProbabilityTable();

      for (int i = 0; i < condProbElems.getLength(); i++) {
        Element condProbElem = (Element) condProbElems.item(i);
        double prob = Double.valueOf(condProbElem.getAttribute("prob"))
            .doubleValue();
        // LOG.log(Level.INFO, "read prob: "+prob);
        RandomVariable obsVar = getObservedVariable(condProbElem);
        RandomVariable sampleVar = getSampleVariable(condProbElem);

        if (obsVar == null || sampleVar == null) {
          throw new RuntimeException(
              "Obs var or Sample var is null when reading probability table");
        }

        BayesUtil.addConditionalProbability(tab, obsVar.getName(), obsVar
            .getValue(), sampleVar.getName(), sampleVar.getValue(), prob);
      }
    }

    return tab;
  }

  private static DeliverySchedule getDeliverySchedule(Node delivSchedNode) {
    Element delivScheduleElem = (Element) delivSchedNode;
    DeliverySchedule schedule = new DeliverySchedule();
    schedule.setNumberOfIntervals(getScenarioValue(getFirstElement(
        "numIntervals", delivScheduleElem)));
    schedule.setVolumePerInterval(getScenarioValue(getFirstElement(
        "volumePerInterval", delivScheduleElem)));
    return schedule;

  }

  private static List getValRangeList(Node attrNode) {
    Element attrElem = (Element) attrNode;

    List valRangeList = null;
    NodeList vals = attrElem.getElementsByTagName("val");

    if (vals != null && vals.getLength() > 0) {
      valRangeList = new Vector(vals.getLength());
      for (int i = 0; i < vals.getLength(); i++) {
        Element valElem = (Element) vals.item(i);
        String val = valElem.getAttribute("name");
        valRangeList.add(val);
      }
    }

    return valRangeList;
  }

  private static RandomVariable getSampleVariable(Node condProbNode) {
    return getRandomVariable(getFirstElement("sample", (Element) condProbNode));
  }

  private static RandomVariable getObservedVariable(Node condProbNode) {
    return getRandomVariable(getFirstElement("obs", (Element) condProbNode));
  }

  private static RandomVariable getRandomVariable(Element elem) {
    RandomVariable var = new RandomVariable();
    if (elem != null) {
      // get the var name and the value
      String varName = elem.getAttribute("var");
      String varValue = elem.getAttribute("value");
      var.setName(varName);
      var.setValue(varValue);
    }

    return var;
  }

  private static ProcedureCallParameters getProcedureCallParams(Node procNode) {
    ProcedureCallParameters params = new ProcedureCallParameters();

    Element paramElem = getFirstElement("parameters", (Element) procNode);
    params.setDataTransferMethods(getSimpleList(paramElem, "datatransfer",
        "method"));
    params.setSemantics(getElementText("semantics", paramElem));
    params.setInvocationRecord(getElementText("invocationrecord", paramElem));
    params.setReturnValue(getElementText("returnvalue", paramElem));

    return params;
  }

  private static ProcedureCallEntryPoint getProcedureCallEntryPoint(
      Node procNode) {
    ProcedureCallEntryPoint point = new ProcedureCallEntryPoint();
    Element entryPointElem = getFirstElement("entrypoint", (Element) procNode);
    String single = getElementText("single", entryPointElem);
    point.setSingle(single);
    return point;
  }

  private static ProcedureCallCardinality getProcedureCallCardinality(
      Node procNode) {
    ProcedureCallCardinality cardinality = new ProcedureCallCardinality();
    Element cardinalityElem = getFirstElement("cardinality", (Element) procNode);
    cardinality.setReceivers(cardinalityElem.getAttribute("receiver"));
    cardinality.setSenders(cardinalityElem.getAttribute("sender"));
    return cardinality;
  }

  private static ProcedureCallAccessibility getProcedureCallAccessibility(
      Node procNode) {
    ProcedureCallAccessibility accessibility = new ProcedureCallAccessibility();
    Element accessElem = getFirstElement("accessibility", (Element) procNode);
    accessibility.setPrivateAccess(Boolean.valueOf(
        accessElem.getAttribute("private")).booleanValue());
    accessibility.setPublicAccess(Boolean.valueOf(
        accessElem.getAttribute("public")).booleanValue());
    accessibility.setProtectedAccess(Boolean.valueOf(
        accessElem.getAttribute("protected")).booleanValue());
    return accessibility;
  }

  private static ProcedureCallInvocation getProcedureCallInvocation(
      Node procNode) {
    ProcedureCallInvocation invocation = new ProcedureCallInvocation();
    Element invocElem = getFirstElement("invocation", (Element) procNode);
    invocation.setExplicit(getElementText("explicit", invocElem));
    return invocation;
  }

  private static DistributorRouting getDistributorRouting(Node distributorNode) {
    Element distributorElem = (Element) distributorNode;
    DistributorRouting routing = new DistributorRouting();
    Element distributorRouting = getFirstElement("routing", distributorElem);

    routing.setType(distributorRouting.getAttribute("type"));
    routing.setMembership(getElementText("membership", distributorRouting));
    routing.setPath(getDeliveryPath(distributorRouting));

    return routing;

  }

  private static DeliveryPath getDeliveryPath(Node distributorNode) {
    Element distributorElem = (Element) distributorNode;
    DeliveryPath path = new DeliveryPath();
    Element pathElement = getFirstElement("path", distributorElem);

    if (pathElement.getAttribute("static").equals("true")) {
      path.setStaticPath(true);
    }

    if (pathElement.getAttribute("dynamic").equals("true")) {
      path.setDynamicPath(true);
    }

    if (pathElement.getAttribute("cached").equals("true")) {
      path.setCachedPath(true);
    }

    return path;
  }

  private static DistributorNaming getDistributorNaming(Node distributorNode) {
    Element distributorElem = (Element) distributorNode;
    DistributorNaming naming = new DistributorNaming();
    Element distributorNaming = getFirstElement("naming", distributorElem);

    naming.setStructures(getSimpleList(distributorNaming, "structure"));
    naming.setType(distributorNaming.getAttribute("type"));

    return naming;
  }

  private static DistributorDelivery getDistributorDelivery(Node distributorNode) {
    Element distributorElem = (Element) distributorNode;
    DistributorDelivery delivery = new DistributorDelivery();
    Element deliveryElement = getFirstElement("delivery", distributorElem);

    delivery.setType(deliveryElement.getAttribute("type"));
    delivery.setSemantics(getSemantics(deliveryElement));
    delivery.setMechanisms(getMechanisms(deliveryElement));

    return delivery;
  }

  private static List getMechanisms(Node deliveryNode) {
    return getSimpleList(deliveryNode, "mechanisms", "mechanism");
  }

  private static List getSemantics(Node deliveryNode) {
    return getSimpleList(deliveryNode, "semantics", "semantic");
  }

  private static StreamCardinality getStreamCardinality(Node streamNode) {
    Element streamElem = (Element) streamNode;
    StreamCardinality cardinality = new StreamCardinality();

    Element cardinalityElement = getFirstElement("cardinality", streamElem);
    cardinality.setReceiverCardinality(cardinalityElement
        .getAttribute("receivers"));
    cardinality
        .setSenderCardinality(cardinalityElement.getAttribute("senders"));

    return cardinality;
  }

  private static ArbitratorTransactions getArbitratorTransactions(Node arbNode) {
    ArbitratorTransactions transactions = new ArbitratorTransactions();
    transactions.setAwareness(getElementText("awareness", (Element) arbNode));
    transactions.setNesting(getElementText("nesting", (Element) arbNode));
    transactions.setIsolation(getElementText("isolation", (Element) arbNode));
    return transactions;

  }

  private static ArbitratorConcurrency getArbitratorConcurrency(Node arbNode) {
    ArbitratorConcurrency concurrency = new ArbitratorConcurrency();
    concurrency.setMechanism(getElementText("mechanism", (Element) arbNode));
    concurrency.setWeight(getElementText("weight", (Element) arbNode));
    concurrency.setType(((Element) arbNode).getAttribute("type"));
    return concurrency;
  }

  private static ArbitratorSecurity getArbitratorSecurity(Node arbNode) {
    ArbitratorSecurity security = new ArbitratorSecurity();

    security.setRequired(Boolean.valueOf(
        ((Element) arbNode).getAttribute("required")).booleanValue());
    security.setAuthentication(getElementText("authentication",
        (Element) arbNode));
    security.setAuthorizations(getSimpleList(arbNode, "authorization",
        "authType"));

    security.setIntegrity(getElementText("integrity", (Element) arbNode));
    security.setDurability(getElementText("durability", (Element) arbNode));

    return security;
  }

  private static List getFormats(Node streamElem) {
    return getSimpleList(streamElem, "formats", "format");
  }

  private static List getLocalities(Node streamElem) {
    return getSimpleList(streamElem, "localities", "locality");
  }

  private static List getDeliveries(Node streamElem) {
    return getSimpleList(streamElem, "deliveries", "delivery");
  }

  private static DataAccessCardinality getDataAccessCardinality(
      Node cardinalityNode) {
    Element cardElem = (Element) cardinalityNode;
    DataAccessCardinality cardinality = new DataAccessCardinality();

    Element cardinalityElement = getFirstElement("cardinality", cardElem);

    cardinality.setReceiverCardinality(cardinalityElement
        .getAttribute("receivers"));
    cardinality
        .setSenderCardinality(cardinalityElement.getAttribute("senders"));

    return cardinality;
  }

  private static List getAccesses(Node accessNode) {
    return getSimpleList(accessNode, "accesses", "access");
  }

  private static DataAccessAvailability getDataAccessAvailability(Node availNode) {
    DataAccessAvailability availability = new DataAccessAvailability();
    Element dataAccessElem = (Element) availNode;

    availability.setAvailTransient(getElementText("transient", dataAccessElem));
    availability.setPersistences(getPersistents(dataAccessElem));

    return availability;
  }

  private static List getPersistents(Node dataAccessNode) {
    return getSimpleList(dataAccessNode, "persistents", "persistent");
  }

  private static Priority getPriority(Node node) {
    Element priorityElem = (Element) node;
    Priority priority = new Priority();

    Element outgoingElem = getFirstElement("outgoing", priorityElem);
    Element incomingElem = getFirstElement("incoming", priorityElem);
    priority.setIncoming(incomingElem.getAttribute("setby"));
    priority.setOutgoing(outgoingElem.getAttribute("setby"));
    return priority;

  }

  private static EventCardinality getEventCardinality(Node node) {
    EventCardinality cardinality = new EventCardinality();
    Element ecElem = (Element) node;

    cardinality.setObserverCardinality(ecElem.getAttribute("observers"));
    cardinality.setProducerCardinality(ecElem.getAttribute("producers"));
    cardinality.setProducers(getElementText("producers", ecElem));
    cardinality.setObservers(getElementText("observers", ecElem));
    cardinality.setEventPatterns(getEventPatterns(ecElem));

    return cardinality;

  }

  private static List getEventPatterns(Node eventPatternsNode) {
    return getSimpleList(eventPatternsNode, "eventpatterns", "pattern");
  }

  private static Mode getMode(Node node) {
    Element modeElem = (Element) node;
    Mode mode = new Mode();

    mode.setType(modeElem.getAttribute("type"));
    mode.setSoftwares(getSimpleList(modeElem, "software"));
    return mode;

  }

  private static org.w3c.dom.Element getFirstElement(String name,
      org.w3c.dom.Element root) {
    NodeList list = root.getElementsByTagName(name);
    if (list != null) {
      return (org.w3c.dom.Element) list.item(0);
    } else
      return null;
  }

  private static String getSimpleElementText(org.w3c.dom.Element node) {
    if (node.getChildNodes().item(0) instanceof Text) {
      return node.getChildNodes().item(0).getNodeValue();
    } else
      return null;
  }

  private static String getElementText(String elemName, org.w3c.dom.Element root) {
    org.w3c.dom.Element elem = getFirstElement(elemName, root);
    if (elem != null) {
      return getSimpleElementText(elem);
    } else
      return null;
  }

  private static List getSimpleList(Node node, String listItemName) {
    if (node == null) {
      return null;
    }
    Element nodeElem = (Element) node;
    List itemList = null;

    NodeList listNodeList = nodeElem.getElementsByTagName(listItemName);

    if (listNodeList != null && listNodeList.getLength() > 0) {
      itemList = new Vector(listNodeList.getLength());

      for (int i = 0; i < listNodeList.getLength(); i++) {
        Element itemElem = ((Element) listNodeList.item(i));
        itemList.add(getSimpleElementText(itemElem));
      }
    }

    return itemList;
  }

  private static List getSimpleList(Node node, String outerListName,
      String listItemName) {
    if (node == null) {
      return null;
    }
    Element nodeElem = (Element) node;
    Element outerListElement = getFirstElement(outerListName, nodeElem);
    return getSimpleList(outerListElement, listItemName);
  }

  private static Document getDCPDocument(DistributionConnectorProfile prof) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    Document document = null;

    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      document = builder.newDocument();

      Element root = (Element) document.createElement("disco:connector");
      root.setAttribute("xmlns:disco", "http://softarch.usc.edu/1.0/disco");
      root.setAttribute("name", prof.getConnectorName());
      root.setAttribute("class", prof.getType());
      document.appendChild(root);

      // depending on what type of DCP it is, add the appropriate "atomic"
      // profiles
      if (prof.getType().equals(DistributionConnectorProfile.P2P_TYPE)) {
        P2PDistributionConnectorProfile profile = (P2PDistributionConnectorProfile) prof;
        addArbitratorMetadataToDoc(document, profile.getArbitratorProfile());
        addDataAccessMetadataToDoc(document, prof.getDataAccessProfile());
        addDistributorMetadataToDoc(document, prof.getDistributorProfile());
        addStreamMetadataToDoc(document, prof.getStreamProfile());
      } else if (prof.getType().equals(DistributionConnectorProfile.CS_TYPE)) {
        ClientServerDistributionConnectorProfile profile = (ClientServerDistributionConnectorProfile) prof;
        addProcedureCallMetadataToDoc(document, profile
            .getProcedureCallProfile());
        addDataAccessMetadataToDoc(document, prof.getDataAccessProfile());
        addDistributorMetadataToDoc(document, prof.getDistributorProfile());
        addStreamMetadataToDoc(document, prof.getStreamProfile());
      } else if (prof.getType().equals(DistributionConnectorProfile.GRID_TYPE)) {
        GridDistributionConnectorProfile profile = (GridDistributionConnectorProfile) prof;
        addProcedureCallMetadataToDoc(document, profile
            .getProcedureCallProfile());
        addDataAccessMetadataToDoc(document, prof.getDataAccessProfile());
        addDistributorMetadataToDoc(document, prof.getDistributorProfile());
        addStreamMetadataToDoc(document, prof.getStreamProfile());
      } else if (prof.getType().equals(DistributionConnectorProfile.EVENT_TYPE)) {
        EventDistributionConnectorProfile profile = (EventDistributionConnectorProfile) prof;
        addEventMetadataToDoc(document, profile.getEventProfile());
        addDataAccessMetadataToDoc(document, prof.getDataAccessProfile());
        addDistributorMetadataToDoc(document, prof.getDistributorProfile());
        addStreamMetadataToDoc(document, prof.getStreamProfile());
      } else
        throw new UnsupportedOperationException("Unknown profile type: ["
            + prof.getType() + "]");

      return document;
    } catch (ParserConfigurationException pce) {
      LOG.log(Level.WARNING, "Error generating DCP xml file!: "
          + pce.getMessage());
    }

    return null;
  }

  private static void addEventMetadataToDoc(Document doc,
      EventConnectorProfile eventProf) {
    Element docRoot = doc.getDocumentElement();
    Element eventRoot = XMLUtils.addNode(doc, docRoot, "event");

    XMLUtils.addNode(doc, eventRoot, "delivery", eventProf.getDelivery());
    XMLUtils.addNode(doc, eventRoot, "synchronicity", eventProf
        .getSynchronicity());
    XMLUtils.addNode(doc, eventRoot, "notification", eventProf
        .getNotification());
    XMLUtils.addNode(doc, eventRoot, "causality",
        eventProf.getCausality() != null ? eventProf.getCausality() : "");

    Element cardinalityElem = XMLUtils.addNode(doc, eventRoot, "cardinality");
    XMLUtils.addAttribute(doc, cardinalityElem, "producers", eventProf
        .getCardinality().getProducerCardinality());
    XMLUtils.addAttribute(doc, cardinalityElem, "observers", eventProf
        .getCardinality().getObserverCardinality());
    XMLUtils.addNode(doc, cardinalityElem, "producers", eventProf
        .getCardinality().getProducers());
    XMLUtils.addNode(doc, cardinalityElem, "observers", eventProf
        .getCardinality().getObservers());

    Element eventPatternsElem = XMLUtils.addNode(doc, cardinalityElem,
        "eventpatterns");
    if (eventProf.getCardinality().getEventPatterns() != null
        && eventProf.getCardinality().getEventPatterns().size() > 0) {
      for (Iterator i = eventProf.getCardinality().getEventPatterns()
          .iterator(); i.hasNext();) {
        String pattern = (String) i.next();
        XMLUtils.addNode(doc, eventPatternsElem, "pattern", pattern);
      }
    }

    Element priorityElem = XMLUtils.addNode(doc, eventRoot, "priority");
    Element outgoingElem = XMLUtils.addNode(doc, priorityElem, "outgoing");
    XMLUtils.addAttribute(doc, outgoingElem, "setby", eventProf.getPriority()
        .getOutgoing());
    Element incomingElem = XMLUtils.addNode(doc, priorityElem, "incoming");
    XMLUtils.addAttribute(doc, incomingElem, "setby", eventProf.getPriority()
        .getIncoming());

    Element modeElem = XMLUtils.addNode(doc, eventRoot, "mode");
    XMLUtils.addAttribute(doc, modeElem, "type", eventProf.getMode().getType());

    if (eventProf.getMode().getSoftwares() != null
        && eventProf.getMode().getSoftwares().size() > 0) {
      for (Iterator i = eventProf.getMode().getSoftwares().iterator(); i
          .hasNext();) {
        String software = (String) i.next();
        XMLUtils.addNode(doc, modeElem, "software", software);
      }
    }

  }

  private static void addProcedureCallMetadataToDoc(Document doc,
      ProcedureCallConnectorProfile pcProf) {

    Element docRoot = doc.getDocumentElement();
    Element procRoot = XMLUtils.addNode(doc, docRoot, "procedurecall");

    Element parametersElem = XMLUtils.addNode(doc, procRoot, "parameters");
    Element dataTransferElem = XMLUtils.addNode(doc, parametersElem,
        "datatransfer");

    if (pcProf.getParams().getDataTransferMethods() != null
        && pcProf.getParams().getDataTransferMethods().size() > 0) {
      for (Iterator i = pcProf.getParams().getDataTransferMethods().iterator(); i
          .hasNext();) {
        String method = (String) i.next();
        XMLUtils.addNode(doc, dataTransferElem, "method", method);
      }
    }

    XMLUtils.addNode(doc, parametersElem, "semantics", pcProf.getParams()
        .getSemantics());
    XMLUtils.addNode(doc, parametersElem, "returnvalue", pcProf.getParams()
        .getReturnValue());
    XMLUtils.addNode(doc, parametersElem, "invocationrecord", pcProf
        .getParams().getInvocationRecord());

    Element entryPointElem = XMLUtils.addNode(doc, procRoot, "entrypoint");
    XMLUtils.addNode(doc, entryPointElem, "single", pcProf.getEntryPoint()
        .getSingle());

    Element invocationElem = XMLUtils.addNode(doc, procRoot, "invocation");
    XMLUtils.addNode(doc, invocationElem, "explicit", pcProf.getInvocation()
        .getExplicit());
    XMLUtils.addNode(doc, procRoot, "synchronicity", pcProf.getSynchronicity());
    Element cardinalityElem = XMLUtils.addNode(doc, procRoot, "cardinality");
    XMLUtils.addAttribute(doc, cardinalityElem, "sender", pcProf
        .getCardinality().getSenders());
    XMLUtils.addAttribute(doc, cardinalityElem, "receiver", pcProf
        .getCardinality().getReceivers());
    Element accessibilityElem = XMLUtils
        .addNode(doc, procRoot, "accessibility");
    XMLUtils.addAttribute(doc, accessibilityElem, "private", String
        .valueOf(pcProf.getAccessibility().isPrivateAccess()));
    XMLUtils.addAttribute(doc, accessibilityElem, "public", String
        .valueOf(pcProf.getAccessibility().isPublicAccess()));
    XMLUtils.addAttribute(doc, accessibilityElem, "protected", String
        .valueOf(pcProf.getAccessibility().isProtectedAccess()));

  }

  private static void addStreamMetadataToDoc(Document doc,
      StreamConnectorProfile streamProf) {
    Element docRoot = doc.getDocumentElement();
    Element streamRoot = XMLUtils.addNode(doc, docRoot, "stream");

    Element deliveriesElem = XMLUtils.addNode(doc, streamRoot, "deliveries");

    if (streamProf.getDeliveries() != null
        && streamProf.getDeliveries().size() > 0) {
      for (Iterator i = streamProf.getDeliveries().iterator(); i.hasNext();) {
        String delivery = (String) i.next();
        XMLUtils.addNode(doc, deliveriesElem, "delivery", delivery);
      }
    }

    XMLUtils.addNode(doc, streamRoot, "bounds", streamProf.getBounds());
    XMLUtils.addNode(doc, streamRoot, "buffering", streamProf.getBuffering());
    XMLUtils.addNode(doc, streamRoot, "throughput", streamProf.getThroughput());
    XMLUtils.addNode(doc, streamRoot, "state", streamProf.getState());
    XMLUtils.addNode(doc, streamRoot, "identity", streamProf.getIdentity());
    XMLUtils.addNode(doc, streamRoot, "synchronicity", streamProf
        .getSynchronicity());

    Element localitiesElem = XMLUtils.addNode(doc, streamRoot, "localities");

    if (streamProf.getLocalities() != null
        && streamProf.getLocalities().size() > 0) {
      for (Iterator i = streamProf.getLocalities().iterator(); i.hasNext();) {
        String locality = (String) i.next();
        XMLUtils.addNode(doc, localitiesElem, "locality", locality);
      }
    }

    Element formatsElem = XMLUtils.addNode(doc, streamRoot, "formats");

    if (streamProf.getFormats() != null && streamProf.getFormats().size() > 0) {
      for (Iterator i = streamProf.getFormats().iterator(); i.hasNext();) {
        String format = (String) i.next();
        XMLUtils.addNode(doc, formatsElem, "format", format);
      }
    }

    Element cardinalityElem = XMLUtils.addNode(doc, streamRoot, "cardinality");
    XMLUtils.addAttribute(doc, cardinalityElem, "senders", streamProf
        .getCardinality().getSenderCardinality());
    XMLUtils.addAttribute(doc, cardinalityElem, "receivers", streamProf
        .getCardinality().getReceiverCardinality());

  }

  private static void addDistributorMetadataToDoc(Document doc,
      DistributorConnectorProfile distProf) {
    Element docRoot = doc.getDocumentElement();
    Element distRoot = XMLUtils.addNode(doc, docRoot, "distributor");

    Element namingElem = XMLUtils.addNode(doc, distRoot, "naming");
    XMLUtils.addAttribute(doc, namingElem, "type", distProf.getNaming()
        .getType());

    if (distProf.getNaming().getStructures() != null
        && distProf.getNaming().getStructures().size() > 0) {
      for (Iterator i = distProf.getNaming().getStructures().iterator(); i
          .hasNext();) {
        String structure = (String) i.next();
        XMLUtils.addNode(doc, namingElem, "structure", structure);
      }

    }

    Element deliveryElement = XMLUtils.addNode(doc, distRoot, "delivery");
    XMLUtils.addAttribute(doc, deliveryElement, "type", distProf.getDelivery()
        .getType());
    Element semanticsElem = XMLUtils.addNode(doc, deliveryElement, "semantics");

    if (distProf.getDelivery().getSemantics() != null
        && distProf.getDelivery().getSemantics().size() > 0) {
      for (Iterator i = distProf.getDelivery().getSemantics().iterator(); i
          .hasNext();) {
        String semantic = (String) i.next();
        XMLUtils.addNode(doc, semanticsElem, "semantic", semantic);
      }
    }

    Element mechanismElem = XMLUtils
        .addNode(doc, deliveryElement, "mechanisms");

    if (distProf.getDelivery().getMechanisms() != null
        && distProf.getDelivery().getMechanisms().size() > 0) {
      for (Iterator i = distProf.getDelivery().getMechanisms().iterator(); i
          .hasNext();) {
        String mechanism = (String) i.next();
        XMLUtils.addNode(doc, mechanismElem, "mechanism", mechanism);
      }
    }

    Element routingElem = XMLUtils.addNode(doc, distRoot, "routing");
    XMLUtils.addAttribute(doc, routingElem, "type", distProf.getRouting()
        .getType());
    XMLUtils.addNode(doc, routingElem, "membership", distProf.getRouting()
        .getMembership());

    Element pathElem = XMLUtils.addNode(doc, routingElem, "path");
    XMLUtils.addAttribute(doc, pathElem, "static", String.valueOf(distProf
        .getRouting().getPath().isStaticPath()));
    XMLUtils.addAttribute(doc, pathElem, "dynamic", String.valueOf(distProf
        .getRouting().getPath().isDynamicPath()));
    XMLUtils.addAttribute(doc, pathElem, "cached", String.valueOf(distProf
        .getRouting().getPath().isCachedPath()));
  }

  private static void addDataAccessMetadataToDoc(Document doc,
      DataAccessConnectorProfile dataProf) {
    Element docRoot = doc.getDocumentElement();
    Element dataRoot = XMLUtils.addNode(doc, docRoot, "dataaccess");
    XMLUtils.addNode(doc, dataRoot, "locality", dataProf.getLocality());
    XMLUtils.addNode(doc, dataRoot, "lifecycle",
        dataProf.getLifecycle() != null ? dataProf.getLifecycle() : "");
    Element accessesElem = XMLUtils.addNode(doc, dataRoot, "accesses");

    if (dataProf.getAccesses() != null && dataProf.getAccesses().size() > 0) {
      for (Iterator i = dataProf.getAccesses().iterator(); i.hasNext();) {
        String access = (String) i.next();
        XMLUtils.addNode(doc, accessesElem, "access", access);
      }
    }

    Element availabilitiesElem = XMLUtils.addNode(doc, dataRoot,
        "availabilities");
    XMLUtils.addNode(doc, availabilitiesElem, "transient", dataProf
        .getAvailabilities().getAvailTransient());
    Element persistentsElem = XMLUtils.addNode(doc, availabilitiesElem,
        "persistents");

    if (dataProf.getAvailabilities().getPersistences() != null
        && dataProf.getAvailabilities().getPersistences().size() > 0) {
      for (Iterator i = dataProf.getAvailabilities().getPersistences()
          .iterator(); i.hasNext();) {
        String persistent = (String) i.next();
        XMLUtils.addNode(doc, persistentsElem, "persistent", persistent);
      }
    }

    Element accessibilityElem = XMLUtils
        .addNode(doc, dataRoot, "accessibility");
    XMLUtils.addAttribute(doc, accessibilityElem, "private", String
        .valueOf(dataProf.isPrivateAccessibility()));
    XMLUtils.addAttribute(doc, accessibilityElem, "public", String
        .valueOf(dataProf.isPublicAccessibility()));
    Element cardinalityElem = XMLUtils.addNode(doc, dataRoot, "cardinality");
    XMLUtils.addAttribute(doc, cardinalityElem, "senders", dataProf
        .getCardinality().getSenderCardinality());
    XMLUtils.addAttribute(doc, cardinalityElem, "receivers", dataProf
        .getCardinality().getReceiverCardinality());

  }

  private static void addArbitratorMetadataToDoc(Document doc,
      ArbitratorConnectorProfile arbProf) {
    Element docRoot = doc.getDocumentElement();
    Element arbRoot = XMLUtils.addNode(doc, docRoot, "arbitrator");

    XMLUtils.addNode(doc, arbRoot, "faulthandling", arbProf.getFaultHandling());
    XMLUtils.addNode(doc, arbRoot, "scheduling", arbProf.getScheduling());
    Element concurrencyElem = XMLUtils.addNode(doc, arbRoot, "concurrency");
    XMLUtils.addAttribute(doc, concurrencyElem, "type", arbProf
        .getConcurrency().getType());
    XMLUtils.addNode(doc, concurrencyElem, "mechanism", arbProf
        .getConcurrency().getMechanism());
    XMLUtils.addNode(doc, concurrencyElem, "weight", arbProf.getConcurrency()
        .getWeight());
    Element transactionsElem = XMLUtils.addNode(doc, arbRoot, "transactions");
    XMLUtils.addNode(doc, transactionsElem, "nesting", arbProf
        .getTransactions().getNesting());
    XMLUtils.addNode(doc, transactionsElem, "awareness", arbProf
        .getTransactions().getAwareness());
    XMLUtils.addNode(doc, transactionsElem, "isolation", arbProf
        .getTransactions().getIsolation());
    Element securityElem = XMLUtils.addNode(doc, arbRoot, "security");
    XMLUtils.addAttribute(doc, securityElem, "required", String.valueOf(arbProf
        .getSecurity().isRequired()));
    XMLUtils.addNode(doc, securityElem, "authentication", arbProf.getSecurity()
        .getAuthentication() != null ? arbProf.getSecurity()
        .getAuthentication() : "");
    Element authorizationElem = XMLUtils.addNode(doc, securityElem,
        "authorization");

    if (arbProf.getSecurity().getAuthorizations() != null
        && arbProf.getSecurity().getAuthorizations().size() > 0) {
      for (Iterator i = arbProf.getSecurity().getAuthorizations().iterator(); i
          .hasNext();) {
        String authType = (String) i.next();
        XMLUtils.addNode(doc, authorizationElem, "authType", authType);
      }

    }

    Element privaciesElem = XMLUtils.addNode(doc, securityElem, "privacies");

    if (arbProf.getSecurity().getPrivacies() != null
        && arbProf.getSecurity().getPrivacies().size() > 0) {
      for (Iterator i = arbProf.getSecurity().getPrivacies().iterator(); i
          .hasNext();) {
        String privacy = (String) i.next();
        XMLUtils.addNode(doc, privaciesElem, "privacy", privacy);
      }
    }

    XMLUtils.addNode(doc, securityElem, "integrity", arbProf.getSecurity()
        .getIntegrity());
    XMLUtils.addNode(doc, securityElem, "durability", arbProf.getSecurity()
        .getDurability());

  }

}
