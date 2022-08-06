package com.ptt.boundary;

import java.time.Instant;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.ptt.control.PlanRepository;
import com.ptt.control.PlanRunRepository;
import com.ptt.entity.PlanRun;
import com.ptt.entity.dto.PlanRunDto;

import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.LocalObjectReferenceBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.PodSpecBuilder;
import io.fabric8.kubernetes.api.model.PodTemplateSpecBuilder;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.api.model.batch.v1.JobSpecBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

@Path("planrun")
public class PlanRunResource {
    @Inject
    PlanRunRepository planRunRepository;
    
    @Inject
    PlanRepository planRepository;
 
    @Inject
    KubernetesClient kubernetesClient;
 
    @POST
    @Path("{planrunid}/run")
    public Response runTestPlan(@PathParam("planrunid") long planRunId) {
        PlanRun planRun = planRunRepository.findById(planRunId);
        if(planRun == null) {
            return Response.status(404, "Plan run doesn't exist!").build();
        }
        kubernetesClient.batch().v1().jobs().inNamespace("ptt").create(
            new JobBuilder()
            .withApiVersion("batch/v1")
            .withMetadata(
                new ObjectMetaBuilder()
                .withName("ptt-client-job-"+Instant.now().toEpochMilli())
                .withNamespace("ptt").build())
            .withSpec(
                new JobSpecBuilder()
                .withTtlSecondsAfterFinished(30)
                .withParallelism(4) //Set variable
                .withTemplate(
                    new PodTemplateSpecBuilder()
                    .withSpec(
                        new PodSpecBuilder()
                        .withContainers(
                            new ContainerBuilder()
                            .withName("ptt-client-pod")
                            .withEnv(
                                new EnvVarBuilder()
                                .withName("TEST_PLANRUN_ID")
                                .withValue(planRun.id.toString())
                                .build()
                            )
                            .withImage("ghcr.io/2023-da-ptt/ptt-client:latest")
                            .withImagePullPolicy("Always")
                            .build()
                        ).withImagePullSecrets(
                            new LocalObjectReferenceBuilder()
                            .withName("dockerconfigjson-github-com")
                            .build()
                        ).withRestartPolicy("Never")
                        .build())
                    .build())
                .build())
            .build());
        return Response.status(202, "Clients have been started!").build();
    }

    @GET
    public List<PlanRunDto> getAllDataPoints() {
        return planRunRepository.findAll().project(PlanRunDto.class).list();
    }

    @GET
    @Path("{planrunid}")
    public PlanRunDto getAllDataPointById(@PathParam("planrunid") long id) {
        return planRunRepository.find("id", id).project(PlanRunDto.class).singleResult();
    }

    @POST
    @Transactional
    public PlanRunDto createPlanRun(PlanRunDto planRunDto) {
        PlanRun planRun = new PlanRun();
        planRun.plan = planRepository.findById(planRunDto.getPlanId());
        planRun.startTime = planRunDto.getStartTime();
        planRun.duration = planRunDto.getDuration();
        planRunRepository.persist(planRun);
        return planRunDto;
    }
}