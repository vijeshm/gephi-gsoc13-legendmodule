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
    
    @Override
    public String toString(){
        return getTitle();
    }
    
}
