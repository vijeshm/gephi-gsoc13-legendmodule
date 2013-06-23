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
     public static final Integer ALIGNMENT_LEFT = 5;
     public static final Integer ALIGNMENT_CENTER = 6;
     public static final Integer ALIGNMENT_RIGHT = 7;
     public static final Integer ALIGNMENT_JUSTIFY = 8;
     public static final Integer ALIGNMENT_ALL = 9;
     public static final Integer ALIGNMENT_LCR = 10;
     */
    public static enum ELEMENT_TYPE {

        LABEL,
        FONT,
        TEXT,
        CHECKBOX,
        IMAGE,
        ALIGNMENT_LEFT,
        ALIGNMENT_CENTER,
        ALIGNMENT_RIGHT,
        ALIGNMENT_JUSTIFY,
        ALIGNMENT_ALL,
        ALIGNMENT_LCR
    };
    private ELEMENT_TYPE type;
    private Integer itemIndex;
    private PreviewProperty property;
    private Object[] data;

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
                
            case FONT:
                this.data = new Object[1];
                this.data[0] = (Font) data[0]; // font
                break;

            case IMAGE:
                this.data = new Object[3];
                this.data[0] = (Image) data[0]; // image
                this.data[1] = (Integer) data[1]; // width
                this.data[2] = (Integer) data[2]; // height
                break;

            // no extra data is required for checkbox and alignments
        }
    }

    // only get methods, no set methods. this is because an element is concrete once it is built.
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

    public void render() {
    }
}