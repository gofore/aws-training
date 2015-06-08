package com.gofore.aws.workshop.ui.util;

import com.gofore.aws.workshop.common.async.Threads;
import com.google.inject.Singleton;

@Singleton
public class Overloader {

    private volatile boolean overloading = false;

    public Overloader() {
        Thread thread = new Thread(this::operate, "overloader");
        thread.setDaemon(true);
        thread.start();
    }

    public boolean isOverloading() {
        return overloading;
    }

    public boolean toggleOverload() {
        overloading = !overloading;
        return overloading;
    }

    private void operate() {
        while (true) {
            if (overloading) {
                for (double d = 0.0; d < Math.PI; d += 0.01) {
                    Math.tan(d);
                }
            } else {
                Threads.sleep(1000);
            }
        }
    }
}
