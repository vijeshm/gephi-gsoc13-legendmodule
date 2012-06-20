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
            System.out.println("@Var: originX: " + originX);
            originY = properties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.ORIGIN_Y));
            System.out.println("@Var: originY: " + originY);

        }
    }

    private void renderSVG(SVGTarget target) {
        org.w3c.dom.Document document = target.getDocument();
        SVGGraphics2D graphics2D = new SVGGraphics2D(document);
        AffineTransform graphTransform = graphics2D.getTransform();
        originTranslation = new AffineTransform(graphTransform);
        originTranslation.translate(originX, originY);
        render(graphics2D, originTranslation, width, height);

        //appending
        org.w3c.dom.Element svgRoot = document.getDocumentElement();
        svgRoot.appendChild(graphics2D.getRoot());
        graphics2D.dispose();
    }

    private void renderPDF(PDFTarget target) {
        PdfContentByte pdfContentByte = target.getContentByte();
        com.itextpdf.text.Document pdfDocument = pdfContentByte.getPdfDocument();
        pdfContentByte.saveState();
        Graphics2D graphics2D = new PdfGraphics2D(pdfContentByte, pdfDocument.getPageSize().getWidth(), pdfDocument.getPageSize().getHeight());
//        Graphics2D graphics2D = contentByte.createGraphics(pdfDocument.getPageSize().getWidth(), pdfDocument.getPageSize().getHeight());
        AffineTransform graphTransform = graphics2D.getTransform();
        originTranslation = new AffineTransform(graphTransform);
        originTranslation.translate(originX, originY);
        render(graphics2D, originTranslation, width, height);
        graphics2D.dispose();
        pdfContentByte.restoreState();
    }

    private void renderProcessing(ProcessingTarget target) {
        Graphics2D graphics2D = (Graphics2D) ((PGraphicsJava2D) target.getGraphics()).g2;
        System.out.println("@Var: Processing>>>>> graphics2D: " + graphics2D.getTransform());
        AffineTransform saveState = graphics2D.getTransform();
        originTranslation = new AffineTransform(saveState);
        originTranslation.translate(originX, originY);
        System.out.println("@Var: originTranslation: " + originTranslation);
//        originTranslation.translate(graphTransform.getTranslateX(),graphTransform.getTranslateY());
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

    public abstract void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height);

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
