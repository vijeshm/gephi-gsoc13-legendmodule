/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.CustomDescriptionItemBuilder;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.api.DescriptionItemElementValue;
import org.gephi.legend.builders.description.elements.CustomValue;
import org.gephi.legend.items.DescriptionItem;
import org.gephi.legend.items.DescriptionItemElement;
import org.gephi.legend.items.LegendItem;
import org.gephi.legend.items.LegendItem.Alignment;
import org.gephi.legend.manager.LegendManager;
import org.gephi.legend.properties.DescriptionProperty;
import org.gephi.legend.properties.LegendProperty;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author edubecks
 */
@ServiceProviders(value = {
    @ServiceProvider(service = ItemBuilder.class, position = 104),
    @ServiceProvider(service = LegendItemBuilder.class, position = 104)
})
public class DescriptionItemBuilder extends LegendItemBuilder {

    @Override
    protected void setDefaultValues() {
    }

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new DescriptionItem(graph);
    }

    @Override
    protected Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel) {
        Item item = createNewLegendItem(graph);
        item.setData(LegendItem.LEGEND_ITEM, item);
        return item;
    }

    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendManager.getProperty(DescriptionProperty.OWN_PROPERTIES, itemIndex, property);

        switch (property) {
            case DescriptionProperty.DESCRIPTION_KEY_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.displayName"),
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case DescriptionProperty.DESCRIPTION_KEY_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.color.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case DescriptionProperty.DESCRIPTION_KEY_FONT_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.alignment.displayName"),
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.alignment.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case DescriptionProperty.DESCRIPTION_VALUE_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.displayName"),
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case DescriptionProperty.DESCRIPTION_VALUE_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.color.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case DescriptionProperty.DESCRIPTION_VALUE_FONT_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.alignment.displayName"),
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.alignment.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case DescriptionProperty.DESCRIPTION_IS_FLOW_LAYOUT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.isFlowLayout.displayName"),
                        NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.isFlowLayout.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case DescriptionProperty.DESCRIPTION_TEMP: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        DescriptionItemElement.class,
                        "temp",
                        "temp",
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

        }

        return previewProperty;
    }

    @Override
    protected PreviewProperty[] createLegendOwnProperties(Item item) {

        int[] properties = DescriptionProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] previewProperties = new PreviewProperty[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            System.out.println("@Var: i: " + i);
            previewProperties[i] = createLegendProperty(item, properties[i], defaultValues[i]);
        }

        return previewProperties;

//        PreviewProperty[] properties = {
//            PreviewProperty.createProperty(this,
//                                           legendProperties.get(DescriptionProperty.DESCRIPTION_KEY_FONT),
//                                           Font.class,
//                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultKeyFont),
//            PreviewProperty.createProperty(this,
//                                           legendProperties.get(DescriptionProperty.DESCRIPTION_KEY_FONT_COLOR),
//                                           Color.class,
//                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.color.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.color.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultKeyFontColor),
//            PreviewProperty.createProperty(this,
//                                           legendProperties.get(DescriptionProperty.DESCRIPTION_KEY_FONT_ALIGNMENT),
//                                           Alignment.class,
//                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.alignment.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.alignment.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultKeyAlignment),
//            PreviewProperty.createProperty(this,
//                                           legendProperties.get(DescriptionProperty.DESCRIPTION_VALUE_FONT),
//                                           Font.class,
//                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultValueFont),
//            PreviewProperty.createProperty(this,
//                                           legendProperties.get(DescriptionProperty.DESCRIPTION_VALUE_FONT_COLOR),
//                                           Color.class,
//                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.color.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.color.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultValueFontColor),
//            PreviewProperty.createProperty(this,
//                                           legendProperties.get(DescriptionProperty.DESCRIPTION_VALUE_FONT_ALIGNMENT),
//                                           Alignment.class,
//                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.alignment.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.alignment.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultValueAlignment),
//            PreviewProperty.createProperty(this,
//                                           legendProperties.get(DescriptionProperty.DESCRIPTION_IS_FLOW_LAYOUT),
//                                           Boolean.class,
//                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.isFlowLayout.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.isFlowLayout.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsFlowLayout),
//            PreviewProperty.createProperty(this,
//                                           legendProperties.get(DescriptionProperty.DESCRIPTION_TEMP),
//                                           DescriptionItemElement.class,
//                                           "temp",
//                                           "temp",
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultDescriptionElement),};
//
//
//        return properties;
    }
//
//    public static boolean updatePreviewProperty(Item item, Integer numOfProperties) {
//        // item index
//        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
//        Integer currentNumOfPropertiews = item.getData(DescriptionItem.NUMBER_OF_ITEMS);
//        System.out.println("@Var: currentNumOfPropertiews: " + currentNumOfPropertiews);
//        System.out.println("@Var: numOfProperties: " + numOfProperties);
//        // number of items didn't change
//        if (numOfProperties.intValue() == currentNumOfPropertiews.intValue()) {
//            return false;
//        }
//        // adding properties
//        else if (numOfProperties.intValue() > currentNumOfPropertiews.intValue()) {
//            int newProperties = numOfProperties.intValue() - currentNumOfPropertiews.intValue();
//            DescriptionItemBuilder.addPreviewProperty(item, currentNumOfPropertiews.intValue(), newProperties);
//        }
//        // removing properties
//        else{
//            int removeProperties = currentNumOfPropertiews.intValue() - numOfProperties.intValue();
//            removePreviewProperty(item, removeProperties);
//        }
//        item.setData(DescriptionItem.NUMBER_OF_ITEMS, numOfProperties);
//        return true;
//    }

    protected static void removePreviewProperty(Item item, int numOfProperties) {
        // item index
        PreviewProperty[] itemProperties = item.getData(LegendItem.DYNAMIC_PROPERTIES);
        PreviewProperty[] newDescriptionProperties = new PreviewProperty[itemProperties.length - numOfProperties * 2];
        System.arraycopy(itemProperties, 0, newDescriptionProperties, 0, newDescriptionProperties.length);
        //
        item.setData(LegendItem.DYNAMIC_PROPERTIES, newDescriptionProperties);
    }

    protected static void addPreviewProperty(Item item, int begin, int numOfProperties) {
        System.out.println("@Var: appendPreviewProperty: " + numOfProperties);
        // item index
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        PreviewProperty[] itemProperties = item.getData(LegendItem.DYNAMIC_PROPERTIES);


        // creating new PreviewProperty[]
        PreviewProperty[] newDescriptionProperties = new PreviewProperty[numOfProperties * 2];
        for (int i = 0; i < numOfProperties; i++) {

            int dataIndex = begin + i;
            String key = new String(NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.displayName") + " " + dataIndex);
            System.out.println("@Var: key: " + key);
            String keyProperty = LegendManager.getDynamicProperty(DescriptionProperty.OWN_PROPERTIES[DescriptionProperty.DESCRIPTION_KEY], itemIndex, dataIndex);
            System.out.println("@Var: appendPreviewProperty key: " + keyProperty);
            String valueProperty = LegendManager.getDynamicProperty(DescriptionProperty.OWN_PROPERTIES[DescriptionProperty.DESCRIPTION_VALUE], itemIndex, dataIndex);
            System.out.println("@Var: appendPreviewProperty value: " + valueProperty);
            DescriptionItemElementValue customValue = new CustomValue();
            DescriptionItemElement value = new DescriptionItemElement(customValue, (NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.displayName") + " " + dataIndex));
            System.out.println("@Var: value: " + value);
            newDescriptionProperties[2 * i] = PreviewProperty.createProperty(
                    item,
                    keyProperty,
                    String.class,
                    NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.displayName") + " " + dataIndex,
                    NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.description") + " " + dataIndex,
                    LegendProperty.DYNAMIC_CATEGORY).setValue(key);
            newDescriptionProperties[2 * i + 1] = PreviewProperty.createProperty(
                    item,
                    valueProperty,
                    DescriptionItemElement.class,
                    NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.displayName") + " " + dataIndex,
                    NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.description") + " " + dataIndex,
                    LegendProperty.DYNAMIC_CATEGORY).setValue(value);
        }

        // appending
        PreviewProperty[] previewProperties = new PreviewProperty[itemProperties.length + newDescriptionProperties.length];
        System.arraycopy(itemProperties, 0, previewProperties, 0, itemProperties.length);
        System.arraycopy(newDescriptionProperties, 0, previewProperties, itemProperties.length, newDescriptionProperties.length);

        for (PreviewProperty previewProperty : previewProperties) {
            System.out.println("@Var: adding previewProperty: " + previewProperty);
            System.out.println("@Var: adding previewProperty: " + previewProperty.getName());

        }

        item.setData(LegendItem.DYNAMIC_PROPERTIES, previewProperties);

    }

    @Override
    public String getType() {
        return DescriptionItem.LEGEND_TYPE;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(LegendManager.class, "DescriptionItem.name");
    }

    private Font defaultKeyFont = new Font("Arial", Font.PLAIN, 13);
    private Color defaultKeyFontColor = Color.RED;
    private Font defaultValueFont = new Font("Arial", Font.BOLD, 16);
    private Color defaultValueFontColor = Color.BLUE;
    private Alignment defaultKeyAlignment = Alignment.LEFT;
    private Alignment defaultValueAlignment = Alignment.LEFT;
    private Boolean defaultIsFlowLayout = true;
//    public static DescriptionItemElementValue defaultDescriptionItemElementValue = new CustomValue();
    public static DescriptionItemElement defaultDescriptionElement = DescriptionItemElement.getDefaultGenerator();
    private final Object[] defaultValues = {
        defaultKeyFont,
        defaultKeyFontColor,
        defaultKeyAlignment,
        defaultValueFont,
        defaultValueFontColor,
        defaultValueAlignment
    };

    @Override
    protected boolean isBuilderForItem(Item item) {
        return item instanceof DescriptionItem;
    }

    @Override
    protected Boolean hasDynamicProperties() {
        return Boolean.TRUE;
    }

    @Override
    public ArrayList<CustomLegendItemBuilder> getAvailableBuilders() {
        Collection<? extends CustomDescriptionItemBuilder> customBuilders = Lookup.getDefault().lookupAll(CustomDescriptionItemBuilder.class);
        ArrayList<CustomLegendItemBuilder> availableBuilders = new ArrayList<CustomLegendItemBuilder>();
        for (CustomDescriptionItemBuilder customBuilder : customBuilders) {
            availableBuilders.add((CustomLegendItemBuilder) customBuilder);
        }
        return availableBuilders;
    }

    @Override
    public void writeDataToXML(XMLStreamWriter writer, Item item) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void readXMLToData(XMLStreamReader reader, Item item) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PreviewProperty readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeItemOwnPropertiesToXML(XMLStreamWriter writer, Item item) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
