package com.gofore.aws.workshop.ui.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchItem {
    
    private final String name;
    private final Map<String, Object> attributes;

    public SearchItem(String name) {
        this.name = name;
        this.attributes = new LinkedHashMap<>();
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public void addAttribute(String name, String value) {
        Object current = attributes.get(name);
        if (current == null) {
            attributes.put(name, value);
        } else if (current instanceof String) {
            List<String> values = new ArrayList<>();
            values.add((String) current);
            values.add(value);
            attributes.put(name, values);
        } else if (current instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> values = (List<String>) current;
            values.add(value);
        }
    }
}
