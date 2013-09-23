package org.gephi.legend.plugin.table;

import org.gephi.legend.spi.CustomLegendItemBuilder;

/**
 * interface that the custom table item builders must implement.
 *
 * This is required by the legend item builders when they need extraneous data
 * to be included in the items. This extraneous data will be used by the
 * renderers to render them appropriately. Every legend plugin must have atleast
 * one (Default.java) custom legend item builder.
 *
 * @author mvvijesh, edubecks
 */
public interface CustomTableItemBuilder extends CustomLegendItemBuilder {

    /**
     * the rows and columns is expected to added in accordance with the
     * extraneous data fetched from other modules.
     *
     * @param item - the item being built
     */
    public void populateTable(TableItem item);
}