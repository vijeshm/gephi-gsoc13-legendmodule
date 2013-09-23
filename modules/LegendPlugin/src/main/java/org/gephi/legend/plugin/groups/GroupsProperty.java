package org.gephi.legend.plugin.groups;

import java.util.HashMap;
import java.util.Map;

/**
 * this class holds the properties associated with group legend.
 *
 * This is a singleton class.
 *
 * @author mvvijesh, edubecks
 */
public class GroupsProperty {

    public static final int GROUPS_SHAPE = 0;
    public static final int GROUPS_SHAPE_WIDTH_FRACTION = 1;
    public static final int GROUPS_LABEL_POSITION = 2;
    public static final int GROUPS_LABEL_FONT = 3;
    public static final int GROUPS_LABEL_FONT_COLOR = 4;
    public static final int GROUPS_LABEL_FONT_ALIGNMENT = 5;
    public static final int GROUPS_PADDING_BETWEEN_TEXT_AND_SHAPE = 6;
    public static final int GROUPS_PADDING_BETWEEN_ELEMENTS = 7;
    public static final String[] OWN_PROPERTIES = {
        ".shape",
        ".shape.width.fraction",
        ".label.position",
        ".label.font",
        ".label.font.color",
        ".label.font.alignment",
        ".paddingBetweenTextAndShape",
        ".paddingBetweenElements",};
    public static final int[] LIST_OF_PROPERTIES = {
        GROUPS_SHAPE,
        GROUPS_SHAPE_WIDTH_FRACTION,
        GROUPS_LABEL_POSITION,
        GROUPS_LABEL_FONT,
        GROUPS_LABEL_FONT_COLOR,
        GROUPS_LABEL_FONT_ALIGNMENT,
        GROUPS_PADDING_BETWEEN_TEXT_AND_SHAPE,
        GROUPS_PADDING_BETWEEN_ELEMENTS,};
    private static GroupsProperty instance;
    private Map<String, Integer> propertyIndex;

    public int getProperty(String propertyName) {
        return propertyIndex.get(propertyName);
    }

    private GroupsProperty() {
        propertyIndex = new HashMap<String, Integer>();
        for (int i = 0; i < OWN_PROPERTIES.length; i++) {
            propertyIndex.put(OWN_PROPERTIES[i], i);
        }
    }

    public static GroupsProperty getInstance() {
        if (instance == null) {
            instance = new GroupsProperty();
        }
        return instance;
    }
}