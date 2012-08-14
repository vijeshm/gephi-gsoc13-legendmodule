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
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.api.CustomTextItemBuilder;
import org.gephi.legend.items.LegendItem;
import org.gephi.legend.items.LegendItem.Alignment;
import org.gephi.legend.items.TextItem;
import org.gephi.legend.manager.LegendManager;
import org.gephi.legend.properties.TextProperty;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
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
    @ServiceProvider(service = ItemBuilder.class, position = 101),
    @ServiceProvider(service = LegendItemBuilder.class, position = 101)
})
public class TextItemBuilder extends LegendItemBuilder {

    @Override
    protected void setDefaultValues() {
    }

    @Override
    protected boolean isBuilderForItem(Item item) {
        return item instanceof TextItem;
    }

    @Override
    public String getType() {
        return TextItem.LEGEND_TYPE;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(LegendManager.class, "TextItem.name");
    }

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new TextItem(graph);
    }

    @Override
    protected Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel) {
        CustomTextItemBuilder customBuilder = (CustomTextItemBuilder) builder;
        Item item = createNewLegendItem(graph);
        item.setData(TextItem.BODY, customBuilder.getText());
        return item;
    }

    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendManager.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, property);

        switch (property) {
            case TextProperty.TEXT_BODY: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TextProperty.TEXT_BODY_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TextProperty.TEXT_BODY_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.color.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TextProperty.TEXT_BODY_FONT_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.alignment.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TextItem.property.body.alignment.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }
        }
        return previewProperty;
    }

    @Override
    protected PreviewProperty[] createLegendOwnProperties(Item item) {


        int[] properties = TextProperty.LIST_OF_PROPERTIES;

        System.out.println("@Var: properties: " + properties.length);
        PreviewProperty[] previewProperties = new PreviewProperty[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            System.out.println("@Var: i: " + i);
            previewProperties[i] = createLegendProperty(item, properties[i], defaultValues[i]);
        }

        return previewProperties;
//
//        ArrayList<String> textProperties = LegendManager.getProperties(TextProperty.OWN_PROPERTIES, itemIndex);
//
//        PreviewProperty[] properties = {
//            PreviewProperty.createProperty(this,
//                                           textProperties.get(TextProperty.TEXT_BODY),
//                                           String.class,
//                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(item.getData(TextItem.BODY)),
//            PreviewProperty.createProperty(this,
//                                           textProperties.get(TextProperty.TEXT_BODY_FONT),
//                                           Font.class,
//                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultBodyFont),
//            PreviewProperty.createProperty(this,
//                                           textProperties.get(TextProperty.TEXT_BODY_FONT_COLOR),
//                                           Color.class,
//                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.color.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.color.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultBodyFontColor),
//            PreviewProperty.createProperty(this,
//                                           textProperties.get(TextProperty.TEXT_BODY_FONT_ALIGNMENT),
//                                           Alignment.class,
//                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.alignment.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.alignment.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultBodyFontAlignment)
//        };
//
//
//        return properties;
    }

    @Override
    protected Boolean hasDynamicProperties() {
        return Boolean.FALSE;
    }

    @Override
    public ArrayList<CustomLegendItemBuilder> getAvailableBuilders() {
        Collection<? extends CustomTextItemBuilder> customBuilders = Lookup.getDefault().lookupAll(CustomTextItemBuilder.class);
        ArrayList<CustomLegendItemBuilder> availableBuilders = new ArrayList<CustomLegendItemBuilder>();
        for (CustomTextItemBuilder customBuilder : customBuilders) {
            availableBuilders.add((CustomLegendItemBuilder) customBuilder);
        }
        return availableBuilders;
    }

    @Override
    protected void writeDataToXML(XMLStreamWriter writer, Item item) throws XMLStreamException {
    }

    @Override
    public void readXMLToData(XMLStreamReader reader, Item item) throws XMLStreamException {
    }

    @Override
    public PreviewProperty readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException {
        String propertyName = reader.getAttributeValue(null, XML_NAME);
        int propertyIndex = TextProperty.getInstance().getProperty(propertyName);
        String valueString = reader.getElementText();
        Class valueClass = defaultValues[propertyIndex].getClass();
        System.out.println("@Var: valueClass: " + valueClass);
        Object value = PreviewProperties.readValueFromText(valueString, valueClass);
        if (value == null) {
            value = readValueFromText(valueString, valueClass);
        }
        System.out.println("@Var: reading propertyType: " + propertyName + " with value: " + value);
        PreviewProperty property = createLegendProperty(item, propertyIndex, value);
        return property;
    }

    @Override
    protected void writeItemOwnPropertiesToXML(XMLStreamWriter writer, Item item) throws XMLStreamException {
        writer.writeStartElement(XML_OWN_PROPERTY);
        PreviewProperty[] ownProperties = item.getData(LegendItem.OWN_PROPERTIES);
        for (PreviewProperty property : ownProperties) {
            String propertyName = property.getName();
            Object propertyValue = property.getValue();
            if (propertyValue != null) {
                String text = null;
                text = PreviewProperties.getValueAsText(propertyValue);
                if(text == null){
                    text = writeValueAsText(propertyValue);
                }
                writer.writeStartElement(XML_PROPERTY);
                String name = LegendManager.getPropertyFromPreviewProperty(property);
                writer.writeAttribute(XML_NAME, name);
                writer.writeCharacters(text);
                writer.writeEndElement();
                System.out.println("@Saving: : " + XML_PROPERTY + " <> " + " name: " + name + " <> " + " value: " + text);
            }
        }
        writer.writeEndElement();
    }

    // DEFAULT VALUES
    protected final String defaultBody = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras aliquam luctus ligula. Nunc mollis sagittis dui eget congue. Sed et turpis leo, vitae interdum magna. Pellentesque sollicitudin laoreet orci. Donec varius eleifend iaculis. Integer congue tempor nulla ac luctus. Nullam velit massa, convallis ut suscipit eget, auctor non velit. Etiam vitae velit sit amet justo luctus semper. Ut laoreet ullamcorper.";
    protected final Font defaultBodyFont = new Font("Arial", Font.PLAIN, 14);
    protected final Color defaultBodyFontColor = Color.BLUE;
    protected final Alignment defaultBodyFontAlignment = Alignment.JUSTIFIED;
    private final Object[] defaultValues = {
        defaultBody,
        defaultBodyFont,
        defaultBodyFontColor,
        defaultBodyFontAlignment
    };
}
