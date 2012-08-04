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
import org.gephi.legend.api.CustomGroupsItemBuilder;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendManager;
import org.gephi.legend.api.PartitionData;
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
public class GroupsItemBuilder extends LegendItemBuilder {

    @Override
    protected void setDefaultValues() {
    }

    @Override
    protected boolean isBuilderForItem(Item item) {
        return item instanceof GroupsItem;
    }

    @Override
    public String getType() {
        return NbBundle.getMessage(LegendManager.class, "GroupsItem.name");
    }   


    @Override
    public Item buildDefaultItem(Graph graph, AttributeModel attributeModel) {
        


        
        PartitionData partitionData = new PartitionData();


        ArrayList<String> labelsGroup = partitionData.getLabels();
        ArrayList<Color> colorsGroup = partitionData.getColors();
        ArrayList<Float> valuesGroup = partitionData.getValues();


        for (int i = 0; i < valuesGroup.size(); i++) {
            System.out.println("@Var: labelsGroup: " + labelsGroup.get(i));
            System.out.println("@Var: valuesGroup: " + valuesGroup.get(i));
            System.out.println("@Var: colorsGroup: " + colorsGroup.get(i));
        }


        GroupsItem item = new GroupsItem(graph);
        
        System.out.println("@Var: Group buildItem: "+item.getType());
        
        item.setData(GroupsItem.COLORS_GROUP, colorsGroup);
        item.setData(GroupsItem.LABELS_GROUP, labelsGroup);
        item.setData(GroupsItem.VALUES_GROUP, valuesGroup);
        return item;
    }

    @Override
    protected PreviewProperty[] createLegendItemProperties(Item item) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        ArrayList<String> groupsProperties = LegendManager.getProperties(GroupsProperty.OWN_PROPERTIES, itemIndex);

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
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultPaddingBetweenElements),
            PreviewProperty.createProperty(this,
                                           groupsProperties.get(GroupsProperty.GROUPS_SCALE_SHAPE),
                                           Boolean.class,
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.scaleShape.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.scaleShape.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsScalingShapes)
        };

        return properties;
    }

    // DEFAULT PROPERTIES
    private Integer defaultNumColumns = 5;
    private LegendItem.Direction defaultLabelPosition = LegendItem.Direction.BOTTOM;
    private Color defaultLabelFontColor = Color.BLACK;
    private Font defaultLabelFont = new Font("Arial", Font.PLAIN, 15);
    private Integer defaultPaddingBetweenTextAndShape = 5;
    private Integer defaultPaddingBetweenElements = 5;
    private LegendItem.Shape defaultShape = LegendItem.Shape.RECTANGLE;
    private Boolean defaultIsScalingShapes = Boolean.FALSE;

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
        return NbBundle.getMessage(LegendManager.class, "GroupsItem.stepsNeeded");
    }

    @Override
    protected Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel) {
        
        GroupsItem item = new GroupsItem(graph);
        
        ArrayList<String> labels = new ArrayList<String>(); 
        ArrayList<Color> colors = new ArrayList<Color>(); 
        ArrayList<Float> values =  new ArrayList<Float>();
        
        CustomGroupsItemBuilder customGroupsBuilder = (CustomGroupsItemBuilder)builder;
        customGroupsBuilder.retrieveData(labels, colors, values);
        
        item.setData(GroupsItem.COLORS_GROUP, colors);
        item.setData(GroupsItem.LABELS_GROUP, labels);
        item.setData(GroupsItem.VALUES_GROUP, values);
        
        return item;
        
    }
    
    


}
