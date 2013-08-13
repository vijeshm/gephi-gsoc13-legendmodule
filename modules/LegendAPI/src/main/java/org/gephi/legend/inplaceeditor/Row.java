/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceeditor;

import java.util.ArrayList;
import java.util.Map;
import org.gephi.legend.inplaceelements.BaseElement;
import org.gephi.preview.api.PreviewProperty;

/**
 *
 * @author mvvijesh
 */
public class Row {

    InplaceEditor ipeditor;
    ArrayList<Column> columns;

    public Row(InplaceEditor ipeditor) {
        this.ipeditor = ipeditor;
        this.columns = new ArrayList<Column>();
    }

    public Column addColumn(Boolean isGrouped) {
        Column newCol = new Column(ipeditor, this, isGrouped);
        columns.add(newCol);
        return newCol;
    }

    public void deleteColumn(Column col) {
        columns.remove(col);
    }

    public BaseElement addElement(Column col, BaseElement.ELEMENT_TYPE type, int itemIndex, PreviewProperty property, Map<String, Object> data, Boolean isDefault, Object propertyValue) {
        BaseElement elem = col.addElement(type, itemIndex, property, data, isDefault, propertyValue);
        return elem;
    }

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
