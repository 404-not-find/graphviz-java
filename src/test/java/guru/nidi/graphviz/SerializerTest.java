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

import org.junit.After;
import org.junit.Test;

import static guru.nidi.graphviz.Compass.*;
import static guru.nidi.graphviz.Factory.*;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class SerializerTest {
    @After
    public void closeContext() {
        CreationContext.end();
    }

    @Test
    public void simple() {
        assertGraph("graph {\n}", graph());
    }

    @Test
    public void directed() {
        assertGraph("digraph 'x' {\n}", graph("x").directed());
    }

    @Test
    public void strict() {
        assertGraph("strict graph 'x' {\n}", graph("x").strict());
    }

    @Test
    public void escapeName() {
        assertGraph("graph 'b\\'la' {\n}", graph("b\"la"));
    }

    @Test
    public void htmlName() {
        assertGraph("graph <bla> {\n}", graph(html("bla")));
    }

    @Test
    public void graphAttr() {
        assertGraph("graph 'x' {\ngraph ['bla'='blu']\n}", graph("x").attr("bla", "blu"));
    }

    @Test
    public void nodes() {
        assertGraph("graph 'x' {\n'x' ['bla'='blu']\n}", graph("x")
                .with(node("x").attr("bla", "blu")));
    }

    @Test
    public void context() {
        final CreationContext ctx = CreationContext.begin();
        ctx.graphs().attrs("g", "x");
        ctx.nodes().attrs("n", "y");
        ctx.links().attrs("l", "z");
        assertGraph("graph 'x' {\ngraph ['g'='x']\n'x' ['n'='y','bla'='blu']\n'y' ['n'='y']\n'x' -- 'y' ['l'='z']\n}", graph("x")
                .with(node("x").attr("bla", "blu").link(to(node("y")))));
    }

    @Test
    public void subgraph() {
        assertGraph("graph 'x' {\nsubgraph 'x' {\n'x' ['bla'='blu']\n}\n}", graph("x")
                .with(graph("x").with(node("x").attr("bla", "blu"))));
    }

    @Test
    public void namelesSubgraph() {
        assertGraph("graph 'x' {\n{\n'x' ['bla'='blu']\n}\n}", graph("x")
                .with(graph().with(node("x").attr("bla", "blu"))));
    }

    @Test
    public void simpleEdge() {
        assertGraph("graph 'x' {\n'x' -- 'y'\n}", graph("x")
                .with(node("x").link(to(node("y")))));
    }

    @Test
    public void attrEdge() {
        assertGraph("graph 'x' {\n'x' -- 'y' ['bla'='blu']\n}", graph("x")
                .with(node("x").link(to(node("y")).attr("bla", "blu"))));
    }

    @Test
    public void graphEdgeStart() {
        assertGraph("graph 'x' {\nsubgraph 'y' {\n'z' -- 'a'\n} -- 'x':n\n}", graph("x").with(
                graph("y").with(node("z").link(to(
                        node("a")))).link(to(node("x").compass(N)))));
    }

    @Test
    public void graphEdgeEnd() {
        assertGraph("graph 'x' {\n'x':n -- subgraph 'y' {\n'z' -- 'a'\n}\n}", graph("x").with(
                node("x").link(between(compass(N),
                        graph("y").with(node("z").link(to(node("a"))))))));
    }

    @Test
    public void graphEdge() {
        assertGraph("graph 'x' {\nsubgraph 'y' {\n'z' -- 'a'\n} -- subgraph 'y2' {\n'z2' -- 'a2'\n}\n}", graph("x").with(
                graph("y").with(node("z").link(to(node("a")))).link(to(
                        graph("y2").with(node("z2").link(to(node("a2"))))))));
    }

    @Test
    public void compassEdge() {
        assertGraph("graph 'x' {\n'x':sw -- 'y':ne\n}", graph("x")
                .with(node("x").link(between(compass(SW), node("y").compass(NE)))));
    }

    @Test
    public void recordEdge() {
        assertGraph("graph 'x' {\n'x':'r1' -- 'y':'r2'\n}", graph("x")
                .with(node("x").link(between(record("r1"), node("y").record("r2")))));
    }

    @Test
    public void compassRecordEdge() {
        assertGraph("graph 'x' {\n'x':'r1':sw -- 'y':'r2':ne\n}", graph("x")
                .with(node("x").link(
                        between(record("r1").compass(SW), node("y").record("r2").compass(NE)))));
    }

    @Test
    public void complexEdge() {
        assertGraph("digraph 'x' {\n'x' -> 'y'\n'y' -> 'z'\n'a' -> 'x'\n}", graph("x").directed()
                .with(
                        node("x").link(to(node("y").link(to(node("z"))))))
                .with(
                        node("a").link(to(node("x")))));
    }

    private void assertGraph(String expected, Graph graph) {
        assertEquals(expected.replace("'", "\""), new Serializer(graph).serialize());
    }
}
