/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceeditor;

import com.bric.swing.ColorPicker;
import com.connectina.swing.fontchooser.JFontChooser;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.gephi.legend.api.BlockNode;
import org.gephi.legend.inplaceelements.BaseElement;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.openide.util.Lookup;

/**
 *
 * @author mvvijesh
 */
public class InplaceEditor implements Item {

    public static final String RENDERER = "inplaceRenderer";
    // public static final String ROWS = "rows";
    public static final String TYPE = "inplace editor";
    public static final String BORDER_THICK = "border thick";
    public static final String BACKGROUND_COLOR = "background color";
    public static final String ORIGIN_X = "originx";
    public static final String ORIGIN_Y = "originy";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String BLOCKNODE = "blocknode";
    private ArrayList<Row> rows;
    private final String type;
    private final Object source;
    private final Map<String, Object> data;

    public InplaceEditor(Object source) {
        this.source = source;
        this.type = TYPE;
        this.rows = new ArrayList<Row>();
        this.data = new HashMap<String, Object>();
    }

    public Row addRow() {
        Row r = new Row(this);
        rows.add(r);
        return r;
    }

    public void deleteRow(Row r) {
        rows.remove(r);
    }

    public Column addColumn(Row r, Boolean isGrouped) {
        Column col = r.addColumn(isGrouped);
        return col;
    }

    public void deleteColumn(Row r, Column col) {
        r.deleteColumn(col);
    }

    public BaseElement addElement(Row r, Column col, BaseElement.ELEMENT_TYPE type, int itemIndex, PreviewProperty property, Map<String, Object> data, Boolean isDefault, Object propertyValue) {
        BaseElement elem = r.addElement(col, type, itemIndex, property, data, isDefault, propertyValue);
        return elem;
    }

    // get methods
    public ArrayList<Row> getRows() {
        return rows;
    }

    public void reflectAction(int x, int y) {
        // get the item associated with the inplace editor
        BlockNode node = getData(BLOCKNODE);
        Item item = node.getItem();

        // find out which element has been clicked based on the click-coordinates and element coordinates
        BaseElement selectedElem = null;
        Column selectedColumn = null;
        for (Row r : rows) {
            ArrayList<Column> columns = r.getColumns();
            for (Column c : columns) {
                ArrayList<BaseElement> elements = c.getElements();
                for (BaseElement elem : elements) {
                    // check the condition excluding the left border. this is to avoid overlapping between elements
                    if ((x > elem.getOriginX() && x <= elem.getOriginX() + elem.getElementWidth())
                            && (y > elem.getOriginY() && y <= elem.getOriginY() + elem.getElementHeight())) {
                        selectedColumn = c;
                        selectedElem = elem;
                        break;
                    }
                }
            }
        }


        // if the click is on a label, nothing should happen
        // if the click is on a font, a font chooser should pop up. on choosing, the new font should be set in the item property, as well as preview property.
        // if the click is on a text, a new text area should pop up. heading should be property that we're editing.
        // if the click is on a checkbox, toggle the behavior - negate the property value and update the preview property.
        // image always appears as a sequence of elements in a column. So, if a click happens on an image, then draw a rectangle around the image, indicating that its selected.
        // if the click is on a color, display a color box popup. the new is set to the corresponding property
        // if the click is on a number, then display a popup to change the number and display its name on the top.
        if (selectedColumn != null && selectedElem != null) {
            if (selectedColumn.isGrouped()) {
                // if the column that you've selected has multiple elements, each should represent the same property
                // the value of the selected element should be set as the value of the common property.
                ArrayList<BaseElement> elements = selectedColumn.getElements();

                // checking if all the properties are the same
                Boolean uniform = true;
                PreviewProperty prop = elements.get(0).getProperty();
                for (BaseElement e : elements) {
                    if (!e.getProperty().getDisplayName().equals(prop.getDisplayName())) {
                        uniform = false;
                        break;
                    }
                }

                if (uniform) {
                    // for all the elements, deselect every element within the group
                    for (BaseElement e : elements) {
                        e.setAssociatedData(BaseElement.SELECTED_WITHIN_GROUP, false);
                    }

                    selectedElem.setAssociatedData(BaseElement.SELECTED_WITHIN_GROUP, true);
                    selectedElem.onSelect();
                } else {
                    prop.setValue(null);
                }

            } else {
                selectedElem.onSelect();
            }
        }
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public <D> D getData(String key) {
        return (D) data.get(key);
    }

    @Override
    public void setData(String key, Object value) {
        data.put(key, value);
    }

    @Override
    public String[] getKeys() {
        return data.keySet().toArray(new String[0]);
    }

    @Override
    public Rectangle getBoundingBox() {
        // the inplace editor is logically not an item. Its just used to modify properties of items.
        return new Rectangle(0, 0, 0, 0);
    }
}
