/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.AbstractTableModel;
import org.gephi.legend.items.LegendItem;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

/**
 *
 * @author eduBecKs
 */
public class LegendManager {

    private Integer activeLegendIndex;
    private Integer currentIndex;
    private Integer firstActiveLegend;
    private Integer numberOfItems;
    private Integer numberOfActiveItems;
    private ArrayList<Boolean> isActive;
    private ArrayList<String> items;
    private ArrayList<Item> legendItems;
    public static final String LEGEND_PROPERTIES = "legend properties";
    //public static final String INDEX = "index";
    private static final String LEGEND_DESCRIPTION = "legend";
    private static final String DYNAMIC = ".dynamic";
    private static final String ITEM_DESCRIPTION = ".item";
    private final Workspace workspace;

    public LegendManager(Workspace workspace) {
        this.workspace = workspace;
        this.currentIndex = 0;
        this.numberOfActiveItems = 0;
        this.numberOfItems = 0;
        this.firstActiveLegend = -1;
        this.activeLegendIndex = -1;
        this.items = new ArrayList<String>();
        this.legendItems = new ArrayList<Item>();
        this.isActive = new ArrayList<Boolean>();
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public boolean hasActiveLegends() {
        for (int i = 0; i < isActive.size(); i++) {
            if (isActive.get(i)) {
                return true;
            }
        }
        return false;
    }

    public void swapItems(int index1, int index2) {
        System.out.println("\n-- from swapItems:");
        System.out.println("index1: " + index1 + " index2: " + index2);
        try {
            Collections.swap(legendItems, index1, index2);
            Collections.swap(items, index1, index2);
            //entries from isActive need not be swapped, since both are legends are active and both the entries are 'true'.
        } catch (IndexOutOfBoundsException e) {
            System.err.println(e);
        }
    }

    public void refreshDynamicPreviewProperties() {
        PreviewController previewController =
                Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        PreviewProperties previewProperties = previewModel.getProperties();

        // clear old properties
        for (PreviewProperty property : previewProperties.getProperties(PreviewProperty.CATEGORY_LEGEND_DYNAMIC_PROPERTY)) {
            previewProperties.removeProperty(property);
        }

        // updating and adding new properties
        for (int i = 0; i < isActive.size(); i++) {
            if (isActive.get(i)) {
                PreviewProperty[] properties =
                        (PreviewProperty[]) legendItems.get(i).getData(LegendItem.DYNAMIC_PROPERTIES);
                for (PreviewProperty property : properties) {
                    previewProperties.putValue(property.getName(), property.getValue());
                }
            }
        }
    }

    public void addItem(Item item) {

        activeLegendIndex = currentIndex;

        items.add(LEGEND_DESCRIPTION + ITEM_DESCRIPTION + currentIndex);
        isActive.add(Boolean.TRUE);
        legendItems.add(item);
        currentIndex += 1;
        numberOfItems += 1;
        numberOfActiveItems += 1;
    }

    public void removeItem(int index) {
        isActive.set(index, Boolean.FALSE);
        setFirstActiveLegend();
        activeLegendIndex = firstActiveLegend;
        numberOfActiveItems -= 1;
    }

    private void setFirstActiveLegend() {
        for (int i = 0; i < numberOfItems; i++) {
            if (isActive.get(i)) {
                firstActiveLegend = i;
                return;
            }
        }
        firstActiveLegend = -1;
    }

    public void setActiveLegend(Integer activeLegend) {
        this.activeLegendIndex = activeLegend;
    }

    public Integer getActiveLegend() {
        return activeLegendIndex;
    }

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public Integer getNumberOfActiveItems() {
        return numberOfActiveItems;
    }

    public int getIndexFromItemIndex(int itemIndex) {
        for (int i = 0; i < numberOfItems; i++) {
            if ((Integer) legendItems.get(i).getData(LegendItem.ITEM_INDEX) == itemIndex) {
                return i;
            }
        }
        return -1;
    }

    public Integer getNextActiveLegend() {
        if (activeLegendIndex == isActive.size() - 1) {
            return -1;
        }

        for (int i = activeLegendIndex + 1; i < numberOfItems; i++) {
            if (isActive.get(i)) {
                return i;
            }
        }
        return -1;
    }

    public Integer getPreviousActiveLegend() {
        if (activeLegendIndex == 0) {
            return -1;
        }
        for (int i = activeLegendIndex - 1; i >= 0; i--) {
            if (isActive.get(i)) {
                return i;
            }
        }
        return -1;
    }

    public Item getActiveLegendItem() {
        if (activeLegendIndex >= 0) {
            return legendItems.get(activeLegendIndex);
        }
        return null;
    }

    public ArrayList<String> getItems() {
        ArrayList<String> activeItems = new ArrayList<String>();
        for (int i = 0; i < numberOfItems; i++) {
            if (isActive.get(i)) {
                activeItems.add(items.get(i));
            }
        }
        return activeItems;
    }

    public ArrayList<Item> getLegendItems() {
        ArrayList<Item> activeItems = new ArrayList<Item>();
        for (int i = 0; i < isActive.size(); i++) {
            if (isActive.get(i)) {
                activeItems.add(legendItems.get(i));
            }
        }
        return activeItems;
    }

    public static String getPropertyFromPreviewProperty(PreviewProperty property) {
        String propertyString = property.getName();
        String name = propertyString.substring(propertyString.indexOf('.', 10));
        return name;

//        Pattern pattern = Pattern.compile("legend.item\\d+.(.*)");
//        Matcher matcher = pattern.matcher(propertyString);
//        return matcher.group(1);
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

    public static boolean isLegendDynamicProperty(PreviewProperty property) {
        if (property.getName().endsWith(DYNAMIC)) {
            return true;
        }
        return false;
    }

    public static ArrayList<String> getProperties(String[] PROPERTIES, int itemIndex) {
        ArrayList<String> properties = new ArrayList<String>();
        for (String property : PROPERTIES) {
            String newProperty = (LEGEND_DESCRIPTION + ITEM_DESCRIPTION + itemIndex + property);
            properties.add(newProperty);
        }
        return properties;
    }

    public static String getDynamicProperty(String property, int itemIndex, int itemIndexNested) {
        return (LEGEND_DESCRIPTION + ITEM_DESCRIPTION + itemIndex + property + itemIndexNested + DYNAMIC);
    }

    public static ArrayList<String> getProperties(ArrayList<String> legendProperties, int itemIndex) {
        ArrayList<String> properties = new ArrayList<String>();
        for (String property : legendProperties) {
            String newProperty = (LEGEND_DESCRIPTION + ITEM_DESCRIPTION + itemIndex + property);
            properties.add(newProperty);
        }
        return properties;
    }

    public static String getProperty(String[] PROPERTIES, int itemIndex, int legendProperty) {
        String property = (LEGEND_DESCRIPTION + ITEM_DESCRIPTION + itemIndex + PROPERTIES[legendProperty]);
        return property;
    }
}
