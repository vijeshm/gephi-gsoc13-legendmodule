package org.gephi.legend.plugin.table;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
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
 * Nodes with Highest Degree mode of the table legend lists the top N
 * (user-specified) number of nodes with the highest degree, along with the
 * degrees.
 *
 * This mode customizes the table to have only two columns and a user-specified
 * number of rows. The first column contains the node names and the second
 * column contains their degrees, in the descending order.
 *
 * @author mvvijesh
 */
@ServiceProvider(service = CustomTableItemBuilder.class, position = 3)
public class NodesWithHighestDegree implements CustomTableItemBuilder {

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
    protected String headerNodeName = "Node";
    protected String headerNodeDegree = "Degree";

    /**
     * builds the table item with a user-specified number of rows, two columns
     * and fills it up with the top N nodes with the highest degree.
     *
     * @param tableItem - the item being built
     */
    @Override
    public void populateTable(TableItem tableItem) {
        String newValueString = (String) JOptionPane.showInputDialog(null, "Enter the top number of nodes:", "Number of Nodes", JOptionPane.PLAIN_MESSAGE, null, null, "");
        Integer numberOfNodes;
        try {
            numberOfNodes = Integer.parseInt(newValueString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Entry. The number of nodes is set to 1.", "Invalid Entry", JOptionPane.PLAIN_MESSAGE);
            numberOfNodes = 1;
        }

        if (numberOfNodes < 1) {
            JOptionPane.showMessageDialog(null, "The minimum number of nodes is 1.", "Invalid Entry", JOptionPane.PLAIN_MESSAGE);
            numberOfNodes = 1;
        }

        Integer tableNumberOfRows = numberOfNodes + 1;
        Integer tableNumberOfColumns = 2;

        String[] nodeNames = new String[numberOfNodes];
        Integer[] nodeDegrees = new Integer[numberOfNodes];

        // find the top N nodes, wrt degree
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        GraphModel graphModel = graphController.getGraphModel();
        Graph graph = graphModel.getGraph();

        NodeIterable nodeIterable = graph.getNodes();
        Iterator<Node> nodeIter = nodeIterable.iterator();
        Node node;
        int degree;
        while (nodeIter.hasNext()) {
            node = nodeIter.next();
            degree = graph.getDegree(node);
            int moveIndex = -1;

            // To find the top N node with the max degree, the degrees could be sorted and top N could be chosen.
            // But that is computationally complex [ O(n * logn) ]
            // The following approach is a linear time algorithm wrt number of nodes, times the top number of nodes N. [ O(n * N)]

            for (int i = 0; i < numberOfNodes; i++) {
                // check if the element fits at the position i
                // if it fits, move the lower elements one block down and assign this to the position
                if (nodeDegrees[i] == null) {
                    nodeDegrees[i] = numberOfNodes;
                    nodeNames[i] = node.getLabel();
                    break;
                }

                if (i == 0) {
                    if (degree >= nodeDegrees[i]) {
                        moveIndex = 0;
                        break;
                    }
                } else if (nodeDegrees[i - 1] > degree && nodeDegrees[i] <= degree) {
                    moveIndex = i;
                }
            }

            if (moveIndex != -1) {
                // move the entries from moveIndex one step down and set the
                for (int i = numberOfNodes - 1; i > moveIndex; i--) {
                    nodeDegrees[i] = nodeDegrees[i - 1];
                    nodeNames[i] = nodeNames[i - 1];
                }

                nodeDegrees[moveIndex] = degree;
                nodeNames[moveIndex] = node.getLabel();
            }
        }

        // build the default table and later customize according to the data
        for (int i = 0; i < tableNumberOfRows; i++) {
            tableItem.addRow(i, cellBackgroundColor, cellBorderColor, cellFont, cellFontAlignment, cellFontColor, cellTextContent, cellShapeShape, cellShapeColor, cellShapeValue, cellImageFile, cellImageIsScaling, cellType);
        }
        for (int i = 0; i < tableNumberOfColumns; i++) {
            tableItem.addColumn(i, cellBackgroundColor, cellBorderColor, cellFont, cellFontAlignment, cellFontColor, cellTextContent, cellShapeShape, cellShapeColor, cellShapeValue, cellImageFile, cellImageIsScaling, cellType);
        }

        ArrayList<ArrayList<Cell>> table = tableItem.getTable();

        // custom built style for node name in header
        Cell headerNodeNameCell = table.get(0).get(0);
        PreviewProperty[] headerNodeNameCellPreviewProp = headerNodeNameCell.getPreviewProperties();
        headerNodeNameCellPreviewProp[Cell.CELL_TEXT_CONTENT].setValue(headerNodeName);
        headerNodeNameCellPreviewProp[Cell.BACKGROUND_COLOR].setValue(new Color(0f, 0f, 0f, 0f));
        headerNodeNameCellPreviewProp[Cell.BORDER_COLOR].setValue(new Color(0f, 0f, 0f, 0f));

        // custom built style for degree in header
        Cell headerNodeDegreeCell = table.get(0).get(1);
        PreviewProperty[] headerNodeDegreeCellPreviewProp = headerNodeDegreeCell.getPreviewProperties();
        headerNodeDegreeCellPreviewProp[Cell.CELL_TEXT_CONTENT].setValue(headerNodeDegree);
        headerNodeDegreeCellPreviewProp[Cell.BACKGROUND_COLOR].setValue(new Color(0f, 0f, 0f, 0f));
        headerNodeDegreeCellPreviewProp[Cell.BORDER_COLOR].setValue(new Color(0f, 0f, 0f, 0f));

        // construct all the other rows
        Cell nodeNameCell;
        PreviewProperty[] nodeNameCellPreviewProp;
        Cell nodeDegreeCell;
        PreviewProperty[] nodeDegreeCellPreviewProp;
        for (int i = 0; i < numberOfNodes; i++) {
            nodeNameCell = table.get(i + 1).get(0);
            nodeNameCellPreviewProp = nodeNameCell.getPreviewProperties();
            nodeNameCellPreviewProp[Cell.CELL_TEXT_CONTENT].setValue(nodeNames[i]);

            nodeDegreeCell = table.get(i + 1).get(1);
            nodeDegreeCellPreviewProp = nodeDegreeCell.getPreviewProperties();
            nodeDegreeCellPreviewProp[Cell.CELL_TEXT_CONTENT].setValue(nodeDegrees[i].toString());
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