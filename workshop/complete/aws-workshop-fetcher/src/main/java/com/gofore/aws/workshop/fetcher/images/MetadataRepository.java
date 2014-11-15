package com.gofore.aws.workshop.fetcher.images;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.gofore.aws.workshop.common.functional.Consumers;
import com.gofore.aws.workshop.common.simpledb.DomainLookup;
import com.gofore.aws.workshop.common.simpledb.SimpleDBClient;
import com.gofore.aws.workshop.fetcher.utils.TermsParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class MetadataRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataRepository.class);
    
    private final SimpleDBClient simpleDBClient;
    private final TermsParser termsParser;
    private final String domain;

    @Inject
    public MetadataRepository(DomainLookup domainLookup, SimpleDBClient simpleDBClient, TermsParser termsParser) {
        this.simpleDBClient = simpleDBClient;
        this.termsParser = termsParser;
        this.domain = domainLookup.getDomain("images");
    }

    public CompletableFuture<Image> save(String id, Image image) {
        List<ReplaceableAttribute> attributes = createAttributes(image);
        PutAttributesRequest request = new PutAttributesRequest(domain, id, attributes);
        return simpleDBClient.putAttributes(request).whenComplete(Consumers.consumer(
                (v) -> LOGGER.info("Successfully saved image {} metadata to {}", id, domain),
                (e) -> LOGGER.error("Failed to save image {} metadata to {}", id, domain, e)
        )).thenApply(v -> image);
    }
    
    private List<ReplaceableAttribute> createAttributes(Image image) {
        Stream<ReplaceableAttribute> meta = Arrays.asList(
                new ReplaceableAttribute("thumbnailUrl", image.getThumbnailUrl(), true),
                new ReplaceableAttribute("imageUrl", image.getImageUrl(), true),
                new ReplaceableAttribute("description", image.getDescription(), true)
        ).stream();
        Stream<ReplaceableAttribute> terms = termsParser
                .parse(image.getDescription())
                .map(v -> new ReplaceableAttribute("term", v, true));
        return Stream.concat(meta, terms).collect(Collectors.toList());
    }
}
