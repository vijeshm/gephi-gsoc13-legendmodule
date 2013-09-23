package org.gephi.legend.plugin.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.File;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.legend.spi.LegendItem.Shape;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;

/**
 * This class represents a table cell.
 *
 * This is not a main legend class. This is a supporting class that just holds
 * individual properties. Hence, the builders and renderers for this class are
 * not necessary.
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
    public static final int CELL_SHAPE_WIDTH = 9;
    public static final int CELL_IMAGE_FILE = 10;
    public static final int CELL_IMAGE_IS_SCALING = 11;
    public static final int CELL_IMAGE_WIDTH = 12;
    public static final int CELL_IMAGE_HEIGHT = 13;
    public static final int CELL_TYPE = 14;
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
        ".cell.shape.width",
        ".cell.image.file",
        ".cell.image.is.scaling",
        ".cell.image.width",
        ".cell.image.height",
        ",cell.type"
    };
    // define the default properties of the cell
    private Item item = null;
    private Integer row = null;
    private Integer column = null;
    public static final Color defaultBackgroundColor = new Color(1f, 1f, 1f, 0.5f);
    public static final Color defaultBorderColor = Color.BLACK;
    public static final Font defaultCellFont = new Font("Arial", Font.PLAIN, 25);
    public static final Alignment defaultCellAlignment = Alignment.CENTER;
    public static final Color defaultCellFontColor = Color.BLACK;
    public static final String defaultCellTextContent = "click to modify properties";
    public static final Shape defaultCellShapeShape = Shape.RECTANGLE;
    public static final Color defaultCellShapeColor = new Color(0f, 0f, 0f, 0.75f);
    public static final Float defaultCellShapeValue = 1f;
    public static final Integer defaultCellShapeWidth = 100;
    public static final File defaultCellImageFile = new File("/");
    public static final Boolean defaultCellImageIsScaling = true;
    public static final Integer defaultCellImageWidth = 50;
    public static final Integer defaultCellImageHeight = 50;
    public static final int defaultCellType = TYPE_TEXT;
    public static final Object[] defaultValues = {
        defaultBackgroundColor,
        defaultBorderColor,
        defaultCellFont,
        defaultCellAlignment,
        defaultCellFontColor,
        defaultCellTextContent,
        defaultCellShapeShape,
        defaultCellShapeColor,
        defaultCellShapeValue,
        defaultCellShapeWidth,
        defaultCellImageFile,
        defaultCellImageIsScaling,
        defaultCellImageWidth,
        defaultCellImageHeight,
        defaultCellType
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

    /**
     *
     * @param item - item to which the cell belongs to
     * @param row - row number of the cell
     * @param column - column number of the cell
     * @param backgroundColor
     * @param borderColor
     * @param cellFont
     * @param cellAlignment
     * @param cellFontColor
     * @param cellTextContent
     * @param cellShapeShape
     * @param cellShapeColor
     * @param cellShapeValue
     * @param cellImageFile
     * @param cellImageIsScaling
     * @param cellType - cell type can be TYPE_TEXT, TYPE_IMAGE, TYPE_SHAPE
     */
    Cell(Item item, int row, int column, Color backgroundColor, Color borderColor, Font cellFont, Alignment cellAlignment, Color cellFontColor, String cellTextContent, Shape cellShapeShape, Color cellShapeColor, float cellShapeValue, File cellImageFile, Boolean cellImageIsScaling, int cellType) {
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
        defaultValues[CELL_IMAGE_FILE] = cellImageFile;
        defaultValues[CELL_IMAGE_IS_SCALING] = cellImageIsScaling;
        defaultValues[CELL_TYPE] = cellType;

        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            addCellProperty(row, column, i, defaultValues[i]);
        }
    }

    /**
     *
     * @param row - row number of the cell
     * @param column - column number of the cell
     * @param propertyIndex - index of the property listed in OWN_PROPERTIES
     * @param value - value that the property takes
     */
    private void addCellProperty(int row, int column, int propertyIndex, Object value) {
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

            case CELL_SHAPE_WIDTH:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_SHAPE_WIDTH],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_SHAPE_WIDTH],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_IMAGE_FILE:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        File.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_IMAGE_FILE],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_IMAGE_FILE],
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

            case CELL_IMAGE_WIDTH:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_IMAGE_WIDTH],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_IMAGE_WIDTH],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;

            case CELL_IMAGE_HEIGHT:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        Integer.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_IMAGE_HEIGHT],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_IMAGE_HEIGHT],
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

    /**
     * get the required width of the cell, in accordance with the cell type.
     *
     * At any given point in time, a cell can be of a particular type. This
     * method will return the width that would be required to completely display
     * the cell. It is used to determine the width of the column and auto adjust
     * the column if the flag is set. (see TableItemRenderer for more details).
     *
     * @param graphics2d - graphics object for the target
     * @return the width required to render the cell, ideally.
     */
    public Integer getActiveWidth(Graphics2D graphics2d) {
        switch ((Integer) previewProperties[CELL_TYPE].getValue()) {
            case TYPE_TEXT:
                Font currentCellFont = (Font) previewProperties[CELL_FONT].getValue();
                graphics2d.setFont(currentCellFont);
                FontMetrics fontMetrics = graphics2d.getFontMetrics(currentCellFont);
                return fontMetrics.stringWidth((String) previewProperties[CELL_TEXT_CONTENT].getValue());

            case TYPE_IMAGE:
                return (int) ((Integer) previewProperties[CELL_IMAGE_WIDTH].getValue());

            case TYPE_SHAPE:
                return (int) ((Integer) previewProperties[CELL_SHAPE_WIDTH].getValue());

            default:
                return -1;
        }
    }

    /**
     * this method update the width of the shape, width and height of the image
     *
     * @param columnWidth - the width of the column
     * @param columnHeight - the height of the column
     */
    public void updateWidthsHeights(int columnWidth, int columnHeight) {
        PreviewProperty cellShapeWidth = previewProperties[CELL_SHAPE_WIDTH];
        PreviewProperty cellImageWidth = previewProperties[CELL_IMAGE_WIDTH];
        PreviewProperty cellImageHeight = previewProperties[CELL_IMAGE_HEIGHT];

        if ((int) ((Integer) cellShapeWidth.getValue()) > columnWidth) {
            cellShapeWidth.setValue((int) (columnWidth));
        }

        if ((int) ((Integer) cellImageWidth.getValue()) > columnWidth) {
            cellImageWidth.setValue((int) (columnWidth));
        }

        if ((int) ((Integer) cellImageHeight.getValue()) > columnHeight) {
            cellImageHeight.setValue((int) (columnHeight));
        }
    }
}