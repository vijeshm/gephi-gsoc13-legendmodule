/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.items;

import org.gephi.legend.api.DescriptionItemElementValue;
import org.gephi.legend.builders.description.elements.CustomValue;

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

    public final static DescriptionItemElement getDefaultGenerator() {
        return new DescriptionItemElement(new CustomValue(), "");
    }

    public String getValue() {
        return value;
    }

}
