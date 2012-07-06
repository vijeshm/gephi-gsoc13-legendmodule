/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.gephi.desktop.welcome.WelcomeTopComponent;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.util.Lookup;

/**
 *
 * @author eduBecKs
 */
public class TestPartitionData {

    public TestPartitionData() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testPartitionData() {
        try {


            //Init a project - and therefore a workspace
            ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
            pc.newProject();
            Workspace workspace = pc.getCurrentWorkspace();

            //Append container to graph structure
            String sample = "/org/gephi/desktop/welcome/samples/Les Miserables.gexf";
            final InputStream stream = WelcomeTopComponent.class.getResourceAsStream(sample);
//            final InputStream stream = new FileInputStream("/Users/eduBecKs/Desktop/partitions.gephi");
            ImportController importController = Lookup.getDefault().lookup(ImportController.class);
            FileImporter fileImporter = importController.getFileImporter(".gexf");
//            FileImporter fileImporter = importController.getFileImporter(new File("/Users/eduBecKs/Desktop/partitions.gephi"));
            System.out.println("@Var: fileImporter: "+fileImporter);
            
            Container container = importController.importFile(stream, fileImporter);

            importController.process(container, new DefaultProcessor(), workspace);

            //Add self loop
            GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
            Graph graph = graphController.getModel().getGraph();
            Node node = graph.getNode(12);
            System.out.println("Self loop " + node.getNodeData().getLabel());
            graph.addEdge(graphController.getModel().factory().newEdge(node, node, 31, true));

            //Set label edges       
            for (Edge edge : graphController.getModel().getGraph().getEdges()) {
                edge.getEdgeData().setLabel("Label test");
            }

            PartitionData partitionData = new PartitionData();
            partitionData.retrieveData();
        } catch (Exception e) {
            System.out.println("@Var: e: "+e);
        }
    }

}
