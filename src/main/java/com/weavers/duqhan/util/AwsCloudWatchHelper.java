package com.weavers.duqhan.util;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.PutMetricDataResult;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

/**
 * A helper class to log various metrics to AWS.
 * @author mamidilaxman
 *
 */
public class AwsCloudWatchHelper {

	private static final String awsAccessKey = "AKIAI7PN2XJSGU66DUNA";
	private static final String awsSecretKey = "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";
	private static AWSCredentialsProvider awsCredentialProvider;
	private static final String awsRegion = "us-east-1";
	private static AmazonCloudWatch cw;
	
	public AwsCloudWatchHelper() {
		BasicAWSCredentials bawsCredentials =	new BasicAWSCredentials(awsAccessKey, awsSecretKey);
		awsCredentialProvider = new AWSStaticCredentialsProvider(bawsCredentials);
		cw = AmazonCloudWatchClientBuilder
    			.standard()
    			.withCredentials(awsCredentialProvider)
    			.withRegion(awsRegion)
    			.build();
	}
	
	/**
	 * This function helps to log a count value to AWS. It is typically an integer to count the number of events that you want to log.
	 * @param dimensionName
	 * @param dimensionValue
	 * @param metricName
	 * @param metricUnit
	 * @param countValue
	 * @param metricNameSpace
	 * @return
	 */
	public int logCount(String dimensionName, String dimensionValue, String metricName, StandardUnit metricUnit, Double countValue,
									String metricNameSpace) {
		
    	Dimension dimension = new Dimension()
    							  .withName(dimensionName)
    							  .withValue(dimensionValue);
    	MetricDatum datum = new MetricDatum()
    							  .withMetricName(metricName)
    							  .withUnit(StandardUnit.Count)
    							  .withValue(countValue)
    							  .withDimensions(dimension);
    	
    	PutMetricDataRequest request = new PutMetricDataRequest()
    						      			.withNamespace(metricNameSpace)
    						      			.withMetricData(datum);
    	PutMetricDataResult metricResponse = cw.putMetricData(request);
    	return metricResponse.getSdkHttpMetadata().getHttpStatusCode();
    	
	}
	
	/**
	 * This function helps to log a timestamp to AWS. It is typically a long value in seconds.
	 * @param dimensionName
	 * @param dimensionValue
	 * @param metricName
	 * @param metricUnit
	 * @param timeToLog
	 * @param metricNameSpace
	 * @return
	 */
	public int logTimeSecounds(String dimensionName, String dimensionValue, String metricName, StandardUnit metricUnit, Double timeToLog,
										String metricNameSpace) {
	        
	        Dimension timeDimension = new Dimension()
	        									.withName(dimensionName)
	        									.withValue(dimensionValue);
	        MetricDatum timeDatum = new MetricDatum()
	        						.withMetricName(metricName)
	        						.withUnit(metricUnit)
	        						.withValue(timeToLog)
	        						.withDimensions(timeDimension);
	        
	        PutMetricDataRequest timeRequest = new PutMetricDataRequest()
	      			.withNamespace(metricNameSpace)
	      			.withMetricData(timeDatum);
	        PutMetricDataResult loginMetricResponse = cw.putMetricData(timeRequest);
	        return loginMetricResponse.getSdkHttpMetadata().getHttpStatusCode();
		
	}
	
	
	
}
