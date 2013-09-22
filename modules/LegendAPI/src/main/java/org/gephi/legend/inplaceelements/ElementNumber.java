package org.gephi.legend.inplaceelements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Map;
import javax.swing.JOptionPane;
import org.gephi.legend.inplaceeditor.Column;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.inplaceeditor.Row;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewProperty;

/**
 * a number element on an inplace editor.
 *
 * Number elements cannot be grouped. The data hashmap is expected to contain
 * two entries: NUMBER_COLOR, NUMBER_FONT. The added element should declare the
 * number of unit-blocks that it would require. (see inplaceItemRenderer
 * description about unit-blocks). The inplace item renderer uses the data
 * hashmap and the number of unit-blocks it would require to render the content.
 * Generally, the default number color and number font is specified in the
 * InplaceItemRenderer class, but you can use your own font and color.
 *
 * @author mvvijesh
 */
public class ElementNumber extends BaseElement {

    public static final String NUMBER_FONT = "element.number.font";
    public static final String NUMBER_COLOR = "element.number.color";

    public ElementNumber(ELEMENT_TYPE type, int itemIndex, PreviewProperty property, InplaceEditor ipeditor, Row row, Column col, Map<String, Object> data, Boolean isGrouped, Boolean isDefault, Object propertyValue) {
        super(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
    }

    @Override
    public void onSelect() {
        // display a dialog to enter a number
        String newValueString = (String) JOptionPane.showInputDialog(null, "New Value:", property.getDisplayName(), JOptionPane.PLAIN_MESSAGE, null, null, property.getValue());
        if (newValueString != null) {
            // check if the user has pressed cancel
            try {
                // based on whether the property type is an Integer or a Float, handle accordingly
                if (property.getType() == Integer.class) {
                    Integer newIntNumber = Integer.parseInt(newValueString);
                    property.setValue(newIntNumber);
                } else if (property.getType() == Float.class) {
                    Float newFloatNumber = Float.parseFloat(newValueString);
                    property.setValue(newFloatNumber);
                }
            } catch (NumberFormatException e) {
            }
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
        Font numberFont = (Font) data.get(NUMBER_FONT);
        Font scaledFont = numberFont.deriveFont((float) (numberFont.getSize() / target.getScaling())); // get the current scaling factor and scale the font accordingly
        graphics2d.setFont(scaledFont);

        String displayString = "" + property.getValue();
        int fontWidth = getFontWidth(graphics2d, (String) displayString);
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
        Font numberFont = (Font) data.get(NUMBER_FONT);
        numberFont = numberFont.deriveFont((float) (numberFont.getSize() / target.getScaling()));
        Color numberColor = (Color) data.get(NUMBER_COLOR);

        graphics2d.setFont(numberFont);
        String displayString = "" + property.getValue();
        int fontWidth = getFontWidth(graphics2d, (String) displayString);
        int fontHeight = getFontHeight(graphics2d);

        graphics2d.setColor(numberColor);
        graphics2d.drawString(displayString,
                (editorOriginX + borderSize) + currentElementsCount * blockUnitSize + numberOfBlocks * blockUnitSize / 2 - fontWidth / 2,
                (editorOriginY + borderSize) + rowBlock * blockUnitSize + blockUnitSize / 2 + fontHeight / 2);

        // update the geometry of the inplace editor
        setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                blockUnitSize * numberOfBlocks,
                blockUnitSize);
    }
}