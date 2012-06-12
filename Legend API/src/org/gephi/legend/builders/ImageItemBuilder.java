/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendManager;
import org.gephi.preview.api.Item;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.legend.items.ImageItem;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = ItemBuilder.class, position = 102)
public class ImageItemBuilder implements ItemBuilder{

    @Override
    public Item[] getItems(Graph graph, AttributeModel attributeModel) {
        Item[] items = new ImageItem[1];
        ImageItem imageItem = new ImageItem(graph);
        imageItem.setData(LegendItem.WORK_INDEX, LegendManager.useWorkIndex());
        imageItem.setData(LegendItem.ITEM_INDEX, LegendManager.useItemIndex());
        imageItem.setData(ImageItem.IMAGE, "/Users/edubecks/Dropbox/gsoc2012/gephi/gephi.communication/test2.png");
        items[0]=imageItem;
        
        return items;
    }

    @Override
    public String getType() {
        return ImageItem.TYPE;
    }
    
    
    
}
