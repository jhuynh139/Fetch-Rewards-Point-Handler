package fetchrewards;
/**
 * Represents an individual transaction submitted by the user.
 * @author Johnson Huynh
 *
 */
public class Transaction implements Comparable<Transaction> {
	private String payer;
	private int points;
	/**
	 * DateTime stamp in ISO.INSTANT format. 
	 */
	private String timestamp;
	/**
	 * Creates new transaction with the specified arguments.
	 * @param payer The payer's/partner's name.
	 * @param points The points added/spent for this transaction.
	 * @param timestamp The time the transaction was carried out in ISO.INSTANT format.
	 */
	public Transaction(String payer, int points, String timestamp) {
        this.payer = payer;
        this.points = points;
        this.timestamp = timestamp;
	}
	/**
	 * Gets a transaction's points.
	 * @return An integer amount of points for the transaction.
	 */
	public int getPoints() {
		return points;
	}
	/**
	 * Updates a transaction's point total.
	 * @param points An integer to set a transaction's point total to.
	 */
	public void setPoints(int points) {
		this.points = points;
	}
	/**
	 * Returns payer's/partner's name for the transaction.
	 * @return A string for the payer's/partner's name.
	 */
	public String getPayer() {
		return payer;
	}
    /**
     * Returns a transaction's time stamp.
     * @return A string representation of the transaction's time stamp.
     */
    public String getTimestamp(){
    	return timestamp;
    }
	/**
	 * Compares transactions by time stamp. Used to maintain priority heap.
	 * @return An integer representing the order of the transactions.
	 */
	@Override
	public int compareTo(Transaction o) {
		return this.timestamp.compareTo(o.timestamp);
	}
	
}
