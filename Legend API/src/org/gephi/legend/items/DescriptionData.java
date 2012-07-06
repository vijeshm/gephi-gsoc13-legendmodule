/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.items;

import java.awt.Color;

/**
 *
 * @author edubecks
 */
public class DescriptionData {

    public enum Data {

        COLOR, VALUE
    };
    private final Color color;
    private final Float value;
    private final Data data;

    public DescriptionData() {
        this.color = Color.BLACK;
        this.value = 0f;
        this.data =  Data.COLOR;
    }

    public DescriptionData(Color color) {
        this.color = color;
        this.value = 0f;
        this.data =  Data.COLOR;
    }

    public DescriptionData(Color color, Float value) {
        this.color = color;
        this.value = value;
        this.data =  Data.COLOR;
    }

    public DescriptionData(Float value) {
        this.color = Color.BLACK;
        this.value = value;
        this.data =  Data.COLOR;
    }

    public Color getColor() {
        return color;
    }

    public Float getValue() {
        return value;
    }

    public Data getData() {
        return data;
    }
    
    

}
