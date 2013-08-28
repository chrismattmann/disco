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

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.factories.ButtonBarFactory;

/**
 * User interface to edit scenarios.
 * Provides a JPanel which should be embedded into another 
 * container, such as a JDialog or JFrame.
 * 
 * To interact with this view, a controller should implement 
 * {@link ScenarioEditorDelegate} and call {@link #setDelegate(ScenarioEditorDelegate)}.
 *
 * @author Trevor Johns &lt;<a href="mailto:tjohns@usc.edu">tjohns@usc.edu</a>&gt;
 * @version $Rev: 141 $
 */
public class ScenarioEditorView implements ActionListener
{    
    private JPanel     panel;
    private JTextField scenarioName;
    private JTextField minVolume;
    private JComboBox  minVolumeUnits;
    private JTextField maxVolume;
    private JComboBox  maxVolumeUnits;
    private JTextField deliveryVolume;
    private JComboBox  deliveryVolumeUnits;
    private JTextField deliveryMinIntervals;
    private JTextField deliveryMaxIntervals;
    private JTextField minUsers;
    private JTextField maxUsers;
    private JTextField minUserTypes;
    private JTextField maxUserTypes;
    private JList  geographicDistribution;
    private JList  accessPolicy;
    private JTextField consistency;
    private JTextField scalability;
    private JTextField dependability;
    private JTextField efficiency;
    private JCheckBox  dataTypesData;
    private JCheckBox  dataTypesMetadata;
    private JButton    save;
    private JButton    cancel;
    
    // TODO: The following constants should be specified elsewhere in the system.
    /** List of valid data units to be displayed in the UI. */
    public static final String[] DATA_UNITS = {"TB", "GB", "MB", "KB", "B"};
    
    /** List of valid geographic distributions for the UI. */
    public static final String[] GEOGRAPHIC_DISTRIBUTIONS = {"", "WAN", "LAN"};
    
    /** List of valid access policies for the UI. */
    public static final String[] ACCESS_POLICIES = {"", "Access Control List"};
    
    /** The delegate for the view. */
    private ScenarioEditorDelegate delegate = null;
    
    /**
     * Class constructor. Currently doesn't do anything.
     * Call {@link #buildPanel()} to get an instance of the UI. May be changed 
     * in the future.
     */
    public ScenarioEditorView ()
    {
        
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
            "right:pref, 4dlu, pref:grow, 4dlu, pref, 4dlu, right:pref, 4dlu, pref:grow, 4dlu, pref", 
            "");
        
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        
        // Add elements to master layout
        builder.appendSeparator("General");
        builder.append("Scenario Name:", scenarioName, 9);
        builder.nextLine();
        
        builder.appendSeparator("Total Volume");
        builder.append("Minimum:", minVolume, minVolumeUnits);
        builder.append("Maximum:", maxVolume, maxVolumeUnits);
        builder.nextLine();
        
        builder.appendSeparator("Delivery Schedule");
        builder.append("Volume:", deliveryVolume, deliveryVolumeUnits);
        builder.nextLine();
        builder.append("Minimum Interval:", deliveryMinIntervals, 3);
        builder.append("Maximum Interval:", deliveryMaxIntervals, 3);
        builder.nextLine();
        
        builder.appendSeparator("Users");
        builder.append("Minimum Users:", minUsers, 3);
        builder.append("Maximum Users:", maxUsers, 3);
        builder.nextLine();
        builder.append("Minimum Types:", minUserTypes, 3);
        builder.append("Maximum Types:", maxUserTypes, 3);
        builder.nextLine();
        
        builder.appendSeparator("Network Properties");
        builder.append("Geographic Distribution:", geographicDistribution, 3);
        builder.append("Access Policy:", accessPolicy, 3);
        builder.nextLine();
        
        builder.appendSeparator("Perfomance Requirements");
        builder.append("Consistency:", consistency, 3);
        builder.append("Dependability:", dependability, 3);
        builder.append("Scalability:", scalability, 3);
        builder.append("Efficiency:", efficiency, 3);
        builder.nextLine();
        
        builder.appendSeparator("Data Types");
        builder.append("Data:", dataTypesData, 3);
        builder.nextLine();
        builder.append("Metadata:", dataTypesMetadata, 3);
        
        builder.nextLine();
        builder.append(ButtonBarFactory.buildOKCancelBar(save, cancel), 11);
        
        return builder.getPanel();
    }
    
    /**
     * Initialize all UI input fields.
     */
    private void initComponents()
    {
        scenarioName            = new JTextField();
        minVolume               = new JTextField();
        minVolumeUnits          = new JComboBox(DATA_UNITS);
        maxVolume               = new JTextField();
        maxVolumeUnits          = new JComboBox(DATA_UNITS);
        deliveryVolume          = new JTextField();
        deliveryVolumeUnits     = new JComboBox(DATA_UNITS);
        deliveryMinIntervals    = new JTextField();
        deliveryMaxIntervals    = new JTextField();
        minUsers                = new JTextField();
        maxUsers                = new JTextField();
        minUserTypes            = new JTextField();
        maxUserTypes            = new JTextField();
        geographicDistribution  = new JList(GEOGRAPHIC_DISTRIBUTIONS);
        accessPolicy            = new JList(ACCESS_POLICIES);
        consistency             = new JTextField();
        scalability             = new JTextField();
        dependability           = new JTextField();
        efficiency              = new JTextField();
        dataTypesData           = new JCheckBox();
        dataTypesMetadata       = new JCheckBox();
        save                    = new JButton("Save");
        cancel                  = new JButton("Cancel");
        save.setDefaultCapable(true);
		save.addActionListener(this);
		cancel.addActionListener(this);
    }
    
    /**
     * Set the delegate for this view.
     * The specified delegate (usually a controller) will 
     * be notified of any important UI events which occur.
     * 
     * @param d The desired deleage for the view.
     */
    public void setDelegate (ScenarioEditorDelegate d)
    {
        delegate = d;
    }
    
    /**
     * Action handler for the UI.
     * Dispatches UI events to the delegate for processing.
     *
     * @see #setDelegate(ScenarioEditorDelegate)
     * @see ScenarioEditorDelegate
     */
	public void actionPerformed (ActionEvent e)
	{
        if (delegate != null) {
            if (e.getSource() == cancel)
                delegate.scenarioEditorCancelButton();
            else if (e.getSource() == save)
                delegate.scenarioEditorSaveButton();
        }
    }
    
    // Accessor methods
    // FIXME: These need validation, somehow. This will take tiem to write though since 
    //        a lot of the internal data structures are just strings.
    
    public boolean isScenarioNameSet () {
        if (scenarioName.getText() == "")
            return false;
        else
            return true;
    }
    
    public String getScenarioName () {
        return scenarioName.getText();
    }
    
    
    public boolean isMinVolumeSet () {
        if (minVolume.getText() == "")
            return false;
        else
            return true;
    }
    
    public String getMinVolume () {
        return minVolume.getText() + " " + minVolumeUnits.getSelectedItem();
    }
    
    
    public boolean isMaxVolumeSet () {
        if (maxVolume.getText() == "")
            return false;
        else
            return true;
    }
    
    public String getMaxVolume () {
        return maxVolume.getText() + " " + maxVolumeUnits.getSelectedItem();
    }
    
    public boolean isDeliveryVolumeSet () {
        if (deliveryVolume.getText() == "")
            return false;
        else
            return true;
    }
    
    public String getDeliveryVolume () {
        return deliveryVolume.getText() + " " + deliveryVolumeUnits.getSelectedItem();
    }
    
    public boolean isDeliveryMinIntervalsSet () {
        if (deliveryMinIntervals.getText() == "")
            return false;
        else
            return true;
    }
    
    public String getDeliveryMinIntervals () {
        return deliveryMinIntervals.getText();
    }
    
    public boolean isDeliveryMaxIntervalsSet () {
        if (deliveryMaxIntervals.getText() == "")
            return false;
        else
            return true;
    }
    
    public String getDeliveryMaxIntervals () {
        return deliveryMaxIntervals.getText();
    }
    
    public boolean isMinUsersSet () {
        if (minUsers.getText() == "")
            return false;
        else
            return true;
    }
    
    public String getMinUsers () {
        return minUsers.getText();
    }
    
    public boolean isMaxUsersSet () {
        if (maxUsers.getText() == "")
            return false;
        else
            return true;
    }
    
    public String getMaxUsers () {
        return maxUsers.getText();
    }
    
    public boolean isMinUserTypesSet () {
        if (minUserTypes.getText() == "")
            return false;
        else
            return true;
    }
    
    public String getMinUserTypes () {
        return minUserTypes.getText();
    }
    
    public boolean isMaxUserTypesSet () {
        if (maxUserTypes.getText() == "")
            return false;
        else
            return true;
    }
    
    public String getMaxUserTypes () {
        return maxUserTypes.getText();
    }
    
    public boolean isGeographicDistributionSet () {
        if (geographicDistribution.isSelectionEmpty())
            return false;
        else
            return true;
    }
    
    public Object[] getGeographicDistribution () {
        return geographicDistribution.getSelectedValues();
    }
    
    public boolean isAccessPolicySet () {
        if (accessPolicy.isSelectionEmpty())
            return false;
        else
            return true;
    }
    
    public Object[] getAccessPolicy () {
        return accessPolicy.getSelectedValues();
    }
    
    public boolean isConsistencySet () {
        if (consistency.getText() == "")
            return false;
        else
            return true;
    }
    
    public String getConsistency () {
        return consistency.getText();
    }
    
    public boolean isDependabilitySet () {
        if (dependability.getText() == "")
            return false;
        else
            return true;
    }
    
    public String getDependability () {
        return dependability.getText();
    }
    
    public boolean isScalabilitySet () {
        if (scalability.getText() == "")
            return false;
        else
            return true;
    }
    
    public String getScalability () {
        return scalability.getText();
    }
    
    public boolean isEfficiencySet () {
        if (efficiency.getText() == "")
            return false;
        else
            return true;
    }
    
    public String getEfficiency () {
        return efficiency.getText();
    }
        
    public ArrayList<String> getDataTypes () {
        ArrayList<String> results = new ArrayList<String>();
        
        if (dataTypesData.isSelected()) {
            results.add("data");
        }
        if (dataTypesMetadata.isSelected()) {
            results.add("metadata");
        }
        return results;
    }
    
}
