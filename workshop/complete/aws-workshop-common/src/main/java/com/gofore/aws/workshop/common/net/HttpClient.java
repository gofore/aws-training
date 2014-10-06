package com.gofore.aws.workshop.common.net;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gofore.aws.workshop.common.async.ShutdownHelper;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

public class HttpClient {

    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36";

    private final ExecutorService executor;

    public HttpClient() {
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ShutdownHelper.addShutdownHook(() -> this.executor);
    }
    
    public CompletableFuture<Response> get(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return Request.Get(url).addHeader("User-Agent", USER_AGENT).execute();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    
}
