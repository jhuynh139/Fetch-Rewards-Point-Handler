package fetchrewards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import org.json.JSONObject;

public class PointHandler {
    /**
     * A Map of priority heaps to spend oldest points more readily, from specific payers/spenders.
     */
    private final Map<String, PriorityQueue<Transaction>> committedTransactions;
    /**
     * A Map of running point totals, so that deductions do not need to be processed before displaying totals.
     */
    private final Map<String, Integer> runningPointTotals;
    /**
     * Deductions to process when a spend is requested.
     */
    private final ArrayList<Transaction> deductions;
    private int totalPoints;
    /**
     * Initializes the objects above to run the points logic.
     */
    public PointHandler(){
        committedTransactions = new HashMap<>();
        runningPointTotals = new HashMap<>();
        totalPoints = 0;
        deductions = new ArrayList<>();
    }
    /**
     * Returns the running point totals.
     * @return A string representation of a JSONObject of the running totals map.
     */
    public String getRunningPointTotals(){
        return "Totals: \n" + new JSONObject(runningPointTotals).toString();
    }
    /**
     * Checks if there are enough points to deduct if a transaction with negative points sent, then adds it to the deductions ArrayList 
     * for future processing when a spend is requested. Otherwise, adds the transaction to one of the min-heaps, then point totals are updated.
     * @param t The transaction to be added to the maps. 
     * @return A string returning the status of the add attempt.
     */
    public String addTransaction(Transaction t){
        if(t.getPoints() < 0){
            if (!runningPointTotals.keySet().contains(t.getPayer()) || runningPointTotals.get(t.getPayer()) + t.getPoints() < 0){
                return "Not enough points...";
            } else {
                deductions.add(t);
            }
        } else {
            if (!committedTransactions.keySet().contains(t.getPayer())){
                committedTransactions.put(t.getPayer(), new PriorityQueue<>());
            }
            committedTransactions.get(t.getPayer()).add(t);
        }
        
        if (!runningPointTotals.keySet().contains(t.getPayer())){
        	runningPointTotals.put(t.getPayer(), 0);
        }
        runningPointTotals.put(t.getPayer(), runningPointTotals.get(t.getPayer()) + t.getPoints());
        
        totalPoints += t.getPoints();
        
        return "Success";
    }
    /**
     * Checks if there are enough points to cover for the spend request; if so, spends points in order of timestamp, regardless of payer/partner or amount,
     * and then updates point totals.
     * @param pointsToSpend Integer amount of points to spend from the pool
     * @return A string representing either a negative status, or a JSONObject with spent point totals from payers/partners. 
     */
    public String spendPoints(int pointsToSpend){
        if (pointsToSpend > totalPoints){
            return "Not enough points...";
        }
        
        totalPoints -= pointsToSpend;
        
        for(Transaction t: deductions){
            int pointsToDeduct = -t.getPoints();
            String payer = t.getPayer();
            while (pointsToDeduct > 0){
                pointsToDeduct = pointsToDeduct - spendPointsFrom(payer, pointsToDeduct);
            }
        }
        deductions.clear();
        
        Map<String, Integer> spentPointTotals = new HashMap<>();

        while(pointsToSpend > 0){
            Transaction oldest = new Transaction("N/A", 0, "N/A");
            int spentPoints;
            for (String key: committedTransactions.keySet()){
                if (committedTransactions.get(key).peek().compareTo(oldest) < 0){
                       oldest = committedTransactions.get(key).peek();
                }
            }
            
            spentPoints = spendPointsFrom(oldest.getPayer(), pointsToSpend);
            
            if (!spentPointTotals.keySet().contains(oldest.getPayer())){
            	spentPointTotals.put(oldest.getPayer(), 0);
            }
            spentPointTotals.put(oldest.getPayer(), spentPointTotals.get(oldest.getPayer()) - spentPoints);
            
            runningPointTotals.put(oldest.getPayer(), runningPointTotals.get(oldest.getPayer()) - spentPoints);
            pointsToSpend -= spentPoints;
        }
        return new JSONObject(spentPointTotals).toString();
    }
    /**
     * Spends points up to the payer's/partner's oldest transaction's leftover points.
     * @param payer Payer/partner who will spend points.
     * @param pointsToSpend An integer representing points that need to be spent.
     * @return An integer representing points that were spent in this call.
     */
    private int spendPointsFrom(String payer, int pointsToSpend){
        int transactionPoints = committedTransactions.get(payer).peek().getPoints();
        int spentPoints = 0;
            if (pointsToSpend >= transactionPoints){
                committedTransactions.get(payer).poll();
                if(committedTransactions.get(payer).isEmpty()) {
                	committedTransactions.remove(payer);
                }
                spentPoints = transactionPoints;
            } else {
                committedTransactions.get(payer).peek().setPoints(transactionPoints - pointsToSpend);
                spentPoints = pointsToSpend;
            }
            
        return spentPoints;
    }
}
