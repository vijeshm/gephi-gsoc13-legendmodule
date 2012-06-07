/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.presets;

import java.awt.Font;
import java.awt.Point;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.legend.api.LegendPreset;
import org.gephi.preview.api.PreviewPreset;
import org.openide.util.NbBundle;

/**
 *
 * @author edubecks
 */
public class TextDefaultPreset extends PreviewPreset {

    public TextDefaultPreset() {
        super(NbBundle.getMessage(TextDefaultPreset.class, "Default.name"));


        //LOCATION
        properties.put(PreviewProperty.LEGEND_ORIGIN_X, 100f);
        properties.put(PreviewProperty.LEGEND_ORIGIN_X, 100f);

        //TITLE
        properties.put(PreviewProperty.LEGEND_TITLE_FONT, new Font("Arial", Font.PLAIN, 12));

        //DESCRIPTION
        properties.put(PreviewProperty.LEGEND_DESCRIPTION_FONT, new Font("Arial", Font.PLAIN, 12));

        // TEXT
        properties.put(PreviewProperty.LEGEND_TEXT_BODY_FONT, new Font("Arial", Font.PLAIN, 12));

    }

}
