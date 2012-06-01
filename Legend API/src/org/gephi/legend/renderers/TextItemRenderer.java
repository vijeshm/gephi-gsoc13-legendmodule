/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

import java.awt.*;
import java.awt.geom.AffineTransform;
import org.gephi.legend.builders.TextItemBuilder;
import org.gephi.legend.items.TextItem;
import org.gephi.legend.properties.TextProperty;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import processing.core.PGraphicsJava2D;

/**
 *
 * @author edubecks
 */

@ServiceProvider(service = Renderer.class, position = 400)
public class TextItemRenderer implements Renderer{

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(TextItemRenderer.class, "TextItemRenderer.name");
    }

    @Override
    public void preProcess(PreviewModel previewModel) {
    }
    
    //VARIABLES
    private Integer width;
    private Integer height;
    private String body;
    
    private Integer bodyFontSize;
    private String bodyFontType;
    private Integer bodyFontStyle;
    

    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {
        
        TextItem textItem = (TextItem) item;
        
        //getting data
        width = textItem.getData(TextItem.WIDTH);
        System.out.println("@Var: width: "+width);
        height = textItem.getData(TextItem.HEIGHT);
        System.out.println("@Var: height: "+height);
        body = textItem.getData(TextItem.BODY);
        System.out.println("@Var: body: "+body);
        
        //getting properties
        bodyFontSize = properties.getIntValue(TextProperty.BODY_FONT_SIZE);
        System.out.println("@Var: bodyFontSize: "+bodyFontSize);
        bodyFontType = properties.getStringValue(TextProperty.BODY_FONT_TYPE);
        System.out.println("@Var: bodyFontType: "+bodyFontType);
        bodyFontStyle = properties.getIntValue(TextProperty.BODY_FONT_STYLE);
        System.out.println("@Var: bodyFontStyle: "+bodyFontStyle);
        Point origin = properties.getValue(TextProperty.ORIGIN);
        System.out.println("@Var: origin: "+origin);
        
        Graphics2D graphics = null;
        if(target instanceof ProcessingTarget){
            graphics = getGraphicsFromProcessingTarget((ProcessingTarget)target);
            
        }
        
        //hack
        origin.x=200;
        origin.y=200;
        AffineTransform originTranslation = new AffineTransform();
        originTranslation.setToTranslation(origin.x, origin.y);
        
        
        renderToGraphics(graphics, originTranslation, width, height);
        
        
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }


    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        return item instanceof TextItem;
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof TextItemBuilder;
    }
    
    
        
    public Graphics2D getGraphicsFromProcessingTarget(ProcessingTarget target){
        return (Graphics2D) ((PGraphicsJava2D) target.getGraphics()).g2;
    }
    
    public void renderToGraphics(Graphics2D graphics, AffineTransform origin, int width, int height){
        
        graphics.setTransform(origin);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        Font font = new Font(bodyFontType, bodyFontStyle, bodyFontSize);
        
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, 100, 100);
        graphics.setColor(Color.RED);
        
        
        graphics.drawString(body, 0, 0);
        System.out.println("@Var: body: "+body);
        
        
        
    }
    
}
