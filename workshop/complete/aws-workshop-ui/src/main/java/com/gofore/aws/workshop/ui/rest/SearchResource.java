package com.gofore.aws.workshop.ui.rest;

import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import com.gofore.aws.workshop.common.functional.Lists;
import com.gofore.aws.workshop.common.rest.RestServerResource;
import com.gofore.aws.workshop.common.simpledb.DomainLookup;
import com.gofore.aws.workshop.common.simpledb.SimpleDBClient;
import com.gofore.aws.workshop.ui.search.SearchItemMapper;
import com.gofore.aws.workshop.ui.search.SearchResult;
import com.google.inject.Inject;
import org.restlet.resource.Get;

public class SearchResource extends RestServerResource {

    // TODO: Task 5: SimpleDB query
    
    /**
     * See https://aws.amazon.com/articles/Amazon-SimpleDB/1231 for SimpleDB query reference
     */
    
    /** SimpleDB select clause template. {domain} will be replaced with the actual domain name. */
    private static final String SELECT_TEMPLATE = "select * from `{domain}`";
    
    /** SimpleDB where condition for 'term' attribute. {term} will be replaced with the actual query. */
    private static final String TERM_WHERE_TEMPLATE = "where term like '{term}%'";
    
    private final SimpleDBClient simpleDBClient;
    private final String select;
    
    @Inject
    public SearchResource(DomainLookup domainLookup, SimpleDBClient simpleDBClient) {
        this.simpleDBClient = simpleDBClient;
        this.select = SELECT_TEMPLATE.replace("{domain}", domainLookup.getDomain("images"));
    }

    @Get("json")
    public SearchResult search() {
        String query = select() + limit();
        SelectRequest request = new SelectRequest(query);
        getQueryValueAsString("n").ifPresent(request::setNextToken);
        return simpleDBClient.select(request).thenApply(this::convert).join();
    }
    
    private SearchResult convert(SelectResult result) {
        return new SearchResult(
                Lists.map(result.getItems(), new SearchItemMapper()),
                result.getNextToken()
        );
    }
    
    private String select() {
        return getQueryValueAsString("q")
                .map(q -> q.replaceAll("['\"`%]+", "").toLowerCase()) // some bad char removal
                .map(q -> select + " " + TERM_WHERE_TEMPLATE.replace("{term}", q))
                .orElse(select);
    }
    
    private String limit() {
        return getQueryValueAsInteger("l")
                .map(l -> " limit " + l)
                .orElse("");
    }
}
