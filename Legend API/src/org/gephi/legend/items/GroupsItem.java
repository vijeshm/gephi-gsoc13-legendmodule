/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.gephi.legend.items;

import org.gephi.legend.api.LegendItem;
import org.gephi.preview.plugin.items.AbstractItem;

/**
 *
 * @author edubecks
 */
public class GroupsItem extends AbstractItem implements LegendItem {
    
    public static final String LEGEND_TYPE= "Legend Groups Item";
    public static final String LABELS_GROUP= "labels group";
    public static final String COLORS_GROUP= "colors group";
    
    

    //BODY


    public GroupsItem(Object source) {
        super(source, TYPE);
    }

}