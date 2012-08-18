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
     * build the the table.
     * <code>verticalLabels</code> and
     * <code>verticalColors</code> should have the same size.
     * <code>horizontalLabels</code> and
     * <code>horizontalColors</code> should have the same size too. Finally,
     * <code>values</code> represents the table and its number of rows should be
     * equal to the number of elements in
     * <code>horizontalLabels</code> as well as the number of columns should be
     * equal to the number of elements in
     * <code>verticalValues</code>
     *
     * @param labels an arraylist of one single value containing the selection
     * of labels: <code>TableItem.LabelSelection.VERTICAL</code> or
     * <code>TableItem.LabelSelection.HORIZONTAL</code>
     * <code>TableItem.LabelSelection.BOTH</code>
     * @param horizontalLabels list of labels that will be displayed at the
     * sides
     * @param verticalLabels list of labels that will be displayed at top or
     * bottom
     * @param values table values representing the matrix
     * @param horizontalColors colors used to represent the horizontal colors
     * @param verticalColors colors used to represent the vertical colors
     * @param valueColors table colors representing the matrix
     */
    public void retrieveData(ArrayList<TableItem.LabelSelection> labels,
                             ArrayList<String> horizontalLabels,
                             ArrayList<String> verticalLabels,
                             ArrayList<ArrayList<String>> values,
                             ArrayList<Color> horizontalColors,
                             ArrayList<Color> verticalColors,
                             ArrayList<ArrayList<Color>> valueColors);

}
