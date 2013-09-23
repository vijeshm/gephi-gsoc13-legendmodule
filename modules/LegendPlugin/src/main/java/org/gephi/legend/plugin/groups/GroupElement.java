package org.gephi.legend.plugin.groups;

import java.awt.Color;
import java.awt.Font;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.legend.spi.LegendItem.Direction;
import org.gephi.legend.spi.LegendItem.Shape;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;

/**
 * class to represent a single element in a group.
 *
 * The layout of a group element contains a shape area and a label area. The
 * label can be position UP or DOWN, relative to the shape. The height of the
 * shape area is determined by the normalized value. The width of the shape area
 * is determined the number of group elements and the legend width. The shape
 * within the shape area is determined by the shape property.
 *
 * @author mvvijesh
 */
public class GroupElement {

    public static final int LABEL_TEXT = 0;
    public static final int LABEL_FONT = 1;
    public static final int LABEL_COLOR = 2;
    public static final int LABEL_ALIGNMENT = 3;
    public static final int LABEL_POSITION = 4;
    public static final int VALUE = 5;
    public static final int SHAPE = 6;
    public static final int SHAPE_COLOR = 7;
    public static String[] OWN_PROPERTIES = {
        ".element.label.text",
        ".element.label.font",
        ".element.label.color",
        ".element.label.alignment",
        ".element.label.position",
        ".element.value",
        ".element.shape",
        ".element.shape.color",};
    private Item item = null;
    // default values
    public static final String defaultLabelText = "Label";
    public static final Font defaultLabelFont = new Font("Arial", Font.PLAIN, 25);
    public static final Color defaultLabelColor = Color.BLACK;
    public static final Alignment defaultLabelAlignment = Alignment.CENTER;
    public static final Direction defaultLabelPosition = Direction.DOWN;
    public static final Float defaultValue = 1f;
    public static final Shape defaultShape = Shape.RECTANGLE;
    public static final Color defaultShapeColor = Color.BLACK;
    public static final Object[] defaultValues = {
        defaultLabelText,
        defaultLabelFont,
        defaultLabelColor,
        defaultLabelAlignment,
        defaultLabelPosition,
        defaultValue,
        defaultShape,
        defaultShapeColor};
    private PreviewProperty[] previewProperties = new PreviewProperty[OWN_PROPERTIES.length];

    public GroupElement(Item item) {
        this.item = item;

        // the rest of the values are default
        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            addElementProperty(i, defaultValues[i]);
        }
    }

    /**
     *
     * @param item - the item being built
     * @param labelText
     * @param labelFont
     * @param labelColor
     * @param labelAlignment
     * @param labelPosition - only UP and DOWN values are supported
     * @param value - the numerical value that the group element represents
     * @param shape - possible values: RECTANGLE, CIRCLE, TRIANGLE
     * @param shapeColor
     */
    GroupElement(Item item, String labelText, Font labelFont, Color labelColor, Alignment labelAlignment, Direction labelPosition, Float value, Shape shape, Color shapeColor) {
        this.item = item;
        defaultValues[LABEL_TEXT] = labelText;
        defaultValues[LABEL_FONT] = labelFont;
        defaultValues[LABEL_COLOR] = labelColor;
        defaultValues[LABEL_ALIGNMENT] = labelAlignment;
        defaultValues[LABEL_POSITION] = labelPosition;
        defaultValues[VALUE] = value;
        defaultValues[SHAPE] = shape;
        defaultValues[SHAPE_COLOR] = shapeColor;

        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            addElementProperty(i, defaultValues[i]);
        }
    }

    /**
     *
     * @param propertyIndex - index of the property in OWN_PROPERTIES
     * @param value - value that the property holds
     */
    private void addElementProperty(int propertyIndex, Object value) {
        PreviewProperty previewProperty = null;
        String propertyString = LegendModel.getProperty(OWN_PROPERTIES, (Integer) item.getData(LegendItem.ITEM_INDEX), propertyIndex);
        switch (propertyIndex) {
            case LABEL_TEXT:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        "GroupItem" + OWN_PROPERTIES[LABEL_TEXT],
                        "GroupItem" + OWN_PROPERTIES[LABEL_TEXT],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case LABEL_FONT:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        "GroupItem" + OWN_PROPERTIES[LABEL_FONT],
                        "GroupItem" + OWN_PROPERTIES[LABEL_FONT],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case LABEL_COLOR:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        "GroupItem" + OWN_PROPERTIES[LABEL_COLOR],
                        "GroupItem" + OWN_PROPERTIES[LABEL_COLOR],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case LABEL_ALIGNMENT:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        "GroupItem" + OWN_PROPERTIES[LABEL_ALIGNMENT],
                        "GroupItem" + OWN_PROPERTIES[LABEL_ALIGNMENT],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case LABEL_POSITION:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Direction.class,
                        "GroupItem" + OWN_PROPERTIES[LABEL_POSITION],
                        "GroupItem" + OWN_PROPERTIES[LABEL_POSITION],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case VALUE:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        "GroupItem" + OWN_PROPERTIES[VALUE],
                        "GroupItem" + OWN_PROPERTIES[VALUE],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case SHAPE:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Shape.class,
                        "GroupItem" + OWN_PROPERTIES[SHAPE],
                        "GroupItem" + OWN_PROPERTIES[SHAPE],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case SHAPE_COLOR:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        "GroupItem" + OWN_PROPERTIES[SHAPE_COLOR],
                        "GroupItem" + OWN_PROPERTIES[SHAPE_COLOR],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
        }

        previewProperties[propertyIndex] = previewProperty;
    }

    public PreviewProperty[] getPreviewProperties() {
        return previewProperties;
    }

    public PreviewProperty getPreviewProperty(int propIndex) {
        return previewProperties[propIndex];
    }
}