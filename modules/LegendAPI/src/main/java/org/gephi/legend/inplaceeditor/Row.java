package org.gephi.legend.inplaceeditor;

import java.util.ArrayList;
import java.util.Map;
import org.gephi.legend.inplaceelements.BaseElement;
import org.gephi.preview.api.PreviewProperty;

/**
 *  represents a row in the inplace editor layout
 * @author mvvijesh
 */
public class Row {

    InplaceEditor ipeditor; // the inplace editor to which the row belongs to
    ArrayList<Column> columns; // list of columns associated with the inplace editor

    public Row(InplaceEditor ipeditor) {
        this.ipeditor = ipeditor;
        this.columns = new ArrayList<Column>();
    }

    /**
     * 
     * @param isGrouped - indicates whether the column to be added contains grouped elements
     * @return newly created column
     */
    public Column addColumn(Boolean isGrouped) {
        Column newCol = new Column(ipeditor, this, isGrouped);
        columns.add(newCol);
        return newCol;
    }

    /**
     * remove the column
     * @param col - column to be removed
     */
    public void deleteColumn(Column col) {
        columns.remove(col);
    }

    /**
     * 
     * @param col - the column to which the element must be added
     * @param type - the type of element being added - CHECKBOX, COLOR, FILE,
     * FONT, FUNCTION, IMAGE, LABEL, NUMBER, TEXT
     * @param itemIndex - index of the item to which the inplace editor belongs
     * to
     * @param property - the preview property that the element will modify
     * @param data - the extra data required by the element of corresponding
     * type
     * @param isDefault - if the element is grouped, this value represents
     * whether it is the default
     * @param propertyValue - if the element is grouped, the value that this
     * element represents
     * @return the newly created element
     */
    public BaseElement addElement(Column col, BaseElement.ELEMENT_TYPE type, int itemIndex, PreviewProperty property, Map<String, Object> data, Boolean isDefault, Object propertyValue) {
        BaseElement elem = col.addElement(type, itemIndex, property, data, isDefault, propertyValue);
        return elem;
    }

    /**
     * 
     * @param col - the column in which the element to be removed is contained
     * @param e - the element to be removed
     */
    public void deleteElement(Column col, BaseElement e) {
        col.deleteElement(e);
    }

    // get methods
    public InplaceEditor getInplaceEditor() {
        return ipeditor;
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }

    // rendering methods
    public void render() {
    }
}
