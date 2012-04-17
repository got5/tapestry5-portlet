package org.apache.tapestry5.portlet.services;

import java.io.IOException;

import org.apache.tapestry5.services.PageRenderRequestParameters;

public interface PortletPageRenderRequestHandler
{

    /**
     * Invoked to activate and render a page. In certain cases, based on values returned when
     * activating the page, a {@link org.apache.tapestry5.services.ComponentEventResultProcessor}
     * may be used to send an alternate response
     * (typically, a redirect).
     * 
     * @param parameters
     *            defines the page name and activation context
     */
    void handle(PageRenderRequestParameters parameters) throws IOException;

}
