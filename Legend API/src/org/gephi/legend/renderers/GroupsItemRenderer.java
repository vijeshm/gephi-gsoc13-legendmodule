/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendManager;
import org.gephi.legend.builders.GroupsItemBuilder;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.NbBundle;
import org.gephi.legend.items.GroupsItem;
import org.gephi.legend.properties.GroupsProperty;
import org.gephi.legend.api.LegendItem.Shape;
import org.gephi.legend.api.LegendItem.Direction;
import org.gephi.legend.items.TableItem;
import org.gephi.legend.properties.TableProperty;
import org.gephi.preview.spi.Renderer;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = Renderer.class, position = 504)
public class GroupsItemRenderer extends LegendItemRenderer {

    @Override
    public void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {

        if (labels.isEmpty()) {
            return;
        }

        graphics2D.setTransform(origin);

        System.out.println("@Var: height: " + height);
        System.out.println("@Var: width: " + width);
        int numGroups = labels.size();
        System.out.println("@Var: numGroups: " + numGroups);
        int numRows = (int) Math.ceil((double) numGroups / numColumns);
        System.out.println("@Var: numColumns: " + numColumns);
        System.out.println("@Var: numRows: " + numRows);

        // max label length
        graphics2D.setFont(labelFont);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();


        int elementHeight = (height - (numRows - 1) * paddingBetweenElements) / numRows;
        int elementWidth = (width - (numColumns - 1) * paddingBetweenElements) / numColumns;
//        int elementWidth = width / numColumns - paddingBetweenElements;
        System.out.println("@Var: elemendWidth: " + elementWidth);
        System.out.println("@Var: elemendHeight: " + elementHeight);

        int maxLabelWidth = Integer.MIN_VALUE;
        int maxLabelHeight = Integer.MIN_VALUE;

        // computing max label width
        for (StringBuilder label : labels) {
            maxLabelHeight = (int) Math.max(maxLabelHeight, computeVerticalTextSpaceUsed(graphics2D, label.toString(), labelFont, 0, 0, width));
        }

        // computing max label height
        for (StringBuilder label : labels) {
            maxLabelWidth = Math.max(maxLabelWidth, fontMetrics.stringWidth(label.toString()));
        }


        System.out.println("@Var: maxLabelHeight: " + maxLabelHeight);
        System.out.println("@Var: maxLabelWidth: " + maxLabelWidth);


        int shapeWidth = 0, shapeHeight = 0, labelHeight = 0, labelWidth = 0;
        if (labelPosition == Direction.RIGHT || labelPosition == Direction.LEFT) {
            shapeWidth = elementWidth - maxLabelWidth - paddingBetweenTextAndShape;
            shapeHeight = elementHeight - paddingBetweenElements;
            labelWidth = maxLabelWidth;
            labelHeight = shapeHeight;
        }
        else if (labelPosition == Direction.UP || labelPosition == Direction.BOTTOM) {
            shapeWidth = elementWidth - paddingBetweenElements;
            shapeHeight = elementHeight - maxLabelHeight - paddingBetweenTextAndShape;
            labelWidth = shapeWidth;
            labelHeight = maxLabelHeight;
        }

        // 
        ArrayList<Float> valuesNormalized = new ArrayList<Float>();
        if (isScalingShapes) {
            float maxValue = Collections.max(valuesGroup);
            for (Float value : valuesGroup) {
                valuesNormalized.add(value / maxValue);
            }
        }
        else {
            for (Float value : valuesGroup) {
                valuesNormalized.add(1f);
            }
        }

        for (int i = 0; i < labels.size(); i++) {
            int x = (i % numColumns) * elementWidth + paddingBetweenElements;
            int y = (i / numColumns) * elementHeight + paddingBetweenElements;
            int xShape = 0, yShape = 0, xLabel = 0, yLabel = 0;
            switch (labelPosition) {
                case RIGHT: {
                    xShape = x;
                    yShape = y;
                    xLabel = x + shapeWidth + paddingBetweenTextAndShape;
                    yLabel = y;
                    break;
                }
                case LEFT: {
                    xShape = x + labelWidth + paddingBetweenTextAndShape;
                    yShape = y;
                    xLabel = x;
                    yLabel = y;
                    break;
                }
                case UP: {
                    xShape = x;
                    yShape = y + labelHeight + paddingBetweenTextAndShape;
                    xLabel = x;
                    yLabel = y;
                    break;
                }
                case BOTTOM: {
                    xShape = x;
                    yShape = y;
                    xLabel = x;
                    yLabel = y + shapeHeight;
                    break;
                }
            }

            drawShape(graphics2D, shape, colorsGroup.get(i), xShape, yShape, shapeWidth, shapeHeight, valuesNormalized.get(i));
            legendDrawText(graphics2D, labels.get(i).toString(), labelFont, colorsGroup.get(i), xLabel, yLabel, labelWidth, labelHeight, LegendItem.Alignment.CENTER);
        }
    }

    public void drawShape(Graphics2D graphics2D, Shape shape, Color color, int x, int y, Integer width, Integer height, float scale) {
        System.out.println("@Var: shape: " + "--------------");
        System.out.println("@Var: shape: " + shape);
        System.out.println("@Var: color: " + color);
        System.out.println("@Var: x: " + x);
        System.out.println("@Var: y: " + y);
        System.out.println("@Var: width: " + width);
        System.out.println("@Var: height: " + height);

        int shapeWidth = (int) (width * scale);
        int shapeHeight = (int) (height * scale);
        x = (int) (x + (width - shapeWidth) / 2);
        y = (int) (y + (height - shapeHeight) / 2);
        graphics2D.setColor(color);
        switch (shape) {
            case RECTANGLE: {
                graphics2D.fillRect(x, y, shapeWidth, shapeHeight);
                break;
            }
            case CIRCLE: {
                graphics2D.fillOval(x, y, shapeWidth, shapeHeight);
                break;
            }
            case TRIANGLE: {
                int[] xpoints = {x, x + shapeWidth, x + shapeWidth / 2};
                int[] ypoints = {y + shapeHeight, y + shapeHeight, y};
                Polygon triangle = new Polygon(xpoints, ypoints, xpoints.length);
                graphics2D.fillPolygon(triangle);
            }
        }
    }

    @Override
    public void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);


        // READING LABELS
        labels = item.getData(GroupsItem.LABELS_IDS);
        for (int i = 0; i < labels.size(); i++) {
            StringBuilder label = labels.get(i);
            String newLabel = properties.getStringValue(GroupsProperty.getLabelProperty(itemIndex, i));
            label.replace(0, newLabel.length(), newLabel);
        }
        
        
        colorsGroup = item.getData(GroupsItem.COLORS);
        valuesGroup = item.getData(GroupsItem.VALUES);

        // properties
        numColumns = properties.getIntValue(LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_NUMBER_COLUMNS));
        numColumns = Math.min(numColumns, labels.size());
        labelPosition = (LegendItem.Direction) properties.getValue(LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_LABEL_POSITION));
        shape = (LegendItem.Shape) properties.getValue(LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_SHAPE));
        labelFont = properties.getFontValue(LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_LABEL_FONT));
        labelFontColor = properties.getColorValue(LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_LABEL_FONT_COLOR));
        isScalingShapes = properties.getBooleanValue(LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_SCALE_SHAPE));
        
        paddingBetweenTextAndShape = properties.getIntValue(LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_PADDING_BETWEEN_TEXT_AND_SHAPE));
        paddingBetweenElements = properties.getIntValue(LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_PADDING_BETWEEN_ELEMENTS));

    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(GroupsItemRenderer.class, "GroupsItemRenderer.name");
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        return item instanceof GroupsItem;
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof GroupsItemBuilder;
    }

    //PROPERTIES
    private Integer numColumns;
    private Integer paddingBetweenTextAndShape;
    private Integer paddingBetweenElements;
    private Color labelFontColor;
    private Font labelFont;
    private Direction labelPosition;
    private Shape shape;
    // TO ADD
    private Boolean isScalingShapes;
    //VALUES
    private ArrayList<StringBuilder> labels;
    private ArrayList<Color> colorsGroup;
    private ArrayList<Float> valuesGroup;
    // min shape size
    private Float MINIMUM_SHAPE_SIZE = 10f;
}
