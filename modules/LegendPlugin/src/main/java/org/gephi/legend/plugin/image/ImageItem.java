/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.gephi.legend.plugin.image;

import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.api.AbstractItem;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.PreviewProperty;


/**
 *
 * @author mvvijesh, edubecks
 */

public class ImageItem extends AbstractItem implements LegendItem {
    public static final String IMAGE_URL = "Image";
    public static final String LEGEND_TYPE = "Image Item";
    
    public ImageItem(Object source) {
        super(source, LEGEND_TYPE);
    }
    
    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[LegendProperty.LABEL].getValue());
    }
}