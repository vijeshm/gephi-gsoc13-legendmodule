package org.gephi.legend.api;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mvvijesh, edubecks
 */
public class LegendProperty {

    // label
    public static final int LABEL = 0;
    // display
    public static final int IS_DISPLAYING = 1;
    // location
    public static final int USER_ORIGIN_X = 2;
    public static final int USER_ORIGIN_Y = 3;
    // dimension
    public static final int WIDTH = 4;
    public static final int HEIGHT = 5;
    // background
    public static final int BACKGROUND_IS_DISPLAYING = 6;
    public static final int BACKGROUND_COLOR = 7;
    public static final int BORDER_IS_DISPLAYING = 8;
    public static final int BORDER_COLOR = 9;
    public static final int BORDER_LINE_THICK = 10;
    // title
    public static final int TITLE_IS_DISPLAYING = 11;
    public static final int TITLE = 12;
    public static final int TITLE_FONT = 13;
    public static final int TITLE_FONT_COLOR = 14;
    public static final int TITLE_ALIGNMENT = 15;
    // description
    public static final int DESCRIPTION_IS_DISPLAYING = 16;
    public static final int DESCRIPTION = 17;
    public static final int DESCRIPTION_FONT = 18;
    public static final int DESCRIPTION_FONT_COLOR = 19;
    public static final int DESCRIPTION_ALIGNMENT = 20;
    // properties set by user
    public static final int USER_LEGEND_NAME = 21;
    public static final String[] LEGEND_PROPERTIES = {
        // label
        ".label", // 0 
        // display
        ".isDisplaying", // 1
        // LOCATION                              
        ".originX", // 2
        ".originY", // 3
        // dimension
        ".width", // 4
        ".height", // 5
        // background
        ".background.isDisplaying", // 6
        ".background.color", // 7
        // border
        ".border.isDisplaying", // 8
        ".border.color", // 9
        ".border.lineThick", // 10
        // title
        ".title.isDisplaying", // 11
        ".title", // 12 
        ".title.font", // 13
        ".title.font.color", // 14
        ".title.alignment", // 15
        // description
        ".description.isDisplaying", // 16
        ".description", // 17
        ".description.font", // 18
        ".description.font.color", // 19
        ".description.alignment", // 20
        // properties set by user
        ".user.legend.name" // 21
    };
    public static final int[] LIST_OF_PROPERTIES = {
        LABEL,
        IS_DISPLAYING,
        USER_ORIGIN_X,
        USER_ORIGIN_Y,
        WIDTH,
        HEIGHT,
        BACKGROUND_IS_DISPLAYING,
        BACKGROUND_COLOR,
        BORDER_IS_DISPLAYING,
        BORDER_COLOR,
        BORDER_LINE_THICK,
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
    private static LegendProperty instance;
    private Map<String, Integer> propertyIndex;

    /**
     * @param propertyName - refers to a string from LEGEND_PROPERTIES array
     * @return the property number
     */
    public int getProperty(String propertyName) {
        return propertyIndex.get(propertyName);
    }

    private LegendProperty() {
        // initialize and populate the property index mapping
        propertyIndex = new HashMap<String, Integer>();
        for (int i = 0; i < LEGEND_PROPERTIES.length; i++) {
            propertyIndex.put(LEGEND_PROPERTIES[i], i);
        }
    }

    /**
     *
     * @return the instance of LegendProperty
     */
    public static LegendProperty getInstance() {
        if (instance == null) {
            instance = new LegendProperty();
        }
        return instance;
    }
}
