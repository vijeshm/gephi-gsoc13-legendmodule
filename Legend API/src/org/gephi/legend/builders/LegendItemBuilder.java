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
import org.gephi.legend.properties.LegendProperty;
import org.gephi.legend.api.LegendManager;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.NbBundle;

/**
 *
 * @author edubecks
 */
public abstract class LegendItemBuilder implements ItemBuilder{

    public abstract Item buildItem(Graph graph, AttributeModel attributeModel);
    
    protected abstract PreviewProperty[] createLegendItemProperties(Item item);
    
    @Override
    public Item[] getItems(Graph graph, AttributeModel attributeModel) {
        Item[] items = new Item[1];
        Item item = buildItem(graph,attributeModel);
        item.setData(LegendItem.WORKSPACE_INDEX, LegendManager.useWorkIndex());
        item.setData(LegendItem.ITEM_INDEX, LegendManager.useItemIndex());
        item.setData(LegendItem.PROPERTIES, getLegendProperties(item));
        items[0] = item;
        return items;
    }
    
    public PreviewProperty[] createLegendProperties(Item item) {
        
        int workspaceIndex = item.getData(LegendItem.WORKSPACE_INDEX);
        int itemIndex = item.getData(LegendItem.ITEM_INDEX);


        ArrayList<String> legendProperties = LegendManager.getProperties(LegendProperty.LEGEND_PROPERTIES, workspaceIndex, itemIndex);

        return new PreviewProperty[]{
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.ORIGIN_X),
                                                   Float.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.originX.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.originX.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultOriginX),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.ORIGIN_Y),
                                                   Float.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.originY.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.originY.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultOriginY),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.TITLE_IS_DISPLAYING),
                                                   Boolean.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.isDisplaying.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.isDisplaying.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsDisplayingTitle),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.TITLE_FONT),
                                                   Font.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultTitleFont),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.TITLE_FONT_COLOR),
                                                   Color.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.color.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.title.font.color.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultTitleFontColor),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.DESCRIPTION_IS_DISPLAYING),
                                                   Boolean.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.isDisplaying.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.isDisplaying.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsDisplayingTitle),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.DESCRIPTION_FONT),
                                                   Font.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultDescriptionFont),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.DESCRIPTION_FONT_COLOR),
                                                   Color.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.color.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.description.font.color.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultDescriptionFontColor),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.WIDTH),
                                                   Float.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.width.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.width.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultWidth),
                    PreviewProperty.createProperty(this,
                                                   legendProperties.get(LegendProperty.HEIGHT),
                                                   Float.class,
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.height.displayName"),
                                                   NbBundle.getMessage(LegendManager.class, "LegendItem.property.height.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultHeight)
                };
    }
    
    
    public PreviewProperty[] getLegendProperties(Item item) {

        PreviewProperty[] legendProperties = createLegendProperties(item);
        PreviewProperty[] properties = createLegendItemProperties(item);
        PreviewProperty[] previewProperties = new PreviewProperty[legendProperties.length + properties.length];
        System.arraycopy(legendProperties, 0, previewProperties, 0, legendProperties.length);
        System.arraycopy(properties, 0, previewProperties, legendProperties.length, properties.length);

        return previewProperties;

    }
    
    
    
    //DEFAULT VALUES 
    protected float defaultOriginX = 100f;
    protected float defaultOriginY = 100f;
    protected float defaultWidth = 200f;
    protected float defaultHeight = 200f;
    protected Boolean defaultIsDisplayingTitle = true;
    protected final Font defaultTitleFont = new Font("Arial", Font.BOLD, 24);
    protected final Color defaultTitleFontColor = Color.BLACK;
    protected Boolean defaultIsDisplayingDescription = true;
    protected final Color defaultDescriptionFontColor = Color.BLACK;
    protected final Font defaultDescriptionFont = new Font("Arial", Font.PLAIN, 10);

    
}
