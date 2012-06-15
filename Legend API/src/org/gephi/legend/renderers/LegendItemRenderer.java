/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

import com.itextpdf.text.pdf.PdfContentByte;
import java.awt.*;
import java.awt.geom.AffineTransform;
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

    //INDEX
    // VARIABLES
    protected Integer width;
    protected Integer height;
    protected AffineTransform originTranslation;
    //description
    protected Boolean isDisplayingDescription;
    protected String description;
    protected Font descriptionFont;
    protected Color descriptionFontColor;
    //title
    protected Boolean isDisplayingTitle;
    protected String title;
    protected Font titleFont;
    protected Color titleFontColor;

    public void readLegendPropertiesAndValues(Item item, PreviewProperties properties) {

        int workspaceIndex = item.getData(LegendItem.WORKSPACE_INDEX);
        int itemIndex = item.getData(LegendItem.ITEM_INDEX);

        // DIMENSIONS
        width = properties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, workspaceIndex, itemIndex, LegendProperty.WIDTH));
        height = properties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, workspaceIndex, itemIndex, LegendProperty.HEIGHT));

        //TITLE
        isDisplayingTitle = properties.getBooleanValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, workspaceIndex, itemIndex, LegendProperty.TITLE_IS_DISPLAYING));
        titleFont = properties.getFontValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, workspaceIndex, itemIndex, LegendProperty.TITLE_FONT));
        titleFontColor = properties.getColorValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, workspaceIndex, itemIndex, LegendProperty.TITLE_FONT_COLOR));
        title = item.getData(LegendItem.TITLE);

        //DESCRIPTION
        isDisplayingDescription = properties.getBooleanValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, workspaceIndex, itemIndex, LegendProperty.DESCRIPTION_IS_DISPLAYING));
        descriptionFont = properties.getFontValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, workspaceIndex, itemIndex, LegendProperty.DESCRIPTION_FONT));
        descriptionFontColor = properties.getColorValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, workspaceIndex, itemIndex, LegendProperty.DESCRIPTION_FONT_COLOR));
        description = item.getData(LegendItem.DESCRIPTION);


        // ORIGIN
        float originX = properties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, workspaceIndex, itemIndex, LegendProperty.ORIGIN_X));
        float originY = properties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES, workspaceIndex, itemIndex, LegendProperty.ORIGIN_Y));
        originTranslation = new AffineTransform();
        originTranslation.setToTranslation(originX, originY);
    }

    protected void renderSVG(SVGTarget target) {
        org.w3c.dom.Document document = target.getDocument();
        SVGGraphics2D graphics2D = new SVGGraphics2D(document);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderToGraphics(graphics2D, originTranslation, width, height);

        //appending
        org.w3c.dom.Element svgRoot = document.getDocumentElement();
        svgRoot.appendChild(graphics2D.getRoot());
    }

    protected void renderPDF(PDFTarget target) {
        PdfContentByte contentByte = target.getContentByte();
        com.itextpdf.text.Document pdfDocument = contentByte.getPdfDocument();
        contentByte.saveState();
        Graphics2D graphics2D = contentByte.createGraphics(pdfDocument.getPageSize().getWidth(), pdfDocument.getPageSize().getHeight());
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderToGraphics(graphics2D, originTranslation, width, height);
        graphics2D.dispose();
        contentByte.restoreState();
    }

    protected void renderProcessing(ProcessingTarget target) {
        Graphics2D graphics2D = (Graphics2D) ((PGraphicsJava2D) target.getGraphics()).g2;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        renderToGraphics(graphics2D, originTranslation, width, height);
    }

    public void render(Graphics2D graphics2D, AffineTransform origin, int width, int height) {
        int titleSpaceUsed = renderTitle(graphics2D, origin);
        renderToGraphics(graphics2D, origin, width, height - titleSpaceUsed);

    }

    public abstract void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, int width, int height);

    public int renderTitle(Graphics2D graphics2D, AffineTransform origin) {
        if (isDisplayingTitle) {
            graphics2D.setColor(titleFontColor);
            graphics2D.setFont(titleFont);
            int titleFontHeight = graphics2D.getFontMetrics().getHeight();
            graphics2D.drawString(title, 0, titleFontHeight);
            origin.translate(0, titleFontHeight);
            return titleFontHeight;
        }
        return 0;
    }

    public void renderDescription(Graphics2D graphics2D, AffineTransform origin) {
        if (isDisplayingDescription) {
            graphics2D.setColor(descriptionFontColor);
            graphics2D.setFont(descriptionFont);
            int descriptionFontHeight = graphics2D.getFontMetrics().getHeight();
            graphics2D.drawString(description, 0, descriptionFontHeight);
            origin.translate(0, descriptionFontHeight);
        }
    }

    @Override
    public void preProcess(PreviewModel previewModel) {
    }

    public abstract void readOwnPropertiesAndValues(Item item, PreviewProperties properties);

    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {
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

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }

    protected void legendDrawString(Graphics2D graphics2D, String label, Font font, Color color, int x, int y, int width, int height, Alignment alignment) {
        graphics2D.setColor(color);
        graphics2D.setFont(font);
        int labelWidth = graphics2D.getFontMetrics().stringWidth(label);
        y += height;
        switch (alignment) {
            case RIGHT: {
                x+= width - labelWidth;
                break;
            }
            case CENTER:{
                x+= ((width - labelWidth)/2);
                break;
            }
        }
        graphics2D.drawString(label, x, y);
    }

}
