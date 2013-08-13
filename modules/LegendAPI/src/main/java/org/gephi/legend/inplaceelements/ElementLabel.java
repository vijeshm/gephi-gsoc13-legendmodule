/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

    @Override
    public void computeNumberOfBlocks(Graphics2D graphics2d, G2DTarget target, int blockUnitSize) {
        Font labelFont = (Font) data.get(LABEL_FONT);
        String labelText = (String) data.get(LABEL_TEXT);
        Font scaledFont = labelFont.deriveFont((float) (labelFont.getSize() / target.getScaling()));
        graphics2d.setFont(scaledFont);
        int fontWidth = getFontWidth(graphics2d, labelText);
        numberOfBlocks = fontWidth / blockUnitSize + 1;
    }

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

        setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                blockUnitSize * numberOfBlocks,
                blockUnitSize);
    }
}
