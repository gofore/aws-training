package com.gofore.aws.workshop.common.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ShutdownHelper {
    
    private static final int TERMINATION_AWAIT_SECONDS = 10;

    public static void addShutdownHook(Supplier<ExecutorService> supplier) {
        addShutdownHook(supplier, () -> {});
    }
    
    public static void addShutdownHook(Supplier<ExecutorService> supplier, Runnable shutdown) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ExecutorService executorService = supplier.get();
                executorService.shutdown();
                executorService.awaitTermination(TERMINATION_AWAIT_SECONDS, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                //LOGGER.error("Failed to stop SQS client in time, some jobs might be lost", ex);
            }
            shutdown.run();
        }));
    }
}
