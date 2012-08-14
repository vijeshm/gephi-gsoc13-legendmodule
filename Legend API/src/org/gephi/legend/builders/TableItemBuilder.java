/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

/**
 *
 * @author edubecks
 */
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
import org.gephi.legend.api.CustomTableItemBuilder;
import org.gephi.legend.items.LegendItem;
import org.gephi.legend.items.LegendItem.Alignment;
import org.gephi.legend.items.LegendItem.Direction;
import org.gephi.legend.items.TableItem;
import org.gephi.legend.items.TableItem.HorizontalPosition;
import org.gephi.legend.items.TableItem.VerticalPosition;
import org.gephi.legend.manager.LegendManager;
import org.gephi.legend.properties.TableProperty;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = ItemBuilder.class, position = 100),
    @ServiceProvider(service = LegendItemBuilder.class, position = 100)
})
public class TableItemBuilder extends LegendItemBuilder {

    @Override
    protected void setDefaultValues() {
        defaultWidth = 600;
        defaultHeight = 500;
    }

    @Override
    protected boolean isBuilderForItem(Item item) {
        return item instanceof TableItem;
    }

    @Override
    public String getType() {
        return TableItem.LEGEND_TYPE;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(LegendManager.class, "TableItem.name");
    }

    @Override
    protected Item buildCustomItem(CustomLegendItemBuilder builder,
                                   Graph graph,
                                   AttributeModel attributeModel) {


        Item item = createNewLegendItem(graph);

        // labels
        ArrayList<StringBuilder> verticalLabels = new ArrayList<StringBuilder>();
        ArrayList<StringBuilder> horizontalLabels = new ArrayList<StringBuilder>();
        ArrayList<TableItem.Labels> labels = new ArrayList<TableItem.Labels>();
        // values
        ArrayList<ArrayList<Float>> values = new ArrayList<ArrayList<Float>>();
        // colors
        ArrayList<Color> horizontalColors = new ArrayList<Color>();
        ArrayList<Color> verticalColors = new ArrayList<Color>();
        ArrayList<ArrayList<Color>> valueColors = new ArrayList<ArrayList<Color>>();

        // retrieving data
        CustomTableItemBuilder customTableBuilder = (CustomTableItemBuilder) builder;
        customTableBuilder.retrieveData(
                labels,
                horizontalLabels,
                verticalLabels,
                values,
                horizontalColors,
                verticalColors,
                valueColors);
        System.out.println("@Var: ------------------>    labels: " + labels);

        // setting data
        item.setData(TableItem.HORIZONTAL_LABELS, horizontalLabels);
        item.setData(TableItem.VERTICAL_LABELS, verticalLabels);
        item.setData(TableItem.LABELS_IDS, labels);
        item.setData(TableItem.TABLE_VALUES, values);
        item.setData(TableItem.COLOR_VALUES, valueColors);
        item.setData(TableItem.COLOR_HORIZONTAL, horizontalColors);
        item.setData(TableItem.COLOR_VERTICAL, verticalColors);

        return item;

    }

    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendManager.getProperty(
                TableProperty.OWN_PROPERTIES, 
                itemIndex, 
                property);

        switch (property) {
            case TableProperty.TABLE_VERTICAL_EXTRA_MARGIN: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.vertical.extraMargin.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.vertical.extraMargin.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TableProperty.TABLE_HORIZONTAL_EXTRA_MARGIN: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.vertical.extraMargin.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.vertical.extraMargin.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TableProperty.TABLE_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.font.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.font.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TableProperty.TABLE_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.font.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.font.color.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TableProperty.TABLE_IS_CELL_COLORING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.isCellColoring.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.isCellColoring.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TableProperty.TABLE_CELL_COLORING_DIRECTION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Direction.class,
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.cellColoringDirection.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.cellColoringDirection.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TableProperty.TABLE_HORIZONTAL_TEXT_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.horizontalText.alignment.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.horizontalText.alignment.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TableProperty.TABLE_HORIZONTAL_TEXT_POSITION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        HorizontalPosition.class,
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.horizontalText.position.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.horizontalText.position.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TableProperty.TABLE_VERTICAL_TEXT_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.alignment.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.alignment.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TableProperty.TABLE_VERTICAL_TEXT_POSITION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        VerticalPosition.class,
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.position.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.position.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TableProperty.TABLE_VERTICAL_TEXT_ROTATION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        TableItem.VerticalTextDirection.class,
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.rotation.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.rotation.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TableProperty.TABLE_IS_DISPLAYING_GRID: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.grid.isDisplaying.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.grid.isDisplaying.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case TableProperty.TABLE_GRID_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.grid.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "TableItem.property.grid.color.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

        }

        return previewProperty;
    }

    @Override
    protected PreviewProperty[] createLegendOwnProperties(Item item) {



        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        ArrayList<String> tableProperties = LegendManager.getProperties(TableProperty.OWN_PROPERTIES, itemIndex);


        // creating one property for each label
        ArrayList<TableItem.Labels> labels = item.getData(TableItem.LABELS_IDS);
        TableItem.Labels labelsIDs = labels.get(0);
        ArrayList<StringBuilder> horizontalLabels = item.getData(TableItem.HORIZONTAL_LABELS);
        ArrayList<StringBuilder> verticalLabels = item.getData(TableItem.VERTICAL_LABELS);
        ArrayList<StringBuilder> labelsGroup = (labelsIDs == TableItem.Labels.HORIZONTAL) ? horizontalLabels : verticalLabels;
        item.setData(TableItem.NUMBER_OF_LABELS, labelsGroup);
        PreviewProperty[] labelProperties = new PreviewProperty[labelsGroup.size()];
        for (int i = 0; i < labelProperties.length; i++) {
            labelProperties[i] = PreviewProperty.createProperty(this,
                                                                TableProperty.getLabelProperty(itemIndex, i),
                                                                String.class,
                                                                NbBundle.getMessage(LegendManager.class, "TableItem.property.labels.displayName") + " " + i,
                                                                NbBundle.getMessage(LegendManager.class, "TableItem.property.labels.description") + " " + i,
                                                                PreviewProperty.CATEGORY_LEGENDS).setValue(labelsGroup.get(i).toString());
        }


        int[] properties = TableProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] previewProperties = new PreviewProperty[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            System.out.println("@Var: i: " + i);
            previewProperties[i] = createLegendProperty(item, properties[i], defaultValues[i]);
        }

//
//        PreviewProperty[] previewProperties = {
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_VERTICAL_EXTRA_MARGIN),
//                                           Integer.class,
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.vertical.extraMargin.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.vertical.extraMargin.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultVerticalExtraMargin),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_HORIZONTAL_EXTRA_MARGIN),
//                                           Integer.class,
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.vertical.extraMargin.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.vertical.extraMargin.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultHorizontalExtraMargin),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_FONT),
//                                           Font.class,
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.font.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.font.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultFont),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_FONT_COLOR),
//                                           Color.class,
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.font.color.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.font.color.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultFontColor),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_IS_CELL_COLORING),
//                                           Boolean.class,
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.isCellColoring.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.isCellColoring.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsCellColoring),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_CELL_COLORING_DIRECTION),
//                                           Direction.class,
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.cellColoringDirection.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.cellColoringDirection.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultCellColoringDirection),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_HORIZONTAL_TEXT_ALIGNMENT),
//                                           Alignment.class,
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.horizontalText.alignment.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.horizontalText.alignment.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultHorizontalTextAlignment),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_HORIZONTAL_TEXT_POSITION),
//                                           HorizontalPosition.class,
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.horizontalText.position.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.horizontalText.position.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultHorizontalLabelsPosition),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_VERTICAL_TEXT_ALIGNMENT),
//                                           Alignment.class,
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.alignment.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.alignment.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultVerticalTextAlignment),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_VERTICAL_TEXT_POSITION),
//                                           VerticalPosition.class,
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.position.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.position.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultVerticalLabelsPosition),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_VERTICAL_TEXT_ROTATION),
//                                           TableItem.VerticalTextDirection.class,
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.rotation.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.rotation.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultVerticalTextRotation),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_IS_DISPLAYING_GRID),
//                                           Boolean.class,
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.grid.isDisplaying.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.grid.isDisplaying.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsDisplayingGrid),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_GRID_COLOR),
//                                           Color.class,
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.grid.color.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.grid.color.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultGridColor),};


        PreviewProperty[] propertiesWithLabels = new PreviewProperty[labelProperties.length + previewProperties.length];
        System.arraycopy(labelProperties, 0, propertiesWithLabels, 0, labelProperties.length);
        System.arraycopy(previewProperties, 0, propertiesWithLabels, labelProperties.length, previewProperties.length);
        return propertiesWithLabels;
    }

    @Override
    protected Boolean hasDynamicProperties() {
        return Boolean.FALSE;
    }

    @Override
    public ArrayList<CustomLegendItemBuilder> getAvailableBuilders() {
        Collection<? extends CustomTableItemBuilder> customBuilders = Lookup.getDefault().lookupAll(CustomTableItemBuilder.class);
        ArrayList<CustomLegendItemBuilder> availableBuilders = new ArrayList<CustomLegendItemBuilder>();
        for (CustomTableItemBuilder customBuilder : customBuilders) {
            availableBuilders.add((CustomLegendItemBuilder) customBuilder);
        }
        return availableBuilders;
    }

    public void writeDataToXML(XMLStreamWriter writer, Item item) throws XMLStreamException {

        String name = null;
        String text = null;

        // labels
        ArrayList<TableItem.Labels> labels = item.getData(TableItem.LABELS_IDS);
        ArrayList<String> horizontalLabels = item.getData(TableItem.HORIZONTAL_LABELS);
        ArrayList<String> verticalLabels = item.getData(TableItem.VERTICAL_LABELS);
        TableItem.Labels labelIds = labels.get(0);
        Integer numItems = (labelIds == TableItem.Labels.HORIZONTAL) ? horizontalLabels.size() : verticalLabels.size();
        name = TableItem.LABELS_IDS;
        text = labelIds.toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute("name", name);
        writer.writeCharacters(text);
        writer.writeEndElement();
        System.out.println("@Saving: : " + LegendItem.DATA + " <> " + " name: " + name + " <> " + " value: " + text);

        // HORIZONTAL LABELS

        name = TableItem.HORIZONTAL_LABELS;
        text = horizontalLabels.toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute("name", name);
        writer.writeCharacters(text);
        writer.writeEndElement();
        System.out.println("@Saving: : " + LegendItem.DATA + " <> " + " name: " + name + " <> " + " value: " + text);

        // VERTICAL LABELS

        name = TableItem.VERTICAL_LABELS;
        text = verticalLabels.toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute("name", name);
        writer.writeCharacters(text);
        writer.writeEndElement();
        System.out.println("@Saving: : " + LegendItem.DATA + " <> " + " name: " + name + " <> " + " value: " + text);





        // values
        ArrayList<ArrayList<Float>> values = item.getData(TableItem.TABLE_VALUES);
        name = TableItem.TABLE_VALUES;
        text = values.toString();
//        for (ArrayList<Float> row : values) {
//            text += row.toString();
//        }
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute("name", name);
        writer.writeCharacters(text);
        writer.writeEndElement();
        System.out.println("@Saving: : " + LegendItem.DATA + " <> " + " name: " + name + " <> " + " value: " + text);


        // color values
        ArrayList<ArrayList<Color>> valueColors = item.getData(TableItem.COLOR_VALUES);
        name = TableItem.COLOR_VALUES;
        ArrayList<ArrayList<Integer>> integerValueColors = new ArrayList<ArrayList<Integer>>();
        for (ArrayList<Color> row : valueColors) {
            ArrayList<Integer> newrow = new ArrayList<Integer>();
            for (Color color : row) {
                newrow.add(color.getRGB());
            }
            integerValueColors.add(newrow);
        }
        text = integerValueColors.toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute("name", name);
        writer.writeCharacters(text);
        writer.writeEndElement();
        System.out.println("@Saving: : " + LegendItem.DATA + " <> " + " name: " + name + " <> " + " value: " + text);


        // horizontal colors
        ArrayList<Color> horizontalColors = item.getData(TableItem.COLOR_HORIZONTAL);
        name = TableItem.COLOR_HORIZONTAL;
        ArrayList<Integer> horizontalColorsRow = new ArrayList<Integer>();
        for (Color color : horizontalColors) {
            horizontalColorsRow.add(color.getRGB());
        }
        text = horizontalColorsRow.toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute("name", name);
        writer.writeCharacters(text);
        writer.writeEndElement();
        System.out.println("@Saving: : " + LegendItem.DATA + " <> " + " name: " + name + " <> " + " value: " + text);



        // vertical colors
        ArrayList<Color> verticalColors = item.getData(TableItem.COLOR_VERTICAL);
        name = TableItem.COLOR_VERTICAL;
        ArrayList<Integer> verticalColorsRow = new ArrayList<Integer>();
        for (Color color : verticalColors) {
            verticalColorsRow.add(color.getRGB());
        }
        text = verticalColorsRow.toString();
        writer.writeStartElement(LegendItem.DATA);
        writer.writeAttribute("name", name);
        writer.writeCharacters(text);
        writer.writeEndElement();
        System.out.println("@Saving: : " + LegendItem.DATA + " <> " + " name: " + name + " <> " + " value: " + text);

    }

    @Override
    public void readXMLToData(XMLStreamReader reader, Item item) throws XMLStreamException {
        boolean end = false;
        while (reader.hasNext() && !end) {
            int type = reader.next();
            switch (type) {
                case XMLStreamReader.START_ELEMENT: {
                    String name = reader.getLocalName();
                    if (name.equals(TableItem.HORIZONTAL_LABELS)) {
                    }
                    break;
                }
                case XMLStreamReader.CHARACTERS: {

                    break;
                }
                case XMLStreamReader.END_ELEMENT: {
                    break;
                }
            }

        }
    }

    //default values
    protected final Integer defaultVerticalExtraMargin = 3;
    protected final Integer defaultHorizontalExtraMargin = 3;
    protected final Integer defaultMinimumMargin = 3;
    protected final Font defaultFont = new Font("Arial", Font.PLAIN, 13);
    protected final Color defaultFontColor = Color.BLACK;
    // grid
    protected final Color defaultGridColor = Color.BLACK;
    protected final Boolean defaultIsDisplayingGrid = true;
    // cell
    protected final Boolean defaultIsCellColoring = false;
    protected final Direction defaultCellColoringDirection = Direction.UP;
    // side labels
    protected final HorizontalPosition defaultHorizontalLabelsPosition = HorizontalPosition.LEFT;
    protected final Alignment defaultHorizontalTextAlignment = Alignment.JUSTIFIED;
    // up/bottom labels
    protected final Alignment defaultVerticalTextAlignment = Alignment.CENTER;
    protected final VerticalPosition defaultVerticalLabelsPosition = VerticalPosition.BOTTOM;
    protected final TableItem.VerticalTextDirection defaultVerticalTextRotation = TableItem.VerticalTextDirection.DIAGONAL;
    private final Object[] defaultValues = {
        defaultFont,
        defaultFontColor,
        defaultIsCellColoring,
        defaultCellColoringDirection,
        defaultHorizontalLabelsPosition,
        defaultHorizontalTextAlignment,
        defaultHorizontalExtraMargin,
        defaultVerticalLabelsPosition,
        defaultVerticalTextAlignment,
        defaultVerticalTextRotation,
        defaultVerticalExtraMargin,
        defaultIsDisplayingGrid,
        defaultGridColor
    };

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new TableItem(graph);
    }

    @Override
    public PreviewProperty readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException {
//        String propertyName = reader.getAttributeValue(null, XML_NAME);
//        int propertyIndex = LegendProperty.getInstance().getProperty(propertyName);
//        String valueString = reader.getElementText();
//        Object value = PreviewProperties.readValueFromText(valueString, defaultValues[propertyIndex].getClass());
//        System.out.println("@Var: reading propertyType: " + propertyName + " with value: " + value);
//        PreviewProperty property = createLegendProperty(item, propertyIndex, value);
//        return property;
        return null;
    }

    @Override
    public void writeItemOwnPropertiesToXML(XMLStreamWriter writer, Item item) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}