package com.gofore.aws.workshop.ui.search;

import java.util.function.Function;

import com.amazonaws.services.simpledb.model.Item;

public class SearchItemMapper implements Function<Item, SearchItem> {

    @Override
    public SearchItem apply(Item item) {
        SearchItem si = new SearchItem(item.getName());
        item.getAttributes().forEach(attr -> si.addAttribute(attr.getName(), attr.getValue()));
        return si;
    }
}
