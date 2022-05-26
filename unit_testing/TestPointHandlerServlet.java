package fetchrewards;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TestPointHandlerServlet extends Mockito{
	/**
	 * Servlet instance to test.
	 */
	static PointHandlerServlet servlet;
	/**
	 * Initializes the servlet and its PointHandler
	 */
	@BeforeAll
	public static void instantiate() {
		servlet = new PointHandlerServlet();
	}
	/**
	 * Asserts that "No empty fields!!!" is sent upon empty parameters.
	 * Also verifies that the HttpServletResponse.SC_BAD_REQUEST status is set.
	 * @throws Exception I/O Exception from getWriter.
	 */
	@Test
	public void testServletAddPOSTEmpty() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		
		when(request.getRequestURI()).thenReturn("/add");
		
		servlet.doPost(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("No empty fields!!!"));
		
		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}
	/**
	 * Asserts that "That's not a number!!!" is sent upon a non-parsable points parameter.
	 * Also verifies that the HttpServletResponse.SC_BAD_REQUEST status is set.
	 * @throws Exception I/O Exception from getWriter.
	 */
	@Test
	public void testServletAddPOSTNaNPoints() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		
		when(request.getRequestURI()).thenReturn("/add");
		when(request.getParameter("points")).thenReturn("32a");
		when(request.getParameter("payer")).thenReturn("Venmo");
		when(request.getParameter("timestamp")).thenReturn("2020-11-01T14:00:00Z");
		
		servlet.doPost(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("That's not a number!!!"));

		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}
	/**
	 * Asserts that "Need a valid timestamp (ISO.INSTANT)." is sent upon wrongly formatted timestamp.
	 * Also verifies that the HttpServletResponse.SC_BAD_REQUEST status is set.
	 * @throws Exception I/O Exception from getWriter.
	 */
	@Test
	public void testServletValidateTimestamp() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		
		servlet.init(mock(ServletConfig.class));
		
		when(request.getRequestURI()).thenReturn("/add");
		when(request.getParameter("points")).thenReturn("300");
		when(request.getParameter("payer")).thenReturn("Venmo");
		when(request.getParameter("timestamp")).thenReturn("2020-14-01T14:00:00Z");
		
		servlet.doPost(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("Need a valid timestamp (ISO.INSTANT)."));
		
		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}
	/**
	 * Asserts that "Not enough points..." is sent when a negative transaction with a point total greater than the available number of points is sent. 
	 * Also verifies that the HttpServletResponse.SC_BAD_REQUEST status is set.
	 * @throws Exception I/O Exception from getWriter.
	 */
	@Test
	public void testServletAddNotEnough() throws Exception{
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		
		servlet.init(mock(ServletConfig.class));
		
		when(request.getRequestURI()).thenReturn("/add");
		when(request.getParameter("points")).thenReturn("-2000");
		when(request.getParameter("payer")).thenReturn("Venmo");
		when(request.getParameter("timestamp")).thenReturn("2020-12-01T14:00:00Z");
		
		servlet.doPost(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("Not enough points..."));
		
		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}
	/**
	 * Asserts that "Not enough points..." is sent when a spend with a point total greater than the available number of points is sent.
	 * Also verifies that the HttpServletResponse.SC_BAD_REQUEST status is set.
	 * @throws Exception I/O Exception from getWriter.
	 */
	@Test
	public void testServletSpendNotEnough() throws Exception{
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		
		servlet.init(mock(ServletConfig.class));
		
		when(request.getRequestURI()).thenReturn("/spend");
		when(request.getParameter("points")).thenReturn("2000");
		
		servlet.doPost(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("Not enough points..."));
		
		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}
	/**
	 * Tests add and get by asserting that a transaction is posted and displayable.
	 * @throws Exception I/O Exception from getWriter.
	 */
	@Test
	public void testServletAddAndGet() throws Exception{
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		
		servlet.init(mock(ServletConfig.class));
		
		when(request.getRequestURI()).thenReturn("/add");
		when(request.getParameter("points")).thenReturn("2000");
		when(request.getParameter("payer")).thenReturn("Venmo");
		when(request.getParameter("timestamp")).thenReturn("2020-12-01T14:00:00Z");
		
		servlet.doPost(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("Success"));
		
		servlet.doGet(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("Totals: \n{\"VENMO\":2000}"));
	}
	/**
	 * Tests running point totals and spend by sending a chunk of transactions and asserting that the totals are expected.
	 * @throws Exception I/O Exception from getWriter.
	 */
	@Test
	public void testServletBulkAddAndSpend() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		
		servlet.init(mock(ServletConfig.class));
		
		when(request.getRequestURI()).thenReturn("/add");
		when(request.getParameter("points")).thenReturn("1000");
		when(request.getParameter("payer")).thenReturn("DANNON");
		when(request.getParameter("timestamp")).thenReturn("2020-11-02T14:00:00Z");
		servlet.doPost(request, response);
		
		when(request.getRequestURI()).thenReturn("/add");
		when(request.getParameter("points")).thenReturn("200");
		when(request.getParameter("payer")).thenReturn("UNILEVER");
		when(request.getParameter("timestamp")).thenReturn("2020-10-31T11:00:00Z");
		servlet.doPost(request, response);
		
		when(request.getRequestURI()).thenReturn("/add");
		when(request.getParameter("points")).thenReturn("-200");
		when(request.getParameter("payer")).thenReturn("DANNON");
		when(request.getParameter("timestamp")).thenReturn("2020-10-31T15:00:00Z");
		servlet.doPost(request, response);
		
		when(request.getRequestURI()).thenReturn("/add");
		when(request.getParameter("points")).thenReturn("10000");
		when(request.getParameter("payer")).thenReturn("MILLER COORS");
		when(request.getParameter("timestamp")).thenReturn("2020-11-01T14:00:00Z");
		servlet.doPost(request, response);
		
		when(request.getRequestURI()).thenReturn("/add");
		when(request.getParameter("points")).thenReturn("300");
		when(request.getParameter("payer")).thenReturn("DANNON");
		when(request.getParameter("timestamp")).thenReturn("2020-10-31T10:00:00Z");
		servlet.doPost(request, response);
		
		servlet.doGet(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("Totals: \n{\"UNILEVER\":200,\"MILLER COORS\":10000,\"DANNON\":1100}"));
		
		when(request.getRequestURI()).thenReturn("/spend");
		when(request.getParameter("points")).thenReturn("5000");
		servlet.doPost(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("{\"UNILEVER\":-200,\"MILLER COORS\":-4700,\"DANNON\":-100}"));
	}
}
