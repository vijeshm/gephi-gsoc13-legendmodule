/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.items.TextItem;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendManager;
import org.gephi.preview.api.Item;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = ItemBuilder.class, position = 101)
public class TextItemBuilder  implements ItemBuilder{

    @Override
    public Item[] getItems(Graph graph, AttributeModel attributeModel) {
        Item[] items = new TextItem[1];
        TextItem textItem = new TextItem(graph);
        textItem.setData(LegendItem.WORK_INDEX, LegendManager.useWorkIndex());
        textItem.setData(LegendItem.ITEM_INDEX, LegendManager.useItemIndex());
        textItem.setData(TextItem.BODY, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In venenatis nibh eget dolor accumsan rhoncus. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nullam nec felis leo, eget placerat eros. Curabitur eros erat, vulputate nec laoreet sed, aliquam ac sem. Nullam sollicitudin, dui eu placerat pulvinar, odio lacus molestie neque, sagittis commodo dui enim luctus est. Etiam quis orci felis, a tristique enim. Phasellus placerat est suscipit nisi dapibus non sollicitudin massa lobortis. Vestibulum ac malesuada diam.");
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
