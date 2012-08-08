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
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.CustomImageItemBuilder;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.api.CustomTableItemBuilder;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendManager;
import org.gephi.preview.api.Item;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.legend.items.ImageItem;
import org.gephi.legend.properties.ImageProperty;
import org.gephi.legend.properties.TextProperty;
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
    @ServiceProvider(service = ItemBuilder.class, position = 102),
    @ServiceProvider(service = LegendItemBuilder.class, position = 102)
})
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
        return NbBundle.getMessage(LegendManager.class, "ImageItem.name");
    }

    @Override
    protected Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel) {
        ImageItem item = new ImageItem(graph);
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

    @Override
    public ArrayList<CustomLegendItemBuilder> getAvailableBuilders() {
        Collection<? extends CustomImageItemBuilder> customBuilders = Lookup.getDefault().lookupAll(CustomImageItemBuilder.class);
        ArrayList<CustomLegendItemBuilder> availableBuilders = new ArrayList<CustomLegendItemBuilder>();
        for (CustomImageItemBuilder customBuilder : customBuilders) {
            availableBuilders.add((CustomLegendItemBuilder) customBuilder);
        }
        return availableBuilders;
    }

}
