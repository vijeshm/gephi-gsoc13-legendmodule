/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.table;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Shape;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author mvvijesh
 */
@ServiceProvider(service = CustomTableItemBuilder.class, position = 1)
public class Default implements CustomTableItemBuilder {
    // this class defines the cells and its properties.
    // If you want to change the way the cells look, modify the populateTable method according to your custom needs.
    
    protected int tableNumberOfRows = 2;
    protected int tableNumberOfColumns = 3;
    
    protected Font cellFont = Cell.cellFont;
    protected Color cellFontColor = Cell.cellFontColor;
    protected LegendItem.Alignment cellFontAlignment = Cell.cellAlignment;
    protected Color cellBackgroundColor = Cell.backgroundColor;
    protected Color cellBorderColor = Cell.borderColor;
    protected String cellTextContent = Cell.cellTextContent;
    protected Shape cellShapeShape = Cell.cellShapeShape;
    protected Color cellShapeColor = Cell.cellShapeColor;
    protected Float cellShapeValue = Cell.cellShapeValue;
    protected File cellImageFile = Cell.cellImageFile;
    protected Boolean cellImageIsScaling = Cell.cellImageIsScaling;
    protected int cellType = Cell.cellType;

    @Override
    public void populateTable(TableItem tableItem) {
        //build the basic default table
        for (int i = 0; i < tableNumberOfRows; i++) {
            tableItem.addRow(i, cellBackgroundColor, cellBorderColor, cellFont, cellFontAlignment, cellFontColor, cellTextContent, cellShapeShape, cellShapeColor, cellShapeValue, cellImageFile, cellImageIsScaling, cellType);
        }

        for (int i = 0; i < tableNumberOfColumns; i++) {
            tableItem.addColumn(i, cellBackgroundColor, cellBorderColor, cellFont, cellFontAlignment, cellFontColor, cellTextContent, cellShapeShape, cellShapeColor, cellShapeValue, cellImageFile, cellImageIsScaling, cellType);
        }
    }

    @Override
    public String getDescription() {
        return DEFAULT_DESCRIPTION;
    }

    @Override
    public String getTitle() {
        return DEFAULT_TITLE;
    }

    @Override
    public boolean isAvailableToBuild() {
        return true;
    }

    @Override
    public String stepsNeededToBuild() {
        return NONE_NEEDED;
    }
    
    @Override
    public String toString() {
        return DEFAULT_TITLE;
    }
}