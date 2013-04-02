package cloud;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class UploadServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5599995034345236029L;
	static AmazonS3Client s3;

	public static final String[] supportedFormat = {".flv", ".mp4", ".m4v", ".avi"};
	public static boolean duplicate = false;
	public static boolean uploadsucceed = false;
	public static boolean emptyfile = false;
	public static boolean wrongformat = false;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		AWSCredentials credentials = new PropertiesCredentials(
				UploadServlet.class.getResourceAsStream("../AwsCredentials.properties"));
		
		emptyfile = true;
		duplicate = true;
		uploadsucceed = false;
		boolean bucketExist = false;
		
		boolean isMulti = ServletFileUpload.isMultipartContent(request);
		if (isMulti) {
			
			ServletFileUpload upload = new ServletFileUpload();
			RDSManager rdsManager = new RDSManager();

			try {
				FileItemIterator iter = upload.getItemIterator(request);
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					InputStream inputStream = item.openStream();
					if (item.isFormField()) {

					} else {
						String fileName = item.getName();
						if (fileName != null && fileName.length() > 0) {
							emptyfile = false;
							
							//Check uplaod video type
							wrongformat = true;
							String videoFormat = fileName.substring(fileName.length() - 4);
							for (String format : supportedFormat) {
								if (videoFormat.equalsIgnoreCase(format)) {
									wrongformat = false;
									break;
								}
							}
							if (wrongformat) {
								emptyfile = false;
								duplicate = false;
								uploadsucceed = false;
								response.sendRedirect("../upload.jsp");
								return;
							}
							
							String query = "SELECT * FROM videos WHERE name='" + fileName +"';";
							if (rdsManager.CheckExistQuery(query)) {
								continue;
							}
							
							duplicate = false;
							
							s3 = new AmazonS3Client(credentials);
							String bucketName = "cloud-ly2278youtube-bucket";
							List<Bucket> list = s3.listBuckets();
							for (Bucket bucket : list) {
								if (bucketName.equals(bucket.getName())) {
									bucketExist = true;
								}
							}
							
							if (!bucketExist) {
								s3.createBucket(bucketName);
							}
							
							File file = File.createTempFile(fileName, "");
							file.deleteOnExit();
							FileOutputStream outStream = new FileOutputStream(file);
							byte buffer[] = new byte[1024];
							int len = 0;
							while ((len = inputStream.read(buffer)) > 0) {
								outStream.write(buffer, 0, len);
							}
							PutObjectRequest putRequest = new PutObjectRequest(bucketName, fileName, file);
							putRequest.setCannedAcl(CannedAccessControlList.PublicRead);
							s3.putObject(putRequest);
							
							uploadsucceed = true;
							
							outStream.close();
							s3.shutdown();
							
							Calendar calendar = Calendar.getInstance();
							String timestamp = calendar.getTime().toString();
							rdsManager.ExecuteNonResultQuery("INSERT INTO videos (name, rate, " + 
							 "numratings, timestamp) VALUES ('" + fileName + "', 0, 0, '" + timestamp + "');");
						}
					}
					inputStream.close();
				}
			} catch (IOException e) {
			} catch (FileUploadException e) {
			}
		}
		
		if (!emptyfile) {
			if (!duplicate) {
				response.sendRedirect("../upload.jsp");
				return;
			}
			else {
				response.sendRedirect("../upload.jsp");
				return;
			}
		}
		else {
			duplicate = false;
			uploadsucceed = false;
			response.sendRedirect("../upload.jsp");
		}
	}
}
