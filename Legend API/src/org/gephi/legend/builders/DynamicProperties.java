/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.builders;

import org.gephi.preview.api.Item;

/**
 *
 * @author edubecks
 */
public interface DynamicProperties {
    public void addPreviewProperty(Item item, int currentNumOfProperties, int numNewProperties);
    public void removePreviewProperty(Item item, int numRemoveProperties);
}
