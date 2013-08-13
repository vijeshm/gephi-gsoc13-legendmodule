/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.Item;

/**
 *
 * @author mvvijesh
 */
public class BlockNode {

    public static String ROOT = "root node";
    public static String TITLE = "title node";
    public static String DESC = "description node";
    public static String LEGEND = "legend node";
    private float originX;
    private float originY;
    private float blockWidth;
    private float blockHeight;
    private BlockNode parent;
    private ArrayList<BlockNode> children;
    private InplaceEditor IPEditor;
    private LegendItem legendItem;
    private String id;
    private Map<String, Object> data; //for optional extra information

    public BlockNode(BlockNode parentNode, float x, float y, float width, float height, Item parentItem, String tag) {
        parent = parentNode;
        originX = x;
        originY = y;
        blockWidth = width;
        blockHeight = height;
        legendItem = (LegendItem) parentItem;
        id = tag;
        children = new ArrayList<BlockNode>();
        IPEditor = null;
        data = new HashMap<String, Object>();
    }
    
    public <D> D getData(String key) {
        return (D) data.get(key);
    }

    public void setData(String key, Object value) {
        data.put(key, value);
    }

    public Boolean isRoot() {
        if (parent != null) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public Boolean isLeaf() {
        if (!children.isEmpty()) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public float getOriginX() {
        return originX;
    }

    public float getOriginY() {
        return originY;
    }

    public float getBlockWidth() {
        return blockWidth;
    }

    public float getBlockHeight() {
        return blockHeight;
    }

    public BlockNode getParent() {
        return parent;
    }

    public LegendItem getItem() {
        return legendItem;
    }

    public ArrayList<BlockNode> getChildren() {
        return children;
    }

    public InplaceEditor getInplaceEditor() {
        return IPEditor;
    }

    public String getTag() {
        return id;
    }

    public void setOriginX(float x) {
        originX = x;
    }

    public void setOriginY(float y) {
        originY = y;
    }

    public void setBlockWidth(float width) {
        blockWidth = width;
    }

    public void setBlockHeight(float height) {
        blockHeight = height;
    }

    public void setInplaceEditor(InplaceEditor ipe) {
        IPEditor = ipe;
    }

    public void updateGeometry(float newOriginX, float newOriginY, float newWidth, float newHeight) {
        originX = newOriginX;
        originY = newOriginY;
        blockWidth = newWidth;
        blockHeight = newHeight;

        /* PREVIOUS: Recursive implementation that bubbles down the children
         float offsetX = newOriginX - originX;
         float offsetY = newOriginY - originY;
         float widthRatio = newWidth / blockWidth;
         float heightRatio = newHeight / blockHeight;
         originX = newOriginX;
         originY = newOriginY;
         blockWidth = newWidth;
         blockHeight = newHeight;
         for (blockNode child : children) {
         float childOriginX = child.getOriginX();
         float childOriginY = child.getOriginY();
         float childBlockWidth = child.getBlockWidth();
         float childBlockHeight = child.getBlockHeight();
         child.updateGeometry(childOriginX + offsetX, childOriginY + offsetY, widthRatio * childBlockWidth, heightRatio * childBlockHeight);
         }
         */
    }

    public BlockNode addChild(float x, float y, float width, float height, String tag) {
        BlockNode child = new BlockNode(this, x, y, width, height, legendItem, tag);
        return addChild(child);
    }
    
    public BlockNode addChild(BlockNode child) {
        children.add(child);
        return child;
    }

    public void removeChild(String tag) {
        for (BlockNode child : children) {
            if (child.getTag() == tag) {
                children.remove(child);
                break;
            }
        }
    }

    public BlockNode getChild(String tag) {
        for (BlockNode child : children) {
            if (child.getTag() == tag) {
                return child;
            }
        }

        return null;
    }
    
    public void removeAllChildren() {
        children = new ArrayList<BlockNode>();
    }

    public Boolean isClickInBlock(int x, int y) {
        return (x >= originX && x <= originX + blockWidth) && (y >= originY && y <= originY + blockHeight);
    }

    public BlockNode getClickedBlock(int x, int y) {
        // trivial case: when the block is a leaf and the click takes place within the block, this is the block you're looking for.
        if (isLeaf() && isClickInBlock(x, y)) {
            return this;
        }

        // check if any of the block's children contains the click. If so, invoke this funciton in the child in order to bubble up the event.
        for (BlockNode child : children) {
            if (child.isClickInBlock(x, y)) {
                return child.getClickedBlock(x, y);
            }
        }

        // this is the case where a block has children and the block is clicked, but the click doesnt occur on any of the children. It takes place in the stray area.
        return this;
    }

    /*
     public void renderIPEditor() {
     IPEditor.render(originX, originY);
     }
     */
}
