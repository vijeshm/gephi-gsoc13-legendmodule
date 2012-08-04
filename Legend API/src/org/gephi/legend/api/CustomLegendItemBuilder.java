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
    
    public abstract String getDescription();
    public abstract String getTitle();
    
    /**
     * Used to determine if the
     * <code>builder</code> can create a new item Ex: GroupsItem can only be
     * built if some partition was applied first
     *
     * @return <code>true</code> if <code>item</code> has dynamic      * properties, <code>false</code> otherwise
     */
    public abstract boolean isAvailableToBuild();

    public abstract String stepsNeededToBuild();
    
    public static final String NONE_NEEDED="";
    public static final String DEFAULT_TITLE="Default";
    
    @Override
    public String toString(){
        return getTitle();
    }
    
}
