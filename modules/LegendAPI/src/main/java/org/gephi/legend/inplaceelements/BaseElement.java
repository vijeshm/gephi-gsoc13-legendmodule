/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
    protected Integer itemIndex;
    protected PreviewProperty property;
    protected Map<String, Object> data;
    protected Integer numberOfBlocks;
    protected ELEMENT_TYPE type;
    protected InplaceEditor ipeditor;
    protected Row row;
    protected Column col;
    protected Boolean isGrouped;
    // these properties are set by the inPlaceRenderer when the the element is rendered.
    protected int originX;
    protected int originY;
    protected int elementWidth;
    protected int elementHeight;

    public BaseElement(ELEMENT_TYPE type, int itemIndex, PreviewProperty property, InplaceEditor ipeditor, Row row, Column col, Map<String, Object> data, Boolean isGrouped, Boolean isDefault, Object propertyValue) {
        this.ipeditor = ipeditor;
        this.row = row;
        this.col = col;
        this.type = type;
        this.isGrouped = isGrouped;
        this.itemIndex = itemIndex;
        this.property = property;
        this.data = data;
        
        // in case elemthe element appears in a group, the value of the property that it represents must be set.
        if(isGrouped) {
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
                elem = new ElementNumber(type, itemIndex, property,ipeditor, row, col,  data, isGrouped, isDefault, propertyValue);
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
    
    protected int getFontWidth(Graphics2D graphics2d, String str) {
        return graphics2d.getFontMetrics().stringWidth(str);
    }

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