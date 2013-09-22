package org.gephi.legend.spi;

import java.util.ArrayList;
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
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * a single renderer class that can handle the rendering of all the legend
 * items.
 *
 * The individual renderers need to register themselves in the legend model,
 * rather than exposing them as services and using the lookup API to retrieve
 * them. The current approach is subject to change.
 *
 * @author mvvijesh, edubecks
 */
@ServiceProviders(value = {
    @ServiceProvider(service = Renderer.class, position = 600),
    @ServiceProvider(service = LegendItemRenderer.class)
})
public class LegendItemRenderer implements Renderer, MouseResponsiveRenderer {

    Integer nextRenderIndex = 0; // list index of the next item to be rendered. This is used to achieve layered rendering

    @Override
    public String getDisplayName() {
        return "Legend Item Renderer";
    }

    @Override
    public void preProcess(PreviewModel previewModel) {
    }

    /**
     *
     * @param item - item to be rendered
     * @param target - the rendering target - can be G2D, PDF or SVG
     * @param properties - PreviewProperties of the current PreviewModel
     *
     * This method is supposed to render the parameter 'item'. If done so,
     * layered rendering is impossible to achive.
     *
     * Layered Rendering approach: The order of items to be rendered is present
     * in the legend model's activeItems list. Everytime the PreviewController
     * invokes this function, instead of rendering the passed item, we render an
     * item from the activeItems list. For this purpose, we maintain a member
     * variable nextRenderIndex that holds the list index of the next item to be
     * rendered. Note that this counter should be reset to 0 on every refresh
     * cycle (i.e, when the last element is reached).
     */
    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {

        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        ArrayList<Item> legendItems = legendModel.getActiveItems();
        Item legendItem = legendItems.get(nextRenderIndex);
        Renderer renderer = legendItem.getData(LegendItem.RENDERER);
        renderer.render(legendItem, target, properties);

        nextRenderIndex += 1;
        if (nextRenderIndex == legendItems.size()) {
            nextRenderIndex = 0;
        }
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }

    /**
     *
     * @param item - the legend item under consideration
     * @param properties - PreviewProperties of the current PreviewModel This
     * @return True if the item can be rendered by this renderer. False
     * otherwise.
     *
     * When an item is presented to this renderer and asked if it can render the
     * item, this renderer must check with all the registered renderers. The
     * items also know which renderer can render them. If there is a match
     * between the item's renderer and one of the registered renderers, then a
     * Boolean true must be returned.
     */
    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        Renderer itemRenderer = item.getData(LegendItem.RENDERER);
        if (itemRenderer == null) {
            return false;
        }

        // get the registered renderers
        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        ArrayList<Renderer> renderers = legendModel.getRenderers();

        // if any of the renderers match, return true
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

    /**
     *
     * @param itemBuilder - the custom item builder being checked against
     * @param properties - preview properties of the preview model
     * @return True if the custom item builder can be built with
     * LegendItemBuilder
     */
    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof LegendItemBuilder;
    }
}