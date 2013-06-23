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
public class column {
    row r;
    inplaceEditor ipeditor;
    ArrayList<element> elements;
    
    public column(inplaceEditor ipeditor, row r)    {
        this.r = r;
        this.ipeditor = ipeditor;
        elements = new ArrayList<element>();
    }
    
    public element addElement(element.ELEMENT_TYPE type, int itemIndex, PreviewProperty property, Object[] data) {
        element elem = new element(type, itemIndex, property, data);
        elements.add(elem);
        return elem;
    }
    
    public void deleteElement(element e)    {
        elements.remove(e);
    }
    
    // get methods
    public row getRow(){
        return r;
    }
    
    public inplaceEditor getInplaceEditor() {
        return ipeditor;
    }
    
    public ArrayList<element> getElements()   {
        return elements;
    }
    
    // rendering methods
    public void render() {
        
    }
}
