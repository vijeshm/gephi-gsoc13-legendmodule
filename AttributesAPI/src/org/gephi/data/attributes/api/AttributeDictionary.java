/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.data.attributes.api;

import java.util.Set;
import org.gephi.graph.api.Attributable;

/**
 *
 * @author Eduardo Ramos<eduramiba@gmail.com>
 */
public interface AttributeDictionary {

    public void putValue(AttributeValue value);

    public void removeValue(AttributeValue value);

    public void replaceValue(AttributeValue oldValue, AttributeValue newValue);

    public Set<Object> getValues();

    public int getValueFrequency(Object value);

    public Set<Attributable> getValueRows(Object value);

    public Object getMinValue();

    public Object getMaxValue();

    public AttributeColumn getColumn();

    public boolean isSortableColumn();
}