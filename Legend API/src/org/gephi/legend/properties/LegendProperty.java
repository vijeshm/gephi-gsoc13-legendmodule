/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.properties;

/**
 *
 * @author eduBecKs
 */
public class LegendProperty {
    
    
    //LOCATION
    public static final int ORIGIN_X = 0;
    public static final int ORIGIN_Y = 1;
    //DIMENSIONS
    public static final int WIDTH = 2;
    public static final int HEIGHT = 3;
    //TITLE
    public static final int TITLE_IS_DISPLAYING = 4;
    public static final int TITLE_FONT = 5;
    public static final int TITLE_FONT_COLOR = 6;
    //DESCRIPTION
    public static final int DESCRIPTION_IS_DISPLAYING = 7;
    public static final int DESCRIPTION_FONT = 8;
    public static final int DESCRIPTION_FONT_COLOR = 9;
    
    public static final String[] LEGEND_PROPERTIES = {
        //LOCATION
         ".originX",
        ".originY",
        ".width",
        ".height",
        //TITLE
        ".title.isDisplaying",
        ".title.font",
        ".title.font.color",
        //DESCRIPTION
        ".description.isDisplaying",
        ".description.font",
        ".description.font.color"
    };
}
