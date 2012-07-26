/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.items.propertyeditors;

import java.awt.Component;
import java.beans.PropertyEditorSupport;

/**
 *
 * @author edubecks
 */
public class DescriptionItemElementPropertyEditor extends PropertyEditorSupport{
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
}
