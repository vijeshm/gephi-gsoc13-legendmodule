/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import org.apache.batik.svggen.ImageHandler;
import org.apache.batik.svggen.ImageHandlerBase64Encoder;
import org.apache.batik.svggen.SVGGraphics2D;
import org.gephi.legend.api.LegendItem;
import org.gephi.legend.api.LegendManager;
import org.gephi.legend.builders.ImageItemBuilder;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.gephi.legend.items.ImageItem;
import org.gephi.legend.properties.ImageProperty;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = Renderer.class, position = 503)
public class ImageItemRenderer extends LegendItemRenderer {

    @Override
    public void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
//        graphics2D.setTransform(origin);
        try {
            if (imageFile.exists()) {
                BufferedImage before = ImageIO.read(imageFile);





                graphics2D.setTransform(origin);
                if (before.getWidth() == width && before.getHeight() == height) {
                    graphics2D.drawImage(before, 0, 0, null);
                }
                else {
                    // SCALING
                    BufferedImage after = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                    double scaleHeight = height / ((double) before.getHeight());
                    double scaleWidth = width / ((double) before.getWidth());
                    AffineTransform scaleTransform = new AffineTransform();
                    scaleTransform.scale(scaleWidth, scaleHeight);
                    AffineTransformOp scaleOperation = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
                    after = scaleOperation.filter(before, after);

                    SVGGraphics2D svgGraphics2D = (SVGGraphics2D) graphics2D;
                    ImageHandler imageHandler = svgGraphics2D.getImageHandler();
                    imageHandler.handleImage((Image) after, svgGraphics2D.getRoot(), svgGraphics2D.getGeneratorContext());
                    graphics2D.drawImage(after, 0, 0, null);
                    
                    
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {

        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        imageFile = properties.getValue(LegendManager.getProperty(ImageProperty.OWN_PROPERTIES, itemIndex, ImageProperty.IMAGE_URL));
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
    private File imageFile;
}
