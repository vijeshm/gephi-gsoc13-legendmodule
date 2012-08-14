/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.CustomGroupsItemBuilder;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.items.LegendItem;
import org.gephi.legend.manager.LegendManager;
import org.gephi.preview.api.Item;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.legend.items.GroupsItem;
import org.gephi.legend.properties.GroupsProperty;
import org.gephi.legend.properties.ImageProperty;
import org.gephi.preview.api.PreviewProperty;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author edubecks
 */
@ServiceProviders(value = {
    @ServiceProvider(service = ItemBuilder.class, position = 103),
    @ServiceProvider(service = LegendItemBuilder.class, position = 103)
})
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
        return GroupsItem.LEGEND_TYPE;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(LegendManager.class, "GroupsItem.name");
    }

    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, property);

        switch (property) {


            case GroupsProperty.GROUPS_NUMBER_COLUMNS: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.numColumns.displayName"),
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.numColumns.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_LABEL_POSITION: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        LegendItem.Direction.class,
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.position.displayName"),
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.position.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_LABEL_FONT: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.font.displayName"),
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.font.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_LABEL_FONT_COLOR: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.font.color.displayName"),
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.font.color.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_SHAPE: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        LegendItem.Shape.class,
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.shape.displayName"),
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.shape.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_PADDING_BETWEEN_TEXT_AND_SHAPE: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.paddingBetweenTextAndShape.displayName"),
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.paddingBetweenTextAndShape.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_PADDING_BETWEEN_ELEMENTS: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.paddingBetweenElements.displayName"),
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.paddingBetweenElements.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }

            case GroupsProperty.GROUPS_SCALE_SHAPE: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.scaleShape.displayName"),
                        NbBundle.getMessage(LegendManager.class, "GroupsItem.property.scaleShape.description"),
                        PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }


        }

        return previewProperty;
    }

    @Override
    protected PreviewProperty[] createLegendOwnProperties(Item item) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        // creating one property for each label
        ArrayList<StringBuilder> labelsGroup = item.getData(GroupsItem.LABELS_IDS);
        PreviewProperty[] labelProperties = new PreviewProperty[labelsGroup.size()];
        for (int i = 0; i < labelProperties.length; i++) {
            labelProperties[i] = PreviewProperty.createProperty(this,
                                                                GroupsProperty.getLabelProperty(itemIndex, i),
                                                                String.class,
                                                                NbBundle.getMessage(LegendManager.class, "GroupsItem.property.labels.displayName") + " " + i,
                                                                NbBundle.getMessage(LegendManager.class, "GroupsItem.property.labels.description") + " " + i,
                                                                PreviewProperty.CATEGORY_LEGENDS).setValue(labelsGroup.get(i).toString());
        }

//        ArrayList<String> groupsProperties = LegendManager.getProperties(GroupsProperty.OWN_PROPERTIES, itemIndex);
//
//        PreviewProperty[] properties = {
//            PreviewProperty.createProperty(this,
//                                           groupsProperties.get(GroupsProperty.GROUPS_NUMBER_COLUMNS),
//                                           Integer.class,
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.numColumns.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.numColumns.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultNumColumns),
//            PreviewProperty.createProperty(this,
//                                           groupsProperties.get(GroupsProperty.GROUPS_LABEL_POSITION),
//                                           LegendItem.Direction.class,
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.position.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.position.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultLabelPosition),
//            PreviewProperty.createProperty(this,
//                                           groupsProperties.get(GroupsProperty.GROUPS_LABEL_FONT),
//                                           Font.class,
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.font.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.font.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultLabelFont),
//            PreviewProperty.createProperty(this,
//                                           groupsProperties.get(GroupsProperty.GROUPS_LABEL_FONT_COLOR),
//                                           Color.class,
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.font.color.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.label.font.color.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultLabelFontColor),
//            PreviewProperty.createProperty(this,
//                                           groupsProperties.get(GroupsProperty.GROUPS_SHAPE),
//                                           LegendItem.Shape.class,
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.shape.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.shape.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultShape),
//            PreviewProperty.createProperty(this,
//                                           groupsProperties.get(GroupsProperty.GROUPS_PADDING_BETWEEN_TEXT_AND_SHAPE),
//                                           Integer.class,
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.paddingBetweenTextAndShape.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.paddingBetweenTextAndShape.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultPaddingBetweenTextAndShape),
//            PreviewProperty.createProperty(this,
//                                           groupsProperties.get(GroupsProperty.GROUPS_PADDING_BETWEEN_ELEMENTS),
//                                           Integer.class,
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.paddingBetweenElements.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.paddingBetweenElements.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultPaddingBetweenElements),
//            PreviewProperty.createProperty(this,
//                                           groupsProperties.get(GroupsProperty.GROUPS_SCALE_SHAPE),
//                                           Boolean.class,
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.scaleShape.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "GroupsItem.property.scaleShape.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsScalingShapes)
//        };

        int[] properties = GroupsProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] previewProperties = new PreviewProperty[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            System.out.println("@Var: i: " + i);
            previewProperties[i] = createLegendProperty(item, properties[i], defaultValues[i]);
            System.out.println("@Var: previewProperties: " + previewProperties[i]);
        }


        PreviewProperty[] propertiesWithLabels = new PreviewProperty[labelProperties.length + previewProperties.length];
        System.arraycopy(labelProperties, 0, propertiesWithLabels, 0, labelProperties.length);
        System.arraycopy(previewProperties, 0, propertiesWithLabels, labelProperties.length, previewProperties.length);
        return propertiesWithLabels;

    }

    // DEFAULT PROPERTIES
    private Integer defaultNumColumns = 5;
    private LegendItem.Direction defaultLabelPosition = LegendItem.Direction.DOWN;
    private Color defaultLabelFontColor = Color.BLACK;
    private Font defaultLabelFont = new Font("Arial", Font.PLAIN, 15);
    private Integer defaultPaddingBetweenTextAndShape = 5;
    private Integer defaultPaddingBetweenElements = 5;
    private LegendItem.Shape defaultShape = LegendItem.Shape.RECTANGLE;
    private Boolean defaultIsScalingShapes = Boolean.FALSE;
    private final Object[] defaultValues = {
        defaultNumColumns,
        defaultShape,
        defaultIsScalingShapes,
        defaultLabelPosition,
        defaultLabelFont,
        defaultLabelFontColor,
        defaultPaddingBetweenTextAndShape,
        defaultPaddingBetweenElements
    };

    @Override
    protected Boolean hasDynamicProperties() {
        return Boolean.FALSE;
    }

    @Override
    protected Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel) {

        Item item = createNewLegendItem(graph);


        ArrayList<StringBuilder> labels = new ArrayList<StringBuilder>();
        ArrayList<Color> colors = new ArrayList<Color>();
        ArrayList<Float> values = new ArrayList<Float>();

        CustomGroupsItemBuilder customGroupsBuilder = (CustomGroupsItemBuilder) builder;
        customGroupsBuilder.retrieveData(labels, colors, values);

        item.setData(GroupsItem.COLORS, colors);
        item.setData(GroupsItem.LABELS_IDS, labels);
        item.setData(GroupsItem.VALUES, values);
        return item;

    }

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
    public void writeDataToXML(XMLStreamWriter writer, Item item) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void readXMLToData(XMLStreamReader reader, Item item) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new GroupsItem(graph);
    }

    @Override
    public PreviewProperty readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeItemOwnPropertiesToXML(XMLStreamWriter writer, Item item) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
