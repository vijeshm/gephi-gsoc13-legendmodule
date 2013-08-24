/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.table;

import java.awt.Color;
import java.awt.Font;
import org.gephi.legend.spi.LegendItem;
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
    
    protected String cellContent = Cell.cellContent;
    protected Font cellFont = Cell.cellFont;
    protected Color cellFontColor = Cell.cellFontColor;
    protected LegendItem.Alignment cellFontAlignment = Cell.cellAlignment;
    protected Color cellBackgroundColor = Cell.backgroundColor;
    protected Color cellBorderColor = Cell.borderColor;

    @Override
    public void populateTable(TableItem tableItem) {
        //build the basic default table
        for (int i = 0; i < tableNumberOfRows; i++) {
            tableItem.addRow(i, cellBackgroundColor, cellBorderColor, cellFont, cellFontAlignment, cellFontColor, cellContent);
        }

        for (int i = 0; i < tableNumberOfColumns; i++) {
            tableItem.addColumn(i, cellBackgroundColor, cellBorderColor, cellFont, cellFontAlignment, cellFontColor, cellContent);
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