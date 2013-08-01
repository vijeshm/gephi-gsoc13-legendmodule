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
public class Column {
    Row r;
    InplaceEditor ipeditor;
    ArrayList<Element> elements;
    
    public Column(InplaceEditor ipeditor, Row r)    {
        this.r = r;
        this.ipeditor = ipeditor;
        elements = new ArrayList<Element>();
    }
    
    public Element addElement(Element.ELEMENT_TYPE type, int itemIndex, PreviewProperty property, Object[] data) {
        Element elem = new Element(type, itemIndex, property, data);
        elements.add(elem);
        return elem;
    }
    
    public void deleteElement(Element e)    {
        elements.remove(e);
    }
    
    // get methods
    public Row getRow(){
        return r;
    }
    
    public InplaceEditor getInplaceEditor() {
        return ipeditor;
    }
    
    public ArrayList<Element> getElements()   {
        return elements;
    }
    
    // rendering methods
    public void render() {
        
    }
}
