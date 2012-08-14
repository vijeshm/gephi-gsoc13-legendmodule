/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.manager;

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
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
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.project.spi.WorkspacePersistenceProvider;
import org.openide.util.Lookup;

/**
 *
 * @author edubecks
 */
public class LegendController {

    private Collection<? extends LegendItemBuilder> registerLegendBuilders() {
        // cleaning combobox
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
//            Item activeLegendItem = legendManager.getActiveLegendItem();

        // LEGEND PROPERTIES
        PreviewProperty[] legendProperties = item.getData(LegendItem.PROPERTIES);
        for (PreviewProperty property : legendProperties) {
            previewController.getModel().getProperties().putValue(property.getName(), property.getValue());
        }
        // LEGEND OWN PROPERTIES
        PreviewProperty[] ownProperties = item.getData(LegendItem.OWN_PROPERTIES);
        for (PreviewProperty property : ownProperties) {
            previewController.getModel().getProperties().putValue(property.getName(), property.getValue());
        }
        // DYNAMIC PROPERTIES
        PreviewProperty[] dynamicProperties = item.getData(LegendItem.DYNAMIC_PROPERTIES);
        for (PreviewProperty property : dynamicProperties) {
            previewController.getModel().getProperties().putValue(property.getName(), property.getValue());
        }
    }

    public void writeXML(XMLStreamWriter writer, Workspace workspace) {
        try {
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel(workspace);
            PreviewProperties previewProperties = previewModel.getProperties();

            if (previewProperties.hasProperty(LegendManager.LEGEND_PROPERTIES)) {

                // testing
                StringWriter stringWriter = new StringWriter();
                XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
                XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);

                LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
                ArrayList<Item> legendItems = legendManager.getLegendItems();
                for (Item item : legendItems) {
                    LegendItemBuilder builder = builders.get(item.getType());

                    writer.writeStartElement(XML_LEGEND_ITEM);
                    builder.writeItemToXML(writer, item);
                    writer.writeEndElement();

                    //debug

                    xmlStreamWriter.writeStartElement(XML_LEGEND_ITEM);
                    builder.writeItemToXML(xmlStreamWriter, item);
                    xmlStreamWriter.writeEndElement();





                }

                System.out.println("@Var: stringWriter: " + stringWriter);
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

            System.out.printf("__HERE __ trying to read from controller \n");


            int newItemIndex = 0;
            boolean end = false;
            while (reader.hasNext() && !end) {

                // legend item

                int type = reader.next();
                switch (type) {
                    case XMLStreamReader.START_ELEMENT: {
                        String legendItem = reader.getLocalName();
                        System.out.println("@Var: legendItem: " + legendItem);

                        if (legendItem.equals(XML_LEGEND_ITEM)) {
                            reader.next();
                            String legendType = reader.getElementText();
                            LegendItemBuilder builder = builders.get(legendType);
                            Item item = builder.createItemFromXML(reader, newItemIndex);

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
}
