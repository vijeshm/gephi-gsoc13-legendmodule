package org.gephi.legend.inplaceeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.gephi.legend.inplaceelements.BaseElement;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;

/**
 * model to hold data about inplace editor items.
 *
 * These items are built using InplaceItemBuilder and rendered using
 * InplaceItemRenderer. An inplace editor is made up of rows. Each row is made
 * up of columns. Each row might contain a different number of columns, unlike a
 * conventional table. Each of these columns contain inplace editor elements
 * within them. A column is said to be grouped if it contains more than one
 * elements in it. All the grouped elements represent the same preview property.
 * Ex: Alignment - a set of four image elements can be combined into a single
 * column group.
 *
 * @author mvvijesh
 */
public class InplaceEditor implements Item {

    public static final String RENDERER = "inplaceRenderer";
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

    /**
     * appends a new row to the inplace editor.
     *
     * @return returns the added row
     */
    public Row addRow() {
        Row r = new Row(this);
        rows.add(r);
        return r;
    }

    /**
     * removes the specified row.
     *
     * @param r - row to be removed
     */
    public void deleteRow(Row r) {
        rows.remove(r);
    }

    /**
     * appends a column to the given row.
     *
     * @param r - the row to which the column must be added
     * @param isGrouped - True if the column is intended to contain a set of
     * elements representing the same preview property, False otherwise.
     * @return the newly created column
     */
    public Column addColumn(Row r, Boolean isGrouped) {
        Column col = r.addColumn(isGrouped);
        return col;
    }

    /**
     * deletes a specified column from the specified row
     *
     * @param r - row from which the column must be removed
     * @param col - the column to be removed
     */
    public void deleteColumn(Row r, Column col) {
        r.deleteColumn(col);
    }

    /**
     * adds an element to the column
     *
     * @param r - the row to which the element is to be added
     * @param col - the column to which the element is to be added
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
    public BaseElement addElement(Row r, Column col, BaseElement.ELEMENT_TYPE type, int itemIndex, PreviewProperty property, Map<String, Object> data, Boolean isDefault, Object propertyValue) {
        BaseElement elem = r.addElement(col, type, itemIndex, property, data, isDefault, propertyValue);
        return elem;
    }

    public ArrayList<Row> getRows() {
        return rows;
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
    public String[] getKeys() {
        return data.keySet().toArray(new String[0]);
    }

    @Override
    public <D> D getData(String key) {
        return (D) data.get(key);
    }

    @Override
    public void setData(String key, Object value) {
        data.put(key, value);
    }

    /**
     * this method will be called when a click occurs within the inplace editor.
     *
     * @param x - x-coordinate of the click
     * @param y - y-coordinate of the click
     */
    public void reflectAction(int x, int y) {
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

                    // only for the selected element, set the flag to be true
                    selectedElem.setAssociatedData(BaseElement.SELECTED_WITHIN_GROUP, true);
                    // perform the required action on the selected element
                    selectedElem.onSelect();
                } else {
                    prop.setValue(null);
                }

            } else {
                // perform the required action on the selected element
                selectedElem.onSelect();
            }
        }
    }
}
