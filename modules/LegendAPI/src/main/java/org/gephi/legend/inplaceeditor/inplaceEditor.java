/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceeditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.gephi.legend.api.LegendController;
import org.gephi.preview.G2DRenderTargetBuilder;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.openide.util.Lookup;
import sun.awt.RepaintArea;

/**
 *
 * @author mvvijesh
 */
public class inplaceEditor implements Item {

    public static final String RENDERER = "inplaceRenderer";
    // public static final String ROWS = "rows";
    public static final String TYPE = "inplace editor";
    public static final String BORDER_THICK = "border thick";
    public static final String BACKGROUND_COLOR = "background color";
    public static final String ORIGIN_X = "originx";
    public static final String ORIGIN_Y = "originy";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String BLOCK_INPLACEEDITOR_GAP = "gap";
    public static final String BLOCKNODE = "blocknode";
    private ArrayList<row> rows;
    private final String type;
    private final Object source;
    private final Map<String, Object> data;

    public inplaceEditor(Object source) {
        this.source = source;
        this.type = TYPE;
        this.rows = new ArrayList<row>();
        this.data = new HashMap<String, Object>();
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

    /*
    // rendering methods
    public void render(float originX, float originY) {
        AffineTransform saveState = graphics2D.getTransform();

        AffineTransform originTranslation = new AffineTransform(saveState);
        originTranslation.translate(originX, originY);

        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setTransform(originTranslation);
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(-500, -300, 500, 300);

        graphics2D.setTransform(saveState);
    }
    */

    public String getType() {
        return type;
    }

    public Object getSource() {
        return source;
    }

    public <D> D getData(String key) {
        return (D) data.get(key);
    }

    public void setData(String key, Object value) {
        data.put(key, value);
    }

    public String[] getKeys() {
        return data.keySet().toArray(new String[0]);
    }
}
