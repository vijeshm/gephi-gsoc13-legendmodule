/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.gephi.legend.api.LegendModel;

/**
 *
 * @author mvvijesh, edubecks
 */
public class TableProperty {
    public static final int TABLE_FONT = 0;
    public static final int TABLE_FONT_COLOR = 1;
    public static final int TABLE_FONT_ALIGNMENT = 2;
    public static final int TABLE_CELL_SPACING = 3;
    public static final int TABLE_CELL_PADDING = 4;
    public static final int TABLE_BORDER_SIZE = 5;
    public static final int TABLE_BORDER_COLOR = 6;
    public static final int TABLE_BACKGROUND_COLOR = 7;
    public static final int TABLE_WIDTH_FULL = 8;
    
    public static String[] OWN_PROPERTIES = {
        ".table.font",
        ".table.font.color",
        ".table.font.alignment",
        ".table.cell.spacing",
        ".table.cell.padding",
        ".table.border.size",
        ".table.border.color",
        ".table.background.color",
        ".table.width.full"
    };
    
    public static final int[] LIST_OF_PROPERTIES = {
        TABLE_FONT,
        TABLE_FONT_COLOR,
        TABLE_FONT_ALIGNMENT,
        TABLE_CELL_SPACING,
        TABLE_CELL_PADDING,
        TABLE_BORDER_SIZE,
        TABLE_BORDER_COLOR,
        TABLE_BACKGROUND_COLOR,
        TABLE_WIDTH_FULL
    };
    
    private static TableProperty instance = new TableProperty();
    private Map<String, Integer> propertyIndex;
    
    private TableProperty() {
        propertyIndex = new HashMap<String, Integer>();
        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            propertyIndex.put(OWN_PROPERTIES[i], i);
        }
    }
    
    public static TableProperty getInstance() {
        return instance;
    }
}