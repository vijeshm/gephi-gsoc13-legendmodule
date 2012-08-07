/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

/**
 *
 * @author edubecks
 */
public abstract class CustomLegendItemBuilder {
    
    /**
     * 
     * @return the Description of the custom Legend builder used
     */
    public abstract String getDescription();
    
    /**
     * 
     * @return the title of the custom Legend builder used
     */
    public abstract String getTitle();
    
    /**
     * Used to determine if the
     * <code>builder</code> can create a new item <br/>
     * Ex: GroupsItem can only be
     * built if some partition was applied first
     *
     * @return <code>true</code> if <code>item</code> has dynamic properties, 
     * <code>false</code> otherwise
     */
    public abstract boolean isAvailableToBuild();

    /**
     * if the value of <code> isAvailableToBuild()</code> is true, it means 
     * that some steps are required in order to use the current Legend Item
     * Builder, then this function returns a description of the needed steps
     * @return 
     */
    public abstract String stepsNeededToBuild();
    
    public static final String NONE_NEEDED="";
    public static final String DEFAULT_TITLE="Default";
    
    @Override
    public String toString(){
        return getTitle();
    }
    
}
