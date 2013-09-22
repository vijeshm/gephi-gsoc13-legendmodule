package org.gephi.legend.inplaceelements;

import com.connectina.swing.fontchooser.JFontChooser;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Map;
import javax.swing.JFrame;
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
 * an inplace editor element to modify the font property of a block.
 *
 * Font elements cannot be grouped. The data hashmap is expected to contain two
 * entries: DISPLAY_FONT, DISPLAY_FONT_COLOR. The added element. The added
 * element should declare the number of unit-blocks that it would require. (see
 * inplaceItemRenderer for more details on unit-blocks). The inplace item
 * renderer uses the data hashmap and the number of unit-blocks it would require
 * to render the content.
 *
 * @author mvvijesh
 */
public class ElementFont extends BaseElement {

    public static final String DISPLAY_FONT = "element.font.display";
    public static final String DISPLAY_FONT_COLOR = "element.font.display.color";

    public ElementFont(ELEMENT_TYPE type, int itemIndex, PreviewProperty property, InplaceEditor ipeditor, Row row, Column col, Map<String, Object> data, Boolean isGrouped, Boolean isDefault, Object propertyValue) {
        super(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
    }

    @Override
    public void onSelect() {
        // get the preview properties from the preview model
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        PreviewProperties previewProperties = previewModel.getProperties();

        // show a font chooser dialog. If the user successfully chooses the font, set it as the property
        Font chosenFont = JFontChooser.showDialog(new JFrame("choose a font"), (Font) property.getValue());
        if (chosenFont != null) {
            property.setValue(chosenFont);
            previewProperties.putValue(property.getName(), chosenFont);
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
        Font displayFont = (Font) data.get(DISPLAY_FONT);
        Font font = property.getValue();
        Font scaledFont = displayFont.deriveFont((float) (displayFont.getSize() / target.getScaling())); // get the current scaling factor and scale the font accordingly
        graphics2d.setFont(scaledFont);
        String displayString = font.getFontName() + " " + font.getSize(); // display the font along with the size
        int fontWidth = getFontWidth(graphics2d, displayString);
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
        Font displayFont = (Font) data.get(DISPLAY_FONT);
        displayFont = displayFont.deriveFont((float) (displayFont.getSize() / target.getScaling()));
        Color displayFontColor = (Color) data.get(DISPLAY_FONT_COLOR);

        Font font = property.getValue();
        graphics2d.setFont(displayFont);
        String displayString = font.getFontName() + " " + font.getSize();
        int fontWidth = getFontWidth(graphics2d, displayString);
        int fontHeight = getFontHeight(graphics2d);

        graphics2d.setColor(displayFontColor);
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