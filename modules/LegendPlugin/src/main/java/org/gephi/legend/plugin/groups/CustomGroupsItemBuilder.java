/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.groups;

import java.awt.Color;
import java.util.ArrayList;
import org.gephi.legend.spi.CustomLegendItemBuilder;
import org.gephi.preview.api.Item;

/**
 *
 * @author edubecks
 */
public interface CustomGroupsItemBuilder extends CustomLegendItemBuilder {

    /**
     * This function receives the objects that need to be filled in order to
     * build the item
     * @param labels
     * @param colors
     * @param values 
     */
    public void retrieveData(Item item, ArrayList<GroupElement> groups);

}