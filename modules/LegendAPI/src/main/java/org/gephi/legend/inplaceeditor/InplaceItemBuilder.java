/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceeditor;

import java.awt.Color;
import java.util.ArrayList;
import org.gephi.attribute.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.BlockNode;
import org.gephi.legend.spi.LegendItemBuilder;
import org.gephi.preview.api.Item;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author mvvijesh
 */
@ServiceProviders(value = {
    @ServiceProvider(service = ItemBuilder.class, position = 106),
    @ServiceProvider(service = InplaceItemBuilder.class, position = 106)
})
public class InplaceItemBuilder implements ItemBuilder {

    private Integer borderLineThickness = 3;
    private Color backgroundColor = new Color(0.5f, 0.5f, 0.5f, 1f);

    public InplaceItemBuilder() {
    }

    public InplaceEditor createInplaceEditor(Graph graph, BlockNode node) {
        InplaceEditor ipeditor = new InplaceEditor(graph);
        ipeditor.setData(InplaceEditor.RENDERER, InplaceItemRenderer.class);
        ipeditor.setData(InplaceEditor.BORDER_THICK, borderLineThickness);
        ipeditor.setData(InplaceEditor.BACKGROUND_COLOR, backgroundColor);
        ipeditor.setData(InplaceEditor.BLOCKNODE, node);
        return ipeditor;
    }

    @Override
    public Item[] getItems(Graph graph, AttributeModel attributeModel) {
        LegendModel legendModel = LegendController.getInstance().getLegendModel();
        ArrayList<Item> items = new ArrayList<Item>();
        InplaceEditor ipeditor = legendModel.getInplaceEditor();
        if (ipeditor != null) {
            items.add(ipeditor);
            return items.toArray(new Item[items.size()]);
        }

        return items.toArray(new Item[0]);
    }

    @Override
    public String getType() {
        return "inplace builder";
    }
}
