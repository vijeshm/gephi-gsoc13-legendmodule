/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

/**
 *
 * @author edubecks
 */
import org.gephi.legend.items.TableItem;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.preview.api.Item;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ItemBuilder.class, position = 90)
public class TableItemBuilder implements ItemBuilder {

    @Override
    public Item[] getItems(Graph graph, AttributeModel attributeModel) {

        //colors
        ArrayList<Color> listOfColors;
        listOfColors = new ArrayList<Color>();
        listOfColors.add(Color.RED);
        listOfColors.add(Color.BLUE);
        listOfColors.add(Color.LIGHT_GRAY);
        listOfColors.add(Color.YELLOW);
        listOfColors.add(Color.WHITE);
        listOfColors.add(Color.GREEN);
        listOfColors.add(Color.PINK);
        listOfColors.add(Color.MAGENTA);

        //values
        Float[][] tableValues1 = {
            {9.11627487663f, 5.35347285074f, 5.53021302669f, 6.20519225239f, 5.27466987493f, 5.48850364226f, 5.48284905154f, 5.65304915413f},
            {6.11032214404f, 5.2818160758f, 5.30893993444f, 5.30624373045f, 6.37271356504f, 5.25206050232f, 5.28980850706f, 6.03624180324f},
            {5.04510203573f, 5.11945988425f, 5.17872911783f, 5.10501075623f, 9.25288976257f, 6.31804895499f, 5.17481323163f, 5.3245338662f},
            {5.00305453268f, 5.78153226657f, 5.21040577561f, 5.28651327902f, 6.07657004397f, 6.43552722671f, 5.33661053887f, 6.16149432112f},
            {5.41582039369f, 8.01040455143f, 5.70189186658f, 7.19051905857f, 5.48662725091f, 5.36079587503f, 5.65666867777f, 6.51873273528f},
            {5.44276841441f, 5.68705442654f, 5.45203949569f, 6.3702550653f, 6.25203315678f, 6.40009442175f, 8.28908129072f, 7.81318233426f},
            {5.35181089016f, 5.07697000134f, 5.76683083336f, 5.04885627802f, 6.51540268438f, 5.05027735868f, 7.21679046513f, 5.58756039989f},
            {5.24203982658f, 5.67995857207f, 7.75338861595f, 7.95242620409f, 5.45866613248f, 5.34302649296f, 5.26438348233f, 5.70365120776f}};
        
            
        //label
            ArrayList<String> labels1;
        labels1 = new ArrayList<String>();
        labels1.add("Juan");
        labels1.add("QWERTYYY");
        labels1.add("John");
        labels1.add("Perez");
        labels1.add("Nada");
        labels1.add("Nada de Nada");
        labels1.add("Un Nombre");
        labels1.add("Holaaaaaaaaaa");


        //Build a number of SomeLegendItem instances, so they are later rendered with SomeLegendItemRenderer
        //In this case we return just one:
        TableItem tableItem = new TableItem(graph/*
                 * Actually, anything can be passed here to identify as the
                 * source for this item
                 */);

        Integer fontSize = 13;
        Integer cellSizeWidth = 40;
        Integer cellSizeHeight = 20;
        Boolean isCellColoring = true;
        TableItem.Direction cellColoring = TableItem.Direction.UP;
        Color background = Color.WHITE;
        
        TableItem.Direction horizontalAlignment = TableItem.Direction.LEFT;
        TableItem.Direction horizontalTextAlignment = TableItem.Direction.RIGHT;
        TableItem.Direction verticalAlignment = TableItem.Direction.UP;
        
        TableItem.VerticalTextDirection verticalTextDirection = TableItem.VerticalTextDirection.DIAGONAL;
        
        String fontType = Font.SANS_SERIF;
        Integer fontStyle = Font.PLAIN;
        // bug, dont know why
        Integer verticalExtraMargin = 0;
        Integer horizontalExtraAlignment = 0;
        
        //Put some data based on current graph, attributes, anything...
        //Here is where ideas to build different legend items and setting them up with the graph happens
        tableItem.setData(TableItem.LABELS, labels1);
        tableItem.setData(TableItem.TABLE_VALUES, tableValues1);
        tableItem.setData(TableItem.LIST_OF_COLORS, listOfColors);
        
        tableItem.setData(TableItem.IS_CELL_COLORING, isCellColoring);
        tableItem.setData(TableItem.CELL_COLORING, cellColoring);
        tableItem.setData(TableItem.BACKGROUND, background);
        
        
        tableItem.setData(TableItem.FONT_SIZE, fontSize);
        tableItem.setData(TableItem.FONT_TYPE, fontType);
        tableItem.setData(TableItem.FONT_STYLE, fontStyle);
        
        tableItem.setData(TableItem.CELL_SIZE_WIDTH, cellSizeWidth);
        tableItem.setData(TableItem.CELL_SIZE_HEIGHT, cellSizeHeight);
        
        
        tableItem.setData(TableItem.HORIZONTAL_ALIGNMENT, horizontalAlignment);
        tableItem.setData(TableItem.HORIZONTAL_TEXT_ALIGNMENT, horizontalTextAlignment);
        tableItem.setData(TableItem.VERTICAL_ALIGNMENT, verticalAlignment);
        
        tableItem.setData(TableItem.VERTICAL_TEXT_DIRECTION, verticalTextDirection);
        
        
        tableItem.setData(TableItem.MINIMUM_MARGIN, 3);
        
        tableItem.setData(TableItem.VERTICAL_EXTRA_MARGIN, verticalExtraMargin);
        tableItem.setData(TableItem.HORIZONTAL_EXTRA_ALIGNMENT, horizontalExtraAlignment);
        
        return new Item[]{tableItem};
    }

    @Override
    public String getType() {
        return TableItem.TYPE;
    }

}