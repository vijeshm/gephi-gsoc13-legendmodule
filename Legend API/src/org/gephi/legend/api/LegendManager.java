/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.gephi.legend.properties.LegendProperty;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.nodes.PropertySupport;
import org.openide.util.Lookup;

/**
 *
 * @author eduBecKs
 */
public class LegendManager {

    private Integer activeLegendIndex;
    private Integer currentIndex;
    private Integer firstActiveLegend;
    private ArrayList<Boolean> isActive;
    private ArrayList<String> items;
    private ArrayList<Item> legendItems;
    // reference to combobox
    private JComboBox activeLegendsComboBox;
    private JPanel legendManagerPanel;
    public static final String LEGEND_PROPERTIES = "legend properties";
    public static final String INDEX = "index";
    private static final String LEGEND_DESCRIPTION = "legend";
    private static final String DYNAMIC = ".dynamic";
    private static final String ITEM_DESCRIPTION = ".item";

    
    public LegendManager(){
        this.currentIndex = 0;
        this.firstActiveLegend = 0;
        this.items = new ArrayList<String>();
        this.legendItems = new ArrayList<Item>();
        this.isActive = new ArrayList<Boolean>();
        this.activeLegendIndex=-1;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public boolean hasActiveLegends() {

        for (int i = 0; i < isActive.size(); i++) {
            if (isActive.get(i)) {
                firstActiveLegend = i;
                return true;
            }
        }
        return false;
    }

    public void refreshDynamicPreviewProperties() {

        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
        Workspace workspace = projectController.getCurrentWorkspace();


        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel(workspace);
        PreviewProperties previewProperties = previewModel.getProperties();

        // clear old properties
        for (PreviewProperty property : previewProperties.getProperties(LegendProperty.DYNAMIC)) {
            System.out.println("@Var: removing property: " + property.getName());
            previewProperties.removeProperty(property);
        }

        for (int i = 0; i < isActive.size(); i++) {
            if (isActive.get(i)) {
                PreviewProperty[] properties = (PreviewProperty[]) legendItems.get(i).getData(LegendItem.DYNAMIC_PROPERTIES);
                for (PreviewProperty property : properties) {
                    System.out.println("@Var: adding property: " + property.getName());
                    //previewProperties.addProperty(property);
                    previewProperties.putValue(property.getName(), property.getValue());
                }
            }
        }
    }

    



    public void addItem(Item item) {

        activeLegendIndex = currentIndex;
        System.out.println("@Var: creating item activeLegend: " + activeLegendIndex);
        System.out.println("@Var: item: " + item);

        items.add(LEGEND_DESCRIPTION + ITEM_DESCRIPTION + currentIndex);
        isActive.add(Boolean.TRUE);
        legendItems.add(item);
        currentIndex++;
    }

    public void removeItem(int index) {

        isActive.set(index, Boolean.FALSE);
        if (hasActiveLegends()) {
            activeLegendIndex = firstActiveLegend;
        }
        else {
            activeLegendIndex = -1;
        }
    }

    public void setActiveLegend(Integer activeLegend) {
        this.activeLegendIndex = activeLegend;
    }

    public Integer getActiveLegend() {
        return activeLegendIndex;
    }
    
    public Item getActiveLegendItem(){
        if(activeLegendIndex>=0){
            return legendItems.get(activeLegendIndex);
        }
        return null;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public ArrayList<Item> getLegendItems() {
        ArrayList<Item> activeItems = new ArrayList<Item>();
        for (int i = 0; i < isActive.size(); i++) {
            if (isActive.get(i)) {
                activeItems.add(legendItems.get(i));
            }
        }
        return activeItems;
    }

    public static boolean getItemIndexFromProperty(PreviewProperty property, Integer index) {
        Pattern pattern = Pattern.compile("\\.item(\\d+)\\.");
        Matcher matcher = pattern.matcher(property.getName());
        if (matcher.find()) {
            index = Integer.valueOf(matcher.group(1));
        }
        return false;
    }

    public static boolean isLegendPropertyForItem(PreviewProperty property, String item) {
        if (property.getName().startsWith(item)) {
            return true;
        }
        return false;
    }

    public static boolean isLegendProperty(PreviewProperty property) {
        if (property.getName().startsWith(LEGEND_DESCRIPTION)) {
            return true;
        }
        return false;
    }

    public static boolean isLegendDynamicProperty(PreviewProperty property) {
        if (property.getName().endsWith(DYNAMIC)) {
            return true;
        }
        return false;
    }

    public static ArrayList<String> getProperties(String[] PROPERTIES, int itemIndex) {
        ArrayList<String> properties = new ArrayList<String>();
        for (String property : PROPERTIES) {
            String newProperty = (LEGEND_DESCRIPTION
                                  + ITEM_DESCRIPTION + itemIndex
                                  + property);
            properties.add(newProperty);
//            System.out.println("Creating >>> " + newProperty);
        }
        return properties;
    }

    public static String getDynamicProperty(String property, int itemIndex, int itemIndexNested) {
        return (LEGEND_DESCRIPTION + ITEM_DESCRIPTION + itemIndex + property + itemIndexNested + DYNAMIC);
    }

    public static ArrayList<String> getProperties(ArrayList<String> legendProperties, int itemIndex) {
        ArrayList<String> properties = new ArrayList<String>();
        for (String property : legendProperties) {
            String newProperty = (LEGEND_DESCRIPTION
                                  + ITEM_DESCRIPTION + itemIndex
                                  + property);
            properties.add(newProperty);
//            System.out.println("Creating >>> " + newProperty);
        }
        return properties;
    }

    public static String getProperty(String[] PROPERTIES, int itemIndex, int legendProperty) {
        String property = (LEGEND_DESCRIPTION
                           + ITEM_DESCRIPTION + itemIndex
                           + PROPERTIES[legendProperty]);
//        System.out.println("@Var: property: " + property);
        return property;
    }

}
