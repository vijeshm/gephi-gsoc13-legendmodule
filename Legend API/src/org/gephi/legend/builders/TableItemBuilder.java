/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

/**
 *
 * @author edubecks
 */
import org.gephi.legend.items.TableItem;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendItem.Direction;
import org.gephi.legend.api.LegendItem.Alignment;
import org.gephi.legend.api.LegendManager;
import org.gephi.legend.api.PartitionData;
import org.gephi.legend.api.StatisticData;
import org.gephi.legend.properties.TableProperty;
import org.gephi.legend.renderers.TableItemRenderer;
import org.gephi.partition.api.NodePartition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.api.PartitionModel;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.Lookup;
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
    protected Item buildItem(Graph graph, AttributeModel attributeModel) {
        PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
        PartitionModel model = partitionController.getModel();

        if (model != null) {
            NodePartition[] nodePartitions = model.getNodePartitions();
            System.out.println("@Var: num Partitions: " + nodePartitions.length);


            Float[][] tableValues = new Float[nodePartitions.length][nodePartitions.length];
            for (int i = 0; i < nodePartitions.length; i++) {
                System.out.println("@Var: nodePartitions: (elements):" + nodePartitions[i].getElementsCount());

                for (int j = 0; j < nodePartitions.length; j++) {
                    tableValues[i][j] = 0f;

                }
            }
        }

        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<Color> colors = new ArrayList<Color>();
        ArrayList<ArrayList<Float>> values = new ArrayList<ArrayList<Float>>();
        System.out.println("@Var: values: " + values);
        StatisticData.generateTable(labels, colors, values);
        System.out.println("@Var: labels: " + labels);
        System.out.println("@Var: colors: " + colors);
        System.out.println("@Var: values: " + values);


        TableItem item = new TableItem(graph);
        item.setData(TableItem.LABELS_IDS, labels);
        item.setData(TableItem.TABLE_VALUES, values);
        item.setData(TableItem.LIST_OF_COLORS, colors);
        item.setData(LegendItem.SUB_TYPE, getType());
        return item;
    }

    

    @Override
    protected PreviewProperty[] createLegendItemProperties(Item item) {



        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
//        StringBuilder labelsJSON = new StringBuilder();
//        for (int i = 0; i < labelsGroup.size() - 1; i++) {
//
//            labelsJSON.append("\"" + labelsGroup.get(i) + "\" : \"" + labelsGroup.get(i) + "\",\n");
//        }
//        labelsJSON.append("\"" + labelsGroup.get(labelsGroup.size() - 1) + "\" : \"" + labelsGroup.get(labelsGroup.size() - 1) + "\"");
//        System.out.println("@Var: labelsJSON: " + labelsJSON);
////        item.setData(TableItem.LABELS, labelsJSON.toString());
//
        ArrayList<String> tableProperties = LegendManager.getProperties(TableProperty.OWN_PROPERTIES, itemIndex);


        ArrayList<String> labelsGroup = item.getData(TableItem.LABELS_IDS);
        PreviewProperty[] labelProperties = new PreviewProperty[labelsGroup.size()];
        for (int i = 0; i < labelProperties.length; i++) {
            labelProperties[i] = PreviewProperty.createProperty(this,
                                                                TableProperty.getLabelProperty(itemIndex, i),
                                                                String.class,
                                                                NbBundle.getMessage(LegendManager.class, "TableItem.property.labels.displayName")+" "+i,
                                                                NbBundle.getMessage(LegendManager.class, "TableItem.property.labels.description")+" "+i,
                                                                PreviewProperty.CATEGORY_LEGENDS).setValue(labelsGroup.get(i));

        }

        PreviewProperty[] properties = {
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_LABELS),
//                                           String.class,
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.labels.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.labels.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(labelsJSON.toString()),
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
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultVerticalTextRotation),};

        
        PreviewProperty[] tableItemProperties = new PreviewProperty[labelProperties.length + properties.length];
        System.arraycopy(labelProperties, 0, tableItemProperties, 0, labelProperties.length);
        System.arraycopy(properties, 0, tableItemProperties, labelProperties.length, properties.length);

        return tableItemProperties;
    }

    //default values
    protected final Integer defaultVerticalExtraMargin = 3;
    protected final Integer defaultHorizontalExtraMargin = 3;
    protected final Integer defaultMinimumMargin = 3;
    protected final Font defaultFont = new Font("Arial", Font.PLAIN, 13);
    protected final Color defaultFontColor = Color.BLACK;
    protected final Boolean defaultIsCellColoring = true;
    protected final Direction defaultCellColoringDirection = Direction.UP;
    protected final Direction defaultHorizontalTextPosition = Direction.LEFT;
    protected final Alignment defaultHorizontalTextAlignment = Alignment.LEFT;
    protected final Alignment defaultVerticalTextAlignment = Alignment.LEFT;
    protected final Direction defaultVerticalTextPosition = Direction.LEFT;
    protected final TableItem.VerticalTextDirection defaultVerticalTextRotation = TableItem.VerticalTextDirection.DIAGONAL;

    @Override
    protected Boolean hasDynamicProperties() {
        return Boolean.FALSE;
    }

    @Override
    public boolean isAvailableToBuild() {
        PartitionData partitionData = new PartitionData();
        return partitionData.isPartitioned();
    }

    @Override
    public String stepsNeededToBuild() {
        return NbBundle.getMessage(LegendManager.class, "TableItem.stepsNeeded");
    }

}