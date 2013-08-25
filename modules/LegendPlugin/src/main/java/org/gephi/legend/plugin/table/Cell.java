/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.table;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import org.gephi.legend.api.AbstractItem;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.legend.spi.LegendItem.Shape;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;

/**
 * This class represents a table cell. This is NOT a main legend class. This is
 * a supporting class that just holds individual properties. Hence, the builders
 * and renderers are not necessary.
 *
 * @author mvvijesh
 */
public class Cell {

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_SHAPE = 2;
    public static final int BACKGROUND_COLOR = 0;
    public static final int BORDER_COLOR = 1;
    public static final int CELL_FONT = 2;
    public static final int CELL_ALIGNMENT = 3;
    public static final int CELL_FONT_COLOR = 4;
    public static final int CELL_TEXT_CONTENT = 5;
    public static final int CELL_SHAPE_SHAPE = 6;
    public static final int CELL_SHAPE_COLOR = 7;
    public static final int CELL_SHAPE_VALUE = 8;
    public static final int CELL_IMAGE_URL = 9;
    public static final int CELL_IMAGE_IS_SCALING = 10;
    public static final int CELL_TYPE = 11;
    public static String[] OWN_PROPERTIES = {
        ".cell.background.color",
        ".cell.border.color",
        ".cell.font.face",
        ".cell.font.alignment",
        ".cell.font.color",
        ".cell.text.content",
        ".cell.shape.shape",
        ".cell.shape.color",
        ".cell.shape.value",
        ".cell.image.url",
        ".cell.image.is.scaling",
        ",cell.type"
    };
    // define the default properties of the cell
    private Item item = null;
    private Integer row = null;
    private Integer column = null;
    public static final Color backgroundColor = new Color(1f, 1f, 1f, 0.5f);
    public static final Color borderColor = Color.BLACK;
    public static final Font cellFont = new Font("Arial", Font.PLAIN, 25);
    public static final Alignment cellAlignment = Alignment.CENTER;
    public static final Color cellFontColor = Color.BLACK;
    public static final String cellTextContent = "click to modify properties";
    public static final Shape cellShapeShape = Shape.RECTANGLE;
    public static final Color cellShapeColor = new Color(0f, 0f, 0f, 0.75f);
    public static final Float cellShapeValue = 1f;
    public static final String cellImageURL = "/org/gephi/legend/graphics/invisible.png";
    public static final Boolean cellImageIsScaling = true;
    public static final int cellType = TYPE_TEXT;
    public static final Object[] defaultValues = {
        backgroundColor,
        borderColor,
        cellFont,
        cellAlignment,
        cellFontColor,
        cellTextContent,
        cellShapeShape,
        cellShapeColor,
        cellShapeValue,
        cellImageURL,
        cellImageIsScaling,
        cellType
    };
    private PreviewProperty[] previewProperties = new PreviewProperty[OWN_PROPERTIES.length];

    Cell(Item item, int row, int column) {
        this.item = item;
        this.row = row;
        this.column = column;

        // the rest of the values are default
        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            addCellProperty(row, column, i, defaultValues[i]);
        }
    }

    Cell(Item item, int row, int column, Color backgroundColor, Color borderColor, Font cellFont, Alignment cellAlignment, Color cellFontColor, String cellTextContent, Shape cellShapeShape, Color cellShapeColor, float cellShapeValue, String cellImageURL, Boolean cellImageIsScaling, int cellType) {
        this.item = item;
        this.row = row;
        this.column = column;
        defaultValues[BACKGROUND_COLOR] = backgroundColor;
        defaultValues[BORDER_COLOR] = borderColor;
        defaultValues[CELL_FONT] = cellFont;
        defaultValues[CELL_ALIGNMENT] = cellAlignment;
        defaultValues[CELL_FONT_COLOR] = cellFontColor;
        defaultValues[CELL_TEXT_CONTENT] = cellTextContent;
        defaultValues[CELL_SHAPE_SHAPE] = cellShapeShape;
        defaultValues[CELL_SHAPE_COLOR] = cellShapeColor;
        defaultValues[CELL_SHAPE_VALUE] = cellShapeValue;
        defaultValues[CELL_IMAGE_URL] = cellImageURL;
        defaultValues[CELL_IMAGE_IS_SCALING] = cellImageIsScaling;
        defaultValues[CELL_TYPE] = cellType;

        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            addCellProperty(row, column, i, defaultValues[i]);
        }
    }

    private void addCellProperty(int row, int column, int propertyIndex, Object value) {
        /*
         // get the list of preview properties, convert to an array list, 
         // During the creation of the legend, previewPropertiesList will be null, since the OWN_PROPERTIES are yet to be defined.
         ArrayList<PreviewProperty> previewProperties = new ArrayList<PreviewProperty>();
         if (getItem().getData(LegendItem.OWN_PROPERTIES) != null) {
         // populate previewProperties with all the preview properties
         Object[] previewPropertyObjectList = getItem().getData(LegendItem.OWN_PROPERTIES);
         PreviewProperty[] previewPropertyList = new PreviewProperty[previewPropertyObjectList.length];
         for (Object prop : previewPropertyObjectList) {
         previewProperties.add((PreviewProperty) prop);
         }
         }
         */

        PreviewProperty previewProperty = null;
        String propertyString = LegendModel.getProperty(OWN_PROPERTIES, (Integer) item.getData(LegendItem.ITEM_INDEX), propertyIndex);
        switch (propertyIndex) {
            case BACKGROUND_COLOR:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[BACKGROUND_COLOR],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[BACKGROUND_COLOR],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case BORDER_COLOR:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[BORDER_COLOR],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[BORDER_COLOR],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_FONT:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Font.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_FONT],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_FONT],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_ALIGNMENT:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Alignment.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_ALIGNMENT],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_ALIGNMENT],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_FONT_COLOR:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_FONT_COLOR],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_FONT_COLOR],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_TEXT_CONTENT:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_TEXT_CONTENT],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_TEXT_CONTENT],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_SHAPE_SHAPE:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Shape.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_SHAPE_SHAPE],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_SHAPE_SHAPE],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_SHAPE_COLOR:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Color.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_SHAPE_COLOR],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_SHAPE_COLOR],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_SHAPE_VALUE:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Float.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_SHAPE_VALUE],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_SHAPE_VALUE],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_IMAGE_URL:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_IMAGE_URL],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_IMAGE_URL],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_IMAGE_IS_SCALING:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Boolean.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_IMAGE_IS_SCALING],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_IMAGE_IS_SCALING],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_TYPE:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_TYPE],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_TYPE],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
        }

        previewProperties[propertyIndex] = previewProperty;
    }

    public PreviewProperty[] getPreviewProperties() {
        return previewProperties;
    }
}