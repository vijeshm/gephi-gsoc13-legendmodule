/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.table;

import com.bric.swing.ColorPicker;
import com.connectina.swing.fontchooser.JFontChooser;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.AbstractLegendItemRenderer;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.blockNode;
import org.gephi.legend.inplaceeditor.column;
import org.gephi.legend.inplaceeditor.element;
import org.gephi.legend.inplaceeditor.inplaceClickResponse;
import org.gephi.legend.inplaceeditor.inplaceEditor;
import org.gephi.legend.inplaceeditor.inplaceItemBuilder;
import org.gephi.legend.inplaceeditor.row;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.netbeans.swing.tabcontrol.event.TabActionEvent;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author mvvijesh, edubecks
 */
@ServiceProvider(service = Renderer.class, position = 501)
public class TableItemRenderer extends AbstractLegendItemRenderer {

    public static String TABLENODE = "table node";
    public static String CELLNODE = "cell node";
    public static String CELLNODE_ROW_NUMBER = "cell node row number";
    public static String CELLNODE_COL_NUMBER = "cell node column number";
    // OWN PROPERTIES - refine the variable name to have semantic
    private Font tableFont;
    private Color tableFontColor;
    private Alignment tableFontAlignment;
    private int tableCellSpacing;
    private int tableCellPadding;
    private int tableCellBorderSize;
    private Color tableCellBorderColor;
    private int tableNumberOfRows;
    private int tableNumberOfColumns;
    private ArrayList<ArrayList<Cell>> table;
    private float colWidthTolerance = 0.1f;
    private int numberOfTableProperties = TableProperty.OWN_PROPERTIES.length;
    private int numberOfCellProperties = Cell.OWN_PROPERTIES.length;

    @Override
    public boolean isAnAvailableRenderer(Item item) {
        return item instanceof TableItem;
    }

    @Override
    protected void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        tableFont = properties.getFontValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_FONT));
        tableFontColor = properties.getColorValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_FONT_COLOR));
        tableFontAlignment = properties.getValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_FONT_ALIGNMENT));
        tableCellSpacing = properties.getIntValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_CELL_SPACING));
        tableCellPadding = properties.getIntValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_CELL_PADDING));
        tableCellBorderSize = properties.getIntValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_BORDER_SIZE));
        tableCellBorderColor = properties.getColorValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_BORDER_COLOR));
        tableNumberOfRows = ((TableItem) item).getNumberOfRows();
        tableNumberOfColumns = ((TableItem) item).getNumberOfColumns();

        table = ((TableItem) item).getTable();
    }

    @Override
    protected void renderToGraphics(Graphics2D graphics2D, blockNode legendNode) {
        int blockOriginX = (int) (legendNode.getOriginX());
        int blockOriginY = (int) (legendNode.getOriginY());
        int blockWidth = (int) legendNode.getBlockWidth();
        int blockHeight = (int) legendNode.getBlockHeight();

        int numberOfRows = table.size();
        int numberOfColumns = table.get(0).size();
        int maxColWidth = (blockWidth - (numberOfColumns + 1) * tableCellSpacing - 2 * numberOfColumns * tableCellPadding - 2 * numberOfColumns * tableCellBorderSize) / numberOfColumns;
        int rowHeight = (blockHeight - (numberOfRows + 1) * tableCellSpacing - 2 * numberOfRows * tableCellPadding - 2 * numberOfRows * tableCellBorderSize) / numberOfRows;

        // determine all the column widths
        int[] colWidths = new int[numberOfColumns];
        int stringWidth;
        FontMetrics fontMetrics;
        PreviewProperty[] previewProperties = null;
        Font cellFont = null;
        String cellContent = null;
        for (int col = 0; col < numberOfColumns; col++) {
            int tempMaxColWidth = 0;
            for (int row = 0; row < numberOfRows; row++) {
                previewProperties = table.get(row).get(col).getPreviewProperties();
                cellFont = (Font) previewProperties[Cell.CELL_FONT].getValue();
                cellContent = (String) previewProperties[Cell.CELL_CONTENT].getValue();

                graphics2D.setFont(cellFont);
                fontMetrics = graphics2D.getFontMetrics(cellFont);
                stringWidth = fontMetrics.stringWidth(cellContent);
                if (stringWidth > tempMaxColWidth) {
                    tempMaxColWidth = stringWidth;
                }
            }

            tempMaxColWidth = (int) ((1 + colWidthTolerance) * tempMaxColWidth);
            // the column width should be exactly the same as the width of the longest string in the column, since it causes rendering problems.
            if (tempMaxColWidth > maxColWidth) {
                colWidths[col] = maxColWidth;
            } else {
                colWidths[col] = tempMaxColWidth;
            }
        }

        int sumOfWidths = 0;
        for (int col = 0; col < numberOfColumns; col++) {
            sumOfWidths += colWidths[col];
        }

        // create a table node, create a corresponding inplace editor and attach it as the legendNode's child
        int tableWidth = sumOfWidths + (numberOfColumns + 1) * tableCellSpacing + 2 * numberOfColumns * tableCellPadding + 2 * numberOfColumns * tableCellBorderSize;
        int tableHeight = blockHeight;
        int tableOriginX = blockOriginX + blockWidth / 2 - tableWidth / 2;
        int tableOriginY = blockOriginY + blockHeight / 2 - tableHeight / 2; //as of now, tableOriginY is same as blockOriginY
        Item item = legendNode.getItem();
        blockNode tableNode = legendNode.getChild(TABLENODE);
        if (tableNode == null) {
            tableNode = legendNode.addChild(tableOriginX, tableOriginY, tableWidth, tableHeight, TABLENODE);
            // the table, by itself doesnt have an inplace editor.
            // The table properties are taken by the cell properties.
            // Hence, there is no need to build a table inplace editor.
        }

        // update the geometry and draw the geometric dimensions - this is redundant only when the first time the legend it created.
        tableNode.updateGeometry(tableOriginX, tableOriginY, tableWidth, tableHeight);
        drawBlockBoundary(graphics2D, tableNode);

        // cells are re-constructed everytime the table item is rendered
        // since cells are built every single time, there is no need to update the geometry
        buildCellNodes(tableNode, item, rowHeight, colWidths);

        // render the cells
        renderCells(graphics2D, tableNode, (TableItem) item);
    }

    private void buildCellNodes(blockNode tableNode, Item item, int rowHeight, int[] colWidths) {
        // the legend model will still contain the reference to the old inplace editor, not the updated one. Hence, update it.
        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        inplaceEditor currentInplaceEditor = legendModel.getInplaceEditor();
        Integer activeCellRow = null;
        Integer activeCellColumn = null;
        // if the active inplace editor belongs to a cell blocknode, then store its row and column.
        // the row and column are later on used to reset the legend model's inplace editor
        if (currentInplaceEditor != null) {
            blockNode currentBlockNode = currentInplaceEditor.getData(inplaceEditor.BLOCKNODE);
            if (currentBlockNode.getTag().equals(CELLNODE)) {
                activeCellRow = currentBlockNode.getData(CELLNODE_ROW_NUMBER);
                activeCellColumn = currentBlockNode.getData(CELLNODE_COL_NUMBER);
            }
        }

        //remove all the cell nodes and rebuild them
        tableNode.removeAllChildren();

        //associate inplace editors with the cellNodes
        Graph graph = null;
        inplaceItemBuilder ipbuilder = Lookup.getDefault().lookup(inplaceItemBuilder.class);
        PreviewProperty[] cellPreviewProperties = null;
        PreviewProperty[] tablePreviewProperties = item.getData(LegendItem.OWN_PROPERTIES);
        Cell cell = null;
        row r;
        column col;
        int itemIndex = item.getData(LegendItem.ITEM_INDEX);

        int tableOriginX = (int) tableNode.getOriginX();
        int tableOriginY = (int) tableNode.getOriginY();
        int tableWidth = (int) tableNode.getBlockWidth();
        int tableHeight = (int) tableNode.getBlockHeight();

        //precompute the relative positions of the columns from the table legend's origin.
        int[] columnOrigins = new int[colWidths.length];
        columnOrigins[0] = 0;
        int sum = 0;
        for (int i = 0; i < colWidths.length; i++) {
            columnOrigins[i] = sum;
            sum += colWidths[i];
        }

        //create blocknodes for cells and associate inplace editors with them.
        for (int rowNumber = 0; rowNumber < tableNumberOfRows; rowNumber++) {
            for (int colNumber = 0; colNumber < tableNumberOfColumns; colNumber++) {
                int tableCellOriginX = tableOriginX + columnOrigins[colNumber] + (colNumber + 1) * tableCellSpacing + (2 * colNumber + 1) * tableCellPadding + (2 * colNumber + 1) * tableCellBorderSize;
                int tableCellOriginY = tableOriginY + rowNumber * rowHeight + (rowNumber + 1) * tableCellSpacing + (2 * rowNumber + 1) * tableCellPadding + (2 * rowNumber + 1) * tableCellBorderSize;
                int tableCellWidth = colWidths[colNumber];
                int tableCellHeight = rowHeight;

                blockNode cellNode = tableNode.addChild(tableCellOriginX, tableCellOriginY, tableCellWidth, tableCellHeight, CELLNODE);
                // setting optional data - to identify which cell this
                // this can be used to reset the inplace editor in the legend model
                cellNode.setData(CELLNODE_ROW_NUMBER, rowNumber);
                cellNode.setData(CELLNODE_COL_NUMBER, colNumber);

                cell = table.get(rowNumber).get(colNumber);
                cellPreviewProperties = cell.getPreviewProperties();

                inplaceEditor ipeditor = ipbuilder.createInplaceEditor(graph, cellNode);
                ipeditor.setData(inplaceEditor.BLOCK_INPLACEEDITOR_GAP, (float) (TRANSFORMATION_ANCHOR_SIZE * 3.0 / 4.0));

                // modify inplace editors
                r = ipeditor.addRow();
                col = r.addColumn();
                Object[] data = new Object[1];
                data[0] = "Table:";
                col.addElement(element.ELEMENT_TYPE.LABEL, itemIndex, null, data);

                // cell spacing
                r = ipeditor.addRow();
                col = r.addColumn();
                data = new Object[3];
                // to display a static image, set data[0] to true or false, data[1] and data[2] to same image.
                data[0] = true;
                data[1] = "/org/gephi/legend/graphics/cell_spacing.png";
                data[2] = "/org/gephi/legend/graphics/cell_spacing.png";
                col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, null, data);

                col = r.addColumn();
                data = new Object[0];
                col.addElement(element.ELEMENT_TYPE.NUMBER, itemIndex, tablePreviewProperties[TableProperty.TABLE_CELL_SPACING], data);

                // cell padding
                col = r.addColumn();
                data = new Object[3];
                data[0] = true;
                data[1] = "/org/gephi/legend/graphics/cell_padding.png";
                data[2] = "/org/gephi/legend/graphics/cell_padding.png";
                col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, null, data);

                col = r.addColumn();
                data = new Object[0];
                col.addElement(element.ELEMENT_TYPE.NUMBER, itemIndex, tablePreviewProperties[TableProperty.TABLE_CELL_PADDING], data);

                // border size
                col = r.addColumn();
                data = new Object[3];
                data[0] = true;
                data[1] = "/org/gephi/legend/graphics/cell_border.png";
                data[2] = "/org/gephi/legend/graphics/cell_border.png";
                col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, null, data);

                col = r.addColumn();
                data = new Object[0];
                col.addElement(element.ELEMENT_TYPE.NUMBER, itemIndex, tablePreviewProperties[TableProperty.TABLE_BORDER_SIZE], data);

                r = ipeditor.addRow();
                // border color
                col = r.addColumn();
                data = new Object[2];
                data[0] = new inplaceClickResponse() {
                    @Override
                    public void performAction(inplaceEditor ipeditor) {
                        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to change the border color of all cells?", "Confirm Border Color Change", JOptionPane.YES_NO_OPTION);
                        if (confirmation == JOptionPane.YES_NO_OPTION) {
                            blockNode cellNode = ipeditor.getData(inplaceEditor.BLOCKNODE);
                            TableItem tableItem = (TableItem) cellNode.getItem();
                            PreviewProperty[] tablePreviewProperties = tableItem.getData(TableItem.OWN_PROPERTIES);
                            Color tableCellBorderColor = tablePreviewProperties[TableProperty.TABLE_BORDER_COLOR].getValue();
                            Color selectedColor = ColorPicker.showDialog(null, tableCellBorderColor, true);
                            if (selectedColor != null) {
                                ArrayList<ArrayList<Cell>> table = tableItem.getTable();
                                tablePreviewProperties[TableProperty.TABLE_BORDER_COLOR].setValue(selectedColor);
                                PreviewProperty[] cellPreviewProperties = null;
                                int numberOfRows = tableItem.getNumberOfRows();
                                int numberOfCols = tableItem.getNumberOfColumns();
                                for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
                                    for (int colNumber = 0; colNumber < numberOfCols; colNumber++) {
                                        Cell cell = table.get(rowNumber).get(colNumber);
                                        cellPreviewProperties = cell.getPreviewProperties();
                                        cellPreviewProperties[Cell.BORDER_COLOR].setValue(selectedColor);
                                    }
                                }
                            }
                        }
                    }
                };
                data[1] = "/org/gephi/legend/graphics/table_cell_border_color.png";
                col.addElement(element.ELEMENT_TYPE.FUNCTION, itemIndex, null, data);

                // table font
                col = r.addColumn();
                data = new Object[2];
                data[0] = new inplaceClickResponse() {
                    @Override
                    public void performAction(inplaceEditor ipeditor) {
                        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to change the font for all cells?", "Confirm Font Change", JOptionPane.YES_NO_OPTION);
                        if (confirmation == JOptionPane.YES_NO_OPTION) {
                            blockNode cellNode = ipeditor.getData(inplaceEditor.BLOCKNODE);
                            TableItem tableItem = (TableItem) cellNode.getItem();
                            PreviewProperty[] tablePreviewProperties = tableItem.getData(TableItem.OWN_PROPERTIES);
                            Font tableCellFont = tablePreviewProperties[TableProperty.TABLE_FONT].getValue();
                            JFontChooser chooser = new JFontChooser(tableCellFont);
                            Font chosenFont = chooser.showDialog(new JFrame("choose a font"), tableCellFont);
                            if (chosenFont != null) {
                                ArrayList<ArrayList<Cell>> table = tableItem.getTable();
                                tablePreviewProperties[TableProperty.TABLE_FONT].setValue(chosenFont);
                                PreviewProperty[] cellPreviewProperties = null;
                                int numberOfRows = tableItem.getNumberOfRows();
                                int numberOfCols = tableItem.getNumberOfColumns();
                                for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
                                    for (int colNumber = 0; colNumber < numberOfCols; colNumber++) {
                                        Cell cell = table.get(rowNumber).get(colNumber);
                                        cellPreviewProperties = cell.getPreviewProperties();
                                        cellPreviewProperties[Cell.CELL_FONT].setValue(chosenFont);
                                    }
                                }
                            }
                        }
                    }
                };
                data[1] = "/org/gephi/legend/graphics/table_font.png";
                col.addElement(element.ELEMENT_TYPE.FUNCTION, itemIndex, null, data);

                // table font color
                col = r.addColumn();
                data = new Object[2];
                data[0] = new inplaceClickResponse() {
                    @Override
                    public void performAction(inplaceEditor ipeditor) {
                        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to change the font color for all cells?", "Confirm Font Color Change", JOptionPane.YES_NO_OPTION);
                        if (confirmation == JOptionPane.YES_NO_OPTION) {
                            blockNode cellNode = ipeditor.getData(inplaceEditor.BLOCKNODE);
                            TableItem tableItem = (TableItem) cellNode.getItem();
                            PreviewProperty[] tablePreviewProperties = tableItem.getData(TableItem.OWN_PROPERTIES);
                            Color tableCellFontColor = tablePreviewProperties[TableProperty.TABLE_FONT_COLOR].getValue();
                            Color selectedColor = ColorPicker.showDialog(null, tableCellFontColor, true);
                            if (selectedColor != null) {
                                ArrayList<ArrayList<Cell>> table = tableItem.getTable();
                                tablePreviewProperties[TableProperty.TABLE_FONT_COLOR].setValue(selectedColor);
                                PreviewProperty[] cellPreviewProperties = null;
                                int numberOfRows = tableItem.getNumberOfRows();
                                int numberOfCols = tableItem.getNumberOfColumns();
                                for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
                                    for (int colNumber = 0; colNumber < numberOfCols; colNumber++) {
                                        Cell cell = table.get(rowNumber).get(colNumber);
                                        cellPreviewProperties = cell.getPreviewProperties();
                                        cellPreviewProperties[Cell.CELL_FONT_COLOR].setValue(selectedColor);
                                    }
                                }
                            }
                        }
                    }
                };
                data[1] = "/org/gephi/legend/graphics/table_font_color.png";
                col.addElement(element.ELEMENT_TYPE.FUNCTION, itemIndex, null, data);
                
                r = ipeditor.addRow();
                // insert_row button
                col = r.addColumn();
                data = new Object[2];
                data[0] = new inplaceClickResponse() {
                    @Override
                    public void performAction(inplaceEditor ipeditor) {
                        int confirmation = JOptionPane.showConfirmDialog(null, "Do you really want to add a row?", "Confirm Row Addition", JOptionPane.YES_NO_OPTION);
                        if (confirmation == JOptionPane.YES_OPTION) {
                            blockNode cellNode = ipeditor.getData(inplaceEditor.BLOCKNODE);
                            TableItem tableItem = (TableItem) cellNode.getItem();
                            int cellRowNumber = cellNode.getData(CELLNODE_ROW_NUMBER);
                            tableItem.addRow(cellRowNumber, Cell.backgroundColor, Cell.borderColor, Cell.cellFont, Cell.cellAlignment, Cell.cellFontColor, Cell.cellContent);
                        }
                    }
                };
                data[1] = "/org/gephi/legend/graphics/insert_row.png";
                col.addElement(element.ELEMENT_TYPE.FUNCTION, itemIndex, null, data);

                // insert_column button
                col = r.addColumn();
                data = new Object[2];
                data[0] = new inplaceClickResponse() {
                    @Override
                    public void performAction(inplaceEditor ipeditor) {
                        int confirmation = JOptionPane.showConfirmDialog(null, "Do you really want to add a column?", "Confirm Column Addition", JOptionPane.YES_NO_OPTION);
                        if (confirmation == JOptionPane.YES_OPTION) {
                            blockNode cellNode = ipeditor.getData(inplaceEditor.BLOCKNODE);
                            TableItem tableItem = (TableItem) cellNode.getItem();
                            int cellColNumber = cellNode.getData(CELLNODE_COL_NUMBER);
                            tableItem.addColumn(cellColNumber, Cell.backgroundColor, Cell.borderColor, Cell.cellFont, Cell.cellAlignment, Cell.cellFontColor, Cell.cellContent);
                        }
                    }
                };
                data[1] = "/org/gephi/legend/graphics/insert_column.png";
                col.addElement(element.ELEMENT_TYPE.FUNCTION, itemIndex, null, data);

                // delete_row button
                col = r.addColumn();
                data = new Object[2];
                data[0] = new inplaceClickResponse() {
                    @Override
                    public void performAction(inplaceEditor ipeditor) {
                        int confirmation = JOptionPane.showConfirmDialog(null, "Do you really want to delete this row?", "Confirm Row Deletion", JOptionPane.YES_NO_OPTION);
                        if (confirmation == JOptionPane.YES_OPTION) {
                            blockNode cellNode = ipeditor.getData(inplaceEditor.BLOCKNODE);
                            TableItem tableItem = (TableItem) cellNode.getItem();
                            int cellRowNumber = cellNode.getData(CELLNODE_ROW_NUMBER);
                            tableItem.deleteRow(cellRowNumber);
                        }
                    }
                };
                data[1] = "/org/gephi/legend/graphics/delete_row.png";
                col.addElement(element.ELEMENT_TYPE.FUNCTION, itemIndex, null, data);

                // delete_column button
                col = r.addColumn();
                data = new Object[2];
                data[0] = new inplaceClickResponse() {
                    @Override
                    public void performAction(inplaceEditor ipeditor) {
                        int confirmation = JOptionPane.showConfirmDialog(null, "Do you really want to delete this column?", "Confirm Column Deletion", JOptionPane.YES_NO_OPTION);
                        if (confirmation == JOptionPane.YES_OPTION) {
                            blockNode cellNode = ipeditor.getData(inplaceEditor.BLOCKNODE);
                            TableItem tableItem = (TableItem) cellNode.getItem();
                            int cellColNumber = cellNode.getData(CELLNODE_COL_NUMBER);
                            tableItem.deleteColumn(cellColNumber);
                        }
                    }
                };
                data[1] = "/org/gephi/legend/graphics/delete_column.png";
                col.addElement(element.ELEMENT_TYPE.FUNCTION, itemIndex, null, data);

                // Cell Properties
                r = ipeditor.addRow();
                col = r.addColumn();
                data = new Object[1];
                data[0] = "Cell:";
                col.addElement(element.ELEMENT_TYPE.LABEL, itemIndex, null, data);

                col = r.addColumn();
                data = new Object[0]; // for a color property, extra data isnt needed.
                col.addElement(element.ELEMENT_TYPE.COLOR, itemIndex, cellPreviewProperties[Cell.BACKGROUND_COLOR], data);

                col = r.addColumn();
                data = new Object[0];
                col.addElement(element.ELEMENT_TYPE.COLOR, itemIndex, cellPreviewProperties[Cell.BORDER_COLOR], data);

                r = ipeditor.addRow();
                col = r.addColumn();
                data = new Object[0];
                col.addElement(element.ELEMENT_TYPE.TEXT, itemIndex, cellPreviewProperties[Cell.CELL_CONTENT], data);

                col = r.addColumn();
                data = new Object[0];
                col.addElement(element.ELEMENT_TYPE.COLOR, itemIndex, cellPreviewProperties[Cell.CELL_FONT_COLOR], data);

                col = r.addColumn();
                data = new Object[0];
                col.addElement(element.ELEMENT_TYPE.FONT, itemIndex, cellPreviewProperties[Cell.CELL_FONT], data);

                r = ipeditor.addRow();
                col = r.addColumn();
                // left-alignment
                data = new Object[4];
                data[0] = cellPreviewProperties[Cell.CELL_ALIGNMENT].getValue() == Alignment.LEFT;
                data[1] = "/org/gephi/legend/graphics/left_unselected.png";
                data[2] = "/org/gephi/legend/graphics/left_selected.png";
                data[3] = Alignment.LEFT;
                col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, cellPreviewProperties[Cell.CELL_ALIGNMENT], data);

                // center alignment
                data = new Object[4];
                data[0] = cellPreviewProperties[Cell.CELL_ALIGNMENT].getValue() == Alignment.CENTER;
                data[1] = "/org/gephi/legend/graphics/center_unselected.png";
                data[2] = "/org/gephi/legend/graphics/center_selected.png";
                data[3] = Alignment.CENTER;
                col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, cellPreviewProperties[Cell.CELL_ALIGNMENT], data);

                // right alignment
                data = new Object[4];
                data[0] = cellPreviewProperties[Cell.CELL_ALIGNMENT].getValue() == Alignment.RIGHT;
                data[1] = "/org/gephi/legend/graphics/right_unselected.png";
                data[2] = "/org/gephi/legend/graphics/right_selected.png";
                data[3] = Alignment.RIGHT;
                col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, cellPreviewProperties[Cell.CELL_ALIGNMENT], data);

                // justified
                data = new Object[4];
                data[0] = cellPreviewProperties[Cell.CELL_ALIGNMENT].getValue() == Alignment.JUSTIFIED;
                data[1] = "/org/gephi/legend/graphics/justified_unselected.png";
                data[2] = "/org/gephi/legend/graphics/justified_selected.png";
                data[3] = Alignment.JUSTIFIED;
                col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, cellPreviewProperties[Cell.CELL_ALIGNMENT], data);

                cellNode.setInplaceEditor(ipeditor);

                // reset the legend model's inplace editor
                if (activeCellRow != null && rowNumber == activeCellRow && activeCellColumn != null && colNumber == activeCellColumn) {
                    legendModel.setInplaceEditor(ipeditor);
                }
            }
        }
    }

    private void renderCells(Graphics2D graphics2D, blockNode tableNode, TableItem item) {
        Font saveFont = graphics2D.getFont();
        Color saveColor = graphics2D.getColor();

        ArrayList<blockNode> cellNodes = tableNode.getChildren();
        PreviewProperty[] previewProperties = null;
        // ArrayList<ArrayList<Cell>> table = item.getTable();
        blockNode cellNode;
        Cell cell;
        Color cellBackgroundColor;
        Color cellBorderColor;
        Font cellFont;
        Alignment cellAlignment;;
        Color cellFontColor;
        String cellContent;
        int cellOriginX;
        int cellOriginY;
        int cellWidth;
        int cellHeight;

        /*
         // CELLSPACING
         graphics2D.setColor(Color.RED);
         graphics2D.fillRect((int) (tableOriginX - currentRealOriginX), (int) (tableOriginY - currentRealOriginY), tableWidth, tableHeight);
         */

        for (int rowNumber = 0; rowNumber < tableNumberOfRows; rowNumber++) {
            for (int colNumber = 0; colNumber < tableNumberOfColumns; colNumber++) {
                cellNode = cellNodes.get(rowNumber * tableNumberOfColumns + colNumber); // the geometric information related to block node can be found here
                cell = table.get(rowNumber).get(colNumber); // the properties of the cell can be found here
                previewProperties = cell.getPreviewProperties();

                // cell geometry
                cellOriginX = (int) (cellNode.getOriginX() - currentRealOriginX);
                cellOriginY = (int) (cellNode.getOriginY() - currentRealOriginY);
                cellWidth = (int) cellNode.getBlockWidth();
                cellHeight = (int) cellNode.getBlockHeight();

                // cell properties
                cellBackgroundColor = (Color) previewProperties[Cell.BACKGROUND_COLOR].getValue(); // cell.getBackgroundColor();
                cellBorderColor = (Color) previewProperties[Cell.BORDER_COLOR].getValue(); // cell.getBorderColor();
                cellFont = (Font) previewProperties[Cell.CELL_FONT].getValue(); // cell.getCellFont();
                cellAlignment = (Alignment) previewProperties[Cell.CELL_ALIGNMENT].getValue(); // cell.getCellAlignment();
                cellFontColor = (Color) previewProperties[Cell.CELL_FONT_COLOR].getValue();// cell.getCellFontColor();
                cellContent = (String) previewProperties[Cell.CELL_CONTENT].getValue(); // cell.getCellContent();

                /*
                 // CELLPADDING
                 graphics2D.setColor(Color.GREEN);
                 graphics2D.fillRect(cellOriginX - tableCellPadding, cellOriginY - tableCellPadding, cellWidth + 2 * tableCellPadding, cellHeight + 2 * tableCellPadding);
                 */

                // BACKGROUND - render the background first, then go with the border
                graphics2D.setColor(cellBackgroundColor);
                graphics2D.fillRect(cellOriginX, cellOriginY, cellWidth, cellHeight);

                // BORDER - external border
                graphics2D.setColor(cellBorderColor);
                graphics2D.fillRect(cellOriginX - tableCellBorderSize, cellOriginY - tableCellBorderSize, cellWidth + 2 * tableCellBorderSize, tableCellBorderSize);  // top
                graphics2D.fillRect(cellOriginX - tableCellBorderSize, cellOriginY + cellHeight, cellWidth + 2 * tableCellBorderSize, tableCellBorderSize);   // bottom
                graphics2D.fillRect(cellOriginX - tableCellBorderSize, cellOriginY, tableCellBorderSize, cellHeight);
                graphics2D.fillRect(cellOriginX + cellWidth, cellOriginY, tableCellBorderSize, cellHeight); // left

                // TEXT
                legendDrawText(graphics2D, cellContent, cellFont, cellFontColor, cellOriginX, cellOriginY, cellWidth, cellHeight, cellAlignment);

                drawBlockBoundary(graphics2D, cellNode);
            }
        }

        graphics2D.setColor(saveColor);
        graphics2D.setFont(saveFont);
    }

    @Override // to be deprecated
    protected void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof TableItemBuilder;
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(TableItemRenderer.class, "TableItemRenderer.name");
    }
}
