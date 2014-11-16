package com.gofore.aws.workshop.common.properties;

import static java.nio.file.Files.newBufferedReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import au.com.bytecode.opencsv.CSVReader;

public class AwsCredentialsCsvLoader extends AbstractPropertyLoader {
    
    private static final String AWS_DIR = ".aws";
    
    private static final String[] DIRS = {
            System.getProperty("user.dir"),
            System.getProperty("user.home")
    };
    
    private final Map<String, String> properties;

    public AwsCredentialsCsvLoader(String csvFile) {
        this.properties = lookupFile(csvFile)
                .map(this::toProperties)
                .orElseGet(Collections::emptyMap);
    }

    @Override
    public Optional<String> lookupOptional(String name) {
        return Optional.ofNullable(properties.get(name));
    }
    
    private Optional<Path> lookupFile(String csvFile) {
        return Stream.of(DIRS).map(d -> Paths.get(d, AWS_DIR, csvFile)).filter(Files::exists).findFirst();
    }
    
    private Map<String, String> toProperties(Path path) {
        try (CSVReader reader = new CSVReader(newBufferedReader(path, PROPERTIES_ENCODING))) {
            String[] credentials = reader.readAll().stream().skip(1).findFirst().get();
            Map<String, String> p = new HashMap<>();
            p.put("aws.user", credentials[0]);
            p.put("aws.access.key", credentials[1]);
            p.put("aws.secret.key", credentials[2]);
            return p;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
