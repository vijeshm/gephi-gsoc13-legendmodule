package org.gephi.legend.plugin.image;

import org.openide.util.lookup.ServiceProvider;

/**
 * This is the default custom item builder for the image legend.
 *
 * This class is exposed as a service. The UI uses these services and the Lookup
 * API to show the list of available custom builders.
 *
 * @author mvvijesh, edubecks
 */
@ServiceProvider(service = CustomImageItemBuilder.class, position = 1)
public class Default implements CustomImageItemBuilder {

    @Override
    public String stepsNeededToBuild() {
        return NONE_NEEDED;
    }

    @Override
    public boolean isAvailableToBuild() {
        return true;
    }

    @Override
    public String getTitle() {
        return DEFAULT_TITLE;
    }

    @Override
    public String getDescription() {
        return DEFAULT_DESCRIPTION;
    }
}