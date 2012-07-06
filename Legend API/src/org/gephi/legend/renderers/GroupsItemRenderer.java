/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
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

        graphics2D.setTransform(origin);

        System.out.println("@Var: height: " + height);
        System.out.println("@Var: width: " + width);
        int numGroups = labelsGroup.size();
        System.out.println("@Var: numGroups: " + numGroups);
        int numRows = (int) Math.ceil((double) numGroups / numColumns);
        System.out.println("@Var: numColumns: " + numColumns);
        System.out.println("@Var: numRows: " + numRows);

        // max label length
        graphics2D.setFont(labelFont);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int maxLabelWidth = Integer.MIN_VALUE;
        int maxLabelHeight = fontMetrics.getHeight();
        for (String label : labelsGroup) {
            maxLabelWidth = Math.max(maxLabelWidth, fontMetrics.stringWidth(label));
        }
        System.out.println("@Var: maxLabelHeight: " + maxLabelHeight);
        System.out.println("@Var: maxLabelWidth: " + maxLabelWidth);


        int elementHeight = (height - (numRows - 1) * paddingBetweenElements) / numRows;
        int elementWidth = (width - (numColumns - 1) * paddingBetweenElements) / numColumns;
//        int elementWidth = width / numColumns - paddingBetweenElements;
        System.out.println("@Var: elemendWidth: " + elementWidth);
        System.out.println("@Var: elemendHeight: " + elementHeight);

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

        for (int i = 0; i < labelsGroup.size(); i++) {
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
            drawShape(graphics2D, shape, colorsGroup.get(i), xShape, yShape, shapeWidth, shapeHeight);
            System.out.println("@Var: xShape: " + xShape);
            System.out.println("@Var: yShape: " + yShape);
            legendDrawText(graphics2D, labelsGroup.get(i), labelFont, colorsGroup.get(i), xLabel, yLabel, labelWidth, labelHeight, LegendItem.Alignment.CENTER);
        }
    }


    public void drawShape(Graphics2D graphics2D, Shape shape, Color color, int x, int y, Integer width, Integer height) {
        graphics2D.setColor(color);
        switch (shape) {
            case RECTANGLE: {
                graphics2D.fillRect(x, y, width, height);
                break;
            }
            case CIRCLE: {
                graphics2D.fillOval(x, y, width, height);
                break;

            }
            case TRIANGLE: {
                int[] xpoints = {x, x + width, x + width / 2};
                int[] ypoints = {y + height, y + height, y};
                Polygon triangle = new Polygon(xpoints, ypoints, xpoints.length);
                graphics2D.fillPolygon(triangle);
            }
        }
    }

    @Override
    public void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        // properties
        numColumns = properties.getIntValue(LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_NUMBER_COLUMNS));
        labelPosition = (LegendItem.Direction) properties.getValue(LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_LABEL_POSITION));
        shape = (LegendItem.Shape) properties.getValue(LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_SHAPE));
        labelFont = properties.getFontValue(LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_LABEL_FONT));
        labelFontColor = properties.getColorValue(LegendManager.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_LABEL_FONT_COLOR));

        //values
        labelsGroup = item.getData(GroupsItem.LABELS_GROUP);
        colorsGroup = item.getData(GroupsItem.COLORS_GROUP);

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
    //VALUES
    private ArrayList<String> labelsGroup;
    private ArrayList<Color> colorsGroup;
}
