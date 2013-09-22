package org.gephi.legend.inplaceeditor;

import java.util.ArrayList;
import java.util.Map;
import org.gephi.legend.inplaceelements.BaseElement;
import org.gephi.preview.api.PreviewProperty;

/**
 * represents a logical equivalent of a cell in the inplace editor layout.
 * @author mvvijesh
 */
public class Column {

    private Row r; // the row that this column object belongs to
    private InplaceEditor ipeditor; // the inplace editor of which this column object is a part of
    private ArrayList<BaseElement> elements; // the inplace editor elements contained in the column
    private Boolean isGrouped; // indicates whether the elements in the column are grouped
    private Boolean isGroupDefaultPicked; // indicates whether a default element has been picked within the group

    public Column(InplaceEditor ipeditor, Row r, Boolean isGrouped) {
        this.r = r;
        this.ipeditor = ipeditor;
        this.isGrouped = isGrouped;
        this.isGroupDefaultPicked = false;
        this.elements = new ArrayList<BaseElement>();
    }

    /**
     * 
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
    public BaseElement addElement(BaseElement.ELEMENT_TYPE type, int itemIndex, PreviewProperty property, Map<String, Object> data, Boolean isDefault, Object propertyValue) {
        BaseElement elem = BaseElement.createElement(type, itemIndex, property, ipeditor, r, this, data, isGrouped, isDefault, propertyValue);
        elements.add(elem);
        return elem;
    }

    public Boolean isDefaultPicked() {
        return isGroupDefaultPicked;
    }

    public void setIsDefaultPicked(Boolean picked) {
        isGroupDefaultPicked = picked;
    }

    public Boolean isGrouped() {
        return isGrouped;
    }

    public void deleteElement(BaseElement e) {
        elements.remove(e);
    }

    // get methods
    public Row getRow() {
        return r;
    }

    public InplaceEditor getInplaceEditor() {
        return ipeditor;
    }

    public ArrayList<BaseElement> getElements() {
        return elements;
    }
}