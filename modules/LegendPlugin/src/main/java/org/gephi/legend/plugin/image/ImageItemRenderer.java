/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.codec.binary.Base64;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.AbstractLegendItemRenderer;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.api.BlockNode;
import org.gephi.legend.inplaceeditor.Column;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.inplaceeditor.InplaceItemBuilder;
import org.gephi.legend.inplaceeditor.Row;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author mvvijesh, edubecks
 */
@ServiceProvider(service = Renderer.class, position = 504)
public class ImageItemRenderer extends AbstractLegendItemRenderer {

    public static final String IMAGENODE = "image node";
    private int NO_IMAGE_FONT_INITIAL_SIZE = 2;
    private int NO_IMAGE_FONT_STEP_SIZE = 2;
    private Font NO_IMAGE_FONT = new Font("Arial", Font.PLAIN, NO_IMAGE_FONT_INITIAL_SIZE);
    private Color NO_IMAGE_FONT_COLOR = Color.BLACK;
    // OWN PROPERTIES
    private File imageFile;
    private Boolean useImageAspectRatio;
    private int imageMargin;
    // ENCODING
    public static final String DATA_PROTOCOL_PNG_PREFIX = "data:image/png;base64,";

    @Override
    public boolean isAnAvailableRenderer(Item item) {
        return item instanceof ImageItem;
    }

    @Override
    protected void renderToGraphics(Graphics2D graphics2D, BlockNode legendNode) {
        try {
            // get the legendNode Geometry
            int blockOriginX = (int) (legendNode.getOriginX());
            int blockOriginY = (int) (legendNode.getOriginY());
            int blockWidth = (int) legendNode.getBlockWidth();
            int blockHeight = (int) legendNode.getBlockHeight();
            Item item = legendNode.getItem();

            BlockNode imageNode = legendNode.getChild(IMAGENODE);
            if (imageNode == null) {
                imageNode = legendNode.addChild(blockOriginX + imageMargin, blockOriginY + imageMargin, blockWidth - 2 * imageMargin, blockHeight - 2 * imageMargin, IMAGENODE);
                buildInplaceImage(imageNode, item);
            }

            imageNode.updateGeometry(blockOriginX + imageMargin, blockOriginY + imageMargin, blockWidth - 2 * imageMargin, blockHeight - 2 * imageMargin);
            drawBlockBoundary(graphics2D, imageNode);

            int canvasOriginX = (int) imageNode.getOriginX();
            int canvasOriginY = (int) imageNode.getOriginY();
            int canvasWidth = (int) imageNode.getBlockWidth();
            int canvasHeight = (int) imageNode.getBlockHeight();

            if (imageFile.exists() && imageFile.isFile()) {
                BufferedImage image = ImageIO.read(imageFile);
                int scaledOriginX = canvasOriginX;
                int scaledOriginY = canvasOriginY;
                int scaledWidth = canvasWidth;
                int scaledHeight = canvasHeight;

                int imageWidth = image.getWidth();
                int imageHeight = image.getHeight();

                if (useImageAspectRatio) {
                    // if aspect ratio needs to be maintained, originX, originY, width and height must be set appropriately
                    scaledHeight = canvasWidth * imageHeight / imageWidth;
                    if (scaledHeight <= canvasHeight) {
                        // width fits completely. height is either perfect or shorter.
                        scaledWidth = canvasWidth;
                        scaledHeight = canvasWidth * imageHeight / imageWidth; //not necessary, since the calue isnt changed. added for readability and consistency.
                    } else {
                        // height fits completely. width is short perfect or shorter.
                        scaledWidth = canvasHeight * imageWidth / imageHeight;
                        scaledHeight = canvasHeight;
                    }

                    scaledOriginX = canvasOriginX + canvasWidth / 2 - scaledWidth / 2;
                    scaledOriginY = canvasOriginY + canvasHeight / 2 - scaledHeight / 2;
                }

                graphics2D.drawImage(image,
                        (int) (scaledOriginX - currentRealOriginX),
                        (int) (scaledOriginY - currentRealOriginY),
                        (int) (scaledOriginX - currentRealOriginX) + scaledWidth,
                        (int) (scaledOriginY - currentRealOriginY) + scaledHeight,
                        0,
                        0,
                        imageWidth,
                        imageHeight, null);
            } else {
                String noImageMessage = NbBundle.getMessage(ImageItemRenderer.class, "ImageItemRenderer.no.image.message");

                int fontSize = NO_IMAGE_FONT_INITIAL_SIZE;
                int stepSize = NO_IMAGE_FONT_STEP_SIZE;
                Font font = NO_IMAGE_FONT;

                graphics2D.setFont(font);
                while (graphics2D.getFontMetrics().stringWidth(noImageMessage) < canvasWidth) {
                    fontSize += stepSize;
                    font = font.deriveFont((float) fontSize);
                    graphics2D.setFont(font);
                }

                fontSize -= stepSize;
                font = font.deriveFont((float) fontSize);
                graphics2D.setFont(font);
                graphics2D.setColor(NO_IMAGE_FONT_COLOR);

                float messageHeight = graphics2D.getFontMetrics().getHeight();
                float messageWidth = graphics2D.getFontMetrics().stringWidth(noImageMessage);
                graphics2D.drawString(noImageMessage, canvasOriginX + canvasWidth / 2 - messageWidth / 2 - currentRealOriginX, canvasOriginY + canvasHeight / 2 - messageHeight / 2 - currentRealOriginY);
            }
        } catch (Exception e) {
            Logger.getLogger(ImageItemRenderer.class.getName()).log(Level.WARNING, e.getMessage());
        }
    }

    private void buildInplaceImage(BlockNode imageNode, Item item) {
        Graph graph = null;
        InplaceItemBuilder ipbuilder = Lookup.getDefault().lookup(InplaceItemBuilder.class);
        InplaceEditor ipeditor = ipbuilder.createInplaceEditor(graph, imageNode);

        Row r;
        Column col;
        PreviewProperty[] previewProperties = item.getData(LegendItem.OWN_PROPERTIES);
        PreviewProperty prop;
        int itemIndex = item.getData(LegendItem.ITEM_INDEX);

        r = ipeditor.addRow();
        col = r.addColumn();
        Object[] data = new Object[1];
        data[0] = "Source: ";
        col.addElement(org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.LABEL, itemIndex, null, data);

        col = r.addColumn();
        col.addElement(org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.FILE, itemIndex, previewProperties[ImageProperty.IMAGE_URL], null);

        r = ipeditor.addRow();
        col = r.addColumn();
        data = new Object[1];
        data[0] = "Margin: ";
        col.addElement(org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.LABEL, itemIndex, null, data);

        col = r.addColumn();
        col.addElement(org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.NUMBER, itemIndex, previewProperties[ImageProperty.IMAGE_MARGIN], null);

        r = ipeditor.addRow();
        col = r.addColumn();
        data = new Object[1];
        data[0] = "Scale: ";
        col.addElement(org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.LABEL, itemIndex, null, data);

        col = r.addColumn();
        data = new Object[1];
        data[0] = useImageAspectRatio;
        col.addElement(org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.CHECKBOX, itemIndex, previewProperties[ImageProperty.LOCK_ASPECT_RATIO], data);

        imageNode.setInplaceEditor(ipeditor);
    }

    @Override // - to be deprecated
    protected void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        try {
            graphics2D.setTransform(origin);
            if (imageFile.exists() && imageFile.isFile()) {
                BufferedImage before = ImageIO.read(imageFile);

                if (before.getWidth() == width && before.getHeight() == height) {
                    graphics2D.drawImage(before, 0, 0, null);
                } else {
                    if (useImageAspectRatio) {
                        float aspectRatio = (float) before.getWidth() / (float) before.getHeight();
                        height = (int) (width / aspectRatio);
                    }

                    // SCALING
                    BufferedImage after = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                    double scaleHeight = height / ((double) before.getHeight());
                    double scaleWidth = width / ((double) before.getWidth());

                    AffineTransform scaleTransform = new AffineTransform();
                    scaleTransform.scale(scaleWidth, scaleHeight);
                    AffineTransformOp scaleOperation = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
                    after = scaleOperation.filter(before, after);

//                    // EXPORTING TO SVG
                    if (graphics2D instanceof SVGGraphics2D) {
                        int x = (int) origin.getTranslateX();
                        int y = (int) origin.getTranslateY();

                        renderImageToSVGGraphics(graphics2D, after, x, y);

                    } else {
                        graphics2D.drawImage(after, 0, 0, null);
                    }

                }
            } else {
                String noImageMessage = NbBundle.getMessage(ImageItemRenderer.class, "ImageItemRenderer.no.image.message");

                float messageHeight = getFontHeight(graphics2D, noImageMessage, titleFont, width, height); //temporary solution. gotto rethink this further.
                legendDrawText(graphics2D, noImageMessage, titleFont, Color.BLACK, 0, 0, width, (int) messageHeight, LegendItem.Alignment.CENTER);
            }
        } catch (Exception e) {
            Logger.getLogger(ImageItemRenderer.class.getName()).log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    protected void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        imageFile = properties.getValue(LegendModel.getProperty(ImageProperty.OWN_PROPERTIES, itemIndex, ImageProperty.IMAGE_URL));
        useImageAspectRatio = properties.getValue(LegendModel.getProperty(ImageProperty.OWN_PROPERTIES, itemIndex, ImageProperty.LOCK_ASPECT_RATIO));
        imageMargin = properties.getIntValue(LegendModel.getProperty(ImageProperty.OWN_PROPERTIES, itemIndex, ImageProperty.IMAGE_MARGIN));
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ImageItemRenderer.class, "ImageItemRenderer.name");
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof ImageItemBuilder;
    }

    public static void renderImageToSVGGraphics(Graphics2D graphics2D, BufferedImage image, int x, int y) {
        try {
            SVGGraphics2D svgGraphics2D = (SVGGraphics2D) graphics2D;
            SVGGeneratorContext svgGeneratorContext = svgGraphics2D.getGeneratorContext();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            String encodedImage = Base64.encodeBase64String(baos.toByteArray());
            baos.close(); // should be inside a finally block
            Document svgDocument = svgGeneratorContext.getDOMFactory();

            Element imageBase64 = svgDocument.createElementNS("http://www.w3.org/2000/svg", "image");
            imageBase64.setAttribute("width", "" + image.getWidth());
            imageBase64.setAttribute("height", "" + image.getHeight());
//                        imageBase64.setAttribute("transform", "scale("+scaleWidth+","+scaleHeight+") translate("+origin.getTranslateX()+","+origin.getTranslateY()+")");
            imageBase64.setAttribute("transform", "scale(1,1) translate(" + x + "," + y + ")");
            imageBase64.setAttribute("xlink:href", DATA_PROTOCOL_PNG_PREFIX + encodedImage);

            svgDocument.getLastChild().appendChild(imageBase64);
//            svgDocument.getFirstChild().appendChild(imageBase64);
        } catch (Exception e) {
            System.out.println("@Var: e: " + e);
        }
    }
}
