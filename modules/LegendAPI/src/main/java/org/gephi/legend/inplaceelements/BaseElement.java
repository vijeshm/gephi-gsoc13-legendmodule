package org.gephi.legend.inplaceelements;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Map;
import org.gephi.legend.inplaceeditor.Column;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.inplaceeditor.Row;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewProperty;

/**
 * Base class for all the types of elements in an inplace editor
 *
 * @author mvvijesh
 */
abstract public class BaseElement {

    public static String SELECTED_WITHIN_GROUP = "element.selected.within.group";
    public static String GROUP_PROPERTY_VALUE = "element.representative.value"; // indicates the value of the property that this particular item is representing

    public static enum ELEMENT_TYPE {

        CHECKBOX,
        COLOR,
        FILE,
        FONT,
        FUNCTION,
        IMAGE,
        LABEL,
        NUMBER,
        TEXT
    };
    protected Integer itemIndex; // index of the item that the element is associated with
    protected PreviewProperty property; // the preview property that the element will modify
    protected Map<String, Object> data;
    protected Integer numberOfBlocks; // the number of unit-blocks that the element would occupy when renderered
    protected ELEMENT_TYPE type; // type of the element
    protected InplaceEditor ipeditor; // the inplace editor associated with this element
    protected Column col; // the column that contains the element
    protected Row row; // the row that contains the column
    protected Boolean isGrouped; // indicates whether the element is grouped
    // these properties are set by the inPlaceRenderer when the the element is rendered.
    protected int originX;
    protected int originY;
    protected int elementWidth;
    protected int elementHeight;

    /**
     *
     * @param type - the type of element being added - CHECKBOX, COLOR, FILE,
     * FONT, FUNCTION, IMAGE, LABEL, NUMBER, TEXT
     * @param itemIndex - index of the item to which the inplace editor belongs
     * to
     * @param property - the preview property that the element will modify
     * @param ipeditor - the inplace editor associated with this element
     * @param row - the row that contains the column
     * @param col - the column that contains the element
     * @param data
     * @param isGrouped - indicates whether the element is grouped
     * @param isDefault - if the element is grouped, this value represents
     * whether it is the default
     * @param propertyValue - if the element is grouped, the value that this
     * element represents
     * @return the newly created base element
     */
    public BaseElement(ELEMENT_TYPE type, int itemIndex, PreviewProperty property, InplaceEditor ipeditor, Row row, Column col, Map<String, Object> data, Boolean isGrouped, Boolean isDefault, Object propertyValue) {
        this.ipeditor = ipeditor;
        this.row = row;
        this.col = col;
        this.type = type;
        this.isGrouped = isGrouped;
        this.itemIndex = itemIndex;
        this.property = property;
        this.data = data;

        // in case the element appears in a group, the value of the property that it represents must be set.
        if (isGrouped) {
            data.put(GROUP_PROPERTY_VALUE, propertyValue);
        } else {
            data.put(GROUP_PROPERTY_VALUE, null);
        }

        // in case an element is selected as default within the group, indicate that the default has been set in the column
        // do not allow selection for elements further
        if (isGrouped && isDefault && !col.isDefaultPicked()) {
            data.put(SELECTED_WITHIN_GROUP, true);
            col.setIsDefaultPicked(true);
        } else {
            data.put(SELECTED_WITHIN_GROUP, false);
        }
    }

    /**
     *
     * @param type - the type of element being added - CHECKBOX, COLOR, FILE,
     * FONT, FUNCTION, IMAGE, LABEL, NUMBER, TEXT
     * @param itemIndex - index of the item to which the inplace editor belongs
     * to
     * @param property - the preview property that the element will modify
     * @param ipeditor - the inplace editor associated with this element
     * @param row - the row that contains the column
     * @param col - the column that contains the element
     * @param data
     * @param isGrouped - indicates whether the element is grouped
     * @param isDefault - if the element is grouped, this value represents
     * whether it is the default
     * @param propertyValue - if the element is grouped, the value that this
     * element represents
     * @return newly created element of specified type
     */
    public static BaseElement createElement(ELEMENT_TYPE type, int itemIndex, PreviewProperty property, InplaceEditor ipeditor, Row row, Column col, Map<String, Object> data, Boolean isGrouped, Boolean isDefault, Object propertyValue) {
        BaseElement elem = null;
        switch (type) {
            case CHECKBOX:
                elem = new ElementCheckbox(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
                break;

            case COLOR:
                elem = new ElementColor(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
                break;

            case FILE:
                elem = new ElementFile(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
                break;

            case FONT:
                elem = new ElementFont(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
                break;

            case FUNCTION:
                elem = new ElementFunction(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
                break;

            case IMAGE:
                elem = new ElementImage(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
                break;

            case LABEL:
                elem = new ElementLabel(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
                break;

            case NUMBER:
                elem = new ElementNumber(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
                break;

            case TEXT:
                elem = new ElementText(type, itemIndex, property, ipeditor, row, col, data, isGrouped, isDefault, propertyValue);
                break;
        }

        return elem;
    }

    // only get methods, no set methods (except for geometry). this is because an element is concrete once it is built.
    public void setGeometry(int x, int y, int width, int height) {
        originX = x;
        originY = y;
        elementWidth = width;
        elementHeight = height;
    }

    public Boolean isGrouped() {
        return isGrouped;
    }

    public Row getRow() {
        return row;
    }

    public Column getColumn() {
        return col;
    }

    public InplaceEditor getInplaceEditor() {
        return ipeditor;
    }

    abstract public void renderElement(Graphics2D graphics2D, G2DTarget target, int currentUnitBlockSize, int editorOriginX, int editorOriginY, int borderSize, int rowBlock, int currentElementsCount);

    abstract public void onSelect();

    abstract public void computeNumberOfBlocks(Graphics2D graphics2d, G2DTarget target, int blockUnitSize);

    /**
     *
     * @param graphics2d - the graphics object for the target
     * @param str - the string whose width is to be computed
     * @return width of the string, under the current configuration of the
     * Graphics2D object
     */
    protected int getFontWidth(Graphics2D graphics2d, String str) {
        return graphics2d.getFontMetrics().stringWidth(str);
    }

    /**
     *
     * @param graphics2d - the graphics object for the target
     * @return height of the text, assuming that the rendering takes place on a
     * single line
     */
    protected int getFontHeight(Graphics2D graphics2d) {
        FontMetrics metric = graphics2d.getFontMetrics();
        return metric.getHeight() - metric.getDescent();
    }

    public PreviewProperty getProperty() {
        return property;
    }

    public int getNumberOfBlocks() {
        return numberOfBlocks;
    }

    public Integer getItemIndex() {
        return itemIndex;
    }

    public void setAssociatedData(String key, Object value) {
        data.put(key, value);
    }

    public Map<String, Object> getAssociatedData() {
        return data;
    }

    public Object getAssociatedData(String key) {
        return data.get(key);
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public int getElementWidth() {
        return elementWidth;
    }

    public int getElementHeight() {
        return elementHeight;
    }
}