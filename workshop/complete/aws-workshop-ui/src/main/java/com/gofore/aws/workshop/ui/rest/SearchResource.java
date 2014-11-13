package com.gofore.aws.workshop.ui.rest;

import com.amazonaws.services.simpledb.model.SelectRequest;
import com.gofore.aws.workshop.common.functional.Lists;
import com.gofore.aws.workshop.common.rest.RestServerResource;
import com.gofore.aws.workshop.common.simpledb.DomainLookup;
import com.gofore.aws.workshop.common.simpledb.SimpleDBClient;
import com.gofore.aws.workshop.ui.search.SearchItemMapper;
import com.gofore.aws.workshop.ui.search.SearchResult;
import com.google.inject.Inject;
import org.restlet.resource.Get;

public class SearchResource extends RestServerResource {
    
    /** SimpleDB select clause template. {domain} will be replaced with the actual domain name. */
    private static final String SELECT_TEMPLATE = "select * from `{domain}`";
    
    /** SimpleDB where condition for 'term' attribute. {term} will be replaced with the actual query. */
    private static final String TERM_WHERE_TEMPLATE = "where term like '{term}%'";
    
    private final SimpleDBClient simpleDBClient;
    private final String select;
    
    @Inject
    public SearchResource(DomainLookup domainLookup, SimpleDBClient simpleDBClient) {
        this.simpleDBClient = simpleDBClient;
        this.select = SELECT_TEMPLATE.replace("{domain}", domainLookup.getImagesDomain());
    }

    @Get("json")
    public SearchResult search() {
        String query = select() + limit();
        SelectRequest request = new SelectRequest(query);
        getQueryValueAsString("n").ifPresent(request::setNextToken);
        return simpleDBClient.select(request)
                .thenApply(r -> new SearchResult(
                        Lists.map(r.getItems(), new SearchItemMapper()),
                        r.getNextToken()
                )).join();
    }
    
    private String select() {
        return getQueryValueAsString("q")
                .map(q -> q.replaceAll("['\"`%]+", "").toLowerCase())
                .map(q -> select + " " + TERM_WHERE_TEMPLATE.replace("{term}", q))
                .orElse(select);
    }
    
    private String limit() {
        return getQueryValueAsInteger("l")
                .map(l -> " limit " + l)
                .orElse("");
    }
}
