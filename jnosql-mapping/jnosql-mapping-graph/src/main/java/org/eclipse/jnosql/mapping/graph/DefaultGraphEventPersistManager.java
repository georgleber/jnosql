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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.EntityPostPersist;
import org.eclipse.jnosql.mapping.EntityPrePersist;

@ApplicationScoped
class DefaultGraphEventPersistManager implements GraphEventPersistManager {


    @Inject
    private Event<EntityPrePersist> entityPrePersistEvent;

    @Inject
    private Event<EntityPostPersist> entityPostPersistEvent;

    @Inject
    private Event<EntityGraphPrePersist> entityGraphPrePersist;

    @Inject
    private Event<EntityGraphPostPersist> entityGraphPostPersist;

    @Override
    public <T> void firePreEntity(T entity) {
        entityPrePersistEvent.fire(EntityPrePersist.of(entity));
    }

    @Override
    public <T> void firePostEntity(T entity) {
        entityPostPersistEvent.fire(EntityPostPersist.of(entity));
    }

    @Override
    public <T> void firePreGraphEntity(T entity) {
        entityGraphPrePersist.fire(EntityGraphPrePersist.of(entity));
    }

    @Override
    public <T> void firePostGraphEntity(T entity) {
        entityGraphPostPersist.fire(EntityGraphPostPersist.of(entity));
    }
}
