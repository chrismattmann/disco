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

import java.awt.*;
import javax.swing.*;

/**
 * Entry point for the GUI. Intended to be launched when the project 
 * JAR file is executed.
 *
 * @author Trevor Johns &lt;<a href="mailto:tjohns@usc.edu">tjohns@usc.edu</a>&gt;
 * @version $Rev: 159 $
 */
public final class Launcher
{
    /**
     * Launches a new JFrame containing the DISCO GUI.
     */
    public static void main (String[] args)
    {
        JFrame frame = new JFrame();
        frame.setTitle("DISCO");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                
        JComponent scenarioRunner = new ScenarioRunnerView(frame).buildPanel();
                
        // Build a placeholder config tab
        JPanel configTab = new JPanel();
        JLabel configLabel = new JLabel("Display list of configuration files here.");
        configLabel.setFont(configLabel.getFont().deriveFont(java.awt.Font.BOLD, 18));
        configTab.add(configLabel);
        
        // Create tab view
        JTabbedPane appTabs = new JTabbedPane();
        appTabs.addTab("Configuration", configTab);
        appTabs.addTab("Run Scenario",  scenarioRunner);
        
        frame.getContentPane().add(appTabs);
        frame.pack();
        frame.setVisible(true);
    }
}
