/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.CustomImageItemBuilder;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.items.ImageItem;
import org.gephi.legend.items.LegendItem;
import org.gephi.legend.manager.LegendManager;
import org.gephi.legend.properties.ImageProperty;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
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
        return ImageItem.LEGEND_TYPE;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(LegendManager.class, "ImageItem.name");
    }

    @Override
    protected Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel) {

        Item item = createNewLegendItem(graph);
        return item;
    }

    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendManager.getProperty(ImageProperty.OWN_PROPERTIES, itemIndex, property);

        switch (property) {
            case ImageProperty.IMAGE_URL: {
                previewProperty = PreviewProperty.createProperty(this,
                                                                 propertyString,
                                                                 File.class,
                                                                 NbBundle.getMessage(LegendManager.class, "ImageItem.property.imageURL.displayName"),
                                                                 NbBundle.getMessage(LegendManager.class, "ImageItem.property.imageURL.description"),
                                                                 PreviewProperty.CATEGORY_LEGENDS).setValue(value);
                break;
            }
        }

        return previewProperty;
    }

    @Override
    protected PreviewProperty[] createLegendOwnProperties(Item item) {

        int[] properties = ImageProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] previewProperties = new PreviewProperty[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            System.out.println("@Var: i: " + i);
            previewProperties[i] = createLegendProperty(item, properties[i], defaultValues[i]);
        }

        return previewProperties;


//        
//        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
//
//        ArrayList<String> imageProperties = LegendManager.getProperties(ImageProperty.OWN_PROPERTIES, itemIndex);
//
//        PreviewProperty[] properties = {
//            PreviewProperty.createProperty(this,
//                                           imageProperties.get(ImageProperty.IMAGE_URL),
//                                           File.class,
//                                           NbBundle.getMessage(LegendManager.class, "ImageItem.property.imageURL.displayName"),
//                                           NbBundle.getMessage(LegendManager.class, "ImageItem.property.imageURL.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultImageFile)
//        };
//
//
//        return properties;
    }

    private File defaultImageFile = new File("/Users/edubecks/Dropbox/gsoc2012/gephi/gephi.communication/test2.png");
    private final Object[] defaultValues = {
        defaultImageFile
    };

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
        return new ImageItem(graph);
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
