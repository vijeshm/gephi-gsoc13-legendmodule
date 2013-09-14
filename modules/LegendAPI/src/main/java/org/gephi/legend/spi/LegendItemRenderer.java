/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.LookAndFeel;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.mouse.LegendMouseListener;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.MouseResponsiveRenderer;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.preview.spi.Renderer;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author mvvijesh, edubecks
 */
@ServiceProvider(service = Renderer.class, position = 600)
public class LegendItemRenderer implements Renderer, MouseResponsiveRenderer {

    // Boolean allItemsRendered = false;
    @Override
    public String getDisplayName() {
        return "Legend Item Renderer";
    }

    @Override
    public void preProcess(PreviewModel previewModel) {
    }

    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {

        Renderer renderer = item.getData(LegendItem.RENDERER);
        renderer.render(item, target, properties);

        /*
         // instead of rendering only this item, this method will render all the legend items currently active
         // Reason:  The rendering technique that takes place in the preview controller gives priority to renderer, rather than items.
         //          i.e, if nodeRenderer is activated first, all the nodes will be rendered, followed by other preview items.
         //          This method cannot be applied if legend item layering is to be enabled. Item-order must be given higher priority for layering legends.
         // Hence, all the legend items (in the order seen at the layers panel) are rendered and a flag is set.

         if (!allItemsRendered) {
         LegendController legendController = LegendController.getInstance();
         LegendModel legendModel = legendController.getLegendModel();
         ArrayList<Item> legendItems = legendModel.getActiveItems();

         Renderer renderer;
         for (Item legendItem : legendItems) {
         renderer = legendItem.getData(LegendItem.RENDERER);
         renderer.render(item, target, properties);
         }
         allItemsRendered = true;
         }
         */
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        Renderer itemRenderer = item.getData(LegendItem.RENDERER);
        if (itemRenderer == null) {
            return false;
        }

        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        ArrayList<Renderer> renderers = legendModel.getRenderers();
        for (Renderer r : renderers) {
            if (itemRenderer.equals(r)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean needsPreviewMouseListener(PreviewMouseListener previewMouseListener) {
        return previewMouseListener instanceof LegendMouseListener;
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof LegendItemBuilder;
    }
}
