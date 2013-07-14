/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.image;

import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author mvvijesh, edubecks
 */
@ServiceProvider(service = CustomImageItemBuilder.class, position = 1)
public class Default implements CustomImageItemBuilder {    
    
    @Override
    public String stepsNeededToBuild() {
        return NONE_NEEDED;
    }
    
    @Override
    public boolean isAvailableToBuild() {
        return true;
    }
    
    @Override
    public String getTitle() {
        return DEFAULT_TITLE;
    }
    
    @Override
    public String getDescription() {
        return DEFAULT_DESCRIPTION;
    }
}
