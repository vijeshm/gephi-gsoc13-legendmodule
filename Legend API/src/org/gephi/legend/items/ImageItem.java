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
public class ImageItem extends AbstractItem implements LegendItem {

        //BODY
    public static final String IMAGE = "image";
    public static final String TYPE= "Image Item";

    public ImageItem(Object source) {
        super(source, TYPE);
    }

    @Override
    public String toString() {
        return TYPE;
    }

}