/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.items.TextItem;
import org.gephi.preview.api.Item;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = ItemBuilder.class, position = 90)
public class TextItemBuilder implements ItemBuilder{

    @Override
    public Item[] getItems(Graph graph, AttributeModel attributeModel) {
        Item[] items = new TextItem[1];
        TextItem textItem = new TextItem(graph);
        textItem.setData(TextItem.BODY, "Hello World!!!");
        textItem.setData(TextItem.TITLE, "Title");
        textItem.setData(TextItem.DESCRIPTION, "Description");
        items[0] = textItem;
        return items;
    }

    @Override
    public String getType() {
        return TextItem.TYPE;
    }
    
    
    

    
}
