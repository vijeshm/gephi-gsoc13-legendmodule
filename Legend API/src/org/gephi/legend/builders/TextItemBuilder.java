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
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.api.CustomTableItemBuilder;
import org.gephi.legend.api.CustomTextItemBuilder;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendItem.Alignment;
import org.gephi.legend.api.LegendManager;
import org.gephi.legend.items.TextItem;
import org.gephi.legend.properties.TextProperty;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = ItemBuilder.class, position = 101)
public class TextItemBuilder extends LegendItemBuilder {

    @Override
    protected void setDefaultValues() {
    }

    @Override
    protected boolean isBuilderForItem(Item item) {
        return item instanceof TextItem;
    }

    @Override
    public String getType() {
        return NbBundle.getMessage(LegendManager.class, "TextItem.name");
    }

    @Override
    public Item buildDefaultItem(Graph graph, AttributeModel attributeModel) {
        TextItem item = new TextItem(graph);
        item.setData(TextItem.BODY, defaultBody);
        item.setData(LegendItem.SUB_TYPE, getType());
        return item;
    }

    
    @Override
    protected Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel) {
        CustomTextItemBuilder customBuilder = (CustomTextItemBuilder) builder;
        TextItem item = new TextItem(graph);
        item.setData(TextItem.BODY, customBuilder.getText());
        System.out.println("@ debug--------------> @Var: customBuilder.getText(): "+customBuilder.getText());
        item.setData(LegendItem.SUB_TYPE, getType());
        return item;
    }

    @Override
    protected PreviewProperty[] createLegendItemProperties(Item item) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        

        ArrayList<String> textProperties = LegendManager.getProperties(TextProperty.OWN_PROPERTIES, itemIndex);

        PreviewProperty[] properties = {
            PreviewProperty.createProperty(this,
                                           textProperties.get(TextProperty.TEXT_BODY),
                                           String.class,
                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(item.getData(TextItem.BODY)),
            PreviewProperty.createProperty(this,
                                           textProperties.get(TextProperty.TEXT_BODY_FONT),
                                           Font.class,
                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultBodyFont),
            PreviewProperty.createProperty(this,
                                           textProperties.get(TextProperty.TEXT_BODY_FONT_COLOR),
                                           Color.class,
                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.color.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.font.color.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultBodyFontColor),
            PreviewProperty.createProperty(this,
                                           textProperties.get(TextProperty.TEXT_BODY_FONT_ALIGNMENT),
                                           Alignment.class,
                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.alignment.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "TextItem.property.body.alignment.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultBodyFontAlignment)
        };


        return properties;
    }

    // DEFAULT VALUES
    protected final String defaultBody = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras aliquam luctus ligula. Nunc mollis sagittis dui eget congue. Sed et turpis leo, vitae interdum magna. Pellentesque sollicitudin laoreet orci. Donec varius eleifend iaculis. Integer congue tempor nulla ac luctus. Nullam velit massa, convallis ut suscipit eget, auctor non velit. Etiam vitae velit sit amet justo luctus semper. Ut laoreet ullamcorper.";
    protected final Font defaultBodyFont = new Font("Arial", Font.PLAIN, 14);
    protected final Color defaultBodyFontColor = Color.BLUE;
    protected final Alignment defaultBodyFontAlignment = Alignment.JUSTIFIED;

    @Override
    protected Boolean hasDynamicProperties() {
        return Boolean.FALSE;
    }

    @Override
    public boolean isAvailableToBuild() {
        return true;
    }

    @Override
    public String stepsNeededToBuild() {
        return "";
    }

    
}
