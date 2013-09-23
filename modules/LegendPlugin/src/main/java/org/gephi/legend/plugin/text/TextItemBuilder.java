package org.gephi.legend.plugin.text;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.attribute.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.AbstractLegendItemBuilder;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.spi.CustomLegendItemBuilder;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.legend.spi.LegendItemBuilder;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * class to build the text items.
 *
 * This class is exposed as a service. The createCustomItem method (in the
 * AbstractLegendItemRenderer) is used to create a text legend item, depending
 * on the custom item builder chosen by the user from the UI. The custom
 * builders are expected to implement the CustomTextItemBuilder interface.
 *
 * @author mvvijesh, edubecks
 */
@ServiceProviders(value = {
    @ServiceProvider(service = ItemBuilder.class, position = 105),
    @ServiceProvider(service = LegendItemBuilder.class, position = 105)
})
public class TextItemBuilder extends AbstractLegendItemBuilder {

    // default values
    protected final String defaultBody = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras aliquam luctus ligula. Nunc mollis sagittis dui eget congue. Sed et turpis leo, vitae interdum magna. Pellentesque sollicitudin laoreet orci. Donec varius eleifend iaculis. Integer congue tempor nulla ac luctus. Nullam velit massa, convallis ut suscipit eget, auctor non velit. Etiam vitae velit sit amet justo luctus semper. Ut laoreet ullamcorper.";
    protected final Font defaultBodyFont = new Font("Arial", Font.PLAIN, 14);
    protected final Color defaultBodyFontColor = Color.BLACK;
    protected final LegendItem.Alignment defaultBodyFontAlignment = LegendItem.Alignment.LEFT;
    private final Object[] defaultValues = {
        defaultBody,
        defaultBodyFont,
        defaultBodyFontColor,
        defaultBodyFontAlignment
    };

    @Override
    public boolean setDefaultValues() {
        return false;
    }

    /**
     *
     * @param item - item to be checked against
     * @return True if GroupsItemBuilder can build the item. False, otherwise.
     */
    @Override
    public boolean isBuilderForItem(Item item) {
        return item instanceof TextItem;
    }

    @Override
    public String getType() {
        return TextItem.LEGEND_TYPE;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(TextItemBuilder.class, "TextItem.name");
    }

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new TextItem(graph);
    }

    /**
     *
     * @param builder - the custom text item builder chosen by the user from the
     * UI
     * @param graph - the current graph to which the text item belongs to
     * @param attributeModel
     * @param newItemIndex - index of the new text item being created
     * @return the newly built text legend item
     */
    @Override
    public Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel, Integer newItemIndex) {
        Item item = createNewLegendItem(graph);

        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();

        // add the renderer to the legend model if it has not been added
        TextItemRenderer textItemRenderer = TextItemRenderer.getInstance();
        if (!legendModel.isRendererAdded(textItemRenderer)) {
            legendModel.addRenderer(textItemRenderer);
        }

        // setting default renderer and item index
        item.setData(LegendItem.RENDERER, textItemRenderer);
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);
        item.setData(LegendItem.CUSTOM_BUILDER, (CustomTextItemBuilder) builder);
        return item;
    }

    /**
     *
     * @param item - the text item being built
     * @param property - the index of the property
     * @param value - the value of the property
     * @return the PreviewProperty object populated with the property string and
     * the value
     */
    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendModel.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, property);

        switch (property) {
            case TextProperty.TEXT_BODY: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.displayName"),
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TextProperty.TEXT_BODY_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.font.displayName"),
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TextProperty.TEXT_BODY_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.font.color.displayName"),
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TextProperty.TEXT_BODY_FONT_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.alignment.displayName"),
                        NbBundle.getMessage(TextItemBuilder.class, "TextItem.property.body.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
        }
        return previewProperty;
    }

    /**
     *
     * @param item - the text item being built
     * @return the list of PreviewProperty objects
     */
    @Override
    public PreviewProperty[] createLegendOwnProperties(Item item) {
        int[] properties = TextProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] legendPreviewProperties = new PreviewProperty[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            legendPreviewProperties[i] = createLegendProperty(item, properties[i], defaultValues[i]);
        }

        // The text legend doesnt need any extra data from any other module in order to be rendered.
        // If the renderer needs extra data, add a method in the CustomTextItemBuilder interface to retrieve the data.
        // extract the CustomTextItemBuilder from the item and invoke the method to retrieve data.
        // Once the required data is retrieved, add it to the text item in any appropriate form.

        return legendPreviewProperties;
    }

    @Override
    public Boolean hasDynamicProperties() {
        return Boolean.FALSE;
    }

    /**
     *
     * @return the list of available custom builders for the text legend
     */
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
    public void writeXMLFromItemOwnProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {

        PreviewProperty[] ownProperties = item.getData(LegendItem.OWN_PROPERTIES);
        for (PreviewProperty property : ownProperties) {
            writeXMLFromSingleProperty(writer, property, previewProperties);
        }
    }

    @Override
    public ArrayList<PreviewProperty> readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException {

        ArrayList<PreviewProperty> properties = new ArrayList<PreviewProperty>();

        // own properties

        boolean end = false;
        while (reader.hasNext() && !end) {
            int type = reader.next();
            switch (type) {
                case XMLStreamReader.START_ELEMENT: {
                    PreviewProperty property = readXMLToSingleOwnProperty(reader, item);
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

    public PreviewProperty readXMLToSingleOwnProperty(XMLStreamReader reader, Item item) throws XMLStreamException {
        String propertyName = reader.getAttributeValue(null, XML_NAME);
        String valueString = reader.getElementText();
        int propertyIndex = TextProperty.getInstance().getProperty(propertyName);
        Class valueClass = defaultValues[propertyIndex].getClass();
        Object value = readValueFromText(valueString, valueClass);
        PreviewProperty property = createLegendProperty(item, propertyIndex, value);
        return property;
    }
}