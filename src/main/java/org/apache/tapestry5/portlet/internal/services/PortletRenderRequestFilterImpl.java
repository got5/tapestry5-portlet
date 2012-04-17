package org.apache.tapestry5.portlet.internal.services;

import java.io.IOException;

import javax.portlet.RenderRequest;

import org.apache.tapestry5.portlet.annotations.Portlet;
import org.apache.tapestry5.portlet.services.PortletPageRenderRequestHandler;
import org.apache.tapestry5.portlet.services.PortletRequestGlobals;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.slf4j.Logger;

public class PortletRenderRequestFilterImpl implements PageRenderRequestFilter
{
    private final PortletRequestGlobals globals;

    private final PortletPageRenderRequestHandler portletHandler;

    private final Logger log;
    public PortletRenderRequestFilterImpl(PortletRequestGlobals globals, 
    									  @Portlet PortletPageRenderRequestHandler portletHandler,
    									  final Logger log)
    {
        super();
        this.globals = globals;
        this.portletHandler = portletHandler;
        this.log = log;
    }

    public void handle(PageRenderRequestParameters parameters, PageRenderRequestHandler handler)
            throws IOException
    {
    	RenderRequest renderRequest = globals.getRenderRequest();
        if (renderRequest != null)
        {
        	log.info("->PortletRenderRequestFilterImpl.handle -> renderRequest for windowId=" +renderRequest.getWindowID());
            portletHandler.handle(parameters);
            return;
        }
        log.info("->PortletRenderRequestFilterImpl.handle -> delegate to next handle");
        handler.handle(parameters);
    }
}
