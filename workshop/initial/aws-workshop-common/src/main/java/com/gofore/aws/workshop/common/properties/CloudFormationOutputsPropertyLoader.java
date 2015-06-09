package com.gofore.aws.workshop.common.properties;

import com.amazonaws.services.cloudformation.model.Output;
import com.gofore.aws.workshop.common.cloudformation.CloudFormationClient;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

public class CloudFormationOutputsPropertyLoader extends AbstractPropertyLoader {

    private final String awsStack;
    private final CloudFormationClient cloudFormationClient;
    private Map<String, Output> outputs;
    
    public CloudFormationOutputsPropertyLoader(ApplicationProperties properties, CloudFormationClient cloudFormationClient) {
        this.awsStack = properties.lookup("aws.stack");
        this.cloudFormationClient = cloudFormationClient;
    }
    
    @Override
    public synchronized Optional<String> lookupOptional(String name) {
        loadOutputs();
        return Optional.ofNullable(outputs.get(name)).map(Output::getOutputValue);
    }

    private void loadOutputs() {
        if (outputs == null) {
            this.outputs = Maps.uniqueIndex(cloudFormationClient.getStackOutputs(awsStack).join(), Output::getOutputKey);
        }
    }
}
