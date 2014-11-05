package com.gofore.aws.workshop.common.it;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import com.gofore.aws.workshop.common.properties.AwsCredentialsCsvLoader;
import com.gofore.aws.workshop.common.properties.PropertyLoader;
import org.junit.Test;

public class VerifyAwsCredentialsIT {
    
    @Test
    public void testAwsCredentialsExist() throws Exception{
        try {
            PropertyLoader credentialsLoader = new AwsCredentialsCsvLoader("aws-workshop-credentials.csv");
            credentialsLoader.lookup("aws.user");
            credentialsLoader.lookup("aws.access.key");
            credentialsLoader.lookup("aws.secret.key");
        } catch (NoSuchElementException ex) {
            fail(ex.getMessage());
        }
    }
    
}
