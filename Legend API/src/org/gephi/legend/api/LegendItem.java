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
    
    //DIMENSIONS
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    
    //LOCATION
    public static final String ORIGIN = "origin";
    
    
    //TITLE
    public static final String TITLE = "title";

    
    //DESCRIPTION
    public static final String DESCRIPTION = "description";

    

    public LegendItem(Object source) {
        super(source, TYPE);
    }

}
