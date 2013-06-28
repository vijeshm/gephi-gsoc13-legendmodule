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
import org.gephi.legend.api.blockNode;
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
    @ServiceProvider(service = inplaceItemBuilder.class, position = 106)
})
public class inplaceItemBuilder implements ItemBuilder {

    private Integer borderLineThickness = 3;
    private Color backgroundColor = new Color(0.5f, 0.5f, 0.5f, 1f);

    public inplaceItemBuilder() {
    }

    public inplaceEditor createInplaceEditor(Graph graph, blockNode node) {
        inplaceEditor ipeditor = new inplaceEditor(graph);
        ipeditor.setData(inplaceEditor.RENDERER, inplaceItemRenderer.class);
        ipeditor.setData(inplaceEditor.BORDER_THICK, borderLineThickness);
        ipeditor.setData(inplaceEditor.BACKGROUND_COLOR, backgroundColor);
        ipeditor.setData(inplaceEditor.BLOCKNODE, node);
        // the gap between the block and its inplace editor must be atleast half the width of the anchor to avoid overlapping.
        // By default, its set to 0. Hence, this property must be reset in the AbstractLegendItemRenderer's render method, since it is a private property.
        ipeditor.setData(inplaceEditor.BLOCK_INPLACEEDITOR_GAP, 0);
        return ipeditor;
    }

    @Override
    public Item[] getItems(Graph graph, AttributeModel attributeModel) {
        LegendModel legendModel = LegendController.getInstance().getLegendModel();
        ArrayList<Item> items = new ArrayList<Item>();
        inplaceEditor ipeditor = legendModel.getInplaceEditor();
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
