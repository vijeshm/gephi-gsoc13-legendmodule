/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceeditor;

import java.util.ArrayList;
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

    public Column addColumn() {
        Column newCol = new Column(ipeditor, this);
        columns.add(newCol);
        return newCol;
    }

    public void deleteColumn(Column col) {
        columns.remove(col);
    }

    public Element addElement(Column col, Element.ELEMENT_TYPE type, int itemIndex, PreviewProperty property, Object[] data) {
        Element elem = col.addElement(type, itemIndex, property, data);
        return elem;
    }

    public void deleteElement(Column col, Element e) {
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
