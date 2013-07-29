/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.table;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.gephi.legend.api.AbstractItem;
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

    public void addRow(int pos, Color backgroundColor, Color borderColor, Font cellFont, Alignment cellAlignment, Color cellFontColor, String cellContent) {
        int numberOfRows = table.size();
        int numberOfColumns = 0;
        if (numberOfRows > 1) {
            numberOfColumns = table.get(0).size();
        }

        ArrayList<Cell> row = new ArrayList<Cell>();
        for (int colNumber = 0; colNumber < numberOfColumns; colNumber++) {
            // the item's own properties are updated during creation of the cell
            Cell cell = new Cell(this, pos, colNumber, backgroundColor, borderColor, cellFont, cellAlignment, cellFontColor, cellContent);
            row.add(cell);
        }

        // add the row to the table
        table.add(pos, row);
    }

    public void deleteRow(int pos) {
        // remove the preview properties before deletion of the row
        int numberOfRows = table.size();
        int numberOfColumns = 0;
        if (numberOfRows > 1) {
            numberOfColumns = table.get(0).size();
        }

        PreviewProperty[] previewPropertiesList = (PreviewProperty[]) this.getData(LegendItem.OWN_PROPERTIES);
        ArrayList<PreviewProperty> previewProperties = new ArrayList<PreviewProperty>(Arrays.asList(previewPropertiesList));
        ArrayList<PreviewProperty> newPreviewProperties = new ArrayList<PreviewProperty>();
        for (int columnNumber = 0; columnNumber < numberOfColumns; columnNumber++) {
            /*
             // this list must be modified on the fly. Hence, use an iterator.
             for (Iterator<PreviewProperty> it = previewProperties.iterator(); it.hasNext(); ) {
             if (it.next().getDisplayName().equals("TableItem.cell." + rowNumber + "." + columnNumber)) {
             it.remove();
             }
             }
             */

            // create an empty array list and populate it with only the active properties
            for (PreviewProperty prop : previewProperties) {
                if (!prop.getDisplayName().startsWith("TableItem.cell." + pos + "." + columnNumber)) {
                    newPreviewProperties.add(prop);
                }
            }
        }
        this.setData(LegendItem.OWN_PROPERTIES, newPreviewProperties.toArray());

        table.remove(pos);
    }

    public void addColumn(int columnNumber, Color backgroundColor, Color borderColor, Font cellFont, Alignment cellAlignment, Color cellFontColor, String cellContent) {
        int numberOfRows = table.size();
        for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
            // the item's own properties are updated during creation of the cell
            Cell cell = new Cell(this, rowNumber, columnNumber, backgroundColor, borderColor, cellFont, cellAlignment, cellFontColor, cellContent);
            table.get(rowNumber).add(columnNumber, cell);
        }
    }

    public void deleteColumn(int pos) {
        int numberOfRows = table.size();
        for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
            table.get(rowNumber).remove(pos);
        }
    }

    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[LegendProperty.LABEL].getValue());
    }
}
