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

import org.junit.Test;

import java.io.File;
import java.util.Map;

import static guru.nidi.graphviz.Factory.*;
import static guru.nidi.graphviz.Link.to;

/**
 *
 */
public class ExampleTest {
    @Test
    public void ex11() {
        new NodeContext(() -> {
            final Graph g = graph("ex1").directed()
                    .with(node("main").links(to(node("parse")), to(node("init")), to(node("cleanup")), to(node("printf"))))
                    .with(node("parse").link(to(node("execute"))))
                    .with(node("execute").links(to(node("make_string")), to(node("printf")), to(node("compare"))))
                    .with(node("init").link(to(node("make_string"))));
            Graphviz.fromGraph(g).renderToFile(new File("target/ex11.png"), "png", 300, 300);
        });
    }

    @Test
    public void ex12() {
        final Node
                printf = node("printf"),
                make_string = node("make_string");
        final Graph g = graph("ex1").directed()
                .with(node("main").links(
                        to(node("parse")
                                .link(to(node("execute")
                                        .links(to(make_string), to(printf), to(node("compare")))))),
                        to(node("init")
                                .link(to(make_string))),
                        to(node("cleanup")),
                        to(printf)));
        Graphviz.fromGraph(g).renderToFile(new File("target/ex12.png"), "png", 300, 300);
    }

    @Test
    public void ex2() {
        final Node
                main = node("main").attr("shape", "box"),
                parse = node("parse"),
                init = node("init"),
                execute = node("execute"),
                compare = node("compare").attrs("shape", "box", "style", "filled", "color", ".7 .3 1.0");
        final Map<String, Object> red = attrs("color", "red");
        final Graph g = graph("ex2").directed().attr("size", "4,4")
                .with(main.links(
                        to(parse).attr("weight", 8),
                        to(init).attr("style", "dotted"),
                        to(node("cleanup")),
                        to(node("printf")).attr("style", "bold").attr("label", "100 times").attrs(red)))
                .with(parse.link(to(execute)))
                //.with(execute)
                .with(init.link(to(node("make_string").attr("label", "make a\nstring"))))
                .with(execute.link(to(compare).attrs(red)));
        System.out.println(new Serializer(g).serialize());
        Graphviz.fromGraph(g).renderToFile(new File("target/ex2.png"), "png", 300, 300);

    }
}
