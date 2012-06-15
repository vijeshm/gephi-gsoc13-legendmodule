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
    public static final String WORKSPACE_INDEX = "work index";
    public static final String ITEM_INDEX = "item index";
    //LOCATION
    public static final String ORIGIN = "origin";
    //TITLE
    public static final String TITLE = "title";
    //DESCRIPTION
    public static final String DESCRIPTION = "description";
    //INDEX
    public static final String PROPERTIES = "properties";

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

        RECTANGLE("rectangle"),
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

        LEFT("left"),
        RIGHT("right"),
        CENTER("center");

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
