package com.gofore.aws.workshop.fetcher;

import com.gofore.aws.workshop.common.net.HtmlClient;
import com.gofore.aws.workshop.common.net.HttpClient;
import com.gofore.aws.workshop.common.properties.ApplicationProperties;
import com.gofore.aws.workshop.common.s3.S3Client;
import com.gofore.aws.workshop.common.s3.S3ClientProvider;
import com.gofore.aws.workshop.common.simpledb.SimpleDBClient;
import com.gofore.aws.workshop.common.sqs.SqsClient;
import com.gofore.aws.workshop.fetcher.images.GoogleImagesFetcher;
import com.gofore.aws.workshop.fetcher.images.ImageFetcher;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class FetcherModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HtmlClient.class).in(Singleton.class);
        bind(HttpClient.class).in(Singleton.class);
        bind(ImageFetcher.class).to(GoogleImagesFetcher.class).in(Singleton.class);
        
        bind(S3Client.class).toProvider(S3ClientProvider.class).in(Singleton.class);
    }

    @Provides @Singleton
    public ApplicationProperties provideApplicationProperties() {
        return new ApplicationProperties()
                .withSystemPropertyLoader()
                .withClasspathPropertyLoader("fetcher.properties");
    }
    
    @Provides @Singleton
    public SimpleDBClient provideSimpleDBClient(ApplicationProperties properties) {
        String accessKey = properties.lookup("aws.access.key");
        String secretKey = properties.lookup("aws.secret.key");
        String endpoint = properties.lookup("aws.simpledb.endpoint");
        return new SimpleDBClient(accessKey, secretKey, endpoint);
    }
    
    @Provides
    @Singleton
    public SqsClient provideSqsClient(ApplicationProperties properties) {
        String accessKey = properties.lookup("aws.access.key");
        String secretKey = properties.lookup("aws.secret.key");
        String endpoint = properties.lookup("aws.sqs.endpoint");
        return new SqsClient(accessKey, secretKey, endpoint);
    }
}
