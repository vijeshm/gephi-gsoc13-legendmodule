/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders.group;

import java.awt.Color;
import java.util.ArrayList;
import org.gephi.legend.api.CustomGroupsItemBuilder;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service=CustomGroupsItemBuilder.class, position=100)
public class Default extends CustomLegendItemBuilder implements CustomGroupsItemBuilder{

    @Override
    public String getDescription() {
        return "Sample Groups";
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
    public void retrieveData(ArrayList<StringBuilder> labels, ArrayList<Color> colors, ArrayList<Float> values) {
        // FILLING LABELS
        StringBuilder group1 = new StringBuilder("group 1");
        StringBuilder group2 = new StringBuilder("group 2");
        labels.add(group1);
        labels.add(group2);
        
        // FILLING COLORS
        Color color1 = Color.RED;
        Color color2 = Color.BLUE;
        colors.add(color1);
        colors.add(color2);
        
        // FILLING VALUES
        Float value1=0.8f;
        Float value2=0.4f;
        values.add(value1);
        values.add(value2);
    }
    
}
