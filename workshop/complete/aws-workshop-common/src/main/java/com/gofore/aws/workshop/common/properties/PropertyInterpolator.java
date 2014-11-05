package com.gofore.aws.workshop.common.properties;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Property interpolator that substitutes {property.name} placeholders with
 * real properties looked up from the delegating property loader. Supports
 * recursive lookups, but there's no guard for infinite recursion, so don't
 * mess up your properties.
 */
public class PropertyInterpolator extends AbstractPropertyLoader {

    private static final Pattern INTERPOLATION_PATTERN = Pattern.compile("\\{([-\\w\\.]+)\\}");
    
    private final PropertyLoader delegate;

    public PropertyInterpolator(PropertyLoader delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<String> lookupOptional(String name) {
        return delegate.lookupOptional(name).map(this::replace);
    }
    
    private String replace(String value) {
        StringBuffer replaced = new StringBuffer();
        Matcher m = INTERPOLATION_PATTERN.matcher(value);
        while (m.find()) {
            Optional<String> val = lookupOptional(m.group(1));
            m.appendReplacement(replaced, val.orElse(m.group()));
        }
        m.appendTail(replaced);
        return replaced.toString();
    }
}
