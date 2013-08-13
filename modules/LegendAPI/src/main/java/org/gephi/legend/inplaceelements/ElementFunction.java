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
import org.gephi.legend.inplaceeditor.InplaceClickResponse;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.inplaceeditor.Row;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewProperty;

/**
 *
 * @author mvvijesh
 */
public class ElementFunction extends BaseElement {

    public static final String FUNCTION_IMAGE = "element.function.image";
    public static final String FUNCTION_CLICK_RESPONDER = "element.function.click.responder";

    public ElementFunction(ELEMENT_TYPE type, int itemIndex, PreviewProperty property, InplaceEditor ipeditor, Row row, Column col, Map<String, Object> data, Boolean isGrouped, Boolean isDefault, Object propertyValue) {
        super(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
    }

    @Override
    public void onSelect() {
        InplaceClickResponse responder = (InplaceClickResponse) data.get(FUNCTION_CLICK_RESPONDER);
        responder.performAction(ipeditor);
    }

    @Override
    public void computeNumberOfBlocks(Graphics2D graphics2d, G2DTarget target, int blockUnitSize) {
        numberOfBlocks = 1;
    }

    @Override
    public void renderElement(Graphics2D graphics2d, G2DTarget target, int blockUnitSize, int editorOriginX, int editorOriginY, int borderSize, int rowBlock, int currentElementsCount) {
        try {
            String functionImage = (String) data.get(FUNCTION_IMAGE);
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream(functionImage));
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

            currentElementsCount += 1;
        } catch (IOException e) {
        }
    }
}
