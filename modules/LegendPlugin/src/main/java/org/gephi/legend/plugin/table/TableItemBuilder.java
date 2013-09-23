package org.gephi.legend.plugin.table;

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
 * class to build the table items.
 *
 * This class is exposed as a service. The createCustomItem method (in the
 * AbstractLegendItemRenderer) is used to create a table legend item, depending
 * on the custom item builder chosen by the user from the UI. The custom
 * builders are expected to implement the CustomTableItemBuilder interface.
 *
 * @author mvvijesh, edubecks
 */
@ServiceProviders(value = {
    @ServiceProvider(service = ItemBuilder.class, position = 101),
    @ServiceProvider(service = LegendItemBuilder.class, position = 101)
})
public class TableItemBuilder extends AbstractLegendItemBuilder {

    protected final String content = Cell.defaultCellTextContent;
    // default Values
    protected final Font defaultFont = Cell.defaultCellFont;
    protected final Color defaultFontColor = Cell.defaultCellFontColor;
    protected final Alignment defaultFontAlignment = Cell.defaultCellAlignment;
    protected final int defaultTableCellSpacing = 10; // the horizontal and vertical spacing between cells
    protected final int defaultTableCellPadding = 5; // the horizontal and vertical spacing between cell content and cell border
    protected final int defaultTableBorderSize = 5; // the size of the cell border. this is consistent across all the cells. i.e, border size for all the cells are equal
    protected final Color defaultTableBorderColor = Cell.defaultBorderColor;
    protected final Color defaultTableBackgroundColor = new Color(0f, 0.35f, 1f, 0.5f);
    protected final Boolean defaultTableWidthFull = false;
    protected final Object[] defaultValues = {
        defaultFont,
        defaultFontColor,
        defaultFontAlignment,
        defaultTableCellSpacing,
        defaultTableCellPadding,
        defaultTableBorderSize,
        defaultTableBorderColor,
        defaultTableBackgroundColor,
        defaultTableWidthFull,};

    @Override
    public boolean setDefaultValues() {
        return false;
    }

    /**
     *
     * @param builder - the custom table item builder chosen by the user from
     * the UI
     * @param graph - the current graph to which the table item belongs to
     * @param attributeModel
     * @param newItemIndex - index of the new text item being created
     * @return the newly built table legend item
     */
    @Override
    public Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel, Integer newItemIndex) {
        TableItem item = (TableItem) createNewLegendItem(graph);
        CustomTableItemBuilder tableItemBuilder = (CustomTableItemBuilder) builder;

        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();

        // add the renderer to the legend model if it has not been added
        TableItemRenderer tableItemRenderer = TableItemRenderer.getInstance();
        if (!legendModel.isRendererAdded(tableItemRenderer)) {
            legendModel.addRenderer(tableItemRenderer);
        }

        // setting default renderer and item index
        item.setData(LegendItem.RENDERER, tableItemRenderer);
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);
        item.setData(LegendItem.CUSTOM_BUILDER, (CustomTableItemBuilder) builder);

        return item;
    }

    /**
     *
     * @param item - the table item being built
     * @param property - the index of the property
     * @param value - the value of the property
     * @return the PreviewProperty object populated with the property string and
     * the value
     */
    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, property);

        switch (property) {
            case TableProperty.TABLE_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.font.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.font.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.font.color.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.font.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_FONT_ALIGNMENT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.font.alignment.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.font.alignment.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_CELL_SPACING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.cell.spacing.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.cell.spacing.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_CELL_PADDING: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.cell.padding.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.cell.padding.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_BORDER_SIZE: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.border.size.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.border.size.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_BORDER_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.border.color.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.border.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_BACKGROUND_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.background.color.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.background.color.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case TableProperty.TABLE_WIDTH_FULL: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.width.full.displayName"),
                        NbBundle.getMessage(TableItemBuilder.class, "TableItem.property.width.full.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
        }

        return previewProperty;
    }

    /**
     *
     * @param item - the table item being built
     * @return the list of PreviewProperty objects
     */
    @Override
    public PreviewProperty[] createLegendOwnProperties(Item item) {
        int[] properties = TableProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] previewProperties = new PreviewProperty[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            previewProperties[i] = createLegendProperty(item, properties[i], defaultValues[i]);
        }

        // the table legend must be populated with the data that needs to rendered.
        // the custmomTableBuilder associated with the item provides this data.
        // see CustomTableItemBuilder interface and Default custom builder for more information
        CustomTableItemBuilder customTableBuilder = (CustomTableItemBuilder) item.getData(LegendItem.CUSTOM_BUILDER);
        // fetch the data to be rendered from the customGroupsBuilder, by populating the groups list.
        customTableBuilder.populateTable((TableItem) item);

        return previewProperties;
    }

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new TableItem(graph);
    }

    /**
     *
     * @param item - item to be checked against
     * @return True if TableItemBuilder can build the item. False, otherwise.
     */
    @Override
    public boolean isBuilderForItem(Item item) {
        return item instanceof TableItem;
    }

    @Override
    public String getType() {
        return TableItem.LEGEND_TYPE;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(TableItemBuilder.class, "TableItem.name");
    }

    /**
     *
     * @return the list of available custom builders for the table legend
     */
    @Override
    public ArrayList<CustomLegendItemBuilder> getAvailableBuilders() {
        Collection<? extends CustomTableItemBuilder> customBuilders = Lookup.getDefault().lookupAll(CustomTableItemBuilder.class);
        ArrayList<CustomLegendItemBuilder> availableBuilders = new ArrayList<CustomLegendItemBuilder>();
        for (CustomTableItemBuilder customBuilder : customBuilders) {
            availableBuilders.add((CustomLegendItemBuilder) customBuilder);
        }
        return availableBuilders;
    }

    @Override
    public Boolean hasDynamicProperties() {
        return Boolean.FALSE;
    }

    @Override
    public ArrayList<PreviewProperty> readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException {
        ArrayList<PreviewProperty> properties = new ArrayList<PreviewProperty>();
        /* - The following code has been commented to maintain the API. This part must be rewritten to suit the new properties.
         // read number of items
         reader.next();
         Integer numberOfItems = Integer.parseInt(reader.getElementText());
         item.setData(org.gephi.legend.plugin.items.TableItem.NUMBER_OF_LABELS, numberOfItems);

         // reading labels
         ArrayList<StringBuilder> labels = new ArrayList<StringBuilder>();
         for (int i = 0; i < numberOfItems; i++) {
         reader.next();
         StringBuilder valueStringBuilder = new StringBuilder(reader.getElementText());
         labels.add(valueStringBuilder);
         PreviewProperty property = createLegendLabelProperty(item, i, valueStringBuilder.toString());
         properties.add(property);
         }
         item.setData(TableItem.LABELS_IDS, labels);

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
         */
        return properties;
    }

    @Override
    public void writeXMLFromItemOwnProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {
        /* - The following code has been commented to maintain the API. This part must be rewritten to suit the new properties.
         // write number of items
         Integer numberOfLabels = item.getData(TableItem.NUMBER_OF_LABELS);
         String name = TableItem.NUMBER_OF_LABELS;
         String text = numberOfLabels.toString();
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
}