package com.gofore.aws.workshop.common.properties;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyInterpolator extends AbstractPropertyLoader {

    private static final Pattern INTERPOLATION_PATTERN = Pattern.compile("\\{([-\\w\\.]+)\\}");
    
    private final PropertyLoader delegate;

    public PropertyInterpolator(PropertyLoader delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<String> lookupOptional(String name) {
        return delegate.lookupOptional(name).flatMap(this::replace);
    }
    
    private Optional<String> replace(String value) {
        StringBuffer replaced = new StringBuffer();
        Matcher m = INTERPOLATION_PATTERN.matcher(value);
        while (m.find()) {
            Optional<String> val = delegate.lookupOptional(m.group(1));
            if (!val.isPresent()) {
                return val;
            } else {
                m.appendReplacement(replaced, val.get());
            }
        }
        m.appendTail(replaced);
        return Optional.of(replaced.toString());
    }
}
