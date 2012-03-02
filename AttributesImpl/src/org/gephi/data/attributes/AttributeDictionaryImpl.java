/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.data.attributes;

import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
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

    private static final boolean DEFAULT_TRIMMING_ENABLED = false;
    private static final int DEFAULT_TRIMMING_FREQUENCY = 30;
    private AttributeColumn column;
    private Map<Object, Set<Attributable>> map;
    private Set<Attributable> nullSet;
    private boolean sortableColumn;
    private boolean trimmingEnabled;
    private int trimmingFrequency;
    private int countDownToTrim;

    public AttributeDictionaryImpl(AttributeColumn column, boolean trimmingEnabled, int trimmingFrequency) {
        this.column = column;
        this.trimmingEnabled = trimmingEnabled;
        this.trimmingFrequency = trimmingFrequency;
        this.nullSet = new ObjectOpenHashSet<Attributable>();
        buildMap();
    }

    public AttributeDictionaryImpl(AttributeColumn column) {
        this(column, DEFAULT_TRIMMING_ENABLED, DEFAULT_TRIMMING_FREQUENCY);
    }

    private void buildMap() {
        sortableColumn = AttributeUtils.getDefault().isNumberColumn(column);
        if (sortableColumn) {
            //Column type is sortable:
            map = new Object2ObjectAVLTreeMap<Object, Set<Attributable>>();
            trimmingEnabled = false;//No need to trim TreeMap
        } else {
            //Column type is not sortable:
            map = new Object2ObjectOpenHashMap<Object, Set<Attributable>>();
            countDownToTrim = trimmingFrequency;
        }
    }

    private Set<Attributable> getValueSet(Object value) {
        if (value == null) {
            return nullSet;
        }
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            Set<Attributable> set = new ObjectOpenHashSet<Attributable>();
            map.put(value, set);
            return set;
        }
    }

    public synchronized void putValue(AttributeValue value) {
        Object objVal = value.getValue();
        Set<Attributable> set = getValueSet(objVal);
        set.add(value.getSource());
    }

    public synchronized void removeValue(AttributeValue value) {
        Object objVal = value.getValue();
        Set<Attributable> set = getValueSet(objVal);
        set.remove(value.getSource());
        if (set.isEmpty() && objVal != null) {
            map.remove(objVal);
            if (trimmingEnabled) {
                countDownToTrim--;
                if (countDownToTrim == 0) {
                    ((Object2ObjectOpenHashMap) map).trim();
                    countDownToTrim = trimmingFrequency;
                }
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
                return ((SortedMap) map).firstKey();
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
                return ((SortedMap) map).lastKey();
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

    public int getCountDownToTrim() {
        return countDownToTrim;
    }

    public boolean isTrimmingEnabled() {
        return trimmingEnabled;
    }

    public int getTrimmingFrequency() {
        return trimmingFrequency;
    }
}
