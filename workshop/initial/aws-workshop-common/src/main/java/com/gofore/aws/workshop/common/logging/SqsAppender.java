package com.gofore.aws.workshop.common.logging;

import java.io.Closeable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SqsAppender extends UnsynchronizedAppenderBase<ILoggingEvent> implements Closeable {
    
    private static final ObjectMapper MAPPER          = new ObjectMapper();
    private static final String       ENDPOINT        = "sqs.{region}.amazonaws.com";
    private static final String       DEFAULT_REGION  = "us-east-1";
    private static final int          DEFAULT_THREADS = 1;

    private final ReentrantLock lock = new ReentrantLock(true);

    protected String accessKey;
    protected String secretKey;
    protected String region;
    protected String queueUrl;
    protected Integer threads;

    private AmazonSQSAsyncClient sqs;


    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setRegion(String region) {
        this.region = region;
    }
    
    public void setQueueUrl(String queueUrl) {
        this.queueUrl = queueUrl;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    @Override
    public void start() {
        lock.lock();
        
        if (queueUrl == null) {
            addError("Logging queue not set for appender '" + name + "'");
            return;
        }
        
        try {
            close();
            this.sqs = initSqs();
            super.start();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void stop() {
        lock.lock();
        try {
            close();
            super.stop();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() {
        lock.lock();
        try {
            if (sqs != null) {
                shutdownExecutor(sqs.getExecutorService());
                sqs.shutdown();
                sqs = null;
            }
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    protected void append(ILoggingEvent event) {
        if (sqs == null) {
            addWarn("Attempted to append with appender '" + name + "' that has uninitialized SQS client");
            return;
        }
        try {
            String message = createLoggingMessage(event);
            SendMessageRequest request = new SendMessageRequest(queueUrl, message);
            sqs.sendMessageAsync(request, new AsyncHandler<SendMessageRequest, SendMessageResult>() {
                @Override
                public void onError(Exception exception) {
                    addError("Appender '" + name + "' failed to send logging event to SQS", exception);
                }
                @Override
                public void onSuccess(SendMessageRequest request, SendMessageResult result) {
                    // noop
                }
            });
        } catch (Exception ex) {
            addError("Appender '" + name + "' failed to append logging event", ex);
        }
    }

    protected AmazonSQSAsyncClient initSqs() {
        AWSCredentialsProvider credentials = new AWSCredentialsProviderChain(
                new EnvironmentVariableCredentialsProvider(),
                new SystemPropertiesCredentialsProvider(),
                new StaticCredentialsProvider(new AppenderCredentials()),
                new ProfileCredentialsProvider(),
                new InstanceProfileCredentialsProvider()
        );
        AmazonSQSAsyncClient sqs = new AmazonSQSAsyncClient(credentials, Executors.newFixedThreadPool(getThreads()));
        sqs.setEndpoint(getEndpoint());
        return sqs;
    }

    protected String createLoggingMessage(ILoggingEvent event) throws JsonProcessingException {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("timestamp", event.getTimeStamp());
        node.put("level", event.getLevel().toString());
        node.put("logger", event.getLoggerName());
        node.put("thread", event.getThreadName());
        node.put("message", event.getFormattedMessage());
        return MAPPER.writeValueAsString(node);
    }
    
    private int getThreads() {
        if (threads != null) {
            return threads;
        } else {
            return DEFAULT_THREADS;
        }
    }

    private String getRegion() {
        if (region != null) {
            return region;
        } else {
            return DEFAULT_REGION;
        }
    }

    private String getEndpoint() {
        return ENDPOINT.replace("{region}", getRegion());
    }

    private void shutdownExecutor(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            addWarn("SQS executor shutdown interrupted for appender '" + name + "'", ex);
        }
    }
    
    class AppenderCredentials implements AWSCredentials {
        @Override
        public String getAWSAccessKeyId() {
            return accessKey;
        }

        @Override
        public String getAWSSecretKey() {
            return secretKey;
        }
    }
}
