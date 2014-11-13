package com.gofore.aws.workshop.ui.rest;

import java.util.stream.Collectors;

import com.amazonaws.services.simpledb.model.SelectRequest;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.rest.RestServerResource;
import com.gofore.aws.workshop.common.simpledb.DomainLookup;
import com.gofore.aws.workshop.common.simpledb.SimpleDBClient;
import com.gofore.aws.workshop.ui.search.SearchItemMapper;
import com.gofore.aws.workshop.ui.search.SearchResult;
import com.google.inject.Inject;
import org.restlet.resource.Get;

public class SearchResource extends RestServerResource {
    
    private static final String SELECT_TEMPLATE = "select * from `{images.domain}`";
    
    private final SimpleDBClient simpleDBClient;
    private final String select;
    
    @Inject
    public SearchResource(ApplicationProperties properties, DomainLookup domainLookup, SimpleDBClient simpleDBClient) {
        this.simpleDBClient = simpleDBClient;
        this.select = SELECT_TEMPLATE.replace("{images.domain}", domainLookup.getImagesDomain());
    }

    @Get("json")
    public SearchResult search() {
        String query = getQueryValueAsString("q")
                .map(q -> q.replaceAll("['\"`%]+", "").toLowerCase())
                .map(q -> select + " where term like '" + q + "%'")
                .orElse(select);
        query = query + getQueryValueAsInteger("l")
                .map(l -> " limit " + l)
                .orElse("");
        SelectRequest request = new SelectRequest(query);
        getQueryValueAsString("n").ifPresent(request::setNextToken);
        return simpleDBClient.select(request)
                .thenApply(r -> new SearchResult(
                        r.getItems().stream().map(new SearchItemMapper()).collect(Collectors.toList()),
                        r.getNextToken()
                )).join();
    }
}
