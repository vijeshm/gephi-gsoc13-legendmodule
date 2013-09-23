package org.gephi.legend.plugin.groups;

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
import org.gephi.legend.spi.LegendItem.Direction;
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
 * class to build the group items.
 *
 * This class is exposed as a service. The createCustomItem method (in the
 * AbstractLegendItemRenderer) is used to create a group legend item, depending
 * on the custom item builder chosen by the user from the UI. A group item
 * layout is composed of a number of group elements. These group elements
 * consists of two entities: a label and a shape. The data for these group
 * elements is provided by a method in the custom group builder. Every custom
 * group builder is expected to implement the CustomGroupsItemBuilder interface.
 *
 * @author mvvijesh, edubecks
 */
@ServiceProviders(value = {
    @ServiceProvider(service = ItemBuilder.class, position = 102),
    @ServiceProvider(service = LegendItemBuilder.class, position = 102)
})
public class GroupsItemBuilder extends AbstractLegendItemBuilder {

    // define the default values preview properties
    private LegendItem.Shape defaultShape = LegendItem.Shape.RECTANGLE;
    private Float defaultShapeWidthFraction = 0.8f; // shape width fraction is the fraction of the element width that the shape occupies
    private LegendItem.Direction defaultLabelPosition = Direction.DOWN; // this label can take two value: Direction.UP and Direction.DOWN
    private Font defaultLabelFont = new Font("Arial", Font.PLAIN, 15);
    private Color defaultLabelFontColor = Color.BLACK;
    private Alignment defaultLabelFontAlignment = Alignment.CENTER;
    private Integer defaultPaddingBetweenTextAndShape = 5; // the veritcal space between the element and the shape
    private Integer defaultPaddingBetweenElements = 20; // the horizontal space between consectutive elements
    private final Object[] defaultValues = {
        defaultShape,
        defaultShapeWidthFraction,
        defaultLabelPosition,
        defaultLabelFont,
        defaultLabelFontColor,
        defaultLabelFontAlignment,
        defaultPaddingBetweenTextAndShape,
        defaultPaddingBetweenElements};

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
        return item instanceof GroupsItem;
    }

    @Override
    public String getType() {
        return GroupsItem.LEGEND_TYPE;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.name");
    }

    /**
     *
     * @param item - the group item being built
     * @param property - the index of the property
     * @param value - the value of the property
     * @return the PreviewProperty object populated with the property string and
     * the value
     */
    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, property);

        switch (property) {

            case GroupsProperty.GROUPS_SHAPE: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        LegendItem.Shape.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.shape.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.shape.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_SHAPE_WIDTH_FRACTION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.shape.width.fraction.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.shape.width.fraction.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_LABEL_POSITION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        LegendItem.Direction.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.label.position.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.label.position.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_LABEL_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.label.font.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.label.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_LABEL_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.label.font.color.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.label.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_LABEL_FONT_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.label.font.alignment.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.label.font.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_PADDING_BETWEEN_TEXT_AND_SHAPE: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.paddingBetweenTextAndShape.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.paddingBetweenTextAndShape.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_PADDING_BETWEEN_ELEMENTS: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.paddingBetweenElements.displayName"),
                        NbBundle.getMessage(GroupsItemBuilder.class, "GroupsItem.property.paddingBetweenElements.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
        }
        return previewProperty;
    }

    /**
     *
     * @param item - the groups item being built
     * @return the list of PreviewProperty objects
     */
    @Override
    public PreviewProperty[] createLegendOwnProperties(Item item) {
        GroupsItem groupsItem = (GroupsItem) item;
        int[] properties = GroupsProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] previewProperties = new PreviewProperty[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            previewProperties[i] = createLegendProperty(item, properties[i], defaultValues[i]);
        }

        // the groups legend must be populated with the data that needs to rendered.
        // the custmomGroupsBuilder associated with the item provides this data.
        // see CustomGroupsItemBuilder interface and Default custom builder for more information
        CustomGroupsItemBuilder customGroupsBuilder = (CustomGroupsItemBuilder) item.getData(LegendItem.CUSTOM_BUILDER);
        // fetch the data to be rendered from the customGroupsBuilder, by populating the groups list.
        ArrayList<GroupElement> groups = customGroupsBuilder.retrieveData(groupsItem);

        // set the populated groups list in the groups item being built
        groupsItem.setGroups(groups);

        return previewProperties;
    }

    @Override
    public Boolean hasDynamicProperties() {
        return Boolean.FALSE;
    }

    /**
     *
     * @param builder - the custom group item builder chosen by the user from
     * the UI
     * @param graph - the current graph to which the groups item belongs to
     * @param attributeModel
     * @param newItemIndex - index of the new groups item being created
     * @return the newly built groups legend item
     */
    @Override
    public Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel, Integer newItemIndex) {
        Item item = createNewLegendItem(graph);

        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();

        // add the renderer to the legend model if it has not been added
        GroupsItemRenderer groupsItemRenderer = GroupsItemRenderer.getInstance();
        if (!legendModel.isRendererAdded(groupsItemRenderer)) {
            legendModel.addRenderer(groupsItemRenderer);
        }

        // setting default renderer, item index and custom builder
        item.setData(LegendItem.RENDERER, groupsItemRenderer);
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);
        item.setData(LegendItem.CUSTOM_BUILDER, (CustomGroupsItemBuilder) builder);
        return item;
    }

    /**
     *
     * @return the list of available custom builders for the groups legend
     */
    @Override
    public ArrayList<CustomLegendItemBuilder> getAvailableBuilders() {
        Collection<? extends CustomGroupsItemBuilder> customBuilders = Lookup.getDefault().lookupAll(CustomGroupsItemBuilder.class);
        ArrayList<CustomLegendItemBuilder> availableBuilders = new ArrayList<CustomLegendItemBuilder>();
        for (CustomGroupsItemBuilder customBuilder : customBuilders) {
            availableBuilders.add((CustomLegendItemBuilder) customBuilder);
        }
        return availableBuilders;
    }

    @Override
    public void writeXMLFromData(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {
        /* disabled to avoid compilation errors. This part will be taken care of during serialization

         String name = null;
         String text = null;

         // write number of items
         Integer numberOfGroups = item.getData(GroupsItem.NUMBER_OF_GROUPS);
         text = numberOfGroups.toString();
         name = GroupsItem.NUMBER_OF_GROUPS;
         writer.writeStartElement(LegendItem.DATA);
         writer.writeAttribute(XML_NAME, name);
         writer.writeCharacters(text);
         writer.writeEndElement();

         // writing labels
         ArrayList<StringBuilder> labels = item.getData(GroupsItem.LABELS_IDS);
         text = labels.toString();
         name = GroupsItem.LABELS_IDS;
         writer.writeStartElement(LegendItem.DATA);
         writer.writeAttribute(XML_NAME, name);
         writer.writeCharacters(text);
         writer.writeEndElement();


         // writing colors
         ArrayList<Color> colors = item.getData(GroupsItem.COLORS);
         name = GroupsItem.COLORS;
         ArrayList<Integer> colorsIntegerArrayList = new ArrayList<Integer>();
         for (Color color : colors) {
         colorsIntegerArrayList.add(color.getRGB());
         }
         text = colorsIntegerArrayList.toString();
         writer.writeStartElement(LegendItem.DATA);
         writer.writeAttribute(XML_NAME, name);
         writer.writeCharacters(text);
         writer.writeEndElement();

         // writing values
         ArrayList<Float> values = item.getData(GroupsItem.VALUES);
         text = values.toString();
         name = GroupsItem.VALUES;
         writer.writeStartElement(LegendItem.DATA);
         writer.writeAttribute(XML_NAME, name);
         writer.writeCharacters(text);
         writer.writeEndElement();
         */
    }

    @Override
    public void readXMLToData(XMLStreamReader reader, Item item) throws XMLStreamException {
        /* disabled to avoid compilation errors. This part will be taken care of during serialization
         // read number of items
         reader.next();
         Integer numberOfItems = Integer.parseInt(reader.getElementText());

         // reading labels
         reader.next();
         String labelsString = reader.getElementText();
         String[] labelsArray = labelsString.replace("[", "").replace("]", "").split(", ");
         ArrayList<StringBuilder> labels = new ArrayList<StringBuilder>();
         for (int i = 0; i < numberOfItems; i++) {
         labels.add(new StringBuilder(labelsArray[i]));
         }

         // reading colors
         reader.next();
         String colorsString = reader.getElementText();
         String[] colorsArray = colorsString.replace("[", "").replace("]", "").split(", ");
         ArrayList<Color> colors = new ArrayList<Color>();
         for (int i = 0; i < numberOfItems; i++) {
         colors.add(new Color(Integer.parseInt(colorsArray[i])));
         }


         // reading values
         reader.next();
         String valuesString = reader.getElementText();
         String[] valuesArray = valuesString.replace("[", "").replace("]", "").split(", ");
         ArrayList<Float> values = new ArrayList<Float>();
         for (int i = 0; i < numberOfItems; i++) {
         values.add(Float.parseFloat(valuesArray[i]));
         }



         item.setData(GroupsItem.NUMBER_OF_GROUPS, labels.size());
         item.setData(GroupsItem.COLORS, colors);
         item.setData(GroupsItem.VALUES, values);
         */
    }

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new GroupsItem(graph);
    }

    @Override
    public void writeXMLFromItemOwnProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {
        /* disabled to avoid compilation errors. This part will be taken care of during serialization
         // write number of items
         Integer numberOfGroups = item.getData(GroupsItem.NUMBER_OF_GROUPS);
         String text = numberOfGroups.toString();
         String name = GroupsItem.NUMBER_OF_GROUPS;
         writer.writeStartElement(LegendItem.DATA);
         writer.writeAttribute(XML_NAME, name);
         writer.writeCharacters(text);
         writer.writeEndElement();

         PreviewProperty[] ownProperties = item.getData(LegendItem.OWN_PROPERTIES);
         for (PreviewProperty property : ownProperties) {
         writeXMLFromSingleProperty(writer, property, previewProperties);
         }
         */
    }

    @Override
    public ArrayList<PreviewProperty> readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException {
        /* disabled to avoid compilation errors. This part will be taken care of during serialization
         ArrayList<PreviewProperty> properties = new ArrayList<PreviewProperty>();

         // read number of items
         reader.next();
         Integer numberOfItems = Integer.parseInt(reader.getElementText());

         // reading labels
         ArrayList<StringBuilder> labels = new ArrayList<StringBuilder>();
         for (int i = 0; i < numberOfItems; i++) {
         reader.next();
         StringBuilder valueStringBuilder = new StringBuilder(reader.getElementText());
         labels.add(valueStringBuilder);
         PreviewProperty property = createLegendLabelProperty(item, i, valueStringBuilder.toString());
         properties.add(property);
         }
         item.setData(GroupsItem.LABELS_IDS, labels);

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
         */

        return new ArrayList<PreviewProperty>();
    }

    public PreviewProperty readXMLToSingleOwnProperty(XMLStreamReader reader, Item item) throws XMLStreamException {
        String propertyName = reader.getAttributeValue(null, XML_NAME);
        String valueString = reader.getElementText();
        int propertyIndex = GroupsProperty.getInstance().getProperty(propertyName);
        Class valueClass = defaultValues[propertyIndex].getClass();
        Object value = readValueFromText(valueString, valueClass);
        PreviewProperty property = createLegendProperty(item, propertyIndex, value);
        return property;
    }
}