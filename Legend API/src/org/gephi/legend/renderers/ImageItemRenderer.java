/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.renderers;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import org.apache.batik.svggen.*;
import org.apache.commons.codec.binary.*;
import org.gephi.legend.builders.ImageItemBuilder;
import org.gephi.legend.items.ImageItem;
import org.gephi.legend.items.LegendItem;
import org.gephi.legend.manager.LegendManager;
import org.gephi.legend.properties.ImageProperty;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = Renderer.class, position = 503)
public class ImageItemRenderer extends LegendItemRenderer {

    @Override
    public void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        System.out.println("@Var: origin: " + origin);
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


                    // EXPORTING TO SVG
                    if (graphics2D instanceof SVGGraphics2D) {
                        SVGGraphics2D svgGraphics2D = (SVGGraphics2D) graphics2D;
                        SVGGeneratorContext svgGeneratorContext = svgGraphics2D.getGeneratorContext();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(after, "png", baos);
                        baos.flush();
                        String encodedImage = Base64.encodeBase64String(baos.toByteArray());
                        System.out.println("@Var: encodedImage: " + encodedImage);
                        baos.close(); // should be inside a finally block
                        Document svgDocument = svgGeneratorContext.getDOMFactory();
                        System.out.println("@Var: domFactory: " + svgDocument);

                        Element imageBase64 = svgDocument.createElementNS("http://www.w3.org/2000/svg", "image");
                        imageBase64.setAttribute("width", "" + after.getWidth());
                        imageBase64.setAttribute("height", "" + after.getHeight());
//                        imageBase64.setAttribute("transform", "scale("+scaleWidth+","+scaleHeight+") translate("+origin.getTranslateX()+","+origin.getTranslateY()+")");
                        imageBase64.setAttribute("transform", "scale(1,1) translate(" + origin.getTranslateX() + "," + origin.getTranslateY() + ")");
                        imageBase64.setAttribute("xlink:href", DATA_PROTOCOL_PNG_PREFIX + encodedImage);

                        svgDocument.getLastChild().appendChild(imageBase64);





                    }
                    else {
                        graphics2D.drawImage(after, 0, 0, null);
                    }




                }
            }
        } catch (Exception e) {
            System.out.println("@Var: e: " + e);


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
    // encoding
    public static final String DATA_PROTOCOL_PNG_PREFIX = "data:image/png;base64,";
}
