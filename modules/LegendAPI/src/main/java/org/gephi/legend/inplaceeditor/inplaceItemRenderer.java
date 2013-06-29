/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceeditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.blockNode;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItemBuilder;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.Item;
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
    @ServiceProvider(service = Renderer.class, position = 505),
    @ServiceProvider(service = inplaceItemRenderer.class, position = 505)
})
public class inplaceItemRenderer implements Renderer {

    private Color BACKGROUND = new Color(0.8f, 0.8f, 0.8f, 1f); // whatever color you choose, ensure that the opacity is 1. else, you'll get lines between elements due to overlapping of renderings.
    private Font LABEL_FONT = new Font("Arial", Font.PLAIN, 30);
    private Color LABEL_COLOR = Color.BLACK;
    private int BLOCK_SIZE = 50;
    private Color BORDER_COLOR = Color.BLACK;
    private int BORDER_SIZE = 1;
    private float COLOR_MARGIN = 0.2f;
    private Map<Integer, String> fontLookup = new HashMap<Integer, String>();
    private Font FONT_DISPLAY_FONT = new Font("Arial", Font.PLAIN, 10);
    private Color FONT_DISPLAY_COLOR = Color.BLACK;

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
        inplaceEditor ipeditor = legendModel.getInplaceEditor();

        // get the geometry of the block associated with the current inplace item.
        blockNode node = ipeditor.getData(inplaceEditor.BLOCKNODE);
        float blockOriginX = node.getOriginX();
        float blockOriginY = node.getOriginY();
        float blockWidth = node.getBlockWidth();
        float blockHeight = node.getBlockHeight();
        float gap = ipeditor.getData(inplaceEditor.BLOCK_INPLACEEDITOR_GAP);

        // save current states and restore it later
        Color saveColorState = graphics2d.getColor();
        Font saveFontState = graphics2d.getFont();

        // border cant be drawn now, since the height and width is unknown. Hence set the pseudo origin
        int editorOriginX = (int) (blockOriginX + blockWidth + gap);
        int editorOriginY = (int) (blockOriginY);
        int editorWidth; // this will be set after rendering the elements, because its inefficient to traverse the rows, columns and elements twice.
        int editorHeight; // this will be set after rendering the elements, because its inefficient to traverse the rows, columns and elements twice.

        // preparation for rendering the inplace editor
        int rowBlock;
        int colBlock;
        int elemBlock;
        int maxElementsCount;
        ArrayList<Integer> elementsCount = new ArrayList<Integer>();
        ArrayList<row> rows = ipeditor.getRows();
        
        // iterate through all the rows, corresponding columns and their corresponding elements. 
        // based on the type of element, render accordingly.
        // editorHeight is trivial. its just the block size times the number of rows.
        // along the way, keep a count on how many element blocks have been defined per row. This is needed to find out the editorWidth.
        // This info is also needed to fill in the "empty" areas later on.
        // while filling, add 1 to width and height, to avoid gaps between elements.
        for (rowBlock = 0; rowBlock < rows.size(); rowBlock++) {
            int currentElementsCount = 0;
            ArrayList<column> columns = rows.get(rowBlock).getColumns();
            for (colBlock = 0; colBlock < columns.size(); colBlock++) {
                ArrayList<element> elements = columns.get(colBlock).getElements();
                for (elemBlock = 0; elemBlock < elements.size(); elemBlock++) {
                    Object[] data = elements.get(elemBlock).getAssociatedData();
                    element elem = elements.get(elemBlock);
                    element.ELEMENT_TYPE type = elem.getElementType();
                    if (type == element.ELEMENT_TYPE.LABEL) {
                        graphics2d.setFont(LABEL_FONT);
                        int fontWidth = getFontWidth(graphics2d, (String) data[0]);
                        int fontHeight = getFontHeight(graphics2d);
                        int numberOfBlocks = fontWidth / BLOCK_SIZE + 1;

                        graphics2d.setColor(BACKGROUND);
                        graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementsCount * BLOCK_SIZE,
                                (editorOriginY + BORDER_SIZE) + rowBlock * BLOCK_SIZE,
                                BLOCK_SIZE * numberOfBlocks + 1,
                                BLOCK_SIZE + 1);

                        graphics2d.setColor(LABEL_COLOR);
                        graphics2d.drawString((String) data[0], (editorOriginX + BORDER_SIZE) + currentElementsCount * BLOCK_SIZE, (editorOriginY + BORDER_SIZE) + rowBlock * BLOCK_SIZE + BLOCK_SIZE / 2 + fontHeight / 2);

                        currentElementsCount += numberOfBlocks;
                    } else if (type == element.ELEMENT_TYPE.CHECKBOX) {
                        try {
                            PreviewProperty prop = elem.getProperty();
                            Boolean isDisplaying = prop.getValue();
                            BufferedImage img;
                            if (isDisplaying) {
                                img = ImageIO.read(getClass().getResourceAsStream("/org/gephi/legend/graphics/visible.png"));
                            } else {
                                img = ImageIO.read(getClass().getResourceAsStream("/org/gephi/legend/graphics/invisible.png"));
                            }

                            graphics2d.setColor(BACKGROUND);
                            graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementsCount * BLOCK_SIZE,
                                    (editorOriginY + BORDER_SIZE) + rowBlock * BLOCK_SIZE,
                                    BLOCK_SIZE + 1,
                                    BLOCK_SIZE + 1);
                            graphics2d.drawImage(img,
                                    (editorOriginX + BORDER_SIZE) + currentElementsCount * BLOCK_SIZE,
                                    (editorOriginY + BORDER_SIZE) + rowBlock * BLOCK_SIZE,
                                    (editorOriginX + BORDER_SIZE) + currentElementsCount * BLOCK_SIZE + BLOCK_SIZE,
                                    (editorOriginY + BORDER_SIZE) + rowBlock * BLOCK_SIZE + BLOCK_SIZE,
                                    0,
                                    0,
                                    img.getWidth(),
                                    img.getHeight(), null);
                            currentElementsCount += 1;
                        } catch (IOException e) {
                        }
                    } else if (type == element.ELEMENT_TYPE.COLOR) {
                        PreviewProperty prop = elem.getProperty();
                        Color color = prop.getValue();
                        graphics2d.setColor(BACKGROUND);
                        graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementsCount * BLOCK_SIZE,
                                (editorOriginY + BORDER_SIZE) + rowBlock * BLOCK_SIZE,
                                BLOCK_SIZE + 1,
                                BLOCK_SIZE + 1);

                        graphics2d.setColor(color);
                        // some margin on all sides of the element
                        graphics2d.fillRect((int) ((editorOriginX + BORDER_SIZE) + currentElementsCount * BLOCK_SIZE + COLOR_MARGIN * BLOCK_SIZE),
                                (int) ((editorOriginY + BORDER_SIZE) + rowBlock * BLOCK_SIZE + COLOR_MARGIN * BLOCK_SIZE),
                                (int) ((1 - 2 * COLOR_MARGIN) * BLOCK_SIZE),
                                (int) ((1 - 2 * COLOR_MARGIN) * BLOCK_SIZE));
                        currentElementsCount += 1;
                    }
                    else if(type == element.ELEMENT_TYPE.FONT)  {
                        PreviewProperty prop = elem.getProperty();
                        Font font = prop.getValue();
                        
                        String displayString = font.getFontName() + " " + font.getSize() + " " + fontLookup.get(font.getStyle());
                        graphics2d.setFont(FONT_DISPLAY_FONT);
                        int fontWidth = getFontWidth(graphics2d, displayString);
                        int fontHeight = getFontHeight(graphics2d);
                        int numberOfBlocks = fontWidth / BLOCK_SIZE + 1;

                        graphics2d.setColor(BACKGROUND);
                        graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementsCount * BLOCK_SIZE,
                                (editorOriginY + BORDER_SIZE) + rowBlock * BLOCK_SIZE,
                                BLOCK_SIZE * numberOfBlocks + 1,
                                BLOCK_SIZE + 1);

                        graphics2d.setColor(FONT_DISPLAY_COLOR);
                        graphics2d.drawString(displayString, (editorOriginX + BORDER_SIZE) + currentElementsCount * BLOCK_SIZE, (editorOriginY + BORDER_SIZE) + rowBlock * BLOCK_SIZE + BLOCK_SIZE / 2 + fontHeight / 2);

                        currentElementsCount += numberOfBlocks;
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
            graphics2d.fillRect((editorOriginX + BORDER_SIZE) + currentElementCount * BLOCK_SIZE,
                    (editorOriginY + BORDER_SIZE) + rowBlock * BLOCK_SIZE,
                    (maxElementsCount - currentElementCount) * BLOCK_SIZE + 1,
                    BLOCK_SIZE + 1);
        }

        editorWidth = maxElementsCount * BLOCK_SIZE + 2 * BORDER_SIZE;
        editorHeight = rowBlock * BLOCK_SIZE + 2 * BORDER_SIZE;

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

        ipeditor.setData(inplaceEditor.ORIGIN_X, editorOriginX);
        ipeditor.setData(inplaceEditor.ORIGIN_Y, editorOriginY);
        ipeditor.setData(inplaceEditor.WIDTH, editorWidth);
        ipeditor.setData(inplaceEditor.HEIGHT, editorHeight);
    }

    private int getFontWidth(Graphics2D graphics2d, String str) {
        return graphics2d.getFontMetrics().stringWidth(str);
    }

    private int getFontHeight(Graphics2D graphics2d) {
        return graphics2d.getFontMetrics().getHeight();
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        Class renderer = item.getData(inplaceEditor.RENDERER);
        return renderer != null && renderer.equals(getClass());
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof inplaceItemBuilder;
    }
}
