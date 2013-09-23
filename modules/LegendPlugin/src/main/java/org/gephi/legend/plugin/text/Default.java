package org.gephi.legend.plugin.text;

import org.openide.util.lookup.ServiceProvider;

/**
 * This is the default custom item builder for the text legend.
 *
 * This class is exposed as a service. The UI uses these services and the Lookup
 * API to show the list of available custom builders. This class defines the
 * cells and its properties.
 *
 * @author mvvijesh, edubecks
 */
@ServiceProvider(service = CustomTextItemBuilder.class, position = 1)
public class Default implements CustomTextItemBuilder {

    @Override
    public String getDescription() {
        return DEFAULT_DESCRIPTION;
    }

    @Override
    public String getTitle() {
        return DEFAULT_TITLE;
    }

    @Override
    public boolean isAvailableToBuild() {
        return true;
    }

    @Override
    public String stepsNeededToBuild() {
        return NONE_NEEDED;
    }
}