package com.gofore.aws.workshop.common.ec2;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.ec2.AmazonEC2Async;
import com.amazonaws.services.ec2.model.*;
import com.amazonaws.util.EC2MetadataUtils;
import com.gofore.aws.workshop.common.functional.Lists;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Ec2Client {

    private final AmazonEC2Async ec2;

    public Ec2Client(AmazonEC2Async ec2) {
        this.ec2 = ec2;
    }

    public String getInstanceId() {
        return EC2MetadataUtils.getInstanceId();
    }

    public Optional<Tag> getTag(Instance instance, String name) {
        return Lists.findFirst(instance.getTags(), t -> t.getKey().equals(name));
    }

    public CompletableFuture<DescribeInstancesResult> describeInstances(DescribeInstancesRequest request) {
        CompletableFuture<DescribeInstancesResult> future = new CompletableFuture<>();
        ec2.describeInstancesAsync(request, new AsyncHandler<DescribeInstancesRequest, DescribeInstancesResult>() {
            @Override
            public void onError(Exception exception) {
                future.completeExceptionally(exception);
            }

            @Override
            public void onSuccess(DescribeInstancesRequest request, DescribeInstancesResult describeInstancesResult) {
                future.complete(describeInstancesResult);
            }
        });
        return future;
    }

    public CompletableFuture<List<Instance>> getInstances(List<String> instanceIds, boolean requirePrivateAccessible) {
        return describeInstances(new DescribeInstancesRequest().withInstanceIds(instanceIds))
                .thenApply(DescribeInstancesResult::getReservations)
                .thenApply(Lists.findFirst())
                .thenApply(Reservation::getInstances)
                .thenApply(instances -> instances.stream().filter(accessible(requirePrivateAccessible)))
                .thenApply(instances -> instances.collect(Collectors.toList()));
    }

    public CompletableFuture<Instance> getInstance(String instanceId, boolean requirePrivateAccessible) {
        return getInstances(ImmutableList.of(instanceId), requirePrivateAccessible)
                .thenApply(Lists.findFirst());
    }

    private Predicate<Instance> accessible(boolean requirePrivateAccessible) {
        if (requirePrivateAccessible) {
            return i -> !Strings.isNullOrEmpty(i.getPrivateIpAddress());
        } else {
            return i -> true;
        }
    }

}
