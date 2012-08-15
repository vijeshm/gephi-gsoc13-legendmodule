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
    public static final String NUMBER_OF_LABELS = "number of labels";
    public static final String NUMBER_OF_ROWS = "number of rows";
    public static final String NUMBER_OF_COLUMNS = "number of columns";
    // colors
    public static final String COLOR_VALUES = "color values";
    public static final String COLOR_VERTICAL = "color vertical";
    public static final String COLOR_HORIZONTAL = "color horizontal";

    public enum Labels {

        HORIZONTAL(0),
        VERTICAL(1);
        private final Integer value;

        private Labels(Integer value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value.toString();
        }

    }

    public enum VerticalPosition {

        UP(0),
        BOTTOM(1);
        private final Integer value;
        private final String[] labels = {
            NbBundle.getMessage(LegendManager.class, "TableItem.position.up"),
            NbBundle.getMessage(LegendManager.class, "TableItem.position.bottom")
        };

        private VerticalPosition(Integer value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return labels[value];
        }

        public String getValue() {
            return value.toString();
        }

    }

    public enum HorizontalPosition {

        RIGHT(0),
        LEFT(1);
        private final Integer value;
        private final String[] labels = {
            NbBundle.getMessage(LegendManager.class, "TableItem.position.right"),
            NbBundle.getMessage(LegendManager.class, "TableItem.position.left")
        };

        private HorizontalPosition(Integer value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return labels[value];
        }

        public String getValue() {
            return value.toString();
        }

    }

    public enum VerticalTextDirection {

        // anti clockwise
        UP(0, -90d),
        HORIZONTAL(1, 0),
        DIAGONAL(2, -45d),
        DOWN(3, -90d);
        private final double rotationAngle;
        private final Integer value;
        private final String[] labels = {
            NbBundle.getMessage(LegendManager.class, "TableItem.rotation.up"),
            NbBundle.getMessage(LegendManager.class, "TableItem.rotation.horizontal"),
            NbBundle.getMessage(LegendManager.class, "TableItem.rotation.diagonal"),
            NbBundle.getMessage(LegendManager.class, "TableItem.rotation.down")
        };

        private VerticalTextDirection(Integer value, double rotationAngle) {
            this.rotationAngle = rotationAngle;
            this.value = value;
        }

        @Override
        public String toString() {
            return labels[value];
        }

        public String getValue() {
            return value.toString();
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
