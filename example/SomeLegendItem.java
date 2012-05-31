package example;

import org.gephi.preview.plugin.items.AbstractItem;

public class SomeLegendItem extends AbstractItem{
    public static final String TYPE = "legend";

    public SomeLegendItem(Object source) {
        super(source, TYPE);
    }
}
