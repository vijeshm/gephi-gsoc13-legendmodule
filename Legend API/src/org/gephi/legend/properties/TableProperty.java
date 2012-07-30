/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.properties;

import java.util.ArrayList;
import org.gephi.legend.api.LegendManager;

/**
 *
 * @author eduBecKs
 */
public class TableProperty{
    //TEXT 

    public static final int TABLE_FONT = 0;
    public static final int TABLE_FONT_COLOR = 1;
    public static final int TABLE_IS_CELL_COLORING = 2;
    public static final int TABLE_HORIZONTAL_TEXT_POSITION = 3;
    public static final int TABLE_HORIZONTAL_TEXT_ALIGNMENT = 4;
    public static final int TABLE_HORIZONTAL_EXTRA_MARGIN = 5;
    public static final int TABLE_VERTICAL_TEXT_POSITION = 6;
    public static final int TABLE_VERTICAL_TEXT_ALIGNMENT = 7;
    public static final int TABLE_VERTICAL_TEXT_ROTATION = 8;
    public static final int TABLE_VERTICAL_EXTRA_MARGIN = 9;
    public static final int TABLE_CELL_COLORING_DIRECTION = 10;
    public static final int TABLE_LABEL = 11;
    
    public static String[] OWN_PROPERTIES = {
        ".table.font",
        ".table.font.color",
        ".table.isCellColoring",
        ".table.horizontalText.position",
        ".table.horizontalText.alignment",
        ".table.horizontal.extraMargin",
        ".table.verticalText.position",
        ".table.verticalText.alignment",
        ".table.verticalText.rotation",
        ".table.vertical.extraMargin",
        ".table.cellColoringDirection",
        ".table.label"
    };
    
    
    public static String getLabelProperty(Integer itemIndex, int i) {
        ArrayList<String> tableProperties = LegendManager.getProperties(TableProperty.OWN_PROPERTIES, itemIndex);
        return tableProperties.get(TableProperty.TABLE_LABEL) + i;
    }
}
