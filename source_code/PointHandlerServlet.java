package fetchrewards;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PointHandlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * Instance of PointHandler object that handles the points logic.
	 */
	private PointHandler pointHandler;
	/**
	 * DateTime format that the time stamps need to conform to.
	 */
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_INSTANT;
	/**
	 * Initializes the point handler.
	 */
	public void init(ServletConfig config) throws ServletException{
		pointHandler = new PointHandler();
	}
	/**
	 * Calls the point handler to get the current running point totals upon a GET.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		sendResponse(response, pointHandler.getRunningPointTotals());
	}
	/**
	 * Calls the point handler to spend points or add transactions based on the form actions upon a POST.
	 * @return Returns either a status, upon an add, POST to an incorrect URL or missing parameters, 
	 * or a string representation of a JSONObject of total spent points, upon a spend.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException{
		String requestURI = request.getRequestURI();
		String action = requestURI.substring(requestURI.lastIndexOf('/') + 1);
		String status = "";
		try {
	        if (action.equals("add")){
	            
	        	if(request.getParameter("payer") == "" || request.getParameter("payer") == null || request.getParameter("timestamp") == "" ||request.getParameter("timestamp") == null) {
	            	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        		sendResponse(response, "No empty fields!!!");
	            	return;
	            }
	          
	        	String payer = request.getParameter("payer").toUpperCase();
	            int points = Integer.parseInt(request.getParameter("points"));
	            
	            if (!validateTimestamp(request.getParameter("timestamp"))){
	            	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            	sendResponse(response, "Need a valid timestamp (ISO.INSTANT).");
	            	return;
	            }
	            String timestamp = request.getParameter("timestamp");
	            
	            status = pointHandler.addTransaction(new Transaction(payer, points, timestamp));  
	        } else if (action.equals("spend")){
	        	int points = Integer.parseInt(request.getParameter("points"));
	            
	            status = pointHandler.spendPoints(points);
	        }
	    } catch(NumberFormatException e) {
	    	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	    	sendResponse(response, "That's not a number!!!");
	        return;
	    }
        if(status.contains("Not enough points...")) {
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        sendResponse(response, status);
	}
	/**
	 * Validates a time stamp to ISO.INSTANT standard.
	 * @param timestamp String to validate.
	 * @return True if the string parses into an ISO.INSTANT time stamp, false otherwise.
	 */
	private boolean validateTimestamp(String timestamp) {
		try {
			dateFormatter.parse(timestamp);
		} catch (DateTimeParseException e) {
			return false;
		}
		return true;
	}
	/**
	 * Outputs messages to the client.
	 * @param response Response from HttpServlet.
	 * @param message A string returning status, a string representation of a JSON response, or an error.
	 */
	private void sendResponse(HttpServletResponse response, String message) {
        try {
            PrintWriter out = response.getWriter();
            out.print(message);
            out.flush();
        }
        catch(Exception e) {
            throw new RuntimeException(Integer.toString(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }
    }
}
	

