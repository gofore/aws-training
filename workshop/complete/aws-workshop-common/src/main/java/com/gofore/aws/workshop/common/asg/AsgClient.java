package com.gofore.aws.workshop.common.asg;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.autoscaling.AmazonAutoScalingAsync;
import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsRequest;
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsResult;
import com.amazonaws.services.autoscaling.model.Instance;
import com.gofore.aws.workshop.common.functional.Lists;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsgClient {

    private final AmazonAutoScalingAsync asg;

    public AsgClient(AmazonAutoScalingAsync asg) {
        this.asg = asg;
    }

    public CompletableFuture<DescribeAutoScalingGroupsResult> describeAutoScalingGroups(DescribeAutoScalingGroupsRequest request) {
        CompletableFuture<DescribeAutoScalingGroupsResult> future = new CompletableFuture<>();
        asg.describeAutoScalingGroupsAsync(request, new AsyncHandler<DescribeAutoScalingGroupsRequest, DescribeAutoScalingGroupsResult>() {
            @Override
            public void onError(Exception exception) {
                future.completeExceptionally(exception);
            }

            @Override
            public void onSuccess(DescribeAutoScalingGroupsRequest request, DescribeAutoScalingGroupsResult describeAutoScalingGroupsResult) {
                future.complete(describeAutoScalingGroupsResult);
            }
        });
        return future;
    }

    public CompletableFuture<List<Instance>> getInstances(String asgName) {
        return describeAutoScalingGroups(new DescribeAutoScalingGroupsRequest().withAutoScalingGroupNames(asgName))
                .thenApply(DescribeAutoScalingGroupsResult::getAutoScalingGroups)
                .thenApply(Lists.findFirst())
                .thenApply(AutoScalingGroup::getInstances);
    }
}
