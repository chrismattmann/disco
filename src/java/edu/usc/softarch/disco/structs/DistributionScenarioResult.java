package edu.usc.softarch.disco.structs;

import java.io.PrintStream;
import java.util.List;
import java.util.Iterator;

import edu.usc.softarch.disco.selection.ConnectorRank;
import edu.usc.softarch.disco.structs.DistributionScenario;

/**
 * Pairing between a {@link DistributionScenario} and a collection of {@link ConnectorRank}s.
 *
 * @author Trevor Johns &lt;<a href="mailto:tjohns@usc.edu">tjohns@usc.edu</a>&gt;
 * @version $Rev: 131 $
 */
public final class DistributionScenarioResult {
    private DistributionScenario scenario;
    private List rankList;
    
    /**
     * Create a new DistributionScenarioResult with given parameters.
     *
     * @param s The distribution scenario for which the list of {@link ConnectorRank}s applies to.
     * @param l The list of {@link ConnectoRank} objects.
     */
    public DistributionScenarioResult(DistributionScenario s, List l) {
        scenario = s;
        rankList = l;
    }
    
    /**
     * Default constructor. Initializes all parameters to null.
     */
    public DistributionScenarioResult() {
        scenario = null;
        rankList = null;
    }
    
    /**
     * Set the scenario being represented.
     */
    public void setScenario(DistributionScenario s) {
        scenario = s;
    }
    
    /**
     * Get the scenario being represented.
     */
    public DistributionScenario getScenario() {
        return scenario;
    }
    
    /**
     * Set the list of {@link ConnectorRank} objects for the represented sceanrio.
     */
    public void setRankList(List l) {
        rankList = l;
    }
    
    /**
     * Get the list of {@link ConnectorRank} objects for the represented sceanrio.
     */
    public List getRankList() {
        return rankList;
    }
    
    public void print(int topN, boolean printScores) {
        print(topN, printScores, System.out);
    }
    
    public void print(int topN, boolean printScores, PrintStream s) {
        if (rankList != null && rankList.size() > 0) {
            int numShown = 0;
            s.println("Scenario: [" + scenario + "]");
            for (Iterator j = rankList.iterator(); j.hasNext();) {
                if (topN != -1 && topN > 0) {
                    if (numShown == topN) {
                        break;
                    }
                }
                ConnectorRank rank = (ConnectorRank) j.next();
                s.println("Connector: [" + rank.getConnectorName()
                    + "]"+(printScores ? "|Rank: [" + rank.getRank() + "]":""));
                numShown++;
            }
        }
    }
    
    public static void printScenarioResults(List<DistributionScenarioResult> results, int topN, boolean printScores) {
        printScenarioResults(results, topN, printScores, System.out);
    }

    public static void printScenarioResults(List<DistributionScenarioResult> results, int topN, boolean printScores, 
      PrintStream s) {
        for (Iterator<DistributionScenarioResult> i = results.iterator(); i.hasNext(); ) {
            DistributionScenarioResult result = i.next();
            result.print(topN, printScores, s);
        }
    }
}
