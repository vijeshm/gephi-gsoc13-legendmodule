package org.gephi.legend.plugin.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.AbstractLegendItemRenderer;
import org.gephi.legend.api.BlockNode;
import org.gephi.legend.inplaceeditor.Column;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.inplaceeditor.InplaceItemBuilder;
import org.gephi.legend.inplaceeditor.InplaceItemRenderer;
import org.gephi.legend.inplaceeditor.Row;
import org.gephi.legend.inplaceelements.BaseElement;
import org.gephi.legend.inplaceelements.ElementColor;
import org.gephi.legend.inplaceelements.ElementFont;
import org.gephi.legend.inplaceelements.ElementImage;
import org.gephi.legend.inplaceelements.ElementText;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 * this is the renderer for text item.
 *
 * This renderer does not provide a service. Hence, it should be registered with
 * the legend model when the corresponding items are built. Since, it doesnt
 * provide a service, this class should be made singleton. Note that this
 * approach is subjected to change.
 *
 * @author mvvijesh, edubecks
 */
public class TextItemRenderer extends AbstractLegendItemRenderer {

    public static String TEXTNODE = "text node";
    // own properties
    private String body;
    private Font bodyFont;
    private Color bodyFontColor;
    private LegendItem.Alignment bodyAlignment;
    // instance
    private static TextItemRenderer instance = null;

    private TextItemRenderer() {
        // private constructor is required to ensure singleton class
    }

    public static TextItemRenderer getInstance() {
        if (instance == null) {
            instance = new TextItemRenderer();
        }

        return instance;
    }

    @Override
    public boolean isAnAvailableRenderer(Item item) {
        return item instanceof TextItem;
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(TextItemRenderer.class, "TextItemRenderer.name");
    }

    /**
     *
     * @param item - table item to be rendered
     * @param properties - preview properties for the preview controller
     */
    @Override
    protected void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {
        PreviewProperty[] textItemPreviewProperties = item.getData(LegendItem.OWN_PROPERTIES);

        body = textItemPreviewProperties[TextProperty.TEXT_BODY].getValue();
        bodyFont = textItemPreviewProperties[TextProperty.TEXT_BODY_FONT].getValue();
        bodyFontColor = textItemPreviewProperties[TextProperty.TEXT_BODY_FONT_COLOR].getValue();
        bodyAlignment = textItemPreviewProperties[TextProperty.TEXT_BODY_FONT_ALIGNMENT].getValue();
    }

    /**
     *
     * @param itemBuilder - custom item builder to be tested against
     * @param properties - preview properties of the preview model
     * @return True if the custom item builder can has built the text items.
     * False, otherwise.
     */
    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof TextItemBuilder;
    }

    /**
     *
     * @param graphics2d - graphics object of the target
     * @param target - the target onto which the item should be rendered - SVG,
     * PDF or G2D
     * @param legendNode - the BlockNode which houses the text node
     */
    @Override
    protected void renderToGraphics(Graphics2D graphics2d, RenderTarget target, BlockNode legendNode) {
        // draw the text
        int blockOriginX = (int) (legendNode.getOriginX());
        int blockOriginY = (int) (legendNode.getOriginY());
        int blockWidth = (int) legendNode.getBlockWidth();
        int blockHeight = (int) legendNode.getBlockHeight();
        legendDrawText(graphics2d, body, bodyFont, bodyFontColor, blockOriginX - currentRealOriginX, blockOriginY - currentRealOriginY, blockWidth, blockHeight, bodyAlignment);

        // create a text node, create a corresponding inplace editor and attach it as the legendNode's child
        int textWidth = blockWidth;
        int textHeight = (int) legendDrawText(graphics2d, body, bodyFont, bodyFontColor, blockOriginX, blockOriginY, blockWidth, blockHeight, bodyAlignment, true);
        int textOriginX = blockOriginX;
        int textOriginY = blockOriginY + blockHeight / 2 - textHeight / 2;
        Item item = legendNode.getItem();
        BlockNode textNode = legendNode.getChild(TEXTNODE);
        if (textNode == null) {
            textNode = legendNode.addChild(textOriginX, textOriginY, textWidth, textHeight, TEXTNODE);
            buildInplaceText(textNode, item, graphics2d, target);
        }

        // update the geometry and draw the geometric dimensions
        textNode.updateGeometry(textOriginX, textOriginY, textWidth, textHeight);
    }

    /**
     *
     * @param textNode - the BlockNode that content of the text item
     * @param item - the text item to be rendered
     * @param graphics2d - graphics object of the target
     * @param target - the target onto which the item should be rendered - SVG,
     * PDF or G2D
     */
    private void buildInplaceText(BlockNode textNode, Item item, Graphics2D graphics2d, RenderTarget target) {
        // associate an inplace renderer with the textNode
        Graph graph = null;
        InplaceItemBuilder ipbuilder = Lookup.getDefault().lookup(InplaceItemBuilder.class);
        InplaceEditor ipeditor = ipbuilder.createInplaceEditor(graph, textNode);

        Row r;
        Column col;
        PreviewProperty[] previewProperties = item.getData(LegendItem.OWN_PROPERTIES);
        int itemIndex = item.getData(LegendItem.ITEM_INDEX);
        Map<String, Object> data;
        BaseElement addedElement;

        r = ipeditor.addRow();
        // see ElementLabel.java to understand how this element is being structured within the inplace editor
        col = r.addColumn(false);
        data = new HashMap<String, Object>();
        data.put(ElementText.EDIT_IMAGE, "/org/gephi/legend/graphics/edit.png");
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.TEXT, itemIndex, previewProperties[TextProperty.TEXT_BODY], data, false, null);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

        col = r.addColumn(false);
        // see ElementColor.java to understand how this element is being structured within the inplace editor
        data = new HashMap<String, Object>();
        data.put(ElementColor.COLOR_MARGIN, InplaceItemRenderer.COLOR_MARGIN);
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.COLOR, itemIndex, previewProperties[TextProperty.TEXT_BODY_FONT_COLOR], data, false, null);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

        // see ElementFont.java to understand how this element is being structured within the inplace editor
        col = r.addColumn(false);
        data = new HashMap<String, Object>();
        data.put(ElementFont.DISPLAY_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
        data.put(ElementFont.DISPLAY_FONT_COLOR, InplaceItemRenderer.FONT_DISPLAY_COLOR);
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.FONT, itemIndex, previewProperties[TextProperty.TEXT_BODY_FONT], data, false, null);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

        r = ipeditor.addRow();
        // see ElementImage.java to understand how this element is being structured within the inplace editor
        col = r.addColumn(true);
        // left-alignment
        data = new HashMap<String, Object>();
        data.put(ElementImage.IMAGE_BOOL, bodyAlignment == Alignment.LEFT);
        data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/left_selected.png");
        data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/left_unselected.png");
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[TextProperty.TEXT_BODY_FONT_ALIGNMENT], data, bodyAlignment == Alignment.LEFT, Alignment.LEFT);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

        // center-alignment
        data = new HashMap<String, Object>();
        data.put(ElementImage.IMAGE_BOOL, bodyAlignment == Alignment.CENTER);
        data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/center_selected.png");
        data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/center_unselected.png");
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[TextProperty.TEXT_BODY_FONT_ALIGNMENT], data, bodyAlignment == Alignment.CENTER, Alignment.CENTER);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

        // right alignment
        data = new HashMap<String, Object>();
        data.put(ElementImage.IMAGE_BOOL, bodyAlignment == Alignment.RIGHT);
        data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/right_selected.png");
        data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/right_unselected.png");
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[TextProperty.TEXT_BODY_FONT_ALIGNMENT], data, bodyAlignment == Alignment.RIGHT, Alignment.RIGHT);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

        // justified
        data = new HashMap<String, Object>();
        data.put(ElementImage.IMAGE_BOOL, bodyAlignment == Alignment.JUSTIFIED);
        data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/justified_selected.png");
        data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/justified_unselected.png");
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[TextProperty.TEXT_BODY_FONT_ALIGNMENT], data, bodyAlignment == Alignment.JUSTIFIED, Alignment.JUSTIFIED);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

        // the node doesnt know which inplace editor is associated with it, hence register it
        textNode.setInplaceEditor(ipeditor);
    }

    /**
     * draws the given text onto the canvas using the graphics2D object
     *
     * @param graphics2D - graphics object of the target
     * @param text - the text to be drawn
     * @param font - the font to be used to draw
     * @param color - the color of the text
     * @param x - x-coordinate of the bounding box
     * @param y - y-coordinate of the bounding box
     * @param width - width of the bounding box
     * @param height - height of the bounding box
     * @param alignment - alignment of the text to be rendered
     */
    protected void drawText(Graphics2D graphics2D, String text, Font font, Color color, double x, double y, Integer width, Integer height, LegendItem.Alignment alignment) {
        legendDrawText(graphics2D, text, font, color, x, y, width, height, alignment, false);
    }
}