package org.apache.tapestry5.portlet.internal.services;

import java.io.IOException;

import org.apache.tapestry5.portlet.services.PortletRequestGlobals;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentEventRequestHandler;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;

/**
 * Use to route request coming from portlet container to the corresponding handler.
 * 
 * @author ccordenier
 * @author ffacon
 */
public class PortletComponentEventRequestFilter implements ComponentEventRequestFilter
{

    private final PortletRequestGlobals globals;

    private final ComponentEventRequestHandler portletHandler;

    private final Request request;

    private final Logger log;

    public PortletComponentEventRequestFilter(PortletRequestGlobals globals,
            ComponentEventRequestHandler handler, Request request, Logger log)
    {
        super();
        this.globals = globals;
        this.portletHandler = handler;
        this.request = request;
        this.log = log;
    }

    public void handle(ComponentEventRequestParameters parameters,
            ComponentEventRequestHandler handler) throws IOException
    {

        if (globals.getActionRequest() != null || globals.getEventRequest() != null)// &&
                                                                                    // !request.isXHR()
                                                                                    // )
        {
            log.debug("->PortletComponentEventRequestFilter.handle -> actionRequest for windowId="
                    + globals.getPortletRequest().getWindowID());
            portletHandler.handle(parameters);
            return;
        }
        log.debug("->PortletComponentEventRequestFilter.handle -> delegate to next handle");
        handler.handle(parameters);
    }

}
