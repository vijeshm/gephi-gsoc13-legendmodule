/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.awt.Color;
import java.util.ArrayList;
import org.gephi.legend.items.TableItem;

/**
 *
 * @author edubecks
 */
public interface CustomTableItemBuilder {

    /**
     * This function receives the objects that need to be filled in order to
     * build the item
     * @param labels list of labels
     * @param horizontalLabels list of labels that will be displayed at the sides
     * @param verticalLabels list of labels that will be displayed at top or bottom
     * @param values table values representing the matrix
     * @param horizontalColors colors used to represent the horizontal colors
     * @param verticalColors colors used to represent the vertical colors
     * @param valueColors table colors representing the matrix
     */
    public void retrieveData(ArrayList<TableItem.LabelSelection> labels, ArrayList<StringBuilder> horizontalLabels, ArrayList<StringBuilder> verticalLabels, ArrayList<ArrayList<Float>> values, ArrayList<Color> horizontalColors, ArrayList<Color> verticalColors, ArrayList<ArrayList<Color>> valueColors);

}
