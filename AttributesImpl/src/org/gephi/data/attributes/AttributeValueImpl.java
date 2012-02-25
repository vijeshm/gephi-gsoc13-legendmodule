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

import org.gephi.data.attributes.api.AttributeOrigin;
import org.gephi.data.attributes.api.AttributeRow;
import org.gephi.data.attributes.api.AttributeValue;
import org.gephi.data.attributes.spi.AttributeValueDelegateProvider;
import org.gephi.graph.api.Attributable;

/**
 *
 * @author Mathieu Bastian
 * @author Martin Škurla
 */
public final class AttributeValueImpl implements AttributeValue {

    private final AttributeColumnImpl column;
    private final AttributeRow row;
    private final Object value;

    public AttributeValueImpl(AttributeColumnImpl column, AttributeRow row, Object value) {
        this.column = column;
        this.row = row;
        this.value = value;
    }

    public AttributeColumnImpl getColumn() {
        return column;
    }

    public AttributeRow getRow() {
        return row;
    }
    
    public Attributable getSource(){
        return row.getSource();
    }

    public Object getValue() {
        if (column.getOrigin() != AttributeOrigin.DELEGATE) {
            return value;
        } else {
            if (value == null) {
                return null;
            }

            AttributeValueDelegateProvider attributeValueDelegateProvider = column.getProvider();

            Object result;
            if (AttributeUtilsImpl.getDefault().isEdgeColumn(column)) {
                result = attributeValueDelegateProvider.getEdgeAttributeValue(value, column);
            } else if (AttributeUtilsImpl.getDefault().isNodeColumn(column)) {
                result = attributeValueDelegateProvider.getNodeAttributeValue(value, column);
            } else {
                throw new AssertionError();
            }

            // important for Neo4j and in future also for other storing engines
            // the conversion can be necessary because of types mismatch
            // for Neo4j return type can be array of primitive type which must be
            // converted into List type
            if (result.getClass().isArray()) {
                result = ListFactory.fromArray(result);
            }

            return result;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AttributeValueImpl other = (AttributeValueImpl) obj;
        if (this.column != other.column && (this.column == null || !this.column.equals(other.column))) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.column != null ? this.column.hashCode() : 0);
        hash = 29 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }
}
