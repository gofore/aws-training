package com.gofore.aws.workshop.common.net;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import com.gofore.aws.workshop.common.async.ShutdownHelper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

@Singleton
public class HttpClient {

    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36";

    private final ExecutorService executor;

    @Inject
    public HttpClient(ExecutorService executor) {
        ShutdownHelper.addShutdownHook(() -> executor);
        this.executor = executor;
    }
    
    public CompletableFuture<Response> get(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return Request.Get(url).addHeader("User-Agent", USER_AGENT).execute();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }, executor);
    }
    
}
