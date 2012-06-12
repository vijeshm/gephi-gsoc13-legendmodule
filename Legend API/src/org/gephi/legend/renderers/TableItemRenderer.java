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

//@ServiceProvider(service = Renderer.class, position = 10)
public class TableItemRenderer extends LegendItemRenderer {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(TableItemRenderer.class, "TableItemRenderer.name");
    }

    @Override
    public void preProcess(PreviewModel previewModel) {
    }

    //default values
    protected final Integer defaultMinimumMargin = 3;
    protected final Integer defaultVerticalExtraMargin = 3;
    protected final Integer defaultHorizontalExtraMargin = 3;
    protected final Integer defaultCellSizeWidth = 40;
    protected final Integer defaultCellSizeHeight = 20;
    protected final Font defaultFont = new Font("Arial", Font.PLAIN, 13);
    protected final Color defaultFontColor = Color.BLACK;
    protected final Boolean defaultIsCellColoring = true;
    protected final TableItem.Direction defaultCellColoringDirection = TableItem.Direction.UP;
    protected final TableItem.Direction defaultHorizontalAlignment = TableItem.Direction.LEFT;
    protected final TableItem.Direction defaultHorizontalTextAlignment = TableItem.Direction.RIGHT;
    protected final TableItem.Direction defaultVerticalAlignment = TableItem.Direction.UP;
    protected final TableItem.VerticalTextDirection defaultVerticalTextDirection = TableItem.VerticalTextDirection.DIAGONAL;

    @Override
    public PreviewProperty[] getProperties() {


        this.legendIndex = LegendManager.useItemIndex();
        PreviewProperty[] legendProperties = createLegendProperties();
        System.out.printf("Creating Text Item:%d\n", legendIndex);

        ArrayList<String> tableProperties = LegendManager.getProperties(TableProperty.OWN_PROPERTIES);

        System.out.println("Creating property @Var: defaultFont: " + defaultFont);
        System.out.println("Creating property @Var: defaultFontColor: " + defaultFontColor);

        return new PreviewProperty[0];


//        PreviewProperty[] properties = {
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_VERTICAL_EXTRA_MARGIN),
//                                           Integer.class,
//                                           NbBundle.getMessage(TableItemRenderer.class, "TableItemRenderer.property.vertical.extraMargin.displayName"),
//                                           NbBundle.getMessage(TableItemRenderer.class, "TableItemRenderer.property.vertical.extraMargin.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultVerticalExtraMargin),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_HORIZONTAL_EXTRA_MARGIN),
//                                           Integer.class,
//                                           NbBundle.getMessage(TableItemRenderer.class, "TableItemRenderer.property.vertical.extraMargin.displayName"),
//                                           NbBundle.getMessage(TableItemRenderer.class, "TableItemRenderer.property.vertical.extraMargin.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultHorizontalExtraMargin),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_FONT),
//                                           Font.class,
//                                           NbBundle.getMessage(TableItemRenderer.class, "TableItemRenderer.property.font.displayName"),
//                                           NbBundle.getMessage(TableItemRenderer.class, "TableItemRenderer.property.font.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultFont),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_FONT_COLOR),
//                                           Color.class,
//                                           NbBundle.getMessage(TableItemRenderer.class, "TableItemRenderer.property.font.color.displayName"),
//                                           NbBundle.getMessage(TableItemRenderer.class, "TableItemRenderer.property.font.color.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultFontColor),
//            PreviewProperty.createProperty(this,
//                                           tableProperties.get(TableProperty.TABLE_IS_CELL_COLORING),
//                                           Boolean.class,
//                                           NbBundle.getMessage(TableItemRenderer.class, "TableItemRenderer.property.isCellColoring.displayName"),
//                                           NbBundle.getMessage(TableItemRenderer.class, "TableItemRenderer.property.isCellColoring.description"),
//                                           PreviewProperty.CATEGORY_LEGENDS).setValue(defaultIsCellColoring)
//        };
//
//
//        PreviewProperty[] previewProperties = new PreviewProperty[legendProperties.length + properties.length];
//        System.arraycopy(legendProperties, 0, previewProperties, 0, legendProperties.length);
//        System.arraycopy(properties, 0, previewProperties, legendProperties.length, properties.length);
//
//        return previewProperties;
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

    public void createVerticalText(Graphics2D graphics2D, AffineTransform affineTransform, int width, int height) {


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
        Integer diagonalShift = (int) (cellSizeWidth * Math.cos(verticalTextDirection.rotationAngle()));

        Integer fontSize = font.getSize();
        String fontType = font.getName();
        Integer fontStyle = font.getStyle();

        //font
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setColor(fontColor);
        graphics2D.setFont(font);

        //metrics
        FontMetrics metrics = graphics2D.getFontMetrics();

        //margin
        int centerDistance = (cellSizeWidth - metrics.getHeight()) / 2;

        switch (verticalTextDirection) {
            case UP: {
                //rotating
                affineTransform.translate(0, height - verticalExtraMargin);
                affineTransform.rotate(verticalTextDirection.rotationAngle());
                graphics2D.setTransform(affineTransform);


                for (int i = 0; i < labels.size(); i++) {
                    String label = labels.get(i);
                    graphics2D.drawString(label,
                                          minimumMargin,
                                          i * cellSizeWidth + fontSize + centerDistance);

                }


                break;
            }
            case DOWN: {

                affineTransform.translate(0, height - verticalExtraMargin);
                affineTransform.rotate(verticalTextDirection.rotationAngle());
                graphics2D.setTransform(affineTransform);


                for (int i = 0; i < labels.size(); i++) {
                    String label = labels.get(i);
                    graphics2D.drawString(label,
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
                graphics2D.setTransform(affineTransform);


                for (int i = 0; i < labels.size(); i++) {
                    String label = labels.get(i);
                    graphics2D.drawString(label,
                                          minimumMargin + ((i) * diagonalShift) + centerDistance + horizontalExtraMargin,
                                          (int) (i * diagonalShift) + fontSize - centerDistance + horizontalExtraMargin);
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

    @Override
    public void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, int width, int height) {
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int maxTextLength = fontMetrics.stringWidth(longestLabel(labels));
        int maxTextHeight = fontMetrics.getHeight();

//        ArrayList<Color> listOfColors = tableItem.getData(TableItem.LIST_OF_COLORS);
        horizontalAlignment = defaultHorizontalAlignment;
        horizontalTextAlignment = defaultHorizontalTextAlignment;
        verticalAlignment = defaultVerticalAlignment;
        cellColoringDirection = defaultCellColoringDirection;
        verticalTextDirection = defaultVerticalTextDirection;
        minimumMargin = defaultMinimumMargin;


        int tempTableWidth = 0, tempTableHeight = 0;
        if (verticalTextDirection == VerticalTextDirection.DIAGONAL) {

            tempTableWidth = (int) (width - minimumMargin - maxTextLength - maxTextLength * Math.cos(verticalTextDirection.rotationAngle()));
            tempTableHeight = (int) (height - minimumMargin - maxTextLength * Math.sin(verticalTextDirection.rotationAngle()));
        }
        else {
            tempTableWidth = width - maxTextLength - minimumMargin;
            tempTableHeight = height - maxTextLength - minimumMargin;
        }


        cellSizeWidth = (int) (Math.floor(tempTableWidth / labels.size()));
        System.out.println("@Var: cellSizeWidth: " + cellSizeWidth);
        cellSizeHeight = (int) (Math.floor(tempTableHeight / labels.size()));
        System.out.println("@Var: cellSizeHeight: " + cellSizeHeight);



        Integer diagonalShift = (int) (cellSizeWidth * Math.cos(verticalTextDirection.rotationAngle()));

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
    private TableItem.Direction cellColoringDirection;
    private TableItem.Direction horizontalAlignment;
    private TableItem.Direction horizontalTextAlignment;
    private TableItem.Direction verticalAlignment;
    private TableItem.VerticalTextDirection verticalTextDirection;
    private Integer verticalExtraMargin;
    private Integer horizontalExtraMargin;
    private Integer minimumMargin;
    private Integer horizontalExtraAlignment;
    private Float[][] tableValues;

    @Override
    public void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {

        //values
        labels = item.getData(TableItem.LABELS);
        tableValues = item.getData(TableItem.TABLE_VALUES);

        //properties
        font = properties.getFontValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, legendIndex, TableProperty.TABLE_FONT));
        System.out.println("@Var: font: " + font);
        fontColor = properties.getColorValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, legendIndex, TableProperty.TABLE_FONT_COLOR));
        System.out.println("@Var: fontColor: " + fontColor);
        isCellColoring = properties.getBooleanValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, legendIndex, TableProperty.TABLE_IS_CELL_COLORING));
        System.out.println("@Var: isCellColoring: " + isCellColoring);
        verticalExtraMargin = properties.getIntValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, legendIndex, TableProperty.TABLE_VERTICAL_EXTRA_MARGIN));
        System.out.println("@Var: verticalExtraMargin: " + verticalExtraMargin);
        horizontalExtraMargin = properties.getIntValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, legendIndex, TableProperty.TABLE_HORIZONTAL_EXTRA_MARGIN));
        System.out.println("@Var: horizontalExtraMargin: " + horizontalExtraMargin);


        // other properties
        minimumMargin = defaultMinimumMargin;
        System.out.println("@Var: minimumMargin: " + minimumMargin);
        cellColoringDirection = defaultCellColoringDirection;
        System.out.println("@Var: cellColoringDirection: " + cellColoringDirection);
        horizontalAlignment = defaultHorizontalAlignment;
        System.out.println("@Var: horizontalAlignment: " + horizontalAlignment);
        horizontalTextAlignment = defaultHorizontalTextAlignment;

        System.out.println("@Var: horizontalTextAlignment: " + horizontalTextAlignment);
        verticalAlignment = defaultVerticalAlignment;
        System.out.println("@Var: verticalAlignment: " + verticalAlignment);
        verticalTextDirection = defaultVerticalTextDirection;
        System.out.println("@Var: verticalTextDirection: " + verticalTextDirection);


    }

}
