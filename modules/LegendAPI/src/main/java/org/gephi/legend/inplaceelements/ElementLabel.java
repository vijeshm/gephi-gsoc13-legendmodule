package org.gephi.legend.inplaceelements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Map;
import org.gephi.legend.inplaceeditor.Column;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.inplaceeditor.Row;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewProperty;

/**
 * a simple text label on the inplace editor.
 *
 * Text labels are ungrouped elements. The data hashmap is expected to contain
 * three entries: LABEL_TEXT, LABEL_COLOR, LABEL_FONT. The added element should
 * declare the number of unit-blocks that it would require. (see
 * inplaceItemRenderer for more details on unit-blocks). The inplace item
 * renderer uses the data hashmap and the number of unit-blocks it would require
 * to render the content. Generally, the default label color and label font is
 * specified in the InplaceItemRenderer class, but you can use your own font and
 * color.
 *
 * @author mvvijesh
 */
public class ElementLabel extends BaseElement {

    public static final String LABEL_TEXT = "element.label.text";
    public static final String LABEL_FONT = "element.label.font";
    public static final String LABEL_COLOR = "element.label.color";

    public ElementLabel(ELEMENT_TYPE type, int itemIndex, PreviewProperty property, InplaceEditor ipeditor, Row row, Column col, Map<String, Object> data, Boolean isGrouped, Boolean isDefault, Object propertyValue) {
        super(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
    }

    @Override
    public void onSelect() {
        // do nothing for a label
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
        Font labelFont = (Font) data.get(LABEL_FONT);
        String labelText = (String) data.get(LABEL_TEXT);
        Font scaledFont = labelFont.deriveFont((float) (labelFont.getSize() / target.getScaling())); // get the current scaling factor and scale the font accordingly
        graphics2d.setFont(scaledFont);
        int fontWidth = getFontWidth(graphics2d, labelText);
        numberOfBlocks = fontWidth / blockUnitSize + 1; // add 1 because integer division floors the value
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
        Font labelFont = (Font) data.get(LABEL_FONT);
        labelFont = labelFont.deriveFont((float) (labelFont.getSize() / target.getScaling()));
        Color labelColor = (Color) data.get(LABEL_COLOR);
        String labelText = (String) data.get(LABEL_TEXT);

        graphics2d.setFont(labelFont);
        int fontHeight = getFontHeight(graphics2d);

        graphics2d.setColor(labelColor);
        graphics2d.drawString(labelText,
                (editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                (editorOriginY + borderSize) + rowBlock * blockUnitSize + blockUnitSize / 2 + fontHeight / 2);

        // update the geometry of the inplace editor
        setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                blockUnitSize * numberOfBlocks,
                blockUnitSize);
    }
}