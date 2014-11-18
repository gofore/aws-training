package com.gofore.aws.workshop.common.cloudformation;

import static com.gofore.aws.workshop.common.functional.Lists.findFirst;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.cloudformation.AmazonCloudFormationAsync;
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest;
import com.amazonaws.services.cloudformation.model.DescribeStacksResult;
import com.amazonaws.services.cloudformation.model.Output;
import com.amazonaws.services.cloudformation.model.Stack;

public class CloudFormationClient {

    private final AmazonCloudFormationAsync cloudFormation;

    public CloudFormationClient(AmazonCloudFormationAsync cloudFormation) {
        this.cloudFormation = cloudFormation;
    }

    public CompletableFuture<DescribeStacksResult> describeStacks(DescribeStacksRequest request) {
        CompletableFuture<DescribeStacksResult> future = new CompletableFuture<>();
        cloudFormation.describeStacksAsync(request, new AsyncHandler<DescribeStacksRequest, DescribeStacksResult>() {
            @Override
            public void onError(Exception exception) {
                future.completeExceptionally(exception);
            }

            @Override
            public void onSuccess(DescribeStacksRequest request, DescribeStacksResult result) {
                future.complete(result);
            }
        });
        return future;
    }

    /**
     * Gets the given stack outputs.
     * 
     * @param stackName the stack name
     * @return future of stack's outputs or failed future if stack does not exist
     */
    public CompletableFuture<List<Output>> getStackOutputs(String stackName) {
        return describeStacks(new DescribeStacksRequest().withStackName(stackName))
                .thenApply(r -> r.getStacks().get(0))
                .thenApply(Stack::getOutputs);
    }


    /**
     * Gets the given stack's output by its key.
     * 
     * @param stackName the stack name
     * @param outputKey the output key
     * @return future of stack's output of failed future if stack or output does not exist
     */
    public CompletableFuture<Output> getStackOutput(String stackName, String outputKey) {
        return getStackOutputs(stackName)
                .thenApply(findFirst(o -> o.getOutputKey().equals(outputKey)))
                .thenApply(Optional::get);
    }
}
