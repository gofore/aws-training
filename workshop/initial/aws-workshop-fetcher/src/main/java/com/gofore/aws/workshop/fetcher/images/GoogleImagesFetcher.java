package com.gofore.aws.workshop.fetcher.images;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.gofore.aws.workshop.common.net.HtmlClient;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.nodes.Element;

@Singleton
public class GoogleImagesFetcher implements ImageFetcher {

    private final HtmlClient htmlClient;

    @Inject
    public GoogleImagesFetcher(HtmlClient htmlClient) {
        this.htmlClient = htmlClient;
    }

    @Override
    public CompletableFuture<Stream<Image>> fetchImages(String url) {
        return htmlClient.load(url)
                .thenApply(doc -> doc.select(".images_table td").stream())
                .thenApply(elements -> elements.map(this::toImage));
    }
    
    private Image toImage(Element element) {
        Element link = element.select("a").first();
        Element img = element.select("img").first();
        String thumbnailUrl = img.attr("abs:src");
        String imageUrl = createUriBuilder(link.attr("abs:href")).getQueryParams().stream()
                .filter(pair -> "q".equals(pair.getName()))
                .findFirst().map(NameValuePair::getValue).get();
        String description = element.text();
        return new Image(thumbnailUrl, imageUrl, description);
    }
    
    private URIBuilder createUriBuilder(String url) {
        try {
            return new URIBuilder(url);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
