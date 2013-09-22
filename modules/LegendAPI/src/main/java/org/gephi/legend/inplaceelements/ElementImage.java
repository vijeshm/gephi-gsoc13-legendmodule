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
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewProperty;

/**
 * an image, grouped or ungrouped on the inplace editor.
 *
 * Image labels may or may not be grouped, depending upon the application logic.
 * The data hashmap is expected to contain three entries: IMAGE_BOOL,
 * IMAGE_IF_FALSE, IMAGE_IF_TRUE. If an image element is not grouped and the
 * associated preview property is of Boolean type, IMAGE_BOOL mirrors the value
 * of the property. if an image element is grouped, the associated preview
 * property may or may not be of Boolean type. IMAGE_BOOL comes in handy in
 * these cases. (refer ElementImage.java for more details). The added element
 * should declare the number of unit-blocks that it would require. (see
 * inplaceItemRenderer for more details on unit-blocks). The inplace item
 * renderer uses the data hashmap and the number of unit-blocks it would require
 * to render the content. If the associated preview property is holds a True
 * value, the resource associated with IMAGE_IF_TRUE is rendered. Otherwise,
 * IMAGE_IF_FALSE is rendered. To display a static image, a null value is passed
 * as the associated preview property.
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
                for (BaseElement element : groupElements) {
                    element.getAssociatedData().put(IMAGE_BOOL, false);
                }

                // turn on the switch for this element
                data.put(IMAGE_BOOL, true);
            } else {
                Boolean currentStatus = property.getValue();
                data.put(IMAGE_BOOL, !currentStatus);
                property.setValue(!currentStatus);
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
        try {
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

            // update the geometry of the inplace editor
            setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                    (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                    blockUnitSize,
                    blockUnitSize);
        } catch (IOException e) {
        }
    }
}