/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.imageio.ImageIO;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.desktop.welcome.WelcomeTopComponent;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.preview.PNGExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.legend.builders.DescriptionItemBuilder;
import org.gephi.legend.builders.GroupsItemBuilder;
import org.gephi.legend.builders.ImageItemBuilder;
import org.gephi.legend.builders.LegendItemBuilder;
import org.gephi.legend.builders.TableItemBuilder;
import org.gephi.legend.builders.TextItemBuilder;
import org.gephi.legend.items.DescriptionItem;
import org.gephi.legend.items.GroupsItem;
import org.gephi.legend.items.ImageItem;
import org.gephi.legend.items.TableItem;
import org.gephi.legend.items.TextItem;
import org.gephi.legend.renderers.DescriptionItemRenderer;
import org.gephi.legend.renderers.GroupsItemRenderer;
import org.gephi.legend.renderers.ImageItemRenderer;
import org.gephi.legend.renderers.LegendItemRenderer;
import org.gephi.legend.renderers.TableItemRenderer;
import org.gephi.legend.renderers.TextItemRenderer;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author edubecks
 */
public class TestLegendItem {

    public TestLegendItem() {
    }

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

    @Test
    public void testPNG() {
        try {

            ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
            pc.newProject();
            Workspace workspace = pc.getCurrentWorkspace();

            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);

            AttributeModel attributeModel = null;

            PreviewModel previewModel = previewController.getModel(workspace);
            PreviewProperties previewProperties = previewModel.getProperties();

            Item item = null;
            LegendItemBuilder builder = null;
            LegendItemRenderer renderer = null;

            // render
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
            Integer newItemIndex = 0;
            item = builder.createItem(newItemIndex, null, null);


            System.out.println("@Var: item: " + item.getData(LegendItem.SUB_TYPE));

            // ADDING ITS PROPERTIES TO PREVIEW PROPERTIES
            PreviewProperty[] properties = item.getData(LegendItem.PROPERTIES);
            for (PreviewProperty property : properties) {
                previewController.getModel().getProperties().addProperty(property);
            }


            OutputStream fos = new FileOutputStream(new File("/Users/edubecks/Desktop/Untitled.png"));
            Writer writer = new OutputStreamWriter(fos, "UTF-8");


            Graphics2D graphics2D = getGraphicsProcessing();
            AffineTransform origin = new AffineTransform();
            origin.setToTranslation(originX, originY);
            renderer.readLegendPropertiesAndValues(item, previewProperties);
            renderer.readOwnPropertiesAndValues(item, previewProperties);
            renderer.render(graphics2D, origin, width, height);
            saveProcessing();




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
