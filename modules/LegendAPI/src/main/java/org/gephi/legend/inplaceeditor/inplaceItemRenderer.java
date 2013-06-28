/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceeditor;

import java.awt.Color;
import java.awt.Graphics2D;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.blockNode;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItemBuilder;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author mvvijesh
 */
@ServiceProviders(value = {
    @ServiceProvider(service = Renderer.class, position = 505),
    @ServiceProvider(service = inplaceItemRenderer.class, position = 505)
})
public class inplaceItemRenderer implements Renderer{
    
    @Override
    public String getDisplayName(){
        return "inplace renderer";
    }
    
    @Override
    public void preProcess(PreviewModel previewModel){
    }
    
    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties){
        G2DTarget g2dtarget = (G2DTarget) target;
        Graphics2D graphics2d = g2dtarget.getGraphics();
        
        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        inplaceEditor ipeditor = legendModel.getInplaceEditor();
        blockNode node = ipeditor.getData(inplaceEditor.BLOCKNODE);
        float blockOriginX = node.getOriginX();
        float blockOriginY = node.getOriginY();
        float blockWidth = node.getBlockWidth();
        float blockHeight = node.getBlockHeight();
        float gap = ipeditor.getData(inplaceEditor.BLOCK_INPLACEEDITOR_GAP);
        
        int editorOriginX = (int)(blockOriginX + blockWidth + gap);
        int editorOriginY = (int)(blockOriginY);
        int editorWidth = 300; // must be computed based on the number of rows and columns
        int editorHeight = 300; // must be computed based on the number of rows and columns
        
        ipeditor.setData(inplaceEditor.ORIGIN_X, editorOriginX);
        ipeditor.setData(inplaceEditor.ORIGIN_Y, editorOriginY);
        ipeditor.setData(inplaceEditor.WIDTH, editorWidth);
        ipeditor.setData(inplaceEditor.HEIGHT, editorHeight);
        
        Color saveState = graphics2d.getColor();
        graphics2d.setColor(new Color(0.5f, 0.5f, 0.5f, 0.8f));
        graphics2d.fillRect(editorOriginX, editorOriginY, editorWidth, editorHeight);
        graphics2d.setColor(saveState);
    }
    
    @Override
    public PreviewProperty[] getProperties(){
        return new PreviewProperty[0];
    }
    
    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties){
        Class renderer = item.getData(inplaceEditor.RENDERER);
        return renderer != null && renderer.equals(getClass());
    }
    
    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties){
        return itemBuilder instanceof inplaceItemBuilder;
    }
}
