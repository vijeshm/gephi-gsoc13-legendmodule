/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeOrigin;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.partition.api.Part;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.api.PartitionModel;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

/**
 *
 * @author edubecks
 */
public class StatisticData {

    public static boolean generateTable(ArrayList<String> labels, ArrayList<Color> colors, ArrayList<ArrayList<Float>> values) {

        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
        Workspace workspace = projectController.getCurrentWorkspace();
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        GraphModel model = graphController.getModel(workspace);
        Graph graph = model.getGraph();
        PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
        PartitionModel partitionModel = partitionController.getModel();






        if (partitionModel.getSelectedPartition() != null) {

            float[][] valuesTemp = new float[partitionModel.getSelectedPartition().getPartsCount()][partitionModel.getSelectedPartition().getPartsCount()];

            HashMap<String, Integer> labelsMap = new HashMap<String, Integer>();

            // FILLING LABELS
            int index = 0;
            for (Part<Node> part : partitionModel.getSelectedPartition().getParts()) {
                labelsMap.put(part.getDisplayName(), index++);
                labels.add(part.getDisplayName());
            }


            // FILLING COLORS
            for (Part<Node> part : partitionModel.getSelectedPartition().getParts()) {
                colors.add(part.getColor());
            }

            // FILLING VALUES
            for (Part<Node> part : partitionModel.getSelectedPartition().getParts()) {
                Node[] nodes = part.getObjects();
                for (Node node : nodes) {
                    for (Edge edge : graph.getEdges(node).toArray()) {
                        Node anotherNode = (node.equals(edge.getSource())) ? edge.getTarget() : edge.getSource();
                        Part anotherPart = partitionModel.getSelectedPartition().getPart(anotherNode);
                        valuesTemp[labelsMap.get(part.getDisplayName())][labelsMap.get(anotherPart.getDisplayName())]++;
                    }
                }

            }

            for (int i = 0; i < valuesTemp.length; i++) {
                ArrayList<Float> row= new ArrayList<Float>();
                for (int j = 0; j < valuesTemp[i].length; j++) {
                    row.add(valuesTemp[i][j]);
                }
                values.add(row);
            }

            System.out.println("@Var: labels: " + labels);
            System.out.println("@Var: colors: " + colors);
            System.out.println("@Var: values: " + valuesTemp);

            return true;
        }
        return false;

    }

    //public cons for the result column name if reused by others
    public static final String AVG_EUCLIDEAN_DISTANCE = "avg_euclidean_distance";
    //Settings
    private boolean useOnlyConnections = false;

    public void averageEuclidianDistance(Graph graph, AttributeModel attributeModel) {
        //Look if the result column already exist and create it if needed
        AttributeTable nodeTable = attributeModel.getNodeTable();
        AttributeColumn col = nodeTable.getColumn(AVG_EUCLIDEAN_DISTANCE);
        if (col == null) {
            col = nodeTable.addColumn(AVG_EUCLIDEAN_DISTANCE, "Average Euclidean Distance", AttributeType.DOUBLE, AttributeOrigin.COMPUTED, 0.0);
        }

        //Lock to graph. This is important to have consistent results if another
        //process is currently modifying it.
        graph.readLock();

        //Iterate on all nodes
        Node[] nodes = graph.getNodes().toArray();
        for (Node n : nodes) {
            double avg = 0;
            int count = 0;
            if (useOnlyConnections) {
                //Calculate distance with neighbors
                for (Node m : graph.getNeighbors(n)) {
                    double xDist = n.getNodeData().x() - m.getNodeData().x();
                    double yDist = n.getNodeData().y() - m.getNodeData().y();
                    double dist = Math.sqrt(xDist * xDist + yDist * yDist);
                    avg = (dist + avg) / ++count;
                }
            }
            else {
                //Calculate distance with all other nodes
                for (Node m : nodes) {
                    if (n != m) {
                        double xDist = n.getNodeData().x() - m.getNodeData().x();
                        double yDist = n.getNodeData().y() - m.getNodeData().y();
                        double dist = Math.sqrt(xDist * xDist + yDist * yDist);
                        avg = (dist + avg) / ++count;
                    }
                }
            }
            //Store the average in the node attribute
            n.getAttributes().setValue(col.getIndex(), avg);
        }

        graph.readUnlock();
    }

}
