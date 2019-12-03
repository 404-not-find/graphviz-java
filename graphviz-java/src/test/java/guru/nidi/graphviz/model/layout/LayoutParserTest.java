/*
 * Copyright © 2015 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.graphviz.model.layout;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static guru.nidi.graphviz.attribute.GraphAttr.*;
import static guru.nidi.graphviz.engine.Format.*;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LayoutParserTest {
    @Test
    void simple() throws IOException {
        final int pad = 10;
        final int dpi = 72; //TODO
        final int margin = 0; //TODO
        final MutableGraph g = (MutableGraph) graph("ex7").cluster().graphAttr().with(dpi(dpi), pad(pad / 72.0), margin(margin / 72.0))
                .with(
                        graph().cluster()
                                .nodeAttr().with(Style.FILLED, Color.WHITE)
                                .graphAttr().with(Style.FILLED, Color.LIGHTGREY, Label.of("process #1"))
                                .with(node("a0").link(node("a1").link(node("a2").link(node("a3"))))),
                        graph("x").cluster()
                                .nodeAttr().with(Style.FILLED)
                                .graphAttr().with(Color.BLUE, Label.of("process #2"))
                                .with(node("b0").link(node("b1").link(node("b2").link(node("b3"))))),
                        node("start").with(Shape.mDiamond("", "")).link("a0", "b0"),
                        node("a1").link("b3"),
                        node("b2").link("a3"),
                        node("a3").link("a0"),
                        node("a3").link("end"),
                        node("b3").link("end"),
                        node("end").with(Shape.mSquare("", ""))
                );
        final File file = new File("target/ex7.json");
        final Graphviz viz = Graphviz.fromGraph(g);//.width(500);
        viz.render(JSON).toFile(file);
        viz.render(PNG).toFile(new File("target/ex7"));
        viz.render(SVG).toFile(new File("target/ex7"));
        Graphviz.fromGraph(g.graphAttrs().add("splines", "ortho")).render(JSON).toFile(new File("target/ex7o"));

        final Collection<MutableNode> nodes = g.nodes();
        final Collection<Link> edges = g.edges();
        try (final InputStream in = new FileInputStream(file)) {
            LayoutParser.applyLayoutToGraph(IOUtils.toString(in, StandardCharsets.UTF_8), (MutableGraph) g);
        }

        final int width = LayoutAttributes.widthOf(g);
        final int height = LayoutAttributes.heightOf(g);
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D gr = image.createGraphics();
        gr.setColor(java.awt.Color.BLACK);
        gr.setStroke(new BasicStroke(2));
        gr.draw(LayoutAttributes.outlineOf(g).toShape());
        for (final MutableNode node : nodes) {
            gr.draw(LayoutAttributes.outlineOf(node).toShape());
        }
        for (final MutableGraph graph : g.graphs()) {
            gr.draw(LayoutAttributes.outlineOf(graph).toShape());
        }
        for (final Link edge : edges) {
            gr.draw(LayoutAttributes.outlineOf(edge).toShape());
        }
        ImageIO.write(image, "png", new File("target/draw.png"));
        assertTrue(true);
    }
}
