/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.items.propertyeditors;

import java.awt.Component;
import java.beans.PropertyEditorSupport;
import org.gephi.legend.api.DescriptionItemElementValue;
import org.gephi.legend.builders.description.CustomValue;
import org.gephi.legend.items.DescriptionItemElement;
import org.gephi.legend.items.DescriptionItem;

/**
 *
 * @author edubecks
 */
public class DescriptionItemElementPropertyEditor extends PropertyEditorSupport {

    @Override
    public Component getCustomEditor() {
        DescriptionItemElementPanel descriptionItemPanel = new DescriptionItemElementPanel();
        descriptionItemPanel.setup(this);
        return descriptionItemPanel;
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override
    public String getAsText() {
        DescriptionItemElement descriptionItemElement = (DescriptionItemElement) getValue();
        if (descriptionItemElement != null) {
            return descriptionItemElement.getValue();
        }
        return "";
    }

    @Override
    public void setAsText(String value) {
        DescriptionItemElementValue descriptionItemElementValue = new CustomValue();
        setValue(new DescriptionItemElement(descriptionItemElementValue, value));
    }

}
