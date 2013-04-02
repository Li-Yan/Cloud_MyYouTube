package cloud;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.cloudfront.AmazonCloudFrontAsyncClient;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClient;
import com.amazonaws.services.cloudfront.model.CreateStreamingDistributionRequest;
import com.amazonaws.services.cloudfront.model.ListStreamingDistributionsRequest;
import com.amazonaws.services.cloudfront.model.ListStreamingDistributionsResult;
import com.amazonaws.services.cloudfront.model.PriceClass;
import com.amazonaws.services.cloudfront.model.S3Origin;
import com.amazonaws.services.cloudfront.model.StreamingDistributionConfig;
import com.amazonaws.services.cloudfront.model.StreamingDistributionList;
import com.amazonaws.services.cloudfront.model.StreamingDistributionSummary;

public class WatchVideo extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2705045404300986010L;
	private static final String streamingDistribution_OriginDomain = 
			"cloud-ly2278youtube-bucket.s3.amazonaws.com";
	
	AWSCredentials credentials;
	AmazonCloudFrontClient cloudFront;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			credentials = new PropertiesCredentials(
					WatchVideo.class.getResourceAsStream("../AwsCredentials.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean streamingDistributionExist = false;
		
		cloudFront = new AmazonCloudFrontClient(credentials);
		ListStreamingDistributionsResult streamingDistributionsResult = cloudFront.listStreamingDistributions(
				new ListStreamingDistributionsRequest());
		StreamingDistributionList streamingDistributionList = streamingDistributionsResult.getStreamingDistributionList();
		List<StreamingDistributionSummary> list= streamingDistributionList.getItems();
		for (StreamingDistributionSummary streamingDistributionSummary : list) {
			if (streamingDistribution_OriginDomain.equals(
					streamingDistributionSummary.getS3Origin().getDomainName())) {
				streamingDistributionExist = true;
				break;
			}
		}
		
		if (!streamingDistributionExist) {
			//Create stream distribution
			AmazonCloudFrontAsyncClient cloudFrontAsyncClient = new AmazonCloudFrontAsyncClient(credentials);
			S3Origin s3Origin = new S3Origin(streamingDistribution_OriginDomain);
			StreamingDistributionConfig config = new StreamingDistributionConfig("", s3Origin, true);
			config.setPriceClass(PriceClass.PriceClass_100);
			cloudFrontAsyncClient.createStreamingDistributionAsync(new CreateStreamingDistributionRequest(config));
		}
		
		String filename = request.getParameter("videoList");
		if (filename == null) {
			response.sendRedirect("../list.jsp");
			return;
		}
		
		Video.selectedVideo = filename;
		
		response.sendRedirect("../videoplayer.jsp");
	}
}
