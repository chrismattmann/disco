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

package edu.usc.softarch.disco.gui;

import java.lang.String;
import java.util.List;
import java.util.LinkedList;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Iterator;
import java.util.Arrays;
import java.io.File;
import java.io.FileFilter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JDialog;
import java.awt.event.*;

import edu.usc.softarch.disco.tools.ScenarioRunner;
import edu.usc.softarch.disco.structs.DistributionScenario;
import edu.usc.softarch.disco.structs.DistributionScenarioResult;
import edu.usc.softarch.disco.structs.ScenarioValue;
import edu.usc.softarch.disco.structs.RangedValue;
import edu.usc.softarch.disco.connector.dcp.DistributionConnectorProfile;
import edu.usc.softarch.disco.util.DistributionScenarioReader;
import edu.usc.softarch.disco.structs.DeliverySchedule;
import edu.usc.softarch.disco.structs.PerformanceRequirements;
import edu.usc.softarch.disco.util.DCPReader;
import edu.usc.softarch.disco.util.RTSI;

/**
 * Controller class for running scenarios.
 * Serves as a delegate for {@link ScenarioEditorView} and {@link ScenarioEditorDelegate}.
 *
 * @author Trevor Johns &lt;<a href="mailto:tjohns@usc.edu">tjohns@usc.edu</a>&gt;
 * @version $Rev: 141 $
 */
public final class ScenarioRunnerController
implements ScenarioRunnerDelegate, ScenarioEditorDelegate, ComponentListener
{
    static final private int SCENARIO_EDITOR_MIN_WIDTH = 550;
    static final private int SCENARIO_EDITOR_REQ_HEIGHT = 610;
    
    private JFileChooser fileChooser;
    private Vector<DistributionScenario> scenarios;
    private ScenarioRunnerView view = null;
    private ScenarioEditorView editor = null;
    private JFrame parentFrame = null;
    private JDialog activeDialog = null;
    
    ArrayList<ScenarioResultsView> activeResults;
    
    /** Package that contains Selectors. This is used when deterining 
      * what packages are available via introspection. */
    private static final String SelectorPackage = "edu.usc.softarch.disco.selection";
    
    /** Selector base class. This is used when deterining 
      * what packages are available via introspection. */
    private static final String SelectorBaseClass = "Selector";
    
    /** The selectors to display in the drop down box. Will be determined by introspection.*/
    private String[] SelectorList;
    
    /**
     * Class constructor.
     * Initializes various class properties.
     *
     * @param v The view that this class will register itself as a delegate for.
     * @param f The parent frame for v, used when displaying dialogs.
     */
    public ScenarioRunnerController (ScenarioRunnerView v, JFrame f)
    {
        activeResults = new ArrayList<ScenarioResultsView>();
        fileChooser = new JFileChooser();
        scenarios = new Vector<DistributionScenario>();
        parentFrame = f;
        view = v;
        view.setDelegate(this);
        
        // Determine the list of possible selectors.
        ArrayList<String> sl = new ArrayList<String>();
        List<Class> selectorClasses = RTSI.find(SelectorPackage, SelectorPackage + "." + SelectorBaseClass);
        
        for (Iterator<Class> i = selectorClasses.iterator(); i.hasNext(); ) {
            sl.add(i.next().getSimpleName());
        }
        
        SelectorList = sl.toArray(new String[sl.size()]);
    }
    
    
    // Scenario Runner Delegate Methods
    /**
     * Scenario Runner: Run scenario event handler.
     * Causes a new scenario to be run based on the current values in the UI.
     */
    public void scenarioRunnerRunScenarioButton ()
    {
        try {
        // Update status display
        view.setStatusDisplay("Running...");
        
        // Get data from the UI
        File dcpDirectory = new File(view.getDCPDirectory());
        File propertiesFile = new File(view.getPropertiesFile());
        String selector = SelectorPackage + "." + view.getSelector();
        boolean displayScores = view.getDisplayScores();
        Integer resultsCount = view.getResultsCount();
        
        // Convert values from the UI into structured data
        List<DistributionConnectorProfile> dcpProfiles = DCPReader.parseDCPs(dcpDirectory);
        Properties props = new Properties();
        props.load(propertiesFile.toURL().openStream());
        
        // Run the scenario
        List<DistributionScenarioResult> results = ScenarioRunner.runScenarios(scenarios, dcpProfiles, props, selector);
        
        // FIXME: The results window needs to be removed from the activeResults array when closed.
        // FIXME: This should be run asynchronosusly or in a seperate thread. Right now this blocks the UI.
        //DistributionScenarioResult.printScenarioResults(results, resultsCount, displayScores);
        ScenarioResultsView v = new ScenarioResultsView(new ScenarioResultsModel(results));
        activeResults.add(v);
        v.showWindow();
        
        // Clear status display
        view.setStatusDisplay("");
        }
        catch (Exception e) {
            System.err.println("Uncaught Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Scenario Runner: Save Log event handler.
     * @todo Needs to be implemented.
     */
    public void scenarioRunnerSaveLogButton () {}
    
    /**
     * Scenario Runner: Clear Log event handler.
     * @todo Needs to be implemented.
     */
    public void scenarioRunnerClearLogButton () {}
    
    /**
     * Scenario Runner: Add Scenario Files event handler.
     * Opens a file coooser dialog to add a list of scenario files 
     * and display them in the UI. Scenario files may be specified individually 
     * or their containing folder may be specified. If a folder is specified, it 
     * will be scanned recursively for .xml files.
     */
    public void scenarioRunnerAddScenarioFilesButton ()
    {
        if (parentFrame != null) {
            File selection;
        
            // Open a file chooser dialog to pick files/folders
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.resetChoosableFileFilters();
            fileChooser.addChoosableFileFilter(XMLFilter.getXMLFilter());
            int returnVal = fileChooser.showOpenDialog(parentFrame);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                selection = fileChooser.getSelectedFile();
                
                // Add all files from the selection to the scenarios list
                addScenarios(selection);

                // Refresh the list of scenarios
                if (view != null) {
                    view.setScenarios(scenarios);
                }
            }
        }
    }
    
    /**
     * Helper to add scenarios to the UI.
     * This method takes a File object and adds it to the list of 
     * scenarios to be run. If a directory is provided, this method 
     * calls itself recursively on each child to add all files contained 
     * within the directory.
     * 
     * @param f The file or directory to add.
     */
    private void addScenarios (File f)
    {
		DistributionScenario s;
		
        // If this is a directory, we want to recursively call ourself 
        // for each child within the directory.
        if (f.isDirectory()) {
            File[] children = f.listFiles(XMLFilter.getXMLFilter());
            for (int i = 0; i < children.length; i++) {
                addScenarios(children[i]);
            }
        }
        
        // Otherwise, add the child to the list of scenarios
        else {
            s = DistributionScenarioReader.readScenario(f);
            s.setName(f.toString());
            scenarios.add(s);
        }
    }
    
    /**
     * Scenario Runner: Add Custom Scenario event handler.
     * Displays a dialog to create a new scenario from parameters.
     *
     * @see ScenarioEditorView
     */
    public void scenarioRunnerAddCustomScenarioButton ()
    {
        // Create a new editor controller and associated panel
        editor = new ScenarioEditorView();
        editor.setDelegate(this);
        
        // Dispaly the dialog
        activeDialog = new JDialog(parentFrame, "Add Custom Scenario", true);
        activeDialog.add(editor.buildPanel());
        activeDialog.setSize(SCENARIO_EDITOR_MIN_WIDTH, SCENARIO_EDITOR_REQ_HEIGHT);
        activeDialog.addComponentListener(this);
        activeDialog.setVisible(true);
        
        // Since we're the editor's delegate, we will be notified 
        // of any important events.
    }
    
    /**
     * Scenario Runner: Add Custom Scenario event handler.
     * Displays a dialog to create a new scenario from parameters.
     *
     * @see ScenarioEditor
     */
    public void scenarioRunnerRemoveScenarioButton (int[] scenarioIndices) {
        // We need to iterate backwards through the list of indices
        // to prevent the order from being reversed
        for (int i = scenarioIndices.length - 1; i >= 0; i--) {
            System.out.println("Removing scenario at index: " + scenarioIndices[i]);
            scenarios.remove(scenarioIndices[i]);
        }
        
        if (parentFrame != null) {
            parentFrame.repaint();
        }
    }
    
    /**
     * Scenario Runner: Scelect DCP Directory event handler.
     * Displays a file chooser to select a DCP directory. Only directories 
     * are made available for selection.
     */
    public void scenarioRunnerSelectDCPDirectoryButton ()
    {
        if (parentFrame != null) {
            File selection;
    
            // Open a file chooser dialog to pick files/folders
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.resetChoosableFileFilters();
            int returnVal = fileChooser.showOpenDialog(parentFrame);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                selection = fileChooser.getSelectedFile();
            
                // Update the view with the selected path
                view.setDCPDirectory(selection);
            }
        }
    }
    
    /**
     * Scenario Runner: Select Properties File event handler.
     * Displays a dialog to select a properites file. Only .properties files 
     * are made available for selection.
     *
     * @see ScenarioEditor
     */
    public void scenarioRunnerSelectPropertiesFileButton ()
    {
        if (parentFrame != null) {
            File selection;
    
            // Open a file chooser dialog to pick files/folders
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.resetChoosableFileFilters();
            fileChooser.addChoosableFileFilter(PropertiesFilter.getPropertiesFilter());
            int returnVal = fileChooser.showOpenDialog(parentFrame);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                selection = fileChooser.getSelectedFile();
            
                // Update the view with the selected path
                view.setPropertiesFile(selection);
            }
        }
    }
    
    /**
     * Scenario Runner: Selector List Data Source.
     * Returns the list of valid selectors that the user may 
     * choose when running a scenario. */
    public String[] getScenarioRunnerSelectorList ()
    {
        return SelectorList;
    }
    
    
    // Scenario Editor Delegate Methods
    /**
     * Scenario Editor: Save event handler.
     * Addes a new DistributionScenario to the scenario list based on the 
     * properties specified in the editor.
     *
     * @todo Needs to be implemented.
     */
     public void scenarioEditorSaveButton () {
         if (editor != null && activeDialog != null) {
             DistributionScenario s = new DistributionScenario();
             ScenarioValue v;
             RangedValue r;
             boolean aSet, bSet, cSet, dSet;

             // For all non-blank properties, propogate the change to the 
             // DistributionScenario model
             // * Scenario Name
             aSet = editor.isScenarioNameSet();
             if (aSet) {
                 s.setName(editor.getScenarioName());
             }

             // * Total Volume
             aSet = editor.isMinVolumeSet();
             bSet = editor.isMaxVolumeSet();
             if (aSet || bSet) {
                 v = new ScenarioValue();
                 r = new RangedValue();
                 if (aSet) {
                     r.setMinValue(editor.getMinVolume());
                     r.setMinInclusive(true);
                 }
                 if (bSet) {
                     r.setMaxValue(editor.getMaxVolume());
                     r.setMaxInclusive(true);
                }
                v.setRangeVal(r);
                s.setTotalVolume(v);
             }
             
             // * Delivery Schedule
             aSet = editor.isDeliveryVolumeSet();
             bSet = editor.isDeliveryMinIntervalsSet();
             cSet = editor.isDeliveryMaxIntervalsSet();
             if (aSet || bSet || cSet) {
                  DeliverySchedule ds = new DeliverySchedule();
                  
                  if (aSet) {
                      v = new ScenarioValue();
                      v.setValue(editor.getDeliveryVolume());
                      ds.setVolumePerInterval(v);
                  }
                  if (bSet || cSet) {
                      v = new ScenarioValue();
                      r = new RangedValue();
                      if (bSet) {
                          r.setMinValue(editor.getDeliveryMinIntervals());
                          r.setMinInclusive(true);
                      }
                      if (cSet) {
                          r.setMaxValue(editor.getDeliveryMaxIntervals());
                          r.setMaxInclusive(true);
                      }
                      v.setRangeVal(r);
                      ds.setNumberOfIntervals(v);
                 }
                 s.setDelivSchedule(ds);
             }
             
             // * Users
             aSet = editor.isMinUsersSet();
             bSet = editor.isMaxUsersSet();
             if (aSet || bSet) {
                 v = new ScenarioValue();
                 r = new RangedValue();
                 if (aSet) {
                     r.setMinValue(editor.getMinUsers());
                     r.setMinInclusive(true);
                 }
                 if (bSet) {
                     r.setMaxValue(editor.getMaxUsers());
                     r.setMaxInclusive(true);
                }
                v.setRangeVal(r);
                s.setNumUsers(v);
             }
             
             aSet = editor.isMinUserTypesSet();
             bSet = editor.isMaxUserTypesSet();
             if (aSet || bSet) {
                 v = new ScenarioValue();
                 r = new RangedValue();
                 if (aSet) {
                     r.setMinValue(editor.getMinUserTypes());
                     r.setMinInclusive(true);
                 }
                 if (bSet) {
                     r.setMaxValue(editor.getMaxUserTypes());
                     r.setMaxInclusive(true);
                }
                v.setRangeVal(r);
                s.setNumUserTypes(v);
             }
             
             // * Network Properties
             aSet = editor.isGeographicDistributionSet();
             if (aSet) {
                 s.setGeoDistribution(Arrays.asList(editor.getGeographicDistribution()));
             }
             
             aSet = editor.isAccessPolicySet();
             if (aSet) {
                 s.setAccessPolicies(Arrays.asList(editor.getAccessPolicy()));
             }
             
             // * Performance Requirements
             aSet = editor.isConsistencySet();
             bSet = editor.isDependabilitySet();
             cSet = editor.isScalabilitySet();
             dSet = editor.isEfficiencySet();
             if (aSet || bSet || cSet || dSet) {
                 PerformanceRequirements pr = new PerformanceRequirements();
                 if (aSet) {
                     pr.setConsistency(editor.getConsistency());
                 }
                 if (bSet) {
                     pr.setDependability(editor.getDependability());
                 }
                 if (cSet) {
                     pr.setScalability(editor.getScalability());
                 }
                 if (dSet) {
                     pr.setEfficiency(editor.getEfficiency());
                 }
                 s.setPerformanceReqs(pr);
             }
             
             // * Data Types
             s.setDataTypes(editor.getDataTypes());
             
             // Add the scnario to the list of scenarios
             scenarios.add(s);
             
             // Refresh the list of scenarios
             if (view != null) {
                 view.setScenarios(scenarios);
             }
             
             // Done!
             closeScenarioEditor();
         }
     }
     
     // Component Listener Methods
     public void componentResized (ComponentEvent e)
     {
         // Resize the scenario editor dialog to make sure it's not
         // too small
         if (e.getComponent() == activeDialog)
         {
             int width = activeDialog.getWidth();
             int height = activeDialog.getHeight();

             boolean resize = false;

             if (width < SCENARIO_EDITOR_MIN_WIDTH) {
                 resize = true;
                 width = SCENARIO_EDITOR_MIN_WIDTH;
             }
             if (height != SCENARIO_EDITOR_REQ_HEIGHT) {
                 resize = true;
                 height = SCENARIO_EDITOR_REQ_HEIGHT;
             }
             if (resize) {
                 activeDialog.setSize(width, height);
             }
         }
     }
     
     public void componentMoved(ComponentEvent e) {}
     public void componentShown(ComponentEvent e) {}
     public void componentHidden(ComponentEvent e) {}
    
    /**
     * Scenario Runner: Cancel event handler.
     * Closes the scenario editor dialog, discarding changes
     */
    public void scenarioEditorCancelButton ()
    {
        closeScenarioEditor();
    }
    
    private void closeScenarioEditor ()
    {
        if (activeDialog != null) {
            activeDialog.setVisible(false);
            activeDialog = null;
            editor = null;
        }
    }
}
