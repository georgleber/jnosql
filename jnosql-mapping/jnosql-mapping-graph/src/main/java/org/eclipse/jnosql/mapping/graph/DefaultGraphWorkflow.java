/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.eclipse.jnosql.mapping.graph;

import org.apache.tinkerpop.gremlin.structure.Vertex;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * The default implementation of {@link GraphWorkflow}
 */
@ApplicationScoped
class DefaultGraphWorkflow implements GraphWorkflow {

    private GraphEventPersistManager graphEventPersistManager;

    private GraphConverter converter;


    DefaultGraphWorkflow() {
    }

    @Inject
    DefaultGraphWorkflow(GraphEventPersistManager graphEventPersistManager, GraphConverter converter) {
        this.graphEventPersistManager = graphEventPersistManager;
        this.converter = converter;
    }

    @Override
    public <T> T flow(T entity, UnaryOperator<Vertex> action) {
        Function<T, T> flow = getFlow(entity, action);
        return flow.apply(entity);
    }

    private <T> Function<T, T> getFlow(T entity, UnaryOperator<Vertex> action) {
        UnaryOperator<T> validation = t -> Objects.requireNonNull(t, "entity is required");

        UnaryOperator<T> firePreEntity = t -> {
            graphEventPersistManager.firePreEntity(t);
            return t;
        };

        UnaryOperator<T> firePreGraphEntity = t -> {
            graphEventPersistManager.firePreGraphEntity(t);
            return t;
        };

        Function<T, Vertex> converterGraph = t -> converter.toVertex(t);

        Function<Vertex, T> converterEntity = t -> converter.toEntity(entity, t);

        UnaryOperator<T> firePostEntity = t -> {
            graphEventPersistManager.firePostEntity(t);
            return t;
        };

        UnaryOperator<T> firePostGraphEntity = t -> {
            graphEventPersistManager.firePostGraphEntity(t);
            return t;
        };

        return validation
                .andThen(firePreEntity)
                .andThen(firePreGraphEntity)
                .andThen(converterGraph)
                .andThen(action)
                .andThen(converterEntity)
                .andThen(firePostEntity)
                .andThen(firePostGraphEntity);
    }
}
