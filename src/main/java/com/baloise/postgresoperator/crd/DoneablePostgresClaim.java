package com.baloise.postgresoperator.crd;

import io.fabric8.kubernetes.api.builder.Function;
import io.fabric8.kubernetes.client.CustomResourceDoneable;

public class DoneablePostgresClaim extends CustomResourceDoneable<PostgresClaim> {
    public DoneablePostgresClaim(PostgresClaim resource, Function<PostgresClaim, PostgresClaim> function) {
        super(resource, function);
    }
}
