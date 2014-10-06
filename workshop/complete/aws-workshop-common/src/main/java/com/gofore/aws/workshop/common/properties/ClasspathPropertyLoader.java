package com.gofore.aws.workshop.common.properties;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;

public class ClasspathPropertyLoader implements PropertyLoader {
    
    private static final Charset ENCODING = Charset.forName("UTF-8");
    
    private final Properties properties;

    public ClasspathPropertyLoader(String propertyFile) {
        try (Reader reader = getReader(propertyFile)) {
            Properties p = new Properties();
            p.load(reader);
            this.properties = p;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String lookup(String name) throws NoSuchElementException {
        return lookupOptional(name).get();
    }

    @Override
    public Optional<String> lookupOptional(String name) {
        return Optional.ofNullable(properties.getProperty(name));
    }

    private Reader getReader(String propertyFile) {
        return new InputStreamReader(getClass().getResourceAsStream(getResourceName(propertyFile)), ENCODING);
    }
    
    private String getResourceName(String propertyFile) {
        if (propertyFile.startsWith("/")) {
            return propertyFile;
        } else {
            return "/" + propertyFile;
        }
    }
}
