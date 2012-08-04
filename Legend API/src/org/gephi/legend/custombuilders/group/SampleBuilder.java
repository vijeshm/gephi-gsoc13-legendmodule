/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.custombuilders.group;

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
public class SampleBuilder extends CustomLegendItemBuilder implements CustomGroupsItemBuilder{

    @Override
    public String getDescription() {
        return "Sample Groups";
    }

    @Override
    public String getTitle() {
        return "Sample Groups";
    }

    @Override
    public void retrieveData(ArrayList<String> labels, ArrayList<Color> colors, ArrayList<Float> values) {
        
        // FILLING LABELS
        String group1 = "group 1";
        String group2 = "group 2";
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
