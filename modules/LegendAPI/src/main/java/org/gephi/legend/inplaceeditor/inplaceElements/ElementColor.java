/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceeditor.inplaceElements;

import com.bric.swing.ColorPicker;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Map;
import org.gephi.legend.inplaceeditor.Column;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.inplaceeditor.Row;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.openide.util.Lookup;

/**
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
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        PreviewProperties previewProperties = previewModel.getProperties();

        Color selectedColor = ColorPicker.showDialog(null, (Color) property.getValue(), true);
        if (selectedColor != null) {
            property.setValue(selectedColor);
            previewProperties.putValue(property.getName(), selectedColor);
        }
    }

    @Override
    public void renderElement(Graphics2D graphics2d, int blockUnitSize, int editorOriginX, int editorOriginY, int borderSize, int rowBlock, int currentElementsCount) {
        numberOfBlocks = 1;
        Color color = property.getValue();
        Float colorMargin = (Float) data.get(COLOR_MARGIN);

        graphics2d.setColor(color);
        // some margin on all sides of the element
        graphics2d.fillRect((int) ((editorOriginX + borderSize) + currentElementsCount * blockUnitSize + colorMargin * blockUnitSize),
                (int) ((editorOriginY + borderSize) + rowBlock * blockUnitSize + colorMargin * blockUnitSize),
                (int) ((1 - 2 * colorMargin) * blockUnitSize),
                (int) ((1 - 2 * colorMargin) * blockUnitSize));

        setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                blockUnitSize,
                blockUnitSize);
    }
}
