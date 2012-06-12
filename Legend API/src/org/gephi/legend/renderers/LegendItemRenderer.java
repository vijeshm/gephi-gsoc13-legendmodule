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
import java.util.ArrayList;
import org.apache.batik.svggen.SVGGraphics2D;
import org.gephi.legend.api.LegendItem;
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
        
        
        ArrayList<String> legendProperties = LegendManager.getProperties(LegendProperty.LEGEND_PROPERTIES);
        
        return new PreviewProperty[0];

//        return new PreviewProperty[]{
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.ORIGIN_X),
//                                                   Float.class,
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.originX.displayName"),
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.originX.description"),
//                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultOriginX),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.ORIGIN_Y),
//                                                   Float.class,
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.originY.displayName"),
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.originY.description"),
//                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultOriginY),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.TITLE_IS_DISPLAYING),
//                                                   Boolean.class,
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.title.isDisplaying.displayName"),
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.title.isDisplaying.description"),
//                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsDisplayingTitle),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.TITLE_FONT),
//                                                   Font.class,
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.title.font.displayName"),
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.title.font.description"),
//                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultTitleFont),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.TITLE_FONT_COLOR),
//                                                   Color.class,
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.title.font.color.displayName"),
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.title.font.color.description"),
//                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultTitleFontColor),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.DESCRIPTION_IS_DISPLAYING),
//                                                   Boolean.class,
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.description.isDisplaying.displayName"),
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.description.isDisplaying.description"),
//                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsDisplayingTitle),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.DESCRIPTION_FONT),
//                                                   Font.class,
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.description.font.displayName"),
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.description.font.description"),
//                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultDescriptionFont),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.DESCRIPTION_FONT_COLOR),
//                                                   Color.class,
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.description.font.color.displayName"),
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.description.font.color.description"),
//                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultDescriptionFontColor),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.WIDTH),
//                                                   Float.class,
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.width.displayName"),
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.width.description"),
//                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultWidth),
//                    PreviewProperty.createProperty(this,
//                                                   legendProperties.get(LegendProperty.HEIGHT),
//                                                   Float.class,
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.height.displayName"),
//                                                   NbBundle.getMessage(TextItemRenderer.class, "LegendItemRenderer.property.height.description"),
//                                                   PreviewProperty.CATEGORY_LEGENDS).setValue(defaultHeight)
//                };
    }
    //INDEX
    protected int legendIndex;
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
        
        

        // DIMENSIONS
        width = properties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES,legendIndex, LegendProperty.WIDTH));
//        width = properties.getIntValue(PreviewProperty.WIDTH);
//        height = properties.getIntValue(PreviewProperty.HEIGHT);
        height = properties.getIntValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES,legendIndex, LegendProperty.HEIGHT));

        //TITLE
        isDisplayingTitle = properties.getBooleanValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES,legendIndex, LegendProperty.TITLE_IS_DISPLAYING));
//        isDisplayingTitle = properties.getBooleanValue(PreviewProperty.TITLE_IS_DISPLAYING);
        titleFont = properties.getFontValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES,legendIndex, LegendProperty.TITLE_FONT));
//        titleFont = properties.getFontValue(PreviewProperty.TITLE_FONT);
        titleFontColor = properties.getColorValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES,legendIndex, LegendProperty.TITLE_FONT_COLOR));
//        titleFontColor = properties.getColorValue(PreviewProperty.TITLE_FONT_COLOR);
        title = item.getData(LegendItem.TITLE);

        //DESCRIPTION
        isDisplayingDescription = properties.getBooleanValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES,legendIndex, LegendProperty.DESCRIPTION_IS_DISPLAYING));
//        isDisplayingDescription = properties.getBooleanValue(PreviewProperty.DESCRIPTION_IS_DISPLAYING);
        descriptionFont = properties.getFontValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES,legendIndex, LegendProperty.DESCRIPTION_FONT));
//        descriptionFont = properties.getFontValue(PreviewProperty.DESCRIPTION_FONT);
        descriptionFontColor = properties.getColorValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES,legendIndex, LegendProperty.DESCRIPTION_FONT_COLOR));
//        descriptionFontColor = properties.getColorValue(PreviewProperty.DESCRIPTION_FONT_COLOR);
        description = item.getData(LegendItem.DESCRIPTION);


        // ORIGIN
        float originX = properties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES,legendIndex, LegendProperty.ORIGIN_X));
//        float originX = properties.getFloatValue(PreviewProperty.ORIGIN_X);
        float originY = properties.getFloatValue(LegendManager.getProperty(LegendProperty.LEGEND_PROPERTIES,legendIndex, LegendProperty.ORIGIN_Y));
//        float originY = properties.getFloatValue(PreviewProperty.ORIGIN_Y);
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
    
    

}
