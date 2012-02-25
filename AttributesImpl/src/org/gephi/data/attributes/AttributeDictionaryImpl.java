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
public final class AttributeDictionaryImpl implements AttributeDictionary {

    private AttributeColumn column;
    private Map<Object, Set<Attributable>> map;
    private Set<Attributable> nullSet;
    private boolean sortableColumn;

    public AttributeDictionaryImpl(AttributeColumn column) {
        this.column = column;
        this.nullSet = new HashSet<Attributable>();
        buildMap();
    }

    private void buildMap() {
        if (AttributeUtils.getDefault().isNumberColumn(column)) {
            //Column type is sortable:
            map = new TreeMap<Object, Set<Attributable>>();
            sortableColumn = true;
        } else {
            //Column type is not sortable:
            map = new HashMap<Object, Set<Attributable>>();
            sortableColumn = false;
        }
    }

    private Set<Attributable> getValueSet(Object value) {
        if (value == null) {
            return nullSet;
        }
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            Set<Attributable> set = new HashSet<Attributable>();
            map.put(value, set);
            return set;
        }
    }

    public synchronized void putValue(AttributeValue value) {
        Object objVal = value.getValue();
        Set<Attributable> set = getValueSet(objVal);
        if(!set.add(value.getSource())){
            //System.out.println("false");
        }
    }

    public synchronized void removeValue(AttributeValue value) {
        Object objVal = value.getValue();
        Set<Attributable> set = getValueSet(objVal);
        set.remove(value.getSource());
        if (set.isEmpty() && objVal != null) {
            map.remove(objVal);
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
            return nullSet.size();
        }
        if (map.containsKey(value)) {
            return map.get(value).size();
        } else {
            return 0;
        }
    }

    public synchronized Set<Attributable> getValueRows(Object value) {
        Set<Attributable> set;
        if (value == null) {
            set = nullSet;
        } else {
            set = map.get(value);
        }
        return set != null ? Collections.unmodifiableSet(set) : null;
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
}
