package cloud;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.rds.AmazonRDSClient;

public class RDSManager {
	AWSCredentials credentials;
	AmazonRDSClient rds;
	String jdbcUrl;
	
	Connection connection;
	
	public RDSManager() {
		init();
	}
	
	private void init() {
		try {
			credentials = new PropertiesCredentials(
					RDSManager.class.getResourceAsStream("../AwsCredentials.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rds = new AmazonRDSClient(credentials);
		String dbName = "myyoutube";
		String userName = "myyoutube";
		String password = "myyoutube";
		String hostname = "myyoutube.ccdolhqfikzh.us-east-1.rds.amazonaws.com";
		String port = "3306";
		String driver = "com.mysql.jdbc.Driver";
		
		jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName;
		
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(jdbcUrl, userName, password);
			
			ResultSet result = ExecuteResultQuery("SHOW TABLES LIKE 'videos';");
			if (!result.next()) {
				//Table 'videos' does not exist
				ExecuteNonResultQuery("CREATE TABLE videos (name VARCHAR(500), rate DOUBLE, " 
						+ "numratings INT, timestamp VARCHAR(50), PRIMARY KEY (name));");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet ExecuteResultQuery(String Query) {
		ResultSet result = null;
		try {
			Statement statement = connection.createStatement();
			result = statement.executeQuery(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public void ExecuteNonResultQuery(String Query) {
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(Query);
			preparedStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean CheckExistQuery(String Query) {
		ResultSet result = ExecuteResultQuery(Query);
		boolean exist = false;
		try {
			exist = result.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exist;
	}
}
