/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders.image;

import org.gephi.legend.api.CustomImageItemBuilder;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service=CustomImageItemBuilder.class, position=1)
public class Default extends CustomLegendItemBuilder implements CustomImageItemBuilder{

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTitle() {
        return DEFAULT_TITLE;
    }

    @Override
    public boolean isAvailableToBuild() {
        return true;
    }

    @Override
    public String stepsNeededToBuild() {
        return NONE_NEEDED;
    }

    @Override
    public void retrieveData() {
        
    }
    
}
