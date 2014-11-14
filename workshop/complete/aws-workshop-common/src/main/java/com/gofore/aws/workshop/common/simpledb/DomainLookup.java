package com.gofore.aws.workshop.common.simpledb;

import java.util.Optional;

import com.amazonaws.services.simpledb.model.ListDomainsRequest;
import com.amazonaws.services.simpledb.model.ListDomainsResult;
import com.gofore.aws.workshop.common.functional.Lists;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DomainLookup {
    
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
                .join();
    }
    
    private String getDomainPrefix(String user, String name) {
        return "aws-workshop-" + user + "-" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
