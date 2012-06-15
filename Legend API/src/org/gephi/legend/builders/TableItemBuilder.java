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
import org.gephi.legend.properties.TableProperty;
import org.gephi.legend.renderers.TableItemRenderer;
import org.gephi.partition.api.NodePartition;
import org.gephi.partition.api.PartitionController;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ItemBuilder.class, position = 100)
public class TableItemBuilder extends LegendItemBuilder {

    @Override
    public String getType() {
        return TableItem.TYPE;
    }

    @Override
    public Item buildItem(Graph graph, AttributeModel attributeModel) {
        //colors
        ArrayList<Color> listOfColors;
        listOfColors = new ArrayList<Color>();
        listOfColors.add(Color.RED);
        listOfColors.add(Color.BLUE);
        listOfColors.add(Color.LIGHT_GRAY);
        listOfColors.add(Color.YELLOW);
        listOfColors.add(Color.WHITE);
        listOfColors.add(Color.GREEN);
        listOfColors.add(Color.PINK);
        listOfColors.add(Color.MAGENTA);

        //values
        Float[][] tableValues1 = {
            {9.11627487663f, 5.35347285074f, 5.53021302669f, 6.20519225239f, 5.27466987493f, 5.48850364226f, 5.48284905154f, 5.65304915413f},
            {6.11032214404f, 5.2818160758f, 5.30893993444f, 5.30624373045f, 6.37271356504f, 5.25206050232f, 5.28980850706f, 6.03624180324f},
            {5.04510203573f, 5.11945988425f, 5.17872911783f, 5.10501075623f, 9.25288976257f, 6.31804895499f, 5.17481323163f, 5.3245338662f},
            {5.00305453268f, 5.78153226657f, 5.21040577561f, 5.28651327902f, 6.07657004397f, 6.43552722671f, 5.33661053887f, 6.16149432112f},
            {5.41582039369f, 8.01040455143f, 5.70189186658f, 7.19051905857f, 5.48662725091f, 5.36079587503f, 5.65666867777f, 6.51873273528f},
            {5.44276841441f, 5.68705442654f, 5.45203949569f, 6.3702550653f, 6.25203315678f, 6.40009442175f, 8.28908129072f, 7.81318233426f},
            {5.35181089016f, 5.07697000134f, 5.76683083336f, 5.04885627802f, 6.51540268438f, 5.05027735868f, 7.21679046513f, 5.58756039989f},
            {5.24203982658f, 5.67995857207f, 7.75338861595f, 7.95242620409f, 5.45866613248f, 5.34302649296f, 5.26438348233f, 5.70365120776f}};


        //label
        ArrayList<String> labels1;
        labels1 = new ArrayList<String>();
        labels1.add("Juan");
        labels1.add("QWERTYYY");
        labels1.add("John");
        labels1.add("Perez");
        labels1.add("Nada");
        labels1.add("Nada de Nada");
        labels1.add("Un Nombre");
        labels1.add("Holaaaaaaaaaa");


        PartitionController pc = Lookup.getDefault().lookup(PartitionController.class);
        if (pc.getModel() != null) {
            pc.refreshPartitions();
            NodePartition[] nodePartitions = pc.getModel().getNodePartitions();
            Float[][] tableValues = new Float[nodePartitions.length][nodePartitions.length];
            for (int i = 0; i < nodePartitions.length; i++) {
                for (int j = 0; j < nodePartitions.length; j++) {
                    tableValues[i][j] = 0f;

                }
            }

        }

        TableItem item = new TableItem(graph);
        item.setData(TableItem.LABELS, labels1);
        item.setData(TableItem.TABLE_VALUES, tableValues1);
        item.setData(TableItem.LIST_OF_COLORS, listOfColors);
        return item;


    }

    @Override
    protected PreviewProperty[] createLegendItemProperties(Item item) {



        Integer workspaceIndex = item.getData(LegendItem.WORKSPACE_INDEX);
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        ArrayList<String> tableProperties = LegendManager.getProperties(TableProperty.OWN_PROPERTIES, workspaceIndex, itemIndex);



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
                                           tableProperties.get(TableProperty.TABLE_VERTICAL_TEXT_POSITION),
                                           Float.class,
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.rotation.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TableItem.property.verticalText.rotation.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultVerticalTextRotation),
        };


        return properties;
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
    protected final Float defaultVerticalTextRotation = 90f;
}