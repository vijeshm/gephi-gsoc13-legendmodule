/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewProperty;
import org.openide.nodes.PropertySupport;

/**
 *
 * @author eduBecKs
 */
public class LegendManager {

    private Integer currentIndex;
    private ArrayList<String> items;
    private ArrayList<String> legendTypes;
    private ArrayList<Item> legendItems;
    
    
    public static final String LEGEND_PROPERTIES = "legend properties";
    public static final String INDEX = "index";
    private static final String LEGEND_DESCRIPTION = "legend";
    private static final String ITEM_DESCRIPTION = ".item";

    public LegendManager() {
        currentIndex = 0;
        items = new ArrayList<String>();
        legendTypes = new ArrayList<String>();
        legendItems = new ArrayList<Item>();
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }


    public void addItem(Item item) {
        
        items.add(LEGEND_DESCRIPTION + ITEM_DESCRIPTION + currentIndex);
        legendTypes.add((String)item.getData(LegendItem.SUB_TYPE));
        legendItems.add(item);
        currentIndex++;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public ArrayList<String> getLegendTypes() {
        return legendTypes;
    }

    public ArrayList<Item> getLegendItems() {
        return legendItems;
    }

    public static boolean getItemIndexFromProperty(PreviewProperty property, Integer index) {
        Pattern pattern = Pattern.compile("\\.item(\\d+)\\.");
        Matcher matcher = pattern.matcher(property.getName());
        if (matcher.find()) {
            index = Integer.valueOf(matcher.group(1));
        }
        return false;
    }

    public static boolean isLegendPropertyForItem(PreviewProperty property, String item) {
        if (property.getName().startsWith(item)) {
            return true;
        }
        return false;
    }

    public static boolean isLegendProperty(PreviewProperty property) {
        if (property.getName().startsWith(LEGEND_DESCRIPTION)) {
            return true;
        }
        return false;
    }



    public static ArrayList<String> getProperties(String[] PROPERTIES, int itemIndex) {
        ArrayList<String> properties = new ArrayList<String>();
        for (String property : PROPERTIES) {
            String newProperty = (LEGEND_DESCRIPTION
                                  + ITEM_DESCRIPTION + itemIndex
                                  + property);
            properties.add(newProperty);
//            System.out.println("Creating >>> " + newProperty);
        }
        return properties;
    }

    public static String getProperty(String[] PROPERTIES, int itemIndex, int legendProperty) {
        String property = (LEGEND_DESCRIPTION
                           + ITEM_DESCRIPTION + itemIndex
                           + PROPERTIES[legendProperty]);
        System.out.println("@Var: property: " + property);
        return property;
    }

}
