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

    public LegendNode(PropertySheet propertySheet) {
        super(Children.LEAF);
        this.propertySheet = propertySheet;
        setDisplayName(NbBundle.getMessage(LegendNode.class, "LegendNode.displayName"));
    }

    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);

        Workspace workspace = projectController.getCurrentWorkspace();
        PreviewModel previewModel = previewController.getModel(workspace);


        if (previewModel != null) {
            PreviewProperties previewProperties = previewModel.getProperties();
            LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);

            ArrayList<String> items = legendManager.getItems();
            ArrayList<String> legendTypes = legendManager.getLegendTypes();

            for (int i = 0; i < items.size(); i++) {
                Sheet.Set itemSet = Sheet.createPropertiesSet();
                String name = "Item " + i + " [" + legendTypes.get(i) + "]";
                itemSet.setDisplayName(name);
                itemSet.setName(name);
                for (PreviewProperty property : previewProperties.getProperties()) {
                    if (LegendManager.isLegendPropertyForItem(property, items.get(i))) {
                        Node.Property nodeProperty = new PreviewPropertyWrapper(property);
                        itemSet.put(nodeProperty);
                    }
                }
                sheet.put(itemSet);

            }

        }
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
}
