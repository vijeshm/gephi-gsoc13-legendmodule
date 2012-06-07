/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

import com.itextpdf.text.pdf.PdfContentByte;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import org.apache.batik.svggen.SVGGraphics2D;
import org.gephi.legend.api.LegendItem;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.Renderer;
import org.openide.util.NbBundle;
import processing.core.PGraphicsJava2D;

/**
 *
 * @author edubecks
 */
public abstract class LegendItemRenderer implements Renderer {

    //DEFAULT VALUES 
    protected float defaultOriginX = 100f;
    protected float defaultOriginY = 100f;
    protected float defaultWidth = 200f;
    protected float defaultHeight = 200f;
    protected Boolean defaultIsDisplayingTitle = true;
    protected final Font defaultTitleFont = new Font("Arial", Font.BOLD, 24);
    protected final Color defaultTitleFontColor = Color.BLACK;
    protected Boolean defaultIsDisplayingDescription = true;
    protected final Color defaultDescriptionFontColor = Color.BLACK;
    protected final Font defaultDescriptionFont = new Font("Arial", Font.PLAIN, 10);

    public PreviewProperty[] createLegendProperties() {


        return new PreviewProperty[]{
                    PreviewProperty.createProperty(this,
                                                   PreviewProperty.LEGEND_ORIGIN_X,
                                                   Float.class,
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.originX.displayName"),
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.originX.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultOriginX),
                    PreviewProperty.createProperty(this,
                                                   PreviewProperty.LEGEND_ORIGIN_Y,
                                                   Float.class,
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.originY.displayName"),
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.originY.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultOriginY),
                    PreviewProperty.createProperty(this,
                                                   PreviewProperty.LEGEND_TITLE_IS_DISPLAYING,
                                                   Boolean.class,
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.title.isDisplaying.displayName"),
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.title.isDisplaying.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsDisplayingTitle),
                    PreviewProperty.createProperty(this,
                                                   PreviewProperty.LEGEND_TITLE_FONT,
                                                   Font.class,
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.title.font.displayName"),
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.title.font.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultTitleFont),
                    PreviewProperty.createProperty(this,
                                                   PreviewProperty.LEGEND_TITLE_FONT_COLOR,
                                                   Color.class,
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.title.font.color.displayName"),
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.title.font.color.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultTitleFontColor),
                    PreviewProperty.createProperty(this,
                                                   PreviewProperty.LEGEND_DESCRIPTION_IS_DISPLAYING,
                                                   Boolean.class,
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.description.isDisplaying.displayName"),
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.description.isDisplaying.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsDisplayingTitle),
                    PreviewProperty.createProperty(this,
                                                   PreviewProperty.LEGEND_DESCRIPTION_FONT,
                                                   Font.class,
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.description.font.displayName"),
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.description.font.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultDescriptionFont),
                    PreviewProperty.createProperty(this,
                                                   PreviewProperty.LEGEND_DESCRIPTION_FONT_COLOR,
                                                   Color.class,
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.description.font.color.displayName"),
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.description.font.color.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultDescriptionFontColor),
                    PreviewProperty.createProperty(this,
                                                   PreviewProperty.LEGEND_WIDTH,
                                                   Float.class,
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.width.displayName"),
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.width.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultWidth),
                    PreviewProperty.createProperty(this,
                                                   PreviewProperty.LEGEND_HEIGHT,
                                                   Float.class,
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.height.displayName"),
                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.height.description"),
                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultHeight)
                };
    }

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

    protected void readLegendPropertiesAndValues(Item item, PreviewProperties properties) {

        // DIMENSIONS
        width = properties.getIntValue(PreviewProperty.LEGEND_WIDTH);
        height = properties.getIntValue(PreviewProperty.LEGEND_HEIGHT);

        //TITLE
        isDisplayingTitle = properties.getBooleanValue(PreviewProperty.LEGEND_TITLE_IS_DISPLAYING);
        titleFont = properties.getFontValue(PreviewProperty.LEGEND_TITLE_FONT);
        titleFontColor = properties.getColorValue(PreviewProperty.LEGEND_TITLE_FONT_COLOR);
        title = item.getData(LegendItem.TITLE);

        //DESCRIPTION
        isDisplayingDescription = properties.getBooleanValue(PreviewProperty.LEGEND_DESCRIPTION_IS_DISPLAYING);
        descriptionFont = properties.getFontValue(PreviewProperty.LEGEND_DESCRIPTION_FONT);
        descriptionFontColor = properties.getColorValue(PreviewProperty.LEGEND_DESCRIPTION_FONT_COLOR);
        description = item.getData(LegendItem.DESCRIPTION);


        // ORIGIN
        float originX = properties.getFloatValue(PreviewProperty.LEGEND_ORIGIN_X);
        float originY = properties.getFloatValue(PreviewProperty.LEGEND_ORIGIN_Y);
        originTranslation = new AffineTransform();
        originTranslation.setToTranslation(originX, originY);
    }

    protected void renderSVG(SVGTarget target) {
        org.w3c.dom.Document document = target.getDocument();
        SVGGraphics2D graphics = new SVGGraphics2D(document);
        renderToGraphics(graphics, originTranslation, width, height);

        //appending
        org.w3c.dom.Element svgRoot = document.getDocumentElement();
        svgRoot.appendChild(graphics.getRoot());
    }

    protected void renderPDF(PDFTarget target) {
        PdfContentByte contentByte = target.getContentByte();
        com.itextpdf.text.Document pdfDocument = contentByte.getPdfDocument();
        contentByte.saveState();
        Graphics2D graphics2D = contentByte.createGraphics(pdfDocument.getPageSize().getWidth(), pdfDocument.getPageSize().getHeight());
        renderToGraphics(graphics2D, originTranslation, width, height);
        graphics2D.dispose();
        contentByte.restoreState();
    }

    protected void renderProcessing(ProcessingTarget target) {
        Graphics2D graphics = (Graphics2D) ((PGraphicsJava2D) target.getGraphics()).g2;
        renderToGraphics(graphics, originTranslation, width, height);
    }

    public abstract void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, int width, int height);
    protected abstract void readOwnPropertiesAndValues(Item item, PreviewProperties properties);

}
