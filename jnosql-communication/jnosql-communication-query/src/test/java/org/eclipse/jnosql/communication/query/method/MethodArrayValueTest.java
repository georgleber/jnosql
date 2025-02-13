/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query.method;

import org.eclipse.jnosql.communication.query.ArrayQueryValue;
import org.eclipse.jnosql.communication.query.QueryValue;
import org.eclipse.jnosql.communication.query.ValueType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MethodArrayValueTest {

    @Test
    public void shouldReturnArrayType() {
        ArrayQueryValue array = MethodArrayValue.of("method");
        assertThat(array).isNotNull();
        ValueType type = array.type();
        assertThat(type).isEqualTo(ValueType.ARRAY);
    }

    @Test
    public void shouldReturnArrayValue() {
        ArrayQueryValue array = MethodArrayValue.of("name");
        assertThat(array.get()).isInstanceOf(QueryValue[].class);
    }

    @Test
    public void shouldEquals(){
        ArrayQueryValue array = MethodArrayValue.of("name");
        Assertions.assertEquals(array, array);
    }

    @Test
    public void shouldHashCode(){
        ArrayQueryValue array = MethodArrayValue.of("name");
        Assertions.assertEquals(array.hashCode(), array.hashCode());
    }
}