/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import org.gephi.legend.properties.LegendProperty;
import org.gephi.preview.api.*;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author eduBecKs
 */
public class LegendNode extends AbstractNode implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public LegendNode(PropertySheet propertySheet, Item activeLegendItem) {
        super(Children.LEAF);
        this.propertySheet = propertySheet;
        this.activeLegendItem = activeLegendItem;
        setDisplayName(NbBundle.getMessage(LegendNode.class, "LegendNode.displayName"));
    }

    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        
        System.out.println("@Var: activeLegendItem: "+activeLegendItem);
        
        // regular properties
        
        
        PreviewProperty[] properties = activeLegendItem.getData(LegendItem.PROPERTIES);
        String label = properties[0].getValue();
        System.out.println("@Var: label: "+label);
        Sheet.Set itemSet = Sheet.createPropertiesSet();
        itemSet.setDisplayName(label);
        itemSet.setName(label);
        for (PreviewProperty property : properties) {
            System.out.println("@Var: createSheet property: "+property.getName());
            Node.Property nodeProperty = new PreviewPropertyWrapper(property);
            itemSet.put(nodeProperty);
        }
        
        
        // dynamic properties
        PreviewProperty[] dynamicProperties = activeLegendItem.getData(LegendItem.DYNAMIC_PROPERTIES);
        for (PreviewProperty property : dynamicProperties) {
            System.out.println("@Var: createSheet dynamic property: "+property.getName());
            Node.Property nodeProperty = new PreviewPropertyWrapper(property);
            itemSet.put(nodeProperty);
        }
        
        
        sheet.put(itemSet);

//        Sheet sheet = Sheet.createDefault();
//        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
//        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
//
//        Workspace workspace = projectController.getCurrentWorkspace();
//        PreviewModel previewModel = previewController.getModel(workspace);


//        if (previewModel != null) {
//            PreviewProperties previewProperties = previewModel.getProperties();
//            LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
//
//            if (legendManager.hasActiveLegends()) {
//
//                ArrayList<String> items = legendManager.getItems();
//
//                Sheet.Set itemSet = Sheet.createPropertiesSet();
//                String label = previewProperties.getStringValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, activeLegend, LegendProperty.LABEL));
//                itemSet.setDisplayName(label);
//                itemSet.setName(label);
//                for (PreviewProperty property : previewProperties.getProperties()) {
//                    if (LegendManager.isLegendPropertyForItem(property, items.get(activeLegend))) {
//                        Node.Property nodeProperty = new PreviewPropertyWrapper(property);
//                        itemSet.put(nodeProperty);
//                    }
//                }
//                sheet.put(itemSet);
//            }
//
////            for (int i = 0; i < items.size(); i++) {
////                Sheet.Set itemSet = Sheet.createPropertiesSet();
////                String name = "Item " + i + " [" + legendTypes.get(i) + "]";
////                itemSet.setDisplayName(name);
////                itemSet.setName(name);
////                for (PreviewProperty property : previewProperties.getProperties()) {
////                    if (LegendManager.isLegendPropertyForItem(property, items.get(i))) {
////                        Node.Property nodeProperty = new PreviewPropertyWrapper(property);
////                        itemSet.put(nodeProperty);
////                    }
////                }
////                sheet.put(itemSet);
////
////            }
//
//        }
        return sheet;
    }

    public static class PreviewPropertyWrapper extends PropertySupport.ReadWrite {

        private final PreviewProperty property;

        public PreviewPropertyWrapper(PreviewProperty previewProperty) {
            super(previewProperty.getName(), previewProperty.getType(), previewProperty.getDisplayName(), previewProperty.getDescription());
            this.property = previewProperty;
        }

        @Override
        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return property.getValue();
        }

        @Override
        public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            property.setValue(t);
        }

    }
    private PropertySheet propertySheet;
    private Integer activeLegend;
    private Item activeLegendItem;
}
