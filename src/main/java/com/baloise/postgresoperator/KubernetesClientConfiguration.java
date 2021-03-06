package com.baloise.postgresoperator;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KubernetesClientConfiguration {

    @Bean
    public KubernetesClient createK8sClient() {
        return new DefaultKubernetesClient();
    }
}
