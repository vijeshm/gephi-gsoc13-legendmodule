/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.preview.api.Item;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.legend.items.GroupsItem;
import org.gephi.partition.api.NodePartition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.api.PartitionModel;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */

@ServiceProvider(service = ItemBuilder.class, position = 103)
public class GroupsItemBuilder implements ItemBuilder{

    @Override
    public Item[] getItems(Graph graph, AttributeModel attributeModel) {
        PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
        PartitionModel model = partitionController.getModel();
        for (NodePartition partition : model.getNodePartitions()) {
            // get groups
        }
        Item[] items = new Item[0];
        GroupsItem item = new GroupsItem(graph);
        items[0] =  item;
        return items;
        
    }

    @Override
    public String getType() {
        return GroupsItem.TYPE;
    }
    
}
