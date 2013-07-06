/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.pdf.PdfContentByte;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import org.apache.batik.svggen.DefaultExtensionHandler;
import org.apache.batik.svggen.ImageHandlerBase64Encoder;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.gephi.graph.api.Graph;
import org.gephi.legend.inplaceeditor.column;
import org.gephi.legend.inplaceeditor.element;
import org.gephi.legend.inplaceeditor.inplaceEditor;
import org.gephi.legend.inplaceeditor.inplaceItemBuilder;
import org.gephi.legend.inplaceeditor.row;
import org.gephi.legend.mouse.LegendMouseListener;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItem.Alignment;
import org.gephi.legend.spi.LegendItemRenderer;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.MouseResponsiveRenderer;
import org.gephi.preview.spi.PreviewMouseListener;
import org.openide.util.Lookup;

/**
 *
 * @author edubecks
 */
public abstract class AbstractLegendItemRenderer implements LegendItemRenderer, MouseResponsiveRenderer {

    private Integer currentItemIndex;
    private float graphOriginX = Float.MAX_VALUE;
    private float graphOriginY = Float.MAX_VALUE;
    private float graphWidth = 0;
    private float graphHeight = 0;
    private final int MARGIN_BETWEEN_ELEMENTS = 5;
    // VARIABLES
    // IS DISPLAYING
    private Boolean isDisplayingLegend;
    // BACKGROUND
    private boolean backgroundIsDisplaying;
    private Color backgroundColor;
    private Boolean borderIsDisplaying;
    private Color borderColor;
    private int borderLineThick;
    // DIMENSIONS
    protected Integer currentWidth;
    protected Integer currentHeight;
    protected AffineTransform originTranslation;
    private float currentRealOriginX;
    private float currentRealOriginY;
    //description
    private Boolean isDisplayingDescription;
    private String description;
    private Alignment descriptionAlignment;
    private Font descriptionFont;
    private Color descriptionFontColor;
    //title
    private Boolean isDisplayingTitle;
    private String title;
    private Font titleFont;
    private Alignment titleAlignment;
    private Color titleFontColor;
    // TRANSFORMATION
    private Boolean currentIsSelected = Boolean.FALSE;
    private Boolean currentIsBeingTransformed;
    private final Color TRANSFORMATION_LEGEND_BORDER_COLOR = new Color(0.5f, 0.5f, 0.5f, 0.5f);
    private final Color TRANSFORMATION_LEGEND_CENTER_COLOR = new Color(1f, 1f, 1f, 0.5f);
    private int TRANSFORMATION_LEGEND_FONT_SIZE = 20;
    private int TRANSFORMATION_LEGEND_FONT_SIZE_MIN = 20;
    private Font TRANSFORMATION_LEGEND_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 2 * TRANSFORMATION_LEGEND_FONT_SIZE);
    private final String TRANSFORMATION_LEGEND_LABEL = "transforming legend..";
    private final Color TRANSFORMATION_ANCHOR_COLOR = Color.LIGHT_GRAY;
    private final int TRANSFORMATION_ANCHOR_SIZE = 20;
    private final int TRANSFORMATION_ANCHOR_LINE_THICK = 3;

    /**
     * the
     *
     * Function that actually renders the legend using the Graphics2D Object
     *
     * @param graphics2D Graphics2D instance used to render legend
     * @param origin transformation that contains the origin and level zoom of
     * the legend
     * @param width width of the legend to be rendered
     * @param height height of the legend to be rendered
     */
    protected abstract void renderToGraphics(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height);

    /**
     * Function that reads the custom properties values from the
     * PreviewProperties of the current PreviewModel
     *
     * @param item current Legend Item
     * @param properties PreviewProperties of the current PreviewModel
     */
    protected abstract void readOwnPropertiesAndValues(Item item, PreviewProperties properties);

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
        render(graphics2D, originTranslation, currentWidth, currentHeight, currentItemIndex);

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
        render(graphics2D, originTranslation, currentWidth, currentHeight, currentItemIndex);
        graphics2D.dispose();
        pdfContentByte.restoreState();
    }

    private void renderG2D(G2DTarget target, int itemIndex) {

        Graphics2D graphics2D = target.getGraphics();

        AffineTransform saveState = graphics2D.getTransform();

        originTranslation = new AffineTransform(saveState);
        originTranslation.translate(currentRealOriginX, currentRealOriginY);

        if (currentIsBeingTransformed) {
            renderTransformed(graphics2D, originTranslation, currentWidth, currentHeight);
            drawScaleAnchors(graphics2D, originTranslation, currentWidth, currentHeight);
        } else {
            render(graphics2D, originTranslation, currentWidth, currentHeight, itemIndex);
        }

        //render(graphics2D, originTranslation, currentWidth, currentHeight);
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

    private void render(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height, int itemIndex) {

        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        Item item = legendModel.getItemAtIndex(legendModel.getListIndexFromItemIndex(itemIndex));

        // General procedure to render a block:
        // 1. create a blockNode with proper parent block reference
        // 2. send its reference to the method that renders the block
        // 3. within the block renderer, set the origin, dimensions and build the inplaceEditor object depending what properties must be changed on rendering the inplaceEditor.

        // The rendering takes place from the outermost block to innermost block
        // BORDER - border is external
        blockNode root = legendModel.getBlockTree(itemIndex); // root node corresponds to the entire area occupied by the legend, including the border.
        root.updateGeometry(currentRealOriginX, currentRealOriginY, currentWidth, currentHeight);
        renderBorder(graphics2D, origin, width, height, borderLineThick, borderColor, item, root);

        // The background properties must also be included as a part of the root block.
        // BACKGROUND
        renderBackground(graphics2D, origin, width, height, item, root);

        // add more options to the root block - control the visibility of title and description
        PreviewProperty[] previewProperties = item.getData(LegendItem.PROPERTIES);
        if (root.getInplaceEditor().getRows().size() == 2) {
            addVisibilityControls("Title", previewProperties[LegendProperty.TITLE_IS_DISPLAYING], isDisplayingTitle, root, item);
            addVisibilityControls("Description", previewProperties[LegendProperty.DESCRIPTION_IS_DISPLAYING], isDisplayingDescription, root, item);
        }

        // A title is a new block. (The first child of a root in fact, initiallly.)
        // Create a new block with root as the parent.
        // TITLE
        AffineTransform titleOrigin = new AffineTransform(origin);
        float titleBoundaryWidth = 0;
        float titleBoundaryHeight = 0;
        if (isDisplayingTitle && !title.isEmpty()) {
            // set the font to titleFont so that the height can be computed accordingly
            graphics2D.setFont(titleFont);
            titleBoundaryWidth = width;
            titleBoundaryHeight = graphics2D.getFontMetrics().getHeight();
            renderTitle(graphics2D, titleOrigin, (int) titleBoundaryWidth, (int) titleBoundaryHeight);
            
            if (!root.hasChild(blockNode.TITLE)) {
                blockNode titleNode = root.addChild(0, 0, titleBoundaryWidth, titleBoundaryHeight, blockNode.TITLE);
                buildInplaceTitle(graphics2D, titleOrigin, titleNode, item);
            }
        } else {
            root.removeChild(blockNode.TITLE);
        }

        
        // A description is a new block. (The second child of a root in fact, initially)
        // Create a new block with root as the parent.
        // DESCRIPTION
        
        float descBoundaryWidth = 0;
        float descBoundaryHeight = 0;
        if (isDisplayingDescription && !description.isEmpty()) {
            // set the font to titleFont so that the height can be computed accordingly
            graphics2D.setFont(descriptionFont);
            descBoundaryWidth = width;
            descBoundaryHeight = graphics2D.getFontMetrics().getHeight();
            origin.translate(0, height - descBoundaryHeight);
            renderDescription(graphics2D, origin, (int) descBoundaryWidth, (int) descBoundaryHeight);
            if (!root.hasChild(blockNode.DESC)) {
                blockNode descNode = root.addChild(0, height - descBoundaryHeight, descBoundaryWidth, descBoundaryHeight, blockNode.DESC);
                buildInplaceDesc(graphics2D, origin, descNode, item);
            }
        } else {
            root.removeChild(blockNode.DESC);
        }
        
        /*
        blockNode descNode = new blockNode(root, Float.MAX_VALUE, Float.MAX_VALUE, 0, 0, item, blockNode.DESC);
        float descriptionHeight = 0;
        if (isDisplayingDescription && !description.isEmpty()) {
            descriptionHeight = getFontHeight(graphics2D, description, descriptionFont, width, height);
        }
        // LEGEND
        AffineTransform legendOrigin = new AffineTransform(origin);
        //adding space between elements
        legendOrigin.translate(0, titleBoundaryHeight + MARGIN_BETWEEN_ELEMENTS);
        int legendWidth = width;
        int legendHeight = (Integer) (height - Math.round(titleBoundaryHeight) - Math.round(descriptionHeight) - 2 * MARGIN_BETWEEN_ELEMENTS);

        // DESCRIPTION
        AffineTransform descriptionOrigin = new AffineTransform(origin);
        descriptionOrigin.translate(0, titleBoundaryHeight + legendHeight + MARGIN_BETWEEN_ELEMENTS);
        if (isDisplayingDescription && !description.isEmpty()) {
            renderDescription(graphics2D, descriptionOrigin, width, (int) descriptionHeight, item, descNode);
        }

        // rendering legend
        renderToGraphics(graphics2D, legendOrigin, legendWidth, legendHeight);

        // is selected
        if (currentIsSelected) {
            drawScaleAnchors(graphics2D, origin, width, height);
        }
        */
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

    private void renderBorder(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height, Integer borderThick, Color borderColor, Item item, blockNode root) {
        if (borderIsDisplaying) {
            graphics2D.setTransform(origin);
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
            inplaceItemBuilder ipbuilder = Lookup.getDefault().lookup(inplaceItemBuilder.class);
            inplaceEditor ipeditor = ipbuilder.createInplaceEditor(graph, root);
            ipeditor.setData(inplaceEditor.BLOCK_INPLACEEDITOR_GAP, (float) (TRANSFORMATION_ANCHOR_SIZE * 3.0 / 4.0));

            row r;
            column col;
            PreviewProperty[] previewProperties = item.getData(LegendItem.PROPERTIES);
            PreviewProperty prop;
            int itemIndex = item.getData(LegendItem.ITEM_INDEX);

            r = ipeditor.addRow();
            col = r.addColumn();
            Object[] data = new Object[1];
            data[0] = "Border: ";
            col.addElement(element.ELEMENT_TYPE.LABEL, itemIndex, null, data); //if its a label, property must be null.

            col = r.addColumn();
            data = new Object[3];
            data[0] = borderIsDisplaying;
            data[1] = "/org/gephi/legend/graphics/invisible.png";
            data[2] = "/org/gephi/legend/graphics/visible.png";
            col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.BORDER_IS_DISPLAYING], data);

            col = r.addColumn();
            data = new Object[0]; // for a color propoerty, extra data isnt needed.
            col.addElement(element.ELEMENT_TYPE.COLOR, itemIndex, previewProperties[LegendProperty.BORDER_COLOR], data);

            col = r.addColumn();
            data = new Object[0]; // for a numerical property, extra data isnt needed.
            col.addElement(element.ELEMENT_TYPE.NUMBER, itemIndex, previewProperties[LegendProperty.BORDER_LINE_THICK], data);

            root.setInplaceEditor(ipeditor);
        }
    }

    private void renderBackground(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height, Item item, blockNode root) {
        // this part of the code will get executed only after the border is rendered. Hence, an inplaceeditor will already be set.
        if (backgroundIsDisplaying) {
            graphics2D.setTransform(origin);
            graphics2D.setColor(backgroundColor);
            graphics2D.fillRect(0, 0, width, height);
        }

        // this function will be called every now and then, in response to mouse events. Hence, the background controls get accumulated.
        // To avoid this, we've to append only when the sole controls added is the border controls. i.e, the number of rows in the ipeditor is 1.
        inplaceEditor ipeditor = root.getInplaceEditor();
        if (ipeditor.getRows().size() == 1) {
            row r;
            column col;
            PreviewProperty[] previewProperties = item.getData(LegendItem.PROPERTIES);
            PreviewProperty prop;
            int itemIndex = item.getData(LegendItem.ITEM_INDEX);

            // add the row that controls background properties
            r = ipeditor.addRow();
            col = r.addColumn();
            Object[] data = new Object[1];
            data[0] = "Background: ";
            col.addElement(element.ELEMENT_TYPE.LABEL, itemIndex, null, data); //if its a label, property must be null.

            col = r.addColumn();
            data = new Object[3];
            data[0] = backgroundIsDisplaying;
            data[1] = "/org/gephi/legend/graphics/invisible.png";
            data[2] = "/org/gephi/legend/graphics/visible.png";
            col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.BACKGROUND_IS_DISPLAYING], data);

            col = r.addColumn();
            data = new Object[0];
            col.addElement(element.ELEMENT_TYPE.COLOR, itemIndex, previewProperties[LegendProperty.BACKGROUND_COLOR], data);
        }
    }

    private void addVisibilityControls(String displayString, PreviewProperty prop, Boolean defaultVal, blockNode root, Item item) {
        inplaceEditor ipeditor = root.getInplaceEditor();
        int itemIndex = item.getData(LegendItem.ITEM_INDEX);

        row r = ipeditor.addRow();

        column col = r.addColumn();
        Object[] data = new Object[1];
        data[0] = displayString;
        col.addElement(element.ELEMENT_TYPE.LABEL, itemIndex, null, data); //if its a label, property must be null.

        col = r.addColumn();
        data = new Object[3];
        data[0] = defaultVal;
        data[1] = "/org/gephi/legend/graphics/invisible.png";
        data[2] = "/org/gephi/legend/graphics/visible.png";
        col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, prop, data);
    }

    private void renderTitle(Graphics2D graphics2D, AffineTransform origin, Integer boundaryWidth, Integer boundaryHeight) {
        // this part of the code is executed only after rendering the background. (border and background would've already been rendered)
        // isDisplayingTitle will definitely be enabled if the control is transfered here. So, there is no need to check for it.
        graphics2D.setTransform(origin);
        legendDrawText(graphics2D, title, titleFont, titleFontColor, 0, 0, boundaryWidth, boundaryHeight, titleAlignment, false);
    }

    private void buildInplaceTitle(Graphics2D graphics2D, AffineTransform origin, blockNode titleNode, Item item) {
        graphics2D.setTransform(origin);
        if (titleNode.getInplaceEditor() == null) {
            Graph graph = null;
            inplaceItemBuilder ipbuilder = Lookup.getDefault().lookup(inplaceItemBuilder.class);
            inplaceEditor ipeditor = ipbuilder.createInplaceEditor(graph, titleNode);
            ipeditor.setData(inplaceEditor.BLOCK_INPLACEEDITOR_GAP, (float) (TRANSFORMATION_ANCHOR_SIZE * 3.0 / 4.0));

            row r;
            column col;
            PreviewProperty[] previewProperties = item.getData(LegendItem.PROPERTIES);
            PreviewProperty prop;
            int itemIndex = item.getData(LegendItem.ITEM_INDEX);

            r = ipeditor.addRow();
            col = r.addColumn();
            Object[] data = new Object[1];
            data[0] = "Title: ";
            col.addElement(element.ELEMENT_TYPE.LABEL, itemIndex, null, data); //if its a label, property must be null.

            col = r.addColumn();
            data = new Object[0];
            col.addElement(element.ELEMENT_TYPE.TEXT, itemIndex, previewProperties[LegendProperty.TITLE], data);

            // we could have another property for title background.

            r = ipeditor.addRow();
            col = r.addColumn();
            data = new Object[0];
            col.addElement(element.ELEMENT_TYPE.COLOR, itemIndex, previewProperties[LegendProperty.TITLE_FONT_COLOR], data);

            col = r.addColumn();
            data = new Object[0];
            col.addElement(element.ELEMENT_TYPE.FONT, itemIndex, previewProperties[LegendProperty.TITLE_FONT], data);

            r = ipeditor.addRow();
            col = r.addColumn();
            // left-alignment
            data = new Object[4];
            data[0] = false;
            data[1] = "/org/gephi/legend/graphics/left_unselected.png";
            data[2] = "/org/gephi/legend/graphics/left_selected.png";
            data[3] = Alignment.LEFT;
            col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.TITLE_ALIGNMENT], data);

            // center alignment
            data = new Object[4];
            data[0] = true;
            data[1] = "/org/gephi/legend/graphics/center_unselected.png";
            data[2] = "/org/gephi/legend/graphics/center_selected.png";
            data[3] = Alignment.CENTER;
            col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.TITLE_ALIGNMENT], data);

            // right alignment
            data = new Object[4];
            data[0] = false;
            data[1] = "/org/gephi/legend/graphics/right_unselected.png";
            data[2] = "/org/gephi/legend/graphics/right_selected.png";
            data[3] = Alignment.RIGHT;
            col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.TITLE_ALIGNMENT], data);

            // justified
            data = new Object[4];
            data[0] = false;
            data[1] = "/org/gephi/legend/graphics/justified_unselected.png";
            data[2] = "/org/gephi/legend/graphics/justified_selected.png";
            data[3] = Alignment.JUSTIFIED;
            col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.TITLE_ALIGNMENT], data);

            titleNode.setInplaceEditor(ipeditor);
        }
    }
    
    private void renderDescription(Graphics2D graphics2D, AffineTransform origin, Integer width, Integer height) {
        graphics2D.setTransform(origin);
        legendDrawText(graphics2D, description, descriptionFont, descriptionFontColor, 0, 0, width, height, descriptionAlignment);
    }

    private void buildInplaceDesc(Graphics2D graphics2D, AffineTransform origin, blockNode descNode, Item item) {
        // the flow of this method is the same as the buildInplaceTitle method. 
        // The flow is repeated keeping in mind that there might be some special treatment give to description in the future.
        graphics2D.setTransform(origin);
        if (descNode.getInplaceEditor() == null) {
            Graph graph = null;
            inplaceItemBuilder ipbuilder = Lookup.getDefault().lookup(inplaceItemBuilder.class);
            inplaceEditor ipeditor = ipbuilder.createInplaceEditor(graph, descNode);
            ipeditor.setData(inplaceEditor.BLOCK_INPLACEEDITOR_GAP, (float) (TRANSFORMATION_ANCHOR_SIZE * 3.0 / 4.0));

            row r;
            column col;
            PreviewProperty[] previewProperties = item.getData(LegendItem.PROPERTIES);
            PreviewProperty prop;
            int itemIndex = item.getData(LegendItem.ITEM_INDEX);

            r = ipeditor.addRow();
            col = r.addColumn();
            Object[] data = new Object[1];
            data[0] = "Description: ";
            col.addElement(element.ELEMENT_TYPE.LABEL, itemIndex, null, data); //if its a label, property must be null.

            col = r.addColumn();
            data = new Object[0];
            col.addElement(element.ELEMENT_TYPE.TEXT, itemIndex, previewProperties[LegendProperty.DESCRIPTION], data);

            // we could have another property for title background.

            r = ipeditor.addRow();
            col = r.addColumn();
            data = new Object[0];
            col.addElement(element.ELEMENT_TYPE.COLOR, itemIndex, previewProperties[LegendProperty.DESCRIPTION_FONT_COLOR], data);

            col = r.addColumn();
            data = new Object[0];
            col.addElement(element.ELEMENT_TYPE.FONT, itemIndex, previewProperties[LegendProperty.DESCRIPTION_FONT], data);

            r = ipeditor.addRow();
            col = r.addColumn();
            // left-alignment
            data = new Object[4];
            data[0] = false;
            data[1] = "/org/gephi/legend/graphics/left_unselected.png";
            data[2] = "/org/gephi/legend/graphics/left_selected.png";
            data[3] = Alignment.LEFT;
            col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.DESCRIPTION_ALIGNMENT], data);

            // center alignment
            data = new Object[4];
            data[0] = true;
            data[1] = "/org/gephi/legend/graphics/center_unselected.png";
            data[2] = "/org/gephi/legend/graphics/center_selected.png";
            data[3] = Alignment.CENTER;
            col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.DESCRIPTION_ALIGNMENT], data);

            // right alignment
            data = new Object[4];
            data[0] = false;
            data[1] = "/org/gephi/legend/graphics/right_unselected.png";
            data[2] = "/org/gephi/legend/graphics/right_selected.png";
            data[3] = Alignment.RIGHT;
            col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.DESCRIPTION_ALIGNMENT], data);

            // justified
            data = new Object[4];
            data[0] = false;
            data[1] = "/org/gephi/legend/graphics/justified_unselected.png";
            data[2] = "/org/gephi/legend/graphics/justified_selected.png";
            data[3] = Alignment.JUSTIFIED;
            col.addElement(element.ELEMENT_TYPE.IMAGE, itemIndex, previewProperties[LegendProperty.DESCRIPTION_ALIGNMENT], data);

            descNode.setInplaceEditor(ipeditor);
        }
    }
    
    private int getFontWidth(Graphics2D graphics2d, String str) {
        return graphics2d.getFontMetrics().stringWidth(str);
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
//                System.out.println("@Var: y: "+y);
//                System.out.println("@Var: yText: " + (yText - y - layout.getAscent()));
//                System.out.println("@Var: height: " + height);
                if (yText - y - layout.getAscent() > height) {
                    break;
                }
//                    break;
                layout.draw(graphics2D, xText, yText);
            }
            descent = layout.getDescent();
            leading = layout.getLeading();
            yText += descent + leading;
        }

        // alignment, font, font color, isdisplaying

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
//        System.out.println("@Var: drawElementLabel: " + text);
        float spaceUsed = legendDrawText(graphics2D, text, font, color, x, y, width, height, alignment, true);
//        System.out.println("@Var: spaceUsed: " + spaceUsed);
        y = y + (height - spaceUsed) / 2;
        return legendDrawText(graphics2D, text, font, color, x, y, width, height, alignment, false);
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        Class renderer = item.getData(LegendItem.RENDERER);
        return renderer != null && renderer.equals(getClass());
    }

    @Override
    public boolean needsPreviewMouseListener(PreviewMouseListener previewMouseListener) {
        return previewMouseListener instanceof LegendMouseListener;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}