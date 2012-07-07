/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.items;

import org.gephi.legend.api.LegendItem;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.plugin.items.AbstractItem;

/**
 *
 * @author edubecks
 */
public class TableItem extends AbstractItem implements LegendItem {

    public static final String LEGEND_TYPE = "Table Legend";
    //data
    public static final String TABLE_VALUES = "table values";
    public static final String LABELS = "labels";
    public static final String LIST_OF_COLORS = "list of colors";
//    
//    //coloring
//    public static final String IS_CELL_COLORING = "is cell coloring";
//    public static final String CELL_COLORING = "cell coloring";
//    public static final String CELL_SIZE_WIDTH = "cell size width";
//    public static final String CELL_SIZE_HEIGHT = "cell size height";
//    public static final String BACKGROUND = "background";
//    public static final String HORIZONTAL_ALIGNMENT = "horizontal alignment";
//    public static final String HORIZONTAL_TEXT_ALIGNMENT = "horizontal text alignment";
//    public static final String VERTICAL_ALIGNMENT = "vertical alignment";
//    public static final String VERTICAL_TEXT_DIRECTION = "vertical text direction";
//    
//    //font
//    public static final String FONT = "font";
//    
//    //margins
//    public static final String VERTICAL_EXTRA_MARGIN = "vertical extra margin";
//    public static final String HORIZONTAL_EXTRA_ALIGNMENT = "horizontal extra alignment";
//    public static final String MINIMUM_MARGIN = "minimum margin";

    public enum VerticalTextDirection {

        // anti clockwise
        UP(-90d),
        DOWN(-90d),
        DIAGONAL(-45d);
        private final double rotationAngle;

        private VerticalTextDirection(double rotationAngle) {
            this.rotationAngle = rotationAngle;
        }

        public double rotationAngle() {
            return Math.toRadians(rotationAngle);
        }

    }

    public TableItem(Object source) {
        super(source, TYPE);
    }

    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[0].getValue()) + " [" + LEGEND_TYPE + "]";
    }

    @Override
    public PreviewProperty[] getDynamicPreviewProperties() {
        return new PreviewProperty[0];
    }
}
