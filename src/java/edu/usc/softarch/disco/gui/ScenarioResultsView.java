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

import edu.usc.softarch.disco.util.TableSorter;

/**
 * View for displaying results from running a scenario.
 *
 * @author Trevor Johns &lt;<a href="mailto:tjohns@usc.edu">tjohns@usc.edu</a>&gt;
 * @version $Rev$
 */
public class ScenarioResultsView extends java.util.Observable
{
    private int MIN_HEIGHT = 100;
    private int MIN_WIDTH = 300;
    
    private static int untitledCount = 0;

    private JFrame window;
    private JTable dataTable;
    ScenarioResultsModel model;
    TableSorter sorter;
    
    /**
     * Constructor. Initialize any UI components and set the data source.
     */
    public ScenarioResultsView (ScenarioResultsModel m)
    {
        // Register the model and wrap it with a TableSorter
        // FIXME: TableSorter treats all cell contents as strngs. As a 
        //        result, cells containing scientific notation are sorted 
        //        incorrectly.
        model = m;
        sorter = new TableSorter(m);
        dataTable = new JTable(sorter);
        sorter.setTableHeader(dataTable.getTableHeader());
        
        // Build the window
        window = new JFrame("Untitled " + getUntitledCountAndIncrement());
        window.setSize(MIN_WIDTH, MIN_HEIGHT);
        
        // Populate the window
        Container panel = window.getContentPane();
        panel.setLayout(new BorderLayout());
        panel.add(dataTable.getTableHeader(), BorderLayout.PAGE_START);
        panel.add(new JScrollPane(dataTable), BorderLayout.CENTER);
    }
     
     /**
      * Obtain the next unread document number and increment the counter.
      * @return The number of the next unread document.
      */
     private static int getUntitledCountAndIncrement() {
        return ++untitledCount;
     }
     
     /**
      * Display the view's window, creating any objects as necessary.
      */
     public void showWindow()
     {
        window.setVisible(true);
     }
     
     /**
      * Hide the view's window, creating any objects as necessary.
      */
     public void hideWindow() {
        window.setVisible(false);
     }
}
