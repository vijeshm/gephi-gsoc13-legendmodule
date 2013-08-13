/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.groups;

import java.util.ArrayList;
import org.gephi.legend.api.AbstractItem;
import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.PreviewProperty;

/**
 *
 * @author mvvijesh, edubecks
 */
public class GroupsItem extends AbstractItem implements LegendItem {

    public static final String LEGEND_TYPE = "new Groups Item";

    protected ArrayList<GroupElement> groups;
    //BODY
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
}