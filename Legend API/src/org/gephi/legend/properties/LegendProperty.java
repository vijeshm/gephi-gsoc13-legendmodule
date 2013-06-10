/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.properties;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mvvijesh, eduBecKs
 */
public class LegendProperty {

    // LABEL
    public static final int LABEL = 0;
    public static final int ITEM = 1;
    // DISPLAY
    public static final int IS_DISPLAYING = 2;
    // LOCATION
    public static final int USER_ORIGIN_X = 3;
    public static final int USER_ORIGIN_Y = 4;
    // DIMENSIONS
    public static final int WIDTH = 5;
    public static final int HEIGHT = 6;
    // BACKGROUND
    public static final int BACKGROUND_IS_DISPLAYING = 7;
    public static final int BACKGROUND_COLOR = 8;
    public static final int BACKGROUND_BORDER_COLOR = 9;
    public static final int BACKGROUND_BORDER_LINE_THICK = 10;
    // TITLE
    public static final int TITLE_IS_DISPLAYING = 11;
    public static final int TITLE = 12;
    public static final int TITLE_FONT = 13;
    public static final int TITLE_FONT_COLOR = 14;
    public static final int TITLE_ALIGNMENT = 15;
    // DESCRIPTION
    public static final int DESCRIPTION_IS_DISPLAYING = 16;
    public static final int DESCRIPTION = 17;
    public static final int DESCRIPTION_FONT = 18;
    public static final int DESCRIPTION_FONT_COLOR = 19;
    public static final int DESCRIPTION_ALIGNMENT = 20;
    // PROPERTIES SET BY USER
    public static final int USER_LEGEND_NAME = 21;
    public static final String[] LEGEND_PROPERTIES = {
        // LABEL
        ".label", // 0 
        ".item", // 1
        // DISPLAY
        ".isDisplaying", // 2
        // LOCATION                              
        ".originX", // 3
        ".originY", // 4
        ".width", // 5
        ".height", // 6
        // BACKGROUND
        ".background.isDisplaying", // 7
        ".background.color", // 8
        ".background.border.color", // 9
        ".background.border.lineThick", // 10
        // TITLE
        ".title.isDisplaying", // 11
        ".title", // 12 
        ".title.font", // 13
        ".title.font.color", // 14
        ".title.alignment", // 15
        // DESCRIPTION
        ".description.isDisplaying", // 16
        ".description", // 17
        ".description.font", // 18
        ".description.font.color", // 19
        ".description.alignment", // 20
        // PROPERTIES SET BY USER
        ".user.legendName" // 21
    };
    
    public static final int[] LIST_OF_PROPERTIES = {
        LABEL,
        ITEM,
        IS_DISPLAYING,
        USER_ORIGIN_X,
        USER_ORIGIN_Y,
        WIDTH,
        HEIGHT,
        BACKGROUND_IS_DISPLAYING,
        BACKGROUND_COLOR,
        BACKGROUND_BORDER_COLOR,
        BACKGROUND_BORDER_LINE_THICK,
        TITLE_IS_DISPLAYING,
        TITLE,
        TITLE_FONT,
        TITLE_FONT_COLOR,
        TITLE_ALIGNMENT,
        DESCRIPTION_IS_DISPLAYING,
        DESCRIPTION,
        DESCRIPTION_FONT,
        DESCRIPTION_FONT_COLOR,
        DESCRIPTION_ALIGNMENT,
        USER_LEGEND_NAME
    };
    private static LegendProperty instance = new LegendProperty();
    private Map<String, Integer> propertyIndex;

    public int getProperty(String propertyName) {
        return propertyIndex.get(propertyName);
    }

    private LegendProperty() {
        propertyIndex = new HashMap<String, Integer>();
        for (int i = 0; i < LEGEND_PROPERTIES.length; i++) {
            propertyIndex.put(LEGEND_PROPERTIES[i], i);
        }
    }

    public static LegendProperty getInstance() {
        return instance;
    }
}
