package org.gephi.legend.plugin.table;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Shape;
import org.openide.util.lookup.ServiceProvider;

/**
 * This is the default custom item builder for the table legend.
 *
 * This class is exposed as a service. The UI uses these services and the Lookup
 * API to show the list of available custom builders. This class defines the
 * cells and its properties. If you want to change the way the cells look,
 * modify the populateTable method according to your custom needs.
 *
 * @author mvvijesh, edubecks
 */
@ServiceProvider(service = CustomTableItemBuilder.class, position = 1)
public class Default implements CustomTableItemBuilder {

    protected int tableNumberOfRows = 2;
    protected int tableNumberOfColumns = 3;
    protected Font cellFont = Cell.defaultCellFont;
    protected Color cellFontColor = Cell.defaultCellFontColor;
    protected LegendItem.Alignment cellFontAlignment = Cell.defaultCellAlignment;
    protected Color cellBackgroundColor = Cell.defaultBackgroundColor;
    protected Color cellBorderColor = Cell.defaultBorderColor;
    protected String cellTextContent = Cell.defaultCellTextContent;
    protected Shape cellShapeShape = Cell.defaultCellShapeShape;
    protected Color cellShapeColor = Cell.defaultCellShapeColor;
    protected Float cellShapeValue = Cell.defaultCellShapeValue;
    protected File cellImageFile = Cell.defaultCellImageFile;
    protected Boolean cellImageIsScaling = Cell.defaultCellImageIsScaling;
    protected int cellType = Cell.defaultCellType;

    /**
     * based on the values defined above, this method will populate the table
     * item with rows and columns of cells.
     *
     * @param tableItem - the item being built
     */
    @Override
    public void populateTable(TableItem tableItem) {
        //build the basic default table
        for (int i = 0; i < tableNumberOfRows; i++) {
            tableItem.addRow(i, cellBackgroundColor, cellBorderColor, cellFont, cellFontAlignment, cellFontColor, cellTextContent, cellShapeShape, cellShapeColor, cellShapeValue, cellImageFile, cellImageIsScaling, cellType);
        }

        // at this point, only rows have been added. The number of columns is still zero.
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