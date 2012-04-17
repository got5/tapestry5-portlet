package org.apache.tapestry5.portlet.internal.services;

import java.io.IOException;

import javax.portlet.StateAwareResponse;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.TrackableComponentEventCallback;
import org.apache.tapestry5.internal.services.ComponentResultProcessorWrapper;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.internal.structure.ComponentPageElement;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.portlet.PortletConstants;
import org.apache.tapestry5.portlet.annotations.Portlet;
import org.apache.tapestry5.portlet.services.PortletActionRenderResponseGenerator;
import org.apache.tapestry5.portlet.services.PortletRequestGlobals;
import org.apache.tapestry5.services.ComponentEventRequestHandler;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentEventResultProcessor;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.Response;

public class PortletComponentEventRequestHandler implements ComponentEventRequestHandler
{

    private final ComponentEventResultProcessor<?> resultProcessor;

    private final RequestPageCache cache;

    private final Response response;

    private final Environment environment;

    private final PortletRequestGlobals globals;

    private final PortletActionRenderResponseGenerator responseGenerator;

    public PortletComponentEventRequestHandler(

    @Portlet
    ComponentEventResultProcessor<?> resultProcessor,

    RequestPageCache cache,

    Response response,

    Environment environment,

    PortletRequestGlobals globals,

    PortletActionRenderResponseGenerator responseGenerator)
    {
        this.resultProcessor = resultProcessor;
        this.cache = cache;
        this.response = response;
        this.environment = environment;
        this.globals = globals;
        this.responseGenerator = responseGenerator;
    }

    public void handle(ComponentEventRequestParameters parameters) throws IOException
    {
        Page activePage = cache.get(parameters.getActivePageName());

        ComponentResultProcessorWrapper callback = new ComponentResultProcessorWrapper(
                resultProcessor);

        // If activating the page returns a "navigational result", then don't
        // trigger the action on the component.

        activePage.getRootElement().triggerContextEvent(EventConstants.ACTIVATE,
                parameters.getPageActivationContext(), callback);

        if (callback.isAborted())
            return;

        Page containerPage = cache.get(parameters.getContainingPageName());

        ComponentPageElement element = containerPage.getComponentElementByNestedId(parameters
                .getNestedComponentId());

        environment.push(ComponentEventResultProcessor.class, resultProcessor);
        environment.push(TrackableComponentEventCallback.class, callback);

        boolean handled = element.triggerContextEvent(parameters.getEventType(),
                parameters.getEventContext(), callback);

        if (!handled)
            throw new TapestryException(PortletServicesMessages.eventNotHandled(element,
                    parameters.getEventType()), element, null);

        environment.pop(TrackableComponentEventCallback.class);
        environment.pop(ComponentEventResultProcessor.class);

        if (callback.isAborted())
        {
            callback.rethrow();
            return;
        }

        if (!response.isCommitted()
                && !((StateAwareResponse) globals.getPortletResponse()).getRenderParameterMap()
                        .containsKey(PortletConstants.PORTLET_PAGE))
        {
            responseGenerator.generateResponse(activePage);
        }
    }

}
