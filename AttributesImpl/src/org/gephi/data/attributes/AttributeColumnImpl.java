/*
 Copyright 2008-2010 Gephi
 Authors : Mathieu Bastian <mathieu.bastian@gephi.org>, Martin Škurla <bujacik@gmail.com>
 Website : http://www.gephi.org

 This file is part of Gephi.

 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2011 Gephi Consortium. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s):

 Portions Copyrighted 2011 Gephi Consortium.
 */
package org.gephi.data.attributes;

import java.util.Set;
import org.gephi.data.attributes.api.*;
import org.gephi.data.attributes.spi.AttributeValueDelegateProvider;
import org.gephi.graph.api.Attributable;

/**
 *
 * @author Mathieu Bastian
 * @author Martin Škurla
 */
public class AttributeColumnImpl implements AttributeColumn {

    protected final AttributeTableImpl table;
    protected int index;
    protected final String id;
    protected final String title;
    protected final AttributeType type;
    protected final AttributeOrigin origin;
    protected final Object defaultValue;
    protected final AttributeDictionary attributeDictionary;
    protected final AttributeValueDelegateProvider attributeValueDelegateProvider;

    public AttributeColumnImpl(AttributeTableImpl table, int index, String id, String title, AttributeType attributeType, AttributeOrigin origin, Object defaultValue, AttributeValueDelegateProvider attributeValueDelegateProvider) {
        this.table = table;
        this.index = index;
        this.id = id;
        this.type = attributeType;
        this.title = title;
        this.origin = origin;
        this.attributeDictionary = new AttributeDictionaryImpl(this);
        this.attributeValueDelegateProvider = attributeValueDelegateProvider;
        this.defaultValue = defaultValue;
    }

    public AttributeTableImpl getTable() {
        return table;
    }

    public AttributeType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public int getIndex() {
        return index;
    }

    public AttributeOrigin getOrigin() {
        return origin;
    }

    public String getId() {
        return id;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public AttributeValueDelegateProvider getProvider() {
        return attributeValueDelegateProvider;
    }

    protected void putValue(AttributeValue value) {
        attributeDictionary.putValue(value);
    }

    protected void removeValue(AttributeValue value) {
        attributeDictionary.removeValue(value);
    }

    public Set<Object> getValues() {
        return attributeDictionary.getValues();
    }

    public int getValueFrequency(Object value) {
        return attributeDictionary.getValueFrequency(value);
    }

    public Set<Attributable> getValueRows(Object value) {
        return attributeDictionary.getValueRows(value);
    }

    public Object getMinValue() {
        return attributeDictionary.getMinValue();
    }

    public Object getMaxValue() {
        return attributeDictionary.getMaxValue();
    }

    public boolean isSortableColumn() {
        return attributeDictionary.isSortableColumn();
    }

    @Override
    public String toString() {
        return title + " (" + type.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AttributeColumn) {
            AttributeColumnImpl o = (AttributeColumnImpl) obj;
            return id.equals(o.id) && o.type == type;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 53 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }
}
