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
public class ImageItem extends AbstractItem implements LegendItem {

        //BODY
    public static final String IMAGE_URL = "image";
    public static final String LEGEND_TYPE= "Image Item";

    public ImageItem(Object source) {
        super(source, LEGEND_TYPE);
    }

    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[LegendProperty.LABEL].getValue());
    }
    
    @Override
    public PreviewProperty[] getDynamicPreviewProperties() {
        return new PreviewProperty[0];
    }

}