package org.gephi.legend.plugin.groups;

import java.awt.Rectangle;
import java.util.ArrayList;
import org.gephi.legend.api.AbstractItem;
import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.spi.LegendItem;
import static org.gephi.legend.spi.LegendItem.PROPERTIES;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;

/**
 * the item model for the groups legend.
 *
 * The group layout is organized into a number of columns called group elements.
 * Each group element consists of a shape area and a label area. The label
 * position can be adjusted to appear above or below the shape area. Each group
 * item will be associated a value that determines the height of the shape.
 * During rendering of the group item, these values are normalized. The custom
 * group item builder has a method that gathers the required information and
 * creates group elements out of it. This information is extracted by the group
 * renderer for rendering the group elements as configured by the custom
 * builder.
 *
 * @author mvvijesh, edubecks
 */
public class GroupsItem extends AbstractItem implements LegendItem, Item.BoundingBoxProvidingItem {

    public static final String LEGEND_TYPE = "Groups Item";
    protected ArrayList<GroupElement> groups; // the list of group elements

    public GroupsItem(Object source) {
        super(source, LEGEND_TYPE);
    }

    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[LegendProperty.LABEL].getValue());
    }

    public ArrayList<GroupElement> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<GroupElement> groups) {
        this.groups = groups;
    }

    /**
     *
     * @return a rectangle that acts as a bounding box for the groups legend.
     */
    @Override
    public Rectangle getBoundingBox() {
        PreviewProperty[] ownProperties = this.getData(PROPERTIES);
        float originX = ownProperties[LegendProperty.USER_ORIGIN_X].getValue();
        float originY = ownProperties[LegendProperty.USER_ORIGIN_Y].getValue();
        float width = ownProperties[LegendProperty.WIDTH].getValue();
        float height = ownProperties[LegendProperty.HEIGHT].getValue();
        return new Rectangle((int) originX, (int) originY, (int) width, (int) height);
    }
}