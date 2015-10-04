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
package guru.nidi.graphviz;

import guru.nidi.graphviz.attribute.Attribute;
import guru.nidi.graphviz.attribute.Attributes;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Attributed<T> implements Attribute {
    final Map<String, Object> attributes = new HashMap<>();

    public T attr(String name, Object value) {
        attributes.put(name, value);
        return (T) this;
    }

    public T attrs(Map<String, Object> attrs) {
        attributes.putAll(attrs);
        return (T) this;
    }

    public T attrs(Object... keysAndValues) {
        return (T) attrs(Attributes.from(keysAndValues));
    }

    @Override
    public void apply(Map<String, Object> attrs) {
        attrs.putAll(attributes);
    }
}
