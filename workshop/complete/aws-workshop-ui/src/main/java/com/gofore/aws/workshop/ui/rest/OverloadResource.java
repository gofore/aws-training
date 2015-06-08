package com.gofore.aws.workshop.ui.rest;

import com.amazonaws.services.ec2.model.Instance;
import com.gofore.aws.workshop.common.ec2.Ec2Client;
import com.gofore.aws.workshop.common.rest.RestServerResource;
import com.gofore.aws.workshop.ui.util.Overloader;
import com.google.inject.Inject;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public class OverloadResource extends RestServerResource {

    private final Overloader overloader;
    private final Ec2Client ec2;

    @Inject
    public OverloadResource(Overloader overloader, Ec2Client ec2) {
        this.overloader = overloader;
        this.ec2 = ec2;
    }

    @Get("json")
    public OverloadStatus getOverloadStatus() {
        return new OverloadStatus(getPrivateIp(), overloader.isOverloading());
    }

    @Put("json")
    public OverloadStatus toggleOverload() {
        return new OverloadStatus(getPrivateIp(), overloader.toggleOverload());
    }

    private String getPrivateIp() {
        return ec2.getInstance(ec2.getInstanceId(), true).thenApply(Instance::getPrivateIpAddress).join();
    }

    public static class OverloadStatus {
        public String privateIp;
        public boolean overloading;

        public OverloadStatus() { }

        public OverloadStatus(String privateIp, boolean overloading) {
            this.privateIp = privateIp;
            this.overloading = overloading;
        }
    }
}
