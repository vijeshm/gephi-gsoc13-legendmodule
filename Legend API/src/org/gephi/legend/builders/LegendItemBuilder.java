/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendItem.Alignment;
import org.gephi.legend.properties.LegendProperty;
//import org.gephi.legend.properties.LegendProperties.LegendProperty;
import org.gephi.legend.api.LegendManager;
import org.gephi.legend.items.DescriptionItem;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author edubecks
 */
public abstract class LegendItemBuilder implements ItemBuilder {

    /**
     * Function used to override default values of <code>properties</code>
     * Possible values to be overriden:<br />
     * 
     * LABEL:
     * <ul>
     * <li> defaultLabel </li>
     * </ul>
     * IS_DISPLAYING:
     * <ul>
     * <li> defaultIsDisplaying </li>
     * </ul>
     * ORIGIN
     * <ul>
     * <li> defaultOriginX </li>
     * <li> defaultOriginY </li>
     * </ul>
     * WIDTH
     * <ul>
     * <li> defaultWidth </li>
     * <li> defaultHeight </li>
     * </ul>
     * TITLE:
     * <ul>
     * <li> defaultIsDisplayingTitle </li>
     * <li> defaultTitle </li>
     * <li> defaultTitleFont </li>
     * <li> defaultTitleAlignment </li>
     * <li> defaultTitleFontColor </li>
     * </ul>
     * DESCRIPTION:
     * <ul>
     * <li> defaultDescription </li>
     * <li> defaultIsDisplayingDescription </li>
     * <li> defaultDescriptionFontColor </li>
     * <li> defaultDescriptionAlignment </li>
     * <li> defaultDescriptionFont </li>
     * </ul>
     */
    protected abstract void setDefaultValues();

    /**
     * Based on <code>properties</code>, determine whether this builder was
     * used to build <code>Item</code>.
     * @param item the item to be tested
     * @return <code>true</code> if <code>item</code> was built by this
     * builder, <code>false</code> otherwise
     */
    protected abstract boolean isBuilderForItem(Item item);

    /**
     * Builds and item based on <code>graph</code> and <code>attributeModel</code> data
     * @param graph
     * @param attributeModel
     * @return 
     */
    protected abstract Item buildItem(Graph graph, AttributeModel attributeModel);

    /**
     * Used to determine if the <code>item</code> has dynamic 
     * properties to be displayed in the PropertySheet Editor
     * @return <code>true</code> if <code>item</code> has dynamic
     * properties, <code>false</code> otherwise
     */
    protected abstract Boolean hasDynamicProperties();

    /**
     * Function used to create the specific PreviewProperty for each type of
     * LegendItem
     * 
     * @param item
     * @return an array of PreviewProperty to be appended to the general 
     * LegendItem's PreviewProperty
     */
    protected abstract PreviewProperty[] createLegendItemProperties(Item item);

    public Item createItem(Integer newItemIndex, Graph graph, AttributeModel attributeModel) {
        Item item = buildItem(graph, attributeModel);
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);
        item.setData(LegendItem.PROPERTIES, getLegendProperties(item));
        item.setData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES, 0);
        item.setData(LegendItem.HAS_DYNAMIC_PROPERTIES, hasDynamicProperties());
        item.setData(LegendItem.DYNAMIC_PROPERTIES, new PreviewProperty[0]);
        item.setData(LegendItem.IS_SELECTED, Boolean.FALSE);
        item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.FALSE);
        item.setData(LegendItem.CURRENT_TRANSFORMATION, "");
        return item;

    }


    @Override
    public Item[] getItems(Graph graph, AttributeModel attributeModel) {

        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        Workspace workspace = projectController.getCurrentWorkspace();
        PreviewModel previewModel = previewController.getModel(workspace);
        PreviewProperties previewProperties = previewModel.getProperties();
        if (previewProperties.hasProperty(LegendManager.LEGEND_PROPERTIES)) {

            LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
            ArrayList<Item> legendItems = legendManager.getLegendItems();
            ArrayList<Item> items = new ArrayList<Item>();
            for (Item item : legendItems) {
                if (isBuilderForItem(item)) {
                    items.add(item);
                }

            }
            return items.toArray(new Item[items.size()]);
        }
        return new Item[0];
    }

    public PreviewProperty[] createLegendProperties(Item item) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        setDefaultValues();

        ArrayList<String> legendProperties = LegendManager.getProperties(LegendProperty.LEGEND_PROPERTIES, itemIndex);

        return new PreviewProperty[]{
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.LABEL),
                                                   String.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.label.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.label.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultLabel + itemIndex),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.IS_DISPLAYING),
                                                   Boolean.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.isDisplaying.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.isDisplaying.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsDisplaying),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.USER_ORIGIN_X),
                                                   Float.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.originX.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.originX.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultOriginX),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.USER_ORIGIN_Y),
                                                   Float.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.originY.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.originY.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultOriginY),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.WIDTH),
                                                   Float.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.width.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.width.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultWidth),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.HEIGHT),
                                                   Float.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.height.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.height.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultHeight),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.TITLE_IS_DISPLAYING),
                                                   Boolean.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.isDisplaying.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.isDisplaying.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsDisplayingTitle),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.TITLE),
                                                   String.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultTitle),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.TITLE_FONT),
                                                   Font.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultTitleFont),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.TITLE_FONT_COLOR),
                                                   Color.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.color.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.color.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultTitleFontColor),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.TITLE_ALIGNMENT),
                                                   Alignment.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.alignment.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.alignment.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultTitleAlignment),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.DESCRIPTION_IS_DISPLAYING),
                                                   Boolean.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.isDisplaying.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.isDisplaying.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsDisplayingTitle),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.DESCRIPTION),
                                                   String.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultDescription),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.DESCRIPTION_FONT),
                                                   Font.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultDescriptionFont),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.DESCRIPTION_FONT_COLOR),
                                                   Color.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.color.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.color.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultDescriptionFontColor),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.DESCRIPTION_ALIGNMENT),
                                                   Alignment.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.alignment.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.alignment.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultDescriptionAlignment)
                };
    }

    public PreviewProperty[] getLegendProperties(Item item) {

        PreviewProperty[] legendProperties = createLegendProperties(item);
        PreviewProperty[] properties = createLegendItemProperties(item);
        PreviewProperty[] previewProperties = new PreviewProperty[legendProperties.length + properties.length];
        System.arraycopy(legendProperties, 0, previewProperties, 0, legendProperties.length);
        System.arraycopy(properties, 0, previewProperties, legendProperties.length, properties.length);

        return previewProperties;

    }

    public static boolean updatePreviewProperty(Item item, int numOfProperties) {
        // item index
//        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
//        Integer currentNumOfPropertiews = item.getData(DescriptionItem.NUMBER_OF_ITEMS);
//        int currentNumOfProperties = ((PreviewProperty[]) (item.getData(LegendItem.DYNAMIC_PROPERTIES))).length;
        System.out.println("@Var: currentNumOfProperties: " + item.getData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES));
        int currentNumOfProperties = ((Integer) (item.getData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES))).intValue();
        System.out.println("@Var: currentNumOfPropertiews: " + currentNumOfProperties);
        System.out.println("@Var: numOfProperties: " + numOfProperties);
        // number of items didn't change
        if (numOfProperties == currentNumOfProperties) {
            return false;
        }
        // adding properties
        else if (numOfProperties > currentNumOfProperties) {
            int newProperties = numOfProperties - currentNumOfProperties;

            //bug
            if (item instanceof DescriptionItem) {
                DescriptionItemBuilder.addPreviewProperty(item, currentNumOfProperties, newProperties);
            }
        }
        // removing properties
        else {
            int removeProperties = currentNumOfProperties - numOfProperties;
            //bug
            if (item instanceof DescriptionItem) {
                DescriptionItemBuilder.removePreviewProperty(item, removeProperties);
            }
        }
        item.setData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES, numOfProperties);
        return true;
    }


    //DEFAULT VALUES 
    // LABEL
    protected String defaultLabel = "";
    // IS_DISPLAYING
    protected boolean defaultIsDisplaying = true;
    //ORIGIN
    protected float defaultOriginX = 0;
    protected float defaultOriginY = 0;
    //WIDTH
    protected float defaultWidth = 500;
    protected float defaultHeight = 300f;
    // TITLE
    protected Boolean defaultIsDisplayingTitle = true;
    protected final String defaultTitle = "TITLE";
    protected final Font defaultTitleFont = new Font("Arial", Font.BOLD, 30);
    protected final Alignment defaultTitleAlignment = Alignment.CENTER;
    protected final Color defaultTitleFontColor = Color.BLACK;
    // DESCRIPTION
    protected final String defaultDescription = "description ... ";
    protected Boolean defaultIsDisplayingDescription = true;
    protected final Color defaultDescriptionFontColor = Color.BLACK;
    protected final Alignment defaultDescriptionAlignment = Alignment.LEFT;
    protected final Font defaultDescriptionFont = new Font("Arial", Font.PLAIN, 10);
}
