/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders.text;

import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.api.CustomTableItemBuilder;
import org.gephi.legend.api.CustomTextItemBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service=CustomTextItemBuilder.class, position=1)
public class Default extends CustomLegendItemBuilder implements CustomTextItemBuilder{

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTitle() {
        return DEFAULT_TITLE;
    }


    @Override
    public String getText() {
        return "Random Text";
    }

    @Override
    public boolean isAvailableToBuild() {
        return true;
    }

    @Override
    public String stepsNeededToBuild() {
        return NONE_NEEDED;
    }
    
}
