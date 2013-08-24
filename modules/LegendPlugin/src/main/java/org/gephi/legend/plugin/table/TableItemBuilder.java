/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.spi.CustomLegendItemBuilder;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.legend.spi.LegendItemBuilder;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.text.PrintPreferences;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author mvvijesh, edubecks
 */
@ServiceProviders(value = {
    @ServiceProvider(service = ItemBuilder.class, position = 101),
    @ServiceProvider(service = LegendItemBuilder.class, position = 101)
})
public class TableItemBuilder extends AbstractLegendItemBuilder {

    protected final String content = Cell.cellContent;
    // default Values
    protected final Font defaultFont = Cell.cellFont;
    protected final Color defaultFontColor = Cell.cellFontColor;
    protected final Alignment defaultFontAlignment = Cell.cellAlignment;
    protected final int defaultTableCellSpacing = 10;
    protected final int defaultTableCellPadding = 5;
    protected final int defaultTableBorderSize = 5;
    protected final Color defaultTableBorderColor = Cell.borderColor;
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
        defaultTableWidthFull
    };

    @Override
    public boolean setDefaultValues() {
        // overwrite the generic properties here
        return false;
    }

    @Override
    public Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel, Integer newItemIndex) {
        TableItem item = (TableItem) createNewLegendItem(graph);
        CustomTableItemBuilder tableItemBuilder = (CustomTableItemBuilder) builder;

        // setting default renderer and item index
        item.setData(LegendItem.RENDERER, TableItemRenderer.class);
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);

        tableItemBuilder.populateTable(item);

        return item;
    }

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

    @Override
    public PreviewProperty[] createLegendOwnProperties(Item item) {
        // Apart from creating Own Properties, this method will also populate the "table" with the default number of rows and columns
        // It will also create the properties of each cell, while the creation of the cell itself.
        TableItem tableItem = (TableItem) item;
        int[] properties = TableProperty.LIST_OF_PROPERTIES;
        ArrayList<PreviewProperty> previewProperties = new ArrayList<PreviewProperty>();
        for (int i = 0; i < defaultValues.length; i++) {
            previewProperties.add(createLegendProperty(tableItem, properties[i], defaultValues[i]));
        }

        /*
         // the the addColumn method would've updated the item's OWN_PROPERTIES.
         // Hence, add all those updated properties to the previewProperties
         Object[] updatedPreviewProps = item.getData(LegendItem.OWN_PROPERTIES);        
         for(Object prop : updatedPreviewProps) {
         previewProperties.add((PreviewProperty)prop);
         }
         */

        return previewProperties.toArray(new PreviewProperty[0]);
    }

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new TableItem(graph);
    }

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
