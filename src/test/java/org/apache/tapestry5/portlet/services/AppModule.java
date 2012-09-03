package org.apache.tapestry5.portlet.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.portlet.upload.services.PortletUploadModule;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
@SubModule({PortletModule.class, PortletUploadModule.class})
public class AppModule
{

    public static void contributeApplicationDefaults(
            MappedConfiguration<String, String> configuration)
    {

        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
        configuration.add(SymbolConstants.APPLICATION_VERSION, "5.2.1-SNAPSHOT");
        configuration.add(SymbolConstants.APPLICATION_CATALOG, "context:WEB-INF/app.properties");
    }

}
