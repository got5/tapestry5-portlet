package org.apache.tapestry5.portlet.internal.services;

import java.io.IOException;

import org.apache.tapestry5.portlet.PortalPage;
import org.apache.tapestry5.portlet.services.PortletActionRenderResponseGenerator;
import org.apache.tapestry5.services.ComponentEventResultProcessor;

/**
 *  Processor that generate the redirect to a specific portal page when 
 *  a event handler method return a PortalPage object
 * 
 * @author ffacon
 */
public class PortalPageNameComponentEventResultProcessor
		implements ComponentEventResultProcessor<PortalPage> {

	
	private final PortletActionRenderResponseGenerator generator;

	public PortalPageNameComponentEventResultProcessor(PortletActionRenderResponseGenerator generator) {
		this.generator = generator;
	}

	public void processResultValue(PortalPage page) throws IOException {
		generator.generateRedirect(page);
	}
}
