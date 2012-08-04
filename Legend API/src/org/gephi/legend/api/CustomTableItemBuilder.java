/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author edubecks
 */
public interface CustomTableItemBuilder {

    public void retrieveData(ArrayList<StringBuilder> labels, ArrayList<StringBuilder> horizontalLabels, ArrayList<StringBuilder> verticalLabels, ArrayList<ArrayList<Float>> values, ArrayList<Color> horizontalColors, ArrayList<Color> verticalColors, ArrayList<ArrayList<Color>> valueColors);

}
