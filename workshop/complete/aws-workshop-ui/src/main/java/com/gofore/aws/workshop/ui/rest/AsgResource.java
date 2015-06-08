package com.gofore.aws.workshop.ui.rest;

import com.amazonaws.services.autoscaling.model.Instance;
import com.amazonaws.services.ec2.model.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gofore.aws.workshop.common.asg.AsgClient;
import com.gofore.aws.workshop.common.ec2.Ec2Client;
import com.gofore.aws.workshop.common.rest.RestServerResource;
import com.google.inject.Inject;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AsgResource extends RestServerResource {

    private static final ObjectMapper om = new ObjectMapper();

    private final Ec2Client ec2;
    private final AsgClient asg;

    @Inject
    public AsgResource(Ec2Client ec2, AsgClient asg) {
        this.ec2 = ec2;
        this.asg = asg;
    }

    @Get("json")
    public List<OverloadResource.OverloadStatus> getOverloadStatuses() {
        return getAsgName().thenCompose(asg::getInstances)
                .thenApply(ai -> ai.stream().map(Instance::getInstanceId).collect(Collectors.toList()))
                .thenCompose(ii -> ec2.getInstances(ii, true))
                .thenApply(ii -> ii.parallelStream().map(this::loadOverloadStatus))
                .thenApply(os -> os.collect(Collectors.toList()))
                .join();
    }

    @Put("json")
    public OverloadResource.OverloadStatus toggleOverload() {
        try {
            String ip = getQueryValueAsString("ip").get();
            HttpURLConnection connection = (HttpURLConnection) getOverloadUrl(ip).openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("PUT");
            return om.readValue(connection.getInputStream(), OverloadResource.OverloadStatus.class);
        } catch (IOException ex) {
            throw  new RuntimeException(ex);
        }
    }

    private OverloadResource.OverloadStatus loadOverloadStatus(com.amazonaws.services.ec2.model.Instance instance) {
        try {
            HttpURLConnection connection = (HttpURLConnection) getOverloadUrl(instance.getPrivateIpAddress()).openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("GET");
            return om.readValue(connection.getInputStream(), OverloadResource.OverloadStatus.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private CompletableFuture<String> getAsgName() {
        return ec2.getInstance(ec2.getInstanceId(), false)
                .thenApply(i -> ec2.getTag(i, "aws:autoscaling:groupName"))
                .thenApply(Optional::get)
                .thenApply(Tag::getValue);
    }

    private URL getOverloadUrl(String ip) {
        try {
            return new URL("http://" + ip + ":9001/api/overload");
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
