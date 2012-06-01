/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.LegendItemBuilder;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.items.TextItem;
import org.gephi.preview.api.Item;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = LegendItemBuilder.class, position = 90)
public class TextItemBuilder implements LegendItemBuilder{

    @Override
    public Item[] getItems(Graph graph, AttributeModel attributeModel) {
        LegendItem[] items = new TextItem[1];
        TextItem textItem = new TextItem(null);
        textItem.setData(TextItem.BODY, "Hello World!!!");
        textItem.setData(TextItem.DESCRIPTION, "");
        textItem.setData(TextItem.TITLE, "");
        
        return items;
    }

    @Override
    public String getType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

    
}
