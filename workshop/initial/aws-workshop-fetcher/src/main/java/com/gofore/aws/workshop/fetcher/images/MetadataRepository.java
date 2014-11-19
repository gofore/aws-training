package com.gofore.aws.workshop.fetcher.images;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.gofore.aws.workshop.common.properties.CloudFormationOutputsPropertyLoader;
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
    public MetadataRepository(CloudFormationOutputsPropertyLoader properties, SimpleDBClient simpleDBClient, TermsParser termsParser) {
        this.simpleDBClient = simpleDBClient;
        this.termsParser = termsParser;
        this.domain = properties.lookup("ImageMetadataDomainName");
    }

    public CompletableFuture<Void> save(String id, Image image) {
        List<ReplaceableAttribute> attributes = createAttributes(image);
        // TODO: Task 4: Put attributes to SimpleDB
        /**
         * @see com.amazonaws.services.simpledb.AmazonSimpleDB#putAttributes(com.amazonaws.services.simpledb.model.PutAttributesRequest)
         */
        throw new UnsupportedOperationException("Task 4 not implemented");
    }
    
    private List<ReplaceableAttribute> createAttributes(Image image) {
        // TODO: Task 4: Put attributes to SimpleDB
        /**
         * Create attributes for 'thumbnailUrl', 'imageUrl' and 'description'.
         * Add multiple 'term' attributes for an array of words parsed with
         * TermsParser.
         */
        throw new UnsupportedOperationException("Task 4 not implemented");
    }
}
