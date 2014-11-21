package com.gofore.aws.workshop.common.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.AppenderAttachable;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.properties.CloudFormationOutputsPropertyLoader;
import org.slf4j.LoggerFactory;

public class SqsLoggingContext {
    
    public static void create(ApplicationProperties applicationProperties, CloudFormationOutputsPropertyLoader cloudFormationProperties) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        AppenderAttachable<ILoggingEvent> logger = lc.getLogger("com.gofore");
        
        SqsAppender sqsAppender = new SqsAppender();
        sqsAppender.setName("sqs");
        sqsAppender.setContext(lc);
        sqsAppender.setAccessKey(applicationProperties.lookupOptional("aws.access.key").orElse(null));
        sqsAppender.setSecretKey(applicationProperties.lookupOptional("aws.secret.key").orElse(null));
        sqsAppender.setRegion(applicationProperties.lookup("aws.region"));
        sqsAppender.setQueueUrl(cloudFormationProperties.lookup("QueueLogsUrl"));
        sqsAppender.start();
        
        logger.addAppender(sqsAppender);
    }
    
}
