package org.gephi.legend.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.gephi.legend.inplaceeditor.InplaceEditor;
import org.gephi.legend.spi.LegendItem;
import org.gephi.preview.api.Item;

/**
 * A block node represents a rectangular region in the layout of a legend, along
 * with the contents and properties of the region.
 *
 * The legend layout is split into numerous rectangular blocks. These blocks are
 * either non-overlapping or nested. Hence, the arrangement of blocks can be
 * represented as a tree of blocks. When a block is nested inside another, they
 * possess a parent-child relationship in the tree. If they are non-overlapping,
 * they possess a sibling relationship. The root node represents the entire
 * layout of the legend item. The root node has three children: Title node,
 * Legend node and Description node (each of which are non-overlapping with one
 * another). The title node and description are common across all the legends.
 * The legend node can be customized according to the type of the legend. In the
 * legend model, the item index is mapped on to a root node.
 *
 * Each BlockNode object represents a node in the tree. It has a single parent
 * and a list of children. A BlockNode is a leaf when there it has no children.
 * Each BlockNode carries information about its location and dimensions. Each
 * BlockNode also knows which legend item it belongs to.
 *
 * Each BlockNode must be associated with an inplace editor. When a BlockNode
 * object is created, note that the inplace editor is set to null. When the
 * click occurs within the boundaries of the block, the inplace editor
 * associated with the BlockNode gets activated in the legend model. The inplace
 * editor contains all the necessary previewProperty objects. Each of these
 * previewProperty objects represent a preview property of the BlockNode.
 *
 * @author mvvijesh
 */
public class BlockNode {

    public static String ROOT = "root node";
    public static String TITLE = "title node";
    public static String DESC = "description node";
    public static String LEGEND = "legend node";
    private float originX; // x coordinate of the block node
    private float originY; // y coordinate of the block node
    private float blockWidth; // width of the block
    private float blockHeight; // height of the block
    private BlockNode parent; // parent in the BlockNode tree
    private ArrayList<BlockNode> children; // list of children in the BlockNode tree
    private InplaceEditor IPEditor; // ipeditor attached to the blockNode
    private LegendItem legendItem; // the legend item associated with BlockNode
    private String tag; // a semantic tag that explains what the node is about
    private Map<String, Object> data; //for optional extra information

    public BlockNode(BlockNode parentNode, float x, float y, float width, float height, Item parentItem, String nodeTag) {
        parent = parentNode;
        originX = x;
        originY = y;
        blockWidth = width;
        blockHeight = height;
        legendItem = (LegendItem) parentItem;
        tag = nodeTag;
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
        return tag;
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
