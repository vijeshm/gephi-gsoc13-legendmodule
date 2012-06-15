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
import org.gephi.legend.api.LegendItem.Alignment;
import org.gephi.legend.api.LegendManager;
import org.gephi.legend.items.DescriptionItem;
import org.gephi.legend.properties.DescriptionProperty;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = ItemBuilder.class, position = 103)
public class DescriptionItemBuilder extends LegendItemBuilder {

    @Override
    public Item buildItem(Graph graph, AttributeModel attributeModel) {
        ArrayList<String> descriptions = new ArrayList<String>();
        descriptions.add("Test 1");
        descriptions.add("Test 2222");
        descriptions.add("Test 33");
        ArrayList<String> values = new ArrayList<String>();
        values.add("Value 11111");
        values.add("Value 222");
        values.add("Value 33333333");

        DescriptionItem item = new DescriptionItem(graph);
        item.setData(DescriptionItem.KEYS, descriptions);
        item.setData(DescriptionItem.VALUES, values);
        
        return item;
    }

    @Override
    protected PreviewProperty[] createLegendItemProperties(Item item) {

        Integer workspaceIndex = item.getData(LegendItem.WORKSPACE_INDEX);
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        ArrayList<String> textProperties = LegendManager.getProperties(DescriptionProperty.OWN_PROPERTIES, workspaceIndex, itemIndex);

        PreviewProperty[] properties = {
            PreviewProperty.createProperty(this,
                                           textProperties.get(DescriptionProperty.DESCRIPTION_KEY_FONT),
                                           Font.class,
                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultKeyFont),
            PreviewProperty.createProperty(this,
                                           textProperties.get(DescriptionProperty.DESCRIPTION_KEY_FONT_COLOR),
                                           Color.class,
                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.color.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.color.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultKeyFontColor),
            PreviewProperty.createProperty(this,
                                           textProperties.get(DescriptionProperty.DESCRIPTION_KEY_FONT_ALIGNMENT),
                                           Alignment.class,
                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.alignment.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.key.font.alignment.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultKeyAlignment),
            PreviewProperty.createProperty(this,
                                           textProperties.get(DescriptionProperty.DESCRIPTION_VALUE_FONT),
                                           Font.class,
                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultValueFont),
            PreviewProperty.createProperty(this,
                                           textProperties.get(DescriptionProperty.DESCRIPTION_VALUE_FONT_COLOR),
                                           Color.class,
                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.color.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.color.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultValueFontColor),
            PreviewProperty.createProperty(this,
                                           textProperties.get(DescriptionProperty.DESCRIPTION_VALUE_FONT_ALIGNMENT),
                                           Alignment.class,
                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.alignment.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.value.font.alignment.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultValueAlignment),
            PreviewProperty.createProperty(this,
                                           textProperties.get(DescriptionProperty.DESCRIPTION_IS_FLOW_LAYOUT),
                                           Boolean.class,
                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.isFlowLayout.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "DescriptionItem.property.isFlowLayout.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsFlowLayout)
        };


        return properties;
    }

    @Override
    public String getType() {
        return DescriptionItem.TYPE;
    }

    private Font defaultKeyFont = new Font("Arial", Font.PLAIN, 13);
    private Color defaultKeyFontColor = Color.RED;
    private Font defaultValueFont = new Font("Arial", Font.BOLD, 16);
    private Color defaultValueFontColor = Color.BLUE;
    private Alignment defaultKeyAlignment = Alignment.LEFT;
    private Alignment defaultValueAlignment = Alignment.LEFT;
    private Boolean defaultIsFlowLayout = true;
}
