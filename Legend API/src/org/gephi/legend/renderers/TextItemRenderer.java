/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

import java.awt.*;
import java.awt.geom.AffineTransform;
import org.gephi.legend.builders.TextItemBuilder;
import org.gephi.legend.items.LegendItem;
import org.gephi.legend.items.LegendItem.Alignment;
import org.gephi.legend.items.TextItem;
import org.gephi.legend.manager.LegendManager;
import org.gephi.legend.properties.TextProperty;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = Renderer.class, position = 502)
public class TextItemRenderer extends LegendItemRenderer {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(TextItemRenderer.class, "TextItemRenderer.name");
    }

    @Override
    protected void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        body = properties.getStringValue(LegendManager.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, TextProperty.TEXT_BODY));
        bodyFont = properties.getFontValue(LegendManager.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, TextProperty.TEXT_BODY_FONT));
        bodyFontColor = properties.getColorValue(LegendManager.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, TextProperty.TEXT_BODY_FONT_COLOR));
        bodyAlignment = (Alignment) properties.getValue(LegendManager.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, TextProperty.TEXT_BODY_FONT_ALIGNMENT));
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        return item instanceof TextItem;
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof TextItemBuilder;
    }

    @Override
    protected void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        if (!body.isEmpty()) {
            graphics2D.setTransform(origin);
            boolean computeSpace = true;
            float currentSize = legendDrawText(graphics2D, body, bodyFont, bodyFontColor, 0, 0, width, height, bodyAlignment, computeSpace);
            while (currentSize > height) {
                bodyFont = new Font(bodyFont.getName(), bodyFont.getStyle(), bodyFont.getSize()-1);
                currentSize = legendDrawText(graphics2D, body, bodyFont, bodyFontColor, 0, 0, width, height, bodyAlignment, computeSpace);
            }
            legendDrawText(graphics2D, body, bodyFont, bodyFontColor, 0, 0, width, height, bodyAlignment);
        }
    }

    // OWN PROPERTIES
    private String body;
    private Font bodyFont;
    private Color bodyFontColor;
    private Alignment bodyAlignment;
}
