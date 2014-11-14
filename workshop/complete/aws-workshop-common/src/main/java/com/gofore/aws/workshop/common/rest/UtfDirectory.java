package com.gofore.aws.workshop.common.rest;

import java.util.Optional;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CharacterSet;
import org.restlet.resource.Directory;

public class UtfDirectory extends Directory {

    public UtfDirectory(Context context, String rootUri) {
        super(context, rootUri);
    }

    @Override
    public void handle(Request request, Response response) {
        super.handle(request, response);
        Optional.ofNullable(response.getEntity())
                .ifPresent(r -> r.setCharacterSet(CharacterSet.UTF_8));
    }
}
