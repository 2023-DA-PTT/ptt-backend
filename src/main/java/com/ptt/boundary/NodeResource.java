package com.ptt.boundary;

import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ptt.manager.PttClientManager;
import io.quarkus.security.Authenticated;

@Path("node")
@Authenticated
public class NodeResource {
    @Inject
    PttClientManager pttClientManager;

    @Path("locations")
    @GET
    public Set<String> getAllNodeLocations() {
        return pttClientManager.getNodeNames();
    }
}
