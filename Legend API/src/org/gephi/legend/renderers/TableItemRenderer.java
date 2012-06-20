/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

/**
 *
 * @author edubecks
 */
import com.itextpdf.text.pdf.PdfContentByte;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendItem.Alignment;
import org.gephi.legend.api.LegendItem.Direction;
import org.gephi.legend.api.LegendManager;
import org.gephi.legend.properties.TableProperty;
import org.gephi.legend.items.TableItem;
import org.gephi.legend.builders.TableItemBuilder;
import org.gephi.legend.items.TableItem.VerticalTextDirection;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;

@ServiceProvider(service = Renderer.class, position = 501)
public class TableItemRenderer extends LegendItemRenderer {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(TableItemRenderer.class, "TableItemRenderer.name");
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        return item instanceof TableItem;
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof TableItemBuilder;
    }

    private void renderProcessing(TableItem tableItem, ProcessingTarget target, PreviewProperties properties) {
        PGraphics pGraphics = target.getGraphics();



        //labels
//        ArrayList<String> labels = tableItem.getData(TableItem.LABELS);
//
//        Integer cellSizeWidth = tableItem.getData(TableItem.CELL_SIZE_WIDTH);
//        Integer cellSizeHeight = tableItem.getData(TableItem.CELL_SIZE_HEIGHT);
//
//        TableItem.VerticalTextDirection verticalTextDirection = tableItem.getData(TableItem.VERTICAL_TEXT_DIRECTION);
//
//        Integer MINIMUM_MARGIN = tableItem.getData(TableItem.MINIMUM_MARGIN);

        Graphics2D graphics = (Graphics2D) ((PGraphicsJava2D) target.getGraphics()).g2;


        FontMetrics fontMetrics = graphics.getFontMetrics();
        int maxLength = fontMetrics.stringWidth(longestLabel(labels));

        Integer diagonalShift = (int) (cellSizeWidth * Math.cos(verticalTextDirection.rotationAngle()));

        Integer horizontalTextWidth = maxLength + 2 * minimumMargin;
        Integer horizontalTextHeight = cellSizeHeight * labels.size();
        Integer verticalTextHeight = maxLength + 2 * minimumMargin;
        Integer verticalTextWidth = cellSizeWidth * labels.size();

        AffineTransform origin = new AffineTransform();
        origin.setToTranslation(100, 100);

        createImage(tableItem, graphics, origin, horizontalTextWidth, horizontalTextHeight, verticalTextWidth, verticalTextHeight);
    }

    public void createImage(TableItem tableItem, Graphics2D graphics, AffineTransform origin, int horizontalTextWidth, int horizontalTextHeight, int verticalTextWidth, int verticalTextHeight) {

        AffineTransform arrangeTranslation = new AffineTransform();
        arrangeTranslation.setTransform(origin);
        arrangeTranslation.translate(horizontalTextWidth, 0);
//            graphics.setTransform(translateTransform);

        createVerticalText(graphics, arrangeTranslation, verticalTextWidth, verticalTextHeight);


//        arrangeTranslation.setTransform(origin);
//        arrangeTranslation.translate(0, verticalTextHeight);
//        createHorizontalText(graphics, arrangeTranslation, horizontalTextWidth, horizontalTextHeight);
//
//
//        arrangeTranslation.setTransform(origin);
//        arrangeTranslation.translate(horizontalTextWidth, verticalTextHeight);
//        createTableImage(graphics, arrangeTranslation);

    }

    public void createVerticalText(Graphics2D graphics2D, AffineTransform affineTransform, Integer width, Integer height) {


//        Float[][] tableValues = tableItem.getData(TableItem.TABLE_VALUES);
//        Boolean isCellColoring = tableItem.getData(TableItem.IS_CELL_COLORING);
//
//        Color BACKGROUND = tableItem.getData(TableItem.BACKGROUND);
//        TableItem.VerticalTextDirection verticalTextDirection = tableItem.getData(TableItem.VERTICAL_TEXT_DIRECTION);
//
//
//        Integer cellSizeWidth = tableItem.getData(TableItem.CELL_SIZE_WIDTH);
//        Integer cellSizeHeight = tableItem.getData(TableItem.CELL_SIZE_HEIGHT);
//
//        //margins
//        Integer verticalExtraMargin = tableItem.getData(TableItem.VERTICAL_EXTRA_MARGIN);
//        Integer horizontalExtraAlignment = tableItem.getData(TableItem.HORIZONTAL_EXTRA_ALIGNMENT);
//        Integer minimumMargin = tableItem.getData(TableItem.MINIMUM_MARGIN);

        // diagonal shift
        Integer diagonalShift = (int) (cellSizeWidth * Math.cos(verticalTextRotation));

        Integer fontSize = font.getSize();

        //font
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setColor(fontColor);
        graphics2D.setFont(font);

        //metrics
        FontMetrics metrics = graphics2D.getFontMetrics();

        //margin
        int centerDistance = (cellSizeWidth - metrics.getHeight()) / 2;

        //overriding centerdistance
        centerDistance = (int) ((cellSizeWidth - fontSize * Math.cos(verticalTextRotation)) / 2);

        //vertical shift for Diagonal case
        int verticalShift = -(int) (height * Math.sin(verticalTextRotation));
        affineTransform.translate(0, verticalShift + diagonalShift - verticalExtraMargin);
        affineTransform.rotate(verticalTextRotation);
        graphics2D.setTransform(affineTransform);


        for (int i = 0; i < labels.size(); i++) {
            String label = labels.get(i);
            graphics2D.drawString(label,
                                  minimumMargin + ((i) * diagonalShift) + centerDistance + horizontalExtraMargin,
                                  (int) (i * diagonalShift) + fontSize - centerDistance + horizontalExtraMargin);
        }

//                
//
//        switch (verticalTextDirection) {
//            case UP: {
//                //rotating
//                affineTransform.translate(0, height - verticalExtraMargin);
//                affineTransform.rotate(verticalTextDirection.rotationAngle());
//                graphics2D.setTransform(affineTransform);
//
//
//                for (int i = 0; i < labels.size(); i++) {
//                    String label = labels.get(i);
//                    graphics2D.legendDrawString(label,
//                                          minimumMargin,
//                                          i * cellSizeWidth + fontSize + centerDistance);
//
//                }
//
//
//                break;
//            }
//            case DOWN: {
//
//                affineTransform.translate(0, height - verticalExtraMargin);
//                affineTransform.rotate(verticalTextDirection.rotationAngle());
//                graphics2D.setTransform(affineTransform);
//
//
//                for (int i = 0; i < labels.size(); i++) {
//                    String label = labels.get(i);
//                    graphics2D.legendDrawString(label,
//                                          height - metrics.stringWidth(label) - minimumMargin,
//                                          i * cellSizeWidth + fontSize + centerDistance);
//
//                }
//
//
//                break;
//            }
//            case DIAGONAL: {
//
//                //overriding centerdistance
//                centerDistance = (int) ((cellSizeWidth - fontSize * Math.cos(verticalTextRotation)) / 2);
//
//                //vertical shift for Diagonal case
//                int verticalShift = -(int) (height * Math.sin(verticalTextRotation));
//                affineTransform.translate(0, verticalShift + diagonalShift - verticalExtraMargin);
//                affineTransform.rotate(verticalTextDirection.rotationAngle());
//                graphics2D.setTransform(affineTransform);
//
//
//                for (int i = 0; i < labels.size(); i++) {
//                    String label = labels.get(i);
//                    graphics2D.legendDrawString(label,
//                                          minimumMargin + ((i) * diagonalShift) + centerDistance + horizontalExtraMargin,
//                                          (int) (i * diagonalShift) + fontSize - centerDistance + horizontalExtraMargin);
//                }
//
//                break;
//            }
//        }



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

    @Override
    public void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int maxTextLength = fontMetrics.stringWidth(longestLabel(labels));
        int maxTextHeight = fontMetrics.getHeight();

//        ArrayList<Color> listOfColors = tableItem.getData(TableItem.LIST_OF_COLORS);


        int tempTableWidth = (int) (width - minimumMargin - maxTextLength - maxTextLength * Math.cos(verticalTextRotation));
        int tempTableHeight = (int) (height - minimumMargin - maxTextLength * Math.sin(verticalTextRotation));

//        int tempTableWidth = 0, tempTableHeight = 0;
//        if (verticalTextDirection == VerticalTextDirection.DIAGONAL) {
//
//            tempTableWidth = (int) (width - minimumMargin - maxTextLength - maxTextLength * Math.cos(verticalTextDirection.rotationAngle()));
//            tempTableHeight = (int) (height - minimumMargin - maxTextLength * Math.sin(verticalTextDirection.rotationAngle()));
//        }
//        else {
//            tempTableWidth = width - maxTextLength - minimumMargin;
//            tempTableHeight = height - maxTextLength - minimumMargin;
//        }


        cellSizeWidth = (int) (Math.floor(tempTableWidth / labels.size()));
        System.out.println("@Var: cellSizeWidth: " + cellSizeWidth);
        cellSizeHeight = (int) (Math.floor(tempTableHeight / labels.size()));
        System.out.println("@Var: cellSizeHeight: " + cellSizeHeight);



        Integer diagonalShift = (int) (cellSizeWidth * Math.cos(verticalTextRotation));

        Integer horizontalTextWidth = maxTextLength + 2 * minimumMargin;
        Integer horizontalTextHeight = cellSizeHeight * labels.size();
        Integer verticalTextHeight = maxTextLength + 2 * minimumMargin;
        Integer verticalTextWidth = cellSizeWidth * labels.size();

        AffineTransform arrangeTranslation = new AffineTransform();
        arrangeTranslation.setTransform(origin);
        arrangeTranslation.translate(horizontalTextWidth, 0);
//            graphics.setTransform(translateTransform);

        createVerticalText(graphics2D, arrangeTranslation, verticalTextWidth, verticalTextHeight);


//        arrangeTranslation.setTransform(origin);
//        arrangeTranslation.translate(0, verticalTextHeight);
//        createHorizontalText(graphics, arrangeTranslation, horizontalTextWidth, horizontalTextHeight);
//
//
//        arrangeTranslation.setTransform(origin);
//        arrangeTranslation.translate(horizontalTextWidth, verticalTextHeight);
//        createTableImage(graphics, arrangeTranslation);
    }

    private Font font;
    private Color fontColor;
    private ArrayList<String> labels;
    private Integer cellSizeWidth;
    private Integer cellSizeHeight;
    private Boolean isCellColoring;
    private Direction cellColoringDirection;
    private Direction horizontalTextPosition;
    private Alignment horizontalTextAlignment;
    private Direction verticalTextPosition;
    private Alignment verticalTextAlignment;
    private Float verticalTextRotation;
    private TableItem.VerticalTextDirection verticalTextDirection;
    private Integer verticalExtraMargin;
    private Integer horizontalExtraMargin;
    private Integer minimumMargin = 3;
    private Float[][] tableValues;

    @Override
    public void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        //values
        labels = item.getData(TableItem.LABELS);
        tableValues = item.getData(TableItem.TABLE_VALUES);

        //properties
        font = properties.getFontValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_FONT));
        fontColor = properties.getColorValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_FONT_COLOR));
        isCellColoring = properties.getBooleanValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_IS_CELL_COLORING));
        verticalExtraMargin = properties.getIntValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_VERTICAL_EXTRA_MARGIN));
        horizontalExtraMargin = properties.getIntValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_HORIZONTAL_EXTRA_MARGIN));

        horizontalTextAlignment = (Alignment) properties.getValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_HORIZONTAL_TEXT_ALIGNMENT));
        verticalTextAlignment = (Alignment) properties.getValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_VERTICAL_TEXT_ALIGNMENT));
        verticalTextPosition = (Direction) properties.getValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_VERTICAL_TEXT_POSITION));
        horizontalTextPosition = (Direction) properties.getValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_HORIZONTAL_TEXT_POSITION));
        cellColoringDirection = (Direction) properties.getValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_CELL_COLORING_DIRECTION));
        verticalTextRotation = properties.getFloatValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_VERTICAL_TEXT_ROTATION));
        verticalTextRotation = (float) Math.toRadians(verticalTextRotation);




    }

}
