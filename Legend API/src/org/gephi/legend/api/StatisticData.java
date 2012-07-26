/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeOrigin;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.statistics.spi.Statistics;

/**
 *
 * @author edubecks
 */
public class StatisticData {
    
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
            } else {
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
