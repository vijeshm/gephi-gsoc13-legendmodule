package org.gephi.legend.inplaceelements;

import com.bric.swing.ColorPicker;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Map;
import org.gephi.legend.inplaceeditor.Column;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.inplaceeditor.Row;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.openide.util.Lookup;

/**
 * a color element on an inplace editor.
 *
 * Color elements cannot be grouped. The data hashmap is expected to contain a
 * single entry, color margin. This is a float value between 0 and 1 which
 * specifies the percentage of the unit-block the color should occupy. (see
 * inplaceItemRenderer for more details on unit blocks). The added element
 * should declare the number of unit-blocks that it would require. (see
 * inplaceItemRenderer for more details on unit-blocks). The inplace item
 * renderer uses the data hashmap and the number of unit-blocks it would require
 * to render the content.
 *
 * @author mvvijesh
 */
public class ElementColor extends BaseElement {

    public static final String COLOR_MARGIN = "element.color.margin";

    public ElementColor(ELEMENT_TYPE type, int itemIndex, PreviewProperty property, InplaceEditor ipeditor, Row row, Column col, Map<String, Object> data, Boolean isGrouped, Boolean isDefault, Object propertyValue) {
        super(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
    }

    @Override
    public void onSelect() {
        // get the preview properties from the preview model
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        PreviewProperties previewProperties = previewModel.getProperties();

        // show a color picker, get the picked value and set the property
        Color selectedColor = ColorPicker.showDialog(null, (Color) property.getValue(), true);
        if (selectedColor != null) {
            property.setValue(selectedColor);
            previewProperties.putValue(property.getName(), selectedColor);
        }
    }

    /**
     *
     * @param graphics2d - the graphics object for the target
     * @param target - the target onto which the item should be rendered - SVG,
     * PDF or G2D
     * @param blockUnitSize - unit size of a block, as defined in
     * InplaceItemRenderer
     */
    @Override
    public void computeNumberOfBlocks(Graphics2D graphics2d, G2DTarget target, int blockUnitSize) {
        // a color element always takes one unit-block 
        numberOfBlocks = 1;
    }

    /**
     *
     * @param graphics2d - the graphics object for the target
     * @param target - the target onto which the item should be rendered - SVG,
     * PDF or G2D
     * @param blockUnitSize - unit size of a block, as defined in
     * InplaceItemRenderer
     * @param editorOriginX - x-coordinate of the inplace editor
     * @param editorOriginY - y-coordinate of the inplace editor
     * @param borderSize - size of the border, as defined in InplaceItemRenderer
     * @param rowBlock - the row number
     * @param currentElementsCount - the number of unit-blocks surpassed in the
     * current row
     */
    @Override
    public void renderElement(Graphics2D graphics2d, G2DTarget target, int blockUnitSize, int editorOriginX, int editorOriginY, int borderSize, int rowBlock, int currentElementsCount) {
        Color color = property.getValue();
        Float colorMargin = (Float) data.get(COLOR_MARGIN);

        graphics2d.setColor(color);
        // some margin on all sides of the element
        graphics2d.fillRect((int) ((editorOriginX + borderSize) + currentElementsCount * blockUnitSize + colorMargin * blockUnitSize),
                (int) ((editorOriginY + borderSize) + rowBlock * blockUnitSize + colorMargin * blockUnitSize),
                (int) ((1 - 2 * colorMargin) * blockUnitSize),
                (int) ((1 - 2 * colorMargin) * blockUnitSize));

        // update the geometry of the inplace editor
        setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                blockUnitSize,
                blockUnitSize);
    }
}