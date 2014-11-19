package com.gofore.aws.workshop.common.properties;

import java.util.Map;
import java.util.Optional;

import com.amazonaws.services.cloudformation.model.Output;
import com.gofore.aws.workshop.common.cloudformation.CloudFormationClient;
import com.google.common.collect.Maps;

public class CloudFormationOutputsPropertyLoader extends AbstractPropertyLoader {

    private final Map<String, Output> outputs;
    
    public CloudFormationOutputsPropertyLoader(ApplicationProperties properties, CloudFormationClient cloudFormationClient) {
        this.outputs = Maps.uniqueIndex(
                cloudFormationClient.getStackOutputs(properties.lookup("aws.stack")).join(),
                Output::getOutputKey
        );
    }
    
    @Override
    public Optional<String> lookupOptional(String name) {
        return Optional.ofNullable(outputs.get(name)).map(Output::getOutputValue);
    }
}
