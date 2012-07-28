/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.items;

import org.gephi.legend.api.LegendItem;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.plugin.items.AbstractItem;

/**
 *
 * @author edubecks
 */
public class TableItem extends AbstractItem implements LegendItem {

    public static final String LEGEND_TYPE = "Table Legend";
    //data
    public static final String TABLE_VALUES = "table values";
    public static final String LABELS = "labels";
    public static final String LABELS_IDS = "labels ids";
    public static final String LIST_OF_COLORS = "list of colors";


    public enum VerticalTextDirection {

        // anti clockwise
        UP(-90d),
        DOWN(-90d),
        DIAGONAL(-45d);
        private final double rotationAngle;

        private VerticalTextDirection(double rotationAngle) {
            this.rotationAngle = rotationAngle;
        }

        public double rotationAngle() {
            return Math.toRadians(rotationAngle);
        }

    }

    public TableItem(Object source) {
        super(source, LEGEND_TYPE);
    }

    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[0].getValue()) + " [" + LEGEND_TYPE + "]";
    }

    @Override
    public PreviewProperty[] getDynamicPreviewProperties() {
        return new PreviewProperty[0];
    }
}
