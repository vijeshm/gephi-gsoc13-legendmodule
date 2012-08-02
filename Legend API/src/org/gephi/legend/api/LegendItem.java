/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import org.gephi.preview.api.PreviewProperty;

/**
 *
 * @author edubecks
 */
public interface LegendItem {

    //INDEX
    public static final String ITEM_INDEX = "item index";
    //LOCATION
    public static final String ORIGIN = "origin";
    //INDEX
    public static final String PROPERTIES = "properties";
    // TYPE 
    public static final String TYPE = "Legend Item";
    public static final String SUB_TYPE = "Legend Sub-type";
    // DESCRIPTION
    public static final String LABEL = "label";
    public static final String HAS_DYNAMIC_PROPERTIES = "has dynamic properties";
    public static final String DYNAMIC_PROPERTIES = "dynamic properties";
    public static final String NUMBER_OF_DYNAMIC_PROPERTIES = "number dynamic properties";
    // MOUSE RESPONSIVE
    public static final String IS_SELECTED = "is selected";
    public static final String IS_BEING_TRANSFORMED = "is being transformed";
    public static final String CURRENT_TRANSFORMATION = "current transformation";
    //ANCHOR CONSTANTS FOR ALL LEGEND ITEMS
    public static final int TRANSFORMATION_ANCHOR_SIZE = 20;
    public static final int TRANSFORMATION_ANCHOR_LINE_THICK = 3;
    public final float LEGEND_MIN_WIDTH = 50;
    public final float LEGEND_MIN_HEIGHT = 50;

    /**
     * If the type of Legend has dynamic PreviewProperty it returns it
     * @return an array of the dynamic PreviewProperty
     */
    public PreviewProperty[] getDynamicPreviewProperties();

    public enum Direction {

        UP("Up"),
        BOTTOM("Bottom"),
        RIGHT("Right"),
        LEFT("Left");
        private final String direction;

        private Direction(String direction) {
            this.direction = direction;
        }

        @Override
        public String toString() {
            return this.direction;
        }

    }

    public enum Shape {

        RECTANGLE("Rectangle"),
        CIRCLE("Circle"),
        TRIANGLE("Triangle");
        private final String shape;

        private Shape(String shape) {
            this.shape = shape;
        }

        @Override
        public String toString() {
            return this.shape;
        }

    }

    public enum Alignment {

        LEFT("Left"),
        RIGHT("Right"),
        CENTER("Center"),
        JUSTIFIED("Justified");
        private final String alignment;

        private Alignment(String alignment) {
            this.alignment = alignment;
        }

        @Override
        public String toString() {
            return this.alignment;
        }

    }
}
