/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.presets;

import java.awt.Font;
import java.awt.Point;
import org.gephi.legend.properties.TextProperty;
import org.gephi.legend.api.LegendPreset;
import org.openide.util.NbBundle;

/**
 *
 * @author edubecks
 */
public class TextDefaultPreset extends LegendPreset {

    public TextDefaultPreset() {
        super(NbBundle.getMessage(TextDefaultPreset.class, "Default.name"));


        //LOCATION
        properties.put(TextProperty.ORIGIN, new Point(100, 100));

        //TITLE
        properties.put(TextProperty.TITLE_FONT_SIZE, 13);
        properties.put(TextProperty.TITLE_FONT_STYLE, Font.PLAIN);
        properties.put(TextProperty.TITLE_FONT_TYPE, Font.SANS_SERIF);

        //DESCRIPTION
        properties.put(TextProperty.DESCRIPTION_FONT_SIZE, 13);
        properties.put(TextProperty.DESCRIPTION_FONT_STYLE, Font.PLAIN);
        properties.put(TextProperty.DESCRIPTION_FONT_TYPE, Font.SANS_SERIF);

        // TEXT
        properties.put(TextProperty.BODY_FONT_SIZE, 13);
        properties.put(TextProperty.BODY_FONT_STYLE, Font.PLAIN);
        properties.put(TextProperty.BODY_FONT_TYPE, Font.SANS_SERIF);

    }

}
