package org.gephi.legend.plugin.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.gephi.legend.api.AbstractItem;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.spi.LegendItem;
import static org.gephi.legend.spi.LegendItem.PROPERTIES;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;

/**
 * the item model for the table legend.
 *
 * The table item contains a 2-d arraylist of cells (see Cell.java), which is
 * logically equivalent to a table. It also has flags to indicate that the
 * structure of the table has changed. This is required by the table item
 * renderer to determine if the table must be re-built and re-drawn. The custom
 * table item builder has a method that gathers the required information and
 * populates the table item with the 2-d arraylist of cells.
 *
 * @author mvvijesh, edubecks
 */
public class TableItem extends AbstractItem implements LegendItem, Item.BoundingBoxProvidingItem {

    public static final String LEGEND_TYPE = "Table Item";
    public static final String CELL_PROPERTIES = "Cell Properties";
    private ArrayList<ArrayList<Cell>> table;
    // flags to indicate if the structure of a table has changed
    private Boolean rowChanged = true;
    private Boolean columnChanged = true;
    private Boolean ipeditorChanged = true;

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

    /**
     * add a new row to the table
     *
     * @param pos - index at which the row must be added
     * @param backgroundColor - background color of each cells in the row
     * @param borderColor - border color of each cells in the row
     * @param cellFont
     * @param cellAlignment
     * @param cellFontColor
     * @param cellTextContent
     * @param cellShapeShape
     * @param cellShapeColor
     * @param cellShapeValue
     * @param cellImageFile
     * @param cellImageIsScaling
     * @param cellType - cell type can be TYPE_TEXT, TYPE_IMAGE or a TYPE_SHAPE
     * (see Cell.java)
     */
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
        rowChanged = true;
    }

    /**
     * delete a row from the table
     *
     * @param pos - position of the row to be deleted
     */
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
        rowChanged = true;
    }

    /**
     * add a new column to the table.
     *
     * @param columnNumber - index at which the column should be added
     * @param backgroundColor - background color of all the newly added cells
     * @param borderColor - border color of all the newly added cells
     * @param cellFont
     * @param cellAlignment
     * @param cellFontColor
     * @param cellContent
     * @param cellShapeShape
     * @param cellShapeColor
     * @param cellShapeValue
     * @param cellImageFile
     * @param cellImageIsScaling
     * @param cellType - cell type can be TYPE_TEXT, TYPE_IMAGE or a TYPE_SHAPE
     * (see Cell.java)
     */
    public void addColumn(int columnNumber, Color backgroundColor, Color borderColor, Font cellFont, Alignment cellAlignment, Color cellFontColor, String cellContent, Shape cellShapeShape, Color cellShapeColor, Float cellShapeValue, File cellImageFile, Boolean cellImageIsScaling, int cellType) {
        int numberOfRows = table.size();
        for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
            // the item's own properties are updated during creation of the cell
            Cell cell = new Cell(this, rowNumber, columnNumber, backgroundColor, borderColor, cellFont, cellAlignment, cellFontColor, cellContent, cellShapeShape, cellShapeColor, cellShapeValue, cellImageFile, cellImageIsScaling, cellType);
            table.get(rowNumber).add(columnNumber, cell);
        }

        // indicate that the table structure has changed
        columnChanged = true;
    }

    /**
     * delete a column from the table
     *
     * @param pos - position of the column to be deleted
     */
    public void deleteColumn(int pos) {
        int numberOfColums = table.get(0).size(); // there will be atleast one row, always

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
        columnChanged = true;
    }

    /**
     * checks if the structure of the table has changed
     *
     * @return True if a row, column or inplace Editor is modified. False,
     * otherwise.
     */
    public Boolean getStructureChanged() {
        return rowChanged || columnChanged || ipeditorChanged;
    }

    public void setRowChanged(Boolean change) {
        rowChanged = change;
    }

    public Boolean getColumnChanged() {
        return columnChanged;
    }

    public void setColumnChanged(Boolean change) {
        columnChanged = change;
    }

    public void setInplaceEditorChanged(Boolean change) {
        ipeditorChanged = change;
    }

    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[LegendProperty.LABEL].getValue());
    }

    /**
     *
     * @return a rectangle that acts as a bounding box for the table legend.
     */
    @Override
    public Rectangle getBoundingBox() {
        PreviewProperty[] ownProperties = this.getData(PROPERTIES);
        float originX = ownProperties[LegendProperty.USER_ORIGIN_X].getValue();
        float originY = ownProperties[LegendProperty.USER_ORIGIN_Y].getValue();
        float width = ownProperties[LegendProperty.WIDTH].getValue();
        float height = ownProperties[LegendProperty.HEIGHT].getValue();
        return new Rectangle((int) originX, (int) originY, (int) width, (int) height);
    }
}
