/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import com.sun.corba.se.impl.interceptors.PICurrent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItemBuilder;
import org.gephi.legend.spi.LegendItemRenderer;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

/**
 *
 * @author edubecks
 */
public class LegendController {

    private ArrayList<PropertyChangeListener> listeners;
    private Map<String, LegendItemRenderer> renderers;
    private Map<String, LegendItemBuilder> builders;
    private Collection<? extends LegendItemBuilder> availableBuilders;
    private Item selectedItem = null;
    private static LegendController instance = new LegendController();
    private static final String XML_LEGEND_ITEM = "legenditem";
    public static final String XML_LEGENDS = "legends";
    public static final String LEGEND_ITEM_SELECTED = "legend.item.selected";

    private LegendController() {
        // initialize references
        builders = new HashMap<String, LegendItemBuilder>();
        renderers = new HashMap<String, LegendItemRenderer>();
        listeners = new ArrayList<PropertyChangeListener>();

        // populate available builders
        availableBuilders = Lookup.getDefault().lookupAll(LegendItemBuilder.class);
        
        // populate 'builders' and 'renderers' with all the builders and renderers
        registerLegendBuilders();
        registerLegendRenderers();
    }
    
    
    /*
     * Returns an Instance of LegendController
     */    
    public static LegendController getInstance() {
        return instance;
    }

    /**
     * Returns LegendModel instance of given workspace
     *
     * @param workspace Workspace
     * @return LegendModel instance
     */
    public LegendModel getLegendModel(Workspace workspace) {
        LegendModel legendModel = workspace.getLookup().lookup(LegendModel.class);

        if (legendModel == null) {
            legendModel = new LegendModel(workspace);
            workspace.add(legendModel);
        }

        return legendModel;
    }

    /**
     * Returns LegendModel instance of current workspace
     *
     * @return LegendModel instance
     */
    public LegendModel getLegendModel() {
        return getLegendModel(Lookup.getDefault().lookup(ProjectController.class).getCurrentWorkspace());
    }

    private void registerLegendBuilders() {
        // registering builders
        for (LegendItemBuilder legendItemBuilder : availableBuilders) {
            builders.put(legendItemBuilder.getType(), legendItemBuilder);
        }
    }

    private void registerLegendRenderers() {
        // retrieving available renderers
        Collection<? extends LegendItemRenderer> legendItemRenderers = Lookup.getDefault().lookupAll(LegendItemRenderer.class);

        // registering renderers
        for (LegendItemRenderer legendItemRenderer : legendItemRenderers) {
            renderers.put(legendItemRenderer.getClass().getName(), legendItemRenderer);
        }
    }

    public void addItemToLegendModel(Item item) {
        addItemToLegendModel(Lookup.getDefault().lookup(ProjectController.class).getCurrentWorkspace(), item);
    }

    /**
     * Add an item
     *
     * @param workspace
     * @param item
     */
    public void addItemToLegendModel(Workspace workspace, Item item) {
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel(workspace);
        PreviewProperties previewProperties = previewModel.getProperties();

        LegendModel legendModel = getLegendModel(workspace);

        legendModel.addItem(item);

        // LEGEND PROPERTIES
        PreviewProperty[] legendProperties = item.getData(LegendItem.PROPERTIES);

        /////!Important: We put simple values instead of PreviewProperty in the properties because of 2 reasons:
        /////     #An old version of Gephi would read and show Legend preview properties if a project file with legends is loaded into it.
        /////     #Renderer manager removes any PreviewProperty that is not explicitely declared by a renderer in its 'getProperties' method
        /////      so when a renderer is disabled, its properties are not shown in the default sheet.
        /////      
        /////      Renderer manager does not remove simple values because they are not automatically shown in the UI, as they are intended for this kind of usage.

        for (PreviewProperty property : legendProperties) {
            previewProperties.putValue(property.getName(), property.getValue());
        }
        
        // LEGEND OWN PROPERTIES
        PreviewProperty[] ownProperties = item.getData(LegendItem.OWN_PROPERTIES);
        for (PreviewProperty property : ownProperties) {
            previewProperties.putValue(property.getName(), property.getValue());
        }
        
        // DYNAMIC PROPERTIES
        PreviewProperty[] dynamicProperties = item.getData(LegendItem.DYNAMIC_PROPERTIES);
        for (PreviewProperty property : dynamicProperties) {
            previewProperties.putValue(property.getName(), property.getValue());
        }
    }

    public void writeXML(XMLStreamWriter writer, Workspace workspace) {
        try {
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel(workspace);
            PreviewProperties previewProperties = previewModel.getProperties();

            LegendModel legendModel = getLegendModel(workspace);

            ArrayList<Item> legendItems = legendModel.getActiveItems();

            writer.writeStartElement(XML_LEGENDS);
            for (Item item : legendItems) {
                LegendItemBuilder builder = builders.get(item.getType());

                writer.writeStartElement(XML_LEGEND_ITEM);
                builder.writeXMLFromItem(writer, item, previewProperties);
                writer.writeEndElement();
            }
            writer.writeEndElement();
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void readXMLToLegendModel(XMLStreamReader reader, Workspace workspace) {
        try {
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel(workspace);

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
                            addItemToLegendModel(workspace, item);

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

    /*
     * returns a Collection of the available Legend Item Builders in the system
     * Default list:
     * TableItem Builder
     * ImageItem Builder
     * GroupsItem Builder
     * Descriptiontem Builder
     */
    public Collection<? extends LegendItemBuilder> getAvailableBuilders() {
        return availableBuilders;
    }

    public Map<String, LegendItemRenderer> getRenderers() {
        return renderers;
    }

    public void selectItem(Item item) {
        // this method gets executed when a new item is selected. 
        // It will notify(?) all the registered listeners that the property "legend.item.selected" has been changed from "selectedItem" to "item"
        selectedItem = item;
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(new PropertyChangeEvent(this, LEGEND_ITEM_SELECTED, selectedItem, item));
        }
    }

    public void addListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    public boolean removeListener(PropertyChangeListener listener) {
        return listeners.remove(listener);
    }

    public boolean updateItemDynamicPreviewProperties(Item item, int numOfProperties) {
        if (!(item instanceof LegendItem.DynamicItem)) {
            return false;
        }
        
        LegendItem.DynamicItem dynamicItem = (LegendItem.DynamicItem) item;
        int currentNumOfProperties = ((Integer) (item.getData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES))).intValue();
        if (numOfProperties == currentNumOfProperties) {
            return false;
        }
        
        dynamicItem.updateDynamicProperties(numOfProperties);
        item.setData(LegendItem.NUMBER_OF_DYNAMIC_PROPERTIES, numOfProperties);
        return true;
    }
}