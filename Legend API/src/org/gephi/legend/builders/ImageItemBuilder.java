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
import org.gephi.preview.api.PreviewProperties;
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
    protected boolean setDefaultValues() {
        this.defaultTitleIsDisplaying = Boolean.FALSE;
        this.defaultDescriptionIsDisplaying = Boolean.FALSE;
        return true;
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
                                                                 PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
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
//                                           PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(defaultImageFile)
//        };
//
//
//        return properties;
    }

    private File defaultImageFile = new File("/Volumes/edubecks/edubecks/Dropbox/imagenes/inception.jpg");
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
    public void writeXMLFromData(XMLStreamWriter writer, Item item) throws XMLStreamException {
    }

    @Override
    public void readXMLToData(XMLStreamReader reader, Item item) throws XMLStreamException {
    }

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new ImageItem(graph);
    }

    @Override
    public void writeXMLFromItemOwnProperties(XMLStreamWriter writer, Item item) throws XMLStreamException {
        PreviewProperty[] ownProperties = item.getData(LegendItem.OWN_PROPERTIES);
        for (PreviewProperty property : ownProperties) {
            writeXMLFromSingleProperty(writer, property);
        }
    }

    @Override
    protected ArrayList<PreviewProperty> readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException {
        ArrayList<PreviewProperty> properties = new ArrayList<PreviewProperty>();

        // own properties
        boolean end = false;
        while (reader.hasNext() && !end) {
            int type = reader.next();
            switch (type) {
                case XMLStreamReader.START_ELEMENT: {
                    PreviewProperty property = readXMLToSingleOwnProperty(reader, item);
                    properties.add(property);
                    break;
                }
                case XMLStreamReader.CHARACTERS: {
                    break;
                }
                case XMLStreamReader.END_ELEMENT: {
                    end = true;
                    break;
                }
            }
        }

        return properties;
    }

    @Override
    protected ArrayList<PreviewProperty> readXMLToDynamicProperties(XMLStreamReader reader, Item item) throws XMLStreamException {
        reader.next();
        return new ArrayList<PreviewProperty>();
    }

    @Override
    protected PreviewProperty readXMLToSingleOwnProperty(XMLStreamReader reader, Item item) throws XMLStreamException {
        String propertyName = reader.getAttributeValue(null, XML_NAME);
        String valueString = reader.getElementText();
        int propertyIndex = ImageProperty.getInstance().getProperty(propertyName);
        Class valueClass = defaultValues[propertyIndex].getClass();
        Object value = PreviewProperties.readValueFromText(valueString, valueClass);
        if (value == null) {
            value = readValueFromText(valueString, valueClass);
        }
        System.out.println("@Var: ReadingXML property: "+propertyName+" with value: "+value);
        PreviewProperty property = createLegendProperty(item, propertyIndex, value);
        return property;
    }

    
    @Override
    protected void writeXMLFromDynamicProperties(XMLStreamWriter writer, Item item) throws XMLStreamException {
    }
}
