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
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendItem.Alignment;
import org.gephi.legend.properties.LegendProperty;
import org.gephi.legend.api.LegendManager;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.Renderer;
import org.openide.util.NbBundle;
import processing.core.PGraphicsJava2D;

/**
 *
 * @author edubecks
 */
public abstract class LegendItemRenderer implements Renderer {

    // VARIABLES
    protected Integer width;
    protected Integer height;
    protected AffineTransform originTranslation;
    //description
    protected Boolean isDisplayingDescription;
    protected String description;
    protected Alignment descriptionAlignment;
    protected Font descriptionFont;
    protected Color descriptionFontColor;
    //title
    protected Boolean isDisplayingTitle;
    protected String title;
    protected Font titleFont;
    protected Alignment titleAlignment;
    protected Color titleFontColor;
    
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
            float originX = properties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.ORIGIN_X));
            float originY = properties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.ORIGIN_Y));
            originTranslation = new AffineTransform();
            originTranslation.setToTranslation(originX, originY);
        }
    }
    
    private void renderSVG(SVGTarget target) {
        org.w3c.dom.Document document = target.getDocument();
        SVGGraphics2D graphics2D = new SVGGraphics2D(document);
        render(graphics2D, originTranslation, width, height);

        //appending
        org.w3c.dom.Element svgRoot = document.getDocumentElement();
        svgRoot.appendChild(graphics2D.getRoot());
        graphics2D.dispose();
    }
    
    private void renderPDF(PDFTarget target) {
        PdfContentByte contentByte = target.getContentByte();
        com.itextpdf.text.Document pdfDocument = contentByte.getPdfDocument();
        contentByte.saveState();
        Graphics2D graphics2D = contentByte.createGraphics(pdfDocument.getPageSize().getWidth(), pdfDocument.getPageSize().getHeight());
        render(graphics2D, originTranslation, width, height);
        graphics2D.dispose();
        contentByte.restoreState();
    }
    
    private void renderProcessing(ProcessingTarget target) {
        Graphics2D graphics2D = (Graphics2D) ((PGraphicsJava2D) target.getGraphics()).g2;
        render(graphics2D, originTranslation, width, height);
    }
    
    public void render(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        System.out.println("@Var: Rendering at origin: "+origin);
        
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        float titleSpaceUsed = renderTitle(graphics2D, origin, width, height);
        
        origin.translate(0, titleSpaceUsed);
        renderToGraphics(graphics2D, origin, width, height);
        origin.translate(0, height);
        float descriptionSpaceUsed = renderDescription(graphics2D, origin, width, height);
        
        
    }
    
    public abstract void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height);
    
    public float renderDescription(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        if (isDisplayingDescription && !description.isEmpty()) {
            graphics2D.setTransform(origin);
            return legendDrawText(graphics2D, description, descriptionFont, descriptionFontColor, origin.getTranslateX(), origin.getTranslateY(), width, height, descriptionAlignment);
        }
        return 0f;
    }
    
    public float renderTitle(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        if (isDisplayingTitle && !title.isEmpty()) {
            graphics2D.setTransform(origin);
            return legendDrawText(graphics2D, title, titleFont, titleFontColor, origin.getTranslateX(), origin.getTranslateY(), width, height, titleAlignment);
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

    
    protected float legendDrawText(Graphics2D graphics2D, String text, Font font, Color color, double x, double y, Integer width, Integer height, Alignment alignment) {
        graphics2D.setColor(color);
        
        AttributedString styledText = new AttributedString(text);
        styledText.addAttribute(TextAttribute.FONT, font);
        AttributedCharacterIterator m_iterator = styledText.getIterator();
        int start = m_iterator.getBeginIndex();
        int end = m_iterator.getEndIndex();
        FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();
        System.out.println("@Var: fontRenderContext: "+fontRenderContext.toString());
        
        LineBreakMeasurer measurer = new LineBreakMeasurer(m_iterator, fontRenderContext);
        measurer.setPosition(start);
        
        
        float xText = (float) x, yText = (float) y; // text positions

        
        while (measurer.getPosition() < end) {
            TextLayout layout = measurer.nextLayout(width);
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
            
            
            yText += layout.getAscent();
            layout.draw(graphics2D, xText, yText);
            yText += layout.getDescent() + layout.getLeading();
        }
        return yText - (float)y;
        
        
        
    }
    
}
