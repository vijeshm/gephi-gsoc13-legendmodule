/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceeditor;

import org.gephi.legend.inplaceeditor.inplaceElements.BaseElement;
import java.util.ArrayList;
import java.util.Map;
import org.gephi.legend.inplaceeditor.inplaceElements.BaseElement;
import org.gephi.preview.api.PreviewProperty;

/**
 *
 * @author mvvijesh
 */
public class Column {
    private Row r;
    private InplaceEditor ipeditor;
    private ArrayList<BaseElement> elements;
    private Boolean isGrouped;
    private Boolean isGroupDefaultPicked;
    
    public Column(InplaceEditor ipeditor, Row r, Boolean isGrouped)    {
        this.r = r;
        this.ipeditor = ipeditor;
        this.isGrouped = isGrouped;
        this.isGroupDefaultPicked = false;
        elements = new ArrayList<BaseElement>();
    }
    
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
    
    public void deleteElement(BaseElement e)    {
        elements.remove(e);
    }
    
    // get methods
    public Row getRow(){
        return r;
    }
    
    public InplaceEditor getInplaceEditor() {
        return ipeditor;
    }
    
    public ArrayList<BaseElement> getElements()   {
        return elements;
    }
    
    // rendering methods
    public void render() {
        
    }
}
