package org.apache.tapestry5.portlet.internal.services;

import java.io.IOException;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.portlet.services.PortletActionRenderResponseGenerator;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ComponentEventResultProcessor;

/**
 * Used when a component event handler returns a string value. The value is
 * interpreted as the logical name of a page. A link to the page will be sent as
 * a the subsequent render request.
 */
public class PortletComponentInstanceEventResultProcessor
		implements ComponentEventResultProcessor<Component> {

	private final RequestPageCache requestPageCache;

	private final PortletActionRenderResponseGenerator generator;

	public PortletComponentInstanceEventResultProcessor(RequestPageCache requestPageCache,
			PortletActionRenderResponseGenerator generator) {
		this.requestPageCache = requestPageCache;
		this.generator = generator;
	}

	public void processResultValue(Component value) throws IOException {
		ComponentResources resources = value.getComponentResources();

		if (resources.getContainer() != null) {
			throw new IllegalArgumentException(String.format("%s is not a valid page to redirect to.",
					resources.getCompleteId()));
		}

		// We have all these layers and layers between us and the page instance,
		// but its easy to extract the page class name and quickly re-resolve
		// that to the page instance.

		Page page = requestPageCache.get(resources.getPageName());
		generator.generateResponse(page);
	}
}
