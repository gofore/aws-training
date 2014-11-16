package com.gofore.aws.workshop.common.rest;

import java.util.Map;

import com.gofore.aws.workshop.common.functional.Maps;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.google.inject.Inject;
import org.restlet.resource.Get;

public class ConfigurationResource extends RestServerResource {

    private final ApplicationProperties properties;

    @Inject
    public ConfigurationResource(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Get("json")
    public Map<String, String> getProperty() {
        return getProperty(getAttribute("name"));
    }
    
    private Map<String, String> getProperty(String name) {
        return properties.lookupOptional(name)
                .map(value -> Maps.of(name, value))
                .orElse(Maps.of());
    }
}
