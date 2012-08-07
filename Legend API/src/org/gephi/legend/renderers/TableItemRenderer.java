/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

/**
 *
 * @author edubecks
 */
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
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

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

    private void createVerticalText(Graphics2D graphics2D, AffineTransform affineTransform, Integer width, Integer height) {

        // diagonal shift
        Integer diagonalShift = (int) (cellSizeWidth * Math.cos(verticalTextDirection.rotationAngle()));

        Integer fontSize = font.getSize();

        //font
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setColor(fontColor);
        graphics2D.setFont(font);

        //metrics
        FontMetrics metrics = graphics2D.getFontMetrics();

        //overriding centerdistance
        int centerDistance = (cellSizeWidth - metrics.getHeight()) / 2;




        switch (verticalTextDirection) {
            case UP: {
                //rotating
                affineTransform.translate(0, height - verticalExtraMargin);
                affineTransform.rotate(verticalTextDirection.rotationAngle());
                graphics2D.setTransform(affineTransform);


                for (int i = 0; i < verticalLabels.size(); i++) {
                    String label = verticalLabels.get(i).toString();
                    graphics2D.setColor(verticalColors.get(i));
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


                for (int i = 0; i < verticalLabels.size(); i++) {
                    String label = verticalLabels.get(i).toString();
                    graphics2D.setColor(verticalColors.get(i));
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


                for (int i = 0; i < verticalLabels.size(); i++) {
                    String label = verticalLabels.get(i).toString();
                    graphics2D.setColor(verticalColors.get(i));
                    graphics2D.drawString(label,
                                          minimumMargin + ((i) * diagonalShift) + centerDistance + horizontalExtraMargin,
                                          (int) (i * diagonalShift) + fontSize - centerDistance + horizontalExtraMargin);
                }

                break;
            }
            case HORIZONTAL:{

                
                for (int i = 0; i < verticalLabels.size(); i++) {
                    String label = verticalLabels.get(i).toString();
                    graphics2D.setColor(verticalColors.get(i));
                    graphics2D.drawString(label,
                                          max,
                                          metrics.getHeight());

                }
            }
        }



    }

    private void createHorizontalText(Graphics2D graphics, AffineTransform arrangeTranslation, Integer width, Integer height) {


        //arrange
        graphics.setTransform(arrangeTranslation);

        int fontSize = font.getSize();
        int horizontalCenterDistance = (cellSizeHeight - fontSize) / 2;



        FontMetrics metrics = graphics.getFontMetrics(font);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setColor(Color.WHITE);
        for (int i = 0; i < horizontalLabels.size(); i++) {
            String label = horizontalLabels.get(i).toString();
            graphics.setColor(horizontalColors.get(i));

            switch (horizontalTextAlignment) {
                case RIGHT: {
                    graphics.drawString(label,
                                        width - metrics.stringWidth(label) - minimumMargin,
                                        (int) (i * cellSizeHeight) + fontSize + horizontalCenterDistance);
                    break;
                }
                case LEFT: {
                    graphics.drawString(label,
                                        minimumMargin,
                                        (int) (i * cellSizeHeight) + fontSize + horizontalCenterDistance);
                    break;
                }
            }
        }
    }

    private void createTableImage(Graphics2D graphics, AffineTransform arrangeTranslation) {
        //drawing background 

        //arrange
        graphics.setTransform(arrangeTranslation);


        graphics.setColor(Color.WHITE);
        for (int i = 0; i < tableValues.length; i++) {

            for (int j = 0; j < tableValues[i].length; j++) {

                graphics.setColor(valueColors.get(i).get(j));

                int x1, y1, x2, y2;

                if (isCellColoring) {
                    switch (cellColoringDirection) {
                        case UP:
                            x1 = j * cellSizeWidth;
                            y2 = (int) (cellSizeHeight * tableValues[i][j]) - 1;
                            y1 = ((i + 1) * cellSizeHeight) - y2;
                            x2 = cellSizeWidth - 1;
//                        System.out.printf("(%d,%d)->(%d,%d)\n", x1, y1, x1 + x2, y1 + y2);
                            graphics.fillRect(x1, y1, x2, y2);
                            break;
                    }
                }
                else {
                    x1 = j * cellSizeWidth;
                    y2 = (int) (cellSizeHeight) - 1;
                    y1 = ((i + 1) * cellSizeHeight) - y2;
                    x2 = cellSizeWidth - 1;
                    legendDrawText(graphics, tableValues[i][j] + "", font, fontColor, x1, y1, x2, y2, Alignment.CENTER);
                }

                // GRID DISPLAYING
                if (isDisplayingGrid) {
                    x1 = j * cellSizeWidth;
                    y2 = (int) (cellSizeHeight) - 1;
                    y1 = ((i + 1) * cellSizeHeight) - y2;
                    x2 = cellSizeWidth - 1;
                    graphics.setColor(gridColor);
                    graphics.drawRect(x1, y1, x2, y2);
                }

            }
        }

    }

    @Override
    public void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        graphics2D.setFont(font);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();

        int maxHorizontalTextWidth = Integer.MIN_VALUE;
        for (StringBuilder label : horizontalLabels) {
            maxHorizontalTextWidth = Math.max(maxHorizontalTextWidth, fontMetrics.stringWidth(label.toString()));
        }
        int maxVerticalTextWidth = Integer.MIN_VALUE;
        for (StringBuilder label : verticalLabels) {
            maxVerticalTextWidth = Math.max(maxVerticalTextWidth, fontMetrics.stringWidth(label.toString()));
        }

        int maxTextHeight = fontMetrics.getHeight();



        Integer horizontalTextWidth = maxHorizontalTextWidth + 2 * minimumMargin;
        Integer verticalTextHeight = maxHorizontalTextWidth + 2 * minimumMargin;
        int tempTableWidth = (int) (width - horizontalExtraMargin - minimumMargin - horizontalTextWidth - maxHorizontalTextWidth * Math.abs(Math.cos(verticalTextDirection.rotationAngle())));
        System.out.println("@Var: tempTableWidth: " + tempTableWidth);
        int tempTableHeight = (int) (height - verticalExtraMargin - minimumMargin - verticalTextHeight);
        System.out.println("@Var: tempTableHeight: " + tempTableHeight);



        if (verticalTextDirection == TableItem.VerticalTextDirection.HORIZONTAL) {
            cellSizeWidth = maxVerticalTextWidth;
        }
        else {
            cellSizeWidth = (int) (Math.floor(tempTableWidth / verticalLabels.size()));
        }
        System.out.println("@Var: cellSizeWidth: " + cellSizeWidth);
        cellSizeHeight = (int) (Math.floor(tempTableHeight / horizontalLabels.size()));
        System.out.println("@Var: cellSizeHeight: " + cellSizeHeight);



        Integer diagonalShift = (int) (cellSizeWidth * Math.cos(verticalTextDirection.rotationAngle()));

        System.out.println("@Var: horizontalTextWidth: " + horizontalTextWidth);
        Integer horizontalTextHeight = cellSizeHeight * labels.size();
        System.out.println("@Var: horizontalTextHeight: " + horizontalTextHeight);
        System.out.println("@Var: verticalTextHeight: " + verticalTextHeight);
        Integer verticalTextWidth = cellSizeWidth * labels.size();
        System.out.println("@Var: verticalTextWidth: " + verticalTextWidth);

        AffineTransform arrangeTranslation = new AffineTransform();
        arrangeTranslation.setTransform(origin);
        arrangeTranslation.translate(horizontalTextWidth, 0);
//            graphics.setTransform(translateTransform);

        createVerticalText(graphics2D, arrangeTranslation, verticalTextWidth, verticalTextHeight);


        arrangeTranslation.setTransform(origin);
        arrangeTranslation.translate(0, verticalTextHeight);
        createHorizontalText(graphics2D, arrangeTranslation, horizontalTextWidth, horizontalTextHeight);

        if (isCellColoring) {
            normalizeWithMinValueZero(tableValues);
        }
        arrangeTranslation.setTransform(origin);
        arrangeTranslation.translate(horizontalTextWidth, verticalTextHeight);
        createTableImage(graphics2D, arrangeTranslation);
    }

    private void normalize(Float[][] table, float minValue, float maxValue) {
        float range = maxValue - minValue;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = (table[i][j] - minValue) / (range);
            }
        }
    }

    private void normalizeWithMinValueZero(Float[][] table) {
        float minValue = 0f;
        float maxValue = Float.MIN_VALUE;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                maxValue = Math.max(maxValue, table[i][j]);
            }
        }
        normalize(table, minValue, maxValue);
    }

    private void normalize(Float[][] table) {
        float minValue = Float.MAX_VALUE;
        float maxValue = Float.MIN_VALUE;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                maxValue = Math.max(maxValue, table[i][j]);
                minValue = Math.min(minValue, table[i][j]);
            }
        }
        normalize(table, minValue, maxValue);
    }

    @Override
    public void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        tableValuesArrayList = item.getData(TableItem.TABLE_VALUES);
        System.out.println("@Var: tableValuesArrayList: " + tableValuesArrayList);

        // READING VALUES
        tableValues = new Float[tableValuesArrayList.size()][tableValuesArrayList.get(0).size()];
        for (int i = 0; i < tableValuesArrayList.size(); i++) {
            for (int j = 0; j < tableValuesArrayList.get(i).size(); j++) {
                tableValues[i][j] = tableValuesArrayList.get(i).get(j);
                System.out.println("@Var: tableValues: " + tableValues[i][j]);
            }
        }

        // READING COLORS
        valueColors = item.getData(TableItem.COLOR_VALUES);
        verticalColors = item.getData(TableItem.COLOR_VERTICAL);
        horizontalColors = item.getData(TableItem.COLOR_HORIZONTAL);


        // READING LABELS
        labels = item.getData(TableItem.LABELS_IDS);
        for (int i = 0; i < labels.size(); i++) {
            StringBuilder label = labels.get(i);
            String newLabel = properties.getStringValue(TableProperty.getLabelProperty(itemIndex, i));
            label.replace(0, newLabel.length(), newLabel);
        }

        horizontalLabels = item.getData(TableItem.HORIZONTAL_LABELS);
        verticalLabels = item.getData(TableItem.VERTICAL_LABELS);


        //properties
        font = properties.getFontValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_FONT));
        fontColor = properties.getColorValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_FONT_COLOR));
        isCellColoring = properties.getBooleanValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_IS_CELL_COLORING));
        verticalExtraMargin = properties.getIntValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_VERTICAL_EXTRA_MARGIN));
        horizontalExtraMargin = properties.getIntValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_HORIZONTAL_EXTRA_MARGIN));

        //grid
        gridColor = properties.getColorValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_FONT_COLOR));
        isDisplayingGrid = properties.getBooleanValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_IS_DISPLAYING_GRID));

        horizontalTextAlignment = (Alignment) properties.getValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_HORIZONTAL_TEXT_ALIGNMENT));
        verticalTextAlignment = (Alignment) properties.getValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_VERTICAL_TEXT_ALIGNMENT));
        verticalTextPosition = (Direction) properties.getValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_VERTICAL_TEXT_POSITION));
        horizontalTextPosition = (Direction) properties.getValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_HORIZONTAL_TEXT_POSITION));
        cellColoringDirection = (Direction) properties.getValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_CELL_COLORING_DIRECTION));
        verticalTextDirection = (TableItem.VerticalTextDirection) properties.getValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_VERTICAL_TEXT_ROTATION));




    }

    private Font font;
    private Color fontColor;
    private ArrayList<StringBuilder> labels;
    private ArrayList<StringBuilder> horizontalLabels;
    private ArrayList<StringBuilder> verticalLabels;
    private ArrayList<Color> horizontalColors;
    private ArrayList<Color> verticalColors;
    private ArrayList<ArrayList<Color>> valueColors;
    private Integer cellSizeWidth;
    private Integer cellSizeHeight;
    private Boolean isCellColoring;
    private Direction cellColoringDirection;
    private Direction horizontalTextPosition;
    private Alignment horizontalTextAlignment;
    private Direction verticalTextPosition;
    private Alignment verticalTextAlignment;
    private TableItem.VerticalTextDirection verticalTextDirection;
    private Integer verticalExtraMargin;
    private Integer horizontalExtraMargin;
    private Integer minimumMargin = 3;
    private ArrayList<ArrayList<Float>> tableValuesArrayList;
    private Float[][] tableValues;
    // Grid
    private Boolean isDisplayingGrid;
    private Color gridColor;
}
