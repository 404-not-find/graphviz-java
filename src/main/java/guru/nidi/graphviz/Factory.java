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

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Factory {
    private Factory() {
    }

    public static Graph graph() {
        return Graph.nameless();
    }

    public static Graph graph(String name) {
        return Graph.named(name);
    }

    public static Graph graph(Name name) {
        return Graph.named(name);
    }

    public static Node node(String name) {
        return Node.named(name);
    }

    public static Node node(Name name) {
        return Node.named(name);
    }

    public static NodePoint compass(Compass compass) {
        return NodePoint.ofCompass(compass);
    }

    public static NodePoint record(String record) {
        return NodePoint.ofRecord(record);
    }

    public static Name name(String name) {
        return Name.of(name);
    }

    public static Name html(String html) {
        return Name.html(html);
    }

    public static Link to(Node node) {
        return Link.to(node);
    }

    public static Link to(LinkTarget node) {
        return Link.to(node);
    }

    public static Link between(LinkTarget from, LinkTarget to) {
        return Link.between(from, to);
    }

    public static Link between(LinkTarget from, Node to) {
        return between(from, NodePoint.of(to));
    }

    public static Map<String, Object> attrs(Object... keysAndValues) {
        final Map<String, Object> res = new HashMap<>();
        for (int i = 0; i < keysAndValues.length; i++) {
            if (keysAndValues[i] instanceof Attribute) {
                ((Attribute) keysAndValues[i]).apply(res);
            } else if (keysAndValues[i] instanceof Map) {
                res.putAll((Map<String, Object>) keysAndValues[i]);
            } else if (!(keysAndValues[i] instanceof String)) {
                throw new IllegalArgumentException(i + "th argument '" + keysAndValues[i] + "' is a key, but not a string");
            } else {
                if (i == keysAndValues.length - 1) {
                    throw new IllegalArgumentException("Last key '" + keysAndValues[i] + "' has no value");
                }
                res.put((String) keysAndValues[i], keysAndValues[i + 1]);
                i++;
            }
        }
        return res;
    }
}
