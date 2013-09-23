package org.gephi.legend.plugin.groups;

import java.util.ArrayList;
import org.gephi.legend.spi.CustomLegendItemBuilder;

/**
 * interface that the custom group item builders must implement.
 *
 * This is required by the legend item builders when they need extraneous data
 * to be included in the items. This extraneous data will be used by the
 * renderers to render them appropriately. Every legend plugin must have atleast
 * one (Default.java) custom legend item builder.
 *
 * @author mvvijesh, edubecks
 */
public interface CustomGroupsItemBuilder extends CustomLegendItemBuilder {

    /**
     *
     * @param item - the item being built
     * @return array list of group elements. These group elements contain the
     * extraneous data fetch from other modules.
     */
    public ArrayList<GroupElement> retrieveData(GroupsItem item);
}