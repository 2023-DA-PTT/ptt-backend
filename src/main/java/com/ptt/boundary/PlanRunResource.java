package com.ptt.boundary;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ptt.control.PlanRunRepository;
import com.ptt.entity.dto.PlanRunDto;

@Path("planrun")
public class PlanRunResource {
    @Inject
    PlanRunRepository planRunRepository;

 
    /*
    @Inject
    KubernetesClient kubernetesClient;
 
    apiVersion: v1
    kind: Pod
    metadata:
      name: ptt-client-test
      namespace: ptt
    spec:
      containers:
      - name: ptt-client-test
        image: ghcr.io/2023-da-ptt/ptt-client:latest
      imagePullSecrets:
      - name: dockerconfigjson-github-com
    *//*
    @GET
    @Path("user/{userId}/run")
    public Response runTestPlan(@PathParam("userId") long userId) {
        kubernetesClient.pods().create(
                new PodBuilder()
                        .withApiVersion("v1")
                        .withKind("Pod")
                        .withMetadata(
                                new ObjectMetaBuilder()
                                        .withName("ptt-client")
                                        .withNamespace("ptt")
                                        .build())
                        .withSpec(
                                new PodSpecBuilder()
                                        .withContainers(
                                                new ContainerBuilder()
                                                        .withName("ptt-client-" + Instant.now().getEpochSecond())
                                                        .withImage("ghcr.io/2023-da-ptt/ptt-client:latest")
                                                        .build()
                                        ).withImagePullSecrets(
                                                new LocalObjectReferenceBuilder()
                                                        .withName("dockerconfigjson-github-com")
                                                        .build()
                                        )
                                        .build()
                        )
                        .build()
        );

        return Response.noContent().build();
    }*/

    @GET
    public List<PlanRunDto> getAllDataPoints() {
        return planRunRepository.findAll().project(PlanRunDto.class).list();
    }
}