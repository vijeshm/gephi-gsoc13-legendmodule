/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.items;

import org.gephi.legend.manager.LegendManager;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.plugin.items.AbstractItem;
import org.openide.util.NbBundle;

/**
 *
 * @author edubecks
 */
public class TableItem extends AbstractItem implements LegendItem {

    public static final String LEGEND_TYPE = "Table Legend";
    //data
    public static final String TABLE_VALUES = "table values";
    public static final String HORIZONTAL_LABELS = "horizontal labels";
    public static final String VERTICAL_LABELS = "vertical labels";
    public static final String LABELS_IDS = "labels ids";
    // colors
    public static final String COLOR_VALUES = "color values";
    public static final String COLOR_VERTICAL = "color vertical";
    public static final String COLOR_HORIZONTAL = "color horizontal";

    
    public enum VerticalPosition {

        UP(NbBundle.getMessage(LegendManager.class, "TableItem.position.up")),
        BOTTOM(NbBundle.getMessage(LegendManager.class, "TableItem.position.bottom"));
        private final String position;

        private VerticalPosition(String position) {
            this.position = position;
        }

        @Override
        public String toString() {
            return this.position;
        }

    }
    
    public enum HorizontalPosition {

        RIGHT(NbBundle.getMessage(LegendManager.class, "TableItem.position.right")),
        LEFT(NbBundle.getMessage(LegendManager.class, "TableItem.position.left"));
        private final String position;

        private HorizontalPosition(String direction) {
            this.position = direction;
        }

        @Override
        public String toString() {
            return this.position;
        }

    }

    public enum VerticalTextDirection {

        // anti clockwise
        UP(-90d),
        DOWN(-90d),
        HORIZONTAL(0),
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
