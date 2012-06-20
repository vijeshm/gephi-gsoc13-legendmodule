/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

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
    public static final String TYPE="Legend Item";
    public static final String SUB_TYPE="Legend Item";
    
    
    
    public static final String INDEX = "index";

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
