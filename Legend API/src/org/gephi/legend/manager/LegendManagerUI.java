/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.manager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditorManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.builders.*;
import org.gephi.legend.items.*;
import org.gephi.legend.items.propertyeditors.DescriptionItemElementPropertyEditor;
import org.gephi.legend.properties.LegendProperty;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.PreviewUI;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author mvvijesh, edubecks
 */
@ServiceProvider(service = PreviewUI.class, position = 404)
public class LegendManagerUI extends javax.swing.JPanel implements PreviewUI {

    private final LegendController legendController;

    /**
     * Creates new form LegendManagerUI
     */
    public LegendManagerUI() {
        legendController = LegendController.getInstance();
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        controls = new javax.swing.JPanel();
        addLegendButton = new javax.swing.JButton();
        removeLegendButton = new javax.swing.JButton();
        moveLayers = new javax.swing.JPanel();
        moveUp = new javax.swing.JButton();
        moveDown = new javax.swing.JButton();
        legendManagerPanel = new javax.swing.JPanel();
        legendLayers = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        controls.setLayout(new javax.swing.BoxLayout(controls, javax.swing.BoxLayout.LINE_AXIS));

        addLegendButton.setText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.addLegendButton.text")); // NOI18N
        addLegendButton.setToolTipText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.addLegendButton.toolTipText")); // NOI18N
        addLegendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLegendButtonActionPerformed(evt);
            }
        });
        controls.add(addLegendButton);

        removeLegendButton.setText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.removeLegendButton.text")); // NOI18N
        removeLegendButton.setToolTipText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.removeLegendButton.toolTipText")); // NOI18N
        removeLegendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeLegendButtonActionPerformed(evt);
            }
        });
        controls.add(removeLegendButton);

        moveLayers.setLayout(new javax.swing.BoxLayout(moveLayers, javax.swing.BoxLayout.Y_AXIS));

        moveUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gephi/legend/graphics/moveup_8.png"))); // NOI18N
        moveUp.setText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.moveUp.text")); // NOI18N
        moveUp.setToolTipText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.moveUp.toolTipText")); // NOI18N
        moveUp.setMargin(new java.awt.Insets(0, 0, 0, 0));
        moveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveUpActionPerformed(evt);
            }
        });
        moveLayers.add(moveUp);

        moveDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gephi/legend/graphics/movedown_8.png"))); // NOI18N
        moveDown.setText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.moveDown.text")); // NOI18N
        moveDown.setToolTipText(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.moveDown.toolTipText")); // NOI18N
        moveDown.setMargin(new java.awt.Insets(0, 0, 0, 0));
        moveDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveDownActionPerformed(evt);
            }
        });
        moveLayers.add(moveDown);

        controls.add(moveLayers);

        add(controls);

        legendManagerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.legendManagerPanel.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP)); // NOI18N
        legendManagerPanel.setLayout(new java.awt.GridBagLayout());

        legendLayers.setMinimumSize(new java.awt.Dimension(152, 57));
        legendLayers.setPreferredSize(new java.awt.Dimension(152, 57));
        legendLayers.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        legendManagerPanel.add(legendLayers, gridBagConstraints);

        add(legendManagerPanel);
        legendManagerPanel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.legendManagerPanel.AccessibleContext.accessibleName")); // NOI18N
        legendManagerPanel.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.legendManagerPanel.AccessibleContext.accessibleDescription")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    private void addLegendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLegendButtonActionPerformed
        // this method is executed when the user clicks on the add button in the legend manager.

        // show a dialog box that lists all the legend builders. The user is expected to choose any one.
        Collection<? extends LegendItemBuilder> availableBuilders = legendController.getAvailablebuilders();
        Object[] availableBuildersArray = availableBuilders.toArray();
        LegendItemBuilder chosenLegend = (LegendItemBuilder) JOptionPane.showInputDialog(this, "Choose the type of Legend:", "Add Legend", JOptionPane.PLAIN_MESSAGE, null, availableBuildersArray, availableBuildersArray[0]);

        // if the user hits cancel, chosenLegend will become null, and no further operations must take place.
        if (chosenLegend != null) {
            // retrieve the list of custom builders. if there are more than one builders (including the Default), allow the user to select the custom builder by providing a dialog.

            ArrayList<CustomLegendItemBuilder> chosenLegendCustomBuilders = chosenLegend.getAvailableBuilders();
            CustomLegendItemBuilder chosenLegendCustomBuilder = chosenLegendCustomBuilders.get(0); //for Default
            //if there is more than one custom builder, allow the user to choose between them.
            if (chosenLegendCustomBuilders.size() > 1) {
                Object[] chosenLegendCustomBuildersArray = chosenLegendCustomBuilders.toArray();
                chosenLegendCustomBuilder = (CustomLegendItemBuilder) JOptionPane.showInputDialog(this, "Choose the custom builder:", "Custom Builder", JOptionPane.PLAIN_MESSAGE, null, chosenLegendCustomBuildersArray, chosenLegendCustomBuildersArray[0]);
            }

            // if the user hits cancel, chosenLegendCustomBuilder will become null, and no further operations must take place.
            if (chosenLegendCustomBuilder != null) {
                // build the legend item
                LegendManager legendManager = legendController.getLegendManager();
                Integer newItemIndex = legendManager.getCurrentIndex();

                if (chosenLegendCustomBuilder.isAvailableToBuild()) {
                    Item item = chosenLegend.createCustomItem(newItemIndex, null, null, chosenLegendCustomBuilder);

                    // adding item to legend manager
                    legendController.addItemToLegendManager(item);

                    // the user must be notified that the legend was actually added. Hence, update the legend layers panel.
                    refreshLayers();
                } else {
                    JOptionPane.showMessageDialog(this, chosenLegendCustomBuilder.stepsNeededToBuild(), NbBundle.getMessage(LegendManager.class, "LegendItem.stepsNeededToBuildItem"), JOptionPane.INFORMATION_MESSAGE, null);
                }
            }
        }
    }//GEN-LAST:event_addLegendButtonActionPerformed

    private JTable getLegendLayerModel() {
        // This method fetches the names all the active legends, arranges them in the form of a table and returns the table.
        // The names of the legends can be changed by double-clicking on the legend layer, editing it and pressing return.

        /* Reason to have a JTable:
         * This is a step towards extendibility. The columns can be added as and when there is a requirement.
         * (For example, you can have a button directly to perform a particular action for the corresponding legend layer.)
         * Right now, we have 1 column, for the user defined name.
         * When an layer is clicked, we can easily figure out what was clicked by finding out the row and column, instead of having callbacks registered for each element.
         */

        LegendManager legendManager = legendController.getLegendManager();
        ArrayList<Item> items = legendManager.getLegendItems(); // fetches all the active legend items.

        int numberOfActiveItems = legendManager.getNumberOfActiveItems();
        // column names should be specified
        String[] columnNames = {"Legends"};

        // data in the rows must be constructed
        Object[][] rowData = new Object[numberOfActiveItems][];
        for (int i = 0; i < numberOfActiveItems; i++) {
            PreviewProperty[] props = (PreviewProperty[]) items.get(i).getData(LegendItem.PROPERTIES);
            PreviewProperty name = props[LegendProperty.USER_LEGEND_NAME];

            rowData[numberOfActiveItems - i - 1] = new Object[columnNames.length];
            rowData[numberOfActiveItems - i - 1][0] = name.getValue();
        }
        // build a table model with the rows and column names. construct a table with the table model.
        DefaultTableModel legendLayerModel = new DefaultTableModel(rowData, columnNames);
        JTable layerOrderTemp = new JTable(legendLayerModel);

        // now that the table is built, we need to add some event listeners in order to make the table interactive.

        // onclick listener will reset the index of the activeLegend whenever a row is clicked
        layerOrderTemp.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // get the row and column of the clicked cell
                int row = layerOrder.rowAtPoint(e.getPoint()); // returns -1 if clicked outside the table. Hence this condition has to be checked in the code further.
                int col = layerOrder.columnAtPoint(e.getPoint());

                if (row >= 0) {
                    LegendManager legendManager = legendController.getLegendManager();
                    ArrayList<Item> items = legendManager.getLegendItems();

                    Item activeItem = items.get(legendManager.getNumberOfActiveItems() - 1 - row); // the item that appears at the bottom of the legend layer panel is the first item in the list of legend Items in the legendManager.
                    Integer itemIndex = (Integer) activeItem.getData(LegendItem.ITEM_INDEX); // Item index of a legend will identify the legend uniquely. Note that it need not necessarily be the index of the item in the list of legends (the items are swapped when layers are re-ordered).
                    legendManager.setActiveLegend(legendManager.getIndexFromItemIndex(itemIndex)); // to set the activeLegend: we need to find out the index of the active item in the legendItems list, and set it as the activeLegend.
                }
            }
        });

        // if the text in a cell has changed, it means that the user has given a different name to the layer. hence the 'user_legend_name' property must be changed to its value.
        layerOrderTemp.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("tableCellEditor".equals(e.getPropertyName())) {
                    if (!layerOrder.isEditing()) {
                        // if the event is fired and the isEditing flag is unset, it means that the editing of the cell is complete.
                        LegendManager legendManager = legendController.getLegendManager();
                        ArrayList<Item> items = legendManager.getLegendItems();

                        // the editing can be done only on the activelegend, In order to update the layer data, we need to find out the row and the column
                        int row = layerOrder.convertRowIndexToModel(layerOrder.getEditingRow());
                        int col = layerOrder.convertColumnIndexToModel(layerOrder.getEditingColumn());

                        // fetch the active item and get the reference for its user defined name attribute.
                        Item activeItem = items.get(legendManager.getActiveLegend());
                        PreviewProperty[] props = (PreviewProperty[]) activeItem.getData(LegendItem.PROPERTIES);
                        PreviewProperty name = props[LegendProperty.USER_LEGEND_NAME];

                        // set the user defined name attribute to the updated cell value
                        name.setValue(layerOrder.getModel().getValueAt(row, col));
                    }
                }
            }
        });

        return layerOrderTemp;
    }

    private void refreshLayers() {
        // this method will update the legend layers and fill the legend layers panel with the user defined names of all the legends.
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();

        // check if previewModel exists
        if (previewModel == null) {
            return;
        }

        LegendManager legendManager = legendController.getLegendManager();

        // remove the existing JTable
        legendLayers.removeAll();

        // get the JTable with all the filled values and the listeners.
        layerOrder = getLegendLayerModel();

        // add the JTable, repaint and update the UI
        legendLayers.add(layerOrder, BorderLayout.CENTER);
        legendLayers.repaint();
        legendLayers.updateUI();

        // after updating the UI, we need to indicate to the user about the legend that is currently active. hence, we select the row that contains the active legend.
        if (legendManager.getActiveLegend() != -1) {
            int activeLegendPosition = legendManager.getNumberOfActiveItems() - 1 - legendManager.getPositionFromActiveLegendIndex();
            layerOrder.setRowSelectionInterval(activeLegendPosition, activeLegendPosition);
        }
    }

    private void removeLegendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeLegendButtonActionPerformed
        // This method is executed when the user clicks on the remove button. It removes the legend that is currently active.

        LegendManager legendManager = legendController.getLegendManager();
        int indexOfActiveLegend = legendManager.getActiveLegend();
        if (indexOfActiveLegend == -1) {
            //if index is -1, it means that that there are no active items
            JOptionPane.showMessageDialog(this, "There are no legends to be removed.");
        } else {
            // confirm removal
            int dialogResult = JOptionPane.showConfirmDialog(this, "Delete this legend?", "confirm removal", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                legendManager.removeItem(indexOfActiveLegend);
                refreshLayers();
            }
        }
    }//GEN-LAST:event_removeLegendButtonActionPerformed

    private void moveUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveUpActionPerformed
        // This method is executed when the user clicks on the move up button.

        LegendManager legendManager = legendController.getLegendManager();
        // moving a layer up is same as swapping the active legend with the next legend. Hence, find the indices for swapping.
        int activeLegendIndex = legendManager.getActiveLegend();
        int nextActiveLegend = legendManager.getNextActiveLegend(); // getNextActiveLegend() returns -1 if the active Legend is already at the top and cannot be moved further up.

        if (nextActiveLegend != -1) {
            legendManager.swapItems(activeLegendIndex, nextActiveLegend);
            legendManager.setActiveLegend(nextActiveLegend);
        }

        refreshLayers();
    }//GEN-LAST:event_moveUpActionPerformed

    private void moveDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveDownActionPerformed
        // This method is executed when the user clicks on the move down button.

        LegendManager legendManager = legendController.getLegendManager();

        // moving a layer down is same as swapping the active legend with the previous legend. Hence, find the indices for swapping.
        int activeLegendIndex = legendManager.getActiveLegend();
        int previousActiveLegend = legendManager.getPreviousActiveLegend(); // getPreviousActiveLegend() returns -1 if the active Legend is already at the bottom and cannot be moved further down.

        if (previousActiveLegend != -1) {
            legendManager.swapItems(activeLegendIndex, previousActiveLegend);
            legendManager.setActiveLegend(previousActiveLegend);
        }

        refreshLayers();
    }//GEN-LAST:event_moveDownActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addLegendButton;
    private javax.swing.JPanel controls;
    private javax.swing.JPanel legendLayers;
    private javax.swing.JPanel legendManagerPanel;
    private javax.swing.JButton moveDown;
    private javax.swing.JPanel moveLayers;
    private javax.swing.JButton moveUp;
    private javax.swing.JButton removeLegendButton;
    // End of variables declaration//GEN-END:variables
    private JTable layerOrder;

    @Override
    public void setup(PreviewModel previewModel) {
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void unsetup() {
    }

    @Override
    public Icon getIcon() {
        return new ImageIcon();
    }

    @Override
    public String getPanelTitle() {
        return NbBundle.getMessage(LegendManagerUI.class, "LegendManagerUI.title");
    }
}
