package org.gephi.legend.plugin.image;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.attribute.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.AbstractLegendItemBuilder;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.spi.CustomLegendItemBuilder;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItemBuilder;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * class to build the image items.
 *
 * This class is exposed as a service. The createCustomItem method (in the
 * AbstractLegendItemRenderer) is used to create a image legend item, depending
 * on the custom item builder chosen by the user from the UI. The custom
 * builders are expected to implement the CustomImageItemBuilder interface.
 *
 * @author mvvijesh, edubecks
 */
@ServiceProviders(value = {
    @ServiceProvider(service = ItemBuilder.class, position = 104),
    @ServiceProvider(service = LegendItemBuilder.class, position = 104)
})
public class ImageItemBuilder extends AbstractLegendItemBuilder {

    // default values
    private File defaultImageFile = new File("/"); // a dummy non-existant file
    private Boolean defaultLockAspectRatio = Boolean.TRUE; // by default, the loaded images will be scaled
    private int defaultMargin = 10; // this is space between the image element borders of the legend nodes
    private final Object[] defaultValues = {
        defaultImageFile,
        defaultLockAspectRatio,
        defaultMargin
    };

    @Override
    public ArrayList<PreviewProperty> readXMLToOwnProperties(XMLStreamReader reader, Item item) throws XMLStreamException {
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

    public PreviewProperty readXMLToSingleOwnProperty(XMLStreamReader reader, Item item) throws XMLStreamException {
        String propertyName = reader.getAttributeValue(null, XML_NAME);
        String valueString = reader.getElementText();
        int propertyIndex = ImageProperty.getInstance().getProperty(propertyName);
        Class valueClass = defaultValues[propertyIndex].getClass();
        Object value = readValueFromText(valueString, valueClass);
        PreviewProperty property = createLegendProperty(item, propertyIndex, value);
        return property;
    }

    /**
     *
     * @param item - the group item being built
     * @param property - the index of the property
     * @param value - the value of the property
     * @return the PreviewProperty object populated with the property string and
     * the value
     */
    private PreviewProperty createLegendProperty(Item item, int property, Object value) {
        PreviewProperty previewProperty = null;
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        String propertyString = LegendModel.getProperty(ImageProperty.OWN_PROPERTIES, itemIndex, property);

        switch (property) {

            case ImageProperty.IMAGE_URL: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        File.class,
                        NbBundle.getMessage(ImageItemBuilder.class, "ImageItem.property.imageURL.displayName"),
                        NbBundle.getMessage(ImageItemBuilder.class, "ImageItem.property.imageURL.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case ImageProperty.LOCK_ASPECT_RATIO: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        NbBundle.getMessage(ImageItemBuilder.class, "ImageItem.property.lockAspectRatio.displayName"),
                        NbBundle.getMessage(ImageItemBuilder.class, "ImageItem.property.lockAspectRatio.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }

            case ImageProperty.IMAGE_MARGIN: {
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        NbBundle.getMessage(ImageItemBuilder.class, "ImageItem.property.imageMargin.displayName"),
                        NbBundle.getMessage(ImageItemBuilder.class, "ImageItem.property.imageMargin.description"),
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
            }
        }

        return previewProperty;
    }

    @Override
    public void writeXMLFromItemOwnProperties(XMLStreamWriter writer, Item item, PreviewProperties previewProperties) throws XMLStreamException {
        PreviewProperty[] ownProperties = item.getData(LegendItem.OWN_PROPERTIES);
        for (PreviewProperty property : ownProperties) {
            writeXMLFromSingleProperty(writer, property, previewProperties);
        }
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(ImageItemBuilder.class, "ImageItem.name");
    }

    @Override
    public Item createNewLegendItem(Graph graph) {
        return new ImageItem(graph);
    }

    /**
     *
     * @return the list of available custom builders for the image legend
     */
    @Override
    public ArrayList<CustomLegendItemBuilder> getAvailableBuilders() {
        Collection<? extends CustomImageItemBuilder> customBuilders = Lookup.getDefault().lookupAll(CustomImageItemBuilder.class);
        ArrayList<CustomLegendItemBuilder> availableBuilders = new ArrayList<CustomLegendItemBuilder>();
        for (CustomImageItemBuilder customBuilder : customBuilders) {
            availableBuilders.add((CustomLegendItemBuilder) customBuilder);
        }
        return availableBuilders;
    }

    /**
     *
     * @param item - the groups item being built
     * @return the list of PreviewProperty objects
     */
    @Override
    public PreviewProperty[] createLegendOwnProperties(Item item) {
        int[] properties = ImageProperty.LIST_OF_PROPERTIES;

        PreviewProperty[] previewProperties = new PreviewProperty[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            previewProperties[i] = createLegendProperty(item, properties[i], defaultValues[i]);
        }

        // The image legend doesnt need any extra data from any other module in order to be rendered.
        // If the renderer needs extra data, add a method in the CustomImageItemBuilder interface to retrieve the data.
        // extract the CustomImageItemBuilder from the item and invoke the method to retrieve data.
        // Once the required data is retrieved, add it to the image item in any appropriate form.

        return previewProperties;
    }

    @Override
    public Boolean hasDynamicProperties() {
        return Boolean.FALSE;
    }

    /**
     *
     * @param builder - the custom image item builder chosen by the user from
     * the UI
     * @param graph - the current graph to which the groups item belongs to
     * @param attributeModel
     * @param newItemIndex - index of the new image item being created
     * @return the newly built image legend item
     */
    @Override
    public Item buildCustomItem(CustomLegendItemBuilder builder, Graph graph, AttributeModel attributeModel, Integer newItemIndex) {
        Item item = createNewLegendItem(graph);

        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();

        // add the renderer to the legend model if it has not been added
        ImageItemRenderer imageItemRenderer = ImageItemRenderer.getInstance();
        if (!legendModel.isRendererAdded(imageItemRenderer)) {
            legendModel.addRenderer(imageItemRenderer);
        }

        // setting default renderer, item index and custom builder
        item.setData(LegendItem.RENDERER, imageItemRenderer);
        item.setData(LegendItem.ITEM_INDEX, newItemIndex);
        item.setData(LegendItem.CUSTOM_BUILDER, (CustomImageItemBuilder) builder);
        return item;
    }

    /**
     *
     * @param item - item to be checked against
     * @return True if GroupsItemBuilder can build the item. False, otherwise.
     */
    @Override
    public boolean isBuilderForItem(Item item) {
        return item instanceof ImageItem;
    }

    @Override
    public boolean setDefaultValues() {
        this.defaultTitleIsDisplaying = Boolean.FALSE;
        this.defaultDescriptionIsDisplaying = Boolean.FALSE;
        return true;
    }

    @Override
    public String getType() {
        return ImageItem.LEGEND_TYPE;
    }
}