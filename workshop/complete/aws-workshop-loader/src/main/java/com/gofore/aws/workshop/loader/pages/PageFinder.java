package com.gofore.aws.workshop.loader.pages;

import java.util.stream.Stream;

public interface PageFinder {
    
    Stream<String> findPageUrls() throws Exception;
    
}
