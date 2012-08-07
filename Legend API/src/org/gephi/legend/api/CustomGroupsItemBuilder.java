/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author edubecks
 */
public interface CustomGroupsItemBuilder {

    /**
     * This function receives the objects that need to be filled in order to
     * build the item
     * @param labels
     * @param colors
     * @param values 
     */
    public void retrieveData(ArrayList<StringBuilder> labels, ArrayList<Color> colors, ArrayList<Float> values);

}
