/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import org.gephi.preview.plugin.items.AbstractItem;

/**
 *
 * @author edubecks
 */
public abstract class LegendItem extends AbstractItem {

    public static final String TYPE = "Legend Item";
    
    public static final String WIDTH =  "width";
    public static final String HEIGHT =  "height";
    //font
    public static final String FONT_SIZE = "font size";
    public static final String FONT_TYPE = "font type";
    public static final String FONT_STYLE = "font style";
    
    //description
    public static final String DESCRIPTION = "description";
    public static final String DESCRIPTION = "description";
    

    public LegendItem(LegendItem source) {
        super(source, TYPE);
    }

}
