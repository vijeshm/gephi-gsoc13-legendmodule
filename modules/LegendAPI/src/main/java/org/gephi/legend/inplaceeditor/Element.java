/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceeditor;

import org.gephi.preview.api.PreviewProperty;

/**
 *
 * @author mvvijesh
 */
public class Element {

    /*
     public static final Integer LABEL = 0;
     public static final Integer FONT = 1;
     public static final Integer TEXT = 2;
     public static final Integer CHECKBOX = 3;
     public static final Integer IMAGE = 4;
     public static final Integer COLOR = 5;
     public static final Integer NUMBER = 6;
     public static final Integer FILE = 7;
     public static final Integer FUNCTION = 8;
     */
    public static enum ELEMENT_TYPE {

        LABEL,
        FONT,
        TEXT,
        CHECKBOX,
        IMAGE,
        COLOR,
        NUMBER,
        FILE,
        FUNCTION
    };
    
    /*
    public static String[] ELEMENT_TYPE_STRING = {
        "LABEL",            // 0
        "FONT",             // 1
        "TEXT",             // 2
        "CHECKBOX",         // 3
        "IMAGE",            // 4
        "COLOR",            // 5
        "NUMBER",           // 6
        "FILE",             // 7
        "FUNCTION"          // 8
    };
    */
    
    private ELEMENT_TYPE type;
    private Integer itemIndex;
    private PreviewProperty property;
    private Object[] data;
    
    // these properties are set by the inPlaceRenderer when the the element is rendered.
    private int originX;
    private int originY;
    private int elementWidth;
    private int elementHeight;

    public Element(ELEMENT_TYPE type, int itemIndex, PreviewProperty property, Object[] data) {
        this.type = type;
        this.itemIndex = itemIndex;
        this.property = property;
        switch (type) {
            case LABEL:
                // data[0] = the label itself
            case IMAGE:
                // data[0] = default boolean values
                // data[1] = path of image to be displayed when data[0] is false
                // data[2] = path of image to be displayed when data[0] is true
                // data[3] = ?? (to be filled later)
            case CHECKBOX:
                // data[0] = default boolean value
            case FUNCTION:
                // data[0] = reference to an object that implements the inplaceClickResponse interface
                // data[1] = image to be used during rendering
                
                this.data = data;
                break;
                
            // no extra data is required for font, number, alignments, color, file
        }
    }

    // only get methods, no set methods (except for geometry). this is because an element is concrete once it is built.
    public void setGeometry(int x, int y, int width, int height) {
        originX = x;
        originY = y;
        elementWidth = width;
        elementHeight = height;
    }
    
    public PreviewProperty getProperty() {
        return property;
    }

    public ELEMENT_TYPE getElementType() {
        return type;
    }

    public Integer getItemIndex() {
        return itemIndex;
    }
    
    /**
     * Note: Refactor to a Map<String, Object>
     */
    public void setAssociatedData(int index, Object value){
        data[index] = value;
    }

    public Object[] getAssociatedData() {
        return data;
    }
    
    public int getOriginX() {
        return originX;
    }
    
    public int getOriginY() {
        return originY;
    }
    
    public int getElementWidth() {
        return elementWidth;
    }
    
    public int getElementHeight() {
        return elementHeight;
    }  
}