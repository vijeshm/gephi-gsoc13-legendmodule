/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.manager;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.legend.builders.LegendItemBuilder;
import org.gephi.legend.items.LegendItem;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

/**
 *
 * @author edubecks
 */
public class LegendController {

    private Collection<? extends LegendItemBuilder> registerLegendBuilders() {
        builders = new HashMap<String, LegendItemBuilder>();

        // retrieving available builders
        Collection<? extends LegendItemBuilder> legendItemBuilders =
                Lookup.getDefault().lookupAll(LegendItemBuilder.class);

        // registering builders
        for (LegendItemBuilder legendItemBuilder : legendItemBuilders) {
            builders.put(legendItemBuilder.getType(), legendItemBuilder);
        }

        return legendItemBuilders;
    }

    public void addItemToLegendManager(Workspace workspace, Item item) {

        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel(workspace);
        PreviewProperties previewProperties = previewModel.getProperties();

        LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);


        legendManager.addItem(item);

        // LEGEND PROPERTIES
        PreviewProperty[] legendProperties = item.getData(LegendItem.PROPERTIES);
        for (PreviewProperty property : legendProperties) {
            previewController.getModel().getProperties().addProperty(property);
//            previewController.getModel().getProperties().putValue(property.getName(), property.getValue());
        }
        // LEGEND OWN PROPERTIES
        PreviewProperty[] ownProperties = item.getData(LegendItem.OWN_PROPERTIES);
        for (PreviewProperty property : ownProperties) {
            previewController.getModel().getProperties().addProperty(property);
//            previewController.getModel().getProperties().putValue(property.getName(), property.getValue());
        }
        // DYNAMIC PROPERTIES
        PreviewProperty[] dynamicProperties = item.getData(LegendItem.DYNAMIC_PROPERTIES);
        for (PreviewProperty property : dynamicProperties) {
            previewController.getModel().getProperties().addProperty(property);
//            previewController.getModel().getProperties().putValue(property.getName(), property.getValue());
        }
    }

    public void writeXML(XMLStreamWriter writer, Workspace workspace) {
        try {
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel(workspace);
            PreviewProperties previewProperties = previewModel.getProperties();

            if (previewProperties.hasProperty(LegendManager.LEGEND_PROPERTIES)) {

                LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
                ArrayList<Item> legendItems = legendManager.getLegendItems();


                writer.writeStartElement(XML_LEGENDS);
                for (Item item : legendItems) {
                    LegendItemBuilder builder = builders.get(item.getType());

                    writer.writeStartElement(XML_LEGEND_ITEM);
                    builder.writeXMLFromItem(writer, item);
                    writer.writeEndElement();
                }
                writer.writeEndElement();

            }
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void readXMLToLegendManager(XMLStreamReader reader, Workspace workspace) {
        try {
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel(workspace);
            PreviewProperties previewProperties = previewModel.getProperties();


            LegendManager legendManager = new LegendManager();
            previewProperties.putValue(LegendManager.LEGEND_PROPERTIES, legendManager);


            int newItemIndex = 0;
            boolean end = false;
            while (reader.hasNext() && !end) {

                // legend item

                int type = reader.next();
                String legendItem = reader.getLocalName();
                switch (type) {
                    case XMLStreamReader.START_ELEMENT: {

                        if (legendItem.equals(XML_LEGEND_ITEM)) {
                            reader.next();
                            String legendType = reader.getElementText();
                            LegendItemBuilder builder = builders.get(legendType);
                            Item item = builder.readXMLToItem(reader, newItemIndex);

                            // adding item
                            addItemToLegendManager(workspace, item);
                            
                            // finish reading item
                            reader.next();
                            newItemIndex++;
                        }
                        break;
                    }
                    case XMLStreamReader.CHARACTERS: {
                        break;
                    }
                    case XMLStreamReader.END_ELEMENT: {
                        // finish reading legends
                        reader.next();
                        end = true;
                        break;
                    }
                }


            }
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }

    }

    private static LegendController instance = new LegendController();
    // available builders
    private Map<String, LegendItemBuilder> builders;
    private Collection<? extends LegendItemBuilder> availablebuilders;

    /*
     * returns a Collection of the available Legend Item Builders in the system
     * Default list:
     * TableItem Builder
     * ImageItem Builder
     * GroupsItem Builder
     * Descriptiontem Builder
     */
    public Collection<? extends LegendItemBuilder> getAvailablebuilders() {
        return availablebuilders;
    }

    private LegendController() {
        builders = new HashMap<String, LegendItemBuilder>();
        availablebuilders = registerLegendBuilders();
    }

    public static LegendController getInstance() {
        return instance;
    }

    private static final String XML_LEGEND_ITEM = "legenditem";
    public static final String XML_LEGENDS = "legends";
}
