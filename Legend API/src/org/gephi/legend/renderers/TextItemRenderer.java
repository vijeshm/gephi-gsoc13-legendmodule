/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendItem.Alignment;
import org.gephi.legend.api.LegendManager;
import org.gephi.legend.properties.TextProperty;
import org.gephi.legend.builders.TextItemBuilder;
import org.gephi.legend.items.TextItem;
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
    public void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {
        
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        
        body = properties.getStringValue(LegendManager.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, TextProperty.TEXT_BODY));
        bodyFont = properties.getFontValue(LegendManager.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, TextProperty.TEXT_BODY_FONT));
        bodyFontColor = properties.getColorValue(LegendManager.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, TextProperty.TEXT_BODY_FONT_COLOR));
        bodyAlignment = (Alignment)properties.getValue(LegendManager.getProperty(TextProperty.OWN_PROPERTIES, itemIndex, TextProperty.TEXT_BODY_FONT_ALIGNMENT));
        System.out.println("@Var: bodyFontAlignment: "+bodyAlignment);
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
    public void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        if(!body.isEmpty()){
            legendDrawText(graphics2D, body, bodyFont, bodyFontColor, origin.getTranslateX(),origin.getTranslateY(),  width, height, bodyAlignment);
        }
    }
    

    
    // OWN PROPERTIES
    private String body;
    private Font bodyFont;
    private Color bodyFontColor;
    private Alignment bodyAlignment;

}
