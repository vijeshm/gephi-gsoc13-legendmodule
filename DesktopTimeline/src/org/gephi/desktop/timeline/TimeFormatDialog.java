/*
Copyright 2008-2012 Gephi
Authors : Sébastien Heymann
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
package org.gephi.desktop.timeline;

import org.gephi.dynamic.api.DynamicController;
import org.gephi.dynamic.api.DynamicModel;
import org.gephi.timeline.api.TimelineModel;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author Sébastien Heymann
 */
public class TimeFormatDialog extends javax.swing.JPanel {

    private DynamicController dynamicController;

    /**
     * Creates new form DateFormatDialog
     */
    public TimeFormatDialog() {
        initComponents();
    }

    public void setup(TimelineModel model) {
        this.dynamicController = Lookup.getDefault().lookup(DynamicController.class);

        if (dynamicController.getModel().getTimeFormat().equals(DynamicModel.TimeFormat.DOUBLE)) {
            numericRadio.setSelected(true);
        } else {
            dateRadio.setSelected(true);
        }
    }

    public void unsetup() {
        dynamicController.setTimeFormat(dateRadio.isSelected() ? DynamicModel.TimeFormat.DATE : DynamicModel.TimeFormat.DOUBLE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        headerTitle = new org.jdesktop.swingx.JXHeader();
        dateRadio = new javax.swing.JRadioButton();
        numericRadio = new javax.swing.JRadioButton();

        headerTitle.setDescription(NbBundle.getMessage (TimelineTopComponent.class, "TimeFormatDialog.headerTitle.description")); // NOI18N
        headerTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gephi/desktop/timeline/resources/time_format.png"))); // NOI18N
        headerTitle.setTitle(NbBundle.getMessage (TimelineTopComponent.class, "TimeFormatDialog.headerTitle.title")); // NOI18N

        buttonGroup.add(dateRadio);
        dateRadio.setText(NbBundle.getMessage (TimelineTopComponent.class, "TimeFormatDialog.dateRadio.text")); // NOI18N

        buttonGroup.add(numericRadio);
        numericRadio.setText(NbBundle.getMessage (TimelineTopComponent.class, "TimeFormatDialog.numericRadio.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(numericRadio)
                .addGap(18, 18, 18)
                .addComponent(dateRadio)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numericRadio)
                    .addComponent(dateRadio))
                .addGap(0, 22, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JRadioButton dateRadio;
    private org.jdesktop.swingx.JXHeader headerTitle;
    private javax.swing.JRadioButton numericRadio;
    // End of variables declaration//GEN-END:variables
}
