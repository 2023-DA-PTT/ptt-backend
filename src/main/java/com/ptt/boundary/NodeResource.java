package com.ptt.boundary;

import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ptt.control.PttClientManager;

@Path("node")
public class NodeResource {
    @Inject
    PttClientManager pttClientManager;

    @Path("locations")
    @GET
    public Set<String> getAllNodeLocations() {
        return pttClientManager.getNodeNames();  
    }
}
