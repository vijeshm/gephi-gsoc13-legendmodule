package org.gephi.legend.plugin.groups;

import java.awt.Color;
import java.util.ArrayList;
import org.openide.util.lookup.ServiceProvider;

/**
 * This is the default custom item builder for the group legend.
 *
 * This class is exposed as a service. The UI uses these services and the Lookup
 * API to show the list of available custom builders.
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

    /**
     *
     * @param item - the item being built
     * @return
     */
    @Override
    public ArrayList<GroupElement> retrieveData(GroupsItem item) {
        ArrayList<GroupElement> groups = new ArrayList<GroupElement>();

        // dummy data
        String[] labels = {"Group 1", "Group 2", "Group 3", "Group 4", "Group 5"};
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.BLACK};
        Float[] values = {1f, 2f, 1.5f, 1f, 0.75f};

        // populate the arraylist with the group elements. The default values for font, color etc are specified in the GroupElement class
        for (int i = 0; i < labels.length; i++) {
            GroupElement groupElement = new GroupElement(item, labels[i], GroupElement.defaultLabelFont, GroupElement.defaultLabelColor, GroupElement.defaultLabelAlignment, GroupElement.defaultLabelPosition, values[i], GroupElement.defaultShape, colors[i]);
            groups.add(groupElement);
        }

        return groups;
    }
}