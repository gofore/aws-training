package com.gofore.aws.workshop.fetcher;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.stream.Collectors;

import com.gofore.aws.workshop.common.net.HtmlClient;
import com.gofore.aws.workshop.fetcher.images.GoogleImagesFetcher;
import com.gofore.aws.workshop.fetcher.images.Image;
import org.junit.Test;

public class GoogleImagesFetcherTest {

    @Test
    public void testFetchImages() throws Exception {
        List<Image> images = new GoogleImagesFetcher(new HtmlClient())
                .fetchImages("http://www.google.com/images?sout=1&q=jorma&start=0")
                .join()
                .collect(Collectors.toList());
        assertNotNull(images);
    }
}