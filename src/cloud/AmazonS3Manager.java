package cloud;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3Client;

public class AmazonS3Manager {
	
	AWSCredentials credentials;
	AmazonS3Client s3;
	String bucketName;
	RDSManager rdsManager;

	public AmazonS3Manager() {
		init();
	}

	private void init() {
		try {
			credentials = new PropertiesCredentials(
					AmazonS3Manager.class.getResourceAsStream("../AwsCredentials.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s3 = new AmazonS3Client(credentials);
		bucketName = "cloud-ly2278youtube-bucket";
		rdsManager = new RDSManager();
	}

	public ArrayList<Video> readAllFiles() {
		ArrayList<Video> videos = new ArrayList<Video>();
		String query = "SELECT * FROM videos ORDER BY rate DESC;";
		ResultSet result = rdsManager.ExecuteResultQuery(query);
		try {
			while (result.next()) {
				String name = result.getObject(1).toString();
				double rate = result.getDouble(2);
				String timestamp = result.getObject(4).toString();
				videos.add(new Video(name, timestamp, rate));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return videos;
	}
}
