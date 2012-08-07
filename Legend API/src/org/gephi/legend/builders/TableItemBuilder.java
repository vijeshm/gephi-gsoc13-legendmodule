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
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.api.CustomTableItemBuilder;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendItem.Alignment;
import org.gephi.legend.api.LegendItem.Direction;
import org.gephi.legend.api.LegendManager;
import org.gephi.legend.api.PartitionData;
import org.gephi.legend.api.StatisticData;
import org.gephi.legend.items.TableItem;
import org.gephi.legend.properties.TableProperty;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ItemBuilder.class, position = 100)
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
        return NbBundle.getMessage(LegendManager.class, "TableItem.name");
    }

    

    @Override
    protected Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel) {
        
        TableItem item = new TableItem(graph);
        
        // labels
        ArrayList<StringBuilder> verticalLabels = new ArrayList<StringBuilder>();
        ArrayList<StringBuilder> horizontalLabels = new ArrayList<StringBuilder>();
        ArrayList<StringBuilder> labels = new ArrayList<StringBuilder>();
        // values
        ArrayList<ArrayList<Float>> values = new ArrayList<ArrayList<Float>>();
        // colors
        ArrayList<Color> horizontalColors = new ArrayList<Color>();
        ArrayList<Color> verticalColors = new ArrayList<Color>();
        ArrayList<ArrayList<Color>> valueColors = new ArrayList<ArrayList<Color>>();
        
        // retrieving data
        CustomTableItemBuilder customTableBuilder = (CustomTableItemBuilder) builder;
        customTableBuilder.retrieveData(labels, horizontalLabels, verticalLabels, values, horizontalColors, verticalColors, valueColors);
        
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

    @Override
    protected PreviewProperty[] createLegendItemProperties(Item item) {



        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        ArrayList<String> tableProperties = LegendManager.getProperties(TableProperty.OWN_PROPERTIES, itemIndex);


        // creating one property for each label
        ArrayList<StringBuilder> labelsGroup = item.getData(TableItem.LABELS_IDS);
        PreviewProperty[] labelProperties = new PreviewProperty[labelsGroup.size()];
        for (int i = 0; i < labelProperties.length; i++) {
            labelProperties[i] = PreviewProperty.createProperty(this,
                                                                TableProperty.getLabelProperty(itemIndex, i),
                                                                String.class,
                                                                NbBundle.getMessage(LegendManager.class, "TableItem.property.labels.displayName")+" "+i,
                                                                NbBundle.getMessage(LegendManager.class, "TableItem.property.labels.description")+" "+i,
                                                                PreviewProperty.CATEGORY_LEGENDS).setValue(labelsGroup.get(i).toString());
        }
        
        

        PreviewProperty[] properties = {
            PreviewProperty.createProperty(this,
                                           tableProperties.get(TableProperty.TABLE_VERTICAL_EXTRA_MARGIN),
                                           Integer.class,
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.vertical.extraMargin.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.vertical.extraMargin.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultVerticalExtraMargin),
            PreviewProperty.createProperty(this,
                                           tableProperties.get(TableProperty.TABLE_HORIZONTAL_EXTRA_MARGIN),
                                           Integer.class,
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.vertical.extraMargin.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.vertical.extraMargin.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultHorizontalExtraMargin),
            PreviewProperty.createProperty(this,
                                           tableProperties.get(TableProperty.TABLE_FONT),
                                           Font.class,
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.font.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.font.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultFont),
            PreviewProperty.createProperty(this,
                                           tableProperties.get(TableProperty.TABLE_FONT_COLOR),
                                           Color.class,
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.font.color.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.font.color.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultFontColor),
            PreviewProperty.createProperty(this,
                                           tableProperties.get(TableProperty.TABLE_IS_CELL_COLORING),
                                           Boolean.class,
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.isCellColoring.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.isCellColoring.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsCellColoring),
            PreviewProperty.createProperty(this,
                                           tableProperties.get(TableProperty.TABLE_CELL_COLORING_DIRECTION),
                                           Direction.class,
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.cellColoringDirection.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.cellColoringDirection.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultCellColoringDirection),
            PreviewProperty.createProperty(this,
                                           tableProperties.get(TableProperty.TABLE_HORIZONTAL_TEXT_ALIGNMENT),
                                           Alignment.class,
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.horizontalText.alignment.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.horizontalText.alignment.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultHorizontalTextAlignment),
            PreviewProperty.createProperty(this,
                                           tableProperties.get(TableProperty.TABLE_HORIZONTAL_TEXT_POSITION),
                                           Direction.class,
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.horizontalText.position.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.horizontalText.position.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultHorizontalTextPosition),
            PreviewProperty.createProperty(this,
                                           tableProperties.get(TableProperty.TABLE_VERTICAL_TEXT_ALIGNMENT),
                                           Alignment.class,
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.alignment.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.alignment.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultVerticalTextAlignment),
            PreviewProperty.createProperty(this,
                                           tableProperties.get(TableProperty.TABLE_VERTICAL_TEXT_POSITION),
                                           Direction.class,
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.position.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.position.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultVerticalTextPosition),
            PreviewProperty.createProperty(this,
                                           tableProperties.get(TableProperty.TABLE_VERTICAL_TEXT_ROTATION),
                                           TableItem.VerticalTextDirection.class,
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.rotation.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.rotation.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultVerticalTextRotation),
            PreviewProperty.createProperty(this,
                                           tableProperties.get(TableProperty.TABLE_IS_DISPLAYING_GRID),
                                           Boolean.class,
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.grid.isDisplaying.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.grid.isDisplaying.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsDisplayingGrid),
            PreviewProperty.createProperty(this,
                                           tableProperties.get(TableProperty.TABLE_GRID_COLOR),
                                           Color.class,
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.grid.color.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.grid.color.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultGridColor),
        };


        PreviewProperty[] propertiesWithLabels = new PreviewProperty[labelProperties.length + properties.length];
        System.arraycopy(labelProperties, 0, propertiesWithLabels, 0, labelProperties.length);
        System.arraycopy(properties, 0, propertiesWithLabels, labelProperties.length, properties.length);
        return propertiesWithLabels;
    }


    @Override
    protected Boolean hasDynamicProperties() {
        return Boolean.FALSE;
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
    protected final Direction defaultHorizontalTextPosition = Direction.LEFT;
    protected final Alignment defaultHorizontalTextAlignment = Alignment.LEFT;
    // up/bottom labels
    protected final Alignment defaultVerticalTextAlignment = Alignment.LEFT;
    protected final Direction defaultVerticalTextPosition = Direction.LEFT;
    protected final TableItem.VerticalTextDirection defaultVerticalTextRotation = TableItem.VerticalTextDirection.HORIZONTAL;

}