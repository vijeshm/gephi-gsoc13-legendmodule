/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceeditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.BlockNode;
import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.spi.LegendItem;
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
    public static Color LABEL_COLOR = Color.BLACK;
    public static Color BORDER_COLOR = Color.BLACK;
    public static int BORDER_SIZE = 1;
    public static float COLOR_MARGIN = 0.2f;
    public static Map<Integer, String> fontLookup = new HashMap<Integer, String>();
    public static Color FONT_DISPLAY_COLOR = Color.BLACK;
    public static Color NUMBER_COLOR = Color.BLACK;

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
        PreviewProperties previewProperties = previewModel.getProperties();
        Font inplaceFont = previewProperties.getFontValue(PreviewProperty.INPLACE_DISPLAY_FONT);
        Font labelFont = new Font(inplaceFont.getName(), inplaceFont.getStyle(), inplaceFont.getSize());
        Font numberFont = new Font(inplaceFont.getName(), inplaceFont.getStyle(), inplaceFont.getSize());
        Font fontFont = new Font(inplaceFont.getName(), inplaceFont.getStyle(), inplaceFont.getSize());
        int blockUnitSize = previewProperties.getIntValue(PreviewProperty.INPLACE_BLOCK_UNIT_SIZE);
        int minBlockUnitSize = previewProperties.getIntValue(PreviewProperty.INPLACE_MIN_BLOCK_UNIT_SIZE);
        if(blockUnitSize < minBlockUnitSize) {
            blockUnitSize = minBlockUnitSize;
        }
        
        
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
            int editorWidth; // this will be set after rendering the elements, because its inefficient to traverse the rows, columns and elements twice.
            int editorHeight; // this will be set after rendering the elements, because its inefficient to traverse the rows, columns and elements twice.

            // preparation for rendering the inplace editor
            int rowBlock;
            int colBlock;
            int elemBlock;
            int maxElementsCount;
            boolean selected;
            ArrayList<Integer> elementsCount = new ArrayList<Integer>();
            ArrayList<Row> rows = ipeditor.getRows();

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

                        // temporary computational variablses
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

                                    graphics2d.setColor(BACKGROUND);
                                    graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                            blockUnitSize + 1,
                                            blockUnitSize + 1);

                                    graphics2d.drawImage(img,
                                            (editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                            (editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize + blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize + blockUnitSize,
                                            0,
                                            0,
                                            img.getWidth(),
                                            img.getHeight(), null);

                                    elem.setGeometry((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                            blockUnitSize,
                                            blockUnitSize);

                                    currentElementsCount += 1;
                                } catch (IOException e) {
                                }
                                break;

                            case COLOR:
                                Color color = prop.getValue();
                                graphics2d.setColor(BACKGROUND);
                                graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                        (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                        blockUnitSize + 1,
                                        blockUnitSize + 1);

                                graphics2d.setColor(color);
                                // some margin on all sides of the element
                                graphics2d.fillRect((int) ((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize + COLOR_MARGIN * blockUnitSize),
                                        (int) ((editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize + COLOR_MARGIN * blockUnitSize),
                                        (int) ((1 - 2 * COLOR_MARGIN) * blockUnitSize),
                                        (int) ((1 - 2 * COLOR_MARGIN) * blockUnitSize));

                                elem.setGeometry((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                        (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
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

                                    graphics2d.setColor(BACKGROUND);
                                    graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                            blockUnitSize + 1,
                                            blockUnitSize + 1);
                                    graphics2d.drawImage(img,
                                            (editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                            (editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize + blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize + blockUnitSize,
                                            0,
                                            0,
                                            img.getWidth(),
                                            img.getHeight(), null);

                                    elem.setGeometry((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
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

                                    graphics2d.setColor(BACKGROUND);
                                    graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                            blockUnitSize + 1,
                                            blockUnitSize + 1);
                                    graphics2d.drawImage(img,
                                            (editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                            (editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize + blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize + blockUnitSize,
                                            0,
                                            0,
                                            img.getWidth(),
                                            img.getHeight(), null);

                                    elem.setGeometry((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                            blockUnitSize,
                                            blockUnitSize);

                                    currentElementsCount += 1;
                                } catch (IOException e) {
                                }
                                break;

                            case FILE:
                                try {
                                    BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/org/gephi/legend/graphics/file.png"));
                                    graphics2d.setColor(BACKGROUND);
                                    graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                            blockUnitSize + 1,
                                            blockUnitSize + 1);
                                    graphics2d.drawImage(img,
                                            (editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                            (editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize + blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize + blockUnitSize,
                                            0,
                                            0,
                                            img.getWidth(),
                                            img.getHeight(), null);

                                    elem.setGeometry((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                            blockUnitSize,
                                            blockUnitSize);

                                    currentElementsCount += 1;
                                } catch (IOException e) {
                                }
                                break;

                            case FUNCTION:
                                try {
                                    BufferedImage img = ImageIO.read(getClass().getResourceAsStream((String) data[1]));
                                    graphics2d.setColor(BACKGROUND);
                                    graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                            blockUnitSize + 1,
                                            blockUnitSize + 1);
                                    graphics2d.drawImage(img,
                                            (editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                            (editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize + blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize + blockUnitSize,
                                            0,
                                            0,
                                            img.getWidth(),
                                            img.getHeight(), null);

                                    elem.setGeometry((editorOriginX + BORDER_SIZE) + currentElementsCount * blockUnitSize,
                                            (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                                            blockUnitSize,
                                            blockUnitSize);

                                    currentElementsCount += 1;
                                } catch (IOException e) {
                                }
                                break;
                        }
                    }
                }
                elementsCount.add(currentElementsCount);
            }

            maxElementsCount = Collections.max(elementsCount);
            // fill in the "white area" blocks
            graphics2d.setColor(BACKGROUND);
            for (rowBlock = 0; rowBlock < elementsCount.size(); rowBlock++) {
                int currentElementCount = elementsCount.get(rowBlock);
                graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementCount * blockUnitSize,
                        (editorOriginY + BORDER_SIZE) + rowBlock * blockUnitSize,
                        (maxElementsCount - currentElementCount) * blockUnitSize + 1,
                        blockUnitSize + 1);
            }

            editorWidth = maxElementsCount * blockUnitSize + 2 * BORDER_SIZE;
            editorHeight = rowBlock * blockUnitSize + 2 * BORDER_SIZE;

            // rendering borders
            graphics2d.setColor(BORDER_COLOR);
            // top
            graphics2d.fillRect(editorOriginX, editorOriginY, editorWidth, BORDER_SIZE);
            // right
            graphics2d.fillRect(editorOriginX + editorWidth - BORDER_SIZE, editorOriginY, BORDER_SIZE, editorHeight);
            // bottom
            graphics2d.fillRect(editorOriginX, editorOriginY + editorHeight - BORDER_SIZE, editorWidth, BORDER_SIZE);
            // left
            graphics2d.fillRect(editorOriginX, editorOriginY, BORDER_SIZE, editorHeight);

            // set back the saved states
            graphics2d.setFont(saveFontState);
            graphics2d.setColor(saveColorState);

            ipeditor.setData(InplaceEditor.ORIGIN_X, editorOriginX);
            ipeditor.setData(InplaceEditor.ORIGIN_Y, editorOriginY);
            ipeditor.setData(InplaceEditor.WIDTH, editorWidth);
            ipeditor.setData(InplaceEditor.HEIGHT, editorHeight);
        }
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