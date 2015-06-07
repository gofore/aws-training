package com.gofore.aws.workshop.common.di;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.cloudformation.AmazonCloudFormationAsync;
import com.amazonaws.services.cloudformation.AmazonCloudFormationAsyncClient;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementAsync;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementAsyncClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpledb.AmazonSimpleDBAsync;
import com.amazonaws.services.simpledb.AmazonSimpleDBAsyncClient;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.gofore.aws.workshop.common.async.ShutdownHelper;
import com.gofore.aws.workshop.common.cloudformation.CloudFormationClient;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.properties.PropertyLoader;
import com.gofore.aws.workshop.common.s3.S3Client;
import com.gofore.aws.workshop.common.simpledb.SimpleDBClient;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AwsModule implements Module {

    @Override
    public void configure(Binder binder) {
        
    }
    
    @Provides
    public ExecutorService executor() {
        return Executors.newFixedThreadPool(15);
    }

    /**
     * Create a credentials provider chain. Providers in the chain are asked
     * for credentials in the same order they are defined.
     * 
     * @param properties application properties
     * @return credentials provider that is backed by multiple providers
     */
    @Provides
    @Singleton
    public AWSCredentialsProvider credentialsProvider(ApplicationProperties properties) {
        return new AWSCredentialsProviderChain(
                new StaticCredentialsProvider(new PropertyLoaderCredentials(properties)),
                new ProfileCredentialsProvider(),
                new InstanceProfileCredentialsProvider()
        );
    }
    
    @Provides
    @Singleton
    public AmazonSQSAsync sqs(ApplicationProperties properties,
                              AWSCredentialsProvider credentials,
                              ExecutorService executor) {
        AmazonSQSAsyncClient sqs =  new AmazonSQSAsyncClient(credentials, executor);
        sqs.setEndpoint(properties.lookup("aws.sqs.endpoint"));
        ShutdownHelper.addShutdownHook(sqs::getExecutorService, sqs::shutdown);
        return sqs;
    }
    
    @Provides
    @Singleton
    public SqsClient sqsClient(AmazonSQSAsync sqs) {
        return new SqsClient(sqs);
    }
    
    @Provides
    @Singleton
    public AmazonS3 s3(ApplicationProperties properties,
                       AWSCredentialsProvider credentials) {
        AmazonS3Client s3 = new AmazonS3Client(credentials);
        s3.setEndpoint(properties.lookup("aws.s3.endpoint"));
        return s3;
    }
    
    @Provides
    @Singleton
    public S3Client s3Client(AmazonS3 s3,
                             ExecutorService executor) {
        return new S3Client(s3, executor);
    }
    
    @Provides
    @Singleton
    public AmazonSimpleDBAsync simpleDB(ApplicationProperties properties,
                                        AWSCredentialsProvider credentials,
                                        ExecutorService executor) {
        AmazonSimpleDBAsyncClient simpleDB = new AmazonSimpleDBAsyncClient(credentials, executor);
        simpleDB.setEndpoint(properties.lookup("aws.simpledb.endpoint"));
        ShutdownHelper.addShutdownHook(simpleDB::getExecutorService, simpleDB::shutdown);
        return simpleDB;
    }
    
    @Provides
    @Singleton
    public SimpleDBClient simpleDBClient(AmazonSimpleDBAsync simpleDB) {
        return new SimpleDBClient(simpleDB);
    }
    
    @Provides
    @Singleton
    public AmazonIdentityManagementAsync iam(AWSCredentialsProvider credentials,
                                             ExecutorService executor) {
        AmazonIdentityManagementAsyncClient iam = new AmazonIdentityManagementAsyncClient(credentials, executor);
        ShutdownHelper.addShutdownHook(iam::getExecutorService, iam::shutdown);
        return iam;
    }

    @Provides
    @Singleton
    public AmazonCloudFormationAsync cloudFormation(ApplicationProperties properties,
                                                    AWSCredentialsProvider credentials,
                                                    ExecutorService executor) {
        AmazonCloudFormationAsyncClient cloudFormation = new AmazonCloudFormationAsyncClient(credentials, executor);
        cloudFormation.setEndpoint(properties.lookup("aws.cloudformation.endpoint"));
        ShutdownHelper.addShutdownHook(cloudFormation::getExecutorService, cloudFormation::shutdown);
        return cloudFormation;
    }
    
    @Provides
    @Singleton
    public CloudFormationClient cloudFormationClient(AmazonCloudFormationAsync cloudFormation,
                                                     ExecutorService executor) {
        return new CloudFormationClient(cloudFormation, executor);
    }
    
    /**
     * Credentials that are bound to a PropertyLoader.
     */
    private static class PropertyLoaderCredentials implements AWSCredentials {
        
        private final PropertyLoader loader;

        private PropertyLoaderCredentials(PropertyLoader loader) {
            this.loader = loader;
        }

        @Override
        public String getAWSAccessKeyId() {
            return loader.lookupOptional("aws.access.key").orElse(null);
        }

        @Override
        public String getAWSSecretKey() {
            return loader.lookupOptional("aws.secret.key").orElse(null);
        }
    }
}
