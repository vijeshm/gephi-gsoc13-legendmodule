package org.gephi.legend.spi;

import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;
import org.openide.util.NbBundle;

/**
 * Basic Item interface for defining legends. It also defines many common
 * properties for all legends.
 *
 * @author edubecks
 */
public interface LegendItem extends Item {

    // builder
    public static final String CUSTOM_BUILDER = "builder";
    // renderer
    public static final String RENDERER = "renderer";
    // data
    public static final String LEGEND_ITEM = "legend item";
    public static final String DATA = "data";
    // index
    public static final String ITEM_INDEX = "item index";
    // location
    public static final String ORIGIN = "origin";
    // index
    public static final String PROPERTIES = "properties";
    public static final String OWN_PROPERTIES = "own properties";
    // type
    public static final String TYPE = "Legend Item";
    // description
    public static final String LABEL = "label";
    public static final String HAS_DYNAMIC_PROPERTIES = "has dynamic properties";
    public static final String DYNAMIC_PROPERTIES = "dynamic properties";
    public static final String NUMBER_OF_DYNAMIC_PROPERTIES = "number of dynamic properties";
    // mouse responsive
    public static final String IS_SELECTED = "is selected";
    public static final String IS_BEING_TRANSFORMED = "is being transformed";
    public static final String CURRENT_TRANSFORMATION = "current transformation";
    // anchor constants for all legend items
    public static final int TRANSFORMATION_ANCHOR_SIZE = 20;
    public static final int TRANSFORMATION_ANCHOR_LINE_THICK = 3;
    public static final float LEGEND_MIN_WIDTH = 50;
    public static final float LEGEND_MIN_HEIGHT = 50;

    public interface DynamicItem {

        /**
         * If the type of Legend has dynamic PreviewProperty it returns it
         *
         * @return an array of the dynamic PreviewProperty
         */
        public PreviewProperty[] getDynamicPreviewProperties();

        public void updateDynamicProperties(int numOfProperties);
    }

    public enum Direction {

        UP(0),
        DOWN(1),
        RIGHT(2),
        LEFT(3);
        private final Integer value;
        private final String[] labels = {
            NbBundle.getMessage(LegendItem.class, "LegendItem.direction.up"),
            NbBundle.getMessage(LegendItem.class, "LegendItem.direction.down"),
            NbBundle.getMessage(LegendItem.class, "LegendItem.direction.right"),
            NbBundle.getMessage(LegendItem.class, "LegendItem.direction.left")
        };

        private Direction(Integer value) {
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

    public enum Shape {

        RECTANGLE(0),
        CIRCLE(1),
        TRIANGLE(2);
        private final Integer value;
        private final String[] labels = {
            NbBundle.getMessage(LegendItem.class, "LegendItem.shape.rectangle"),
            NbBundle.getMessage(LegendItem.class, "LegendItem.shape.circle"),
            NbBundle.getMessage(LegendItem.class, "LegendItem.shape.triangle")
        };

        private Shape(Integer value) {
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

    public enum Alignment {

        LEFT(0),
        RIGHT(1),
        CENTER(2),
        JUSTIFIED(3);
        private final String[] labels = {
            NbBundle.getMessage(LegendItem.class, "LegendItem.text.alignment.left"),
            NbBundle.getMessage(LegendItem.class, "LegendItem.text.alignment.right"),
            NbBundle.getMessage(LegendItem.class, "LegendItem.text.alignment.center"),
            NbBundle.getMessage(LegendItem.class, "LegendItem.text.alignment.justified")
        };
        private final Integer value;

        private Alignment(Integer value) {
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
}