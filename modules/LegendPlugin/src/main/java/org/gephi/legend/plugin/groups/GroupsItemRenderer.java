/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.groups;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.AbstractLegendItemRenderer;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.BlockNode;
import org.gephi.legend.inplaceeditor.Column;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.inplaceeditor.InplaceItemBuilder;
import org.gephi.legend.inplaceeditor.InplaceItemRenderer;
import org.gephi.legend.inplaceeditor.Row;
import org.gephi.legend.inplaceelements.BaseElement;
import org.gephi.legend.inplaceelements.ElementColor;
import org.gephi.legend.inplaceelements.ElementFont;
import org.gephi.legend.inplaceelements.ElementImage;
import org.gephi.legend.inplaceelements.ElementLabel;
import org.gephi.legend.inplaceelements.ElementNumber;
import org.gephi.legend.inplaceelements.ElementText;
import org.gephi.legend.plugin.table.Cell;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.legend.spi.LegendItem.Direction;
import org.gephi.legend.spi.LegendItem.Shape;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author mvvijesh, edubecks
 */
@ServiceProvider(service = Renderer.class, position = 502)
public class GroupsItemRenderer extends AbstractLegendItemRenderer {

    public static String GROUP_NODE = "group node";
    public static String GROUP_ELEMENT_NODE = "group element node";
    //PROPERTIES
    protected Shape shape;
    protected Float shapeWidthFraction;
    protected Direction labelPosition;
    protected Font labelFont;
    protected Color labelFontColor;
    protected Alignment labelFontAlignment;
    protected Integer paddingBetweenTextAndShape;
    protected Integer paddingBetweenElements;
    protected Color Background;
    // min shape size
    protected Float MINIMUM_SHAPE_SIZE = 10f;

    @Override
    public boolean isAnAvailableRenderer(Item item) {
        return item instanceof GroupsItem;
    }

    @Override
    protected void renderToGraphics(Graphics2D graphics2d, RenderTarget target, BlockNode legendNode) {
        // doesnt support multiple rows yet

        int blockOriginX = (int) (legendNode.getOriginX());
        int blockOriginY = (int) (legendNode.getOriginY());
        int blockWidth = (int) legendNode.getBlockWidth();
        int blockHeight = (int) legendNode.getBlockHeight();

        GroupsItem item = (GroupsItem) legendNode.getItem();
        PreviewProperty[] itemPreviewProperties = item.getData(LegendItem.OWN_PROPERTIES);
        ArrayList<GroupElement> groups = item.getGroups();
        int numberOfGroups = groups.size();
        int itemIndex = item.getData(LegendItem.ITEM_INDEX);

        // currently the group legend occupies the entire block.
        int groupOriginX = blockOriginX;
        int groupOriginY = blockOriginY;
        int groupWidth = blockWidth;
        int groupHeight = blockHeight;

        BlockNode groupNode = legendNode.getChild(GROUP_NODE);
        if (groupNode == null) {
            groupNode = legendNode.addChild(groupOriginX, groupOriginY, groupWidth, groupHeight, GROUP_NODE);
            // buildInplaceGroup(legendNode, item, graphics2d, target);
        }

        groupNode.updateGeometry(groupOriginX, groupOriginY, groupWidth, groupHeight);
        drawBlockBoundary(graphics2d, groupNode);

        // render background
        graphics2d.setColor(Background);
        graphics2d.fillRect((int) (groupOriginX - currentRealOriginX), (int) (groupOriginY - currentRealOriginY), groupWidth, groupHeight);

        int elementWidth = (groupWidth - (numberOfGroups + 1) * paddingBetweenElements) / numberOfGroups;
        int elementHeight = groupHeight;

        // check if the group elements have already been built
        BlockNode groupElement = legendNode.getChild(GROUP_ELEMENT_NODE); // gets the first instance of group element node
        if (groupElement == null) {
            // build group elements only for the first time

            // utility variables
            Graph graph = null;
            PreviewProperty[] elementPreviewProperties;
            BlockNode elementNode;
            Map<String, Object> data;
            Row r;
            Column col;
            BaseElement addedElement;

            int elementOriginX;
            int elementOriginY;
            for (int i = 0; i < groups.size(); i++) {
                elementOriginX = groupOriginX + i * elementWidth + (i + 1) * paddingBetweenElements;
                elementOriginY = groupOriginY;

                elementNode = groupNode.addChild(elementOriginX, elementOriginY, elementWidth, elementHeight, GROUP_ELEMENT_NODE);
                elementPreviewProperties = groups.get(i).getPreviewProperties();

                InplaceItemBuilder ipbuilder = Lookup.getDefault().lookup(InplaceItemBuilder.class);
                InplaceEditor ipeditor = ipbuilder.createInplaceEditor(graph, elementNode);

                r = ipeditor.addRow();
                col = r.addColumn(false);
                data = new HashMap<String, Object>();
                data.put(ElementLabel.LABEL_TEXT, "Group:");
                data.put(ElementLabel.LABEL_COLOR, InplaceItemRenderer.LABEL_COLOR);
                data.put(ElementLabel.LABEL_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.LABEL, itemIndex, null, data, false, null);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                r = ipeditor.addRow();
                col = r.addColumn(false);
                data = new HashMap<String, Object>();
                data.put(ElementLabel.LABEL_TEXT, "Shape:");
                data.put(ElementLabel.LABEL_COLOR, InplaceItemRenderer.LABEL_COLOR);
                data.put(ElementLabel.LABEL_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.LABEL, itemIndex, null, data, false, null);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                col = r.addColumn(true);
                // rectangle
                data = new HashMap<String, Object>();
                data.put(ElementImage.IMAGE_BOOL, elementPreviewProperties[GroupElement.SHAPE].getValue() == Shape.RECTANGLE);
                data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/group_rectangle_selected.png");
                data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/group_rectangle_unselected.png");
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, elementPreviewProperties[GroupElement.SHAPE], data, true, Shape.RECTANGLE);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                // circle
                data = new HashMap<String, Object>();
                data.put(ElementImage.IMAGE_BOOL, elementPreviewProperties[GroupElement.SHAPE].getValue() == Shape.CIRCLE);
                data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/group_circle_selected.png");
                data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/group_circle_unselected.png");
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, elementPreviewProperties[GroupElement.SHAPE], data, false, Shape.CIRCLE);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                // triangle
                data = new HashMap<String, Object>();
                data.put(ElementImage.IMAGE_BOOL, elementPreviewProperties[GroupElement.SHAPE].getValue() == Shape.TRIANGLE);
                data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/group_triangle_selected.png");
                data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/group_triangle_unselected.png");
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, elementPreviewProperties[GroupElement.SHAPE], data, false, Shape.TRIANGLE);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                col = r.addColumn(false);
                data = new HashMap<String, Object>();
                data.put(ElementColor.COLOR_MARGIN, InplaceItemRenderer.COLOR_MARGIN);
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.COLOR, itemIndex, elementPreviewProperties[GroupElement.SHAPE_COLOR], data, false, null);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                r = ipeditor.addRow();
                col = r.addColumn(false);
                data = new HashMap<String, Object>();
                data.put(ElementLabel.LABEL_TEXT, "Label:");
                data.put(ElementLabel.LABEL_COLOR, InplaceItemRenderer.LABEL_COLOR);
                data.put(ElementLabel.LABEL_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.LABEL, itemIndex, null, data, false, null);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                r = ipeditor.addRow();
                col = r.addColumn(false);
                data = new HashMap<String, Object>();
                data.put(ElementText.EDIT_IMAGE, "/org/gephi/legend/graphics/edit.png");
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.TEXT, itemIndex, elementPreviewProperties[GroupElement.LABEL_TEXT], data, false, null);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                col = r.addColumn(false);
                data = new HashMap<String, Object>();
                data.put(ElementColor.COLOR_MARGIN, InplaceItemRenderer.COLOR_MARGIN);
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.COLOR, itemIndex, elementPreviewProperties[GroupElement.LABEL_COLOR], data, false, null);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                col = r.addColumn(false);
                data = new HashMap<String, Object>();
                data.put(ElementFont.DISPLAY_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
                data.put(ElementFont.DISPLAY_FONT_COLOR, InplaceItemRenderer.FONT_DISPLAY_COLOR);
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.FONT, itemIndex, elementPreviewProperties[GroupElement.LABEL_FONT], data, false, null);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                r = ipeditor.addRow();
                col = r.addColumn(true);
                // left-alignment
                data = new HashMap<String, Object>();
                data.put(ElementImage.IMAGE_BOOL, elementPreviewProperties[GroupElement.LABEL_ALIGNMENT].getValue() == Alignment.LEFT);
                data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/left_selected.png");
                data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/left_unselected.png");
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, elementPreviewProperties[GroupElement.LABEL_ALIGNMENT], data, false, Alignment.LEFT);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                // center-alignment
                data = new HashMap<String, Object>();
                data.put(ElementImage.IMAGE_BOOL, elementPreviewProperties[GroupElement.LABEL_ALIGNMENT].getValue() == Alignment.CENTER);
                data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/center_selected.png");
                data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/center_unselected.png");
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, elementPreviewProperties[GroupElement.LABEL_ALIGNMENT], data, true, Alignment.CENTER);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                // right alignment
                data = new HashMap<String, Object>();
                data.put(ElementImage.IMAGE_BOOL, elementPreviewProperties[GroupElement.LABEL_ALIGNMENT].getValue() == Alignment.RIGHT);
                data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/right_selected.png");
                data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/right_unselected.png");
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, elementPreviewProperties[GroupElement.LABEL_ALIGNMENT], data, true, Alignment.RIGHT);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                // justified
                data = new HashMap<String, Object>();
                data.put(ElementImage.IMAGE_BOOL, elementPreviewProperties[GroupElement.LABEL_ALIGNMENT].getValue() == Alignment.JUSTIFIED);
                data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/justified_selected.png");
                data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/justified_unselected.png");
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, elementPreviewProperties[GroupElement.LABEL_ALIGNMENT], data, true, Alignment.JUSTIFIED);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                r = ipeditor.addRow();
                col = r.addColumn(true);
                // position up
                data = new HashMap<String, Object>();
                data.put(ElementImage.IMAGE_BOOL, elementPreviewProperties[GroupElement.LABEL_POSITION].getValue() == Direction.UP);
                data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/groups_label_position_up_selected.png");
                data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/groups_label_position_up_unselected.png");
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, elementPreviewProperties[GroupElement.LABEL_POSITION], data, true, Direction.UP);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                // position down
                data = new HashMap<String, Object>();
                data.put(ElementImage.IMAGE_BOOL, elementPreviewProperties[GroupElement.LABEL_POSITION].getValue() == Direction.DOWN);
                data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/groups_label_position_down_selected.png");
                data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/groups_label_position_down_unselected.png");
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, elementPreviewProperties[GroupElement.LABEL_POSITION], data, false, Direction.DOWN);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                r = ipeditor.addRow();
                col = r.addColumn(false);
                data = new HashMap<String, Object>();
                data.put(ElementLabel.LABEL_TEXT, "Value:");
                data.put(ElementLabel.LABEL_COLOR, InplaceItemRenderer.LABEL_COLOR);
                data.put(ElementLabel.LABEL_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.LABEL, itemIndex, null, data, false, null);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                col = r.addColumn(false);
                data = new HashMap<String, Object>();
                data.put(ElementNumber.NUMBER_COLOR, InplaceItemRenderer.NUMBER_COLOR);
                data.put(ElementNumber.NUMBER_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
                addedElement = col.addElement(BaseElement.ELEMENT_TYPE.NUMBER, itemIndex, elementPreviewProperties[GroupElement.VALUE], data, false, null);
                addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

                elementNode.setInplaceEditor(ipeditor);
            }
        }
        // update the geometry of the groups
        updateGroupGeometry(groupNode, numberOfGroups);

        renderGroupElements(graphics2d, groups, groupNode.getChildren());
    }

    private void updateGroupGeometry(BlockNode groupNode, int numberOfGroups) {
        int groupOriginX = (int) groupNode.getOriginX();
        int groupOriginY = (int) groupNode.getOriginY();
        int groupWidth = (int) groupNode.getBlockWidth();
        int groupHeight = (int) groupNode.getBlockHeight();

        int elementWidth = (groupWidth - (numberOfGroups + 1) * paddingBetweenElements) / numberOfGroups;
        int elementHeight = groupHeight;
        int elementOriginX;
        int elementOriginY;

        ArrayList<BlockNode> elementNodes = groupNode.getChildren();
        for (int i = 0; i < numberOfGroups; i++) {
            elementOriginX = groupOriginX + i * elementWidth + (i + 1) * paddingBetweenElements;
            elementOriginY = groupOriginY;

            elementNodes.get(i).updateGeometry(elementOriginX, elementOriginY, elementWidth, elementHeight);
        }
    }

    private void renderGroupElements(Graphics2D graphics2D, ArrayList<GroupElement> groups, ArrayList<BlockNode> groupElementNodes) {
        // according to the data from the groups and geometry information from the groupElementNodes, render the groups        

        int elementOriginX;
        int elementOriginY;
        int elementWidth;
        int elementHeight;
        BlockNode groupElementNode;
        GroupElement groupElement;
        PreviewProperty[] elementPreviewProperties;
        String elementLabelText;
        Font elementLabelFont;
        Color elementLabelFontColor;
        Alignment elementLabelFontAlignment;
        Direction elementLabelPosition;
        float elementValue;
        Shape elementShape;
        Color elementShapeColor;
        FontMetrics fontMetrics;

        int shapeOriginX, shapeOriginY, shapeWidth, shapeHeight;
        int labelOriginX, labelOriginY, labelWidth, labelHeight;

        ArrayList<Float> normalizedValues = new ArrayList<Float>(); //do not change the original data. instead, compute a new array
        float maxValue = Float.MIN_VALUE;
        for (GroupElement group : groups) {
            maxValue = Math.max(maxValue, (Float) group.getPreviewProperty(GroupElement.VALUE).getValue());
        }

        for (GroupElement group : groups) {
            normalizedValues.add((Float) group.getPreviewProperty(GroupElement.VALUE).getValue() / maxValue);
        }

        for (int i = 0; i < groups.size(); i++) {
            groupElementNode = groupElementNodes.get(i);
            groupElement = groups.get(i);

            elementOriginX = (int) (groupElementNode.getOriginX() - currentRealOriginX);
            elementOriginY = (int) (groupElementNode.getOriginY() - currentRealOriginY);
            elementWidth = (int) groupElementNode.getBlockWidth();
            elementHeight = (int) groupElementNode.getBlockHeight();
            elementPreviewProperties = groupElement.getPreviewProperties();

            elementLabelText = elementPreviewProperties[GroupElement.LABEL_TEXT].getValue();
            elementLabelFont = elementPreviewProperties[GroupElement.LABEL_FONT].getValue();
            elementLabelFontColor = elementPreviewProperties[GroupElement.LABEL_COLOR].getValue();
            elementLabelFontAlignment = elementPreviewProperties[GroupElement.LABEL_ALIGNMENT].getValue();
            elementLabelPosition = elementPreviewProperties[GroupElement.LABEL_POSITION].getValue();
            elementValue = elementPreviewProperties[GroupElement.VALUE].getValue();
            elementShape = elementPreviewProperties[GroupElement.SHAPE].getValue();
            elementShapeColor = elementPreviewProperties[GroupElement.SHAPE_COLOR].getValue();

            graphics2D.setFont(elementLabelFont);
            fontMetrics = graphics2D.getFontMetrics();

            shapeOriginX = 0;
            shapeOriginY = 0;
            shapeWidth = 0;
            shapeHeight = 0;
            labelOriginX = 0;
            labelOriginY = 0;
            labelWidth = 0;
            labelHeight = 0;

            switch (elementLabelPosition) {
                case UP:
                    labelOriginX = elementOriginX;
                    labelOriginY = elementOriginY;
                    labelWidth = elementWidth;
                    labelHeight = fontMetrics.getHeight();

                    shapeOriginX = elementOriginX;
                    shapeOriginY = elementOriginY + fontMetrics.getHeight();
                    shapeWidth = elementWidth;
                    shapeHeight = elementHeight - fontMetrics.getHeight() - paddingBetweenTextAndShape;
                    break;

                case LEFT:
                    labelOriginX = elementOriginX;
                    labelOriginY = elementOriginY;
                    labelWidth = fontMetrics.stringWidth(elementLabelText);
                    int thresholdWidth = elementWidth / 2;
                    if (labelWidth > thresholdWidth) {
                        labelWidth = thresholdWidth;
                    }
                    labelHeight = elementHeight;

                    shapeOriginX = elementOriginX + labelWidth;
                    shapeOriginY = elementOriginY;
                    shapeWidth = elementWidth - labelWidth - paddingBetweenTextAndShape;
                    shapeHeight = elementHeight;
                    break;

                case DOWN:
                    labelOriginX = elementOriginX;
                    labelOriginY = elementOriginY + elementHeight - fontMetrics.getHeight();
                    labelWidth = elementWidth;
                    labelHeight = fontMetrics.getHeight();

                    shapeOriginX = elementOriginX;
                    shapeOriginY = elementOriginY;
                    shapeWidth = elementWidth;
                    shapeHeight = elementHeight - fontMetrics.getHeight() - paddingBetweenTextAndShape;
                    break;

                case RIGHT:
                    labelWidth = fontMetrics.stringWidth(elementLabelText);
                    labelHeight = fontMetrics.getHeight();
                    labelOriginX = elementOriginX + elementWidth - labelWidth;
                    labelOriginY = elementHeight;

                    shapeOriginX = elementOriginX;
                    shapeOriginY = elementOriginY;
                    shapeWidth = elementWidth - labelWidth - paddingBetweenTextAndShape;
                    shapeHeight = elementHeight;
                    break;
            }

            drawShape(graphics2D, elementShape, elementShapeColor, shapeOriginX, shapeOriginY, shapeWidth, shapeHeight, normalizedValues.get(i));
            drawElementLabel(graphics2D, elementLabelText, elementLabelFont, elementLabelFontColor, labelOriginX, labelOriginY, labelWidth, labelHeight, elementLabelFontAlignment);
            drawBlockBoundary(graphics2D, groupElementNode);
        }
    }

    @Override // to be deprecated
    protected void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
    }

    /**
     * Override this function to draw each element label in a different way.
     *
     * @param graphics2D the Graphics object to draw to
     * @param label text to be displayed as label
     * @param labelFont font used to draw the label
     * @param labelColor rendering color for the label
     * @param x the x coordinate of the area containing the label
     * @param y the y coordinate of the area containing the label
     * @param width the width of the area containing the label
     * @param height the height of the area containing the label
     *
     */
    protected void drawElementLabel(Graphics2D graphics2D, String label, Font labelFont, Color labelColor, int x, int y, Integer width, Integer height, Alignment alignment) {
        legendDrawText(graphics2D, label, labelFont, labelColor, x, y, width, height, alignment);
    }

    /**
     * Override this function to draw each element shape or representative
     * element in a different way.
     *
     * @param graphics2D the Graphics object to draw to
     * @param shape shape specified by the user, it could be one the three
     * values:
     * @param shape the shape that needs to be drawn
     * @param x the x coordinate of the area containing the label
     * @param y the y coordinate of the area containing the label
     * @param width the width of the area containing the label
     * @param height the height of the area containing the label
     * @param scale the scaling factor for the width and height.
     */
    private void drawShape(Graphics2D graphics2D, Shape shape, Color color, int x, int y, Integer width, Integer height, float scale) {

        int shapeWidth = (int) (width * shapeWidthFraction);
        int shapeHeight = (int) (height * scale);
        x = (x + (width - shapeWidth) / 2);
        y = y + height - shapeHeight;
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
    protected void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        shape = properties.getValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_SHAPE));
        shapeWidthFraction = properties.getValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_SHAPE_WIDTH_FRACTION));
        labelPosition = properties.getValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_LABEL_POSITION));
        labelFont = properties.getValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_LABEL_FONT));
        labelFontColor = properties.getValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_LABEL_FONT_COLOR));
        labelFontAlignment = properties.getValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_LABEL_FONT_ALIGNMENT));
        paddingBetweenTextAndShape = properties.getValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_PADDING_BETWEEN_TEXT_AND_SHAPE));
        paddingBetweenElements = properties.getValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_PADDING_BETWEEN_ELEMENTS));
        Background = properties.getValue(LegendModel.getProperty(GroupsProperty.OWN_PROPERTIES, itemIndex, GroupsProperty.GROUPS_BACKGROUND));
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(GroupsItemRenderer.class, "GroupsItemRenderer.name");
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof GroupsItemBuilder;
    }
}