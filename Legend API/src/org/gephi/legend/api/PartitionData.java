/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.awt.Color;
import java.util.ArrayList;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.partition.api.NodePartition;
import org.gephi.partition.api.Part;
import org.gephi.partition.api.Partition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.api.PartitionModel;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.ui.components.SimpleHTMLReport;
import org.openide.util.Lookup;

/**
 *
 * @author eduBecKs
 */
public class PartitionData {
    
    ArrayList<String> labels;
    ArrayList<Color> colors;
    ArrayList<Float> values;

    public PartitionData() {
        this.labels = new ArrayList<String>();
        this.colors = new ArrayList<Color>();
        this.values = new ArrayList<Float>();
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

    public ArrayList<Float> getValues() {
        return values;
    }
    
    

    public void retrieveData() {
        
        
        
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        Workspace workspace = pc.getCurrentWorkspace();
        
        
        PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
        PartitionModel partitionModel = partitionController.getModel();

        if (partitionModel.getSelectedPartition() != null && partitionModel.getSelectedPartitioning() == PartitionModel.NODE_PARTITIONING) {
            NodePartition nodesPartition = (NodePartition) partitionModel.getSelectedPartition();
            for (Part<Node> part : nodesPartition.getParts()) {
                values.add(part.getPercentage());
                colors.add(part.getColor());
                labels.add(part.getValue().toString());
            }
        }
    }
        
//        
//        partitionController.refreshPartitions();
//        
//        Partition[] partitionArray = partitionModel.getNodePartitions();
//        
//        for (Partition partition : partitionArray) {
//            System.out.println("@Var: partition: "+partition);
//        }
//        partitionController.setSelectedPartitioning(PartitionModel.NODE_PARTITIONING);
//        partitionController.setSelectedPartition(partitionArray[0]);
//        System.out.println("@Var: partitionModel.getSelectedPartition(): "+partitionModel.getSelectedPartition());
//        
//        partitionController.transform(partitionModel.getSelectedPartition(), partitionModel.getSelectedTransformer());
//        
//        Part[] parts = partitionArray[0].getParts();
//        
//        for (Part part : parts) {
//            System.out.println("@Var: part: "+part);
//        }

//        for (AttributeColumn column : attributeModel.getNodeTable().getColumns()) {
//
//            System.out.println("@Var: attributeColumn: " + column);
//            System.out.println("@Var: attributeColumn: " + column.getId());
//            System.out.println("@Var: attributeColumn: " + column.getTitle());
//
//        }
//        AttributeColumn attributeColumn = attributeModel.getNodeTable().getColumn(2);
//        System.out.println("@Var: attributeColumn.getId(): " + attributeColumn.getId());
//
//
//        Graph graph = graphModel.getHierarchicalGraph();
//        graph.getNodeCount();
//        System.out.println("@Var: graph.getNodeCount(): " + graph.getNodeCount());
//
//        partitionController.buildPartition(attributeColumn, graph);


}
