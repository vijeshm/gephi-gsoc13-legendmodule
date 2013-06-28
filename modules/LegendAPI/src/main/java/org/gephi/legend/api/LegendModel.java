/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gephi.legend.inplaceeditor.inplaceEditor;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

/**
 *
 * @author mvvijesh, eduBecKs
 */
public class LegendModel {

    private Integer pickedLegendIndex;
    private Integer nextItemIndex;
    private Integer numberOfActiveItems;
    private Integer numberOfInactiveItems;
    private ArrayList<Item> activeLegendItems;
    private ArrayList<Item> inactiveLegendItems;
    private Map<Integer, blockNode> indexNodeMap;
    private inplaceEditor ipeditor;
    private static final String LEGEND_DESCRIPTION = "legend";
    private static final String DYNAMIC = ".dynamic";
    private static final String ITEM_DESCRIPTION = ".item";
    private final Workspace workspace;
    public static final String LEGEND_PROPERTIES = "legend properties";
    public static final String INDEX = "index"; // Where is this being used?
    

    public LegendModel(Workspace workspace) {
        this.workspace = workspace;
        this.pickedLegendIndex = -1;
        this.nextItemIndex = 0;
        this.numberOfActiveItems = 0;
        this.numberOfInactiveItems = 0;
        this.activeLegendItems = new ArrayList<Item>();
        this.inactiveLegendItems = new ArrayList<Item>();
        this.indexNodeMap = new HashMap<Integer, blockNode>();
        this.ipeditor = null;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public Integer getNextItemIndex() {
        return nextItemIndex;
    }

    public boolean hasActiveLegends() {
        if (activeLegendItems.isEmpty()) {
            return false;
        }

        return true;
    }

    public void swapItems(int index1, int index2) {
        try {
            Collections.swap(activeLegendItems, index1, index2);
        } catch (IndexOutOfBoundsException e) {
            System.err.println(e);
        }
    }
    
    public blockNode getBlockTree(int itemIndex) {
        return indexNodeMap.get(itemIndex);
    }
    
    public void setBlockTree(int itemIndex, blockNode root) {
        indexNodeMap.put(itemIndex, root);
    }

    public Integer getTotalNumberOfItems() {
        return numberOfActiveItems + numberOfInactiveItems;
    }

    public Integer getNumberOfActiveItems() {
        return numberOfActiveItems;
    }

    public Integer getNumberOfInactiveItems() {
        return numberOfInactiveItems;
    }

    public int getListIndexFromItemIndex(int itemIndex) {
        // itemIndex need not be in the same as the list index. because the user might have re-ordered the legend layers.
        // the given an item index, returns its index in the activeLegendItems.
        for (int i = 0; i < numberOfActiveItems; i++) {
            if ((Integer) activeLegendItems.get(i).getData(LegendItem.ITEM_INDEX) == itemIndex) {
                return i;
            }
        }
        return -1;
    }

    public void setPickedLegend(Integer pickedLegend) {
        if (pickedLegend >= 0 && pickedLegend < numberOfActiveItems) {
            this.pickedLegendIndex = pickedLegend;
        }
    }

    public void setPickedLegend(Item item) {
        int itemIndex = getListIndexFromItemIndex((Integer) item.getData(LegendItem.ITEM_INDEX));
        if (itemIndex != -1) {
            this.pickedLegendIndex = itemIndex;
        }
    }

    public Integer getPickedLegend() {
        return pickedLegendIndex;
    }

    public Integer getPreviousActiveLegend() {
        if (pickedLegendIndex == 0) {
            return -1;
        }
        return pickedLegendIndex - 1;
    }

    public Integer getNextActiveLegend() {
        if (pickedLegendIndex == numberOfActiveItems - 1) {
            return -1;
        }
        return pickedLegendIndex + 1;
    }

    public void addItem(Item item) {
        if (item != null) {
            activeLegendItems.add(item);
            int itemIndex = (Integer) item.getData(LegendItem.ITEM_INDEX);
            PreviewProperty[] previewProperties = item.getData(LegendItem.PROPERTIES);
            /*
            float originX = previewProperties[LegendProperty.USER_ORIGIN_X].getValue();
            float originY = previewProperties[LegendProperty.USER_ORIGIN_Y].getValue();
            float width = previewProperties[LegendProperty.WIDTH].getValue();
            float height = previewProperties[LegendProperty.HEIGHT].getValue();
            */
            indexNodeMap.put(itemIndex, new blockNode(null, Float.MAX_VALUE, Float.MAX_VALUE, 0, 0));

            numberOfActiveItems += 1;
            pickedLegendIndex = numberOfActiveItems - 1;
            nextItemIndex += 1;
        }
    }

    public void removeItem(int index) {
        // remove from activeLegendItems and append it onto inactiveLegendItems
        if (index >= 0 && index < numberOfActiveItems) {
            inactiveLegendItems.add(activeLegendItems.remove(index));
            if (getPreviousActiveLegend() != -1) {
                setPickedLegend(getPreviousActiveLegend());
            } else if (getNextActiveLegend() != -1) {
                setPickedLegend(getNextActiveLegend());
            } else {
                setPickedLegend(-1);
            }

            numberOfActiveItems -= 1;
            numberOfInactiveItems += 1;
        }
    }

    public void restoreItem(int itemIndex) {
        int index = -1;
        for (int i = 0; i < numberOfInactiveItems; i++) {
            if ((Integer) inactiveLegendItems.get(i).getData(LegendItem.ITEM_INDEX) == itemIndex) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            activeLegendItems.add(inactiveLegendItems.remove(itemIndex));
            numberOfActiveItems += 1;
            numberOfInactiveItems -= 1;
            pickedLegendIndex = numberOfActiveItems - 1;
        }
    }

    public void destroyItem(int itemIndex) {
        int index = -1;
        for (int i = 0; i < numberOfInactiveItems; i++) {
            if ((Integer) inactiveLegendItems.get(i).getData(LegendItem.ITEM_INDEX) == itemIndex) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            inactiveLegendItems.remove(itemIndex);
            numberOfInactiveItems -= 1;
        }
    }

    public Item getItemAtIndex(int index) {
        if (index >= 0 && index < numberOfActiveItems) {
            return activeLegendItems.get(index);
        }

        return null;
    }

    public Item getPickedLegendItem() {
        if (pickedLegendIndex >= 0) {
            return getItemAtIndex(pickedLegendIndex);
        }
        return null;
    }

    public Item getSelectedItem() {
        // assumes that multiple selections are invalid. If multiple selections are present, then this returns the first occurance.
        for (Item item : activeLegendItems) {
            if (item.getData(LegendItem.IS_SELECTED)) {
                return item;
            }
        }
        return null;
    }

    public ArrayList<Item> getActiveItems() {
        return activeLegendItems;
    }

    public ArrayList<Item> getInactiveItems() {
        return inactiveLegendItems;
    }
    
    public inplaceEditor getInplaceEditor() {
        return ipeditor;
    }
    
    public void setInplaceEditor(inplaceEditor ipeditor) {
        this.ipeditor = ipeditor;
    }

    public void refreshDynamicPreviewProperties() {
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        PreviewProperties previewProperties = previewModel.getProperties();

        // clear old properties
        for (PreviewProperty property : previewProperties.getProperties(PreviewProperty.CATEGORY_LEGEND_DYNAMIC_PROPERTY)) {
            previewProperties.removeProperty(property);
        }

        // updating and adding new properties
        for (Item item : activeLegendItems) {
            PreviewProperty[] properties = (PreviewProperty[]) item.getData(LegendItem.DYNAMIC_PROPERTIES);
            for (PreviewProperty property : properties) {
                previewProperties.putValue(property.getName(), property.getValue());
            }
        }
    }

    // Why are the following methods used for??
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
