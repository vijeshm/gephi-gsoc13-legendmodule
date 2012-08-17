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

    /**
     * Provides an user friendly value to be used at the Description Item Element
     * @return User friendly renderer name, not null
     */
    public abstract String getValue();

    /**
     * Provides an user friendly name for the Description Item This name will
     * appear in the description element list
     *
     * @return User friendly renderer name, not null
     */
    public abstract String getTitle();

    /**
     * Provides an user friendly description for the Description Item This name
     * will appear in the description element list
     *
     * @return User friendly renderer name, not null
     */
    public abstract String getDescription();

    @Override
    public String toString() {
        return getTitle();
    }

}
