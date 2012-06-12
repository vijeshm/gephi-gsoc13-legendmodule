/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.items;

import org.gephi.legend.api.LegendItem;
import org.gephi.preview.plugin.items.AbstractItem;

/**
 *
 * @author edubecks
 */
public class TableItem extends AbstractItem implements LegendItem{

    public static final String TYPE = "Table Legend";
    
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
    
    public enum Direction {

        UP("Up"),
        BOTTOM("Bottom"),
        RIGHT("Right"),
        LEFT("Left");
        private final String direction;

        private Direction(String direction) {
            this.direction = direction;
        }

        @Override
        public String toString() {
            return this.direction;
        }

    }
    
    public TableItem(Object source) {
        super(source, TYPE);
    }
    
    @Override
    public String toString() {
        return TYPE;
    }

}
