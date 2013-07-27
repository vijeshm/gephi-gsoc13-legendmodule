/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.AbstractLegendItemRenderer;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.blockNode;
import org.gephi.legend.inplaceeditor.column;
import org.gephi.legend.inplaceeditor.element;
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
        tableNumberOfRows = properties.getIntValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_NUMBER_OF_ROWS));
        tableNumberOfColumns = properties.getIntValue(LegendModel.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_NUMBER_OF_COLUMNS));

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
        for (int col = 0; col < numberOfColumns; col++) {
            int tempMaxColWidth = 0;
            for (int row = 0; row < numberOfRows; row++) {
                graphics2D.setFont(table.get(row).get(col).getCellFont());
                fontMetrics = graphics2D.getFontMetrics(table.get(row).get(col).getCellFont());
                stringWidth = fontMetrics.stringWidth(table.get(row).get(col).getCellContent());
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
        row r;
        column col;
        PreviewProperty[] previewProperties = item.getData(LegendItem.OWN_PROPERTIES);
        PreviewProperty prop;
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

                inplaceEditor ipeditor = ipbuilder.createInplaceEditor(graph, cellNode);
                ipeditor.setData(inplaceEditor.BLOCK_INPLACEEDITOR_GAP, (float) (TRANSFORMATION_ANCHOR_SIZE * 3.0 / 4.0));

                // modify inplace editors
                r = ipeditor.addRow();
                col = r.addColumn();
                Object[] data = new Object[1];
                data[0] = "Table:";
                col.addElement(element.ELEMENT_TYPE.LABEL, itemIndex, null, data);

                cellNode.setInplaceEditor(ipeditor);
                
                // reset the legend model's inplace editor
                if(activeCellRow != null && rowNumber == activeCellRow && activeCellColumn != null && colNumber == activeCellColumn) {
                    legendModel.setInplaceEditor(ipeditor);
                }
            }
        }
    }

    private void renderCells(Graphics2D graphics2D, blockNode tableNode, TableItem item) {
        Font saveFont = graphics2D.getFont();
        Color saveColor = graphics2D.getColor();

        ArrayList<blockNode> cellNodes = tableNode.getChildren();
        ArrayList<ArrayList<Cell>> table = item.getTable();
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

                // cell geometry
                cellOriginX = (int) (cellNode.getOriginX() - currentRealOriginX);
                cellOriginY = (int) (cellNode.getOriginY() - currentRealOriginY);
                cellWidth = (int) cellNode.getBlockWidth();
                cellHeight = (int) cellNode.getBlockHeight();

                // cell properties
                cellBackgroundColor = cell.getBackgroundColor();
                cellBorderColor = cell.getBorderColor();
                cellFont = cell.getCellFont();
                cellAlignment = cell.getCellAlignment();
                cellFontColor = cell.getCellFontColor();
                cellContent = cell.getCellContent();

                /*
                 // CELLPADDING
                 graphics2D.setColor(Color.GREEN);
                 graphics2D.fillRect(cellOriginX - tableCellPadding, cellOriginY - tableCellPadding, cellWidth + 2 * tableCellPadding, cellHeight + 2 * tableCellPadding);
                 */

                // BACKGROUND - render the background first, then go with the border
                graphics2D.setColor(cellBackgroundColor);
                graphics2D.fillRect(cellOriginX, cellOriginY, cellWidth, cellHeight);

                // BORDER - is internal and falls with the boundaries of the cell.
                graphics2D.setColor(cellBorderColor);
                graphics2D.fillRect(cellOriginX, cellOriginY, cellWidth, tableCellBorderSize);  // top
                graphics2D.fillRect(cellOriginX, cellOriginY + cellHeight - tableCellBorderSize, cellWidth, tableCellBorderSize);   // bottom
                graphics2D.fillRect(cellOriginX, cellOriginY + tableCellBorderSize, tableCellBorderSize, cellHeight - 2 * tableCellBorderSize);
                graphics2D.fillRect(cellOriginX + cellWidth - tableCellBorderSize, cellOriginY + tableCellBorderSize, tableCellBorderSize, cellHeight - 2 * tableCellBorderSize); // left

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
