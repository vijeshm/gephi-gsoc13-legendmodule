/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.AbstractLegendItemRenderer;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.api.blockNode;
import org.gephi.legend.inplaceeditor.column;
import org.gephi.legend.inplaceeditor.element;
import org.gephi.legend.inplaceeditor.inplaceEditor;
import org.gephi.legend.inplaceeditor.inplaceItemBuilder;
import org.gephi.legend.inplaceeditor.row;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author mvvijesh
 */
@ServiceProvider(service = Renderer.class, position = 505)
public class TextItemRenderer extends AbstractLegendItemRenderer {
    // unique identifiers for some of the blocks (nodes). A similar definition is given in blockNode.java for root, title, description and legend.

    public static String TEXTNODE = "text node";
    // OWN PROPERTIES
    private String body;
    private Font bodyFont;
    private Color bodyFontColor;
    private LegendItem.Alignment bodyAlignment;

    @Override
    public boolean isAnAvailableRenderer(Item item) {
        return item instanceof TextItem;
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(TextItemRenderer.class, "TextItemRenderer.name");
    }

    @Override
    protected void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        
        body = properties.getStringValue(LegendModel.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, TextProperty.TEXT_BODY));
        bodyFont = properties.getFontValue(LegendModel.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, TextProperty.TEXT_BODY_FONT));
        bodyFontColor = properties.getColorValue(LegendModel.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, TextProperty.TEXT_BODY_FONT_COLOR));
        bodyAlignment = (Alignment) properties.getValue(LegendModel.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, TextProperty.TEXT_BODY_FONT_ALIGNMENT));
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof TextItemBuilder;
    }

    @Override
    protected void renderToGraphics(Graphics2D graphics2D, blockNode legendNode) {
        // draw the text
        int blockOriginX = (int) (legendNode.getOriginX());
        int blockOriginY = (int) (legendNode.getOriginY());
        int blockWidth = (int) legendNode.getBlockWidth();
        int blockHeight = (int) legendNode.getBlockHeight();
        legendDrawText(graphics2D, body, bodyFont, bodyFontColor, blockOriginX - currentRealOriginX, blockOriginY - currentRealOriginY, blockWidth, blockHeight, bodyAlignment);

        // create a text node, create a corresponding inplace editor and attach it as the legendNode's child
        int textWidth = blockWidth;
        int textHeight = (int) legendDrawText(graphics2D, body, bodyFont, bodyFontColor, blockOriginX, blockOriginY, blockWidth, blockHeight, bodyAlignment, true);
        int textOriginX = blockOriginX;
        int textOriginY = blockOriginY + blockHeight / 2 - textHeight / 2;
        Item item = legendNode.getItem();
        blockNode textNode = legendNode.getChild(TEXTNODE);
        if (textNode == null) {
            textNode = legendNode.addChild(textOriginX, textOriginY, textWidth, textHeight, TEXTNODE);
            buildInplaceText(textNode, item);
        }

        // update the geometry and draw the geometric dimensions
        textNode.updateGeometry(textOriginX, textOriginY, textWidth, textHeight);
        drawBlockBoundary(graphics2D, textNode);
    }

    private void buildInplaceText(blockNode textNode, Item item) {
        // associate an inplace renderer with the textNode
        Graph graph = null;
        inplaceItemBuilder ipbuilder = Lookup.getDefault().lookup(inplaceItemBuilder.class);
        inplaceEditor ipeditor = ipbuilder.createInplaceEditor(graph, textNode);
        ipeditor.setData(inplaceEditor.BLOCK_INPLACEEDITOR_GAP, (float) (TRANSFORMATION_ANCHOR_SIZE * 3.0 / 4.0));

        row r;
        column col;
        PreviewProperty[] previewProperties = item.getData(LegendItem.OWN_PROPERTIES);
        PreviewProperty prop;
        int itemIndex = item.getData(LegendItem.ITEM_INDEX);

        r = ipeditor.addRow();
        col = r.addColumn();
        Object[] data = new Object[0];
        col.addElement(element.ELEMENT_TYPE.TEXT, itemIndex, previewProperties[TextProperty.TEXT_BODY], data);
        
        col = r.addColumn();
        data = new Object[0];
        col.addElement(element.ELEMENT_TYPE.COLOR, itemIndex, previewProperties[TextProperty.TEXT_BODY_FONT_COLOR], data);

        col = r.addColumn();
        data = new Object[0];
        col.addElement(element.ELEMENT_TYPE.FONT, itemIndex, previewProperties[TextProperty.TEXT_BODY_FONT], data);

        r = ipeditor.addRow();
        col = r.addColumn();
        // left-alignment
        data = new Object[4];
        data[0] = previewProperties[TextProperty.TEXT_BODY_FONT_ALIGNMENT].getValue() == Alignment.LEFT;
        data[1] = "/org/gephi/legend/graphics/left_unselected.png";
        data[2] = "/org/gephi/legend/graphics/left_selected.png";
        data[3] = Alignment.LEFT;
        col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[TextProperty.TEXT_BODY_FONT_ALIGNMENT], data);

        // center alignment
        data = new Object[4];
        data[0] = previewProperties[TextProperty.TEXT_BODY_FONT_ALIGNMENT].getValue() == Alignment.CENTER;
        data[1] = "/org/gephi/legend/graphics/center_unselected.png";
        data[2] = "/org/gephi/legend/graphics/center_selected.png";
        data[3] = Alignment.CENTER;
        col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[TextProperty.TEXT_BODY_FONT_ALIGNMENT], data);

        // right alignment
        data = new Object[4];
        data[0] = previewProperties[TextProperty.TEXT_BODY_FONT_ALIGNMENT].getValue() == Alignment.RIGHT;
        data[1] = "/org/gephi/legend/graphics/right_unselected.png";
        data[2] = "/org/gephi/legend/graphics/right_selected.png";
        data[3] = Alignment.RIGHT;
        col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[TextProperty.TEXT_BODY_FONT_ALIGNMENT], data);

        // justified
        data = new Object[4];
        data[0] = previewProperties[TextProperty.TEXT_BODY_FONT_ALIGNMENT].getValue() == Alignment.JUSTIFIED;
        data[1] = "/org/gephi/legend/graphics/justified_unselected.png";
        data[2] = "/org/gephi/legend/graphics/justified_selected.png";
        data[3] = Alignment.JUSTIFIED;
        col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[TextProperty.TEXT_BODY_FONT_ALIGNMENT], data);

        textNode.setInplaceEditor(ipeditor);
    }

    @Override // to be deprecated
    protected void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        if (!body.isEmpty()) {
            graphics2D.setTransform(origin);

            // resizing text

//            float currentSize = computeVerticalTextSpaceUsed(graphics2D, body, bodyFont, width);
////            float currentSize = legendDrawText(graphics2D, body, bodyFont, bodyFontColor, 0, 0, width, height, bodyAlignment, computeSpace);
//            while (currentSize > height) {
//                bodyFont = new Font(bodyFont.getName(), bodyFont.getStyle(), bodyFont.getSize() - 1);
//                currentSize = computeVerticalTextSpaceUsed(graphics2D, body, bodyFont, width);
////                currentSize = legendDrawText(graphics2D, body, bodyFont, bodyFontColor, 0, 0, width, height, bodyAlignment, computeSpace);
//            }


            drawText(graphics2D, body, bodyFont, bodyFontColor, 0, 0, width, height, bodyAlignment);
        }
    }

    protected void drawText(Graphics2D graphics2D, String text, Font font, Color color, double x, double y, Integer width, Integer height, LegendItem.Alignment alignment) {
        legendDrawText(graphics2D, text, font, color, x, y, width, height, alignment, false);
    }
}