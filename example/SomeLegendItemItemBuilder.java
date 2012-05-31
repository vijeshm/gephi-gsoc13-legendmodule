package example;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.preview.api.Item;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service=ItemBuilder.class)
public class SomeLegendItemItemBuilder implements ItemBuilder {

    public Item[] getItems(Graph graph, AttributeModel attributeModel) {
        //Build a number of SomeLegendItem instances, so they are later rendered with SomeLegendItemRenderer
        //In this case we return just one:
        SomeLegendItem item = new SomeLegendItem(graph/*Actually, anything can be passed here to identify as the source for this item*/);
        
        //Put some data based on current graph, attributes, anything...
        //Here is where ideas to build different legend items and setting them up with the graph happens
        item.setData("nodes-count", graph.getNodeCount());
        //item.setData("some-property", someValue);
        
        return new Item[]{item};
    }

    public String getType() {
        return SomeLegendItem.TYPE;
    }
    
}
