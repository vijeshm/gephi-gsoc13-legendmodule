/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders.description.elements;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.legend.api.DescriptionItemElementValue;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.api.PartitionModel;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = DescriptionItemElementValue.class, position = 4)
public class NodeWithMostConnections extends DescriptionItemElementValue {

    @Override
    public String getValue() {
        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
        Workspace workspace = projectController.getCurrentWorkspace();
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        Graph graph = graphController.getModel(workspace).getGraph();
        Node nodeWithMostConnections = null;
        int maxConnections = Integer.MIN_VALUE;
        for (Node node : graph.getNodes().toArray()) {
            if(graph.getDegree(node)>maxConnections){
                nodeWithMostConnections = node;
            }
        }
        return nodeWithMostConnections.getNodeData().getLabel()+" ("+maxConnections+")";
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(NodeWithMostConnections.class, "DescriptionItemElementValue.NodeWithMostConnections.displayName");
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(NodeWithMostConnections.class, "DescriptionItemElementValue.NodeWithMostConnections.description");
    }
}
