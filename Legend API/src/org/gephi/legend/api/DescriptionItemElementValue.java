/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

/**
 *
 * @author edubecks
 */
public abstract class DescriptionItemElementValue {
    
    public abstract String getValue();
    
    public abstract String getTitle();
    
    public abstract String getDescription();
    
    @Override
    public String toString(){
        return getTitle();
    }
    
    
    
}
