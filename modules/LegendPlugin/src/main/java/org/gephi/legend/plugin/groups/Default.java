/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.groups;

import java.awt.Color;
import java.util.ArrayList;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.Item;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author mvvijesh, edubecks
 */
@ServiceProvider(service = CustomGroupsItemBuilder.class, position = 1)
public class Default implements CustomGroupsItemBuilder {

    @Override
    public String getDescription() {
        return DEFAULT_DESCRIPTION;
    }

    @Override
    public String getTitle() {
        return DEFAULT_TITLE;
    }

    @Override
    public boolean isAvailableToBuild() {
        return true;
    }

    @Override
    public String stepsNeededToBuild() {
        return NONE_NEEDED;
    }

    @Override
    public void retrieveData(Item item, ArrayList<GroupElement> groups) {
        String[] labels = {"group 1", "group 2", "group 3", "group 4"};
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
        Float[] values = {0.8f, 0.3f, 0.9f, 0.5f};

        for (int i = 0; i < labels.length; i++) {
            GroupElement groupElement = new GroupElement(item, labels[i], GroupElement.labelFont, GroupElement.labelColor, GroupElement.labelAlignment, GroupElement.labelPosition, values[i], GroupElement.shape, colors[i], true);
            groups.add(groupElement);
        }
    }
}