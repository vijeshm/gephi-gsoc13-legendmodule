package org.gephi.legend.plugin.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.codec.binary.Base64;
import org.gephi.graph.api.Graph;
import org.gephi.legend.api.AbstractLegendItemRenderer;
import org.gephi.legend.api.BlockNode;
import org.gephi.legend.inplaceeditor.Column;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.inplaceeditor.InplaceItemBuilder;
import org.gephi.legend.inplaceeditor.InplaceItemRenderer;
import org.gephi.legend.inplaceeditor.Row;
import org.gephi.legend.inplaceelements.BaseElement;
import org.gephi.legend.inplaceelements.ElementCheckbox;
import org.gephi.legend.inplaceelements.ElementFile;
import org.gephi.legend.inplaceelements.ElementLabel;
import org.gephi.legend.inplaceelements.ElementNumber;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * this is the renderer for image item.
 *
 * This renderer does not provide a service. Hence, it should be registered with
 * the legend model when the corresponding items are built. Since, it doesnt
 * provide a service, this class should be made singleton. Note that this
 * approach is subjected to change.
 *
 * @author mvvijesh, edubecks
 */
public class ImageItemRenderer extends AbstractLegendItemRenderer {

    public static final String IMAGENODE = "image node";
    private int NO_IMAGE_FONT_INITIAL_SIZE = 2;
    private int NO_IMAGE_FONT_STEP_SIZE = 2;
    private Font NO_IMAGE_FONT = new Font("Arial", Font.PLAIN, NO_IMAGE_FONT_INITIAL_SIZE);
    private Color NO_IMAGE_FONT_COLOR = Color.BLACK;
    // own properties
    private File imageFile; // the image file to be displayed
    private Boolean useImageAspectRatio; // Boolean value that determines if the image should scale
    private int imageMargin; // horizontal space between image border and legend border
    // encoding
    public static final String DATA_PROTOCOL_PNG_PREFIX = "data:image/png;base64,";
    // instance
    private static ImageItemRenderer instance = null;

    private ImageItemRenderer() {
        // private constructor is required to ensure singleton class
    }

    public static ImageItemRenderer getInstance() {
        if (instance == null) {
            instance = new ImageItemRenderer();
        }

        return instance;
    }

    @Override
    public boolean isAnAvailableRenderer(Item item) {
        return item instanceof ImageItem;
    }

    /**
     *
     * @param graphics2D - graphics object for the target
     * @param target - the target onto which the item should be rendered - SVG,
     * PDF or G2D
     * @param legendNode - BlockNode onto which the legend content will be
     * rendered.
     */
    @Override
    protected void renderToGraphics(Graphics2D graphics2D, RenderTarget target, BlockNode legendNode) {
        try {
            // get the legendNode Geometry
            int blockOriginX = (int) (legendNode.getOriginX());
            int blockOriginY = (int) (legendNode.getOriginY());
            int blockWidth = (int) legendNode.getBlockWidth();
            int blockHeight = (int) legendNode.getBlockHeight();
            Item item = legendNode.getItem();

            // if an image node is already created within a legend node, we need not create it again and build the inplace editors for it.
            BlockNode imageNode = legendNode.getChild(IMAGENODE);
            if (imageNode == null) {
                // the image node has not been added. Hence, add create and add it.
                imageNode = legendNode.addChild(blockOriginX + imageMargin, blockOriginY + imageMargin, blockWidth - 2 * imageMargin, blockHeight - 2 * imageMargin, IMAGENODE);

                // build and associate an inplace editor with the image node
                buildInplaceImage(imageNode, item, graphics2D, target);
            }

            // irrespective of whether an image node is newly built, update the origin and dimensions of the image legend
            imageNode.updateGeometry(blockOriginX + imageMargin, blockOriginY + imageMargin, blockWidth - 2 * imageMargin, blockHeight - 2 * imageMargin);

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

                // dynamically adapting text, when the legend is being transformed
                graphics2D.setFont(font);
                while (graphics2D.getFontMetrics().stringWidth(noImageMessage) < canvasWidth) {
                    fontSize += stepSize;
                    font = font.deriveFont((float) fontSize);
                    graphics2D.setFont(font);
                }

                fontSize -= stepSize; //font size wouldve gone one step ahead, decrease it.
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

    /**
     *
     * @param imageNode - BlockNode containing the image content
     * @param item - the legend image item
     * @param graphics2D - the graphics object for the target
     * @param target - the target onto which the item should be rendered - SVG,
     * PDF or G2D
     */
    private void buildInplaceImage(BlockNode imageNode, Item item, Graphics2D graphics2d, RenderTarget target) {
        Graph graph = null;
        InplaceItemBuilder ipbuilder = Lookup.getDefault().lookup(InplaceItemBuilder.class);
        InplaceEditor ipeditor = ipbuilder.createInplaceEditor(graph, imageNode);

        Row r;
        Column col;
        PreviewProperty[] previewProperties = item.getData(LegendItem.OWN_PROPERTIES);
        int itemIndex = item.getData(LegendItem.ITEM_INDEX);
        Map<String, Object> data;
        BaseElement addedElement;

        r = ipeditor.addRow();
        // see ElementLabel.java to understand how this element is being structured within the inplace editor
        col = r.addColumn(false);
        data = new HashMap<String, Object>();
        data.put(ElementLabel.LABEL_TEXT, "Source: ");
        data.put(ElementLabel.LABEL_COLOR, InplaceItemRenderer.LABEL_COLOR);
        data.put(ElementLabel.LABEL_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.LABEL, itemIndex, null, data, false, null);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

        // see ElementFile.java to understand how this element is being structured within the inplace editor
        col = r.addColumn(false);
        data = new HashMap<String, Object>();
        data.put(ElementFile.FILE_PATH, "/org/gephi/legend/graphics/file.png");
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.FILE, itemIndex, previewProperties[ImageProperty.IMAGE_URL], data, false, null);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

        r = ipeditor.addRow();
        // see ElementLabel.java to understand how this element is being structured within the inplace editor
        col = r.addColumn(false);
        data = new HashMap<String, Object>();
        data.put(ElementLabel.LABEL_TEXT, "Margin: ");
        data.put(ElementLabel.LABEL_COLOR, InplaceItemRenderer.LABEL_COLOR);
        data.put(ElementLabel.LABEL_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.LABEL, itemIndex, null, data, false, null);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

        // see ElementNumber.java to understand how this element is being structured within the inplace editor
        col = r.addColumn(false);
        data = new HashMap<String, Object>();
        data.put(ElementNumber.NUMBER_COLOR, InplaceItemRenderer.NUMBER_COLOR);
        data.put(ElementNumber.NUMBER_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.NUMBER, itemIndex, previewProperties[ImageProperty.IMAGE_MARGIN], data, false, null);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

        r = ipeditor.addRow();
        // see ElementLabel.java to understand how this element is being structured within the inplace editor
        col = r.addColumn(false);
        data = new HashMap<String, Object>();
        data.put(ElementLabel.LABEL_TEXT, "Scale: ");
        data.put(ElementLabel.LABEL_COLOR, InplaceItemRenderer.LABEL_COLOR);
        data.put(ElementLabel.LABEL_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.LABEL, itemIndex, null, data, false, null);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

        col = r.addColumn(false);
        // see ElementCheckbox.java to understand how this element is being structured within the inplace editor
        data = new HashMap<String, Object>();
        data.put(ElementCheckbox.IS_CHECKED, useImageAspectRatio);
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.CHECKBOX, itemIndex, previewProperties[ImageProperty.LOCK_ASPECT_RATIO], data, false, null);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

        imageNode.setInplaceEditor(ipeditor);
    }

    /**
     *
     * @param item - image item being rendered
     * @param properties - PreviewProperty for the PreviewModel
     */
    @Override
    protected void readOwnPropertiesAndValues(Item item, PreviewProperties properties) {
        PreviewProperty[] imageItemPreviewProperties = item.getData(LegendItem.OWN_PROPERTIES);

        imageFile = (File) imageItemPreviewProperties[ImageProperty.IMAGE_URL].getValue();
        useImageAspectRatio = (Boolean) imageItemPreviewProperties[ImageProperty.LOCK_ASPECT_RATIO].getValue();
        imageMargin = (Integer) imageItemPreviewProperties[ImageProperty.IMAGE_MARGIN].getValue();
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ImageItemRenderer.class, "ImageItemRenderer.name");
    }

    /**
     * @param itemBuilder - the custom item builder being checked against
     * @param properties - preview properties of the preview model
     * @return True if the custom item builder can be built with
     * ImageItemBuilder
     */
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
