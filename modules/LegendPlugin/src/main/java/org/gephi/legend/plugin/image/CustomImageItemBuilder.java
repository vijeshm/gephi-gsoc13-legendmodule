package org.gephi.legend.plugin.image;

import org.gephi.legend.spi.CustomLegendItemBuilder;

/**
 * interface that the custom image item builders must implement.
 *
 * This is required by the legend item builders when they need extraneous data
 * to be included in the items. This extraneous data will be used by the
 * renderers to render them appropriately. Every legend plugin must have atleast
 * one (Default.java) custom legend item builder.
 *
 * @author mvvijesh, edubecks
 */
public interface CustomImageItemBuilder extends CustomLegendItemBuilder {
    //nothing new is being added
}
