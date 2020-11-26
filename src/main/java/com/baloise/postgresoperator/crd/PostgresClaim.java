package com.baloise.postgresoperator.crd;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;

public class PostgresClaim extends CustomResourceDefinition implements Namespaced {
}
