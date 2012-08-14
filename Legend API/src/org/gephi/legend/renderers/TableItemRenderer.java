/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

/**
 *
 * @author edubecks
 */
import com.sun.java.swing.plaf.windows.WindowsTreeUI;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import org.gephi.legend.items.LegendItem;
import org.gephi.legend.items.LegendItem.Alignment;
import org.gephi.legend.items.LegendItem.Direction;
import org.gephi.legend.manager.LegendManager;
import org.gephi.legend.builders.TableItemBuilder;
import org.gephi.legend.items.TableItem;
import org.gephi.legend.properties.TableProperty;
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

    private void createVerticalText(Graphics2D graphics2D, AffineTransform arrangeTranslation, Integer width, Integer height) {

        graphics2D.setTransform(arrangeTranslation);

        int cellSizeWidth = width / verticalLabels.size();
        int cellSizeHeight = height / horizontalLabels.size();

        // diagonal shift
        Integer diagonalShift = (int) (cellSizeWidth * Math.cos(verticalTextDirection.rotationAngle()));


        //font
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setColor(fontColor);
        graphics2D.setFont(font);

        //metrics
        FontMetrics metrics = graphics2D.getFontMetrics();
        Integer fontHeight = metrics.getHeight();

        //overriding centerdistance
        int centerDistance = (cellSizeWidth - metrics.getHeight()) / 2;


        int maxVerticalTextWidth = Integer.MIN_VALUE;
        for (StringBuilder label : verticalLabels) {
            maxVerticalTextWidth = Math.max(maxVerticalTextWidth, metrics.stringWidth(label.toString()));
        }


        switch (verticalTextDirection) {
            case UP: {
                //rotating
                arrangeTranslation.translate(0, height - verticalExtraMargin);
                arrangeTranslation.rotate(verticalTextDirection.rotationAngle());


                for (int i = 0; i < verticalLabels.size(); i++) {
                    String label = verticalLabels.get(i).toString();
                    graphics2D.setColor(verticalColors.get(i));
                    graphics2D.drawString(label,
                                          MINIMUM_MARGIN,
                                          i * cellSizeWidth + fontHeight + centerDistance);

                }


                break;
            }
            case DOWN: {

                arrangeTranslation.translate(0, height - verticalExtraMargin);
                arrangeTranslation.rotate(verticalTextDirection.rotationAngle());
                graphics2D.setTransform(arrangeTranslation);


                for (int i = 0; i < verticalLabels.size(); i++) {
                    String label = verticalLabels.get(i).toString();
                    graphics2D.setColor(verticalColors.get(i));
                    graphics2D.drawString(label,
                                          height - metrics.stringWidth(label) - MINIMUM_MARGIN,
                                          i * cellSizeWidth + fontHeight + centerDistance);

                }


                break;
            }
            case DIAGONAL: {

                //overriding centerdistance
                centerDistance = (int) ((cellSizeWidth - fontHeight * Math.cos(verticalTextDirection.rotationAngle())) / 2);

                //vertical shift for Diagonal case
                int verticalShift = -(int) (height * Math.sin(verticalTextDirection.rotationAngle()));
                // centering
                arrangeTranslation.translate(centerDistance - fontHeight / 2, 0);

                arrangeTranslation.translate(0, verticalShift + diagonalShift - verticalExtraMargin);
                arrangeTranslation.rotate(verticalTextDirection.rotationAngle());
                graphics2D.setTransform(arrangeTranslation);


                for (int i = 0; i < verticalLabels.size(); i++) {
                    String label = verticalLabels.get(i).toString();
                    graphics2D.setColor(verticalColors.get(i));
                    graphics2D.drawString(label,
                                          MINIMUM_MARGIN + ((i) * diagonalShift) + centerDistance + horizontalExtraMargin,
                                          (int) (i * diagonalShift) + fontHeight - centerDistance + horizontalExtraMargin);
                }

                break;
            }
            case HORIZONTAL: {


                for (int i = 0; i < verticalLabels.size(); i++) {
                    String label = verticalLabels.get(i).toString();
                    graphics2D.setColor(verticalColors.get(i));

                    legendDrawText(graphics2D, label, font, fontColor, i * cellSizeWidth, 0, cellSizeWidth, height, verticalTextAlignment);
//                    graphics2D.drawString(label,
//                                          maxVerticalTextWidth,
//                                          metrics.getHeight());

                }
            }
        }



    }

    private void createHorizontalText(Graphics2D graphics, AffineTransform arrangeTranslation, Integer width, Integer height) {



        int cellSizeWidth = width / verticalLabels.size();
        int cellSizeHeight = height / horizontalLabels.size();




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

            legendDrawText(graphics, label, font, fontColor, 0, (int) (i * cellSizeHeight), width - MINIMUM_MARGIN, cellSizeHeight, horizontalTextAlignment);
//
//            switch (horizontalTextAlignment) {
//                case RIGHT: {
//                    legendDrawText(graphics, label, font, fontColor, 0, (int) (i * cellSizeHeight), width, cellSizeHeight, horizontalTextAlignment);
////                    graphics.drawString(label,
////                                        width - metrics.stringWidth(label) - MINIMUM_MARGIN,
////                                        (int) (i * cellSizeHeight) + fontSize + horizontalCenterDistance);
//                    break;
//                }
//                case LEFT: {
//                    graphics.drawString(label,
//                                        MINIMUM_MARGIN,
//                                        (int) (i * cellSizeHeight) + fontSize + horizontalCenterDistance);
//                    break;
//                }
//            }
        }
    }

    private void createTableImage(Graphics2D graphics, AffineTransform arrangeTranslation, int width, int height) {
        //drawing background 


        int cellSizeWidth = width / verticalLabels.size();
        int cellSizeHeight = height / horizontalLabels.size();

        //arrange
        graphics.setTransform(arrangeTranslation);


        graphics.setColor(Color.WHITE);
        for (int i = 0; i < tableValues.length; i++) {

            for (int j = 0; j < tableValues[i].length; j++) {

                graphics.setColor(valueColors.get(i).get(j));

                int x1, y1, x2, y2;

                if (isCellColoring) {
                    switch (cellColoringDirection) {
                        case UP: {
                            x1 = j * cellSizeWidth;
                            y2 = (int) (cellSizeHeight * tableValues[i][j]) - 1;
                            y1 = ((i + 1) * cellSizeHeight) - y2;
                            x2 = cellSizeWidth - 1;
                            graphics.fillRect(x1, y1, x2, y2);
                            break;
                        }
                        case DOWN: {
                            x1 = j * cellSizeWidth;
                            y2 = (int) (cellSizeHeight * tableValues[i][j]) - 1;
                            y1 = ((i + 1) * cellSizeHeight) - y2;
                            x2 = cellSizeWidth - 1;
                            graphics.fillRect(x1, y1, x2, y2);
                            break;
                        }
                    }
                }
                else {
                    x1 = j * cellSizeWidth;
                    y1 = i * cellSizeHeight;
                    y2 = cellSizeHeight;
                    x2 = cellSizeWidth;
                    legendDrawText(graphics, tableValues[i][j] + "", font, fontColor, x1, y1, x2, y2, Alignment.CENTER);
                }

                // GRID DISPLAYING
                if (isDisplayingGrid) {
                    x1 = j * cellSizeWidth;
                    y1 = i * cellSizeHeight;
                    y2 = cellSizeHeight;
                    x2 = cellSizeWidth;
                    graphics.setColor(gridColor);
                    graphics.drawRect(x1, y1, x2, y2);
                }

            }
        }

    }

    @Override
    public void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {

        graphics2D.setTransform(origin);
        graphics2D.setFont(font);
        FontMetrics fontMetrics = graphics2D.getFontMetrics(font);

        int maxHorizontalTextWidth = Integer.MIN_VALUE;
        for (StringBuilder label : horizontalLabels) {
            maxHorizontalTextWidth = Math.max(maxHorizontalTextWidth, fontMetrics.stringWidth(label.toString()));
        }
        int maxVerticalTextWidth = Integer.MIN_VALUE;
        for (StringBuilder label : verticalLabels) {
            maxVerticalTextWidth = Math.max(maxVerticalTextWidth, fontMetrics.stringWidth(label.toString()));
        }

        int horizontalLabelsWidth = maxHorizontalTextWidth + 2 * MINIMUM_MARGIN;

        int verticalLabelsHeight = 0;
        int tableWidth = 0;
        int tableHeight = 0;
        switch (verticalTextDirection) {
            case UP: {
                verticalLabelsHeight = maxVerticalTextWidth + 2 * MINIMUM_MARGIN;
                tableHeight = height - verticalLabelsHeight;
                tableWidth = width - horizontalLabelsWidth;
                break;
            }
            case DOWN: {
                verticalLabelsHeight = maxVerticalTextWidth + 2 * MINIMUM_MARGIN;
                tableHeight = height - verticalLabelsHeight;
                tableWidth = width - horizontalLabelsWidth;


                break;
            }
            case DIAGONAL: {
                verticalLabelsHeight = maxVerticalTextWidth + 2 * MINIMUM_MARGIN;
                System.out.println("@Var: verticalLabelsHeight: " + verticalLabelsHeight);
//                verticalLabelsHeight = (int)(verticalLabelsHeight *  - Math.sin(verticalTextDirection.rotationAngle()));
                System.out.println("@Var: verticalLabelsHeight: " + verticalLabelsHeight);
                tableHeight = height - verticalLabelsHeight;
                tableWidth = width - horizontalLabelsWidth - (int) Math.cos(verticalLabelsHeight);
                break;
            }
            case HORIZONTAL: {
                verticalLabelsHeight = fontMetrics.getHeight();
                tableHeight = height - verticalLabelsHeight;
                tableWidth = width - horizontalLabelsWidth;
                break;
            }
        }

        int verticalLabelsWidth = tableWidth;
        int horizontalLabelsHeight = tableHeight;




        // DRAWING VERTICAL TEXT
        AffineTransform verticalTextOrigin = new AffineTransform(origin);
        AffineTransform horizontalTextOrigin = new AffineTransform(origin);
        AffineTransform tableOrigin = new AffineTransform(origin);
        // arranging vertical text
        if (verticalTextPosition == TableItem.VerticalPosition.UP) {
            verticalTextOrigin.translate(0, 0);
            tableOrigin.translate(0, verticalLabelsHeight);
            horizontalTextOrigin.translate(0, verticalLabelsHeight);
        }
        else if (verticalTextPosition == TableItem.VerticalPosition.BOTTOM) {
            verticalTextOrigin.translate(0, tableHeight);
            tableOrigin.translate(0, 0);
            horizontalTextOrigin.translate(0, 0);
        }
        // arranging horizontal text
        if (horizontalTextPosition == TableItem.HorizontalPosition.LEFT) {
            horizontalTextOrigin.translate(0, 0);
            verticalTextOrigin.translate(horizontalLabelsWidth, 0);
            tableOrigin.translate(horizontalLabelsWidth, 0);
        }
        else if (horizontalTextPosition == TableItem.HorizontalPosition.RIGHT) {
            horizontalTextOrigin.translate(tableWidth, 0);
            verticalTextOrigin.translate(0, 0);
            tableOrigin.translate(0, 0);
        }
        createVerticalText(graphics2D, verticalTextOrigin, verticalLabelsWidth, verticalLabelsHeight);
        createHorizontalText(graphics2D, horizontalTextOrigin, horizontalLabelsWidth, horizontalLabelsHeight);
        if (isCellColoring) {
            normalizeWithMinValueZero(tableValues);
        }
        createTableImage(graphics2D, tableOrigin, tableWidth, tableHeight);
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
        ArrayList<TableItem.Labels> labelsGroup = item.getData(TableItem.LABELS_IDS);
        TableItem.Labels labelsIDs = labelsGroup.get(0);
        horizontalLabels = item.getData(TableItem.HORIZONTAL_LABELS);
        verticalLabels = item.getData(TableItem.VERTICAL_LABELS);
        labels = (labelsIDs ==TableItem.Labels.HORIZONTAL)? horizontalLabels : verticalLabels;
        for (int i = 0; i < labels.size(); i++) {
            StringBuilder label = labels.get(i);
            String newLabel = properties.getStringValue(TableProperty.getLabelProperty(itemIndex, i));
            label.replace(0, newLabel.length(), newLabel);
        }



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
        verticalTextPosition = (TableItem.VerticalPosition) properties.getValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_VERTICAL_TEXT_POSITION));
        horizontalTextPosition = (TableItem.HorizontalPosition) properties.getValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_HORIZONTAL_TEXT_POSITION));
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
    private Boolean isCellColoring;
    private Direction cellColoringDirection;
    private TableItem.HorizontalPosition horizontalTextPosition;
    private Alignment horizontalTextAlignment;
    private TableItem.VerticalPosition verticalTextPosition;
    private Alignment verticalTextAlignment;
    private TableItem.VerticalTextDirection verticalTextDirection;
    private Integer verticalExtraMargin;
    private Integer horizontalExtraMargin;
    private final Integer MINIMUM_MARGIN = 3;
    private ArrayList<ArrayList<Float>> tableValuesArrayList;
    private Float[][] tableValues;
    // Grid
    private Boolean isDisplayingGrid;
    private Color gridColor;
}
