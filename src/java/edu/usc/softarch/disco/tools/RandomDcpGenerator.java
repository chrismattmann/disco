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
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import edu.usc.softarch.disco.structs.ArbitratorConcurrency;
import edu.usc.softarch.disco.structs.ArbitratorSecurity;
import edu.usc.softarch.disco.structs.ArbitratorTransactions;
import edu.usc.softarch.disco.util.DCPWriter;
import edu.usc.softarch.disco.util.ValRangeReader;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A utility command line tool to generate large amounts of randomly populated
 * {@link DistributionConnectorProfile}s.
 * </p>.
 */
public class RandomDcpGenerator {

	private String dcpOutputDirPath = null;

	private String valRangeFilePath = null;

	private Random randomDcpType = null;

	public RandomDcpGenerator(String dcpOutputDir, String valRangeFilePath) {
		this.dcpOutputDirPath = dcpOutputDir;
		if (!this.dcpOutputDirPath.endsWith("/")) {
			this.dcpOutputDirPath += "/";
		}
		this.valRangeFilePath = valRangeFilePath;
		this.randomDcpType = new Random();
	}

	public void generateDcps(int num) {
		Map valRange = ValRangeReader.readValRangeFile(new File(
				this.valRangeFilePath));

		for (int i = 0; i < num; i++) {
			generateRandomDcp(i, randomDcpType.nextInt(4) + 1, valRange);
		}
	}

	private void generateRandomDcp(int connNum, int type, Map valRange) {

		DistributionConnectorProfile prof = null;

		// generate Dcp
		if (type == 1) {
			// P2P
			P2PDistributionConnectorProfile p2pProf = new P2PDistributionConnectorProfile(
					"conn_" + connNum);
			p2pProf.setArbitratorProfile(getRandomArbitratorProf(valRange));
			p2pProf.setDataAccessProfile(getRandomDataAccessProf(valRange));
			p2pProf.setDistributorProfile(getRandomDistributorProf(valRange));
			p2pProf.setStreamProfile(getRandomStreamProf(valRange));
			prof = p2pProf;

		} else if (type == 2) {
			// Grid
			GridDistributionConnectorProfile gridProf = new GridDistributionConnectorProfile(
					"conn_" + connNum);
			gridProf
					.setProcedureCallProfile(getRandomProcedureCallProf(valRange));
			gridProf.setDataAccessProfile(getRandomDataAccessProf(valRange));
			gridProf.setDistributorProfile(getRandomDistributorProf(valRange));
			gridProf.setStreamProfile(getRandomStreamProf(valRange));
			prof = gridProf;
		} else if (type == 3) {
			// Event
			EventDistributionConnectorProfile eventProf = new EventDistributionConnectorProfile(
					"conn_" + connNum);
			eventProf.setEventProfile(getRandomEventProfile(valRange));
			eventProf.setDataAccessProfile(getRandomDataAccessProf(valRange));
			eventProf.setDistributorProfile(getRandomDistributorProf(valRange));
			eventProf.setStreamProfile(getRandomStreamProf(valRange));
			prof = eventProf;
		} else if (type == 4) {
			// Client/Server
			ClientServerDistributionConnectorProfile csProf = new ClientServerDistributionConnectorProfile(
					"conn_" + connNum);
			csProf
					.setProcedureCallProfile(getRandomProcedureCallProf(valRange));
			csProf.setDataAccessProfile(getRandomDataAccessProf(valRange));
			csProf.setDistributorProfile(getRandomDistributorProf(valRange));
			csProf.setStreamProfile(getRandomStreamProf(valRange));
			prof = csProf;
		}

		DCPWriter.writeDCP(prof, this.dcpOutputDirPath + "dcp_" + connNum
				+ ".xml");
	}

	private ProcedureCallConnectorProfile getRandomProcedureCallProf(
			Map valRange) {
		ProcedureCallConnectorProfile prof = new ProcedureCallConnectorProfile();

		prof.getEntryPoint().setSingle("");
		prof.getParams()
				.setSemantics(
						getRandomValue((List) valRange
								.get("procedure_call_semantics")));
		prof.getParams().setInvocationRecord(
				getRandomValue((List) valRange
						.get("procedure_call_params_invocation_record")));
		prof.getParams().getDataTransferMethods().add(
				getRandomValue((List) valRange
						.get("procedure_call_params_datatransfer")));
		prof.getParams().setReturnValue(
				getRandomValue((List) valRange
						.get("procedure_call_params_return_value")));

		prof.getAccessibility().setPrivateAccess(new Random().nextBoolean());
		prof.getAccessibility().setPublicAccess(new Random().nextBoolean());
		prof.getAccessibility().setProtectedAccess(new Random().nextBoolean());

		prof.getCardinality().setReceivers(
				getRandomValue((List) valRange
						.get("procedure_call_cardinality_receivers")));
		prof.getCardinality().setSenders(
				getRandomValue((List) valRange
						.get("procedure_call_cardinality_senders")));

		prof.getInvocation().setExplicit(
				getRandomValue((List) valRange
						.get("procedure_call_invocation_explicit")));

		prof.setSynchronicity(getRandomValue((List) valRange
				.get("procedure_call_synchronicity")));

		return prof;
	}

	private EventConnectorProfile getRandomEventProfile(Map valRange) {
		EventConnectorProfile prof = new EventConnectorProfile();

		prof.getMode().setType(
				getRandomValue((List) valRange.get("event_mode_type")));

		prof.getMode().getSoftwares().add(
				getRandomValue((List) valRange.get("event_mode_softwares")));

		prof.getPriority().setIncoming(
				getRandomValue((List) valRange.get("event_priority_incoming")));
		prof.getPriority().setOutgoing(
				getRandomValue((List) valRange.get("event_priority_outgoing")));
		prof.getCardinality().setObserverCardinality(
				getRandomValue((List) valRange
						.get("event_cardinality_observer_cardinality")));
		prof.getCardinality().setProducerCardinality(
				getRandomValue((List) valRange
						.get("event_cardinality_producer_cardinality")));
		prof.getCardinality().setObservers(
				getRandomValue((List) valRange
						.get("event_cardinality_observers")));
		prof.getCardinality().setProducers(
				getRandomValue((List) valRange
						.get("event_cardinality_producers")));
		prof.getCardinality().getEventPatterns().add(
				getRandomValue((List) valRange
						.get("event_cardinality_patterns")));
		prof.setDelivery(getRandomValue((List) valRange.get("event_delivery")));
		prof.setNotification(getRandomValue((List) valRange
				.get("event_notification")));
		prof.setSynchronicity(getRandomValue((List) valRange
				.get("event_synchronicity")));

		return prof;
	}

	private StreamConnectorProfile getRandomStreamProf(Map valRange) {
		StreamConnectorProfile prof = new StreamConnectorProfile();

		prof.setBounds(getRandomValue((List) valRange.get("stream_bounds")));
		prof.setBuffering(getRandomValue((List) valRange
				.get("stream_buffering")));
		prof
				.setIdentity(getRandomValue((List) valRange
						.get("stream_identity")));
		prof.setState(getRandomValue((List) valRange.get("stream_state")));
		prof.setSynchronicity(getRandomValue((List) valRange
				.get("stream_synchronicity")));
		prof.setThroughput(getRandomValue((List) valRange
				.get("stream_throughput")));

		prof.getDeliveries().add(
				getRandomValue((List) valRange.get("stream_deliveries")));
		prof.getFormats().add(
				getRandomValue((List) valRange.get("stream_formats")));
		prof.getLocalities().add(
				getRandomValue((List) valRange.get("stream_localities")));
		prof.getCardinality().setReceiverCardinality(
				getRandomValue((List) valRange
						.get("stream_cardinality_receivers")));
		prof.getCardinality().setSenderCardinality(
				getRandomValue((List) valRange
						.get("stream_cardinality_senders")));

		return prof;
	}

	private DistributorConnectorProfile getRandomDistributorProf(Map valRange) {
		DistributorConnectorProfile prof = new DistributorConnectorProfile();
		prof.getDelivery()
				.setType(
						getRandomValue((List) valRange
								.get("distributor_delivery_type")));
		prof.getDelivery().getMechanisms().add(
				getRandomValue((List) valRange
						.get("distributor_delivery_mechanisms")));
		prof.getDelivery().getSemantics().add(
				getRandomValue((List) valRange
						.get("distributor_delivery_semantics")));
		prof.getNaming().setType(
				getRandomValue((List) valRange.get("distributor_naming_type")));
		prof.getNaming().getStructures().add(
				getRandomValue((List) valRange
						.get("distributor_naming_structures")));
		prof.getRouting().setMembership(
				getRandomValue((List) valRange
						.get("distributor_routing_membership")));
		prof.getRouting()
				.setType(
						getRandomValue((List) valRange
								.get("distributor_routing_type")));
		prof.getRouting().getPath().setCachedPath(new Random().nextBoolean());
		prof.getRouting().getPath().setDynamicPath(new Random().nextBoolean());
		prof.getRouting().getPath().setStaticPath(new Random().nextBoolean());

		return prof;
	}

	private DataAccessConnectorProfile getRandomDataAccessProf(Map valRange) {
		DataAccessConnectorProfile prof = new DataAccessConnectorProfile();
		prof.setLocality(getRandomValue((List) valRange
				.get("data_access_locality")));
		prof.setPrivateAccessibility(new Random().nextBoolean());
		prof.setPublicAccessibility(new Random().nextBoolean());
		prof.setLifecycle("");
		prof.getAccesses().add(getRandomValue((List) valRange.get("data_access_accesses")));
		prof.getAvailabilities().setAvailTransient(
				getRandomValue((List) valRange
						.get("data_access_transient_availability")));
		prof.getAvailabilities().getPersistences().add(
				getRandomValue((List) valRange.get("data_access_persistance")));
		prof.getCardinality().setReceiverCardinality(
				getRandomValue((List) valRange
						.get("data_access_cardinality_receivers")));
		prof.getCardinality().setSenderCardinality(
				getRandomValue((List) valRange
						.get("data_access_cardinality_senders")));
		return prof;
	}

	private ArbitratorConnectorProfile getRandomArbitratorProf(Map valRange) {
		ArbitratorConnectorProfile prof = new ArbitratorConnectorProfile();
		ArbitratorConcurrency concurrency = new ArbitratorConcurrency();
		concurrency.setMechanism("Rendezvous");
		concurrency.setWeight("Heavy");
		concurrency.setType("Point-to-Point");
		prof.setConcurrency(concurrency);
		ArbitratorSecurity security = new ArbitratorSecurity();
		security.getAuthorizations().add("capability");
		security.getAuthorizations().add("fairness");
		security.getPrivacies().add("Anonymous");
		security.getPrivacies().add("Tracked");
		security.setIntegrity("SHA");
		security.setDurability("Multi-Session");
		prof.setSecurity(security);
		ArbitratorTransactions transactions = new ArbitratorTransactions();
		transactions.setAwareness("Supported");
		transactions.setIsolation("Read/Write");
		transactions.setNesting("Multiple");
		prof.setTransactions(transactions);
		prof.setFaultHandling("Voting");
		prof.setScheduling("Ensure Reassembly");
		return prof;
	}

	private String getRandomValue(List valList) {
		int listSize = valList.size();
		Random randomizer = new Random();
		int idx = randomizer.nextInt(listSize);
		return (String) valList.get(idx);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dcpOutputDirPath = null, valRangeFilePath = null;
		int num = -1;

		String usage = "RandomDcpGenerator [options]\n"
				+ "--outputDir <dir path>\n" + "--valRange <path>\n"
				+ "--num <number of profiles to generate>\n";

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--outputDir")) {
				dcpOutputDirPath = args[++i];
			} else if (args[i].equals("--valRange")) {
				valRangeFilePath = args[++i];
			} else if (args[i].equals("--num")) {
				num = Integer.parseInt(args[++i]);
			}
		}

		if (dcpOutputDirPath == null || valRangeFilePath == null || num == -1) {
			System.err.println(usage);
			System.exit(1);
		}

		RandomDcpGenerator generator = new RandomDcpGenerator(dcpOutputDirPath,
				valRangeFilePath);
		generator.generateDcps(num);
	}

}
