/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendManager;
import org.gephi.preview.api.Item;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.legend.items.ImageItem;
import org.gephi.legend.properties.ImageProperty;
import org.gephi.legend.properties.TextProperty;
import org.gephi.preview.api.PreviewProperty;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = ItemBuilder.class, position = 102)
public class ImageItemBuilder extends LegendItemBuilder {

    @Override
    protected void setDefaultValues() {
    }

    @Override
    protected boolean isBuilderForItem(Item item) {
        return item instanceof ImageItem;
    }

    @Override
    public String getType() {
        return ImageItem.LEGEND_TYPE;
    }

    @Override
    public Item buildItem(Graph graph, AttributeModel attributeModel) {
        ImageItem item = new ImageItem(graph);
        item.setData(LegendItem.SUB_TYPE, getType());
        return item;
    }

    @Override
    protected PreviewProperty[] createLegendItemProperties(Item item) {
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        ArrayList<String> imageProperties = LegendManager.getProperties(ImageProperty.OWN_PROPERTIES, itemIndex);

        PreviewProperty[] properties = {
            PreviewProperty.createProperty(this,
                                           imageProperties.get(ImageProperty.IMAGE_URL),
                                           File.class,
                                           NbBundle.getMessage(LegendManager.class, "ImageItem.property.imageURL.displayName"),
                                           NbBundle.getMessage(LegendManager.class, "ImageItem.property.imageURL.description"),
                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultImageFile)
        };


        return properties;
    }

    private File defaultImageFile = new File("/Users/edubecks/Dropbox/gsoc2012/gephi/gephi.communication/test2.png");

    @Override
    protected Boolean hasDynamicProperties() {
        return Boolean.FALSE;
    }
}
