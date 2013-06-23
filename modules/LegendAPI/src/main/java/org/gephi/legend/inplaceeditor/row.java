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
public class row {

    inplaceEditor ipeditor;
    ArrayList<column> columns;

    public row(inplaceEditor ipeditor) {
        this.ipeditor = ipeditor;
        this.columns = new ArrayList<column>();
    }

    public column addColumn() {
        column newCol = new column(ipeditor, this);
        columns.add(newCol);
        return newCol;
    }

    public void deleteColumn(column col) {
        columns.remove(col);
    }

    public element addElement(column col, element.ELEMENT_TYPE type, int itemIndex, PreviewProperty property, Object[] data) {
        element elem = col.addElement(type, itemIndex, property, data);
        return elem;
    }

    public void deleteElement(column col, element e) {
        col.deleteElement(e);
    }

    // get methods
    public inplaceEditor getInplaceEditor() {
        return ipeditor;
    }

    public ArrayList<column> getColumns() {
        return columns;
    }

    // rendering methods
    public void render() {
    }
}
