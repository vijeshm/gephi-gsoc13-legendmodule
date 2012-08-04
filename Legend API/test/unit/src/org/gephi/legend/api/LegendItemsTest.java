/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
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
import org.gephi.io.exporter.preview.PDFExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.legend.builders.*;
import org.gephi.legend.items.*;
import org.gephi.legend.renderers.*;
import org.gephi.preview.PreviewModelImpl;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
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

    BufferedImage processingImage;

    public Graphics2D getGraphicsProcessing() {
        processingImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        processingImage.getGraphics().setColor(Color.WHITE);
        processingImage.getGraphics().fillRect(0, 0, width, height);
        return (Graphics2D) processingImage.getGraphics();
    }

    public void saveProcessing() {
        try {
            ImageIO.write(processingImage, "PNG", new File("/Users/edubecks/Desktop/Untitled.png"));
        } catch (Exception e) {
        }
    }

    public void savePDF() {
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

        AttributeModel attributeModel = null;

        PreviewModel previewModel = previewController.getModel(workspace);
        PreviewProperties previewProperties = previewModel.getProperties();

        // LEGEND MANAGER
        previewProperties.putValue(LegendManager.LEGEND_PROPERTIES, new LegendManager());
        LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
        Integer newItemIndex = legendManager.getCurrentIndex();


        Item item = null;
        LegendItemBuilder builder = null;
        LegendItemRenderer renderer = null;



        if (selectedType.equals(DescriptionItem.LEGEND_TYPE)) {
            builder = new DescriptionItemBuilder();
            renderer = new DescriptionItemRenderer();
        }
        else if (selectedType.equals(TextItem.LEGEND_TYPE)) {
            builder = new TextItemBuilder();
            renderer = new TextItemRenderer();
        }
        else if (selectedType.equals(GroupsItem.LEGEND_TYPE)) {
            builder = new GroupsItemBuilder();
            renderer = new GroupsItemRenderer();
        }
        else if (selectedType.equals(ImageItem.LEGEND_TYPE)) {
            builder = new ImageItemBuilder();
            renderer = new ImageItemRenderer();
        }
        else if (selectedType.equals(TableItem.LEGEND_TYPE)) {
            builder = new TableItemBuilder();
            renderer = new TableItemRenderer();
        }







        // CREATE ITEM AND ADD IT TO LEGEND MANAGER
        item = builder.createDefaultItem(newItemIndex, graph, attributeModel);


        legendManager.addItem(item);

        // ADDING ITS PROPERTIES TO PREVIEW PROPERTIES
        PreviewProperty[] properties = item.getData(LegendItem.PROPERTIES);
        for (PreviewProperty property : properties) {
            previewController.getModel().getProperties().addProperty(property);
        }


        Graphics2D graphics2D = getGraphicsProcessing();
        AffineTransform origin = new AffineTransform();
        origin.setToTranslation(originX, originY);
        renderer.readLegendPropertiesAndValues(item, previewProperties);
        renderer.readOwnPropertiesAndValues(item, previewProperties);
        renderer.render(graphics2D, origin, width, height);
        saveProcessing();

    }

    

    private int width = 200;
    private int height = 200;
    private int originX = 0;
    private int originY = 0;
//    private String selectedType = ImageItem.LEGEND_TYPE;
//    private String selectedType = TableItem.LEGEND_TYPE;
//    private String selectedType = DescriptionItem.LEGEND_TYPE;
//    private String selectedType = TextItem.LEGEND_TYPE;
    private String selectedType = GroupsItem.LEGEND_TYPE;
}
