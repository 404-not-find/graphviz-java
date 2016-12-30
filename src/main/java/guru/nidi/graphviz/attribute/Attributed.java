/*
 * Copyright (C) 2015 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.graphviz.attribute;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;

public interface Attributed<T> extends Attribute {

    default T with(String name, Object value) {
        return with(singletonMap(name, value));
    }

    default T with(Object... keysAndValues) {
        return with(Attribute.from(keysAndValues));
    }

    default T with(Attribute attribute) {
        final Map<String, Object> attrs = new HashMap<>();
        attribute.applyTo(attrs);
        return with(attrs);
    }

    T with(Map<String, Object> attrs);

}
