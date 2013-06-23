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
public class inplaceEditor {

    ArrayList<row> rows;

    public inplaceEditor() {
        rows = new ArrayList<row>();
    }

    public row addRow() {
        row r = new row(this);
        rows.add(r);
        return r;
    }

    public void deleteRow(row r) {
        rows.remove(r);
    }

    public column addColumn(row r) {
        column col = r.addColumn();
        return col;
    }

    public void deleteColumn(row r, column col) {
        r.deleteColumn(col);
    }

    public element addElement(row r, column col, element.ELEMENT_TYPE type, int itemIndex, PreviewProperty property, Object[] data) {
        element elem = r.addElement(col, type, itemIndex, property, data);
        return elem;
    }

    // get methods
    public ArrayList<row> getRows() {
        return rows;
    }

    // rendering methods
    public void render() {
    }
}
