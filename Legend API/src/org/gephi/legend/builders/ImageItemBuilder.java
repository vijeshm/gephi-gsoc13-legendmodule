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
import org.gephi.preview.api.PreviewProperty;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = ItemBuilder.class, position = 102)
public class ImageItemBuilder extends LegendItemBuilder{

    @Override
    public String getType() {
        return ImageItem.TYPE;
    }

    @Override
    public Item buildItem(Graph graph, AttributeModel attributeModel) {
        ImageItem item = new ImageItem(graph);
        item.setData(ImageItem.IMAGE_URL, "/Users/edubecks/Dropbox/gsoc2012/gephi/gephi.communication/test2.png");
        return item;
    }

    @Override
    protected PreviewProperty[] createLegendItemProperties(Item item) {
        return new PreviewProperty[0];
    }
    
    
    
}
