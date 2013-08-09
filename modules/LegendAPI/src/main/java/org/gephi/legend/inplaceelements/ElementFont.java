/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        PreviewProperties previewProperties = previewModel.getProperties();

        Font chosenFont = JFontChooser.showDialog(new JFrame("choose a font"), (Font) property.getValue());
        if (chosenFont != null) {
            property.setValue(chosenFont);
            previewProperties.putValue(property.getName(), chosenFont);
        }
    }

    @Override
    public int setNumberOfBlocks(Graphics2D graphics2d, G2DTarget target, int blockUnitSize) {
        Font displayFont = (Font) data.get(DISPLAY_FONT);
        Font font = property.getValue();
        Font scaledFont = displayFont.deriveFont((float) (displayFont.getSize() / target.getScaling()));
        graphics2d.setFont(scaledFont);
        String displayString = font.getFontName() + " " + font.getSize();
        int fontWidth = getFontWidth(graphics2d, displayString);
        numberOfBlocks = fontWidth / blockUnitSize + 1;

        return numberOfBlocks;
    }

    @Override
    public void renderElement(Graphics2D graphics2d, G2DTarget target, int blockUnitSize, int editorOriginX, int editorOriginY, int borderSize, int rowBlock, int currentElementsCount) {
        setNumberOfBlocks(graphics2d, target, blockUnitSize);

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

        setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                blockUnitSize * numberOfBlocks,
                blockUnitSize);
    }
}