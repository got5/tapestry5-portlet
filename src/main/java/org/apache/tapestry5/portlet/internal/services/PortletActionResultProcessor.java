package org.apache.tapestry5.portlet.internal.services;

import java.io.IOException;

import javax.portlet.StateAwareResponse;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.portlet.PortletConstants;
import org.apache.tapestry5.portlet.PortletRenderable;
import org.apache.tapestry5.portlet.PortletUtilities;
import org.apache.tapestry5.portlet.services.PortletLinkSource;
import org.apache.tapestry5.portlet.services.PortletRequestGlobals;
import org.apache.tapestry5.services.ComponentEventResultProcessor;

/**
 * 	ComponentEventResultProcessor which use the data provided by PortletRenderable 
 *  to create the link which do the the communication between two portlets
 *   
 * 	@author ffacon
 */
public class PortletActionResultProcessor
		implements ComponentEventResultProcessor<PortletRenderable> {
	private final PortletLinkSource linkSource;

	private final PortletRequestGlobals globals;

	public PortletActionResultProcessor(PortletLinkSource linkSource, PortletRequestGlobals globals) {
		this.globals = globals;
		this.linkSource = linkSource;
	}

	public void processResultValue(PortletRenderable value) throws IOException {
		// Create the link from render response information
		Link result = linkSource.createPageRenderLink(value.getPageName(), false);
		StateAwareResponse response = (StateAwareResponse)globals.getPortletResponse();
        for (String name : value.getRenderParameters().keySet()) {
			response.setRenderParameter(name, value.getRenderParameters().get(name));
		}
		response.setRenderParameter(PortletConstants.PORTLET_PAGE, PortletUtilities.stripQueryString(result.toURI()));
	}
}
