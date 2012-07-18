/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.mouse.example;

import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.MouseResponsiveRenderer;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.preview.spi.Renderer;
import org.gephi.project.api.Workspace;
import org.openide.util.lookup.ServiceProvider;
import processing.core.PGraphics;

/**
 *
 * @author Eduardo Ramos<eduramiba@gmail.com>
 */
//@ServiceProvider(service=Renderer.class)
public class RendererTest implements Renderer, MouseResponsiveRenderer {

    public String getDisplayName() {
        return "Test mouse";
    }

    public void preProcess(PreviewModel previewModel) {
    }

    public void render(Item item, RenderTarget target, PreviewProperties properties) {
        if (target instanceof ProcessingTarget) {
            PGraphics graphics = ((ProcessingTarget)target).getGraphics();
            graphics.fill(0);
            graphics.stroke(0);
            graphics.rect(properties.getFloatValue("mouse.test.x"), properties.getFloatValue("mouse.test.y"), 25, 25);
        }
    }

    public PreviewProperty[] getProperties() {
        return new PreviewProperty[]{
                    PreviewProperty.createProperty(this, "mouse.test.x", Float.class, "X", null, "mouse.test").setValue(0),
                    PreviewProperty.createProperty(this, "mouse.test.y", Float.class, "Y", null, "mouse.test").setValue(0)
                };
    }

    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        return item instanceof ItemTest;
    }

    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof ItemBuilderTest;
    }

    public PreviewMouseListener[] getListeners() {
        return new PreviewMouseListener[]{
                    new PreviewMouseListener() {

                        public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
                            properties.putValue("mouse.test.x", event.x);
                            properties.putValue("mouse.test.y", event.y);
                            event.setConsumed(true);
                        }

                        public void mousePressed(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
                            event.setConsumed(true);
                        }

                        public void mouseDragged(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
                            properties.putValue("mouse.test.x", event.x);
                            properties.putValue("mouse.test.y", event.y);
                            event.setConsumed(true);
                        }

                        public void mouseReleased(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
                            event.setConsumed(true);
                        }
                    }
                };
    }
}
