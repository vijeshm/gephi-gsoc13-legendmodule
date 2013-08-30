/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.table;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.gephi.legend.api.AbstractItem;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.PreviewProperty;
import org.openide.util.NbBundle;

/**
 *
 * @author mvvijesh, edubecks
 */
public class TableItem extends AbstractItem implements LegendItem {

    public static final String LEGEND_TYPE = "Table Legend";
    public static final String CELL_PROPERTIES = "Cell Properties";
    // public static final String NUMBER_OF_ROWS = "Number of Rows";
    // public static final String NUMBER_OF_COLUMNS = "Number of Columns";
    private ArrayList<ArrayList<Cell>> table;
    private Boolean structureChanged;

    public TableItem(Object source) {
        super(source, LEGEND_TYPE);
        table = new ArrayList<ArrayList<Cell>>();
    }

    public ArrayList<ArrayList<Cell>> getTable() {
        return table;
    }

    public int getNumberOfRows() {
        return table.size();
    }

    public int getNumberOfColumns() {
        if (table.size() > 0) {
            return table.get(0).size();
        }
        return 0;
    }

    public void addRow(int pos, Color backgroundColor, Color borderColor, Font cellFont, Alignment cellAlignment, Color cellFontColor, String cellTextContent, Shape cellShapeShape, Color cellShapeColor, Float cellShapeValue, File cellImageFile, Boolean cellImageIsScaling, int cellType) {
        int numberOfRows = table.size();
        int numberOfColumns = 0;
        if (numberOfRows > 1) {
            numberOfColumns = table.get(0).size();
        }

        ArrayList<Cell> row = new ArrayList<Cell>();
        for (int colNumber = 0; colNumber < numberOfColumns; colNumber++) {
            // the item's own properties are updated during creation of the cell
            Cell cell = new Cell(this, pos, colNumber, backgroundColor, borderColor, cellFont, cellAlignment, cellFontColor, cellTextContent, cellShapeShape, cellShapeColor, cellShapeValue, cellImageFile, cellImageIsScaling, cellType);
            row.add(cell);
        }

        // add the row to the table
        table.add(pos, row);

        // indicate that the table structure has changed
        structureChanged = true;
    }

    public void deleteRow(int pos) {
        int numberOfRows = table.size();
        if (numberOfRows == 1) {
            JOptionPane.showConfirmDialog(null, "The table must contain atleast one row.", "Unable to delete row", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        table.remove(pos);

        // remove the currently active inplace editor (inplace editor of the deleted cell)
        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        legendModel.setInplaceEditor(null);

        // indicate that the table structure has changed
        structureChanged = true;
    }

    public void addColumn(int columnNumber, Color backgroundColor, Color borderColor, Font cellFont, Alignment cellAlignment, Color cellFontColor, String cellContent, Shape cellShapeShape, Color cellShapeColor, Float cellShapeValue, File cellImageFile, Boolean cellImageIsScaling, int cellType) {
        int numberOfRows = table.size();
        for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
            // the item's own properties are updated during creation of the cell
            Cell cell = new Cell(this, rowNumber, columnNumber, backgroundColor, borderColor, cellFont, cellAlignment, cellFontColor, cellContent, cellShapeShape, cellShapeColor, cellShapeValue, cellImageFile, cellImageIsScaling, cellType);
            table.get(rowNumber).add(columnNumber, cell);
        }

        // indicate that the table structure has changed
        structureChanged = true;
    }

    public void deleteColumn(int pos) {
        int numberOfColums = table.get(0).size(); //there will be atleast one row, always

        if (numberOfColums == 1) {
            JOptionPane.showConfirmDialog(null, "The table must contain atleast one column.", "Unable to delete column", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        int numberOfRows = table.size();
        for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
            table.get(rowNumber).remove(pos);
        }

        // remove the currently active inplace editor (inplace editor of the deleted cell)
        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        legendModel.setInplaceEditor(null);

        // indicate that the table structure has changed
        structureChanged = true;
    }

    public Boolean getStructureChanged() {
        return structureChanged;
    }

    public void setStructureChanged(Boolean change) {
        structureChanged = change;
    }

    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[LegendProperty.LABEL].getValue());
    }
}
