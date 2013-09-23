package org.gephi.legend.plugin.table;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Shape;
import org.gephi.preview.api.PreviewProperty;
import org.openide.util.lookup.ServiceProvider;

/**
 * Description mode of a table contains two rows: first - property name, second
 * - property value.
 *
 * This mode is used to customize the table to have only two columns and a
 * user-specified number of rows. The first column contains a property name and
 * the second column contains the corresponding value. The data for the
 * property-value pairs can be fetched from an external data source if
 * necessary.
 *
 * @author mvvijesh
 */
@ServiceProvider(service = CustomTableItemBuilder.class, position = 2)
public class DescriptionMode implements CustomTableItemBuilder {

    protected Font cellFont = Cell.defaultCellFont;
    protected Color cellFontColor = Cell.defaultCellFontColor;
    protected LegendItem.Alignment cellFontAlignment = Cell.defaultCellAlignment;
    protected Color cellBackgroundColor = Cell.defaultBackgroundColor;
    protected Color cellBorderColor = Cell.defaultBorderColor;
    protected String cellTextContent = Cell.defaultCellTextContent;
    protected Shape cellShapeShape = Cell.defaultCellShapeShape;
    protected Color cellShapeColor = Cell.defaultCellShapeColor;
    protected Float cellShapeValue = Cell.defaultCellShapeValue;
    protected File cellImageFile = Cell.defaultCellImageFile;
    protected Boolean cellImageIsScaling = Cell.defaultCellImageIsScaling;
    protected int cellType = Cell.defaultCellType;
    protected String propertyName = "Name"; // dummy property name
    protected String propertyValue = "Value"; // dummy value
    protected String headerPropertyName = "Property"; // first column header
    protected String headerPropertyValue = "Value"; // second column header

    /**
     * builds the table item with a user-specified number of rows, two columns
     * and custom built style cells for headers.
     *
     * @param tableItem - the item being built
     */
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

        Integer tableNumberOfRows = numberOfProperties + 1; // the extra row is for the header
        Integer tableNumberOfColumns = 2;

        String[] propertyNames = new String[numberOfProperties];
        String[] propertyValues = new String[numberOfProperties];

        // populate propertyNames and propertyValues - this can be populated by the data from external sources as well
        for (int i = 0; i < numberOfProperties; i++) {
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

        // custom built style for property name in header
        Cell headerPropertyNameCell = table.get(0).get(0);
        PreviewProperty[] headerPropertyNameCellPreviewProp = headerPropertyNameCell.getPreviewProperties();
        headerPropertyNameCellPreviewProp[Cell.CELL_TEXT_CONTENT].setValue(headerPropertyName);
        headerPropertyNameCellPreviewProp[Cell.BACKGROUND_COLOR].setValue(new Color(0f, 0f, 0f, 0f));
        headerPropertyNameCellPreviewProp[Cell.BORDER_COLOR].setValue(new Color(0f, 0f, 0f, 0f));

        // custom built style for value in header
        Cell headerPropertyValueCell = table.get(0).get(1);
        PreviewProperty[] headerPropertyValueCellPreviewProp = headerPropertyValueCell.getPreviewProperties();
        headerPropertyValueCellPreviewProp[Cell.CELL_TEXT_CONTENT].setValue(headerPropertyValue);
        headerPropertyValueCellPreviewProp[Cell.BACKGROUND_COLOR].setValue(new Color(0f, 0f, 0f, 0f));
        headerPropertyValueCellPreviewProp[Cell.BORDER_COLOR].setValue(new Color(0f, 0f, 0f, 0f));

        // construct all the other rows
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