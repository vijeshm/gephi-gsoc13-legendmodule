/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

import com.itextpdf.text.pdf.PdfContentByte;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import org.apache.batik.svggen.SVGGraphics2D;
import com.itextpdf.awt.PdfGraphics2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import org.apache.batik.svggen.*;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendItem.Alignment;
import org.gephi.legend.properties.LegendProperty;
import org.gephi.legend.api.LegendManager;
import org.gephi.preview.api.*;
import org.gephi.preview.plugin.items.NodeItem;
import org.gephi.preview.spi.Renderer;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.w3c.dom.Element;
import processing.core.PGraphicsJava2D;

/**
 *
 * @author edubecks
 */
public abstract class LegendItemRenderer implements Renderer {

    public abstract void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height);

    private float defaultMargin = 100f;
    private float graphOriginX = Float.MAX_VALUE;
    private float graphOriginY = Float.MAX_VALUE;
    private float graphWidth = 0;
    private float graphHeight = 0;
    // VARIABLES
    protected Integer width;
    protected Integer height;
    protected AffineTransform originTranslation;
    private float originX;
    private float originY;
    //description
    private Boolean isDisplayingDescription;
    private String description;
    private Alignment descriptionAlignment;
    private Font descriptionFont;
    private Color descriptionFontColor;
    //title
    private Boolean isDisplayingTitle;
    private String title;
    private Font titleFont;
    private Alignment titleAlignment;
    private Color titleFontColor;
    // BUG!!!!
    private int rendererIndex = 0;

    public void readLegendPropertiesAndValues(Item item, PreviewProperties properties) {

        if (item != null) {

            Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

            // DIMENSIONS
            width = properties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
            height = properties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));

            //TITLE
            isDisplayingTitle = properties.getBooleanValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.TITLE_IS_DISPLAYING));
            titleFont = properties.getFontValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.TITLE_FONT));
            titleFontColor = properties.getColorValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.TITLE_FONT_COLOR));
            titleAlignment = (Alignment) properties.getValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.TITLE_ALIGNMENT));
            title = properties.getStringValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.TITLE));

            //DESCRIPTION
            isDisplayingDescription = properties.getBooleanValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.DESCRIPTION_IS_DISPLAYING));
            descriptionFont = properties.getFontValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.DESCRIPTION_FONT));
            descriptionFontColor = properties.getColorValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.DESCRIPTION_FONT_COLOR));
            descriptionAlignment = (Alignment) properties.getValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.DESCRIPTION_ALIGNMENT));
            description = properties.getStringValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.DESCRIPTION));


            // ORIGIN
            originX = properties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.ORIGIN_X));
            originY = properties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.ORIGIN_Y));
//            originTranslation = new AffineTransform();
            

        }
    }

    private void renderSVG(SVGTarget target) {
        org.w3c.dom.Document document = target.getDocument();
        ImageHandler imageHandler = new ImageHandlerBase64Encoder();
        
        DefaultExtensionHandler defaultExtensionHandler = new DefaultExtensionHandler();
        
        SVGGraphics2D graphics2D = new SVGGraphics2D(document, imageHandler, defaultExtensionHandler, true);
        float targetOriginX = (int) graphOriginX;
        System.out.println("@Var: tempX: " + targetOriginX);
        float targetOriginY = (int) graphOriginY;
        System.out.println("@Var: tempY: " + targetOriginY);
//        graphics2D = (SVGGraphics2D) graphics2D.create((int)targetOriginX, (int)targetOriginY, (int)graphWidth, (int)graphHeight);
//        graphics2D.setClip((int)targetOriginX, (int)targetOriginY, (int)graphWidth, (int)graphHeight);
        originTranslation = new AffineTransform();
        originTranslation.translate(originX, originY);
        originTranslation.translate(targetOriginX, targetOriginY);
//        graphics2D.setTransform(originTranslation);
////      
//        
////        AffineTransform graphTransform = graphics2D.getTransform();
////        System.out.printf("SVG:   graphTransform T[%f,%f] S[%f,%f]\n", graphTransform.getTranslateX(), graphTransform.getTranslateY(), graphTransform.getScaleX(), graphTransform.getScaleY());
////        originTranslation = new AffineTransform(graphTransform);
////        originTranslation.translate(graphOriginX, graphOriginY);
//
//        graphics2D.setColor(Color.BLACK);
//        graphics2D.fillRect(10,10, 600,600);
//        graphics2D.setTransform(new AffineTransform());

        render(graphics2D, originTranslation, width, height);

        //appending
        org.w3c.dom.Element svgRoot = document.getDocumentElement();
        svgRoot.appendChild(graphics2D.getRoot().getLastChild());
        graphics2D.dispose();
    }

    private void renderPDF(PDFTarget target) {
        PdfContentByte pdfContentByte = target.getContentByte();
        com.itextpdf.text.Document pdfDocument = pdfContentByte.getPdfDocument();
        pdfContentByte.saveState();
        
        float pdfWidth = target.getPageSize().getWidth()- target.getMarginLeft() - target.getMarginRight();
        System.out.println("@Var: pdfWidth: " + pdfWidth);
        float pdfHeight = target.getPageSize().getHeight() - target.getMarginBottom() - target.getMarginTop();
        float scaleWidth = pdfWidth / graphWidth;
        System.out.println("@Var: scaleWidth: " + scaleWidth);
        float scaleHeight = pdfWidth / graphHeight;
        System.out.println("@Var: scaleHeight: " + scaleHeight);
        float scaleValue = Math.min(scaleWidth, scaleHeight);
        System.out.println("@Var: scaleValue: " + scaleValue);
        
//        float pdfOriginX = pdfWidth * graphOriginX / graphWidth;
//        float pdfOriginY = pdfHeight * graphOriginY / graphHeight;
        
        float targetOriginX = (int) graphOriginX;
        System.out.println("@Var: tempX: " + targetOriginX);
        float targetOriginY = (int) graphOriginY - 12;
//        Graphics2D graphics2D = new PdfGraphics2D(pdfContentByte, graphWidth, graphHeight);
        System.out.println("@Var: tempY: " + targetOriginY);
        originTranslation = new AffineTransform();
        originTranslation.translate(targetOriginX, targetOriginY);
        originTranslation.translate(originX, - originY);
        
        
        
//        originTranslation.translate(pdfOriginX, pdfOriginY);
//                originTranslation.scale(scaleValue, scaleValue);
//        originTranslation.translate(30, 30);
        pdfContentByte.transform(originTranslation);
//        Graphics2D graphics2D = new PdfGraphics2D(pdfContentByte, pdfDocument.getPageSize().getWidth(), pdfDocument.getPageSize().getHeight());
        Graphics2D graphics2D = new PdfGraphics2D(pdfContentByte, graphWidth, graphHeight);
//        Graphics2D graphics2D = pdfContentByte.createGraphics(pdfDocument.getPageSize().getWidth(), pdfDocument.getPageSize().getHeight());
        AffineTransform graphTransform = graphics2D.getTransform();



        
        


//        graphics2D.setColor(Color.PINK);
//        graphics2D.fillRect(tempX, tempY, (int) graphWidth, (int) graphHeight);

        originTranslation = new AffineTransform();
        render(graphics2D, originTranslation, width, height);
        graphics2D.dispose();
        pdfContentByte.restoreState();
    }

    private void renderProcessing(ProcessingTarget target) {
        Graphics2D graphics2D = (Graphics2D) ((PGraphicsJava2D) target.getGraphics()).g2;
        AffineTransform graphTransform = graphics2D.getTransform();
        System.out.printf("PROCESSING:   graphTransform T[%f,%f] S[%f,%f]\n", graphTransform.getTranslateX(), graphTransform.getTranslateY(), graphTransform.getScaleX(), graphTransform.getScaleY());
        AffineTransform saveState = graphics2D.getTransform();
        originTranslation = new AffineTransform(graphTransform);
        originTranslation.translate(graphOriginX, graphOriginY);
        originTranslation.translate(originX, originY);
//        originTranslation.translate(graphTransform.getTranslateX(), graphTransform.getTranslateY());
//        originTranslation.translate(graphOriginX * graphTransform.getScaleX() , graphOriginY * graphTransform.getScaleY());
//        originTranslation.scale(graphTransform.getScaleX(), graphTransform.getScaleY());
//        originTranslation.translate(originX, originY);
//        originTranslation.translate(graphTransform.getTranslateX(),graphTransform.getTranslateY());

        graphics2D.setTransform(originTranslation);

//        originTranslation.scale(graphTransform.getScaleX(), graphTransform.getScaleY());

        // temp


        render(graphics2D, originTranslation, width, height);
        graphics2D.setTransform(saveState);
    }

    public void render(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {

        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // TITLE
        AffineTransform titleOrigin = new AffineTransform(origin);
        float titleSpaceUsed = renderTitle(graphics2D, titleOrigin, width, height);
        boolean descriptionComputeSpace = true;
        float descriptionSpaceUsed = legendDrawText(graphics2D, description, descriptionFont, descriptionFontColor, origin.getTranslateX(), origin.getTranslateY(), width, height, descriptionAlignment, descriptionComputeSpace);

        // LEGEND
        AffineTransform legendOrigin = new AffineTransform(origin);
        legendOrigin.translate(0, titleSpaceUsed);
        int legendWidth = width;
        int legendHeight = (Integer) (height - Math.round(titleSpaceUsed) - Math.round(descriptionSpaceUsed));
        renderToGraphics(graphics2D, legendOrigin, legendWidth, legendHeight);

        // DESCRIPTION
        AffineTransform descriptionOrigin = new AffineTransform(origin);
        descriptionOrigin.translate(0, titleSpaceUsed + legendHeight);
        renderDescription(graphics2D, descriptionOrigin, width, height);


    }

    public float renderDescription(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        if (isDisplayingDescription && !description.isEmpty()) {
            graphics2D.setTransform(origin);
            return legendDrawText(graphics2D, description, descriptionFont, descriptionFontColor, 0, 0, width, height, descriptionAlignment);
        }
        return 0f;
    }

    public float renderTitle(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        if (isDisplayingTitle && !title.isEmpty()) {
            graphics2D.setTransform(origin);
            return legendDrawText(graphics2D, title, titleFont, titleFontColor, 0, 0, width, height, titleAlignment);
        }
        return 0f;
    }

    @Override
    public void preProcess(PreviewModel previewModel) {
    }

    public abstract void readOwnPropertiesAndValues(Item item, PreviewProperties properties);

    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {

        if (item != null) {

            ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
            Workspace workspace = pc.getCurrentWorkspace();
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel(workspace);
            Dimension dimensions = previewModel.getDimensions();
            graphHeight = dimensions.height;
            graphWidth = dimensions.width;
            System.out.println("@Var: dimensions: " + dimensions);
            Point topLeftPosition = previewModel.getTopLeftPosition();
            graphOriginX = topLeftPosition.x;
            graphOriginY = topLeftPosition.y;

//            System.out.println("@Var: topLeftPosition: "+topLeftPosition);
//            graphOriginX = Float.MAX_VALUE;
//            graphOriginY = Float.MAX_VALUE;
//            for (Item node : previewModel.getItems(Item.NODE)) {
//                graphOriginX = Math.min(graphOriginX, (Float) node.getData(NodeItem.X) - (Float) node.getData(NodeItem.SIZE));
//                graphOriginY = Math.min(graphOriginY, (Float) node.getData(NodeItem.Y) - (Float) node.getData(NodeItem.SIZE));
//            }

            System.out.printf("graphOrigin [%f.%f]\n", graphOriginX, graphOriginY);

//            graphOriginX -= defaultMargin;
//            graphOriginY -= defaultMargin;

            System.out.println("@Var: rendering item: " + item);

            readLegendPropertiesAndValues(item, properties);
            readOwnPropertiesAndValues(item, properties);

            if (target instanceof ProcessingTarget) {
                renderProcessing((ProcessingTarget) target);
            }
            else if (target instanceof SVGTarget) {
                renderSVG((SVGTarget) target);
            }
            else if (target instanceof PDFTarget) {
                renderPDF((PDFTarget) target);
            }
        }
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }

    protected float legendDrawText(Graphics2D graphics2D, String text, Font font, Color color, double x, double y, Integer width, Integer height, Alignment alignment, boolean isComputingSpace) {
        AttributedString styledText = new AttributedString(text);
        styledText.addAttribute(TextAttribute.FONT, font);
        graphics2D.setFont(font);
        graphics2D.setColor(color);
        AttributedCharacterIterator m_iterator = styledText.getIterator();
        int start = m_iterator.getBeginIndex();
        int end = m_iterator.getEndIndex();
        FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();

        LineBreakMeasurer measurer = new LineBreakMeasurer(m_iterator, fontRenderContext);
        measurer.setPosition(start);


        float xText = (float) x, yText = (float) y; // text positions

        float descent = 0, leading = 0;
        while (measurer.getPosition() < end) {
            TextLayout layout = measurer.nextLayout(width);
            yText += layout.getAscent();
            if (!isComputingSpace) {
                switch (alignment) {
                    case LEFT: {
                        break;
                    }
                    case RIGHT: {
                        Rectangle2D bounds = layout.getBounds();
                        xText = (float) ((x + width - bounds.getWidth()) - bounds.getX());
                        break;
                    }
                    case CENTER: {
                        Rectangle2D bounds = layout.getBounds();
                        xText = (float) ((x + width / 2 - bounds.getWidth() / 2) - bounds.getX());
                        break;
                    }
                    case JUSTIFIED: {
                        if (measurer.getPosition() < end) {
                            layout = layout.getJustifiedLayout(width);
                        }
                        break;
                    }

                }
                layout.draw(graphics2D, xText, yText);
            }
            descent = layout.getDescent();
            leading = layout.getLeading();
            yText += descent + leading;
        }
        return (float) Math.ceil(yText - y - descent - leading);
    }

    protected float legendDrawText(Graphics2D graphics2D, String text, Font font, Color color, double x, double y, Integer width, Integer height, Alignment alignment) {
        return legendDrawText(graphics2D, text, font, color, x, y, width, height, alignment, false);
    }

}
