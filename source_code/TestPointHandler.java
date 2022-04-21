package fetchrewards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TestPointHandler {
	/**
	 * Tests the addTransaction function for spending over the point totals, then adding one transaction, then combining multiple.
	 */
	@Test 
	public void testHandlerAddTransaction() {
		PointHandler handler = new PointHandler();
		
		Transaction negative = new Transaction("DANNON", -1000, "2020-11-01T14:00:00Z");
		assertEquals(handler.addTransaction(negative), "Not enough points...");
		
		Transaction positive = new Transaction("DANNON", 300, "2020-10-31T10:00:00Z");
		assertEquals(handler.addTransaction(positive), "Success");
		assertEquals(handler.addTransaction(negative), "Not enough points...");
		
		Transaction thousand = new Transaction("DANNON", 1000, "2000-11-01T14:00:00Z");
		handler.addTransaction(thousand);
		assertEquals(handler.getRunningPointTotals(), "Totals: \n{\"DANNON\":1300}");
		
		handler.addTransaction(negative);
		assertEquals(handler.getRunningPointTotals(), "Totals: \n{\"DANNON\":300}");
	}
	/**
	 * Tests spending above global total, and then spending of oldest points, similar to points.pdf.
	 */
	@Test
	public void testHandlerSpendPoints() {
		PointHandler handler = new PointHandler();
		
		handler.addTransaction(new Transaction("DANNON", 1000, "2020-11-02T14:00:00Z"));
		handler.addTransaction(new Transaction("UNILEVER", 200, "2020-10-31T11:00:00Z"));
		handler.addTransaction(new Transaction("DANNON", -200, "2020-10-31T15:00:00Z"));
		handler.addTransaction(new Transaction("MILLER COORS", 10000, "2020-11-01T14:00:00Z"));
		handler.addTransaction(new Transaction("DANNON", 300, "2020-10-31T10:00:00Z"));
		
		assertEquals(handler.spendPoints(20000), "Not enough points...");
		assertEquals(handler.spendPoints(5000), "{\"UNILEVER\":-200,\"MILLER COORS\":-4700,\"DANNON\":-100}");
	}
}
