package org.apache.tapestry5.portlet.internal.services;

import java.io.IOException;

import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.portlet.services.PortletActionRenderResponseGenerator;
import org.apache.tapestry5.services.ComponentEventResultProcessor;

/**
 * Used when a component event handler returns a string value. The value is
 * interpreted as the logical name of a page. A link to the page will be sent as
 * a the subsequent render request.
 */
public class PortletPageComponentEventResultProcessor
		implements ComponentEventResultProcessor<String> {

	private final RequestPageCache requestPageCache;

	private final PortletActionRenderResponseGenerator generator;

	public PortletPageComponentEventResultProcessor(RequestPageCache requestPageCache,
			PortletActionRenderResponseGenerator generator) {
		this.requestPageCache = requestPageCache;
		this.generator = generator;
	}

	public void processResultValue(String value) throws IOException {
		Page page = requestPageCache.get(value);

		generator.generateResponse(page);
	}
}
