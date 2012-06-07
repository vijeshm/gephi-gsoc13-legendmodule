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
    
    public static final String TYPE= "Legend Groups Item";

    //BODY
//    public static final String NUMBER_ROWS = "number of rows";
    public static final String NUMBER_COLUMNS = "number of columns";
    public static final String SHAPE = "shape";
    public static final String LEGEND_ARRANGMENT = "legend's arrangment";

    public GroupsItem(Object source) {
        super(source, TYPE);
    }

}