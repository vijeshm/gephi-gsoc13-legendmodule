/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.datalab.plugin.manipulators.columns;

import java.awt.Image;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.data.attributes.api.AttributeUtils;
import org.gephi.datalab.spi.columns.AttributeColumnsManipulator;
import org.gephi.datalab.spi.columns.AttributeColumnsManipulatorUI;
import org.gephi.graph.api.Attributable;
import org.gephi.graph.api.EdgeData;
import org.gephi.graph.api.NodeData;
import org.gephi.ui.components.SimpleHTMLReport;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Eduardo Ramos<eduramiba@gmail.com>
 */
@ServiceProvider(service = AttributeColumnsManipulator.class)
public class AttributeDictionaryTest implements AttributeColumnsManipulator {

    public void execute(AttributeTable table, AttributeColumn column) {
        boolean isNodeColumn = AttributeUtils.getDefault().isNodeColumn(column);
        int total = 0, freq;
        StringBuilder sb = new StringBuilder();
        if (column.isSortableColumn()) {
            sb.append("Min : ");
            sb.append(column.getMinValue());
            sb.append("\n");
            sb.append("Max : ");
            sb.append(column.getMaxValue());
            sb.append("\n\n");
        }
        //Null value:
        sb.append("null : ");
        freq = column.getValueFrequency(null);
        sb.append(freq);
        sb.append(" rows:\n");
        for (Attributable attributable : column.getValueRows(null)) {
            sb.append("    ");
            if (isNodeColumn) {
                sb.append(((NodeData) attributable).getId());
                sb.append(" - ");
                sb.append(((NodeData) attributable).getLabel());
            } else {
                sb.append(((EdgeData) attributable).getId());
                sb.append(" - ");
                sb.append(((EdgeData) attributable).getLabel());
            }
            sb.append("\n");
        }
        sb.append("\n");
        total+=freq;

        //Rest of values:
        for (Object value : column.getValues()) {
            sb.append(value.toString());
            sb.append(" : ");
            freq = column.getValueFrequency(value);
            total += freq;
            sb.append(freq);
            sb.append(" rows:\n");
            for (Attributable attributable : column.getValueRows(value)) {
                sb.append("    ");
                if (isNodeColumn) {
                    sb.append(((NodeData) attributable).getId());
                    sb.append(" - ");
                    sb.append(((NodeData) attributable).getLabel());
                } else {
                    sb.append(((EdgeData) attributable).getId());
                    sb.append(" - ");
                    sb.append(((EdgeData) attributable).getLabel());
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        sb.append("Total : ");
        sb.append(total);
        sb.append("\n\n");
        new SimpleHTMLReport(null, sb.toString());
    }

    public String getName() {
        return "AttributeDictionaryTest";
    }

    public String getDescription() {
        return null;
    }

    public boolean canManipulateColumn(AttributeTable table, AttributeColumn column) {
        return true;
    }

    public AttributeColumnsManipulatorUI getUI(AttributeTable table, AttributeColumn column) {
        return null;
    }

    public int getType() {
        return 600;
    }

    public int getPosition() {
        return 0;
    }

    public Image getIcon() {
        return null;
    }
}
