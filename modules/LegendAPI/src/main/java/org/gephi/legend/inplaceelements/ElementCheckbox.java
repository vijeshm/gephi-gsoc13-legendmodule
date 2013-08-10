/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceelements;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;
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
public class ElementCheckbox extends BaseElement {

    public static final String IS_CHECKED = "element.checkbox.checked";

    public ElementCheckbox(ELEMENT_TYPE type, int itemIndex, PreviewProperty property, InplaceEditor ipeditor, Row row, Column col, Map<String, Object> data, Boolean isGrouped, Boolean isDefault, Object propertyValue) {
        super(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
    }

    @Override
    public void onSelect() {
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        PreviewProperties previewProperties = previewModel.getProperties();

        Boolean currentState = (Boolean) property.getValue();
        data.put(IS_CHECKED, !currentState);
        property.setValue(!currentState);
        previewProperties.putValue(property.getName(), !currentState);
    }
    
    @Override
    public void computeNumberOfBlocks(Graphics2D graphics2d, G2DTarget target,int blockUnitSize) {
        numberOfBlocks = 1;
    }

    @Override
    public void renderElement(Graphics2D graphics2d, G2DTarget target,int blockUnitSize, int editorOriginX, int editorOriginY, int borderSize, int rowBlock, int currentElementsCount) {
        try {
            computeNumberOfBlocks(graphics2d, target, blockUnitSize);
            
            Boolean isSelected = (Boolean) data.get(IS_CHECKED);
            BufferedImage img;
            if (isSelected) {
                img = ImageIO.read(getClass().getResourceAsStream("/org/gephi/legend/graphics/checked.png"));
            } else {
                img = ImageIO.read(getClass().getResourceAsStream("/org/gephi/legend/graphics/unchecked.png"));
            }

            graphics2d.drawImage(img,
                    (editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                    (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                    (editorOriginX + borderSize) + currentElementsCount * blockUnitSize + blockUnitSize,
                    (editorOriginY + borderSize) + rowBlock * blockUnitSize + blockUnitSize,
                    0,
                    0,
                    img.getWidth(),
                    img.getHeight(), null);

            setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                    (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                    blockUnitSize,
                    blockUnitSize);
        } catch (IOException e) {
        }
    }
}
