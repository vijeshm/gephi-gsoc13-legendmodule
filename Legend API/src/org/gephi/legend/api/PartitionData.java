/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.partition.api.NodePartition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.api.PartitionModel;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

/**
 *
 * @author eduBecKs
 */
public class PartitionData {

    public PartitionData() {
    }

    public void retrieveData() {
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        Workspace workspace = pc.getCurrentWorkspace();
        PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        GraphModel graphModel = graphController.getModel(workspace);
        AttributeController attributeController = Lookup.getDefault().lookup(AttributeController.class);
        AttributeModel attributeModel = attributeController.getModel(workspace);
        
        for (AttributeColumn column : attributeModel.getNodeTable().getColumns()) {
            
        System.out.println("@Var: attributeColumn: "+column);
        System.out.println("@Var: attributeColumn: "+column.getId());
        System.out.println("@Var: attributeColumn: "+column.getTitle());
        
        }
        AttributeColumn attributeColumn = attributeModel.getNodeTable().getColumn(2);
        System.out.println("@Var: attributeColumn.getId(): "+attributeColumn.getId());
        partitionController.refreshPartitions();
        

        Graph graph = graphModel.getHierarchicalGraph();
        graph.getNodeCount();
        System.out.println("@Var: graph.getNodeCount(): "+graph.getNodeCount());
        PartitionModel partitionModel = partitionController.getModel();
        
        partitionController.buildPartition(attributeColumn, graph);
        



        if (partitionModel != null) {
            NodePartition[] nodePartitions = partitionModel.getNodePartitions();
            System.out.println("@Var: num Partitions: " + nodePartitions.length);


            Float[][] tableValues = new Float[nodePartitions.length][nodePartitions.length];
            for (int i = 0; i < nodePartitions.length; i++) {
                System.out.println("@Var: nodePartitions: (elements):" + nodePartitions[i].getElementsCount());

                for (int j = 0; j < nodePartitions.length; j++) {
                    tableValues[i][j] = 0f;

                }
            }

        }
    }

}
