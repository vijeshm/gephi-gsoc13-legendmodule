/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceelements;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javax.imageio.ImageIO;
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
public class ElementImage extends BaseElement {

    public static final String IMAGE_BOOL = "element.image.boolean";
    public static final String IMAGE_IF_TRUE = "element.image.true";
    public static final String IMAGE_IF_FALSE = "element.image.false";

    public ElementImage(ELEMENT_TYPE type, int itemIndex, PreviewProperty property, InplaceEditor ipeditor, Row row, Column col, Map<String, Object> data, Boolean isGrouped, Boolean isDefault, Object propertyValue) {
        super(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
    }

    @Override
    public void onSelect() {
        if (property != null) { // prop is null when providing support for static images.
            if (isGrouped) {
                property.setValue(data.get(GROUP_PROPERTY_VALUE));
                
                // turn off the switch for all the elements. 
                ArrayList<BaseElement> groupElements = col.getElements();
                for(BaseElement element : groupElements) {
                    element.getAssociatedData().put(IMAGE_BOOL, false);
                }
                
                // turn on the switch for this element
                data.put(IMAGE_BOOL, true);
            } else {
                Boolean isPicked = (Boolean) data.get(IMAGE_BOOL);
                data.put(IMAGE_BOOL, !isPicked);
                property.setValue(!isPicked);
            }
        }
    }

    @Override
    public int setNumberOfBlocks(Graphics2D graphics2d, int blockUnitSize) {
        numberOfBlocks = 1;
        return numberOfBlocks;
    }

    @Override
    public void renderElement(Graphics2D graphics2d, int blockUnitSize, int editorOriginX, int editorOriginY, int borderSize, int rowBlock, int currentElementsCount) {
        try {
            setNumberOfBlocks(graphics2d, blockUnitSize);

            String imgTrue = (String) data.get(IMAGE_IF_TRUE);
            String imgFalse = (String) data.get(IMAGE_IF_FALSE);
            Boolean propertyValue = (Boolean) data.get(IMAGE_BOOL);
            Boolean isSelectedWithinGroup = (Boolean) data.get(SELECTED_WITHIN_GROUP);
            
            // load the default image (unselected)
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream(imgFalse));
            // if atleast one element is selected, the first one is taken into consideration
            // if no elements are selected, the last one is NOT forcibly selected
            if (isGrouped && isSelectedWithinGroup || !isGrouped && propertyValue) {
                img = ImageIO.read(getClass().getResourceAsStream(imgTrue));
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
