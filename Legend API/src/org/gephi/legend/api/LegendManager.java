/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComboBox;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewProperty;
import org.openide.nodes.PropertySupport;

/**
 *
 * @author eduBecKs
 */
public class LegendManager {

    private Integer activeLegend;
    private Integer currentIndex;
    private Integer firstActiveLegend;
    private ArrayList<Boolean> isActive;
    private ArrayList<String> items;
    private ArrayList<Item> legendItems;
    // reference to combobox
    private JComboBox activeLegendsComboBox;
    public static final String LEGEND_PROPERTIES = "legend properties";
    public static final String INDEX = "index";
    private static final String LEGEND_DESCRIPTION = "legend";
    private static final String ITEM_DESCRIPTION = ".item";

    public LegendManager() {
        currentIndex = 0;
        firstActiveLegend = 0;
        items = new ArrayList<String>();
        legendItems = new ArrayList<Item>();
        isActive = new ArrayList<Boolean>();
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public boolean hasActiveLegends() {

        for (int i = 0; i < isActive.size(); i++) {
            if (isActive.get(i)) {
                firstActiveLegend = i;
                return true;
            }
        }
        return false;
    }

    public void refreshActiveLegendsComboBox() {
        System.out.println("@Var: refreshActiveLegendsComboBox activeLegend: " + activeLegend);
        activeLegendsComboBox.removeAllItems();
        if (activeLegend != -1) {
            for (int i = 0; i < isActive.size(); i++) {
                if (isActive.get(i)) {
                    Item item = legendItems.get(i);
//                PreviewProperty[] properties = item.getData(LegendItem.PROPERTIES);
//                String label = properties[0].getValue();
//                String legendType = " [" + item.getData(LegendItem.SUB_TYPE) + "]";
//                System.out.println("@Var: add item to combobox: " + label);
                    activeLegendsComboBox.addItem(item);
                }
            }
            System.out.println("@Var: refreshActiveLegendsComboBox activeLegend: " + activeLegend);
//        activeLegendsComboBox.setSelectedIndex(activeLegend);
            activeLegendsComboBox.setSelectedItem(legendItems.get(activeLegend));
        }
        else{
            activeLegendsComboBox.setSelectedIndex(-1);
        }

    }

    public void setActiveLegendsComboBox(JComboBox activeLegendsComboBox) {
        this.activeLegendsComboBox = activeLegendsComboBox;
    }

    public void addItem(Item item) {
        
        activeLegend = currentIndex;
        System.out.println("@Var: creating item activeLegend: "+activeLegend);
        System.out.println("@Var: item: "+item);
        
        items.add(LEGEND_DESCRIPTION + ITEM_DESCRIPTION + currentIndex);
        isActive.add(Boolean.TRUE);
        legendItems.add(item);
        currentIndex++;
        // refresh list
        refreshActiveLegendsComboBox();
    }

    public void removeItem(int index) {

        isActive.set(index, Boolean.FALSE);
        if (hasActiveLegends()) {
            activeLegend = firstActiveLegend;
        }
        else {
            activeLegend = -1;
        }
        // refresh list
        refreshActiveLegendsComboBox();
    }

    public void setActiveLegend(Integer activeLegend) {
        this.activeLegend = activeLegend;
    }

    public Integer getActiveLegend() {
        return activeLegend;
    }

    public ArrayList<String> getItems() {
        return items;
    }



    public ArrayList<Item> getLegendItems() {
        ArrayList<Item> activeItems= new ArrayList<Item>();
        for (int i = 0; i < isActive.size(); i++) {
            if(isActive.get(i)){
                activeItems.add(legendItems.get(i));
            }
        }
        return activeItems;
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

    public static ArrayList<String> getProperties(ArrayList<String> legendProperties, int itemIndex) {
        ArrayList<String> properties = new ArrayList<String>();
        for (String property : legendProperties) {
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
//        System.out.println("@Var: property: " + property);
        return property;
    }

}
