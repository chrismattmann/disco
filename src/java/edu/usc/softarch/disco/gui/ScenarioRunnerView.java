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
import java.util.Vector;
import java.util.List;
import java.io.File;
import javax.swing.*;
import java.awt.event.*;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import edu.usc.softarch.disco.structs.DistributionScenario;

/**
 * User interface to run scenarios.
 * Provides a JPanel which should be embedded into another 
 * container, such as a JDialog or JFrame.
 * 
 * To interact with this view, a controller should implement 
 * {@link ScenarioRunnerDelegate} and call {@link #setDelegate(ScenarioEditorDelegate)}.
 *
 * @author Trevor Johns &lt;<a href="mailto:tjohns@usc.edu">tjohns@usc.edu</a>&gt;
 * @version $Rev: 141 $
 */
public class ScenarioRunnerView implements ActionListener
{
    private JTextField   dcpDirectory;
    private JButton      selectDCPDirectory;
    private JTextField   propertiesFile;
    private JButton      selectPropertiesFile;
    private JComboBox    selector;
    private JList        scenarios;
    private JButton      addScenarioFiles;
    private JButton      addCustomScenario;
    private JButton      removeScenario;
    private JRadioButton displayAllResults;
    private JRadioButton displayLimitedResults;
    private JTextField   displayLimitedResultsCount;
    private JCheckBox    displayScores;
    private JButton      runScenario;
    private JLabel       statusDisplay;
    
    /** The delegate for the view. */
    private ScenarioRunnerDelegate delegate = null;
    
    /** The controller for the view. Should be the same as the delegate.
     * @todo This really should be decoupled from the view, especially considering 
     *       that there is an abstract interface available for this class.
     */
    private ScenarioRunnerController controller = null;
    
    /** The parent frame for the panel so dialogs can be displayed.
     * @todo There should be a better way to get this info.
     */
    private JFrame parentFrame;
    
    /**
     * Class constructor.
     * Creates a new ScenarioRunnerController.
     */
    public ScenarioRunnerView (JFrame parentFrame)
    {
        // Create new controller. Controller will set itself as the 
        // delegate for this class.
        controller = new ScenarioRunnerController(this, parentFrame);
    }
    
    /**
     * Genereate an instance of the UI's JPanel.
     * 
     * @return The JPanel for the UI.
     */
    public JComponent buildPanel ()
    {
        
        // Create all UI elements
        initComponents();
        
        // Create a reusable cell constraints object
        CellConstraints cc = new CellConstraints();
        
        // Prepare master layout
        FormLayout layout = new FormLayout (
            "right:pref, 4dlu, 100dlu:grow, 4dlu, min", 
            "p, d, p, d, p, d, fill:pref:grow, d, top:p, d, top:p, d, p, d, p");
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        
        // Add elements to master layout
        builder.addLabel("DCP Directory:", cc.xy(1,1));
        builder.add(dcpDirectory, cc.xy(3,1));
        builder.add(selectDCPDirectory, cc.xy(5,1));
        
        builder.addLabel("Properties File:", cc.xy(1,3));
        builder.add(propertiesFile, cc.xy(3,3));
        builder.add(selectPropertiesFile, cc.xy(5,3));
        
        builder.addLabel("Selector:", cc.xy(1,5));
        builder.add(selector, cc.xyw(3,5,3));
        
        builder.addLabel("Scenarios:", cc.xy(1,7, "right, top"));
        builder.add(new JScrollPane(scenarios), cc.xyw(3,7,3));
        
        // Create the add/remove scenario buttons
        FormLayout scenarioControlsLayout = new FormLayout (
            "p, d, p, d:grow, p", 
            "p");
        
        PanelBuilder scenarioControlsBuilder = new PanelBuilder(scenarioControlsLayout);
        
        scenarioControlsBuilder.add(addScenarioFiles, cc.xy(1,1));
        scenarioControlsBuilder.add(addCustomScenario, cc.xy(3,1));
        scenarioControlsBuilder.add(removeScenario, cc.xy(5,1));
        
        builder.add(scenarioControlsBuilder.getPanel(), cc.xyw(3,9,3));
        // End add/remove scenario buttons
        
        // Create the display options panel
        builder.addLabel("Display Options:", cc.xy(1,11));
        FormLayout displayOptionsLayout = new FormLayout (
            "pref, 4dlu, pref, 20dlu, pref:grow", 
            "p, d, p, d, p");
        
        PanelBuilder displayOptionsBuilder = new PanelBuilder(displayOptionsLayout);
        
        displayOptionsBuilder.add(displayAllResults, cc.xy(1,1));
        displayOptionsBuilder.addLabel("Display all results", cc.xyw(3,1,3));
        displayOptionsBuilder.add(displayLimitedResults, cc.xy(1,3));
        displayOptionsBuilder.addLabel("Display only ", cc.xy(3,3));
        displayOptionsBuilder.add(displayLimitedResultsCount, cc.xy(4,3));
        displayOptionsBuilder.addLabel(" results", cc.xy(5,3));
        displayOptionsBuilder.add(displayScores, cc.xy(1,5));
        displayOptionsBuilder.addLabel("Enable the display of scores", cc.xyw(3,5,3));
        
        builder.add(displayOptionsBuilder.getPanel(), cc.xyw(2,11,3));
        // End display options panel
        
        // Create the control buttons
        FormLayout buttonLayout = new FormLayout (
            "pref, 4dlu, pref, 4dlu:grow, pref", 
            "p");
        
        PanelBuilder buttonBuilder = new PanelBuilder(buttonLayout);
        
        buttonBuilder.add(statusDisplay, cc.xy(1,1));
        buttonBuilder.add(runScenario, cc.xy(5,1));
        
        builder.add(buttonBuilder.getPanel(), cc.xyw(1,13,5));
        // End control buttons
        
        return builder.getPanel();
    }
    
    /**
     * Initialize all UI input fields.
     */
    private void initComponents ()
    {
        dcpDirectory                = new JTextField();
        selectDCPDirectory          = new JButton("...");
        propertiesFile              = new JTextField();
        selectPropertiesFile        = new JButton("...");
        selector                    = new JComboBox(controller.getScenarioRunnerSelectorList());
        scenarios                   = new JList();
        addScenarioFiles            = new JButton("Add Files");
        addCustomScenario           = new JButton("Add Custom");
        removeScenario              = new JButton("Remove");
        displayAllResults           = new JRadioButton();
        displayLimitedResults       = new JRadioButton();
        displayLimitedResultsCount  = new JTextField();
        displayScores               = new JCheckBox();
        runScenario                 = new JButton("Run Scenario");
        statusDisplay               = new JLabel("");
        
        // Set button groups
        ButtonGroup displaySettings = new ButtonGroup();
        displaySettings.add(displayAllResults);
        displaySettings.add(displayLimitedResults);
        
        // Set default settings
        displayAllResults.setSelected(true);
        displayLimitedResults.setSelected(false);
        displayLimitedResultsCount.setEnabled(false);
        
        // Set action listeners
        runScenario.setDefaultCapable(true);
        runScenario.addActionListener(this);
        addScenarioFiles.addActionListener(this);
        addCustomScenario.addActionListener(this);
        removeScenario.addActionListener(this);
        selectDCPDirectory.addActionListener(this);
        selectPropertiesFile.addActionListener(this);
        displayAllResults.addActionListener(this);
        displayLimitedResults.addActionListener(this);
    }
    
    /**
     * Set the delegate for this view.
     * The specified delegate (usually a controller) will 
     * be notified of any important UI events which occur.
     * 
     * @param d The desired deleage for the view.
     */
    public void setDelegate (ScenarioRunnerDelegate d)
    {
        delegate = d;
    }
    
    /**
     * Action handler for the UI.
     * Dispatches UI events to the delegate for processing.
     *
     * @see #setDelegate(ScenarioEditorDelegate)
     * @see ScenarioRunnerDelegate
     */
    public void actionPerformed (ActionEvent e)
    {
        if (delegate != null) {
            if (e.getSource() == runScenario)
                delegate.scenarioRunnerRunScenarioButton();
            else if (e.getSource() == addScenarioFiles)
                delegate.scenarioRunnerAddScenarioFilesButton();
            else if (e.getSource() == addCustomScenario)
                delegate.scenarioRunnerAddCustomScenarioButton();
            else if (e.getSource() == removeScenario)
                delegate.scenarioRunnerRemoveScenarioButton(scenarios.getSelectedIndices());
            else if (e.getSource() == selectDCPDirectory)
                delegate.scenarioRunnerSelectDCPDirectoryButton();
            else if (e.getSource() == selectPropertiesFile)
                delegate.scenarioRunnerSelectPropertiesFileButton();
            else if (e.getSource() == displayAllResults)
                displayLimitedResultsCount.setEnabled(false);
            else if (e.getSource() == displayLimitedResults)
                displayLimitedResultsCount.setEnabled(true);
        }
    }
    
    /** Get the contents of the DCP Directory field.
     * @return The filename of the DCP directory selected in the UI.
     */
    public String getDCPDirectory ()
    {
        return dcpDirectory.getText();
    }
    
    /** Get the contents of the Property File field.
     * @return The filename of the Property File selected in the UI.
     */
    public String getPropertiesFile ()
    {
        return propertiesFile.getText();
    }
    
    /** Get the contents of the Selector drop down box.
     * @return The complete class name of the currently selected selector as a String.
     */
    public String getSelector ()
    {
        return (String) selector.getSelectedItem();
    }
    
    /** Get the contents of the Scenarios list.
     * @return A collection of all selected DistributionScenarios.
     */
    public void setScenarios(Vector<DistributionScenario> data) {
        scenarios.setListData(data);
    }
    
    /** Get the value of the Display Scores checkbox.
     * @return True if scores should be true, false otherwise.
     */
    public boolean getDisplayScores ()
    {
        return displayScores.isSelected();
    }
    
    /** Get the number of results which should be displayed.
     * @return -1 if result display is unlimited, the numver of scores 
     *         which should be displayed otherwise.
     */
    // FIXME: Requires validation.
    public int getResultsCount ()
    {
        if (!displayLimitedResults.isSelected())
            return -1;
        else return Integer.parseInt(displayLimitedResultsCount.getText());
    }
    
    /** Set the DCP directory path which should be displayed in the UI.
     * @param path The file path which should be displayed.
     */
    public void setDCPDirectory (File path) {
        dcpDirectory.setText(path.toString());
    }
    
    /** Set the properties file path which should be displayed in the UI.
     * @param path The file path which should be displayed.
     */
    public void setPropertiesFile (File path) {
        propertiesFile.setText(path.toString());
    }
    
    /** Update the status display. Use this to inform the user when the application 
     * is busy.
     * @param msg The status message to be displayed. Use an empty string to clear.
     */
    public void setStatusDisplay (String msg) {
        statusDisplay.setText(msg);
    }
    
}
