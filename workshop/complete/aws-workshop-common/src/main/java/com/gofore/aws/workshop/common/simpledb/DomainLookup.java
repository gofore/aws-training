package com.gofore.aws.workshop.common.simpledb;

import java.util.Optional;

import com.amazonaws.services.simpledb.model.ListDomainsRequest;
import com.amazonaws.services.simpledb.model.ListDomainsResult;
import com.gofore.aws.workshop.common.functional.Consumers;
import com.gofore.aws.workshop.common.functional.Lists;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DomainLookup {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainLookup.class);
    
    private final SimpleDBClient simpleDBClient;
    private final String user;

    @Inject
    public DomainLookup(ApplicationProperties properties, SimpleDBClient simpleDBClient) {
        this.simpleDBClient = simpleDBClient;
        this.user = properties.lookup("aws.user");
    }

    public String getDomain(String name) {
        String prefix = getDomainPrefix(user, name);
        return simpleDBClient.listDomains(new ListDomainsRequest())
                .thenApply(ListDomainsResult::getDomainNames)
                .thenApply(Lists.findFirst(d -> d.startsWith(prefix)))
                .thenApply(Optional::get)
                .whenComplete(Consumers.onError(e -> LOGGER.error("Failed to load domain for {}", prefix, e)))
                .join();
    }
    
    private String getDomainPrefix(String user, String name) {
        return "aws-workshop-" + user + "-" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
