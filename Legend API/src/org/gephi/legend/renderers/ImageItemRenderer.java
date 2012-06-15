/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import org.gephi.legend.api.LegendManager;
import org.gephi.legend.builders.ImageItemBuilder;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.gephi.legend.items.ImageItem;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
//@ServiceProvider(service = Renderer.class, position = 401)
public class ImageItemRenderer extends LegendItemRenderer{

    @Override
    public void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, int width, int height) {
//        graphics2D.setTransform(origin);
        try {
            BufferedImage before = ImageIO.read(new File(imageURL));
            BufferedImage after = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            double scaleHeight =height / ((double) before.getHeight());
            double scaleWidth =width / ((double) before.getWidth());
            origin.scale(scaleWidth, scaleHeight);
            AffineTransformOp scaleOperation = new AffineTransformOp(origin, AffineTransformOp.TYPE_BILINEAR);
            System.out.println("@Var: image: "+before);
            
            graphics2D.drawImage(scaleOperation.filter(before, after), 0, 0,null);
        } catch (Exception e) {
        }
    }

    @Override
    public void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {
        imageURL = item.getData(ImageItem.IMAGE_URL);
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ImageItemRenderer.class, "ImageItemRenderer.name");
    }

    
    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        return item instanceof ImageItem;
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof ImageItemBuilder;
    }
    
    // OWN PROPERTIES
    private String imageURL;

    
}
