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

package edu.usc.softarch.disco.servlet;

// JDK imports
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// DISCO imports
import edu.usc.softarch.disco.selection.BayesianSelector;
import edu.usc.softarch.disco.selection.ConnectorRank;
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.util.DCPReader;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A Servlet exposing the {@link BayesianSelector} interface across the web.
 * </p>.
 */
public class BayesianSelectionServlet extends HttpServlet {

	/* serial version UID */
	private static final long serialVersionUID = 1076717730736484128L;

	/* dcp directory */
	private String dcpDir = null;

	/* valRange file */
	private String valRangeFile = null;

	/* list of distribution connector profiles */
	private List dcps = null;

	/* our domain profile */
	private String domainProfile = null;

	/* our bayesian selector */
	private static BayesianSelector selector = null;

	/**
	 * 
	 */
	public BayesianSelectionServlet() {
	}

	public void init(ServletConfig config) throws ServletException {
		this.dcpDir = config.getServletContext().getInitParameter(
				"edu.usc.softarch.disco.selection.bayes.dcpDir");
		this.valRangeFile = config.getServletContext().getInitParameter(
				"edu.usc.softarch.disco.selection.bayes.valRange.file");

		this.domainProfile = config.getServletContext().getInitParameter(
				"edu.usc.softarch.disco.selection.bayes.domainProf.file");

		if (this.dcpDir == null || this.valRangeFile == null
				|| domainProfile == null) {
			throw new ServletException(
					"Must specify: [edu.usc.softarch.disco.selection.bayes.dcpDir] and "
							+ "[edu.usc.softarch.disco.selection.bayes.valRange.file] and "
							+ "[edu.usc.softarch.disco.selection.bayes.domainProf.file]");
		}

		try {
			this.dcps = DCPReader.parseDCPs(new File(this.dcpDir));
		} catch (Exception e) {
			throw new ServletException(e);
		}
		selector = new BayesianSelector(this.domainProfile, this.valRangeFile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doIt(req, res);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doIt(req, res);
	}

	private void doIt(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		// look for the following parameters
		// total_volume
		// delivsched_numintervals
		// delivsched_volperinterval
		// num_users

		String totalVolume = req.getParameter("total_volume");
		String delivSchedNumIntervals = req
				.getParameter("delivsched_numintervals");
		String delivSchedVolPerInt = req.getParameter("delivsched_volperint");
		String numUsers = req.getParameter("num_users");

  if(totalVolume == null && delivSchedNumIntervals == null &&
      delivSchedVolPerInt == null && numUsers == null){
    res.setContentType("text/xml");
    res.getWriter().println("<Response>");
    res.getWriter().println("<Status>OK</Status>");
    res.getWriter().println("<Message>System Active</Message>");
    res.getWriter().println("<Value/>");
    res.getWriter().println("</Response>");
    return;
  }
  
		if (totalVolume == null) {
			throw new IllegalArgumentException(
					"total_volume parameter must be specified!");
		}

		if (delivSchedNumIntervals == null) {
			throw new IllegalArgumentException(
					"delivsched_numintervals parameter must be specified!");
		}

		if (delivSchedVolPerInt == null) {
			throw new IllegalArgumentException(
					"delivsched_volperint parameter must be specified!");
		}

		if (numUsers == null) {
			throw new IllegalArgumentException(
					"num_users parameter must be specified!");
		}

		DistributionScenario scenario = new DistributionScenario();
		scenario.getDelivSchedule().getNumberOfIntervals().setValue(
    delivSchedNumIntervals);
		scenario.getDelivSchedule().getVolumePerInterval().setValue(delivSchedVolPerInt);
		scenario.getNumUsers().setValue(numUsers);
		scenario.getTotalVolume().setValue(totalVolume);

		List connectorRankList = selector.selectConnectors(scenario, dcps);

		if (connectorRankList != null && connectorRankList.size() > 0) {
			// okay we are going to build an XML response
			res.setContentType("text/xml");

			res.getWriter().println("<?xml version=\"1.0\"?>");
			res.getWriter().println("<Response>");
			res.getWriter().println("<Status>OK</Status>");
			res.getWriter().println("<Message>Connector Rank List</Message>");
			res.getWriter().println("<Value>");
   res.getWriter().println("<ConnectorList>");

			for (Iterator i = connectorRankList.iterator(); i.hasNext();) {
				ConnectorRank rank = (ConnectorRank) i.next();
    res.getWriter().println("<Connector>");
    res.getWriter().println("<Name>"+rank.getConnectorName()+"</Name>");
    res.getWriter().println("<Score>"+rank.getRank()+"</Score>");
    res.getWriter().println("</Connector>");
			}

   res.getWriter().println("</ConnectorList>");
			res.getWriter().println("</Value>");
			res.getWriter().println("</Response>");
		}

	}

}
