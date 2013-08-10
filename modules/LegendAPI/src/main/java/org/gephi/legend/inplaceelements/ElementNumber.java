/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.openide.util.Lookup;

/**
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
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        PreviewProperties previewProperties = previewModel.getProperties();

        String newValueString = (String) JOptionPane.showInputDialog(null, "New Value:", property.getDisplayName(), JOptionPane.PLAIN_MESSAGE, null, null, property.getValue());
        if (newValueString != null) {
            try {
                Integer newNumber = Integer.parseInt(newValueString);
                property.setValue(newNumber);
                previewProperties.putValue(property.getName(), newNumber);
            } catch (NumberFormatException e) {
            }
        }
    }

    @Override
    public void computeNumberOfBlocks(Graphics2D graphics2d, G2DTarget target, int blockUnitSize) {
        Font numberFont = (Font) data.get(NUMBER_FONT);
        Font scaledFont = numberFont.deriveFont((float) (numberFont.getSize() / target.getScaling()));
        graphics2d.setFont(scaledFont);

        String displayString = "" + property.getValue();
        int fontWidth = getFontWidth(graphics2d, (String) displayString);
        numberOfBlocks = fontWidth / blockUnitSize + 1;
    }

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

        setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                blockUnitSize * numberOfBlocks,
                blockUnitSize);
    }
}
