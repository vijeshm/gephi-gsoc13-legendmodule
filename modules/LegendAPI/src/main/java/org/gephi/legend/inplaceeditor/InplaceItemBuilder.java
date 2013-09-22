package org.gephi.legend.inplaceeditor;

import java.awt.Color;
import java.util.ArrayList;
import org.gephi.attribute.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.BlockNode;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.preview.api.Item;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * builder for inplace editor items.
 *
 * This class is exposed as a service. The createInplaceEditor() method will
 * create the inplace editor item and return it. The inplace editor which
 * BlockNode it is associated with. But the reverse mapping must be done in the
 * counter part of the code, where the inplace editor items are populated.
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

    /**
     *
     * @param graph - the graph to which the inplace item belongs to
     * @param node - the block node to be associated with the inplace editor
     * item
     * @return newly created inplace editor the inplace editor item knows which
     * BlockNode it belongs to, but the BlockNode should update its field after
     * the editor items have been populated.
     */
    public InplaceEditor createInplaceEditor(Graph graph, BlockNode node) {
        InplaceEditor ipeditor = new InplaceEditor(graph);
        ipeditor.setData(InplaceEditor.RENDERER, InplaceItemRenderer.class);
        ipeditor.setData(InplaceEditor.BORDER_THICK, borderLineThickness);
        ipeditor.setData(InplaceEditor.BACKGROUND_COLOR, backgroundColor);
        ipeditor.setData(InplaceEditor.BLOCKNODE, node);
        return ipeditor;
    }

    /**
     *
     * @param graph
     * @param attributeModel
     * @return all the inplace editor items
     */
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
