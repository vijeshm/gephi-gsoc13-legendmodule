/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendManager;
import org.gephi.preview.api.Item;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.legend.items.GroupsItem;
import org.gephi.legend.properties.GroupsProperty;
import org.gephi.legend.renderers.TextItemRenderer;
import org.gephi.partition.api.NodePartition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.api.PartitionModel;
import org.gephi.preview.api.PreviewProperty;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */

@ServiceProvider(service = ItemBuilder.class, position = 103)
public class GroupsItemBuilder extends LegendItemBuilder{


    @Override
    public String getType() {
        return GroupsItem.TYPE;
    }

    @Override
    public Item buildItem(Graph graph, AttributeModel attributeModel) {
        //        PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
//        PartitionModel model = partitionController.getModel();
//        for (NodePartition partition : model.getNodePartitions()) {
//            // get groups
//        }
        
        GroupsItem item = new GroupsItem(graph);
        
        ArrayList<String> labelsGroup = new ArrayList<String>();
        labelsGroup.add("Test 1");
        labelsGroup.add("Test 2");
        labelsGroup.add("Test 3");
        ArrayList<Color> colorsGroup = new ArrayList<Color>();
        colorsGroup.add(Color.GREEN);
        colorsGroup.add(Color.BLUE);
        colorsGroup.add(Color.RED);
        item.setData(GroupsItem.COLORS_GROUP, colorsGroup);
        item.setData(GroupsItem.LABELS_GROUP, labelsGroup);
        return item;
    }

    @Override
    protected PreviewProperty[] createLegendItemProperties(Item item) {
        
        Integer workspaceIndex = item.getData(LegendItem.WORKSPACE_INDEX);
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        
        ArrayList<String> groupsProperties = LegendManager.getProperties(GroupsProperty.OWN_PROPERTIES, workspaceIndex, itemIndex);

        PreviewProperty[] properties = {
            PreviewProperty.createProperty(this,
                                           groupsProperties.get(GroupsProperty.GROUPS_NUMBER_COLUMNS),
                                           Integer.class,
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.numColumns.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.numColumns.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultNumColumns),
            PreviewProperty.createProperty(this,
                                           groupsProperties.get(GroupsProperty.GROUPS_LABEL_POSITION),
                                           LegendItem.Direction.class,
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.position.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.position.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultLabelPosition),
            PreviewProperty.createProperty(this,
                                           groupsProperties.get(GroupsProperty.GROUPS_LABEL_FONT),
                                           Font.class,
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.font.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.font.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultLabelFont),
            PreviewProperty.createProperty(this,
                                           groupsProperties.get(GroupsProperty.GROUPS_LABEL_FONT_COLOR),
                                           Color.class,
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.font.color.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.font.color.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultLabelFontColor),
            PreviewProperty.createProperty(this,
                                           groupsProperties.get(GroupsProperty.GROUPS_SHAPE),
                                           LegendItem.Shape.class,
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.shape.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.shape.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultShape),
            PreviewProperty.createProperty(this,
                                           groupsProperties.get(GroupsProperty.GROUPS_PADDING_BETWEEN_TEXT_AND_SHAPE),
                                           Integer.class,
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.paddingBetweenTextAndShape.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.paddingBetweenTextAndShape.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultPaddingBetweenTextAndShape),
            PreviewProperty.createProperty(this,
                                           groupsProperties.get(GroupsProperty.GROUPS_PADDING_BETWEEN_ELEMENTS),
                                           Integer.class,
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.paddingBetweenElements.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.paddingBetweenElements.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultPaddingBetweenElements)

        };

        return properties;
    }
    
    
            // DEFAULT PROPERTIES
    private Integer defaultNumColumns = 2;
    private LegendItem.Direction defaultLabelPosition = LegendItem.Direction.BOTTOM;
    private Color defaultLabelFontColor = Color.BLACK;
    private Font defaultLabelFont = new Font("Arial", Font.PLAIN, 36);
    private Integer defaultPaddingBetweenTextAndShape = 5;
    private Integer defaultPaddingBetweenElements = 5;
    private LegendItem.Shape defaultShape = LegendItem.Shape.TRIANGLE;
    
}
