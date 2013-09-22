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
 * an inplace editor element that invokes a registered method when clicked.
 *
 * Function elements cannot be grouped. The data hashmap is expected to contain
 * two entries: FUNCTION_IMAGE, FUNCTION_CLICK_RESPONDER. The former is the
 * image to be displayed on the inplace editor. The latter is an anonymous
 * object that implements InplaceClickesponder. This anonymous object is created
 * when the FUNCTION_CLICK_RESPONDER object is being set. The added element
 * should declare the number of unit-blocks that it would require. (see
 * inplaceItemRenderer for more details on unit-blocks). The inplace item
 * renderer uses the data hashmap and the number of unit-blocks it would require
 * to render the content.
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
        // invoke the registered function
        InplaceClickResponse responder = (InplaceClickResponse) data.get(FUNCTION_CLICK_RESPONDER);
        responder.performAction(ipeditor);
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

            // update the geometry of the inplace editor
            setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                    (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                    blockUnitSize,
                    blockUnitSize);
        } catch (IOException e) {
        }
    }
}