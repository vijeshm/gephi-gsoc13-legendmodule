/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.data.attributes;

import java.util.*;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeDictionary;
import org.gephi.data.attributes.api.AttributeUtils;
import org.gephi.data.attributes.api.AttributeValue;
import org.gephi.graph.api.Attributable;

/**
 *
 * @author Eduardo Ramos<eduramiba@gmail.com>
 */
public final class FrequencyOnlyAttributeDictionaryImpl implements AttributeDictionary {

    private AttributeColumn column;
    private Map<Object, Integer> map;
    private int nullFrequency;
    private boolean sortableColumn;

    public FrequencyOnlyAttributeDictionaryImpl(AttributeColumn column) {
        this.column = column;
        this.nullFrequency = 0;
        buildMap();
    }

    private void buildMap() {
        if (AttributeUtils.getDefault().isNumberColumn(column)) {
            //Column type is sortable:
            map = new TreeMap<Object, Integer>();
            sortableColumn = true;
        } else {
            //Column type is not sortable:
            map = new HashMap<Object, Integer>();
            sortableColumn = false;
        }
    }

    public synchronized void putValue(AttributeValue value) {
        Object objVal = value.getValue();
        if (objVal == null) {
            nullFrequency++;
        } else {
            Integer count = getValueFrequency(objVal) + 1;
            map.put(objVal, count);
        }
    }

    public synchronized void removeValue(AttributeValue value) {
        Object objVal = value.getValue();
        if (objVal == null && nullFrequency > 0) {
            nullFrequency--;
        } else {
            Integer count = getValueFrequency(objVal) - 1;
            if (count > 0) {
                map.put(objVal, count);
            } else {
                map.remove(objVal);
            }
        }
    }

    public void replaceValue(AttributeValue oldValue, AttributeValue newValue) {
        removeValue(oldValue);
        putValue(newValue);
    }

    public synchronized Set<Object> getValues() {
        return Collections.unmodifiableSet(map.keySet());
    }

    public synchronized int getValueFrequency(Object value) {
        if (value == null) {
            return nullFrequency;
        }
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            return 0;
        }
    }

    public synchronized Object getMinValue() {
        if (sortableColumn) {
            if (map.isEmpty()) {
                return null;
            } else {
                return ((TreeMap) map).firstKey();
            }
        } else {
            throw new UnsupportedOperationException(column.getTitle() + " is not a sortable column.");
        }
    }

    public synchronized Object getMaxValue() {
        if (sortableColumn) {
            if (map.isEmpty()) {
                return null;
            } else {
                return ((TreeMap) map).lastKey();
            }
        } else {
            throw new UnsupportedOperationException(column.getTitle() + " is not a sortable column.");
        }
    }

    public boolean isSortableColumn() {
        return sortableColumn;
    }

    public AttributeColumn getColumn() {
        return column;
    }

    public Set<Attributable> getValueRows(Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
