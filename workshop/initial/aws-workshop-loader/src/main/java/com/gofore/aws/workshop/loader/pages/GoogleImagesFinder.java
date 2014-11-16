package com.gofore.aws.workshop.loader.pages;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GoogleImagesFinder implements PageFinder {

    private static final String PAGE_URL = "http://www.google.com/images?sout=1&q={query}&start={idx}";
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36";
    private static final String URL_ENCODING = "UTF-8";
    private static final Pattern RESULTS_PATTERN = Pattern.compile("[\\d,]+");
    private static final int IMAGES_PER_PAGE = 20;
    
    private final String query;
    private final Optional<Long> limit;

    public GoogleImagesFinder(String query, Optional<Long> limit) {
        this.query = query;
        this.limit = limit;
    }

    @Override
    public Stream<String> findPageUrls() throws Exception {
        Document document = Jsoup.connect(getPageUrl(1))
                .userAgent(USER_AGENT)
                .get();
        long results = getResults(document.select("#resultStats").text());
        long pages = results / IMAGES_PER_PAGE;
        long range = Math.min(pages, limit.orElse(Long.MAX_VALUE));
        return LongStream.rangeClosed(1, range).mapToObj(this::getPageUrl);
    }
    
    private String getPageUrl(long page) {
        try {
            return PAGE_URL
                    .replace("{query}", URLEncoder.encode(query, URL_ENCODING))
                    .replace("{idx}", Long.toString((page - 1) * IMAGES_PER_PAGE));
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    private long getResults(String text) {
        Matcher matcher = RESULTS_PATTERN.matcher(text);
        if (matcher.find()) {
            return Long.parseLong(matcher.group().replace(",", ""));
        } else {
            return 0;
        }
    }
}
