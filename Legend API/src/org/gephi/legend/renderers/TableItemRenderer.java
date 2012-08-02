/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

/**
 *
 * @author edubecks
 */
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        Integer diagonalShift = (int) (cellSizeWidth * Math.cos(verticalTextDirection.rotationAngle()));

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

    public void createHorizontalText(Graphics2D graphics, AffineTransform arrangeTranslation, Integer width, Integer height) {


        //arrange
        graphics.setTransform(arrangeTranslation);

        int fontSize = font.getSize();
        int horizontalCenterDistance = (cellSizeHeight - fontSize) / 2;



        FontMetrics metrics = graphics.getFontMetrics(font);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setColor(Color.WHITE);
        for (int i = 0; i < labels.size(); i++) {
            String label = labels.get(i);
            if (isCellColoring) {
                graphics.setColor(colors.get(i));
            }

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

    public void createTableImage(Graphics2D graphics, AffineTransform arrangeTranslation) {
        //drawing background 

        //arrange
        graphics.setTransform(arrangeTranslation);


        graphics.setColor(Color.WHITE);
        for (int i = 0; i < tableValues.length; i++) {
            if (isCellColoring) {
                graphics.setColor(colors.get(i));
            }
            for (int j = 0; j < tableValues[i].length; j++) {
//                System.out.printf("[%d][%d] -> (%f) -> Direction %s\n", i, j,
//                                  tableValues[i][j], cellColoring.toString());
                switch (cellColoringDirection) {
                    case UP:
                        int x1 = j * cellSizeWidth;
                        int y2 = (int) (cellSizeHeight * tableValues[i][j]) - 1;
                        int y1 = ((i + 1) * cellSizeHeight) - y2;
                        int x2 = cellSizeWidth - 1;
//                        System.out.printf("(%d,%d)->(%d,%d)\n", x1, y1, x1 + x2, y1 + y2);
                        graphics.fillRect(x1, y1, x2, y2);
                        break;
                }

            }
        }

    }

    private String longestLabel(ArrayList<String> labels) {

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
        System.out.println("@Var: width: " + width);
        System.out.println("@Var: height: " + height);
        graphics2D.setFont(font);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();

        int maxTextWidth = Integer.MIN_VALUE;
        for (String label : labels) {
            maxTextWidth = Math.max(maxTextWidth, fontMetrics.stringWidth(label));
        }

        int maxTextHeight = fontMetrics.getHeight();

//        ArrayList<Color> listOfColors = tableItem.getData(TableItem.LIST_OF_COLORS);


        Integer horizontalTextWidth = maxTextWidth + 2 * minimumMargin;
//        Integer verticalTextHeight = maxTextWidth  * (int) Math.abs(Math.sin(verticalTextDirection.rotationAngle())) + 2 * minimumMargin ;
        Integer verticalTextHeight = maxTextWidth + 2 * minimumMargin;
        int tempTableWidth = (int) (width - horizontalExtraMargin - minimumMargin - horizontalTextWidth - maxTextWidth * Math.abs(Math.cos(verticalTextDirection.rotationAngle())));
        System.out.println("@Var: tempTableWidth: " + tempTableWidth);
        int tempTableHeight = (int) (height - verticalExtraMargin - minimumMargin - verticalTextHeight);
        System.out.println("@Var: tempTableHeight: " + tempTableHeight);

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
            normalize(tableValues);
        }
        arrangeTranslation.setTransform(origin);
        arrangeTranslation.translate(horizontalTextWidth, verticalTextHeight);
        createTableImage(graphics2D, arrangeTranslation);
    }

    public void normalize(Float[][] table, float minValue, float maxValue) {
        float range = maxValue - minValue;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = (table[i][j] - minValue) / (range);
            }
        }
    }
    //options

    public void normalize(Float[][] table) {
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

        //values
//        labelsJSON = properties.getStringValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_LABELS));
//        labelsJSON = "{\n"+labelsJSON+"\n}";
//        System.out.println("@Var: labelsJSON: "+labelsJSON);
//        
//        JsonParser jsonParser = new JsonParser();
//        JsonObject json = jsonParser.parse(labelsJSON).getAsJsonObject();
//        
//        for (int i = 0; i < labels.size(); i++) {
//            System.out.printf("Changing from %s\n",labels.get(i));
//            labels.set(i,json.get(labels.get(i)).getAsString());
//            System.out.printf("Changing to %s\n",labels.get(i));
//        }

//        labels =item.getData(TableItem.LABELS_IDS);
        tableValuesArrayList = item.getData(TableItem.TABLE_VALUES);
        System.out.println("@Var: tableValuesArrayList: "+tableValuesArrayList);
        tableValues = new Float[tableValuesArrayList.size()][tableValuesArrayList.get(0).size()];

        for (int i = 0; i < tableValuesArrayList.size(); i++) {
            for (int j = 0; j < tableValuesArrayList.get(i).size(); j++) {
                tableValues[i][j] = tableValuesArrayList.get(i).get(j);
            }
        }
        colors = item.getData(TableItem.LIST_OF_COLORS);
        System.out.println("@Var: colors: "+colors);
        

        labels = new ArrayList<String>();
        for (int i = 0; i < colors.size(); i++) {
            labels.add(properties.getStringValue(TableProperty.getLabelProperty(itemIndex, i)));
        }
        System.out.println("@Var: labels: "+labels);


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
        verticalTextDirection = (TableItem.VerticalTextDirection) properties.getValue(LegendManager.getProperty(TableProperty.OWN_PROPERTIES, itemIndex, TableProperty.TABLE_VERTICAL_TEXT_ROTATION));




    }

    private Font font;
    private Color fontColor;
    private ArrayList<String> labels;
    private ArrayList<String> horizontalLabels;
    private ArrayList<String> verticalLabels;
    private String labelsJSON;
    private ArrayList<Color> colors;
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
    private ArrayList<ArrayList<Float>> tableValuesArrayList;
    private Float[][] tableValues;
}
