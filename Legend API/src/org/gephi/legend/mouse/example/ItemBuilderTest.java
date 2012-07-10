/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.mouse.example;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.preview.api.Item;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Eduardo Ramos<eduramiba@gmail.com>
 */
@ServiceProvider(service=ItemBuilder.class)
public class ItemBuilderTest implements ItemBuilder {

    public Item[] getItems(Graph graph, AttributeModel attributeModel) {
        return new Item[]{new ItemTest(graph, "mouse.test")};
    }

    public String getType() {
        return "mouse.test";
    }
}
