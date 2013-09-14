/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.pdf.PdfContentByte;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;
import org.apache.batik.svggen.DefaultExtensionHandler;
import org.apache.batik.svggen.ImageHandlerBase64Encoder;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.gephi.graph.api.Graph;
import org.gephi.legend.inplaceeditor.Column;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.inplaceeditor.InplaceItemBuilder;
import org.gephi.legend.inplaceeditor.InplaceItemRenderer;
import org.gephi.legend.inplaceeditor.Row;
import org.gephi.legend.inplaceelements.BaseElement;
import org.gephi.legend.inplaceelements.ElementColor;
import org.gephi.legend.inplaceelements.ElementFont;
import org.gephi.legend.inplaceelements.ElementImage;
import org.gephi.legend.inplaceelements.ElementLabel;
import org.gephi.legend.inplaceelements.ElementNumber;
import org.gephi.legend.inplaceelements.ElementText;
import org.gephi.legend.mouse.LegendMouseListener;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.legend.spi.LegendItemRenderer;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.MouseResponsiveRenderer;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.preview.spi.Renderer;
import org.openide.util.Lookup;

/**
 *
 * @author mvvijesh, edubecks
 */
public abstract class AbstractLegendItemRenderer implements Renderer {

    protected Integer currentItemIndex;
    protected float graphOriginX = Float.MAX_VALUE;
    protected float graphOriginY = Float.MAX_VALUE;
    protected float graphWidth = 0;
    protected float graphHeight = 0;
    protected final int MARGIN_BETWEEN_ELEMENTS = 5;
    // VARIABLES
    // IS DISPLAYING
    protected Boolean isDisplayingLegend;
    // BACKGROUND
    protected boolean backgroundIsDisplaying;
    protected Color backgroundColor;
    protected Boolean borderIsDisplaying;
    protected Color borderColor;
    protected int borderLineThick;
    // DIMENSIONS
    protected Integer currentWidth;
    protected Integer currentHeight;
    protected AffineTransform originTranslation;
    protected float currentRealOriginX;
    protected float currentRealOriginY;
    // DESCRIPTION
    protected Boolean isDisplayingDescription;
    protected String description;
    protected Alignment descriptionAlignment;
    protected Font descriptionFont;
    protected Color descriptionFontColor;
    // TITLE
    protected Boolean isDisplayingTitle;
    protected String title;
    protected Font titleFont;
    protected Alignment titleAlignment;
    protected Color titleFontColor;
    // TRANSFORMATION
    protected Boolean currentIsSelected = Boolean.FALSE;
    protected Boolean currentIsBeingTransformed;
    protected final Color TRANSFORMATION_LEGEND_BORDER_COLOR = new Color(0.5f, 0.5f, 0.5f, 0.5f);
    protected final Color TRANSFORMATION_LEGEND_CENTER_COLOR = new Color(1f, 1f, 1f, 0.5f);
    protected int TRANSFORMATION_LEGEND_FONT_SIZE = 20;
    protected int TRANSFORMATION_LEGEND_FONT_SIZE_MIN = 20;
    protected Font TRANSFORMATION_LEGEND_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 2 * TRANSFORMATION_LEGEND_FONT_SIZE);
    protected final String TRANSFORMATION_LEGEND_LABEL = "transforming legend..";
    protected final Color TRANSFORMATION_ANCHOR_COLOR = Color.LIGHT_GRAY;
    protected final int TRANSFORMATION_ANCHOR_SIZE = 20;
    protected final int TRANSFORMATION_ANCHOR_LINE_THICK = 3;

    /**
     * the Function that actually renders the legend using the Graphics2D Object
     *
     * @param graphics2D Graphics2D instance used to render legend
     * @param target the rendering target - can be G2D, PDF or SVG
     * @param legendNode the node onto which the legend data must be rendered
     */
    protected abstract void renderToGraphics(Graphics2D graphics2D, RenderTarget target, BlockNode legendNode);

    /**
     * Function that reads the custom properties values from the
     * PreviewProperties of the current PreviewModel
     *
     * @param item current Legend Item
     * @param properties PreviewProperties of the current PreviewModel
     */
    protected abstract void readOwnPropertiesAndValues(Item item, PreviewProperties properties);

    /**
     * Indicates if it is a legend item renderer for the given item.
     *
     * @param item Legend item
     * @return True if it is a renderer for the item, false otherwise
     */
    public abstract boolean isAnAvailableRenderer(Item item);

    private void readLocationProperties(Item item, PreviewProperties previewProperties) {
        if (item != null) {
            currentItemIndex = item.getData(LegendItem.ITEM_INDEX);

            // LEGEND DIMENSIONS
            currentWidth = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.WIDTH));
            currentHeight = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.HEIGHT));

            // GRAPH DIMENSIONS
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            PreviewModel previewModel = previewController.getModel();
            Dimension dimensions = previewModel.getDimensions();
            graphHeight = dimensions.height;
            graphWidth = dimensions.width;
            Point topLeftPosition = previewModel.getTopLeftPosition();
            graphOriginX = topLeftPosition.x;
            graphOriginY = topLeftPosition.y;

            // LEGEND POSITION
            currentRealOriginX = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.USER_ORIGIN_X));
            currentRealOriginY = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.USER_ORIGIN_Y));
        }
    }

    private void readLegendPropertiesAndValues(Item item, PreviewProperties previewProperties) {
        if (item != null) {
            currentIsSelected = item.getData(LegendItem.IS_SELECTED);
            currentIsBeingTransformed = item.getData(LegendItem.IS_BEING_TRANSFORMED);

            readLocationProperties(item, previewProperties);

            isDisplayingLegend = previewProperties.getBooleanValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.IS_DISPLAYING));

            // BACKGROUND
            backgroundIsDisplaying = previewProperties.getBooleanValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.BACKGROUND_IS_DISPLAYING));
            backgroundColor = previewProperties.getColorValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.BACKGROUND_COLOR));
            borderIsDisplaying = previewProperties.getBooleanValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.BORDER_IS_DISPLAYING));
            borderColor = previewProperties.getColorValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.BORDER_COLOR));
            borderLineThick = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.BORDER_LINE_THICK));

            // TITLE
            isDisplayingTitle = previewProperties.getBooleanValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE_IS_DISPLAYING));
            titleFont = previewProperties.getFontValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE_FONT));
            titleFontColor = previewProperties.getColorValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE_FONT_COLOR));
            titleAlignment = (Alignment) previewProperties.getValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE_ALIGNMENT));
            title = previewProperties.getStringValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.TITLE));

            //DESCRIPTION
            isDisplayingDescription = previewProperties.getBooleanValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION_IS_DISPLAYING));
            descriptionFont = previewProperties.getFontValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION_FONT));
            descriptionFontColor = previewProperties.getColorValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION_FONT_COLOR));
            descriptionAlignment = (Alignment) previewProperties.getValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION_ALIGNMENT));
            description = previewProperties.getStringValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, currentItemIndex, LegendProperty.DESCRIPTION));
        }
    }

    private void renderSVG(SVGTarget target) {
        org.w3c.dom.Document document = target.getDocument();

        SVGGeneratorContext svgGeneratorContext = SVGGraphics2D.buildSVGGeneratorContext(document, new ImageHandlerBase64Encoder(), new DefaultExtensionHandler());
        svgGeneratorContext.setEmbeddedFontsOn(true);

        SVGGraphics2D graphics2D = new SVGGraphics2D(svgGeneratorContext, false);

        originTranslation = new AffineTransform();
        originTranslation.translate(currentRealOriginX, currentRealOriginY);
        render(graphics2D, target, originTranslation, currentWidth, currentHeight, currentItemIndex);

        //appending
        org.w3c.dom.Element svgRoot = document.getDocumentElement();
        svgRoot.appendChild(graphics2D.getRoot().getLastChild());
        graphics2D.dispose();
    }

    private void renderPDF(PDFTarget target) {
        PdfContentByte pdfContentByte = target.getContentByte();
        com.itextpdf.text.Document pdfDocument = pdfContentByte.getPdfDocument();
        pdfContentByte.saveState();

        originTranslation = new AffineTransform();
        // BUG dont know why 11
        originTranslation.translate(graphOriginX, graphOriginY - 11);
        pdfContentByte.transform(originTranslation);
        Graphics2D graphics2D = new PdfGraphics2D(pdfContentByte, graphWidth, graphHeight);
        originTranslation = new AffineTransform();

        originTranslation.translate(-graphOriginX, -graphOriginY);
        originTranslation.translate(currentRealOriginX, currentRealOriginY);
        render(graphics2D, target, originTranslation, currentWidth, currentHeight, currentItemIndex);
        graphics2D.dispose();
        pdfContentByte.restoreState();
    }

    private void renderG2D(G2DTarget target, int itemIndex) {

        Graphics2D graphics2D = target.getGraphics();

        AffineTransform saveState = graphics2D.getTransform();

        originTranslation = new AffineTransform(saveState);
        originTranslation.translate(currentRealOriginX, currentRealOriginY);

        /*
         if (currentIsBeingTransformed) {
         renderTransformed(graphics2D, originTranslation, currentWidth, currentHeight);
         drawScaleAnchors(graphics2D, originTranslation, currentWidth, currentHeight);
         } else {
         render(graphics2D, originTranslation, currentWidth, currentHeight, itemIndex);
         }
         */
        render(graphics2D, target, originTranslation, currentWidth, currentHeight, itemIndex);

        graphics2D.setTransform(saveState);
    }

    private void renderTransformed(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        graphics2D.setTransform(origin);
        graphics2D.setColor(TRANSFORMATION_LEGEND_BORDER_COLOR);
        graphics2D.fillRect(0, 0, width, height);
        graphics2D.setColor(TRANSFORMATION_LEGEND_CENTER_COLOR);
        graphics2D.fillRect(TRANSFORMATION_ANCHOR_LINE_THICK,
                TRANSFORMATION_ANCHOR_LINE_THICK,
                width - 2 * TRANSFORMATION_ANCHOR_LINE_THICK,
                height - 2 * TRANSFORMATION_ANCHOR_LINE_THICK);
        // centeredText
        graphics2D.setColor(TRANSFORMATION_LEGEND_BORDER_COLOR);
        // determine the optimum font size for the draw area
        int drawAreaWidth = width - 2 * TRANSFORMATION_ANCHOR_LINE_THICK;
        Float tolerance = 0.9f;
        int fontSizeStep = 1;
        int draggedLegendLabelWidth = graphics2D.getFontMetrics().stringWidth(TRANSFORMATION_LEGEND_LABEL);

        while (draggedLegendLabelWidth <= tolerance * drawAreaWidth) {
            TRANSFORMATION_LEGEND_FONT_SIZE += fontSizeStep;
            TRANSFORMATION_LEGEND_FONT = TRANSFORMATION_LEGEND_FONT.deriveFont(1.0f * TRANSFORMATION_LEGEND_FONT_SIZE);
            graphics2D.setFont(TRANSFORMATION_LEGEND_FONT);
            draggedLegendLabelWidth = graphics2D.getFontMetrics().stringWidth(TRANSFORMATION_LEGEND_LABEL);
        }

        while (draggedLegendLabelWidth >= drawAreaWidth && TRANSFORMATION_LEGEND_FONT_SIZE >= TRANSFORMATION_LEGEND_FONT_SIZE_MIN) {
            TRANSFORMATION_LEGEND_FONT_SIZE -= fontSizeStep;
            TRANSFORMATION_LEGEND_FONT = TRANSFORMATION_LEGEND_FONT.deriveFont(1.0f * TRANSFORMATION_LEGEND_FONT_SIZE);
            graphics2D.setFont(TRANSFORMATION_LEGEND_FONT);
            draggedLegendLabelWidth = graphics2D.getFontMetrics().stringWidth(TRANSFORMATION_LEGEND_LABEL);
        }
        graphics2D.drawString(TRANSFORMATION_LEGEND_LABEL, (width - draggedLegendLabelWidth) / 2, height / 2);
    }

    private void render(Graphics2D graphics2D, RenderTarget target, AffineTransform origin, Integer width, Integer height, int itemIndex) {

        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setTransform(origin);

        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        Item item = legendModel.getItemAtIndex(legendModel.getListIndexFromItemIndex(itemIndex));

        // General procedure to render a block:
        // 1. create a blockNode with proper parent block reference
        // 2. send its reference to the method that renders the block
        // 3. within the block renderer, set the origin, dimensions and build the inplaceEditor object depending what properties must be changed on rendering the inplaceEditor.

        // The rendering takes place from the outermost block to innermost block
        // BORDER - border is external
        BlockNode root = legendModel.getBlockTree(itemIndex); // root node corresponds to the entire area occupied by the legend, including the border.        
        renderBorder(graphics2D, target, width, height, borderLineThick, borderColor, item, root);

        // The background properties must also be included as a part of the root block.
        // BACKGROUND
        renderBackground(graphics2D, target, width, height, item, root);

        // add more options to the root block - control the visibility of title and description
        PreviewProperty[] legendItemPreviewProperties = item.getData(LegendItem.PROPERTIES);
        if (root.getInplaceEditor().getRows().size() == 2) {
            addVisibilityControls("Title:", legendItemPreviewProperties[LegendProperty.TITLE_IS_DISPLAYING], isDisplayingTitle, root, item, graphics2D, target);
            addVisibilityControls("Description:", legendItemPreviewProperties[LegendProperty.DESCRIPTION_IS_DISPLAYING], isDisplayingDescription, root, item, graphics2D, target);
        }

        root.updateGeometry(currentRealOriginX, currentRealOriginY, width, height);
        // drawBlockBoundary(graphics2D, root);

        // A title is a new block. (The first child of a root in fact, initiallly.)
        // Create a new block with root as the parent.
        // TITLE
        // AffineTransform titleOrigin = new AffineTransform(origin);
        float titleBoundaryWidth = 0;
        float titleBoundaryHeight = 0;
        if (isDisplayingTitle && !title.isEmpty()) {
            // set the font to titleFont so that the height can be computed accordingly
            graphics2D.setFont(titleFont);
            titleBoundaryWidth = width;
            titleBoundaryHeight = graphics2D.getFontMetrics().getHeight();
            renderTitle(graphics2D, (int) titleBoundaryWidth, (int) titleBoundaryHeight);

            BlockNode titleNode = root.getChild(BlockNode.TITLE);
            // the following code ensures that there is only one title node at any point in time.
            if (titleNode == null) {
                titleNode = root.addChild(0, 0, titleBoundaryWidth, titleBoundaryHeight, BlockNode.TITLE);
                buildInplaceTitle(titleNode, item, graphics2D, target);
            }

            titleNode.updateGeometry(currentRealOriginX, currentRealOriginY, titleBoundaryWidth, titleBoundaryHeight);
            // drawBlockBoundary(graphics2D, titleNode);
        } else {
            root.removeChild(BlockNode.TITLE);
        }

        // A description is a new block. (The second child of a root in fact, initially)
        // Create a new block with root as the parent.
        // DESCRIPTION
        // AffineTransform descOrigin = new AffineTransform(origin);
        float descBoundaryWidth = 0;
        float descBoundaryHeight = 0;
        if (isDisplayingDescription && !description.isEmpty()) {
            // set the font to titleFont so that the height can be computed accordingly
            graphics2D.setFont(descriptionFont);
            descBoundaryWidth = width;
            descBoundaryHeight = graphics2D.getFontMetrics().getHeight();
            // descOrigin.translate(0, height - descBoundaryHeight);
            renderDescription(graphics2D, 0, (int) (height - descBoundaryHeight), (int) descBoundaryWidth, (int) descBoundaryHeight);

            BlockNode descNode = root.getChild(BlockNode.DESC);
            // the following code ensures that there is only one description node at any point in time.
            if (descNode == null) {
                descNode = root.addChild(0, height - descBoundaryHeight, descBoundaryWidth, descBoundaryHeight, BlockNode.DESC);
                buildInplaceDesc(descNode, item, graphics2D, target);
            }

            descNode.updateGeometry(currentRealOriginX, currentRealOriginY + height - descBoundaryHeight, descBoundaryWidth, descBoundaryHeight);
            // drawBlockBoundary(graphics2D, descNode);
        } else {
            root.removeChild(BlockNode.DESC);
        }

        // rendering legend
        BlockNode legendNode = root.getChild(BlockNode.LEGEND);
        if (legendNode == null) {
            legendNode = root.addChild(currentRealOriginX, currentRealOriginY + titleBoundaryHeight, width, height - titleBoundaryHeight - descBoundaryHeight, BlockNode.LEGEND);
            legendNode.setInplaceEditor(root.getInplaceEditor()); // for all emptpy areas in the legend block, a click should correspond to the root's inplace editor
        }

        legendNode.updateGeometry(currentRealOriginX, currentRealOriginY + titleBoundaryHeight, width, height - titleBoundaryHeight - descBoundaryHeight);
        // drawBlockBoundary(graphics2D, legendNode);
        renderToGraphics(graphics2D, target, legendNode);

        // draw the anchors if the item is selected
        if (currentIsSelected) {
            drawScaleAnchors(graphics2D, origin, width, height);
        }
    }

    private void drawScaleAnchors(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        float[][] anchorLocations = {
            {-TRANSFORMATION_ANCHOR_SIZE / 2, -TRANSFORMATION_ANCHOR_SIZE / 2, TRANSFORMATION_ANCHOR_SIZE, TRANSFORMATION_ANCHOR_SIZE},
            {width - TRANSFORMATION_ANCHOR_SIZE / 2, -TRANSFORMATION_ANCHOR_SIZE / 2, TRANSFORMATION_ANCHOR_SIZE, TRANSFORMATION_ANCHOR_SIZE},
            {-TRANSFORMATION_ANCHOR_SIZE / 2, height - TRANSFORMATION_ANCHOR_SIZE / 2, TRANSFORMATION_ANCHOR_SIZE, TRANSFORMATION_ANCHOR_SIZE},
            {width - TRANSFORMATION_ANCHOR_SIZE / 2, height - TRANSFORMATION_ANCHOR_SIZE / 2, TRANSFORMATION_ANCHOR_SIZE, TRANSFORMATION_ANCHOR_SIZE}
        };


        graphics2D.setTransform(origin);
        graphics2D.setColor(TRANSFORMATION_LEGEND_BORDER_COLOR);
        graphics2D.drawRect(0, 0, width, height);

        for (int i = 0; i < anchorLocations.length; i++) {
            graphics2D.setColor(TRANSFORMATION_ANCHOR_COLOR);
            graphics2D.fillRect((int) anchorLocations[i][0], (int) anchorLocations[i][1], (int) anchorLocations[i][2], (int) anchorLocations[i][3]);

            graphics2D.setColor(Color.WHITE);
            graphics2D.fillRect((int) anchorLocations[i][0] + TRANSFORMATION_ANCHOR_LINE_THICK,
                    (int) anchorLocations[i][1] + TRANSFORMATION_ANCHOR_LINE_THICK,
                    (int) anchorLocations[i][2] - 2 * TRANSFORMATION_ANCHOR_LINE_THICK,
                    (int) anchorLocations[i][3] - 2 * TRANSFORMATION_ANCHOR_LINE_THICK);
        }
    }

    private void renderBorder(Graphics2D graphics2D, RenderTarget target, Integer width, Integer height, Integer borderThick, Color borderColor, Item item, BlockNode root) {
        if (borderIsDisplaying) {
            graphics2D.setColor(borderColor);

            // border is external
            // top
            graphics2D.fillRect(-borderThick, -borderThick, width + 2 * borderThick, borderThick);
            // bottom
            graphics2D.fillRect(-borderThick, height, width + 2 * borderThick, borderThick);
            // left
            graphics2D.fillRect(-borderThick, 0, borderThick, height);
            // right
            graphics2D.fillRect(width, 0, borderThick, height);
        }

        if (root.getInplaceEditor() == null) {
            // this part of the code gets executed only when the element gets rendered for the first time. 
            // next time onwards, the ipeditor will already be built and it need not be structured every time the item is being rendered.

            Graph graph = null;
            InplaceItemBuilder ipbuilder = Lookup.getDefault().lookup(InplaceItemBuilder.class);
            InplaceEditor ipeditor = ipbuilder.createInplaceEditor(graph, root);

            Row r;
            Column col;
            PreviewProperty[] previewProperties = item.getData(LegendItem.PROPERTIES);
            int itemIndex = item.getData(LegendItem.ITEM_INDEX);
            Map<String, Object> data;
            BaseElement addedElement;

            r = ipeditor.addRow();
            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementLabel.LABEL_TEXT, "Border: ");
            data.put(ElementLabel.LABEL_COLOR, InplaceItemRenderer.LABEL_COLOR);
            data.put(ElementLabel.LABEL_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.LABEL, itemIndex, null, data, false, null);
            addedElement.computeNumberOfBlocks(graphics2D, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementImage.IMAGE_BOOL, borderIsDisplaying);
            data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/invisible.png");
            data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/visible.png");
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.BORDER_IS_DISPLAYING], data, false, null);
            addedElement.computeNumberOfBlocks(graphics2D, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementColor.COLOR_MARGIN, InplaceItemRenderer.COLOR_MARGIN);
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.COLOR, itemIndex, previewProperties[LegendProperty.BORDER_COLOR], data, false, null);
            addedElement.computeNumberOfBlocks(graphics2D, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementNumber.NUMBER_COLOR, InplaceItemRenderer.NUMBER_COLOR);
            data.put(ElementNumber.NUMBER_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.NUMBER, itemIndex, previewProperties[LegendProperty.BORDER_LINE_THICK], data, false, null);
            addedElement.computeNumberOfBlocks(graphics2D, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            root.setInplaceEditor(ipeditor);
        }
    }

    private void renderBackground(Graphics2D graphics2D, RenderTarget target, Integer width, Integer height, Item item, BlockNode root) {
        // this part of the code will get executed only after the border is rendered. Hence, an inplaceeditor will already be set.
        if (backgroundIsDisplaying) {
            graphics2D.setColor(backgroundColor);
            graphics2D.fillRect(0, 0, width, height);
        }

        // this function will be called every now and then, in response to mouse events. Hence, the background controls get accumulated.
        // To avoid this, we've to append only when the sole controls added is the border controls. i.e, the number of rows in the ipeditor is 1.
        InplaceEditor ipeditor = root.getInplaceEditor();
        if (ipeditor.getRows().size() == 1) {
            Row r;
            Column col;
            PreviewProperty[] previewProperties = item.getData(LegendItem.PROPERTIES);
            int itemIndex = item.getData(LegendItem.ITEM_INDEX);
            Map<String, Object> data;
            BaseElement addedElement;

            // add the row that controls background properties
            r = ipeditor.addRow();
            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementLabel.LABEL_TEXT, "Background: ");
            data.put(ElementLabel.LABEL_COLOR, InplaceItemRenderer.LABEL_COLOR);
            data.put(ElementLabel.LABEL_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.LABEL, itemIndex, null, data, false, null); // the last two arguments are related to grouped items. 
            //For an element not belonging to any group, its values do not matter.
            addedElement.computeNumberOfBlocks(graphics2D, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementImage.IMAGE_BOOL, backgroundIsDisplaying);
            data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/visible.png");
            data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/invisible.png");
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.BACKGROUND_IS_DISPLAYING], data, false, null);
            addedElement.computeNumberOfBlocks(graphics2D, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementColor.COLOR_MARGIN, InplaceItemRenderer.COLOR_MARGIN);
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.COLOR, itemIndex, previewProperties[LegendProperty.BACKGROUND_COLOR], data, false, null);
            addedElement.computeNumberOfBlocks(graphics2D, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);
        }
    }

    private void addVisibilityControls(String displayString, PreviewProperty prop, Boolean defaultVal, BlockNode root, Item item, Graphics2D graphics2d, RenderTarget target) {
        InplaceEditor ipeditor = root.getInplaceEditor();
        int itemIndex = item.getData(LegendItem.ITEM_INDEX);

        Row r = ipeditor.addRow();
        Map<String, Object> data;
        BaseElement addedElement;

        Column col = r.addColumn(false);
        data = new HashMap<String, Object>();
        data.put(ElementLabel.LABEL_TEXT, displayString);
        data.put(ElementLabel.LABEL_COLOR, InplaceItemRenderer.LABEL_COLOR);
        data.put(ElementLabel.LABEL_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.LABEL, itemIndex, null, data, false, null);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

        col = r.addColumn(false);
        data = new HashMap<String, Object>();
        data.put(ElementImage.IMAGE_BOOL, defaultVal);
        data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/visible.png");
        data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/invisible.png");
        addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, prop, data, false, null);
        addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);
    }

    private void renderTitle(Graphics2D graphics2D, Integer boundaryWidth, Integer boundaryHeight) {
        // this part of the code is executed only after rendering the background. (border and background would've already been rendered)
        // isDisplayingTitle will definitely be enabled if the control is transfered here. So, there is no need to check for it.
        legendDrawText(graphics2D, title, titleFont, titleFontColor, 0, 0, boundaryWidth, boundaryHeight, titleAlignment, false);
    }

    private void buildInplaceTitle(BlockNode titleNode, Item item, Graphics2D graphics2d, RenderTarget target) {
        if (titleNode.getInplaceEditor() == null) {
            Graph graph = null;
            InplaceItemBuilder ipbuilder = Lookup.getDefault().lookup(InplaceItemBuilder.class);
            InplaceEditor ipeditor = ipbuilder.createInplaceEditor(graph, titleNode);

            Row r;
            Column col;
            PreviewProperty[] previewProperties = item.getData(LegendItem.PROPERTIES);

            int itemIndex = item.getData(LegendItem.ITEM_INDEX);
            Map<String, Object> data;
            BaseElement addedElement;
            String propertyName;

            r = ipeditor.addRow();
            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementLabel.LABEL_TEXT, "Title :");
            data.put(ElementLabel.LABEL_COLOR, InplaceItemRenderer.LABEL_COLOR);
            data.put(ElementLabel.LABEL_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.LABEL, itemIndex, null, data, false, null);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            // we could have another property for title background.

            r = ipeditor.addRow();
            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementText.EDIT_IMAGE, "/org/gephi/legend/graphics/edit.png");
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.TEXT, itemIndex, previewProperties[LegendProperty.TITLE], data, false, null);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementColor.COLOR_MARGIN, InplaceItemRenderer.COLOR_MARGIN);
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.COLOR, itemIndex, previewProperties[LegendProperty.TITLE_FONT_COLOR], data, false, null);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementFont.DISPLAY_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
            data.put(ElementFont.DISPLAY_FONT_COLOR, InplaceItemRenderer.FONT_DISPLAY_COLOR);
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.FONT, itemIndex, previewProperties[LegendProperty.TITLE_FONT], data, false, null);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            r = ipeditor.addRow();
            col = r.addColumn(true);
            // left-alignment
            data = new HashMap<String, Object>();
            data.put(ElementImage.IMAGE_BOOL, titleAlignment == Alignment.LEFT);
            data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/left_selected.png");
            data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/left_unselected.png");
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.TITLE_ALIGNMENT], data, titleAlignment == Alignment.LEFT, Alignment.LEFT);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            // center-alignment
            data = new HashMap<String, Object>();
            data.put(ElementImage.IMAGE_BOOL, titleAlignment == Alignment.CENTER);
            data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/center_selected.png");
            data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/center_unselected.png");
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.TITLE_ALIGNMENT], data, titleAlignment == Alignment.CENTER, Alignment.CENTER);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            // right alignment
            data = new HashMap<String, Object>();
            data.put(ElementImage.IMAGE_BOOL, titleAlignment == Alignment.RIGHT);
            data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/right_selected.png");
            data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/right_unselected.png");
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.TITLE_ALIGNMENT], data, titleAlignment == Alignment.RIGHT, Alignment.RIGHT);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            // justified
            data = new HashMap<String, Object>();
            data.put(ElementImage.IMAGE_BOOL, titleAlignment == Alignment.JUSTIFIED);
            data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/justified_selected.png");
            data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/justified_unselected.png");
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.TITLE_ALIGNMENT], data, titleAlignment == Alignment.JUSTIFIED, Alignment.JUSTIFIED);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            titleNode.setInplaceEditor(ipeditor);
        }
    }

    private void renderDescription(Graphics2D graphics2D, Integer x, Integer y, Integer width, Integer height) {
        legendDrawText(graphics2D, description, descriptionFont, descriptionFontColor, x, y, width, height, descriptionAlignment);
    }

    private void buildInplaceDesc(BlockNode descNode, Item item, Graphics2D graphics2d, RenderTarget target) {
        // the flow of this method is the same as the buildInplaceTitle method. 
        // The flow is repeated keeping in mind that there might be some special treatment give to description in the future.
        if (descNode.getInplaceEditor() == null) {
            Graph graph = null;
            InplaceItemBuilder ipbuilder = Lookup.getDefault().lookup(InplaceItemBuilder.class);
            InplaceEditor ipeditor = ipbuilder.createInplaceEditor(graph, descNode);

            Row r;
            Column col;
            PreviewProperty[] previewProperties = item.getData(LegendItem.PROPERTIES);
            int itemIndex = item.getData(LegendItem.ITEM_INDEX);
            Map<String, Object> data;
            BaseElement addedElement;

            r = ipeditor.addRow();
            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementLabel.LABEL_TEXT, "Description:");
            data.put(ElementLabel.LABEL_COLOR, InplaceItemRenderer.LABEL_COLOR);
            data.put(ElementLabel.LABEL_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.LABEL, itemIndex, null, data, false, null);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            // we could have another property for description background.

            r = ipeditor.addRow();
            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementText.EDIT_IMAGE, "/org/gephi/legend/graphics/edit.png");
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.TEXT, itemIndex, previewProperties[LegendProperty.DESCRIPTION], data, false, null);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementColor.COLOR_MARGIN, InplaceItemRenderer.COLOR_MARGIN);
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.COLOR, itemIndex, previewProperties[LegendProperty.DESCRIPTION_FONT_COLOR], data, false, null);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            col = r.addColumn(false);
            data = new HashMap<String, Object>();
            data.put(ElementFont.DISPLAY_FONT, InplaceItemRenderer.INPLACE_DEFAULT_DISPLAY_FONT);
            data.put(ElementFont.DISPLAY_FONT_COLOR, InplaceItemRenderer.FONT_DISPLAY_COLOR);
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.FONT, itemIndex, previewProperties[LegendProperty.DESCRIPTION_FONT], data, false, null);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            r = ipeditor.addRow();
            col = r.addColumn(true);
            // left-alignment
            data = new HashMap<String, Object>();
            data.put(ElementImage.IMAGE_BOOL, descriptionAlignment == Alignment.LEFT);
            data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/left_selected.png");
            data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/left_unselected.png");
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.DESCRIPTION_ALIGNMENT], data, descriptionAlignment == Alignment.LEFT, Alignment.LEFT);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            // center-alignment
            data = new HashMap<String, Object>();
            data.put(ElementImage.IMAGE_BOOL, descriptionAlignment == Alignment.CENTER);
            data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/center_selected.png");
            data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/center_unselected.png");
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.DESCRIPTION_ALIGNMENT], data, descriptionAlignment == Alignment.CENTER, Alignment.CENTER);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            // right alignment
            data = new HashMap<String, Object>();
            data.put(ElementImage.IMAGE_BOOL, descriptionAlignment == Alignment.RIGHT);
            data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/right_selected.png");
            data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/right_unselected.png");
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.DESCRIPTION_ALIGNMENT], data, descriptionAlignment == Alignment.RIGHT, Alignment.RIGHT);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            // justified
            data = new HashMap<String, Object>();
            data.put(ElementImage.IMAGE_BOOL, descriptionAlignment == Alignment.JUSTIFIED);
            data.put(ElementImage.IMAGE_IF_TRUE, "/org/gephi/legend/graphics/justified_selected.png");
            data.put(ElementImage.IMAGE_IF_FALSE, "/org/gephi/legend/graphics/justified_unselected.png");
            addedElement = col.addElement(BaseElement.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.DESCRIPTION_ALIGNMENT], data, descriptionAlignment == Alignment.JUSTIFIED, Alignment.JUSTIFIED);
            addedElement.computeNumberOfBlocks(graphics2d, (G2DTarget) target, InplaceItemRenderer.DEFAULT_INPLACE_BLOCK_UNIT_SIZE);

            descNode.setInplaceEditor(ipeditor);
        }
    }

    private int getFontWidth(Graphics2D graphics2d, String str) {
        return graphics2d.getFontMetrics().stringWidth(str);
    }

    protected void drawBlockBoundary(Graphics2D graphics2D, BlockNode node) {
        graphics2D.setColor(Color.RED);
        graphics2D.setFont(new Font("Arial", Font.PLAIN, 20));
        int originX = (int) (node.getOriginX() - currentRealOriginX);
        int originY = (int) (node.getOriginY() - currentRealOriginY);
        int width = (int) node.getBlockWidth();
        int height = (int) node.getBlockHeight();
        int tagWidth = graphics2D.getFontMetrics().stringWidth(node.getTag());
        int tagHeight = graphics2D.getFontMetrics().getHeight();

        graphics2D.setColor(Color.RED);
        graphics2D.drawRect(originX, originY, width, height);
        graphics2D.drawString(node.getTag(), originX + width / 2 - tagWidth / 2, originY + height / 2);

        /*
         for (blockNode child : node.getChildren()) {
         int childOriginX = (int) child.getOriginX();
         int childOriginY = (int) child.getOriginY();
         int childWidth = (int) child.getBlockWidth();
         int childHeight = (int) child.getBlockHeight();

         drawBlockBoundaries(graphics2D, origin, child);
         }*/
    }

    /**
     * Using the width as a parameter, it computes the vertical space used by
     * some text even if it fits in multiple lines
     *
     * @param graphics2D
     * @param text string containing the text
     * @param font font used to display the text
     * @param width max width to be used by the text
     * @return
     */
    protected float getFontHeight(Graphics2D graphics2D, String text, Font font, Integer boundaryWidth, Integer boundaryHeight) {
        return legendDrawText(graphics2D, text, font, Color.BLACK, 0, 0, boundaryWidth, boundaryHeight, Alignment.LEFT, true);
    }

    @Override
    public void preProcess(PreviewModel previewModel) {
    }

    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {
        if (item != null) {
            readLegendPropertiesAndValues(item, properties);
            readOwnPropertiesAndValues(item, properties);
            if (isDisplayingLegend) {
                if (target instanceof G2DTarget) {
                    renderG2D((G2DTarget) target, currentItemIndex);
                } else if (target instanceof SVGTarget) {
                    renderSVG((SVGTarget) target);
                } else if (target instanceof PDFTarget) {
                    renderPDF((PDFTarget) target);
                }
            }
        }
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }

    protected float legendDrawText(Graphics2D graphics2D, String text, Font font, Color color, double x, double y, Integer width, Integer height, Alignment alignment, boolean isComputingSpace) {
        if (text.isEmpty()) {
            return 0f;
        }

        AttributedString styledText = new AttributedString(text);
        styledText.addAttribute(TextAttribute.FONT, font);
        graphics2D.setFont(font);
        graphics2D.setColor(color);
        AttributedCharacterIterator m_iterator = styledText.getIterator();
        int start = m_iterator.getBeginIndex();
        int end = m_iterator.getEndIndex();
        FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();

        LineBreakMeasurer measurer = new LineBreakMeasurer(m_iterator, fontRenderContext);
        measurer.setPosition(start);

        float xText = (float) x;
        float yText = (float) y; // text positions

        float descent = 0;
        float leading = 0;
        while (measurer.getPosition() < end) {
            TextLayout layout = measurer.nextLayout(width);

            yText += layout.getAscent();

            if (!isComputingSpace) {
                switch (alignment) {
                    case LEFT: {
                        break;
                    }

                    case RIGHT: {
                        Rectangle2D bounds = layout.getBounds();
                        xText = (float) ((x + width - bounds.getWidth()) - bounds.getX());
                        break;
                    }

                    case CENTER: {
                        Rectangle2D bounds = layout.getBounds();
                        xText = (float) ((x + width / 2 - bounds.getWidth() / 2) - bounds.getX());
                        break;
                    }

                    case JUSTIFIED: {
                        if (measurer.getPosition() < end) {
                            layout = layout.getJustifiedLayout(width);
                        }
                        break;
                    }
                }
                if (yText - y + layout.getDescent() + layout.getLeading() >= height) {
                    break;
                }
                layout.draw(graphics2D, xText, yText);
            }
            descent = layout.getDescent();
            leading = layout.getLeading();
            yText += descent + leading;
        }

        return (float) Math.ceil(yText - y - leading);
    }

    /**
     * Function that display some text just like the regular
     * <code>drawString</code> function from
     * <code>Graphics2D</code>. It has an additional parameter Alignment to
     * define the alignment of the text.
     *
     * @param graphics2D
     * @param text
     * @param font
     * @param color
     * @param x
     * @param y
     * @param width
     * @param height
     * @param alignment
     * @return
     */
    protected float legendDrawText(Graphics2D graphics2D, String text, Font font, Color color, double x, double y, Integer width, Integer height, Alignment alignment) {
        // (x, y, width, height) represents the box in which the given text must be rendered. The text is drawn in the vertical center by default. The horizontal alignment depends on the parameters.
        float spaceUsed = legendDrawText(graphics2D, text, font, color, x, y, width, height, alignment, true);
        y = y + (height - spaceUsed) / 2;
        return legendDrawText(graphics2D, text, font, color, x, y, width, height, alignment, false);
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        Class renderer = item.getData(LegendItem.RENDERER);
        return renderer != null && renderer.equals(getClass());
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}