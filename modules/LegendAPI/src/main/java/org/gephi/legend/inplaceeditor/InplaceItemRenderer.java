/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceeditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.BlockNode;
import static org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.CHECKBOX;
import static org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.COLOR;
import static org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.FILE;
import static org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.FONT;
import static org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.FUNCTION;
import static org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.IMAGE;
import static org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.LABEL;
import static org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.NUMBER;
import static org.gephi.legend.inplaceeditor.Element.ELEMENT_TYPE.TEXT;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author mvvijesh
 */
@ServiceProviders(value = {
    @ServiceProvider(service = Renderer.class, position = 506),
    @ServiceProvider(service = InplaceItemRenderer.class, position = 506)
})
public class InplaceItemRenderer implements Renderer {

    // all these values should be made as final. They are temporarily not final because of debugging purposes
    public static Color BACKGROUND = new Color(0.8f, 0.8f, 0.8f, 1f); // whatever color you choose, ensure that the opacity is 1. else, you'll get lines between elements due to overlapping of renderings.
    public static Font DEFAULT_LABEL_FONT = new Font("Arial", Font.PLAIN, 30);
    public static Color LABEL_COLOR = Color.BLACK;
    public static Color BORDER_COLOR = Color.BLACK;
    public static int BORDER_SIZE = 1;
    public static float COLOR_MARGIN = 0.2f;
    public static Map<Integer, String> fontLookup = new HashMap<Integer, String>();
    public static Font DEFAULT_FONT_DISPLAY_FONT = new Font("Arial", Font.PLAIN, 20);
    public static Color FONT_DISPLAY_COLOR = Color.BLACK;
    public static Font DEFAULT_NUMBER_FONT = new Font("Arial", Font.PLAIN, 20);
    public static Color NUMBER_COLOR = Color.BLACK;
    public static int DEFAULT_INPLACE_BLOCK_UNIT_SIZE = 50;
    public static int INPLACE_MIN_BLOCK_UNIT_SIZE = 5;
    public static Font INPLACE_DEFAULT_DISPLAY_FONT = new Font("Arial", Font.PLAIN, 32);

    @Override
    public String getDisplayName() {
        return "inplace renderer";
    }

    @Override
    public void preProcess(PreviewModel previewModel) {
        fontLookup.put(Font.BOLD, "Bold");
        fontLookup.put(Font.CENTER_BASELINE, "Center Baseline");
        fontLookup.put(Font.HANGING_BASELINE, "Hanging Baseline");
        fontLookup.put(Font.ITALIC, "Italic");
        fontLookup.put(Font.LAYOUT_LEFT_TO_RIGHT, "Layout Left to Right");
        fontLookup.put(Font.LAYOUT_NO_LIMIT_CONTEXT, "Layout No Limit Context");
        fontLookup.put(Font.LAYOUT_NO_START_CONTEXT, "Layout No Start Context");
        fontLookup.put(Font.LAYOUT_RIGHT_TO_LEFT, "Layout Right to Left");
        fontLookup.put(Font.PLAIN, "Plain");
        fontLookup.put(Font.ROMAN_BASELINE, "Roman Baseline");
        fontLookup.put(Font.TRUETYPE_FONT, "True Type Font");
        fontLookup.put(Font.TYPE1_FONT, "Type1 Font");
    }

    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {
        G2DTarget g2dtarget = (G2DTarget) target;
        Graphics2D graphics2d = g2dtarget.getGraphics();

        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        InplaceEditor ipeditor = legendModel.getInplaceEditor();

        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();

        float scalingFactor = 1;
        if (properties.hasProperty(PreviewProperty.ZOOM_LEVEL)) {
            scalingFactor = properties.getFloatValue(PreviewProperty.ZOOM_LEVEL);
        }

        //Calculate block and border size taking zoom factor into account.
        //We want to make in place editors size independent of zoom.
        float blockUnitSizeFloat = ((float) DEFAULT_INPLACE_BLOCK_UNIT_SIZE / scalingFactor);
        int blockUnitSize = (int) blockUnitSizeFloat;
        if (blockUnitSize < INPLACE_MIN_BLOCK_UNIT_SIZE) {
            blockUnitSizeFloat = INPLACE_MIN_BLOCK_UNIT_SIZE;
            blockUnitSize = INPLACE_MIN_BLOCK_UNIT_SIZE;
        }

        float borderSizeFloat = ((float) BORDER_SIZE / scalingFactor);
        int borderSize = (int) borderSizeFloat;
        if (borderSize <= 0) {
            borderSize = 1;//At least 1 pixel
        }


        // change the block size and font size only if the scaled down size is still larger than the miniumum
        Font inplaceFont = INPLACE_DEFAULT_DISPLAY_FONT;
        //if (blockUnitSizeFloat > INPLACE_MIN_BLOCK_UNIT_SIZE) {//Commented out, otherwise it doesn't work well with high zoom
            inplaceFont = inplaceFont.deriveFont((float) inplaceFont.getSize() / scalingFactor);
        //}

        Font labelFont = new Font(inplaceFont.getName(), inplaceFont.getStyle(), inplaceFont.getSize());
        Font numberFont = new Font(inplaceFont.getName(), inplaceFont.getStyle(), inplaceFont.getSize());
        Font fontFont = new Font(inplaceFont.getName(), inplaceFont.getStyle(), inplaceFont.getSize());

        if (ipeditor != null) {
            // get the geometry of the block associated with the current inplace item.
            BlockNode node = ipeditor.getData(InplaceEditor.BLOCKNODE);
            float blockOriginX = node.getOriginX();
            float blockOriginY = node.getOriginY();
            float blockWidth = node.getBlockWidth();
            float blockHeight = node.getBlockHeight();

            // save current states and restore it later
            Color saveColorState = graphics2d.getColor();
            Font saveFontState = graphics2d.getFont();

            // border cant be drawn now, since the height and width is unknown. Hence set the pseudo origin
            int editorOriginX = (Integer) ipeditor.getData(InplaceEditor.ORIGIN_X); // (blockOriginX + blockWidth + gap);
            int editorOriginY = (Integer) ipeditor.getData(InplaceEditor.ORIGIN_Y); // (blockOriginY);
            float editorWidth; // this will be set after rendering the elements, because its inefficient to traverse the rows, columns and elements twice.
            float editorHeight; // this will be set after rendering the elements, because its inefficient to traverse the rows, columns and elements twice.

            // preparation for rendering the inplace editor
            int rowBlock;
            int colBlock;
            int elemBlock;
            int maxElementsCount;
            boolean selected;
            ArrayList<Row> rows = ipeditor.getRows();


            ////////////////////
            ////////////////////
            //NOTE for refactor:
            //Switch case needs to be completely removed.
            //Elements/editors need to be subclassed in order to generally and, in a simple way, provide all data we need here for rendering them.
            //Do not use Object array for associated data. This leads to hardcoded positions, use a Map<String, Object> instead.
            //For numbers/strings to access this data, always create a constant, avoid copying values all over the code.

            //With a first analysis this will be probably needed:
            //A render() method for elements that abstracts the drawing. Initially set the drawing origin for it if possible.
            //Since In place editors keep same size with a lot of zoom, we need to use float precision for all inPlace editor rendering where possible.
            //Elements should provide the desiredNumberOfBlocks field with a method or through the data Map. Since this is common to all elements, it can be directly a method
            ////////////////////
            ////////////////////


            //First iterate elements for precalculating editor elements and size so we can draw background:
            maxElementsCount = 0;
            for (Row row : rows) {
                int currentElementsCount = 0;
                for (Column column : row.getColumns()) {
                    for (Element elem : column.getElements()) {
                        //!!Read refactoring notes up for reducing this to one line (elem.NumberOfBlocks):
                        Object[] data = elem.getAssociatedData();
                        Element.ELEMENT_TYPE type = elem.getElementType();
                        PreviewProperty prop = elem.getProperty();
                        
                        int fontWidth;
                        int numberOfBlocks;
                        String displayString;
                        switch (type) {
                            case LABEL:
                                graphics2d.setFont(labelFont);
                                fontWidth = getFontWidth(graphics2d, (String) data[0]);
                                numberOfBlocks = fontWidth / blockUnitSize + 1;
                                break;
                            case FONT:
                                Font font = prop.getValue();
                                graphics2d.setFont(fontFont);
                                displayString = font.getFontName() + " " + font.getSize();
                                fontWidth = getFontWidth(graphics2d, displayString);
                                numberOfBlocks = fontWidth / blockUnitSize + 1;
                                break;
                            case NUMBER:
                                graphics2d.setFont(numberFont);
                                displayString = "" + prop.getValue();
                                fontWidth = getFontWidth(graphics2d, (String) displayString);
                                numberOfBlocks = fontWidth / blockUnitSize + 1;
                                break;
                            default:
                                numberOfBlocks = 1;
                        }
                        currentElementsCount += numberOfBlocks;
                    }

                }

                maxElementsCount = Math.max(maxElementsCount, currentElementsCount);
            }

            editorWidth = maxElementsCount * blockUnitSize + 2 * borderSizeFloat;
            editorHeight = rows.size() * blockUnitSize + 2 * borderSizeFloat;

            //Fill the background at once for the whole editor
            graphics2d.setColor(BACKGROUND);
            fillRect(graphics2d, editorOriginX, editorOriginY, editorWidth, editorHeight);


            // iterate through all the rows, corresponding columns and their corresponding elements. 
            // based on the type of element, render accordingly.
            // editorHeight is trivial. its just the block size times the number of rows.
            // along the way, keep a count on how many element blocks have been defined per row. This is needed to find out the editorWidth.
            // This info is also needed to fill in the "empty" areas later on.
            // while filling, add 1 to width and height, to avoid gaps between elements.
            for (rowBlock = 0; rowBlock < rows.size(); rowBlock++) {
                int currentElementsCount = 0;
                ArrayList<Column> columns = rows.get(rowBlock).getColumns();
                for (colBlock = 0; colBlock < columns.size(); colBlock++) {
                    ArrayList<Element> elements = columns.get(colBlock).getElements();
                    selected = false;
                    for (elemBlock = 0; elemBlock < elements.size(); elemBlock++) {
                        Element elem = elements.get(elemBlock);
                        Object[] data = elem.getAssociatedData();
                        Element.ELEMENT_TYPE type = elem.getElementType();
                        PreviewProperty prop = elem.getProperty();

                        // temporary computational variables
                        int fontWidth;
                        int fontHeight;
                        int numberOfBlocks;
                        String displayString;
                        /*
                         float usableFraction = 0.8f;
                         int stepSize = 1;
                         float fontSize; // reset to zero in every case clause
                         int desiredNumberOfBlocks;
                         Font tempFont;
                         */

                        switch (type) {
                            case LABEL:
                                graphics2d.setFont(labelFont);
                                fontWidth = getFontWidth(graphics2d, (String) data[0]);
                                fontHeight = getFontHeight(graphics2d);
                                numberOfBlocks = fontWidth / blockUnitSize + 1;

                                graphics2d.setColor(BACKGROUND);
                                graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                        (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                        blockUnitSize * numberOfBlocks + 1,
                                        blockUnitSize + 1);

                                graphics2d.setColor(LABEL_COLOR);
                                graphics2d.drawString((String) data[0],
                                        (editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                        (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize + blockUnitSize / 2 + fontHeight / 2);

                                elem.setGeometry((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                        (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                        blockUnitSize * numberOfBlocks,
                                        blockUnitSize);

                                currentElementsCount += numberOfBlocks;
                                break;
                            case CHECKBOX:
                                try {
                                    BufferedImage img;
                                    if ((Boolean) data[0]) {
                                        img = ImageIO.read(getClass().getResourceAsStream("/org/gephi/legend/graphics/checked.png"));
                                    } else {
                                        img = ImageIO.read(getClass().getResourceAsStream("/org/gephi/legend/graphics/unchecked.png"));
                                    }

                                    graphics2d.drawImage(img,
                                            (editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                                            (editorOriginX + borderSize) + currentElementsCount * blockUnitSize + blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize + blockUnitSize,
                                            0,
                                            0,
                                            img.getWidth(),
                                            img.getHeight(), null);

                                    elem.setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                                            blockUnitSize,
                                            blockUnitSize);

                                    currentElementsCount += 1;
                                } catch (IOException e) {
                                }
                                break;
                            case COLOR:
                                Color color = prop.getValue();

                                graphics2d.setColor(color);
                                // some margin on all sides of the element
                                graphics2d.fillRect((int) ((editorOriginX + borderSize) + currentElementsCount * blockUnitSize + COLOR_MARGIN * blockUnitSize),
                                        (int) ((editorOriginY + borderSize) + rowBlock * blockUnitSize + COLOR_MARGIN * blockUnitSize),
                                        (int) ((1 - 2 * COLOR_MARGIN) * blockUnitSize),
                                        (int) ((1 - 2 * COLOR_MARGIN) * blockUnitSize));

                                elem.setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                                        (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                                        blockUnitSize,
                                        blockUnitSize);

                                currentElementsCount += 1;
                                break;
                            case FONT:
                                Font font = prop.getValue();
                                graphics2d.setFont(fontFont);
                                displayString = font.getFontName() + " " + font.getSize();
                                fontWidth = getFontWidth(graphics2d, displayString);
                                fontHeight = getFontHeight(graphics2d);
                                numberOfBlocks = fontWidth / blockUnitSize + 1;

                                graphics2d.setColor(BACKGROUND);
                                graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                        (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                        blockUnitSize * numberOfBlocks + 1,
                                        blockUnitSize + 1);

                                graphics2d.setColor(FONT_DISPLAY_COLOR);
                                graphics2d.drawString(displayString,
                                        (editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize + numberOfBlocks * blockUnitSize / 2 - fontWidth / 2,
                                        (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize + blockUnitSize / 2 + fontHeight / 2);

                                elem.setGeometry((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                        (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                        blockUnitSize * numberOfBlocks,
                                        blockUnitSize);

                                currentElementsCount += numberOfBlocks;
                                break;
                            case IMAGE:
                                try {
                                    // load the default image (unselected)
                                    BufferedImage img = ImageIO.read(getClass().getResourceAsStream((String) data[1]));
                                    // if atleast one element is selected, the first one is taken into consideration
                                    // if no elements are selected, the last one is NOT forcibly selected
                                    if (!selected && (Boolean) data[0]) {
                                        img = ImageIO.read(getClass().getResourceAsStream((String) data[2]));
                                        selected = true;
                                    }

                                    graphics2d.drawImage(img,
                                            (editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                                            (editorOriginX + borderSize) + currentElementsCount * blockUnitSize + blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize + blockUnitSize,
                                            0,
                                            0,
                                            img.getWidth(),
                                            img.getHeight(), null);

                                    elem.setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                                            blockUnitSize,
                                            blockUnitSize);

                                    currentElementsCount += 1;
                                } catch (IOException e) {
                                }
                                break;
                            case NUMBER:
                                graphics2d.setFont(numberFont);
                                displayString = "" + prop.getValue();
                                fontWidth = getFontWidth(graphics2d, (String) displayString);
                                fontHeight = getFontHeight(graphics2d);
                                numberOfBlocks = fontWidth / blockUnitSize + 1;

                                graphics2d.setColor(BACKGROUND);
                                graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                        (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                        blockUnitSize * numberOfBlocks + 1,
                                        blockUnitSize + 1);

                                graphics2d.setColor(NUMBER_COLOR);
                                graphics2d.drawString(displayString,
                                        (editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize + numberOfBlocks * blockUnitSize / 2 - fontWidth / 2,
                                        (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize + blockUnitSize / 2 + fontHeight / 2);

                                elem.setGeometry((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                        (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                        blockUnitSize * numberOfBlocks,
                                        blockUnitSize);

                                currentElementsCount += numberOfBlocks;
                                break;
                            case TEXT:
                                try {
                                    BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/org/gephi/legend/graphics/edit.png"));

                                    graphics2d.drawImage(img,
                                            (editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                                            (editorOriginX + borderSize) + currentElementsCount * blockUnitSize + blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize + blockUnitSize,
                                            0,
                                            0,
                                            img.getWidth(),
                                            img.getHeight(), null);

                                    elem.setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                                            blockUnitSize,
                                            blockUnitSize);

                                    currentElementsCount += 1;
                                } catch (IOException e) {
                                }
                                break;
                            case FILE:
                                try {
                                    BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/org/gephi/legend/graphics/file.png"));
                                    graphics2d.drawImage(img,
                                            (editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                                            (editorOriginX + borderSize) + currentElementsCount * blockUnitSize + blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize + blockUnitSize,
                                            0,
                                            0,
                                            img.getWidth(),
                                            img.getHeight(), null);

                                    elem.setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                                            blockUnitSize,
                                            blockUnitSize);

                                    currentElementsCount += 1;
                                } catch (IOException e) {
                                }
                                break;

                            case FUNCTION:
                                try {
                                    BufferedImage img = ImageIO.read(getClass().getResourceAsStream((String) data[1]));
                                    graphics2d.drawImage(img,
                                            (editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                                            (editorOriginX + borderSize) + currentElementsCount * blockUnitSize + blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize + blockUnitSize,
                                            0,
                                            0,
                                            img.getWidth(),
                                            img.getHeight(), null);

                                    elem.setGeometry((editorOriginX + borderSize) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + borderSize) + rowBlock * blockUnitSize,
                                            blockUnitSize,
                                            blockUnitSize);

                                    currentElementsCount += 1;
                                } catch (IOException e) {
                                }
                                break;
                        }
                    }
                }
            }





            // rendering borders
            graphics2d.setColor(BORDER_COLOR);
            // top
            fillRect(graphics2d, editorOriginX, editorOriginY, editorWidth, borderSizeFloat);
            // right
            fillRect(graphics2d, editorOriginX + editorWidth - borderSizeFloat, editorOriginY, borderSizeFloat, editorHeight);
            // bottom
            fillRect(graphics2d, editorOriginX, editorOriginY + editorHeight - borderSizeFloat, editorWidth, borderSizeFloat);
            // left
            fillRect(graphics2d, editorOriginX, editorOriginY, borderSizeFloat, editorHeight);

            // set back the saved states
            graphics2d.setFont(saveFontState);
            graphics2d.setColor(saveColorState);

            ipeditor.setData(InplaceEditor.ORIGIN_X, editorOriginX);
            ipeditor.setData(InplaceEditor.ORIGIN_Y, editorOriginY);
            ipeditor.setData(InplaceEditor.WIDTH, editorWidth);
            ipeditor.setData(InplaceEditor.HEIGHT, editorHeight);
        }
    }

    /**
     * Helper method to fill rectangles with float precision. We use float precision specially for inPlace editor since they are kept the same size in screen independently of zoom factor.
     */
    private void fillRect(Graphics2D graphics2d, float x, float y, float width, float height) {
        Rectangle2D.Float rect = new Rectangle2D.Float(x, y, width, height);
        graphics2d.fill(rect);
    }

    private int getFontWidth(Graphics2D graphics2d, String str) {
        return graphics2d.getFontMetrics().stringWidth(str);
    }

    private int getFontHeight(Graphics2D graphics2d) {
        FontMetrics metric = graphics2d.getFontMetrics();
        return metric.getHeight() - metric.getDescent();
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        Class renderer = item.getData(InplaceEditor.RENDERER);
        return renderer != null && renderer.equals(getClass());
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof InplaceItemBuilder;
    }
}