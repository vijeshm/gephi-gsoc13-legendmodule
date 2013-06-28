package org.gephi.legend.mouse;

import java.awt.Graphics2D;
import java.util.ArrayList;
import org.gephi.desktop.preview.PreviewTopComponent;
import org.gephi.legend.api.LegendController;
import org.gephi.legend.api.LegendModel;
import org.gephi.legend.api.LegendProperty;
import org.gephi.legend.api.blockNode;
import org.gephi.legend.inplaceeditor.inplaceEditor;
import org.gephi.legend.inplaceeditor.inplaceItemBuilder;
import org.gephi.legend.spi.LegendItem;
import static org.gephi.legend.spi.LegendItem.LEGEND_MIN_HEIGHT;
import static org.gephi.legend.spi.LegendItem.LEGEND_MIN_WIDTH;
import static org.gephi.legend.spi.LegendItem.TRANSFORMATION_ANCHOR_SIZE;
import org.gephi.preview.G2DRenderTargetBuilder;
import org.gephi.preview.PreviewControllerImpl;
import org.gephi.preview.PreviewModelImpl;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Eduardo Ramos<eduramiba@gmail.com>
 */
@ServiceProvider(service = PreviewMouseListener.class)
public class LegendMouseListener implements PreviewMouseListener {

    private float relativeX;
    private float relativeY;
    private float relativeAnchorX;
    private float relativeAnchorY;
    private int clickedAnchor;
    private final String TRANSFORMATION_SCALE_OPERATION = "Scale operation";
    private final String TRANSFORMATION_TRANSLATE_OPERATION = "Translate operation";

    private int isClickingInAnchor(int pointX, int pointY, Item item, PreviewProperties previewProperties) {
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
        float realOriginX = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
        float realOriginY = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
        int width = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
        int height = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));
        float[][] anchorLocations = {
            {
                -TRANSFORMATION_ANCHOR_SIZE / 2,
                -TRANSFORMATION_ANCHOR_SIZE / 2,
                TRANSFORMATION_ANCHOR_SIZE,
                TRANSFORMATION_ANCHOR_SIZE
            },
            {
                width - TRANSFORMATION_ANCHOR_SIZE / 2,
                -TRANSFORMATION_ANCHOR_SIZE / 2,
                TRANSFORMATION_ANCHOR_SIZE,
                TRANSFORMATION_ANCHOR_SIZE
            },
            {
                -TRANSFORMATION_ANCHOR_SIZE / 2,
                height - TRANSFORMATION_ANCHOR_SIZE / 2,
                TRANSFORMATION_ANCHOR_SIZE,
                TRANSFORMATION_ANCHOR_SIZE
            },
            {
                width - TRANSFORMATION_ANCHOR_SIZE / 2,
                height - TRANSFORMATION_ANCHOR_SIZE / 2,
                TRANSFORMATION_ANCHOR_SIZE,
                TRANSFORMATION_ANCHOR_SIZE
            }
        };

        pointX -= realOriginX;
        pointY -= realOriginY;

        for (int i = 0; i < anchorLocations.length; i++) {
            if ((pointX >= anchorLocations[i][0]
                    && pointX < (anchorLocations[i][0] + anchorLocations[i][2]))
                    && pointY >= anchorLocations[i][1]
                    && pointY < (anchorLocations[i][1] + anchorLocations[i][3])) {
                return i;
            }
        }
        return -1;
    }

    private boolean isClickingInLegend(int pointX, int pointY, Item item, PreviewProperties previewProperties) {
        Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);

        int borderThickness = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.BORDER_LINE_THICK));
        float realOriginX = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
        float realOriginY = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
        int width = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
        int height = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));

        /*
        // adjustment in order to include border as a part of the legend as well
        realOriginX = realOriginX - borderThickness;
        realOriginY = realOriginY - borderThickness;
        width = width + 2 * borderThickness;
        height = height + 2 * borderThickness;
        */

        if ((pointX >= realOriginX && pointX < (realOriginX + width))
                && (pointY >= realOriginY && pointY < (realOriginY + height))) {
            return true;
        }
        return false;
    }

    @Override
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        // when mouse is clicked on the canvas, any of the following can take place.
        // the click can be outside any legend. In this case, all the items must be deselected.
        // the click can be within the coordinates of a unique legend (including the anchor overlap). In this case, only the unique legend must be selected.
        // the click can be within the overlapping area of two or more legends. In this case, the legend on the top must be selected.

        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        legendModel.setInplaceEditor(null); // clear the inplace editor before rendering. this is needed to disable the inplace editor from showing.
        ArrayList<Item> items = legendModel.getActiveItems();

        // deselect all legends
        for (Item item : items) {
            item.setData(LegendItem.IS_SELECTED, Boolean.FALSE);
        }

        // starting from the last legend (topmost legend layer), select the legend for which the click within its boundaries.
        // it is important to break out of the loop to avoid the possibility of multiple selection in case of overlapping legends.
        for (int i = items.size() - 1; i >= 0; i--) {
            if (isClickingInLegend(event.x, event.y, items.get(i), previewProperties) || isClickingInAnchor(event.x, event.y, items.get(i), previewProperties) >= 0) {
                items.get(i).setData(LegendItem.IS_SELECTED, Boolean.TRUE);
                legendController.selectItem(items.get(i));

                blockNode root = legendModel.getBlockTree((Integer) items.get(i).getData(LegendItem.ITEM_INDEX));
                blockNode clickedBlock = root.getClickedBlock(event.x, event.y);
                legendModel.setInplaceEditor(clickedBlock.getInplaceEditor());
                break;
            }
        }

        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        previewController.refreshPreview();
        event.setConsumed(true);
    }

    @Override
    public void mousePressed(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        mouseClicked(event, previewProperties, workspace); //Update selected legend as if the press was a click.

        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        Item item = legendModel.getSelectedItem();

        if (item != null) {
            Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
            float realOriginX = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
            float realOriginY = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
            int width = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
            int height = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));

            clickedAnchor = isClickingInAnchor(event.x, event.y, item, previewProperties);
            if (clickedAnchor != -1) {
                relativeX = event.x - realOriginX;
                relativeY = event.y - realOriginY;
                switch (clickedAnchor) {
                    // Top Left Anchor
                    case 0: {
                        relativeAnchorX = relativeX;
                        relativeAnchorY = relativeY;
                        break;
                    }
                    // Top Right
                    case 1: {
                        relativeAnchorX = relativeX - width;
                        relativeAnchorY = relativeY;
                        break;
                    }
                    // Bottom Left
                    case 2: {
                        relativeAnchorX = relativeX;
                        relativeAnchorY = relativeY - height;
                        break;
                    }
                    // Bottom Right
                    case 3: {
                        relativeAnchorX = relativeX - width;
                        relativeAnchorY = relativeY - height;
                        break;
                    }
                }

                item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.TRUE);
                item.setData(LegendItem.CURRENT_TRANSFORMATION, TRANSFORMATION_SCALE_OPERATION);

                event.setConsumed(true);
                return;
            } else if (isClickingInLegend(event.x, event.y, item, previewProperties)) {
                relativeX = event.x - realOriginX;
                relativeY = event.y - realOriginY;

                item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.TRUE);
                item.setData(LegendItem.CURRENT_TRANSFORMATION, TRANSFORMATION_TRANSLATE_OPERATION);

                event.setConsumed(true);
                return;
            } else {
                item.setData(LegendItem.IS_SELECTED, Boolean.FALSE);
                item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.FALSE);
                relativeX = 0;
                relativeY = 0;
                legendController.selectItem(null);

                event.setConsumed(true);
                return;
            }
        }
    }

    @Override
    public void mouseDragged(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        Item item = legendModel.getSelectedItem();

        if (item != null) {
            Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
            Boolean isBeingTransformed = (Boolean) item.getData(LegendItem.IS_BEING_TRANSFORMED);
            if (isBeingTransformed) {
                String currentTransformation = item.getData(LegendItem.CURRENT_TRANSFORMATION);

                // SCALING
                if (currentTransformation.equals(TRANSFORMATION_SCALE_OPERATION)) {

                    float realOriginX = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
                    float realOriginY = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
                    int width = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
                    int height = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));

                    // the value of clickedAnchor would've been set by the the mousePressed method prior to the execution of this method.
                    if (clickedAnchor != -1) {
                        float newOriginX = realOriginX;
                        float newOriginY = realOriginY;
                        float newWidth = width;
                        float newHeight = height;

                        switch (clickedAnchor) {
                            // Top Left Anchor
                            case 0: {
                                newOriginX = event.x - relativeAnchorX;
                                newOriginY = event.y - relativeAnchorY;
                                newWidth = realOriginX + width - newOriginX;
                                newHeight = realOriginY + height - newOriginY;

                                // reset the newly computed height to min height, if it is less than the min height
                                if (newHeight < LEGEND_MIN_HEIGHT) {
                                    newHeight = LEGEND_MIN_HEIGHT;

                                    // originY must not be changed in case the height has reached its minimum. hence it is reset. Note that originX can still change.
                                    if (Math.abs(realOriginY - newOriginY) > TRANSFORMATION_ANCHOR_SIZE) {
                                        // to avoid large displacements of the origin when the mouse is dragged quickly
                                        newOriginY = (realOriginY + height) - LEGEND_MIN_HEIGHT;
                                    } else {
                                        newOriginY = realOriginY;
                                    }
                                }

                                // reset the newly computed width to min width, if it is less than the min width
                                if (newWidth < LEGEND_MIN_WIDTH) {
                                    newWidth = LEGEND_MIN_WIDTH;

                                    // originX must not be changed in case the width has reached its minimum. hence it is reset. Note that originY can still change.
                                    if (Math.abs(realOriginX - newOriginX) > TRANSFORMATION_ANCHOR_SIZE) {
                                        // to avoid large displacements of the origin when the mouse is dragged quickly
                                        newOriginX = (realOriginX + width) - LEGEND_MIN_WIDTH;
                                    } else {
                                        newOriginX = realOriginX;
                                    }
                                }

                                previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X), newOriginX);
                                previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y), newOriginY);
                                previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH), newWidth);
                                previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT), newHeight);
                                break;
                            }
                            // Top Right

                            case 1: {
                                newOriginX = realOriginX;
                                newOriginY = event.y - relativeAnchorY;
                                newWidth = event.x - relativeAnchorX - newOriginX;
                                newHeight = realOriginY + height - newOriginY;
                                if (newHeight >= LEGEND_MIN_HEIGHT) {
                                    previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y), newOriginY);
                                    previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT), newHeight);
                                }

                                if (newWidth >= LEGEND_MIN_WIDTH) {
                                    previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH), newWidth);
                                }
                                break;
                            }
                            // Bottom Left
                            case 2: {
                                newOriginX = event.x - relativeAnchorX;
                                newOriginY = realOriginY;
                                newWidth = realOriginX + width - newOriginX;
                                newHeight = event.y - realOriginY - relativeAnchorY;
                                if (newWidth >= LEGEND_MIN_WIDTH) {
                                    previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X), newOriginX);
                                    previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH), newWidth);
                                }
                                if (newHeight >= LEGEND_MIN_HEIGHT) {
                                    previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT), newHeight);
                                }
                                break;
                            }
                            // Bottom Right
                            case 3: {
                                newOriginX = realOriginX;
                                newOriginY = realOriginY;
                                newWidth = event.x - relativeAnchorX - newOriginX;
                                newHeight = event.y - relativeAnchorY - newOriginY;
                                if (newWidth >= LEGEND_MIN_WIDTH) {
                                    previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH), newWidth);
                                }
                                if (newHeight >= LEGEND_MIN_HEIGHT) {
                                    previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT), newHeight);
                                }
                                break;
                            }
                        }

                        // root.updateGeometry(newOriginX - realOriginX, newOriginY - realOriginY, newWidth / width, newHeight / height);

                        // change for key event
                        boolean isCtrlKeyPressed = event.keyEvent != null && event.keyEvent.isControlDown();
                        if (isCtrlKeyPressed) {
                            float scaleWidth = newWidth / width;
                            float scaleHeight = newHeight / height;
                            float scale = Math.min(scaleWidth, scaleHeight);
                            newWidth = width * scale;
                            newHeight = height * scale;
                        }

                        event.setConsumed(true);
                    }
                } else if (currentTransformation.equals(TRANSFORMATION_TRANSLATE_OPERATION)) {
                    float newOriginX = event.x - relativeX;
                    float newOriginY = event.y - relativeY;

                    previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X), newOriginX);
                    previewProperties.putValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y), newOriginY);

                    event.setConsumed(true);
                }
            }
        }
    }

    @Override
    public void mouseReleased(PreviewMouseEvent event, PreviewProperties previewProperties, Workspace workspace) {
        LegendController legendController = LegendController.getInstance();
        LegendModel legendModel = legendController.getLegendModel();
        Item item = legendModel.getSelectedItem();
        if (item != null) {
            item.setData(LegendItem.IS_BEING_TRANSFORMED, Boolean.FALSE);

            Integer itemIndex = item.getData(LegendItem.ITEM_INDEX);
            float realOriginX = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_X));
            float realOriginY = previewProperties.getFloatValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.USER_ORIGIN_Y));
            int width = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.WIDTH));
            int height = previewProperties.getIntValue(LegendModel.getProperty(LegendProperty.LEGEND_PROPERTIES, itemIndex, LegendProperty.HEIGHT));

            blockNode root = legendModel.getBlockTree(itemIndex);
            // relativeX would've been set in mousePressed event using the formula: (event.x - realOriginX). Hence, to obtain the realOriginX, we need to pass (event.x - relativeX)
            // 3rd parameter is newWidth/oldWidth. 4th parameter is newHeight/oldHeight. Here, it is 1 because it is a translate operation.
            root.updateGeometry(realOriginX, realOriginY, width, height);

            // note that the canvas gets refreshed twice if the control is passed here through clicking
            PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
            previewController.refreshPreview();

            event.setConsumed(true);
            return;
        }

        event.setConsumed(false);
    }
}
