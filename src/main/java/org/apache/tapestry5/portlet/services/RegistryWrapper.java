package org.apache.tapestry5.portlet.services;

import javax.portlet.PortletContext;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;

/**
 * Helper class that resolves the common services used by the Tapestry ApplicationPorlet class.
 * 
 * @author ccordenier
 */
public class RegistryWrapper {

	private Registry registry;

	private PortletContext context;

	private PortletActionRequestHandler actionHandler;

	private PortletRenderRequestHandler renderHandler;

	private PortletResourceRequestHandler resourceHandler;

	private PortletEventRequestHandler eventHandler;

	public RegistryWrapper(PortletContext context) {
		this.context = context;
	}

    /**
     * Retrieve the tapestry registry from context.
     */
    void init()
    {
        if (registry == null)
        {
            if (context.getAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME) != null)
            {
                registry = (Registry) context.getAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME);
                actionHandler = registry.getService("PortletActionRequestHandler",
                        PortletActionRequestHandler.class);
                renderHandler = registry.getService("PortletRenderRequestHandler",
                        PortletRenderRequestHandler.class);
                resourceHandler = registry.getService("PortletResourceRequestHandler",
                        PortletResourceRequestHandler.class);
                eventHandler = registry.getService("PortletEventRequestHandler",
                        PortletEventRequestHandler.class);
            }
            else
            {
                throw new NullPointerException(
                        "No Tapestry Registry available in Context, check your web.xml file");
            }
        }
    }

	public PortletActionRequestHandler getActionHandler() {
		init();
		return actionHandler;
	}

	public PortletRenderRequestHandler getRenderHandler() {
		init();
		return renderHandler;
	}

	public PortletResourceRequestHandler getResourceHandler() {
		init();
		return resourceHandler;
	}

	public PortletEventRequestHandler getEventHandler() {
		init();
		return eventHandler;
	}
	
	public PortletConfigProvider getPortletConfigProvider() {
        init();
        return registry.getService(PortletConfigProvider.class);
    }

	public void cleanupThread() {
		init();
		registry.cleanupThread();
	}

}
