/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.table;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JOptionPane;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.NodeIterable;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Shape;
import org.gephi.preview.api.PreviewProperty;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author mvvijesh
 */
@ServiceProvider(service = CustomTableItemBuilder.class, position = 2)
public class DescriptionMode implements CustomTableItemBuilder {

    protected Font cellFont = Cell.cellFont;
    protected Color cellFontColor = Cell.cellFontColor;
    protected LegendItem.Alignment cellFontAlignment = Cell.cellAlignment;
    protected Color cellBackgroundColor = Cell.backgroundColor;
    protected Color cellBorderColor = Cell.borderColor;
    protected String cellTextContent = Cell.cellTextContent;
    protected Shape cellShapeShape = Cell.cellShapeShape;
    protected Color cellShapeColor = Cell.cellShapeColor;
    protected Float cellShapeValue = Cell.cellShapeValue;
    protected File cellImageFile = Cell.cellImageFile;
    protected Boolean cellImageIsScaling = Cell.cellImageIsScaling;
    protected int cellType = Cell.cellType;
    
    protected String propertyName = "Name";
    protected String propertyValue = "Value";
    protected String headerPropertyName = "Property";
    protected String headerPropertValue = "Value";

    @Override
    public void populateTable(TableItem tableItem) {
        String newValueString = (String) JOptionPane.showInputDialog(null, "Enter the number of properties:", "Number of Properties", JOptionPane.PLAIN_MESSAGE, null, null, "");
        Integer numberOfProperties;
        try {
            numberOfProperties = Integer.parseInt(newValueString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Entry. The number of properties is set to 1.", "Invalid Entry", JOptionPane.PLAIN_MESSAGE);
            numberOfProperties = 1;
        }

        if (numberOfProperties < 1) {
            JOptionPane.showMessageDialog(null, "The minimum number of nodes is 1.", "Invalid Entry", JOptionPane.PLAIN_MESSAGE);
            numberOfProperties = 1;
        }

        Integer tableNumberOfRows = numberOfProperties + 1;
        Integer tableNumberOfColumns = 2;

        String[] propertyNames = new String[numberOfProperties];
        String[] propertyValues = new String[numberOfProperties];
        
        for(int i = 0; i < numberOfProperties; i++) {
            propertyNames[i] = propertyName + " " + i + ":";
            propertyValues[i] = propertyValue + " " + i;
        }

        // build the default table and later customize according to the data
        for (int i = 0; i < tableNumberOfRows; i++) {
            tableItem.addRow(i, cellBackgroundColor, cellBorderColor, cellFont, cellFontAlignment, cellFontColor, cellTextContent, cellShapeShape, cellShapeColor, cellShapeValue, cellImageFile, cellImageIsScaling, cellType);
        }
        for (int i = 0; i < tableNumberOfColumns; i++) {
            tableItem.addColumn(i, cellBackgroundColor, cellBorderColor, cellFont, cellFontAlignment, cellFontColor, cellTextContent, cellShapeShape, cellShapeColor, cellShapeValue, cellImageFile, cellImageIsScaling, cellType);
        }

        ArrayList<ArrayList<Cell>> table = tableItem.getTable();

        Cell headerPropertyNameCell = table.get(0).get(0);
        PreviewProperty[] headerPropertyNameCellPreviewProp = headerPropertyNameCell.getPreviewProperties();
        headerPropertyNameCellPreviewProp[Cell.CELL_TEXT_CONTENT].setValue(headerPropertyName);
        headerPropertyNameCellPreviewProp[Cell.BACKGROUND_COLOR].setValue(new Color(0f, 0f, 0f, 0f));
        headerPropertyNameCellPreviewProp[Cell.BORDER_COLOR].setValue(new Color(0f, 0f, 0f, 0f));

        Cell headerPropertyValueCell = table.get(0).get(1);
        PreviewProperty[] headerPropertyValueCellPreviewProp = headerPropertyValueCell.getPreviewProperties();
        headerPropertyValueCellPreviewProp[Cell.CELL_TEXT_CONTENT].setValue(headerPropertValue);
        headerPropertyValueCellPreviewProp[Cell.BACKGROUND_COLOR].setValue(new Color(0f, 0f, 0f, 0f));
        headerPropertyValueCellPreviewProp[Cell.BORDER_COLOR].setValue(new Color(0f, 0f, 0f, 0f));

        Cell propertyNameCell;
        PreviewProperty[] propertyNameCellPreviewProp;
        Cell propertyValueCell;
        PreviewProperty[] propertyValueCellPreviewProp;
        for (int i = 0; i < numberOfProperties; i++) {
            propertyNameCell = table.get(i + 1).get(0);
            propertyNameCellPreviewProp = propertyNameCell.getPreviewProperties();
            propertyNameCellPreviewProp[Cell.CELL_TEXT_CONTENT].setValue(propertyNames[i]);

            propertyValueCell = table.get(i + 1).get(1);
            propertyValueCellPreviewProp = propertyValueCell.getPreviewProperties();
            propertyValueCellPreviewProp[Cell.CELL_TEXT_CONTENT].setValue(propertyValues[i]);
        }
    }

    @Override
    public String getDescription() {
        return "This mode is used to generate a description legend, a set of key-value pairs for properties and their values.";
    }

    @Override
    public String getTitle() {
        return "Description Legend: set of key-value pairs.";
    }

    @Override
    public boolean isAvailableToBuild() {
        return true;
    }

    @Override
    public String stepsNeededToBuild() {
        return NONE_NEEDED;
    }

    @Override
    public String toString() {
        return "Description Mode";
    }
}
