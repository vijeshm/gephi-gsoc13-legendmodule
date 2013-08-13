/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public static final int IS_DISPLAYING = 8;
    public static String[] OWN_PROPERTIES = {
        ".element.label.text",
        ".element.label.font",
        ".element.label.color",
        ".element.label.alignment",
        ".element.label.position",
        ".element.value",
        ".element.shape",
        ".element.shape.color",
        ".element.is.displaying"
    };
    private Item item = null;
    public static final String labelText = "Label";
    public static final Font labelFont = new Font("Arial", Font.PLAIN, 25);
    public static final Color labelColor = Color.BLACK;
    public static final Alignment labelAlignment = Alignment.CENTER;
    public static final Direction labelPosition = Direction.UP;
    public static final Float value = 1f;
    public static final Shape shape = Shape.RECTANGLE;
    public static final Color shapeColor = Color.BLACK;
    public static final Boolean isDisplaying = Boolean.TRUE;
    public static final Object[] defaultValues = {
        labelText,
        labelFont,
        labelColor,
        labelAlignment,
        labelPosition,
        value,
        shape,
        shapeColor,
        isDisplaying
    };
    private PreviewProperty[] previewProperties = new PreviewProperty[OWN_PROPERTIES.length];

    public GroupElement(Item item) {
        this.item = item;

        // the rest of the values are default
        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            addElementProperty(i, defaultValues[i]);
        }
    }

    GroupElement(Item item, String labelText, Font labelFont, Color labelColor, Alignment labelAlignment, Direction labelPosition, Float value, Shape shape, Color shapeColor, Boolean isDisplaying) {
        this.item = item;
        defaultValues[LABEL_TEXT] = labelText;
        defaultValues[LABEL_FONT] = labelFont;
        defaultValues[LABEL_COLOR] = labelColor;
        defaultValues[LABEL_ALIGNMENT] = labelAlignment;
        defaultValues[LABEL_POSITION] = labelPosition;
        defaultValues[VALUE] = value;
        defaultValues[SHAPE] = shape;
        defaultValues[SHAPE_COLOR] = shapeColor;
        defaultValues[IS_DISPLAYING] = isDisplaying;

        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            addElementProperty(i, defaultValues[i]);
        }
    }

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
                
                case IS_DISPLAYING: 
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        "GroupItem" + OWN_PROPERTIES[IS_DISPLAYING],
                        "GroupItem" + OWN_PROPERTIES[IS_DISPLAYING],
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