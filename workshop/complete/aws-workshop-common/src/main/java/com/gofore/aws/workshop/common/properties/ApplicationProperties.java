package com.gofore.aws.workshop.common.properties;

import java.util.Optional;

public class ApplicationProperties extends AbstractPropertyLoader {

    private final PropertyLoaderChain chain = new PropertyLoaderChain();
    private final PropertyLoader interpolator = new PropertyInterpolator(chain);
    
    public ApplicationProperties withSystemPropertyLoader() {
        chain.add(new SystemPropertyLoader());
        return this;
    }

    public ApplicationProperties withEnvironmentPropertyLoader() {
        chain.add(new EnvironmentPropertyLoader());
        return this;
    }

    public ApplicationProperties withAwsCredentialsEnvLoader() {
        chain.add(new AwsCredentialsEnvLoader());
        return this;
    }
    
    public ApplicationProperties withAwsCredentialsCsvLoader(String csvFile) {
        chain.add(new AwsCredentialsCsvLoader(csvFile));
        return this;
    }
    
    public ApplicationProperties withClasspathPropertyLoader(String propertyFile) {
        chain.add(new ClasspathPropertyLoader(propertyFile));
        return this;
    }

    @Override
    public Optional<String> lookupOptional(String name) {
        return interpolator.lookupOptional(name);
    }
}
