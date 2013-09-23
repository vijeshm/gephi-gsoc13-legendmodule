package org.gephi.legend.api;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.attribute.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.spi.CustomLegendItemBuilder;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.legend.spi.LegendItemBuilder;
import org.gephi.legend.spi.LegendItemRenderer;
import org.gephi.preview.api.*;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 * A builder class for building legend items.
 *
 * This class is made abstract in order to share common code across all the
 * legend item builders. The legend item builders are expected to extend this
 * class and implement the abstract methods.
 *
 * @author mvvijesh, edubecks
 */
public abstract class AbstractLegendItemBuilder implements LegendItemBuilder {

    // XML
    protected static final String XML_PROPERTY = "property";
    private static final String XML_LEGEND_TYPE = "legendtype";
    private static final String XML_RENDERER = "renderer";
    private static final String XML_DYNAMIC_PROPERTY = "dynamicproperty";
    private static final String XML_LEGEND_PROPERTY = "legendproperty";
    protected static final String XML_OWN_PROPERTY = "itemproperty";
    protected static final String XML_NAME = "name";
    protected static final String XML_CLASS = "class";
    private static final String XML_DATA = "itemdata";
    // default values
    // background and border
    protected Boolean defaultBackgroundIsDisplaying = Boolean.TRUE;
    protected Color defaultBackgroundColor = new Color(1f, 1f, 1f, 0.5f);
    protected Boolean defaultBorderIsDisplaying = Boolean.TRUE;
    protected Color defaultBorderColor = new Color(0f, 0f, 0f, 0.75f);
    protected Integer defaultBorderLineThick = 5;
    // label
    protected String defaultLabel = "";
    // is displaying
    protected Boolean defaultIsDisplaying = Boolean.TRUE;
    // origin
    protected Float defaultOriginX = 0f;
    protected Float defaultOriginY = 0f;
    // width and height
    protected Float defaultWidth = 500f;
    protected Float defaultHeight = 300f;
    // title
    protected Boolean defaultTitleIsDisplaying = Boolean.TRUE;
    protected String defaultTitle = "Click to change title properties.";
    protected Font defaultTitleFont = new Font("Arial", Font.BOLD, 30);
    protected Alignment defaultTitleAlignment = Alignment.CENTER;
    protected Color defaultTitleFontColor = Color.BLACK;
    // description
    protected String defaultDescription = "Click to change description properties.";
    protected Boolean defaultDescriptionIsDisplaying = Boolean.TRUE;
    protected Color defaultDescriptionFontColor = Color.BLACK;
    protected Alignment defaultDescriptionAlignment = Alignment.LEFT;
    protected Font defaultDescriptionFont = new Font("Arial", Font.PLAIN, 10);
    // properties set by user
    protected String defaultUserLegendName = "legend name";
    // default values list
    private ArrayList<Object> defaultValuesArrayList;

    /**
     * this function creates a custom item of a particular legend type, based on
     * the custom legend item builder chosen.
     *
     * @param newItemIndex - unique ID for the new item to be created
     * @param graph - the graph object to be associated with the legend item
     * @param attributeModel
     * @param builder - the custom builder chosen by the user
     * @return newly created item
     */
    public Item createCustomItem(Integer newItemIndex, Graph graph, AttributeModel attributeModel, CustomLegendItemBuilder builder) {
        Item item = buildCustomItem(builder, graph, attributeModel, newItemIndex);
        createDefaultProperties(item);
        return item;
    }

    /**
     * populates the item and preview properties from the preview model with
     * various properties
     *
     * @param item - the newly built legend item whose properties need to be
     * populated
     */
    private void createDefaultProperties(Item item) {
        item.setData(LegendItem.PROPERTIES, createLegendProperties(item));
        item.setData(LegendItem.OWN_PROPERTIES, createLegendOwnProperties(item)); // we can inject code that fetches whatever data that is required and populate it into the object being built. 
        // To enable this, a custom item builder must be associated with the item. 
        // This builder will implement an interface which has a method to retrieve data
        // (more methods can be added or modified to make it more powerful)

        item.setData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES, 0);
        item.setData(LegendItem.HAS_DYNAMIC_PROPERTIES, hasDynamicProperties());
        item.setData(LegendItem.DYNAMIC_PROPERTIES, new PreviewProperty[0]);
        item.setData(LegendItem.IS_SELECTED, Boolean.FALSE);
        item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.FALSE);
        item.setData(LegendItem.CURRENT_TRANSFORMATION, "");
    }

    /**
     *
     * @param graph
     * @param attributeModel
     * @return the list of items which can be built using this builder
     */
    @Override
    public Item[] getItems(Graph graph, AttributeModel attributeModel) {
        LegendModel legendManager = LegendController.getInstance().getLegendModel();

        ArrayList<Item> legendItems = legendManager.getActiveItems();
        ArrayList<Item> items = new ArrayList<Item>();
        for (Item item : legendItems) {
            if (isBuilderForItem(item)) {
                items.add(item);
            }
        }
        return items.toArray(new Item[items.size()]);
    }

    /**
     *
     * @param item - the item being built
     * @param property - the property number
     * @param value - value of the property
     * @return the PreviewProperty constructed from the property number and
     * value
     */
    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, property);

        // based on the property number, create an appropriate PreviewProperty object
        switch (property) {
            case LegendProperty.LABEL: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.label.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.label.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.isDisplaying.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.USER_ORIGIN_X: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.originX.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.originX.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.USER_ORIGIN_Y: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.originY.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.originY.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.WIDTH: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.width.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.width.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.HEIGHT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.height.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.height.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BACKGROUND_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.background.isDisplaying.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.background.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BACKGROUND_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.background.color.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.background.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BORDER_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.border.isDisplaying.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.border.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BORDER_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.border.color.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.border.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.BORDER_LINE_THICK: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.border.lineThick.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.border.lineThick.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.isDisplaying.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.font.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.font.color.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.TITLE_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.alignment.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.title.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_IS_DISPLAYING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.isDisplaying.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.font.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.font.color.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
            case LegendProperty.DESCRIPTION_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.alignment.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.description.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
            }
            break;

            case LegendProperty.USER_LEGEND_NAME: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.user.legendName.displayName"),
                        NbBundle.getMessage(AbstractLegendItemBuilder.class, "LegendItem.property.user.legendName.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
        }

        return previewProperty;

    }

    /**
     * create the preview properties, populate the item and previewModel's
     * PreviewProperties with it,
     *
     * @param item - the item being built
     * @return list of new created item properties. These properties are common
     * to all the legends.
     */
    private PreviewProperty[] createLegendProperties(Item item) {
        if (setDefaultValues()) {
            updateDefaultValues();
        }

        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        PreviewProperties previewProperties = previewModel.getProperties();

        PreviewProperty property;
        int[] properties = LegendProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] itemPreviewProperties = new PreviewProperty[defaultValuesArrayList.size()];

        // creating label
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        // setting the label
        property = createLegendProperty(item, LegendProperty.LABEL, defaultLabel + itemIndex + " [" + item.getType() + "]");
        previewProperties.addProperty(property);
        itemPreviewProperties[LegendProperty.LABEL] = property; // the same reference is also maintained within the legend item

        // setting the legend name
        property = createLegendProperty(item, LegendProperty.USER_LEGEND_NAME, defaultLabel + itemIndex + " [" + item.getType() + "]");
        previewProperties.addProperty(property);
        itemPreviewProperties[LegendProperty.USER_LEGEND_NAME] = property; // the same reference is also maintained within the legend item

        for (int i = 0; i < itemPreviewProperties.length - 1; i++) {
            if (i != LegendProperty.LABEL && i != LegendProperty.USER_LEGEND_NAME) {
                property = createLegendProperty(item, properties[i], defaultValuesArrayList.get(i));
                previewProperties.addProperty(property);
                itemPreviewProperties[i] = property; // the same reference is also maintained within the legend item
            }
        }

        return itemPreviewProperties;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public void writeXMLFromData(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {
    }

    @Override
    public void writeXMLFromDynamicProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {
    }

    /**
     * Function that automatically saves a property using its PropertyName and
     * the Value attached to it. Only works if property has a known value type.
     * Known types:
     * <code>Integer</code>,
     * <code> Float</code>,
     * <code> String</code>,,
     * <code> Color</code>,
     * <code> LegendItem.Alignment</code>,
     * <code> LegendItem.Shape</code> and
     * <code> LegendItem.Direction</code>
     *
     * @param writer the XMLStreamWriter to write to
     * @param property property to be saved
     * @param previewProperties Current workspace PreviewProperties
     * @throws XMLStreamException
     */
    protected void writeXMLFromSingleProperty(XMLStreamWriter writer, PreviewProperty property, PreviewProperties previewProperties) throws XMLStreamException {
        //Better read from previewProperties instead of just the property, because LegendMouseListener puts origin x and y in previewProperties.
        Object propertyValue = previewProperties.getValue(property.getName());

        if (propertyValue != null) {
            String text = writeValueAsText(propertyValue);
            writer.writeStartElement(XML_PROPERTY);
            String name = LegendModel.getPropertyFromPreviewProperty(property);
            System.out.println("@Var: SAVING XML name: " + name + " , " + text);
            writer.writeAttribute(XML_NAME, name);
            writer.writeAttribute(XML_CLASS, propertyValue.getClass().getName());
            writer.writeCharacters(text);
            writer.writeEndElement();
        }
    }

    /**
     * Function that takes an item and saves its data, legend properties,
     * specific item properties, dynamic properties and data using the specified
     * writer.
     *
     * @param writer the XMLStreamWriter to write to
     * @param item the item to be saved
     * @param previewProperties Current workspace PreviewProperties
     * @throws XMLStreamException
     */
    @Override
    public void writeXMLFromItem(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {

        // legend type
        writer.writeStartElement(XML_LEGEND_TYPE);
        writer.writeCharacters(item.getType());
        writer.writeEndElement();

        // renderer
        writer.writeStartElement(XML_RENDERER);
        System.out.println("" + item.getData(LegendItem.RENDERER).getClass().getName());
        writer.writeCharacters(item.getData(LegendItem.RENDERER).getClass().getName());
        writer.writeEndElement();

        // legend properties
        writer.writeStartElement(XML_LEGEND_PROPERTY);
        PreviewProperty[] legendProperties = item.getData(LegendItem.PROPERTIES);
        for (PreviewProperty property : legendProperties) {
            writeXMLFromSingleProperty(writer, property, previewProperties);
        }
        writer.writeEndElement();

        // own properties
        writer.writeStartElement(XML_OWN_PROPERTY);
        writeXMLFromItemOwnProperties(writer, item, previewProperties);
        writer.writeEndElement();

        // dynamic properties
        writer.writeStartElement(XML_DYNAMIC_PROPERTY);
        writeXMLFromDynamicProperties(writer, item, previewProperties);
        writer.writeEndElement();


        // data
        writer.writeStartElement(XML_DATA);
        writeXMLFromData(writer, item, previewProperties);
        writer.writeEndElement();
    }

    /**
     * Function that retrieves the data from an XML reader and converts it to
     * data for each kind of item
     *
     * @param reader the XML reader to read the data from
     * @param item the item where the data would be stored
     * @throws XMLStreamException
     */
    @Override
    public void readXMLToData(XMLStreamReader reader, Item item) throws XMLStreamException {
    }

    public PreviewProperty readXMLToSingleLegendProperty(XMLStreamReader reader, Item item) throws XMLStreamException {
        String propertyName = reader.getAttributeValue(null, XML_NAME);
        String valueString = reader.getElementText();
        int propertyIndex = LegendProperty.getInstance().getProperty(propertyName);
        Class valueClass = defaultValuesArrayList.get(propertyIndex).getClass();

//        Object value = PreviewProperties.readValueFromText(valueString, valueClass);
//        if (value == null) {
//            value = readValueFromText(valueString, valueClass);
//        }

        Object value = readValueFromText(valueString, valueClass);

        PreviewProperty property = createLegendProperty(item, propertyIndex, value);
        return property;
    }

    /**
     * Function that takes some value in a String form and converts it to the
     * specified class type
     *
     * @param valueString the value in a String form
     * @param valueClass the class type to convert the value
     * @return
     */
    protected Object readValueFromText(String valueString, Class valueClass) {
//        System.out.println("@Var: valueClass: "+valueClass);
//        System.out.println("@Var: valueString: "+valueString);

        // bug
        if (valueString.startsWith("org.netbeans.beaninfo.editors.ColorEditor")) {
            // bug
            Pattern rgb = Pattern.compile(".*\\[r=(\\d+),g=(\\d+),b=(\\d+)\\]");
            Matcher matcher = rgb.matcher(valueString);
            if (matcher.matches()) {
                valueString = "[" + matcher.group(1) + "," + matcher.group(2) + "," + matcher.group(3) + "]";
            }
        }

        Object value = PreviewProperties.readValueFromText(valueString, valueClass);
        if (value != null) {
            return value;
        }

        if (valueClass.equals(LegendItem.Alignment.class)) {
            value = availableAlignments[Integer.parseInt(valueString)];
        } else if (valueClass.equals(LegendItem.Shape.class)) {
            value = availableShapes[Integer.parseInt(valueString)];
        } else if (valueClass.equals(LegendItem.Direction.class)) {
            value = availableDirections[Integer.parseInt(valueString)];
        } else if (valueClass.equals(Boolean.class)) {
            value = Boolean.parseBoolean(valueString);
        } else if (valueClass.equals(Integer.class)) {
            value = Integer.parseInt(valueString);
        } else if (valueClass.equals(File.class)) {
            value = new File(valueString);
        }
        return value;
    }

    /**
     * Function that retrieves the properties (propertyName and value) from an
     * XML reader and converts it to list of properties. Normally using the
     * <code>readXMLToSingleOwnProperty</code> for each property.
     *
     * @param reader the XML reader to read the data from
     * @param item the item where the data would be stored
     * @return
     * @throws XMLStreamException
     */
    @Override
    public abstract ArrayList<PreviewProperty> readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException;

    /**
     * Function that retrieves the dynamic properties (if any) from an XML
     * reader and converts it to list of properties. Normally using the
     * <code>readXMLToSingleOwnProperty</code> for each property.
     *
     * @param reader the XML reader to read the data from
     * @param item the item where the data would be stored
     * @return
     * @throws XMLStreamException
     */
    @Override
    public ArrayList<PreviewProperty> readXMLToDynamicProperties(XMLStreamReader reader, Item item) throws XMLStreamException {
        reader.nextTag();
        return new ArrayList<PreviewProperty>();
    }

    @Override
    public ArrayList<PreviewProperty> readXMLToLegendProperties(XMLStreamReader reader, Item item) throws XMLStreamException {
        ArrayList<PreviewProperty> properties = new ArrayList<PreviewProperty>();

        // legend properties

        boolean end = false;
        while (reader.hasNext() && !end) {
            int type = reader.next();
            switch (type) {
                case XMLStreamReader.START_ELEMENT: {
                    PreviewProperty property = readXMLToSingleLegendProperty(reader, item);
//                    System.out.println("@Var: .. success property: "+property.getName());
//                    System.out.println("@Var: property: "+property.getValue());

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

    @Override
    public void readXMLToRenderer(XMLStreamReader reader, Item item) throws XMLStreamException {
        if (reader.getLocalName().equals(XML_RENDERER)) {
            String valueString = reader.getElementText();
            System.out.println("@Var: renderer....  " + valueString);
            LegendItemRenderer availableRenderer = LegendController.getInstance().getRenderers().get(valueString);
            System.out.println("@Var: renderer....  " + availableRenderer);
            if (availableRenderer != null) {
                item.setData(LegendItem.RENDERER, availableRenderer);
            }
        }
    }

    /**
     * Function that reads the legend properties, specific item properties,
     * dynamic properties and data and converts it to an Item using the
     * specified reader.
     *
     * @param reader the XML reader to read the data from
     * @param newItemIndex used to create the Item
     * @return
     * @throws XMLStreamException
     */
    @Override
    public Item readXMLToItem(XMLStreamReader reader, Integer newItemIndex) throws XMLStreamException {
        Item item = createNewLegendItem(null);
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);

        // opening renderer
        reader.nextTag();
        readXMLToRenderer(reader, item);


        // opening legend properties
        reader.nextTag();
        ArrayList<PreviewProperty> legendProperties = readXMLToLegendProperties(reader, item);

        // opening own properties
        reader.nextTag();
        ArrayList<PreviewProperty> ownProperties = readXMLToOwnProperties(reader, item);

        // opening dynamic properties
        reader.nextTag();
        ArrayList<PreviewProperty> dynamicProperties = readXMLToDynamicProperties(reader, item);
        // closing dynamic properties

        // data
        reader.nextTag();
        readXMLToData(reader, item);

        // finish reading
        reader.next();



        PreviewProperty[] legendPropertiesArray = legendProperties.toArray(new PreviewProperty[legendProperties.size()]);
        PreviewProperty[] ownPropertiesArray = ownProperties.toArray(new PreviewProperty[ownProperties.size()]);
        PreviewProperty[] dynamicPropertiesArray = dynamicProperties.toArray(new PreviewProperty[dynamicProperties.size()]);

        // setting data
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);
        item.setData(LegendItem.OWN_PROPERTIES, ownPropertiesArray);
        item.setData(LegendItem.PROPERTIES, legendPropertiesArray);

        item.setData(LegendItem.HAS_DYNAMIC_PROPERTIES, hasDynamicProperties());
        item.setData(LegendItem.DYNAMIC_PROPERTIES, dynamicPropertiesArray);
        item.setData(LegendItem.IS_SELECTED, Boolean.FALSE);
        item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.FALSE);
        item.setData(LegendItem.CURRENT_TRANSFORMATION, "");

        return item;
    }

    /**
     * Converts the propertyValue of a known type to an String object
     *
     * @param propertyValue Known * * * * * * * * * * * * * * * * * * * * *
     * types: <code> LegendItem.Alignment</code>, <code> LegendItem.Shape</code>
     * and <code> LegendItem.Direction</code>
     * @return
     */
    protected String writeValueAsText(Object propertyValue) {

        String text = PreviewProperties.getValueAsText(propertyValue);
        if (text != null) {
            return text;
        }
        if (propertyValue instanceof LegendItem.Alignment) {
            LegendItem.Alignment propertyValueString = (LegendItem.Alignment) propertyValue;
            text = propertyValueString.getValue();
        } else if (propertyValue instanceof LegendItem.Direction) {
            LegendItem.Direction propertyValueString = (LegendItem.Direction) propertyValue;
            text = propertyValueString.getValue();
        } else if (propertyValue instanceof LegendItem.Shape) {
            LegendItem.Shape propertyValueString = (LegendItem.Shape) propertyValue;
            text = propertyValueString.getValue();
        } else {
            text = propertyValue.toString();
        }
        return text;
    }

    public AbstractLegendItemBuilder() {
        updateDefaultValues();
    }

    /**
     * populate the list of default values
     */
    public final void updateDefaultValues() {
        this.defaultValuesArrayList = new ArrayList<Object>();
        defaultValuesArrayList.add(defaultLabel);
        defaultValuesArrayList.add(defaultIsDisplaying);
        defaultValuesArrayList.add(defaultOriginX);
        defaultValuesArrayList.add(defaultOriginY);
        defaultValuesArrayList.add(defaultWidth);
        defaultValuesArrayList.add(defaultHeight);
        defaultValuesArrayList.add(defaultBackgroundIsDisplaying);
        defaultValuesArrayList.add(defaultBackgroundColor);
        defaultValuesArrayList.add(defaultBorderIsDisplaying);
        defaultValuesArrayList.add(defaultBorderColor);
        defaultValuesArrayList.add(defaultBorderLineThick);
        defaultValuesArrayList.add(defaultTitleIsDisplaying);
        defaultValuesArrayList.add(defaultTitle);
        defaultValuesArrayList.add(defaultTitleFont);
        defaultValuesArrayList.add(defaultTitleFontColor);
        defaultValuesArrayList.add(defaultTitleAlignment);
        defaultValuesArrayList.add(defaultDescriptionIsDisplaying);
        defaultValuesArrayList.add(defaultDescription);
        defaultValuesArrayList.add(defaultDescriptionFont);
        defaultValuesArrayList.add(defaultDescriptionFontColor);
        defaultValuesArrayList.add(defaultDescriptionAlignment);
        defaultValuesArrayList.add(defaultUserLegendName);
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
        LegendItem.Direction.RIGHT,
        LegendItem.Direction.LEFT
    };
}