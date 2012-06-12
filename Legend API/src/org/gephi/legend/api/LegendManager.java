/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.util.ArrayList;

/**
 *
 * @author eduBecKs
 */
public class LegendManager {
    //INDEX

    public static final String INDEX = "index";
    private static int currentItemIndex = -1;
    private static final String legendDescription = "legend";
    private static final String itemDescription = ".item";
    private static int currentWorkIndex = -1;
    private static final String workDescription = ".work";

    public static int useItemIndex() {
        currentItemIndex += 1;
        return currentItemIndex;
    }

    public static int useWorkIndex() {
        currentWorkIndex += 1;
        return currentWorkIndex;
    }

    public static ArrayList<String> getProperties(String[] PROPERTIES) {
        ArrayList<String> properties = new ArrayList<String>();
        for (String property : PROPERTIES) {
            String newProperty = (legendDescription
                                  + workDescription + currentWorkIndex
                                  + itemDescription + currentItemIndex
                                  + property);
            properties.add(newProperty);
            System.out.println("Creating >>> " + newProperty);
        }
        return properties;
    }

    public static String getProperty(String[] PROPERTIES, int legendIndex, int legendProperty) {
        String property = (legendDescription
                           + workDescription + currentWorkIndex
                           + itemDescription + legendIndex
                           + PROPERTIES[legendProperty]);
        System.out.println("@Var: property: " + property);
        return property;
    }

}
