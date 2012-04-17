package org.apache.tapestry5.portlet.internal.services;

import java.io.IOException;

import javax.portlet.StateAwareResponse;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.portlet.PortletConstants;
import org.apache.tapestry5.portlet.PortletUtilities;
import org.apache.tapestry5.portlet.services.PortletRequestGlobals;
import org.apache.tapestry5.services.ComponentEventResultProcessor;
import org.apache.tapestry5.services.PageRenderLinkSource;

public class PortletClassResultProcessor implements ComponentEventResultProcessor<Class<?>>
{
    private final PageRenderLinkSource linkSource;

    private final PortletRequestGlobals globals;

    public PortletClassResultProcessor(PageRenderLinkSource linkSource,
            PortletRequestGlobals globals)
    {
        this.globals = globals;
        this.linkSource = linkSource;
    }

    public void processResultValue(Class<?> value) throws IOException
    {
        // Create the link from render response information
        Link result = linkSource.createPageRenderLink(value);
        ((StateAwareResponse) globals.getPortletResponse()).setRenderParameter(
                PortletConstants.PORTLET_PAGE, PortletUtilities.stripQueryString(result.toURI()));
    }
}
