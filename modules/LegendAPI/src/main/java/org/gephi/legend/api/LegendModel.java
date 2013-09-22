package org.gephi.legend.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.Renderer;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

/**
 * A model that holds legend data The legend model contains a list of active
 * items, inactive items, renderers list, the currently selected item, active
 * inplace editor and item_index-root_node mapping. The order of the items in
 * the active list determines the order of rendering. Hence, there are methods
 * to modify the order of this list. The order of the items in the inactive list
 * is the order in which the items were removed. The renderers will be
 * registered when the corresponding item is built.
 *
 * Terminologies: active legends - legends that are currently displaying
 * inactive legends - legends that have been deleted picked legend - that active
 * legend on which a click event has occurred item index - unique number that
 * identifies the item list index - the position of the item in the active
 * legends list
 *
 * Rendering Technique for legend items: The graphical layout for every legend
 * items is composed of rectangular blocks (see BlockNode.java). These blocks
 * are either non-overlapping, or completely nested. i.e, partial overlap is not
 * allowed. This gives rise to a block tree. Each legend item, identified using
 * the item index, is associated with a tree. Each block is rendered using a
 * separate method. Each block has a set of properties, such as text, border,
 * background etc. Each block is associated with an inplace editor, which is
 * used to modify the associated properties. Inplace Editors have separate
 * builders and renderers.
 *
 * At any point of time, there is only one active inplace editor. The legend
 * model holds the reference to the active inplace editor.
 *
 * @author mvvijesh, edubecks
 */
public class LegendModel {

    private Integer pickedLegendIndex; // legend item index of the selected legend
    private Integer nextItemIndex; // the index of the item that will be created next
    private Integer numberOfActiveItems; // length of activeLegendItems - to reduce the overhead of calling size method repeatedly.
    private Integer numberOfInactiveItems; // length of inactiveLegendItems - to reduce the overhead of calling size method repeatedly.
    private ArrayList<Item> activeLegendItems;
    private ArrayList<Item> inactiveLegendItems;
    private Map<Integer, BlockNode> indexNodeMap; // provides a mapping between items and a root node
    private InplaceEditor ipeditor; // inplace editor of the block on which a event occured
    private final Workspace workspace;
    private ArrayList<Renderer> renderers;
    private static final String LEGEND_DESCRIPTION = "legend";
    private static final String DYNAMIC = ".dynamic";
    private static final String ITEM_DESCRIPTION = ".item";

    public LegendModel(Workspace currentWorkspace) {
        workspace = currentWorkspace;
        pickedLegendIndex = -1;
        nextItemIndex = 0;
        numberOfActiveItems = 0;
        numberOfInactiveItems = 0;
        activeLegendItems = new ArrayList<Item>();
        inactiveLegendItems = new ArrayList<Item>();
        indexNodeMap = new HashMap<Integer, BlockNode>();
        ipeditor = null;
        renderers = new ArrayList<Renderer>();
    }

    /**
     * adds the renderer to the list of renderers. Note that it doesnt check for
     * duplicates.
     *
     * @param renderer - renderer to be added
     */
    public void addRenderer(Renderer renderer) {
        renderers.add(renderer);
    }

    /**
     * @return the list of renderers
     */
    public ArrayList<Renderer> getRenderers() {
        return renderers;
    }

    /**
     * check if the renderer has already been added
     *
     * @param renderer - renderer to be checked
     */
    public Boolean isRendererAdded(Renderer renderer) {
        for (Renderer r : renderers) {
            if (r.equals(renderer)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * @return workspace
     */
    public Workspace getWorkspace() {
        return workspace;
    }

    /**
     * @return the index of the item to be created next. This serves as a unique
     * ID for the legend item
     */
    public Integer getNextItemIndex() {
        return nextItemIndex;
    }

    /**
     * check if there are any active legends
     */
    public boolean hasActiveLegends() {
        if (activeLegendItems.isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * swap the items at indices index1 and index2 in the activeLegendItems list
     * this method is used to reorder the elements for layered rendering
     *
     * @param index1 - index of item to be swapped
     * @param index2 - index of item to be swapped
     */
    public void swapItems(int index1, int index2) {
        try {
            Collections.swap(activeLegendItems, index1, index2);
        } catch (IndexOutOfBoundsException e) {
            System.err.println(e);
        }
    }

    /**
     * @param itemIndex
     * @return root node of the block tree for the given item index
     */
    public BlockNode getBlockTree(int itemIndex) {
        return indexNodeMap.get(itemIndex);
    }

    /**
     * remove the root node of the block tree for the given item index
     *
     * @param itemIndex
     */
    public void removeBlockTree(int itemIndex) {
        indexNodeMap.remove(itemIndex);
    }

    /**
     * associate a root block node with a legend item
     *
     * @param itemIndex - item index of the legend item
     * @param root - root node
     */
    public void setBlockTree(int itemIndex, BlockNode root) {
        indexNodeMap.put(itemIndex, root);
    }

    /**
     * @return the total number of displaying legend items and restorable legend
     * items
     */
    public Integer getTotalNumberOfItems() {
        return numberOfActiveItems + numberOfInactiveItems;
    }

    /**
     * @return the number of legend items being displayed
     */
    public Integer getNumberOfActiveItems() {
        return numberOfActiveItems;
    }

    /**
     * @return the number of restorable legend items
     */
    public Integer getNumberOfInactiveItems() {
        return numberOfInactiveItems;
    }

    /**
     * itemIndex need not be in the same as the list index. because the user
     * might have re-ordered the legend layers. given an item index, returns its
     * index in the activeLegendItems.
     *
     * @param itemIndex
     * @return list index of the item with the corresponding item index
     */
    public int getListIndexFromItemIndex(int itemIndex) {
        for (int i = 0; i < numberOfActiveItems; i++) {
            if ((Integer) activeLegendItems.get(i).getData(LegendItem.ITEM_INDEX) == itemIndex) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param pickedLegendListIndex - list index of the picked legend
     */
    public void setPickedLegend(Integer pickedLegendListIndex) {
        // pickedLegend can also be -1, if there are no more items
        if (pickedLegendListIndex >= -1 && pickedLegendListIndex < numberOfActiveItems) {
            pickedLegendIndex = pickedLegendListIndex;
        }
    }

    /**
     * @param item - item to be selected
     */
    public void setPickedLegend(Item item) {
        int itemListIndex = getListIndexFromItemIndex((Integer) item.getData(LegendItem.ITEM_INDEX));
        if (itemListIndex != -1) {
            this.pickedLegendIndex = itemListIndex;
        }
    }

    /**
     * @return the index of the currently selected item
     */
    public Integer getPickedLegend() {
        return pickedLegendIndex;
    }

    /**
     * @return the active legend stacked on top of the currently selected legend
     */
    public Integer getPreviousActiveLegend() {
        if (pickedLegendIndex == 0) {
            // if the currently picked item is the first item, there is no previous item. Hence, return -1.
            return -1;
        }
        return pickedLegendIndex - 1;
    }

    /**
     * @return the active legend stacked below the currently selected legend
     */
    public Integer getNextActiveLegend() {
        if (pickedLegendIndex == numberOfActiveItems - 1) {
            // if the currently picked item is the last item, there is no next item. Hence, return -1.
            return -1;
        }
        return pickedLegendIndex + 1;
    }

    /**
     * @param item - item to be added
     */
    public void addItem(Item item) {
        if (item != null) {
            activeLegendItems.add(item);

            // fetch item index to populate the index-node map
            int itemIndex = (Integer) item.getData(LegendItem.ITEM_INDEX);

            // fetch dimensions to create root node
            PreviewProperty[] previewProperties = item.getData(LegendItem.PROPERTIES);
            float originX = previewProperties[LegendProperty.USER_ORIGIN_X].getValue();
            float originY = previewProperties[LegendProperty.USER_ORIGIN_Y].getValue();
            float width = previewProperties[LegendProperty.WIDTH].getValue();
            float height = previewProperties[LegendProperty.HEIGHT].getValue();

            // associate the item index with a root node
            indexNodeMap.put(itemIndex, new BlockNode(null, originX, originY, width, height, item, BlockNode.ROOT));

            // update counters
            numberOfActiveItems += 1;
            nextItemIndex += 1;

            // set the picked legend to the latest item added
            pickedLegendIndex = numberOfActiveItems - 1;

            // whenever a new item is added, disable the old inplace editor
            ipeditor = null;
        }
    }

    /**
     * @param listIndex - list index of the item to be removed
     */
    public void removeItem(int listIndex) {
        // remove from activeLegendItems and append it onto inactiveLegendItems
        if (listIndex >= 0 && listIndex < numberOfActiveItems) {
            inactiveLegendItems.add(activeLegendItems.remove(listIndex));
            if (getPreviousActiveLegend() != -1) {
                // first item was removed
                setPickedLegend(getPreviousActiveLegend());
            } else if (getNextActiveLegend() != -1) {
                // last item was removed
                setPickedLegend(getNextActiveLegend());
            } else {
                // there was only one item and it was removed
                setPickedLegend(-1);
            }

            // update counters
            numberOfActiveItems -= 1;
            numberOfInactiveItems += 1;

            // set the picked legend to the item above the removed legend
            pickedLegendIndex = numberOfActiveItems - 1;
        }
    }

    /**
     * @param itemIndex - the item index of the item to be restored
     */
    public void restoreItem(int itemIndex) {
        // find the list index (within inactiveLegendItems) of the given item item index
        int index = -1;
        for (int i = 0; i < numberOfInactiveItems; i++) {
            if ((Integer) inactiveLegendItems.get(i).getData(LegendItem.ITEM_INDEX) == itemIndex) {
                index = i;
                break;
            }
        }

        // restore the item if it exists and update the counters
        if (index != -1) {
            activeLegendItems.add(inactiveLegendItems.remove(itemIndex));

            // update counters
            numberOfActiveItems += 1;
            numberOfInactiveItems -= 1;

            // reset the picked legend index to the latest element restored
            pickedLegendIndex = numberOfActiveItems - 1;
        }
    }

    /**
     * @param itemIndex - item index of the item to be removed the
     * inactiveLegendItems list
     */
    public void destroyItem(int itemIndex) {
        // permanently delete the item from the inactiveLegendItems list

        // find the list index of the itemIndex
        int index = -1;
        for (int i = 0; i < numberOfInactiveItems; i++) {
            if ((Integer) inactiveLegendItems.get(i).getData(LegendItem.ITEM_INDEX) == itemIndex) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            inactiveLegendItems.remove(index);
            numberOfInactiveItems -= 1;
        }
    }

    /**
     * @param listIndex - list index of the item to be retrieved
     * @return the legend item at the position listIndex in the
     * activeLegendItems list
     */
    public Item getItemAtIndex(int listIndex) {
        if (listIndex >= 0 && listIndex < numberOfActiveItems) {
            return activeLegendItems.get(listIndex);
        }

        return null;
    }

    /**
     * @return return the currently picked item returns null if no item is
     * picked
     */
    public Item getPickedLegendItem() {
        if (pickedLegendIndex >= 0) {
            return getItemAtIndex(pickedLegendIndex);
        }
        return null;
    }

    /**
     * @return If multiple selections are present, then this returns the first
     * occurance. assumes that multiple selections are invalid
     */
    public Item getSelectedItem() {
        for (Item item : activeLegendItems) {
            if (item.getData(LegendItem.IS_SELECTED)) {
                return item;
            }
        }
        return null;
    }

    /**
     * @return the list of currently visible items
     */
    public ArrayList<Item> getActiveItems() {
        return activeLegendItems;
    }

    /**
     * @return the list of removed items
     */
    public ArrayList<Item> getInactiveItems() {
        return inactiveLegendItems;
    }

    /**
     * @return the currently active inplace editor associated with some block of
     * the picked legend
     */
    public InplaceEditor getInplaceEditor() {
        return ipeditor;
    }

    /**
     * @param ipeditor - the inplace editor that will be rendered onto the
     * canvas
     */
    public void setInplaceEditor(InplaceEditor ipeditor) {
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
