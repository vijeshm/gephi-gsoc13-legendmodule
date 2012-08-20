/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.items;

import org.gephi.legend.properties.LegendProperty;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.plugin.items.AbstractItem;

/**
 *
 * @author edubecks
 */
public class DescriptionItem extends AbstractItem implements LegendItem {

    public static final String LEGEND_TYPE = "Description Item";
    public static final String KEY = "keys";
    public static final String VALUE = "values";
    public static final String NUMBER_OF_ITEMS = "number of items";

    public DescriptionItem(Object source) {
        super(source, LEGEND_TYPE);
    }

    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[LegendProperty.LABEL].getValue());
    }
    
    @Override
    public PreviewProperty[] getDynamicPreviewProperties() {
        return (PreviewProperty[]) this.getData(DYNAMIC_PROPERTIES);
    }

}