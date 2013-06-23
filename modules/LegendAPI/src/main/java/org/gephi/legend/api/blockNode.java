/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.util.ArrayList;
import org.gephi.legend.inplaceeditor.inplaceEditor;

/**
 *
 * @author mvvijesh
 */
public class blockNode {

    private float originX;
    private float originY;
    private float blockWidth;
    private float blockHeight;
    private blockNode parent;
    private ArrayList<blockNode> children;
    private inplaceEditor IPEditor;

    public blockNode(blockNode parentNode, float x, float y, float width, float height) {
        originX = x;
        originY = y;
        blockWidth = width;
        blockHeight = height;
        parent = parentNode;
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
        if (children.size() != 0) {
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

    public ArrayList<blockNode> getChildren() {
        return children;
    }

    public inplaceEditor getInplaceEditor() {
        return IPEditor;
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

    public void addChild(float x, float y, int width, int height) {
        blockNode child = new blockNode(this, x, y, width, height);
        children.add(child);
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

    public void renderIPEditor() {
    }
}
