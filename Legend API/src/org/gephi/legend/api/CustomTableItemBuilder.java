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
public interface CustomTableItemBuilder {
    
    public void retrieveData(ArrayList<StringBuffer> labels, ArrayList<StringBuffer> horizontalLabels, ArrayList<StringBuffer> verticalLabels, ArrayList<Color> colors, ArrayList<ArrayList<Float>> values);

}
