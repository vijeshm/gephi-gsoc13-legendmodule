/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.plugin.text;

import java.awt.Rectangle;
import org.gephi.legend.api.AbstractItem;
import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.spi.LegendItem;
import static org.gephi.legend.spi.LegendItem.PROPERTIES;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;

/**
 *
 * @author mvvijesh
 */
public class TextItem extends AbstractItem implements LegendItem, Item.BoundingBoxProvidingItem {
    //BODY

    public static final String BODY = "Body";
    public static final String LEGEND_TYPE = "Text Item";

    public TextItem(Object source) {
        super(source, LEGEND_TYPE);
    }

    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[LegendProperty.LABEL].getValue());
    }

    @Override
    public Rectangle getBoundingBox() {
        PreviewProperty[] ownProperties = this.getData(PROPERTIES);
        float originX = ownProperties[LegendProperty.USER_ORIGIN_X].getValue();
        float originY = ownProperties[LegendProperty.USER_ORIGIN_Y].getValue();
        float width = ownProperties[LegendProperty.WIDTH].getValue();
        float height = ownProperties[LegendProperty.HEIGHT].getValue();
        return new Rectangle((int) originX, (int) originY, (int) width, (int) height);
    }
}