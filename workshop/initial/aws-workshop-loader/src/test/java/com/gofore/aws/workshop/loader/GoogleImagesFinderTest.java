package com.gofore.aws.workshop.loader;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.gofore.aws.workshop.loader.pages.GoogleImagesFinder;
import org.junit.Test;

public class GoogleImagesFinderTest {

    @Test
    public void testFindPageUrls() throws Exception {
        List<String> urls = new GoogleImagesFinder("pekka peräpukama", Optional.<Long>empty()).findPageUrls().collect(Collectors.toList());
        assertNotNull(urls);
    }
}