package com.gofore.aws.workshop.common.net;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import com.gofore.aws.workshop.common.async.ShutdownHelper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Singleton
public class HtmlClient {

    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36";
    
    private final ExecutorService executor;
    
    @Inject
    public HtmlClient(ExecutorService executor) {
        ShutdownHelper.addShutdownHook(() -> executor);
        this.executor = executor;
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
