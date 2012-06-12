/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import org.gephi.legend.builders.GroupsItemBuilder;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.NbBundle;
import org.gephi.legend.items.GroupsItem;

/**
 *
 * @author edubecks
 */
//@ServiceProvider(service = Renderer.class, position = 401)
public class GroupsItemRenderer extends LegendItemRenderer{

    @Override
    public void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, int width, int height) {
    }

    @Override
    public void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(GroupsItemRenderer.class, "GroupsItemRenderer.name");
    }

    @Override
    public void preProcess(PreviewModel previewModel) {
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        return item instanceof GroupsItem;
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof GroupsItemBuilder;
    }
    
    
    // DEFAULT VALUES
    Integer defaultNumColumns = 1;
    
    //PROPERTIES
    
}
