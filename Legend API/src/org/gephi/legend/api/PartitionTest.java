package org.gephi.legend.api;

import javax.swing.Icon;
import org.gephi.datalab.spi.ManipulatorUI;
import org.gephi.datalab.spi.general.PluginGeneralActionsManipulator;
import org.gephi.graph.api.Node;
import org.gephi.partition.api.*;
import org.gephi.ui.components.SimpleHTMLReport;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * Example usage of partition API to retrieve data about the partition applied.
 * We check that the user has a node partition selected, and show the partition data in a report.
 * @author Eduardo Ramos<eduramiba@gmail.com>
 */
@ServiceProvider(service = PluginGeneralActionsManipulator.class)
public class PartitionTest implements PluginGeneralActionsManipulator {

    @Override
    public void execute() {
        PartitionController pc = Lookup.getDefault().lookup(PartitionController.class);
        StringBuilder sb = new StringBuilder();
        if (pc.getModel().getSelectedPartition() != null && pc.getModel().getSelectedPartitioning() == PartitionModel.NODE_PARTITIONING) {
            NodePartition nodesPartition = (NodePartition) pc.getModel().getSelectedPartition();
            sb.append("Column: ");
            sb.append(nodesPartition.getColumn().getTitle());
            sb.append("\n\n");

            sb.append("Parts:");
            for (Part<Node> part : nodesPartition.getParts()) {
                sb.append("  Percentage: ");
                sb.append(part.getPercentage());
                sb.append('\n');
                sb.append("  Color: ");
                sb.append(part.getColor());
                sb.append('\n');
                sb.append("  Value: ");
                sb.append(part.getValue());
                sb.append('\n');

                sb.append('\n');
            }
        }else{
            sb.append("No nodes partition is selected\n");
        }
        SimpleHTMLReport report = new SimpleHTMLReport(null, sb.toString());
    }

    @Override
    public String getName() {
        return "Get partition data";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean canExecute() {
        return true;
    }

    @Override
    public ManipulatorUI getUI() {
        return null;
    }

    @Override
    public int getType() {
        return 100;
    }

    @Override
    public int getPosition() {
        return 0;
    }

    @Override
    public Icon getIcon() {
        return null;
    }
}
