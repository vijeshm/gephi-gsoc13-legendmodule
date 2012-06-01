/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.properties;

import org.gephi.legend.api.LegendProperty;

/**
 *
 * @author edubecks
 */
public class TextProperty extends LegendProperty{
    
    //DIMENSIONS
    //BODY
    public static final String BODY_FONT_SIZE = "body font size";
    public static final String BODY_FONT_TYPE = "body font type";
    public static final String BODY_FONT_STYLE = "body font style";
    
    public TextProperty(Object source, String name, Class type, String displayName, String description, String category, String... dependantProperties) {
        super(source, name, type, displayName, description, category, dependantProperties);
    }
    
    
    /**
     * Create a new preview property. The <code>name</code> should be unique. If
     * the type is different from basic types (Integer, Float, Double, String, 
     * Boolean or Color) make sure to implement a {@link PropertyEditor} and register it:
     * <pre>PropertyEditorManager.registerEditor(MyType.class, MyTypePropertyEditor.class);</pre>
     * The category can be one of the default categories:
     * <ul><li>LegendProperty.CATEGORY_NODES</li>
     * <li>LegendProperty.CATEGORY_EDGES</li>
     * <li>LegendProperty.CATEGORY_NODE_LABELS</li>
     * <li>LegendProperty.CATEGORY_EDGE_LABELS</li>
     * <li>LegendProperty.CATEGORY_EDGE_ARROWS</li></ul>
     * The <code>dependantProperties</code> list is used to automatically disable
     * the property if the dependant property is not selected. The dependant properties
     * need to be <code>Boolean</code> type.
     * @param source the property source, for instance the renderer
     * @param name the property's name
     * @param type the property's value type
     * @param displayName the property's display name
     * @param description the property's description
     * @param category the property's category
     * @param dependantProperties a list of boolean properties this property depend on
     * @return a new preview property
     */
    public static TextProperty createProperty(Object source, String name, Class type, String displayName, String description, String category, String... dependantProperties) {
        return new TextProperty(source, name, type, displayName, description, category, dependantProperties);
    }
    
    
}
