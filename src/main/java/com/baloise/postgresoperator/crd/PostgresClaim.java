package com.baloise.postgresoperator.crd;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;

public class PostgresClaim extends CustomResource implements Namespaced {
}
