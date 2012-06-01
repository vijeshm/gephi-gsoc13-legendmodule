/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

/**
 *
 * @author edubecks
 */
public class LegendProperty {
    
    //LOCATION
    public static final String ORIGIN = "origin";
    
    //TITLE
    public static final String TITLE_FONT_SIZE = "title-font-size";
    public static final String TITLE_FONT_TYPE = "title-font-type";
    public static final String TITLE_FONT_STYLE = "title-font-style";
    
    //DESCRIPTION
    public static final String DESCRIPTION_FONT_SIZE = "description-font-size";
    public static final String DESCRIPTION_FONT_TYPE = "description-font-type";
    public static final String DESCRIPTION_FONT_STYLE = "description-font-style";
    
    //Variables
    final String name;
    final String displayName;
    final String description;
    final Object source;
    final String category;
    final Class type;
    Object value;
    String[] dependencies = new String[0];
    
    
    
    
    LegendProperty(Object source, String name, Class type, String displayName, String description, String category) {
        this.source = source;
        this.name = name;
        this.type = type;
        this.displayName = displayName;
        this.description = description;
        this.category = category;
    }

    public LegendProperty(Object source, String name, Class type, String displayName, String description, String category, String[] dependencies) {
        this.source = source;
        this.name = name;
        this.type = type;
        this.displayName = displayName;
        this.description = description;
        this.category = category;
        this.dependencies = dependencies;
    }
    
    
    

    
    /**
     * Returns the property value.
     * @param <T> the return type
     * @return the property value or <code>null</code>
     */
    public <T> T getValue() {
        return (T) value;
    }

    /**
     * Sets this property value and return it. The value can be <code>null</code>.
     * @param value the value to be set
     * @return this property instance
     */
    public LegendProperty setValue(Object value) {
        this.value = value;
        return this;
    }

    /**
     * Returns the (unique) name of this property.
     * @return the property's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of this property.
     * @return this property's type
     */
    public Class getType() {
        return type;
    }

    /**
     * Returns the display name of this property or <code>null</code> if not set.
     * @return this property's display name or <code>null</code>
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the description of this property or <code>null</code> if not set.
     * @return this property's description or <code>null</code>
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the source object of this property.
     * @return this property's source object
     */
    public Object getSource() {
        return source;
    }

    /**
     * Returns the category of this property or <code>null</code> if not set.
     * @return this property's category or <code>null</code>
     */
    public String getCategory() {
        return category;
    }
    
}
