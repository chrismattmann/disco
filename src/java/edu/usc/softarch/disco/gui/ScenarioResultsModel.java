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

import java.util.List;
import java.util.Iterator;
import javax.swing.*;

import edu.usc.softarch.disco.structs.DistributionScenarioResult;
import edu.usc.softarch.disco.selection.ConnectorRank;


/**
 * Model for storing results from a set of distribution scenarios.
 * This is a wrapper around List<DistributionScenarioResult> and provides 
 * an interface for displaying this data in a tabular format, specifically 
 * in a JTable.
 * 
 * This also provides a convienent structure for future expansion, as opposed 
 * to the rather inflexible list-based interface used elsewhere in the code.
 * However, at the time of writing only the GUI is aware of this model's existence.
 * 
 * The tabular data is provided as three columns (in order): Scenario, Selector, Score
 * 
 * @author Trevor Johns &lt;<a href="mailto:tjohns@usc.edu">tjohns@usc.edu</a>&gt;
 * @version $Rev$
 */
public final class ScenarioResultsModel extends javax.swing.table.AbstractTableModel {
    
    String[] columnNames = {"Scenario",
                            "Connector",
                            "Score"};
    
    List<DistributionScenarioResult> data;
    
    /**
     * Constructor. Requires a DistributionScenarioResult list as input for 
     * the model.
     * @param d A list of diistribution scenario results. Will be used to 
     *          populate the model.
     */
    public ScenarioResultsModel(List<DistributionScenarioResult> d) {
        data = d;
    }
    
    /**
     * Return the number of rows of data in the model.
     */
    public int getRowCount() {
        int count = 0;
        
        for (Iterator<DistributionScenarioResult> i = data.iterator(); i.hasNext(); ) {
            DistributionScenarioResult resultSet = i.next();
            List rankList = resultSet.getRankList();
            
            count += rankList.size();
        }
        
        return count;
    }
    
    /**
     * Return the number of columns of data in the model.
     */
    public int getColumnCount() {
        return 3;
    }
    
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    /**
     * Return the value for a specific cell the the model's table.
     */
    public Object getValueAt(int row, int column) {
        int resultCount = 0;
        DistributionScenarioResult resultSet = null;
        List rankList = null;
        ConnectorRank rank = null;
        boolean found = false;
        
        // Locate the requested row
        for (Iterator<DistributionScenarioResult> i = data.iterator(); i.hasNext() && !found; ) {
            resultSet = i.next();
            rankList = resultSet.getRankList();
            if (resultCount + rankList.size() > row) {
                // Desired row exists in this scenario
                rank = (ConnectorRank) rankList.get(row - resultCount);
                found = true;
            }
            else {
                resultCount += rankList.size();
            }
        }
        
        if (!found)
            throw new IndexOutOfBoundsException();
        else {
            // Return the desired piece of data
            if (column == 0) {
                // Return scenario name
                return resultSet.getScenario().getName();
            }
            else if (column == 1) {
                // Return selector name
                return rank.getConnectorName();
            }
            else if (column == 2) {
                // Return score
                return rank.getRank();
            }
            else {
                // Index out of bounds
                throw new IndexOutOfBoundsException();
            }
        }
    }
}
