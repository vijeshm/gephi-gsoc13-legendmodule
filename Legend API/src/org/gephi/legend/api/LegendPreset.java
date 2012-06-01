/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author edubecks
 */

public abstract class LegendPreset implements Comparable<LegendPreset> {

    protected final Map<String, Object> properties;
    protected final String name;

    public LegendPreset(String name) {
        properties = new HashMap<String, Object>();
        this.name = name;
    }

    public LegendPreset(String name, Map<String, Object> propertiesMap) {
        properties = propertiesMap;
        this.name = name;
    }

    /**
     * Returns a read-only map of all properties
     *
     * @return a read-only map of all properties
     */
    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(LegendPreset o) {
        return o.name.compareTo(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj != null && obj instanceof LegendPreset) {
            LegendPreset p = (LegendPreset) obj;
            if (p.name.equals(name) && p.properties.equals(properties)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.properties != null ? this.properties.hashCode() : 0);
        hash = 13 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
