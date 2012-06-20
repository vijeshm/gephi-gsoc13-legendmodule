/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.desktop.welcome.WelcomeTopComponent;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.legend.builders.TableItemBuilder;
import org.gephi.preview.PreviewModelImpl;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.spi.Renderer;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.junit.Test;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import processing.core.PApplet;

/**
 *
 * @author edubecks
 */
public class LegendItemsTest {

    private int width = 300;
    private int height = 300;
    BufferedImage processingImage;

    public Graphics2D getGraphicsProcessing() {
        processingImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        processingImage.getGraphics().setColor(Color.WHITE);
        processingImage.getGraphics().fillRect(0, 0, width, height);
        return (Graphics2D) processingImage.getGraphics();
    }

    public void saveProcessing() {
        try {
            ImageIO.write(processingImage, "PNG", new File("processing.png"));
        } catch (Exception e) {
        }
    }

    @Test
    public void tableItem() {
        //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        //Append container to graph structure
        String sample = "/org/gephi/desktop/welcome/samples/Les Miserables.gexf";
        final InputStream stream = WelcomeTopComponent.class.getResourceAsStream(sample);
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        FileImporter fileImporter = importController.getFileImporter(".gexf");
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

        AttributeController attributeController = Lookup.getDefault().lookup(AttributeController.class);
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
//        PreviewModelImpl model = pc.getCurrentWorkspace().getLookup().lookup(PreviewModelImpl.class);
//        AttributeModel attributeModel = attributeController.getModel(model.getWorkspace());

        AttributeModel attributeModel = null;

        TableItemBuilder builder = new TableItemBuilder();
        Item item = builder.createItem(graph, attributeModel);
        System.out.println("@Var: item: "+item.getData(LegendItem.SUB_TYPE));
        


    }

}
