package cloud;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateRate extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7048722003060884670L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		RDSManager rdsManager = new RDSManager();
		
		String filename = request.getParameter("videoList");
		if (filename == null) {
			response.sendRedirect("../list.jsp");
			return;
		}
		
		String rate = request.getParameter("rate");
		if (rate.equals("-")) {
			response.sendRedirect("../list.jsp");
			return;
		}
		
		String query = "SELECT * FROM videos WHERE name='" + filename + "';";
		ResultSet result = rdsManager.ExecuteResultQuery(query);
		try {
			if (result.next()) {
				double oldRate = result.getDouble("rate");
				int numratings = result.getInt("numratings");
				double newRate = (Double.parseDouble(rate) + oldRate * numratings) / (numratings + 1);
				newRate = ((int)(newRate * 1000) + 0.0) / 1000;
				numratings++;
				query = "UPDATE videos SET rate=" + newRate + ", numratings=" + numratings 
						+ " WHERE name='" +filename +"';";
				rdsManager.ExecuteNonResultQuery(query);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.sendRedirect("../list.jsp");
	}
}
