/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.io.*;
import org.apache.batik.dom.GenericDOMImplementation;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.desktop.welcome.WelcomeTopComponent;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.preview.PNGExporter;
import org.gephi.io.exporter.preview.SVGExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.legend.builders.GroupsItemBuilder;
import org.gephi.legend.builders.ImageItemBuilder;
import org.gephi.legend.builders.TextItemBuilder;
import org.gephi.legend.properties.GroupsProperty;
import org.gephi.legend.properties.LegendProperty;
import org.gephi.preview.api.*;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.junit.*;
import static org.junit.Assert.*;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.w3c.dom.DOMImplementation;

/**
 *
 * @author edubecks
 */
public class TestProcessingTarget {
    
    public TestProcessingTarget() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void testPNG() {
        try {

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


            // svg document


            
            
            



            // attribute ???
            AttributeController attributeController = Lookup.getDefault().lookup(AttributeController.class);
            AttributeModel attributeModel = null;

            // preview
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel(workspace);
            PreviewProperties previewProperties = previewModel.getProperties();
            previewController.refreshPreview(workspace);


            previewModel.getProperties().putValue("width", 1000);
            previewModel.getProperties().putValue("height", 1000);
            ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET, workspace);



            previewProperties.putValue(LegendManager.LEGEND_PROPERTIES, new LegendManager());

            LegendManager legendManager = previewProperties.getValue(LegendManager.LEGEND_PROPERTIES);
            Integer itemIndex = legendManager.getCurrentIndex();


            // creating item
            Item item = addGroupsItem(itemIndex, graph, attributeModel);
//            Item item = addTextItem(newItemIndex, graph, attributeModel);


            // add item
            legendManager.addItem(item);
            PreviewProperty[] properties = item.getData(LegendItem.PROPERTIES);
            
            for (PreviewProperty property : properties) {
                previewProperties.addProperty(property);
            }
            
            //test
//            previewProperties.getProperty(LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_NUMBER_COLUMNS)).setValue(2);
            previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.ORIGIN_X)).setValue(-100);
            previewProperties.getProperty(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.ORIGIN_Y)).setValue(-100);
            



            // render
            previewController.refreshPreview(workspace);
            previewController.render(target);
            
            
            OutputStream fos = new FileOutputStream(new File("/Users/edubecks/Desktop/Untitled.png"));
            Writer writer = new OutputStreamWriter(fos, "UTF-8");
            
            
            PNGExporter pngExporter = new PNGExporter();
//            pngExporter.setHeight(2000);
//            pngExporter.setWidth(2000);
            pngExporter.setWorkspace(workspace);
            pngExporter.setOutputStream(fos);
            pngExporter.execute();
            



        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
    }

    public Item addTextItem(int newItemIndex, Graph graph, AttributeModel attributeModel) {
        TextItemBuilder builder = new TextItemBuilder();
        Item item = builder.createItem(newItemIndex, graph, attributeModel);
        return item;
    }
    
    public Item addImageItem(int newItemIndex, Graph graph, AttributeModel attributeModel) {
        ImageItemBuilder builder = new ImageItemBuilder();
        Item item = builder.createItem(newItemIndex, graph, attributeModel);
        return item;
    }
    
    public Item addGroupsItem(int newItemIndex, Graph graph, AttributeModel attributeModel) {
        GroupsItemBuilder builder = new GroupsItemBuilder();
        Item item = builder.createItem(newItemIndex, graph, attributeModel);
        return item;
    }
    
    
}
