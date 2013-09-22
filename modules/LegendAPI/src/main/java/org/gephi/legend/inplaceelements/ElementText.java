package org.gephi.legend.inplaceelements;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;
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
 * an inplace editor element that allows a text property to be edited.
 *
 * Text elements cannot be grouped. The data hashmap is expected to contain a
 * single entry: EDIT_IMAGE. The added element should declare the number of
 * unit-blocks that it would require. (see inplaceItemRenderer description about
 * unit-blocks). The inplace item renderer uses the data hashmap and the number
 * of unit-blocks it would require to render the content.
 *
 * @author mvvijesh
 */
public class ElementText extends BaseElement {

    public static final String EDIT_IMAGE = "element.text.edit.image";

    public ElementText(ELEMENT_TYPE type, int itemIndex, PreviewProperty property, InplaceEditor ipeditor, Row row, Column col, Map<String, Object> data, Boolean isGrouped, Boolean isDefault, Object propertyValue) {
        super(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
    }

    @Override
    public void onSelect() {
        // get the preview properties from the preview model
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        PreviewProperties previewProperties = previewModel.getProperties();

        // show a dialog to get the new text input
        String newValue = (String) JOptionPane.showInputDialog(null, "Enter new text:", (String) property.getValue());
        if (newValue != null) {
            // if the user hasnt pressed cancel, set the property
            property.setValue(newValue);
            previewProperties.putValue(property.getName(), newValue);
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
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream((String) data.get(EDIT_IMAGE)));

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
