/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.api.Item;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */

public interface LegendItemBuilder extends ItemBuilder{
    
        /**
     * Build items from the <code>graph</code> and <code>attributeModel</code>.
     * @param graph the graph to build items from
     * @param attributeModel the attribute model associated to the graph
     * @return an array of new items, from the same type returned by {@link #getType()}
     */
    public Item[] getItems(Graph graph, AttributeModel attributeModel);

    /**
     * Returns the type of this builder. 
     * <p>
     * The type should <b>always</b> match
     * the type of <code>Item</code> the builder is building. For instance if the
     * builder is building <code>Item.Node</code> type, this method should return
     * <code>Item.Node</code>.
     * @return the builder item type.
     */
    public String getType();
    
}
