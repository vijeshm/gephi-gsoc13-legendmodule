/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import org.gephi.legend.api.AbstractLegendItemRenderer;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author root
 */
@ServiceProvider(service = Renderer.class, position = 506)
public class TextItemRenderer extends AbstractLegendItemRenderer{
    
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