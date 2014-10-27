package com.gofore.aws.workshop.ui.search;

import java.util.List;

public class SearchResult {
    
    private final List<SearchItem> items;
    private final String nextToken;

    public SearchResult(List<SearchItem> items, String nextToken) {
        this.items = items;
        this.nextToken = nextToken;
    }

    public List<SearchItem> getItems() {
        return items;
    }

    public String getNextToken() {
        return nextToken;
    }
}
