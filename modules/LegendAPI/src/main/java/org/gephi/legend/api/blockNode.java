/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import org.gephi.legend.inplaceeditor.inplaceEditor;
import org.gephi.preview.api.Item;

/**
 *
 * @author mvvijesh
 */
public class blockNode {

    public static String ROOT = "root node";
    public static String TITLE = "title node";
    public static String DESC = "description node";
    public static String LEGEND = "legend node";
    private float originX;
    private float originY;
    private float blockWidth;
    private float blockHeight;
    private blockNode parent;
    private ArrayList<blockNode> children;
    private inplaceEditor IPEditor;
    private Item legendItem;
    private String id;

    public blockNode(blockNode parentNode, float x, float y, float width, float height, Item parentItem, String tag) {
        parent = parentNode;
        originX = x;
        originY = y;
        blockWidth = width;
        blockHeight = height;
        legendItem = parentItem;
        id = tag;
        children = new ArrayList<blockNode>();
        IPEditor = null;
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

    public blockNode getParent() {
        return parent;
    }

    public Item getItem() {
        return legendItem;
    }

    public ArrayList<blockNode> getChildren() {
        return children;
    }

    public inplaceEditor getInplaceEditor() {
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

    public void setInplaceEditor(inplaceEditor ipe) {
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

    public blockNode addChild(float x, float y, float width, float height, String tag) {
        blockNode child = new blockNode(this, x, y, width, height, legendItem, tag);
        return addChild(child);
    }
    
    public blockNode addChild(blockNode child) {
        children.add(child);
        return child;
    }

    public void removeChild(String tag) {
        for (blockNode child : children) {
            if (child.getTag() == tag) {
                children.remove(child);
                break;
            }
        }
    }

    public blockNode getChild(String tag) {
        for (blockNode child : children) {
            if (child.getTag() == tag) {
                return child;
            }
        }

        return null;
    }
    
    public void removeAllChildren() {
        children = new ArrayList<blockNode>();
    }

    public Boolean isClickInBlock(int x, int y) {
        return (x >= originX && x <= originX + blockWidth) && (y >= originY && y <= originY + blockHeight);
    }

    public blockNode getClickedBlock(int x, int y) {
        // trivial case: when the block is a leaf and the click takes place within the block, this is the block you're looking for.
        if (isLeaf() && isClickInBlock(x, y)) {
            return this;
        }

        // check if any of the block's children contains the click. If so, invoke this funciton in the child in order to bubble up the event.
        for (blockNode child : children) {
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
