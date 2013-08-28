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

package edu.usc.softarch.disco.tools;

// JDK imports
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import edu.usc.softarch.disco.util.DCPReader;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A Tool to generate statistics from a given directory where
 * Distribution Connector Profiles (or DCPs) reside in.
 * </p>.
 */
public class DcpStatsGenerator {
	
	/* our log stream */
	private static final Logger LOG = Logger.getLogger(DcpStatsGenerator.class.getName());

	/* the directory where dcp files reside in */
	private File dcpDirectory = null;

	/* stats maps for the atomic connector information */
	private static final HashMap DISTRIBUTOR_MAP = new HashMap();

	private static final HashMap PROC_CALL_MAP = new HashMap();

	private static final HashMap EVENT_MAP = new HashMap();

	private static final HashMap ARBITRATOR_MAP = new HashMap();

	private static final HashMap DATA_ACCESS_MAP = new HashMap();

	private static final HashMap STREAM_MAP = new HashMap();

	private int totalGridDcps = 0;

	private int totalP2Pdcps = 0;

	private int totalCsDcps = 0;

	private int totalEventDcps = 0;

	public DcpStatsGenerator(File dcpDir) {
		this.dcpDirectory = dcpDir;
	}

	public void generateStats() throws Exception {
		List dcps = DCPReader.parseDCPs(dcpDirectory);

		if (dcps != null && dcps.size() > 0) {
			LOG.log(Level.INFO, "Processing: [" + dcps.size() + "] DCPs");

			for (Iterator i = dcps.iterator(); i.hasNext();) {
				DistributionConnectorProfile profile = (DistributionConnectorProfile) i
						.next();

				if (profile instanceof ClientServerDistributionConnectorProfile) {
					generateCSStats(profile);
					totalCsDcps++;
				} else if (profile instanceof GridDistributionConnectorProfile) {
					generateGridStats(profile);
					totalGridDcps++;
				} else if (profile instanceof EventDistributionConnectorProfile) {
					generateEventConnStats(profile);
					totalEventDcps++;
				} else if (profile instanceof P2PDistributionConnectorProfile) {
					generateP2PStats(profile);
					totalP2Pdcps++;
				} else {
					LOG.log(Level.WARNING, "Unknown connector type: ["
							+ profile.getClass().getName() + "]");
				}
			}

			LOG.log(Level.INFO, "Total Client-Server DCPs: [" + totalCsDcps + "]");
			LOG.log(Level.INFO, "Total GRID DCPs: [" + totalGridDcps + "]");
			LOG.log(Level.INFO, "Total Event DCPs: [" + totalEventDcps + "]");
			LOG.log(Level.INFO, "Total P2P DCPs: [" + totalP2Pdcps + "]");

			LOG.log(Level.INFO, "Atomic Connector Statistics");
			
			LOG.log(Level.INFO, " *** Procedure Call Stats ***");
			outputStatsMap(PROC_CALL_MAP);

			LOG.log(Level.INFO, " *** Event Stats ***");
			outputStatsMap(EVENT_MAP);

			LOG.log(Level.INFO, " *** Arbitrator Stats *** ");
			outputStatsMap(ARBITRATOR_MAP);

			LOG.log(Level.INFO, " *** Data Access Stats *** ");
			outputStatsMap(DATA_ACCESS_MAP);

			LOG.log(Level.INFO, " *** Stream Stats *** ");
			outputStatsMap(STREAM_MAP);

			LOG.log(Level.INFO, " *** Distributor Stats *** ");
			outputStatsMap(DISTRIBUTOR_MAP);

		} else {
			LOG.log(Level.WARNING, "No distribution connectors found!");
		}
	}

	/**
	 * @param args
	 *            Specify <code>--dcpDir</code> to indicate the directory to
	 *            search for DCP xml files within (to generate stats from)
	 */
	public static void main(String[] args) throws Exception {
		String dcpDir = null;
		String usage = "DcpStatsGenerator --dcpDir </path/to/dir>\n";

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--dcpDir")) {
				dcpDir = args[++i];
			}
		}

		if (dcpDir == null) {
			System.err.println(usage);
			System.exit(1);
		}

		DcpStatsGenerator generator = new DcpStatsGenerator(new File(dcpDir));
		generator.generateStats();
	}

	private void outputStatsMap(HashMap map) {
		if (map != null && map.keySet().size() > 0) {
			for (Iterator i = map.keySet().iterator(); i.hasNext();) {
				String attrName = (String) i.next();
				LOG.log(Level.INFO, "Attribute: [" + attrName + "]: Values: ");
				
				HashMap valDomain = (HashMap) map.get(attrName);

				if (valDomain != null && valDomain.keySet().size() > 0) {
					for (Iterator j = valDomain.keySet().iterator(); j
							.hasNext();) {
						String attrVal = (String) j.next();
						int totalCount = Integer.parseInt((String) valDomain
								.get(attrVal));
						LOG.log(Level.INFO, "[" + attrVal + ":" + totalCount + "]");
					}
				}

			}
		} else
			LOG.log(Level.WARNING, "No Stats!");
	}

	private void generateP2PStats(DistributionConnectorProfile profile) {
		P2PDistributionConnectorProfile p2pProf = (P2PDistributionConnectorProfile) profile;

		generateArbitratorStats(p2pProf.getArbitratorProfile(), ARBITRATOR_MAP);
		generateDataAccessStats(p2pProf.getDataAccessProfile(), DATA_ACCESS_MAP);
		generateStreamStats(p2pProf.getStreamProfile(), STREAM_MAP);
		generateDistributorStats(p2pProf.getDistributorProfile(),
				DISTRIBUTOR_MAP);
	}

	private void generateEventConnStats(DistributionConnectorProfile profile) {
		EventDistributionConnectorProfile eventProf = (EventDistributionConnectorProfile) profile;

		generateEventStats(eventProf.getEventProfile(), EVENT_MAP);
		generateDataAccessStats(eventProf.getDataAccessProfile(),
				DATA_ACCESS_MAP);
		generateStreamStats(eventProf.getStreamProfile(), STREAM_MAP);
		generateDistributorStats(eventProf.getDistributorProfile(),
				DISTRIBUTOR_MAP);
	}

	private void generateGridStats(DistributionConnectorProfile profile) {
		GridDistributionConnectorProfile gridProf = (GridDistributionConnectorProfile) profile;

		generateProcedureCallStats(gridProf.getProcedureCallProfile(),
				PROC_CALL_MAP);
		generateDataAccessStats(gridProf.getDataAccessProfile(),
				DATA_ACCESS_MAP);
		generateStreamStats(gridProf.getStreamProfile(), STREAM_MAP);
		generateDistributorStats(gridProf.getDistributorProfile(),
				DISTRIBUTOR_MAP);
	}

	private void generateCSStats(DistributionConnectorProfile profile) {
		ClientServerDistributionConnectorProfile csProf = (ClientServerDistributionConnectorProfile) profile;

		// Map is of the form:
		// [csProfFieldName]->Map of [value]=>[count]

		generateDataAccessStats(csProf.getDataAccessProfile(), DATA_ACCESS_MAP);
		generateProcedureCallStats(csProf.getProcedureCallProfile(),
				PROC_CALL_MAP);
		generateStreamStats(csProf.getStreamProfile(), STREAM_MAP);
		generateDistributorStats(csProf.getDistributorProfile(),
				DISTRIBUTOR_MAP);
	}

	private void generateArbitratorStats(ArbitratorConnectorProfile prof,
			HashMap map) {
		generatePropertyStats("arbitrator_faulthandling", prof
				.getFaultHandling(), map);
		generatePropertyStats("arbitrator_scheduling", prof.getScheduling(),
				map);

		generatePropertyStats("arbitrator_concurrency_mechanism", prof
				.getConcurrency().getMechanism(), map);
		generatePropertyStats("arbitrator_concurrency_weight", prof
				.getConcurrency().getWeight(), map);
		generatePropertyStats("arbitrator_concurrency_type", prof
				.getConcurrency().getType(), map);

		generatePropertyStats("arbitrator_security_authentication", prof
				.getSecurity().getAuthentication(), map);
		generatePropertyStats("arbitrator_security_durability", prof
				.getSecurity().getDurability(), map);
		generatePropertyStats("arbitrator_security_integrity", prof
				.getSecurity().getIntegrity(), map);

		generatePropertyStatsFromList(prof.getSecurity().getAuthorizations(),
				"arbitrator_security_authorizations", map);
		generatePropertyStatsFromList(prof.getSecurity().getPrivacies(),
				"arbitrator_security_privacies", map);

		generatePropertyStats("arbitrator_transactions_isolation", prof
				.getTransactions().getIsolation(), map);
		generatePropertyStats("arbitrator_transactions_awareness", prof
				.getTransactions().getAwareness(), map);
		generatePropertyStats("arbitrator_transactions_nesting", prof
				.getTransactions().getNesting(), map);

	}

	private void generateEventStats(EventConnectorProfile prof, HashMap map) {
		generatePropertyStatsFromList(prof.getCardinality().getEventPatterns(),
				"event_cardinality_eventpattern", map);

		generatePropertyStats("event_cardinality_observer_cardinality", prof
				.getCardinality().getObserverCardinality(), map);

		generatePropertyStats("event_cardinality_observers", prof
				.getCardinality().getObservers(), map);
		generatePropertyStats("event_cardinality_producer_cardinality", prof
				.getCardinality().getProducerCardinality(), map);
		generatePropertyStats("event_cardinality_producers", prof
				.getCardinality().getProducers(), map);

		generatePropertyStats("event_causality", prof.getCausality(), map);
		generatePropertyStats("event_delivery", prof.getDelivery(), map);
		generatePropertyStatsFromList(prof.getMode().getSoftwares(),
				"event_mode_softwares", map);
		generatePropertyStats("event_mode_type", prof.getMode().getType(), map);

		generatePropertyStats("event_notification", prof.getNotification(), map);
		generatePropertyStats("event_priority_incoming", prof.getPriority()
				.getIncoming(), map);
		generatePropertyStats("event_priority_outgoing", prof.getPriority()
				.getOutgoing(), map);

		generatePropertyStats("event_synchronicity", prof.getSynchronicity(),
				map);
	}

	private void generateDistributorStats(DistributorConnectorProfile prof,
			HashMap map) {
		generatePropertyStats("distributor_delivery_type", prof.getDelivery()
				.getType(), map);
		generatePropertyStatsFromList(prof.getDelivery().getMechanisms(),
				"distributor_delivery_mechanisms", map);
		generatePropertyStatsFromList(prof.getDelivery().getSemantics(),
				"distributor_delivery_semantics", map);

		generatePropertyStats("distributor_naming_type", prof.getNaming()
				.getType(), map);
		generatePropertyStatsFromList(prof.getNaming().getStructures(),
				"distributor_naming_structures", map);

		generatePropertyStats("distributor_routing_membership", prof
				.getRouting().getMembership(), map);
		generatePropertyStats("distributor_routing_type", prof.getRouting()
				.getType(), map);

		if (prof.getRouting().getPath().isCachedPath()) {
			generatePropertyStats("distributor_routing_path", "cached", map);
		}

		if (prof.getRouting().getPath().isDynamicPath()) {
			generatePropertyStats("distributor_routing_path", "dynamic", map);
		}

		if (prof.getRouting().getPath().isStaticPath()) {
			generatePropertyStats("distributor_routing_path", "static", map);
		}
	}

	private void generateStreamStats(StreamConnectorProfile prof, HashMap map) {

		generatePropertyStats("stream_bounds", prof.getBounds(), map);
		generatePropertyStats("stream_buffering", prof.getBuffering(), map);
		generatePropertyStats("sream_identity", prof.getIdentity(), map);
		generatePropertyStats("stream_state", prof.getState(), map);
		generatePropertyStats("stream_synchronicity", prof.getSynchronicity(),
				map);
		generatePropertyStats("stream_throughput", prof.getThroughput(), map);

		generatePropertyStatsFromList(prof.getDeliveries(),
				"stream_deliveries", map);
		generatePropertyStatsFromList(prof.getFormats(), "stream_formats", map);
		generatePropertyStatsFromList(prof.getLocalities(),
				"stream_localities", map);

		generatePropertyStats("stream_cardinality_senders", prof
				.getCardinality().getSenderCardinality(), map);
		generatePropertyStats("stream_cardinality_receivers", prof
				.getCardinality().getReceiverCardinality(), map);
	}

	private void generateProcedureCallStats(ProcedureCallConnectorProfile prof,
			HashMap map) {
		if (prof.getAccessibility().isPrivateAccess()) {
			generatePropertyStats("proc_call_accessibility", "private", map);
		}

		if (prof.getAccessibility().isPublicAccess()) {
			generatePropertyStats("proc_call_accessibility", "public", map);
		}

		if (prof.getAccessibility().isProtectedAccess()) {
			generatePropertyStats("proc_call_accessibility", "protected", map);
		}

		generatePropertyStats("proc_call_cardinality_senders", prof
				.getCardinality().getSenders(), map);
		generatePropertyStats("proc_call_cardinality_receivers", prof
				.getCardinality().getReceivers(), map);

		generatePropertyStats("proc_call_entry_point_single", prof
				.getEntryPoint().getSingle(), map);
		generatePropertyStats("proc_call_invocation_explicit", prof
				.getInvocation().getExplicit(), map);
		generatePropertyStatsFromList(
				prof.getParams().getDataTransferMethods(),
				"proc_call_params_datatransfer", map);
		generatePropertyStats("proc_call_params_invocation_record", prof
				.getParams().getInvocationRecord(), map);
		generatePropertyStats("proc_call_params_semantics", prof.getParams()
				.getSemantics(), map);
		generatePropertyStats("proc_call_params_return_value", prof.getParams()
				.getReturnValue(), map);
	}

	private void generateDataAccessStats(DataAccessConnectorProfile prof,
			HashMap map) {
		generatePropertyStats("data_access_lifecycle", prof.getLifecycle(), map);
		generatePropertyStats("data_access_locality", prof.getLocality(), map);
		generatePropertyStatsFromList(prof.getAccesses(),
				"data_access_accesses", map);
		generatePropertyStats("data_access_avail_transient", prof
				.getAvailabilities().getAvailTransient(), map);
		generatePropertyStatsFromList(prof.getAvailabilities()
				.getPersistences(), "data_access_persistences", map);
		generatePropertyStats("data_access_cardinality_senders", prof
				.getCardinality().getSenderCardinality(), map);
		generatePropertyStats("data_access_cardinality_receivers", prof
				.getCardinality().getReceiverCardinality(), map);
	}

	private void generatePropertyStatsFromList(List list, String attrName,
			HashMap map) {
		if (list != null && list.size() > 0) {
			for (Iterator i = list.iterator(); i.hasNext();) {
				String attrVal = (String) i.next();
				generatePropertyStats(attrName, attrVal, map);
			}
		}
	}

	private void generatePropertyStats(String attrName, String attrVal,
			HashMap map) {
		HashMap valMap = getValMap(attrName, map);
		updateCount(attrVal, valMap);
	}

	private void updateCount(String attrVal, HashMap valMap) {
		int totCount = -1;

		if (valMap.get(attrVal) != null) {
			// get count and increment
			totCount = Integer.parseInt((String) valMap.get(attrVal));
			totCount++;
		} else {
			totCount = 1;
		}

		valMap.put(attrVal, String.valueOf(totCount));
	}

	private HashMap getValMap(String attr, HashMap map) {
		if (map.get(attr) == null) {
			HashMap newAttrMap = new HashMap();
			map.put(attr, newAttrMap);
			return newAttrMap;
		} else {
			return (HashMap) map.get(attr);
		}
	}

}
