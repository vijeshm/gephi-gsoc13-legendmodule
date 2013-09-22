package org.gephi.legend.inplaceeditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.inplaceelements.BaseElement;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.Renderer;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * a renderer class for inplace editor items.
 *
 * This class is exposed as a service. It is used to render the inplace editors.
 * The layout for inplace editors is split into blocks of unit size. This block
 * of unit size is called as a unit-block. The size of the unit block is defined
 * by DEFAULT_INPLACE_BLOCK_UNIT_SIZE member. The inplace editor item organizes
 * the editor as a set of rows, columns and elements. A row contains a set of
 * columns, and a column contains a set of elements. The rows need not contain
 * equal number of columns, and the columns need not contain equal number of
 * elements. The elements declare the number of blocks that they require for
 * them to be rendered. Although this can be computed on-the-fly during
 * rendering, we use this approach to make the procedure clearer and avoid
 * having a 2-pass technique.
 *
 * @author mvvijesh
 */
@ServiceProviders(value = {
    @ServiceProvider(service = Renderer.class, position = 700),
    @ServiceProvider(service = InplaceItemRenderer.class)
})
public class InplaceItemRenderer implements Renderer {

    public static final Color BACKGROUND = new Color(0.8f, 0.8f, 0.8f, 1f); // whatever color you choose, ensure that the opacity is 1. else, you'll get lines between elements due to overlapping of renderings.
    public static final Color LABEL_COLOR = Color.BLACK;
    public static final Color BORDER_COLOR = Color.BLACK;
    public static final int BORDER_SIZE = 1;
    public static final float COLOR_MARGIN = 0.2f;
    public static final Map<Integer, String> fontLookup = new HashMap<Integer, String>();
    public static final Color FONT_DISPLAY_COLOR = Color.BLACK;
    public static final Color NUMBER_COLOR = Color.BLACK;
    public static final int DEFAULT_INPLACE_BLOCK_UNIT_SIZE = 25;
    public static final int INPLACE_MIN_BLOCK_UNIT_SIZE = 5;
    public static final Font INPLACE_DEFAULT_DISPLAY_FONT = new Font("Arial", Font.PLAIN, 16);

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

    /**
     * determines if the current render is for an export.
     *
     * Used for not drawing in-place editors.
     *
     * @param target Rendering target
     * @return
     */
    private boolean isExport(PreviewProperties properties) {
        return properties.hasProperty(PreviewProperty.IS_EXPORT) && properties.getBooleanValue(PreviewProperty.IS_EXPORT);
    }

    /**
     * method to render the inplace editor.
     *
     * @param item - the inplace item to the rendered
     * @param target - the target onto which the inplace item must be rendered
     * @param properties - preview model's preview properties
     */
    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {
        // if the item is being exported, dont render the inplace editors
        if (!isExport(properties)) {
            // get the currently active inplace editor
            LegendController legendController = LegendController.getInstance();
            LegendModel legendModel = legendController.getLegendModel();
            InplaceEditor ipeditor = legendModel.getInplaceEditor();

            // when the user clicks outside all of the items or drags an item, the inplace editor will be set to null.
            if (ipeditor != null) {
                G2DTarget g2dtarget = (G2DTarget) target;
                Graphics2D graphics2d = g2dtarget.getGraphics();

                float zoomLevel = 1.0f;
                if (properties.hasProperty(PreviewProperty.ZOOM_LEVEL)) {
                    zoomLevel = properties.getFloatValue(PreviewProperty.ZOOM_LEVEL);
                }

                //Calculate block and border size taking zoom factor into account.
                //We want to make in place editors size independent of zoom.
                float blockUnitSizeFloat = ((float) DEFAULT_INPLACE_BLOCK_UNIT_SIZE / zoomLevel);
                int blockUnitSize = (int) blockUnitSizeFloat;
                if (blockUnitSize < INPLACE_MIN_BLOCK_UNIT_SIZE) {
                    blockUnitSize = INPLACE_MIN_BLOCK_UNIT_SIZE;
                    blockUnitSizeFloat = INPLACE_MIN_BLOCK_UNIT_SIZE;
                }

                float borderSizeFloat = ((float) BORDER_SIZE / zoomLevel);
                int borderSize = (int) borderSizeFloat;
                if (borderSize <= 0) {
                    borderSize = 1; // at least 1 pixel
                }

                // save current states and restore it later
                Color saveColorState = graphics2d.getColor();
                Font saveFontState = graphics2d.getFont();

                // preparation for rendering the inplace editor
                int rowBlock;
                int colBlock;
                int elemBlock;
                int maxElementsCount;
                ArrayList<Row> rows = ipeditor.getRows();

                // iterate elements for precalculating editor elements and size and draw the background
                maxElementsCount = 0;
                for (Row row : rows) {
                    int currentElementsCount = 0;
                    for (Column column : row.getColumns()) {
                        for (BaseElement elem : column.getElements()) {
                            // for the current target and font scenarios, compute the number of blocks and get them.
                            elem.computeNumberOfBlocks(graphics2d, g2dtarget, blockUnitSize);
                            currentElementsCount += elem.getNumberOfBlocks();
                        }
                    }
                    maxElementsCount = Math.max(maxElementsCount, currentElementsCount);
                }

                // determine the dimensions of the inplace editor
                int editorOriginX = (Integer) ipeditor.getData(InplaceEditor.ORIGIN_X);
                int editorOriginY = (Integer) ipeditor.getData(InplaceEditor.ORIGIN_Y);
                float editorWidth = maxElementsCount * blockUnitSize + 2 * borderSize;
                float editorHeight = rows.size() * blockUnitSize + 2 * borderSize;

                //Fill the background at once for the whole editor
                graphics2d.setColor(BACKGROUND);
                fillRect(graphics2d, editorOriginX, editorOriginY, editorWidth, editorHeight);

                // iterate through all the rows, corresponding columns and their corresponding elements. 
                // based on the type of element, render accordingly.
                // editorHeight is trivial. its just the block size times the number of rows.
                for (rowBlock = 0; rowBlock < rows.size(); rowBlock++) {
                    int currentElementsCount = 0;
                    ArrayList<Column> columns = rows.get(rowBlock).getColumns();
                    for (colBlock = 0; colBlock < columns.size(); colBlock++) {
                        ArrayList<BaseElement> elements = columns.get(colBlock).getElements();
                        for (elemBlock = 0; elemBlock < elements.size(); elemBlock++) {
                            BaseElement elem = elements.get(elemBlock);
                            elem.renderElement(graphics2d, (G2DTarget) target, blockUnitSize, editorOriginX, editorOriginY, borderSize, rowBlock, currentElementsCount);
                            currentElementsCount += elem.getNumberOfBlocks();
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

                // populate the inplace editor object with the dimensions info
                ipeditor.setData(InplaceEditor.ORIGIN_X, editorOriginX);
                ipeditor.setData(InplaceEditor.ORIGIN_Y, editorOriginY);
                ipeditor.setData(InplaceEditor.WIDTH, editorWidth);
                ipeditor.setData(InplaceEditor.HEIGHT, editorHeight);
            }
        }
    }

    /**
     * Helper method to fill rectangles with float precision.
     *
     * We use float precision specially for inPlace editor since they are kept
     * the same size in screen independently of zoom factor.
     *
     * @param graphics2d - a graphics object used for rendering
     * @param x - x-coordinate of the rectangle
     * @param y - y-coordinate of the rectangle
     * @param width - width of the rectangle
     * @param height - height of the rectangle
     */
    private void fillRect(Graphics2D graphics2d, float x, float y, float width, float height) {
        Rectangle2D.Float rect = new Rectangle2D.Float(x, y, width, height);
        graphics2d.fill(rect);
    }

    /**
     *
     * @param graphics2d - the graphics object for the target
     * @param str - the string whose width is to be computed
     * @return width of the string, under the current configuration of the
     * Graphics2D object
     */
    private int getFontWidth(Graphics2D graphics2d, String str) {
        return graphics2d.getFontMetrics().stringWidth(str);
    }

    /**
     *
     * @param graphics2d - the graphics object for the target
     * @return height of the text, assuming that the rendering takes place on a
     * single line
     */
    private int getFontHeight(Graphics2D graphics2d) {
        FontMetrics metric = graphics2d.getFontMetrics();
        return metric.getHeight() - metric.getDescent();
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }

    /**
     *
     * @param item - the item being check against the renderer
     * @param properties - preview properties of the preview model
     * @return True if the item can be rendered using this renderer, False
     * otherwise
     */
    @Override
    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        // each item knows which renderer can render it
        Class renderer = item.getData(InplaceEditor.RENDERER);
        return renderer != null && renderer.equals(getClass());
    }

    /**
     *
     * @param itemBuilder - the custom item builder being checked against
     * @param properties - preview properties of the preview model
     * @return True if the custom item builder can be built with
     * InplaceItemBuilder
     */
    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof InplaceItemBuilder;
    }
}