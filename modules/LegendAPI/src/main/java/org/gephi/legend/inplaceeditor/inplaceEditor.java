/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceeditor;

import com.bric.swing.ColorPicker;
import com.connectina.swing.fontchooser.JFontChooser;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.api.blockNode;
import org.gephi.legend.spi.LegendItem;
import org.gephi.legend.spi.LegendItemBuilder;
import org.gephi.preview.G2DRenderTargetBuilder;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.openide.util.Lookup;
import sun.awt.RepaintArea;
import sun.reflect.generics.tree.Tree;

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

    public void reflectAction(int x, int y) {
        int borderSize = inplaceItemRenderer.BORDER_SIZE;
        int unitSize = inplaceItemRenderer.UNIT_SIZE;
        int editorOriginX = getData(ORIGIN_X);
        int editorOriginY = getData(ORIGIN_Y);
        int editorWidth = getData(WIDTH);
        int editorHeight = getData(HEIGHT);

        // get the item associated with the inplace editor
        blockNode node = getData(BLOCKNODE);
        Item item = node.getItem();
        int itemIndex = item.getData(LegendItem.ITEM_INDEX);

        // get the preview properties to make the changes get reflected
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        PreviewProperties previewProperties = previewModel.getProperties();

        // find out which element has been clicked based on the click-coordinates and element coordinates
        element selectedElem = null;
        column selectedColumn = null;
        for (row r : rows) {
            ArrayList<column> columns = r.getColumns();
            for (column c : columns) {
                ArrayList<element> elements = c.getElements();
                for (element elem : elements) {
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
            if (selectedColumn.elements.size() > 1) {
                // if the column that you've selected has multiple elements, each should represent the same property
                // the value of the selected element should be set as the value of the common property.
                // Object[] data: data[0] says whether it is selected. data[1] says what value to set as the property.
                ArrayList<element> elements = selectedColumn.elements;

                // checking if all the properties are the same
                Boolean uniform = true;
                PreviewProperty prop = elements.get(0).getProperty();
                for (element e : elements) {
                    if (e.getProperty().getDisplayName() != prop.getDisplayName()) {
                        uniform = false;
                        break;
                    }
                }

                if (uniform) {
                    // for all the elements, set data[0] as unselected. for the selectedItem, set data[0] as selected.
                    for (element e : elements) {
                        Object[] elementData = e.getAssociatedData();
                        elementData[0] = false;
                    }

                    Object[] elementData = selectedElem.getAssociatedData();
                    elementData[0] = true;
                    prop.setValue(elementData[3]);
                    previewProperties.putValue(prop.getName(), elementData[3]);
                } else {
                    prop.setValue(null);
                }

            } else {

                PreviewProperty prop = selectedElem.getProperty();
                Object[] elementData = selectedElem.getAssociatedData();

                switch (selectedElem.getElementType()) {
                    case LABEL:
                        break;

                    case FONT:
                        JFontChooser chooser = new JFontChooser((Font) prop.getValue());
                        Font chosenFont = chooser.showDialog(new JFrame("choose a font"), (Font) prop.getValue());
                        if (chosenFont != null) {
                            prop.setValue(chosenFont);
                            previewProperties.putValue(prop.getName(), chosenFont);
                        }
                        break;

                    case TEXT:
                        String newValue = (String) JOptionPane.showInputDialog(null, "Enter new text:", (String) prop.getValue());
                        if (newValue != null) {
                            prop.setValue(newValue);
                            previewProperties.putValue(prop.getName(), newValue);
                        }
                        break;

                    case CHECKBOX:
                        Boolean currentState = (Boolean) prop.getValue();
                        elementData[0] = !currentState;
                        prop.setValue(!currentState);
                        previewProperties.putValue(prop.getName(), !currentState);
                        break;

                    case IMAGE:
                        Boolean isSelected = (Boolean) elementData[0];
                        elementData[0] = !isSelected;
                        prop.setValue(!isSelected);
                        previewProperties.putValue(prop.getName(), !isSelected);
                        break;

                    case COLOR:
                        Color selectedColor = ColorPicker.showDialog(null, (Color) prop.getValue(), true);
                        if (selectedColor != null) {
                            prop.setValue(selectedColor);
                            previewProperties.putValue(prop.getName(), selectedColor);
                        }
                        break;

                    case NUMBER:
                        String newValueString = (String) JOptionPane.showInputDialog(null, "New Value:", prop.getDisplayName(), JOptionPane.PLAIN_MESSAGE, null, null, null);
                        if (newValueString != null) {
                            try {
                                Integer newNumber = Integer.parseInt(newValueString);
                                prop.setValue(newNumber);
                                previewProperties.putValue(prop.getName(), newNumber);
                            } catch (NumberFormatException e) {
                            }
                        }
                        break;

                    case FILE:
                        JFileChooser fc = new JFileChooser();
                        int returnVal = fc.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            previewProperties.putValue(prop.getName(), file);
                        }
                }
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
}
