package com.gofore.aws.workshop.common.net;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gofore.aws.workshop.common.async.ShutdownHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlClient {

    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36";
    
    private final ExecutorService executor;
    
    public HtmlClient() {
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ShutdownHelper.addShutdownHook(() -> this.executor);
    }
    
    public CompletableFuture<Document> load(String url) {
        return CompletableFuture.supplyAsync(() -> getDocument(url), executor);
    }
    
    private Document getDocument(String url) {
        try {
            return Jsoup.connect(url).userAgent(USER_AGENT).get();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
