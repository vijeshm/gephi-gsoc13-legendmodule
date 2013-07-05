/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.inplaceeditor;

import java.awt.Font;
import java.awt.Image;
import org.gephi.preview.api.PreviewProperty;

/**
 *
 * @author mvvijesh
 */
public class element {

    /*
     public static final Integer LABEL = 0;
     public static final Integer FONT = 1;
     public static final Integer TEXT = 2;
     public static final Integer CHECKBOX = 3;
     public static final Integer IMAGE = 4;
     public static final Integer COLOR = 5;
     public static final Integer NUMBER = 6;
     */
    public static enum ELEMENT_TYPE {

        LABEL,
        FONT,
        TEXT,
        CHECKBOX,
        IMAGE,
        COLOR,
        NUMBER
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

    public element(ELEMENT_TYPE type, int itemIndex, PreviewProperty property, Object[] data) {
        this.type = type;
        this.itemIndex = itemIndex;
        this.property = property;
        switch (type) {
            case LABEL:
            case TEXT:
                this.data = new Object[1];
                this.data[0] = (String) data[0]; // label, or current text
                break;

            case IMAGE:
                this.data = data;
                break;
                
            // no extra data is required for checkbox, number, alignments, font and color
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