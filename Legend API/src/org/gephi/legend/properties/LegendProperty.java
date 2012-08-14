/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.properties;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eduBecKs
 */
public class LegendProperty {

    // LABEL
    public static final int LABEL = 0;
    // DISPLAY
    public static final int IS_DISPLAYING = 1;
    //LOCATION
    public static final int USER_ORIGIN_X = 2;
    public static final int USER_ORIGIN_Y = 3;
    //DIMENSIONS
    public static final int WIDTH = 4;
    public static final int HEIGHT = 5;
    // BACKGROUND
    public static final int BACKGROUND_IS_DISPLAYING = 6;
    public static final int BACKGROUND_COLOR = 7;
    public static final int BACKGROUND_BORDER_COLOR = 8;
    public static final int BACKGROUND_BORDER_LINE_THICK = 9;
    //TITLE
    public static final int TITLE_IS_DISPLAYING = 10;
    public static final int TITLE = 11;
    public static final int TITLE_FONT = 12;
    public static final int TITLE_FONT_COLOR = 13;
    public static final int TITLE_ALIGNMENT = 14;
    //DESCRIPTION
    public static final int DESCRIPTION_IS_DISPLAYING = 15;
    public static final int DESCRIPTION = 16;
    public static final int DESCRIPTION_FONT = 17;
    public static final int DESCRIPTION_FONT_COLOR = 18;
    public static final int DESCRIPTION_ALIGNMENT = 19;
    

    
    
    public static final String[] LEGEND_PROPERTIES = {
        // label
        ".label",                               // 0 
        ".isDisplaying",                        // 1
        //LOCATION                              
        ".originX",                             // 2
        ".originY",                             // 3
        ".width",                               // 4
        ".height",                              // 5
        // BACKGROUND
        ".background.isDisplaying",             // 6
        ".background.color",                    // 7
        ".background.border.color",             // 8
        ".background.border.lineThick",         // 9
        //TITLE
        ".title.isDisplaying",                  // 10
        ".title",                               // 11 
        ".title.font",                          // 12
        ".title.font.color",                    // 13
        ".title.alignment",                     // 14
        //DESCRIPTION
        ".description.isDisplaying",            // 15
        ".description",                         // 16
        ".description.font",                    // 17
        ".description.font.color",              // 18
        ".description.alignment"                // 19
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
        DESCRIPTION_ALIGNMENT
    };
    
    
   
    private static LegendProperty instance = new LegendProperty();
    private Map<String,Integer> propertyIndex;
    
        public int getProperty(String propertyName){
        return propertyIndex.get(propertyName);
    }
 
    private LegendProperty() {
        propertyIndex= new HashMap<String, Integer>();
        for (int i = 0; i < LEGEND_PROPERTIES.length; i++) {
            propertyIndex.put(LEGEND_PROPERTIES[i], i);
        }        
    }
 
    public static LegendProperty getInstance() {
        return instance;
    }
}
