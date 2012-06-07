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
public class TextItem extends AbstractItem implements LegendItem {

    //BODY
    public static final String BODY = "body";
    public static final String TYPE= "Legend Text Item";

    public TextItem(Object source) {
        super(source, TYPE);
    }

}
