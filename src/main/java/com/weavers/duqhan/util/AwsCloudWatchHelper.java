/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.util;

/**
 *
 * @author weaversAndroid
 */
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
 *
 * @author mamidilaxman
 *
 */
public class AwsCloudWatchHelper {

    private static final String awsAccessKey = "AKIAJQSBZKD5ATWEMYZQ";
    private static final String awsSecretKey = "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";
    private static AWSCredentialsProvider awsCredentialProvider;
    private static final String awsRegion = "us-east-1";
    private static AmazonCloudWatch cw;

    private static final String COUNT_NAMESPACES = "DUQHAN SITE/TRAFFIC";
    private static final String TIME_NAMESPACES = "DUQHAN API/OVERHEAD";

    public AwsCloudWatchHelper() {
        BasicAWSCredentials bawsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        awsCredentialProvider = new AWSStaticCredentialsProvider(bawsCredentials);
        cw = AmazonCloudWatchClientBuilder
                .standard()
                .withCredentials(awsCredentialProvider)
                .withRegion(awsRegion)
                .build();
    }

    /**
     * This function helps to log a count value to AWS. It is typically an
     * integer to count the number of events that you want to log.
     *
     * @param dimensionName
     * @param dimensionValue
     * @param metricName
     * @param countValue
     * @return
     */
    public int logCount(String dimensionName, String dimensionValue, String metricName) {
        Dimension dimension = new Dimension().withName(dimensionName).withValue(dimensionValue);
        MetricDatum datum = new MetricDatum().withMetricName(metricName).withUnit(StandardUnit.Count).withValue(1.0).withDimensions(dimension);
        PutMetricDataRequest request = new PutMetricDataRequest().withNamespace(COUNT_NAMESPACES).withMetricData(datum);
        PutMetricDataResult metricResponse = cw.putMetricData(request);
        return metricResponse.getSdkHttpMetadata().getHttpStatusCode();
    }

    /**
     * This function helps to log a timestamp to AWS. It is typically a long
     * value in seconds.
     *
     * @param dimensionName
     * @param dimensionValue
     * @param metricName
     * @param timeToLog
     * @return
     */
    public int logTimeSecounds(String dimensionName, String dimensionValue, String metricName, Double timeToLog) {
        Dimension timeDimension = new Dimension().withName(dimensionName).withValue(dimensionValue);
        MetricDatum timeDatum = new MetricDatum().withMetricName(metricName).withUnit(StandardUnit.Seconds).withValue(timeToLog).withDimensions(timeDimension);
        PutMetricDataRequest timeRequest = new PutMetricDataRequest().withNamespace(TIME_NAMESPACES).withMetricData(timeDatum);
        PutMetricDataResult loginMetricResponse = cw.putMetricData(timeRequest);
        return loginMetricResponse.getSdkHttpMetadata().getHttpStatusCode();
    }

    public static void main(String[] args) {
        AwsCloudWatchHelper acwh = new AwsCloudWatchHelper();
        long loginStartTime = System.currentTimeMillis();
//        System.out.println("rrrrrrrrrrrr" + acwh.logCount("Login dimension", "Login dimension value", "Number of users using login(Duplicate users too)", StandardUnit.Count, 1.0, "DUQHAN SITE/TRAFFIC"));
//        System.out.println("rrrrrrrrrrrr" + acwh.logCount("Login dimension", "Login dimension value1", "Number of users using login(Duplicate users too)", StandardUnit.Count, 1.0, "DUQHAN SITE/TRAFFIC"));
        System.out.println("rrrrrrrrrrrr" + acwh.logCount("Login", "Login count", "Login API hit counter"));
        long loginEndTime = System.currentTimeMillis();
        System.out.println("rrrrrrrrrrrr" + acwh.logTimeSecounds("Login", "Login response", "Login API response time", (loginEndTime - loginStartTime) / 1000.0));
    }
}
