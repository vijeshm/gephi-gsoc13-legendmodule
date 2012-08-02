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
public interface TableGenerator {    
    public boolean generateTable(ArrayList<String> horizontalLabels, ArrayList<String> verticalLabels, ArrayList<Color> colors, ArrayList<ArrayList<Float>> values);
    public String getDescription();
}
