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

import edu.usc.softarch.disco.structs.DistributionScenario;

/** 
 * Protocol for ScenarioRunner controller/view communication.
 * Methods in this interface must be implemented by the 
 * ScenarioRunnerView's delegate. They will be called when the 
 * associated action occurs.
 *
 * @see ScenarioRunnerView
 * @author Trevor Johns &lt;<a href="mailto:tjohns@usc.edu">tjohns@usc.edu</a>&gt;
 * @version $Rev: 141 $
 */
public interface ScenarioRunnerDelegate
{
    /** Run scenario button pressed. */
    public void scenarioRunnerRunScenarioButton ();
        
    /** Add scenario files button pressed. */
    public void scenarioRunnerAddScenarioFilesButton ();
    
    /** Add custom scenario button pressed. */
    public void scenarioRunnerAddCustomScenarioButton ();
    
    /** Remove scenario button pressed.
     * @param scenarioIndicies The list of scenario's (by index) selected 
     *          when the button was pressed.
     */
    public void scenarioRunnerRemoveScenarioButton (int[] scenarioIndices);
    
    /** Select DCP directory button pressed. */
    public void scenarioRunnerSelectDCPDirectoryButton ();
    
    /** Select properties file button pressed. */
    public void scenarioRunnerSelectPropertiesFileButton ();
    
    /** Get list of scenario choices for selection in the ScenarioRunner UI. */
    public String[] getScenarioRunnerSelectorList ();
}
