/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.items;

import java.awt.Color;
import org.gephi.legend.api.DescriptionItemElementValue;

/**
 *
 * @author edubecks
 */
public class DescriptionItemElement {
    
    private DescriptionItemElementValue descriptionItemElementValue;
    private final String value;


    public DescriptionItemElement() {
        this.value = "";
    }
    public DescriptionItemElement(DescriptionItemElementValue descriptionItemElementValue, String value) {
        this.descriptionItemElementValue = descriptionItemElementValue;
        this.value = value;
    }

    public DescriptionItemElementValue getGenerator() {
        return descriptionItemElementValue;
    }

    
    public String getValue() {
        return value;
    }
    
    
    

    

}
