package com.gofore.aws.workshop.common.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Threads {

    public static final long ERROR_WAIT_MILLIS = 5000;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Threads.class);
    
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            LOGGER.debug("Sleeping thread was interrupted", ex);
        }
    }
    
    private Threads() {
        
    }
}
