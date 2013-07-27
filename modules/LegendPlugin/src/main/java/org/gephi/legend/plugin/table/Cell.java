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

    public static final int BACKGROUND_COLOR = 0;
    public static final int BORDER_COLOR = 1;
    public static final int CELL_FONT = 2;
    public static final int CELL_ALIGNMENT = 3;
    public static final int CELL_FONT_COLOR = 4;
    public static final int CELL_CONTENT = 5;
    public static String[] OWN_PROPERTIES = {
        ".cell.background.color",
        ".cell.border.color",
        ".cell.font.face",
        ".cell.font.alignment",
        ".cell.font.color",
        ".cell.content"
    };

    // define the default properties of the cell
    private Item item = null;
    private Integer row = null;
    private Integer column = null;
    private Color backgroundColor = new Color(1f, 1f, 1f, 0.5f);
    private Color borderColor = Color.BLACK;
    private Font cellFont = new Font("Arial", Font.PLAIN, 20);
    private Alignment cellAlignment = Alignment.CENTER;
    private Color cellFontColor = Color.BLACK;
    private String cellContent = "click to modify";
    private Object[] defaultValues = {
        backgroundColor,
        borderColor,
        cellFont,
        cellAlignment,
        cellFontColor,
        cellContent
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

    Cell(Item item, int row, int column, Color backgroundColor, Color borderColor, Font cellFont, Alignment cellAlignment, Color cellFontColor, String cellContent) {
        this.item = item;
        this.row = row;
        this.column = column;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.cellFont = cellFont;
        this.cellAlignment = cellAlignment;
        this.cellFontColor = cellFontColor;
        this.cellContent = cellContent;

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

            case CELL_CONTENT:
                previewProperty = PreviewProperty.createProperty(
                        this,
                        propertyString,
                        String.class,
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_CONTENT],
                        "TableItem.cell." + row + "." + column + OWN_PROPERTIES[CELL_CONTENT],
                        PreviewProperty.CATEGORY_LEGEND_PROPERTY).setValue(value);
                break;
        }

        previewProperties[propertyIndex] = previewProperty;
    }
    
    public PreviewProperty[] getPreviewProperties() {
        return previewProperties;
    }
}