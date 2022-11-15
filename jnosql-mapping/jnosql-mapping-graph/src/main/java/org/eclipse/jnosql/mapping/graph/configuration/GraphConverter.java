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
package org.eclipse.jnosql.mapping.graph.configuration;

import jakarta.nosql.Settings;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.eclipse.jnosql.mapping.config.MicroProfileSettings;
import org.eclipse.jnosql.mapping.graph.GraphConfiguration;
import org.eclipse.jnosql.mapping.reflection.Reflections;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import java.util.function.Supplier;

import static org.eclipse.jnosql.mapping.config.MappingConfigurations.GRAPH_PROVIDER;


@ApplicationScoped
class GraphConverter implements Supplier<Graph> {

    @Override
    @Produces
    public Graph get(){
        Settings settings = MicroProfileSettings.INSTANCE;

        GraphConfiguration configuration = settings.get(GRAPH_PROVIDER, Class.class)
                .filter(c -> GraphConfiguration.class.isAssignableFrom(c))
                .map(c -> {
                    final Reflections reflections = CDI.current().select(Reflections.class).get();
                    return (GraphConfiguration) reflections.newInstance(c);
                }).orElseGet(() -> GraphConfiguration.getConfiguration());

        Graph graph = configuration.apply(settings);
        return graph;
    }

    public void close(@Disposes Graph graph) throws Exception {
        graph.close();
    }
}
