/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

/**
 *
 * @author edubecks
 */
import com.itextpdf.text.pdf.PdfContentByte;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.w3c.dom.Element;
import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;

@ServiceProvider(service = TableItemRenderer.class, position = 10)
public class TableItemRenderer implements org.gephi.preview.spi.Renderer {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(TableItemRenderer.class, "localized.name.key");
    }

    @Override
    public void preProcess(PreviewModel previewModel) {
    }

    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {
        TableItem tableItem = (TableItem) item;

        //font
        //font
        Integer fontSize = tableItem.getData(TableItem.FONT_SIZE);
        String fontType = tableItem.getData(TableItem.FONT_TYPE);
        Integer fontStyle = tableItem.getData(TableItem.FONT_STYLE);
        Font font = new Font(fontType, fontStyle, fontSize);

        //labels
        ArrayList<String> labels = tableItem.getData(TableItem.LABELS);

        Integer cellSizeWidth = tableItem.getData(TableItem.CELL_SIZE_WIDTH);
        Integer cellSizeHeight = tableItem.getData(TableItem.CELL_SIZE_HEIGHT);


        TableItem.VerticalTextDirection verticalTextDirection = tableItem.getData(TableItem.VERTICAL_TEXT_DIRECTION);


        Integer MINIMUM_MARGIN = tableItem.getData(TableItem.MINIMUM_MARGIN);


        Graphics2D graphics = null;



        if (target instanceof ProcessingTarget) {
            graphics = getGraphicsFromRendeting((ProcessingTarget) target);
        }
        else if (target instanceof SVGTarget) {
            renderSVG(tableItem, (SVGTarget) target, properties);
        }
        else if (target instanceof PDFTarget) {
            renderPDF(tableItem, (PDFTarget) target, properties);
        }

        graphics.setFont(font);
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int maxLength = fontMetrics.stringWidth(longestLabel(labels));





        Integer diagonalShift = (int) (cellSizeWidth * Math.cos(verticalTextDirection.rotationAngle()));



        Integer horizontalTextWidth = maxLength + 2 * MINIMUM_MARGIN;
        Integer horizontalTextHeight = cellSizeHeight * labels.size();
        Integer verticalTextHeight = maxLength + 2 * MINIMUM_MARGIN;
        Integer verticalTextWidth = cellSizeWidth * labels.size();

        AffineTransform origin = new AffineTransform();
        origin.setToTranslation(100, 100);

        createImage(tableItem, graphics, origin, horizontalTextWidth, horizontalTextHeight, verticalTextWidth, verticalTextHeight);


    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        return item instanceof TableItem;
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof TableItemBuilder;
    }

    private Graphics2D getGraphicsFromRendeting(ProcessingTarget target) {
        return (Graphics2D) ((PGraphicsJava2D) target.getGraphics()).g2;
    }

    private void renderProcessing(TableItem item, ProcessingTarget target, PreviewProperties properties) {
        PGraphics graphics = target.getGraphics();
        //Render here with processing graphics
    }

    private void renderSVG(TableItem tableItem, SVGTarget target, PreviewProperties properties) {
    }

    private void renderPDF(TableItem tableItem, PDFTarget target, PreviewProperties properties) {
        PdfContentByte cb = target.getContentByte();
        //Render here with PdfContentByte in a similar way as processing and svg
    }

    public void createImage(TableItem tableItem, Graphics2D graphics, AffineTransform origin, int horizontalTextWidth, int horizontalTextHeight, int verticalTextWidth, int verticalTextHeight) {

        AffineTransform arrangeTranslation = new AffineTransform();
        arrangeTranslation.setTransform(origin);
        arrangeTranslation.translate(horizontalTextWidth, 0);
//            graphics.setTransform(translateTransform);

        createVerticalText(tableItem, graphics, arrangeTranslation, verticalTextWidth, verticalTextHeight);


//        arrangeTranslation.setTransform(origin);
//        arrangeTranslation.translate(0, verticalTextHeight);
//        createHorizontalText(graphics, arrangeTranslation, horizontalTextWidth, horizontalTextHeight);
//
//
//        arrangeTranslation.setTransform(origin);
//        arrangeTranslation.translate(horizontalTextWidth, verticalTextHeight);
//        createTableImage(graphics, arrangeTranslation);

    }

    public void createVerticalText(TableItem tableItem, Graphics2D graphics, AffineTransform affineTransform, int width, int height) {

    int fontSize = tableItem.getData(TableItem.FONT_SIZE);
        String fontType = tableItem.getData(TableItem.FONT_TYPE);
        Integer fontStyle = tableItem.getData(TableItem.FONT_STYLE);

        ArrayList<String> labels = tableItem.getData(TableItem.LABELS);
        Float[][] tableValues = tableItem.getData(TableItem.TABLE_VALUES);
        Boolean isCellColoring = tableItem.getData(TableItem.IS_CELL_COLORING);

        Color BACKGROUND = tableItem.getData(TableItem.BACKGROUND);
        ArrayList<Color> listOfColors = tableItem.getData(TableItem.LIST_OF_COLORS);
        TableItem.Direction horizontalAlignment = tableItem.getData(TableItem.HORIZONTAL_ALIGNMENT);
        TableItem.Direction horizontalTextAlignment = tableItem.getData(TableItem.HORIZONTAL_TEXT_ALIGNMENT);
        TableItem.Direction verticalAlignment = tableItem.getData(TableItem.VERTICAL_ALIGNMENT);
        TableItem.Direction cellColoring = tableItem.getData(TableItem.CELL_COLORING);
        TableItem.VerticalTextDirection verticalTextDirection = tableItem.getData(TableItem.VERTICAL_TEXT_DIRECTION);
        
        
        Integer cellSizeWidth = tableItem.getData(TableItem.CELL_SIZE_WIDTH);
        Integer cellSizeHeight = tableItem.getData(TableItem.CELL_SIZE_HEIGHT);

        Font font = tableItem.getData(TableItem.FONT);
        //margins
        Integer verticalExtraMargin = tableItem.getData(TableItem.VERTICAL_EXTRA_MARGIN);
        Integer horizontalExtraAlignment = tableItem.getData(TableItem.HORIZONTAL_EXTRA_ALIGNMENT);
        Integer minimumMargin = tableItem.getData(TableItem.MINIMUM_MARGIN);

        // diagonal shift
        Integer diagonalShift = (int) (cellSizeWidth * Math.cos(verticalTextDirection.rotationAngle()));



        //font
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                  RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setFont(font);
        graphics.setColor(Color.MAGENTA);

        //metrics
        FontMetrics metrics = graphics.getFontMetrics();

        //margin
        int centerDistance = (cellSizeWidth - metrics.getHeight()) / 2;

        switch (verticalTextDirection) {
            case UP: {
                //rotating
                affineTransform.translate(0, height - verticalExtraMargin);
                affineTransform.rotate(verticalTextDirection.rotationAngle());
                graphics.setTransform(affineTransform);


                for (int i = 0; i < labels.size(); i++) {
                    String label = labels.get(i);
                    graphics.drawString(label,
                                        minimumMargin,
                                        i * cellSizeWidth + fontSize + centerDistance);

                }


                break;
            }
            case DOWN: {

                affineTransform.translate(0, height - verticalExtraMargin);
                affineTransform.rotate(verticalTextDirection.rotationAngle());
                graphics.setTransform(affineTransform);


                for (int i = 0; i < labels.size(); i++) {
                    String label = labels.get(i);
                    graphics.drawString(label,
                                        height - metrics.stringWidth(label) - minimumMargin,
                                        i * cellSizeWidth + fontSize + centerDistance);

                }


                break;
            }
            case DIAGONAL: {

                //overriding centerdistance
                centerDistance = (int) ((cellSizeWidth - fontSize * Math.cos(verticalTextDirection.rotationAngle())) / 2);

                //vertical shift for Diagonal case
                int verticalShift = -(int) (height * Math.sin(verticalTextDirection.rotationAngle()));
                affineTransform.translate(0, verticalShift + diagonalShift - verticalExtraMargin);
                affineTransform.rotate(verticalTextDirection.rotationAngle());
                graphics.setTransform(affineTransform);


                for (int i = 0; i < labels.size(); i++) {
                    String label = labels.get(i);
                    graphics.drawString(label,
                                        minimumMargin + ((i) * diagonalShift) + centerDistance + horizontalExtraAlignment,
                                        (int) (i * diagonalShift) + fontSize - centerDistance + horizontalExtraAlignment);
                }

                break;
            }
        }



    }

    private String longestLabel(ArrayList<String> labels) {

        assert (labels.size() > 0) : "labels is empty";

        String maxLabel = labels.get(0);
        for (int i = 1; i < labels.size(); i++) {
            if (labels.get(i).length() > maxLabel.length()) {
                maxLabel = labels.get(i);
            }
        }
        return maxLabel;
    }

}
