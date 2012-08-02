/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.api.CustomTableItemBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = CustomTableItemBuilder.class, position = 2)
public class TableSampleBuilder extends CustomLegendItemBuilder implements CustomTableItemBuilder {

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTitle() {
        return "Sample";
    }

 
    @Override
    public void retrieveData(ArrayList<StringBuffer> labels, ArrayList<StringBuffer> horizontalLabels, ArrayList<StringBuffer> verticalLabels, ArrayList<Color> colors, ArrayList<ArrayList<Float>> values) {
        // LABELS ID
        StringBuffer label1 = new StringBuffer("Label 1");
        StringBuffer label2 = new StringBuffer("Label 2");
        StringBuffer value = new StringBuffer("Value");
        labels.add(label1);
        labels.add(label2);
        
        // HORIZONTAL LABELS
        horizontalLabels.add(label1);
        horizontalLabels.add(label2);
        
        // VERTICAL LABELS
        verticalLabels.add(value);
        
        ArrayList<Float> row = new ArrayList<Float>();
        row.add(1f);
        row.add(0.5f);
        values.add(row);
    }

}
