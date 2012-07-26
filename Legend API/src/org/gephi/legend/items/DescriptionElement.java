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
public class DescriptionElement {
    
    private final String key;
    private final String value;


    public DescriptionElement() {
        this.key = "";
        this.value = "";
    }
    public DescriptionElement(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public static DescriptionElement DescriptionElementWithKey(String key) {
        return new DescriptionElement(key,"");
    }
    public static DescriptionElement DescriptionElementWithValue(String value) {
        return new DescriptionElement("",value);
    }
    
    

    

}
