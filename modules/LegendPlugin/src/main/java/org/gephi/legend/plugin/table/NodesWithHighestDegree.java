/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.table;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.PreviewProperty;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author mvvijesh
 */
@ServiceProvider(service = CustomTableItemBuilder.class, position = 2)
public class NodesWithHighestDegree implements CustomTableItemBuilder {

    protected String cellContent = Cell.cellContent;
    protected Font cellFont = Cell.cellFont;
    protected Color cellFontColor = Cell.cellFontColor;
    protected LegendItem.Alignment cellFontAlignment = Cell.cellAlignment;
    protected Color cellBackgroundColor = Cell.backgroundColor;
    protected Color cellBorderColor = Cell.borderColor;

    @Override
    public void populateTable(TableItem tableItem) {
        String newValueString = (String) JOptionPane.showInputDialog(null, "Enter the top number of nodes:", "Number of Nodes", JOptionPane.PLAIN_MESSAGE, null, null, "");
        Integer numberOfNodes = Integer.parseInt(newValueString); // make sure that this number is atleast one
        Integer tableNumberOfRows = numberOfNodes + 1;
        Integer tableNumberOfColumns = 2;

        String[] nodeNames = new String[numberOfNodes];
        Integer[] nodeDegrees = new Integer[numberOfNodes];

        // create a random array of node degrees
        Random randomGenerator = new Random();
        for (int i = 0; i < numberOfNodes; i++) {
            nodeNames[i] = "Node " + (i + 1);
            nodeDegrees[i] = randomGenerator.nextInt(100);
        }

        // build the default table and later customize according to the data
        for (int i = 0; i < tableNumberOfRows; i++) {
            tableItem.addRow(i, cellBackgroundColor, cellBorderColor, cellFont, cellFontAlignment, cellFontColor, cellContent);
        }
        for (int i = 0; i < tableNumberOfColumns; i++) {
            tableItem.addColumn(i, cellBackgroundColor, cellBorderColor, cellFont, cellFontAlignment, cellFontColor, cellContent);
        }

        ArrayList<ArrayList<Cell>> table = tableItem.getTable();

        Cell headerNodeNameCell = table.get(0).get(0);
        PreviewProperty[] headerNodeNameCellPreviewProp = headerNodeNameCell.getPreviewProperties();
        headerNodeNameCellPreviewProp[Cell.CELL_CONTENT].setValue("Node");
        headerNodeNameCellPreviewProp[Cell.BACKGROUND_COLOR].setValue(new Color(0f, 0f, 0f, 0f));
        headerNodeNameCellPreviewProp[Cell.BORDER_COLOR].setValue(new Color(0f, 0f, 0f, 0f));

        Cell headerNodeDegreeCell = table.get(0).get(1);
        PreviewProperty[] headerNodeDegreeCellPreviewProp = headerNodeDegreeCell.getPreviewProperties();
        headerNodeDegreeCellPreviewProp[Cell.CELL_CONTENT].setValue("Degree");
        headerNodeDegreeCellPreviewProp[Cell.BACKGROUND_COLOR].setValue(new Color(0f, 0f, 0f, 0f));
        headerNodeDegreeCellPreviewProp[Cell.BORDER_COLOR].setValue(new Color(0f, 0f, 0f, 0f));

        Cell nodeNameCell;
        PreviewProperty[] nodeNameCellPreviewProp;
        Cell nodeDegreeCell;
        PreviewProperty[] nodeDegreeCellPreviewProp;
        for (int i = 0; i < numberOfNodes; i++) {
            nodeNameCell = table.get(i + 1).get(0);
            nodeNameCellPreviewProp = nodeNameCell.getPreviewProperties();
            nodeNameCellPreviewProp[Cell.CELL_CONTENT].setValue(nodeNames[i]);

            nodeDegreeCell = table.get(i + 1).get(1);
            nodeDegreeCellPreviewProp = nodeDegreeCell.getPreviewProperties();
            nodeDegreeCellPreviewProp[Cell.CELL_CONTENT].setValue(nodeDegrees[i].toString());
        }
    }

    @Override
    public String getDescription() {
        return "This mode is used to generate a table that list the top N nodes with the highest degree";
    }

    @Override
    public String getTitle() {
        return "Nodes with the highest degree";
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
        return "Nodes with highest degree";
    }
}
