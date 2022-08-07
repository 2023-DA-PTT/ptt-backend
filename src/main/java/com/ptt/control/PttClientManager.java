package com.ptt.control;
import java.time.Instant;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.ptt.entity.dto.PlanRunInstructionDto;

import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.LocalObjectReferenceBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.PodSpecBuilder;
import io.fabric8.kubernetes.api.model.PodTemplateSpecBuilder;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.api.model.batch.v1.JobSpecBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.runtime.configuration.ProfileManager;

@ApplicationScoped
public class PttClientManager {
    @Inject
    KubernetesClient kubernetesClient;
    
    public void startClient(long planRunId, List<PlanRunInstructionDto> planRunInstuctionDtos) {
        for(PlanRunInstructionDto dto : planRunInstuctionDtos) {
            startClient(planRunId, dto);
        }
    }

    public void startClient(long planRunId, PlanRunInstructionDto planRunInstructionDto) {
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
                .withParallelism(planRunInstructionDto.getAmount())
                .withTemplate(
                    new PodTemplateSpecBuilder()
                    .withSpec(
                        new PodSpecBuilder()
                        .withNodeName(planRunInstructionDto.getNodeName())
                        .withContainers(
                            new ContainerBuilder()
                            .withName("ptt-client-pod")
                            .withEnv(
                                new EnvVarBuilder()
                                .withName("TEST_PLAN_RUN_ID")
                                .withValue(String.valueOf(planRunId))
                                .build()
                            )
                            .withImage("ghcr.io/2023-da-ptt/ptt-client:latest")
                            .withImagePullPolicy(
                                ProfileManager.getActiveProfile().equals("prod") ? "Always" : "IfNotPresent")
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
    }
}
