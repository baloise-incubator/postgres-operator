package com.baloise.postgresoperator.crd;

import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinitionBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.DoneableCustomResourceDefinition;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.internal.KubernetesDeserializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostgresClaimWatcher implements Watcher<PostgresClaim> {

    private static final String CRD_GROUP = "incubator.baloise.com";
    private static final String CRD_NAME = "postgresclaims." + CRD_GROUP;

    private final KubernetesClient kubernetesClient;

    @PostConstruct
    public void registerCrd() {
        Resource<CustomResourceDefinition, DoneableCustomResourceDefinition> res = kubernetesClient.customResourceDefinitions().withName(CRD_NAME);
        CustomResourceDefinition customResourceDefinition = res.get();
        if (customResourceDefinition == null) {
            log.info("Registering PostgresClaim CRD");
            KubernetesDeserializer.registerCustomKind(CRD_GROUP + "/v1beta1", "PostgresClaim", PostgresClaim.class);
            customResourceDefinition = new CustomResourceDefinitionBuilder()
                    .withApiVersion("apiextensions.k8s.io/v1beta1")
                    .withNewMetadata().withName(CRD_NAME).endMetadata()
                    .withNewSpec().withGroup(CRD_GROUP)
                    .withVersion("v1beta1")
                    .withNewNames().withKind("PostgresClaim").withShortNames("postgresclaim").withPlural("postgresclaims").endNames()
                    .withScope("Namespaced")
                    .endSpec()
                    .build();
            kubernetesClient.customResourceDefinitions().create(customResourceDefinition);
        }
        MixedOperation<PostgresClaim, PostgresClaimList, DoneablePostgresClaim, Resource<PostgresClaim, DoneablePostgresClaim>> op = kubernetesClient.customResources(customResourceDefinition, PostgresClaim.class, PostgresClaimList.class, DoneablePostgresClaim.class);
        op.inAnyNamespace().watch(this);
    }

    @Override
    public void eventReceived(Action action, PostgresClaim postgresClaim) {
        log.info("Received action: " + action.name());
        log.info("for PostgresClaim: " + postgresClaim.getMetadata().getName());
    }

    @Override
    public void onClose(KubernetesClientException e) {
        log.error("Application Exception: ", e);
    }
}
