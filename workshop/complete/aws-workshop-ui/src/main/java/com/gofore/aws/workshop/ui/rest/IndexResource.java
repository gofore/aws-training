package com.gofore.aws.workshop.ui.rest;

import com.gofore.aws.workshop.common.rest.RestServerResource;
import org.restlet.resource.Get;

public class IndexResource extends RestServerResource {
    
    @Get
    public void index() {
        redirectPermanent("/index.html");
    }
    
}
