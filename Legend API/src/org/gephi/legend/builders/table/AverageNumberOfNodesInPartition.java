/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders.table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.legend.api.CustomLegendItemBuilder;
import org.gephi.legend.api.CustomTableItemBuilder;
import org.gephi.legend.items.TableItem;
import org.gephi.partition.api.Part;
import org.gephi.partition.api.Partition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.api.PartitionModel;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */

@ServiceProvider(service=CustomTableItemBuilder.class, position=1)
public class AverageNumberOfNodesInPartition extends CustomLegendItemBuilder implements CustomTableItemBuilder{


    @Override
    public String getDescription() {
        return NbBundle.getMessage(AverageNumberOfNodesInPartition.class, "Table.builder.AverageNumberOfNodesInPartition.description");
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(AverageNumberOfNodesInPartition.class, "Table.builder.AverageNumberOfNodesInPartition.title");
    }


    @Override
    public void retrieveData(ArrayList<TableItem.LabelSelection> labels, ArrayList<String> horizontalLabels, ArrayList<String> verticalLabels, ArrayList<ArrayList<Float>> values, ArrayList<Color> horizontalColors, ArrayList<Color> verticalColors, ArrayList<ArrayList<Color>> valueColors) {
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        GraphModel model = graphController.getModel();
        Graph graph = model.getGraph();
        PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
        PartitionModel partitionModel = partitionController.getModel();



        if (partitionModel.getSelectedPartition() != null) {
            Partition selectedPartition = partitionModel.getSelectedPartition();

            float[][] valuesTemp = new float[selectedPartition.getPartsCount()][selectedPartition.getPartsCount()];

            HashMap<String, Integer> labelsMap = new HashMap<String, Integer>();

            // FILLING LABELS
            int index = 0;
            for (Part part : partitionModel.getSelectedPartition().getParts()) {
//                StringBuilder label = new StringBuilder(part.getDisplayName());
                String label = part.getDisplayName();
                labelsMap.put(label, index++);
                horizontalLabels.add(label);
                verticalLabels.add(label);
            }
            labels.add(TableItem.LabelSelection.BOTH);
            


            // FILLING VALUES
            for (Edge edge : graph.getEdges().toArray()) {
                Part sourcePart = selectedPartition.getPart(edge.getSource());
                Part targetPart = selectedPartition.getPart(edge.getTarget());
                valuesTemp[labelsMap.get(sourcePart.getDisplayName())][labelsMap.get(targetPart.getDisplayName())]++;
            }

            for (int i = 0; i < valuesTemp.length; i++) {
                ArrayList<Float> row= new ArrayList<Float>();
                for (int j = 0; j < valuesTemp[i].length; j++) {
                    row.add(valuesTemp[i][j]);
                }
                values.add(row);
            }
            
            
            // FILLING COLORS
            for (Part<Node> part : selectedPartition.getParts()) {
                Color color = part.getColor();
                verticalColors.add(color);
                horizontalColors.add(color);
                ArrayList<Color> colorRow = new ArrayList<Color>();
                for (int i = 0; i < valuesTemp.length; i++) {
                    colorRow.add(color);
                }
                valueColors.add(colorRow);
            }


        }
    }

    @Override
    public boolean isAvailableToBuild() {
        PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
        PartitionModel partitionModel = partitionController.getModel();
        if (partitionModel != null) {
            if (partitionModel.getSelectedPartition() != null) {
                return (partitionModel.getSelectedPartition().getPartsCount() > 1);
            }
        }
        return false;
    }

    @Override
    public String stepsNeededToBuild() {
        return NbBundle.getMessage(AverageNumberOfNodesInPartition.class, "Table.builder.AverageNumberOfNodesInPartition.stepsNeeded");
    }




    
}
