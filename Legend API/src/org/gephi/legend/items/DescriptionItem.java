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
public class DescriptionItem extends AbstractItem implements LegendItem {

    public static final String LEGEND_TYPE= "Description Item";
    public static final String KEYS= "keys";
    public static final String VALUES= "values";
    
    
    public DescriptionItem(Object source) {
        super(source,TYPE);
    }

}