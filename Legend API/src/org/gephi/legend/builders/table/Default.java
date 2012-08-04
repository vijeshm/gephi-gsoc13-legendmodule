/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders.table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.api.CustomTableItemBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = CustomTableItemBuilder.class, position = 2)
public class Default extends CustomLegendItemBuilder implements CustomTableItemBuilder {

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTitle() {
        return "Sample";
    }


    @Override
    public void retrieveData(ArrayList<StringBuilder> labels, ArrayList<StringBuilder> horizontalLabels, ArrayList<StringBuilder> verticalLabels, ArrayList<ArrayList<Float>> values, ArrayList<Color> horizontalColors, ArrayList<Color> verticalColors, ArrayList<ArrayList<Color>> valueColors) {
        // LABELS ID
        StringBuilder label1 = new StringBuilder("Label 1");
        StringBuilder label2 = new StringBuilder("Label 2");
        StringBuilder value = new StringBuilder("Value");
        labels.add(label1);
        labels.add(label2);
        
        // VERTICAL LABELS
        verticalLabels.add(label1);
        verticalLabels.add(label2);
        
        // VERTICAL COLORS
        Color color1 = Color.BLACK;
        verticalColors.add(color1);
        verticalColors.add(color1);
        
        // HORIZONTAL LABELS
        horizontalLabels.add(value);
        
        // HORIZONTAL COLORS
        Color color2 = Color.RED;
        horizontalColors.add(color2);
        
        // VALUES
        ArrayList<Float> row = new ArrayList<Float>();
        row.add(1f);
        row.add(0.5f);
        values.add(row);
        
        // VALUES COLOR
        Color color3 = Color.GREEN;
        ArrayList<Color> colorsRow = new ArrayList<Color>();
        colorsRow.add(color3);
        colorsRow.add(color3);
        valueColors.add(colorsRow);
        
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
