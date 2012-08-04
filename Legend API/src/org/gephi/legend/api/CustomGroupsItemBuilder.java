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

    public void retrieveData(ArrayList<String> labels, ArrayList<Color> colors, ArrayList<Float> values);

}
