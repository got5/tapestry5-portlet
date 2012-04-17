package org.apache.tapestry5.portlet.internal.services;

import java.io.IOException;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.internal.services.ComponentResultProcessorWrapper;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.portlet.annotations.Portlet;
import org.apache.tapestry5.portlet.services.PortletPageRenderRequestHandler;
import org.apache.tapestry5.portlet.services.PortletResponseRenderer;
import org.apache.tapestry5.services.ComponentEventResultProcessor;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.slf4j.Logger;

public class PortletPageRenderRequestHandlerImpl implements PortletPageRenderRequestHandler
{
    private final RequestPageCache cache;

    private final ComponentEventResultProcessor resultProcessor;

    private final PortletResponseRenderer pageResponseRenderer;
    
    private final Logger log;

    public PortletPageRenderRequestHandlerImpl(RequestPageCache cache,
                                        @Portlet
                                        ComponentEventResultProcessor resultProcessor,
                                        @Portlet
                                        PortletResponseRenderer pageResponseRenderer,
                                        Logger log)
    {
        this.cache = cache;
        this.resultProcessor = resultProcessor;
        this.pageResponseRenderer = pageResponseRenderer;
        this.log = log;
    }

    public void handle(PageRenderRequestParameters parameters) throws IOException
    {
        Page page = cache.get(parameters.getLogicalPageName());

        log.debug("->PortletPageRenderRequestHandlerImpl.handle render for page "+ page.getName());
        
        ComponentResultProcessorWrapper callback = new ComponentResultProcessorWrapper(resultProcessor);

        page.getRootElement().triggerContextEvent(EventConstants.ACTIVATE, parameters.getActivationContext(),
                                                  callback);

        
        // The handler will have asked the result processor to send a response.
        if (callback.isAborted()) return;
        
        if (!parameters.isLoopback())
            page.pageReset();

        pageResponseRenderer.renderPageResponse(page);
    }

}
