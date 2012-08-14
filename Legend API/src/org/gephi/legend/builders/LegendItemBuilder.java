/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.items.DescriptionItem;
import org.gephi.legend.items.LegendItem;
import org.gephi.legend.items.LegendItem.Alignment;
import org.gephi.legend.manager.LegendManager;
import org.gephi.legend.properties.LegendProperty;
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
     * Function used to override default values of
     * <code>properties</code> Possible values to be overriden:<br />
     *
     * LABEL: <ul> <li> defaultLabel </li> </ul> IS_DISPLAYING: <ul> <li>
     * defaultIsDisplaying </li> </ul> ORIGIN <ul> <li> defaultOriginX </li>
     * <li> defaultOriginY </li> </ul> WIDTH <ul> <li> defaultWidth </li> <li>
     * defaultHeight </li> </ul> TITLE: <ul> <li> defaultTitleIsDisplaying </li>
     * <li> defaultTitle </li> <li> defaultTitleFont </li> <li>
     * defaultTitleAlignment </li> <li> defaultTitleFontColor </li> </ul>
     * DESCRIPTION: <ul> <li> defaultDescription </li> <li>
     * defaultDescriptionIsDisplaying </li> <li> defaultDescriptionFontColor
     * </li> <li> defaultDescriptionAlignment </li> <li> defaultDescriptionFont
     * </li> </ul>
     */
    protected abstract boolean setDefaultValues();

    /**
     * Based on
     * <code>properties</code>, determine whether this builder was used to build
     * <code>Item</code>.
     *
     * @param item the item to be tested
     * @return <code>true</code> if <code>item</code> was built by this *
     * builder, <code>false</code> otherwise
     */
    protected abstract boolean isBuilderForItem(Item item);

    /**
     * Builds and item based on
     * <code>graph</code> and
     * <code>attributeModel</code> data
     *
     * @param graph
     * @param attributeModel
     * @return
     */
    protected abstract Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel);

    /**
     * Used to determine if the
     * <code>item</code> has dynamic properties to be displayed in the
     * PropertySheet Editor
     *
     * @return <code>true</code> if <code>item</code> has dynamic * *
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
    protected abstract PreviewProperty[] createLegendOwnProperties(Item item);

    public abstract ArrayList<CustomLegendItemBuilder> getAvailableBuilders();

    public abstract Item createNewLegendItem(Graph graph);

    public Item createCustomItem(Integer newItemIndex, Graph graph, AttributeModel attributeModel, CustomLegendItemBuilder builder) {
        System.out.println("@Var: creating createCustomItem: ");
        Item item = buildCustomItem(builder, graph, attributeModel);
        createDefaultPropertiers(newItemIndex, item);
        return item;

    }

    private void createDefaultPropertiers(Integer newItemIndex, Item item) {
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);
        item.setData(LegendItem.PROPERTIES, createLegendProperties(item));
        item.setData(LegendItem.OWN_PROPERTIES, createLegendOwnProperties(item));
        item.setData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES, 0);
        item.setData(LegendItem.HAS_DYNAMIC_PROPERTIES, hasDynamicProperties());
        item.setData(LegendItem.DYNAMIC_PROPERTIES, new PreviewProperty[0]);
        item.setData(LegendItem.IS_SELECTED, Boolean.FALSE);
        item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.FALSE);
        item.setData(LegendItem.CURRENT_TRANSFORMATION, "");
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

    private PreviewProperty createLegendProperty(Item item, int property, Object value) {

        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, property);



        switch (property) {
            case LegendProperty.LABEL: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.label.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.label.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value + " " + itemIndex);
                break;
            }
            case LegendProperty.IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.isDisplaying.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.USER_ORIGIN_X: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.originX.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.originX.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.USER_ORIGIN_Y: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.originY.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.originY.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.WIDTH: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.width.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.width.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.HEIGHT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.height.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.height.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BACKGROUND_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.isDisplaying.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BACKGROUND_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BACKGROUND_BORDER_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.border.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.border.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BACKGROUND_BORDER_LINE_THICK: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.border.lineThick.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.border.lineThick.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.isDisplaying.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.alignment.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.isDisplaying.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.alignment.displayName"),
                        NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
            }

        }

        return previewProperty;

    }

    public PreviewProperty[] createLegendProperties(Item item) {

        System.out.printf("__HERE __\n" + defaultTitleIsDisplaying);
        if(setDefaultValues()){
            updateDefaultValues();
        }
        System.out.printf("__HERE __\n" + defaultTitleIsDisplaying);

//        ArrayList<String> legendProperties = LegendManager.getProperties(LegendProperty.LEGEND_PROPERTIES, itemIndex);

        int[] properties = LegendProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] previewProperties = new PreviewProperty[defaultValuesArrayList.size()];
        for (int i = 0; i < previewProperties.length; i++) {
            previewProperties[i] = createLegendProperty(item, properties[i], defaultValuesArrayList.get(i));
        }

        return previewProperties;
//
//        return new PreviewProperty[]{
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.LABEL),
//                                                   String.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.label.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.label.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultLabel + itemIndex),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.IS_DISPLAYING),
//                                                   Boolean.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.isDisplaying.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.isDisplaying.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultIsDisplaying),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.USER_ORIGIN_X),
//                                                   Float.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.originX.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.originX.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultOriginX),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.USER_ORIGIN_Y),
//                                                   Float.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.originY.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.originY.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultOriginY),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.WIDTH),
//                                                   Float.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.width.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.width.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultWidth),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.HEIGHT),
//                                                   Float.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.height.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.height.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultHeight),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.BACKGROUND_IS_DISPLAYING),
//                                                   Boolean.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.isDisplaying.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.isDisplaying.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultBackgroundIsDisplaying),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.BACKGROUND_COLOR),
//                                                   Color.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.color.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.color.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultBackgroundColor),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.BACKGROUND_BORDER_COLOR),
//                                                   Color.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.border.color.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.border.color.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultBackgroundBorderColor),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.BACKGROUND_BORDER_LINE_THICK),
//                                                   Integer.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.border.lineThick.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.background.border.lineThick.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultBackgroundBorderLineThick),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.TITLE_IS_DISPLAYING),
//                                                   Boolean.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.isDisplaying.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.isDisplaying.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultTitleIsDisplaying),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.TITLE),
//                                                   String.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultTitle),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.TITLE_FONT),
//                                                   Font.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultTitleFont),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.TITLE_FONT_COLOR),
//                                                   Color.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.color.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.color.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultTitleFontColor),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.TITLE_ALIGNMENT),
//                                                   Alignment.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.alignment.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.alignment.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultTitleAlignment),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.DESCRIPTION_IS_DISPLAYING),
//                                                   Boolean.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.isDisplaying.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.isDisplaying.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultTitleIsDisplaying),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.DESCRIPTION),
//                                                   String.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultDescription),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.DESCRIPTION_FONT),
//                                                   Font.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultDescriptionFont),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.DESCRIPTION_FONT_COLOR),
//                                                   Color.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.color.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.color.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultDescriptionFontColor),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.DESCRIPTION_ALIGNMENT),
//                                                   Alignment.class,
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.alignment.displayName"),
//                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.alignment.description"),
//                                                   PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultDescriptionAlignment)
//                };
    }

    public PreviewProperty[] getLegendProperties(Item item) {

        PreviewProperty[] legendProperties = createLegendProperties(item);
        PreviewProperty[] properties = createLegendOwnProperties(item);
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

    @Override
    public String toString() {
        return getTitle();
    }

    public abstract String getTitle();

    protected abstract void writeXMLFromData(XMLStreamWriter writer, Item item) throws XMLStreamException;

    protected abstract void writeXMLFromItemOwnProperties(XMLStreamWriter writer, Item item) throws XMLStreamException;

    protected void writeXMLFromSingleProperty(XMLStreamWriter writer, PreviewProperty property) throws XMLStreamException {
        Object propertyValue = property.getValue();
        if (propertyValue != null) {
            String text = PreviewProperties.getValueAsText(propertyValue);
            if (text == null) {
                text = writeValueAsText(propertyValue);
            }
            writer.writeStartElement(XML_PROPERTY);
            String name = LegendManager.getPropertyFromPreviewProperty(property);
            writer.writeAttribute(XML_NAME, name);
            writer.writeCharacters(text);
            writer.writeEndElement();
            System.out.println("@Saving: : " + XML_LEGEND_PROPERTY + " <> " + " name: " + name + " <> " + " value: " + text);
        }
    }

    public void writeXMLFromItem(XMLStreamWriter writer, Item item) throws XMLStreamException {

        // legend type
        writer.writeStartElement(XML_LEGEND_TYPE);
        writer.writeCharacters(item.getType());
        writer.writeEndElement();

        // legend properties
        writer.writeStartElement(XML_LEGEND_PROPERTY);
        PreviewProperty[] legendProperties = item.getData(LegendItem.PROPERTIES);
        for (PreviewProperty property : legendProperties) {
            writeXMLFromSingleProperty(writer, property);
        }
        writer.writeEndElement();

        // own properties
        writer.writeStartElement(XML_OWN_PROPERTY);
        writeXMLFromItemOwnProperties(writer, item);
        writer.writeEndElement();

        // dynamic properties
        writer.writeStartElement(XML_DYNAMIC_PROPERTY);
        PreviewProperty[] dynamicProperties = item.getData(LegendItem.DYNAMIC_PROPERTIES);
        for (PreviewProperty property : dynamicProperties) {
            writeXMLFromSingleProperty(writer, property);
        }
        writer.writeEndElement();


        // data
        writer.writeStartElement(XML_DATA);
        writeXMLFromData(writer, item);
        writer.writeEndElement();



    }

    public abstract void readXMLToData(XMLStreamReader reader, Item item) throws XMLStreamException;

    protected abstract PreviewProperty readXMLToSingleOwnProperty(XMLStreamReader reader, Item item) throws XMLStreamException;

    protected PreviewProperty readXMLToSingleLegendProperty(XMLStreamReader reader, Item item) throws XMLStreamException {
        String propertyName = reader.getAttributeValue(null, XML_NAME);
        String valueString = reader.getElementText();
        System.out.println("@Var: ReadingXML property: "+propertyName+" with value: "+valueString);
        int propertyIndex = LegendProperty.getInstance().getProperty(propertyName);
        Class valueClass = defaultValuesArrayList.get(propertyIndex).getClass();
        Object value = PreviewProperties.readValueFromText(valueString, valueClass);
        if (value == null) {
            value = readValueFromText(valueString, valueClass);
        }
        System.out.println("@Var: ReadingXML property: "+propertyName+" with value: "+value);
        
        PreviewProperty property = createLegendProperty(item, propertyIndex, value);
        return property;
    }

    protected Object readValueFromText(String valueStr, Class valueClass) {
        Object value = null;
        if (valueClass.equals(LegendItem.Alignment.class)) {
            value = availableAlignments[Integer.parseInt(valueStr)];
        }
        else if (valueClass.equals(LegendItem.Shape.class)) {
            value = availableShapes[Integer.parseInt(valueStr)];
        }
        else if (valueClass.equals(LegendItem.Direction.class)) {
            value = availableDirections[Integer.parseInt(valueStr)];
        }
        else if (valueClass.equals(Boolean.class)) {
            value = Boolean.parseBoolean(valueStr);
        }
        else if (valueClass.equals(Integer.class)) {
            value = Integer.parseInt(valueStr);
        }
        else if (valueClass.equals(File.class)) {
            value = new File(valueStr);
        }
        return value;
    }

    protected abstract ArrayList<PreviewProperty> readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException;

    protected abstract ArrayList<PreviewProperty> readXMLToDynamicProperties(XMLStreamReader reader, Item item) throws XMLStreamException;

    private ArrayList<PreviewProperty> readXMLToLegendProperties(XMLStreamReader reader, Item item) throws XMLStreamException {


        ArrayList<PreviewProperty> properties = new ArrayList<PreviewProperty>();

        // legend properties

        boolean end = false;
        while (reader.hasNext() && !end) {
            int type = reader.next();
            switch (type) {
                case XMLStreamReader.START_ELEMENT: {
                    PreviewProperty property = readXMLToSingleLegendProperty(reader, item);
                    properties.add(property);
                    break;
                }
                case XMLStreamReader.CHARACTERS: {
                    break;
                }
                case XMLStreamReader.END_ELEMENT: {
                    end = true;
                    break;
                }
            }
        }

        return properties;

    }

    public Item readXMLToItem(XMLStreamReader reader, Integer newItemIndex) throws XMLStreamException {


        Item item = createNewLegendItem(null);
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);

        // opening legend properties
        reader.nextTag();
        System.out.println("@Var: reader: "+reader.getLocalName());
        System.out.println("@Var: legend properties: ");
        ArrayList<PreviewProperty> legendProperties = readXMLToLegendProperties(reader, item);

        // opening own properties
        reader.nextTag();
        System.out.println("@Var: reader: "+reader.getLocalName());
        System.out.println("@Var: own properties: ");
        ArrayList<PreviewProperty> ownProperties = readXMLToOwnProperties(reader, item);

        // opening dynamic properties
        reader.nextTag();
        System.out.println("@Var: reader: "+reader.getLocalName());
        System.out.println("@Var: dynamic properties: ");
        ArrayList<PreviewProperty> dynamicProperties = readXMLToDynamicProperties(reader, item);
        // closing dynamic properties

        // data
//        reader.nextTag();
//        System.out.println("@Var: reader: "+reader.getLocalName());
        reader.nextTag();
        System.out.println("@Var: reader: "+reader.getLocalName());
        System.out.println("@Var: data: ");
        readXMLToData(reader, item);
        
        // finish reading
        reader.next();
        

//        // dynamic properties
//        System.out.println("@Var: dynamic properties: ");
//        boolean end = false;
//        while (reader.hasNext() && !end) {
//            int type = reader.next();
//            switch (type) {
//                case XMLStreamReader.START_ELEMENT: {
////                    String propertyType = reader.getLocalName();
////                    PreviewProperty property = readXMLToSingleOwnProperty(reader, item);
////                    legendProperties.add(property);
//                    break;
//                }
//                case XMLStreamReader.CHARACTERS: {
//                    break;
//                }
//                case XMLStreamReader.END_ELEMENT: {
//                    end = true;
//                    break;
//                }
//            }
//        }





        PreviewProperty[] legendPropertiesArray = legendProperties.toArray(new PreviewProperty[legendProperties.size()]);
        PreviewProperty[] ownPropertiesArray = ownProperties.toArray(new PreviewProperty[ownProperties.size()]);
        PreviewProperty[] dynamicPropertiesArray = dynamicProperties.toArray(new PreviewProperty[dynamicProperties.size()]);
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);
        item.setData(LegendItem.PROPERTIES, legendPropertiesArray);
        item.setData(LegendItem.OWN_PROPERTIES, ownPropertiesArray);
        item.setData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES, 0);
        boolean hasDynamicProperties = (dynamicProperties.size() > 0);
        item.setData(LegendItem.HAS_DYNAMIC_PROPERTIES, hasDynamicProperties);
        item.setData(LegendItem.DYNAMIC_PROPERTIES, dynamicPropertiesArray);
        item.setData(LegendItem.IS_SELECTED, Boolean.FALSE);
        item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.FALSE);
        item.setData(LegendItem.CURRENT_TRANSFORMATION, "");

        return item;
    }

    protected String writeValueAsText(Object propertyValue) {
        System.out.println("@Var: interpreting propertyValue: " + propertyValue.getClass());

        String text = null;
        if (propertyValue instanceof LegendItem.Alignment) {
            LegendItem.Alignment propertyValueAlignment = (LegendItem.Alignment) propertyValue;
            text = propertyValueAlignment.getValue();
        }
        else if (propertyValue instanceof LegendItem.Direction) {
            LegendItem.Direction propertyValueAlignment = (LegendItem.Direction) propertyValue;
            text = propertyValueAlignment.getValue();
        }
        else if (propertyValue instanceof LegendItem.Shape) {
            LegendItem.Shape propertyValueAlignment = (LegendItem.Shape) propertyValue;
            text = propertyValueAlignment.getValue();
        }
        else {
            text = propertyValue.toString();
        }
        return text;
    }

    // xml 
    protected static final String XML_PROPERTY = "property";
    private static final String XML_LEGEND_TYPE = "legendtype";
    private static final String XML_DYNAMIC_PROPERTY = "dynamicproperty";
    private static final String XML_LEGEND_PROPERTY = "legendproperty";
    protected static final String XML_OWN_PROPERTY = "itemproperty";
    protected static final String XML_NAME = "name";
    private static final String XML_DATA = "itemdata";
    //DEFAULT VALUES 
    // BACKGROUND AND BORDER
    protected Boolean defaultBackgroundIsDisplaying = Boolean.TRUE;
    protected Color defaultBackgroundColor = Color.WHITE;
    protected Color defaultBackgroundBorderColor = Color.BLACK;
    protected Integer defaultBackgroundBorderLineThick = 1;
    // LABEL
    protected String defaultLabel = "";
    // IS_DISPLAYING
    protected Boolean defaultIsDisplaying = Boolean.TRUE;
    //ORIGIN
    protected Float defaultOriginX = 0f;
    protected Float defaultOriginY = 0f;
    //WIDTH
    protected Float defaultWidth = 500f;
    protected Float defaultHeight = 300f;
    // TITLE
    protected Boolean defaultTitleIsDisplaying = Boolean.TRUE;
    protected String defaultTitle = "TITLE";
    protected Font defaultTitleFont = new Font("Arial", Font.BOLD, 30);
    protected Alignment defaultTitleAlignment = Alignment.CENTER;
    protected Color defaultTitleFontColor = Color.BLACK;
    // DESCRIPTION
    protected String defaultDescription = "description ... description ...description ...description ...description ...description ...description ...description ...description ...description ...description ...description ...";
    protected Boolean defaultDescriptionIsDisplaying = true;
    protected Color defaultDescriptionFontColor = Color.BLACK;
    protected Alignment defaultDescriptionAlignment = Alignment.LEFT;
    protected Font defaultDescriptionFont = new Font("Arial", Font.PLAIN, 10);
    // default values list
    private ArrayList<Object> defaultValuesArrayList;

    public LegendItemBuilder() {
        updateDefaultValues();
    }
    
    

    public void updateDefaultValues() {
        this.defaultValuesArrayList = new ArrayList<Object>();
        defaultValuesArrayList.add(this.defaultLabel);
        defaultValuesArrayList.add(this.defaultIsDisplaying);
        defaultValuesArrayList.add(this.defaultOriginX);
        defaultValuesArrayList.add(this.defaultOriginY);
        defaultValuesArrayList.add(this.defaultWidth);
        defaultValuesArrayList.add(this.defaultHeight);
        defaultValuesArrayList.add(this.defaultBackgroundIsDisplaying);
        defaultValuesArrayList.add(this.defaultBackgroundColor);
        defaultValuesArrayList.add(this.defaultBackgroundBorderColor);
        defaultValuesArrayList.add(this.defaultBackgroundBorderLineThick);
        defaultValuesArrayList.add(this.defaultTitleIsDisplaying);
        defaultValuesArrayList.add(this.defaultTitle);
        defaultValuesArrayList.add(this.defaultTitleFont);
        defaultValuesArrayList.add(this.defaultTitleFontColor);
        defaultValuesArrayList.add(this.defaultTitleAlignment);
        defaultValuesArrayList.add(this.defaultDescriptionIsDisplaying);
        defaultValuesArrayList.add(this.defaultDescription);
        defaultValuesArrayList.add(this.defaultDescriptionFont);
        defaultValuesArrayList.add(this.defaultDescriptionFontColor);
        defaultValuesArrayList.add(this.defaultDescriptionAlignment);
    }

    private final Object[] availableAlignments = {
        LegendItem.Alignment.LEFT,
        LegendItem.Alignment.RIGHT,
        LegendItem.Alignment.CENTER,
        LegendItem.Alignment.JUSTIFIED
    };
    private final Object[] availableShapes = {
        LegendItem.Shape.RECTANGLE,
        LegendItem.Shape.CIRCLE,
        LegendItem.Shape.TRIANGLE
    };
    private final Object[] availableDirections = {
        LegendItem.Direction.UP,
        LegendItem.Direction.DOWN,
        LegendItem.Direction.LEFT,
        LegendItem.Direction.RIGHT
    };
}
