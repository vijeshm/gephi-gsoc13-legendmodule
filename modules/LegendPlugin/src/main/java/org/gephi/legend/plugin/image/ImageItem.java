package org.gephi.legend.plugin.image;

import java.awt.Rectangle;
import org.gephi.legend.api.AbstractItem;
import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.spi.LegendItem;
import static org.gephi.legend.spi.LegendItem.PROPERTIES;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperty;

/**
 * the item model for the image legend.
 *
 * @author mvvijesh, edubecks
 */
public class ImageItem extends AbstractItem implements LegendItem, Item.BoundingBoxProvidingItem {

    public static final String IMAGE_URL = "Image";
    public static final String LEGEND_TYPE = "Image Item";

    public ImageItem(Object source) {
        super(source, LEGEND_TYPE);
    }

    @Override
    public String toString() {
        return (((PreviewProperty[]) this.getData(LegendItem.PROPERTIES))[LegendProperty.LABEL].getValue());
    }

    /**
     *
     * @return a rectangle that acts as a bounding box for the image legend.
     */
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