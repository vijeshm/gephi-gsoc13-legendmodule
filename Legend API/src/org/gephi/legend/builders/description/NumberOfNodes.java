/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders.description;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.legend.api.DescriptionItemElementValue;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = DescriptionItemElementValue.class, position = 2)
public class NumberOfNodes extends DescriptionItemElementValue {

    @Override
    public String getValue() {
        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
        Workspace workspace = projectController.getCurrentWorkspace();
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        Graph graph = graphController.getModel(workspace).getGraph();
        return "" + graph.getNodeCount();
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(CustomValue.class, "DescriptionItemElementValue.NumberOfNodes.displayName");
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(CustomValue.class, "DescriptionItemElementValue.NumberOfNodes.description");
    }

}
