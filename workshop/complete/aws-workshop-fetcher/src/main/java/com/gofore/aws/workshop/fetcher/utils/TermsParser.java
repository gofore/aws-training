package com.gofore.aws.workshop.fetcher.utils;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.google.inject.Singleton;

@Singleton
public class TermsParser {
    
    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\W+");
    
    public Stream<String> parse(String text) {
        return SPLIT_PATTERN.splitAsStream(text).map(String::toLowerCase);
    }
    
}
