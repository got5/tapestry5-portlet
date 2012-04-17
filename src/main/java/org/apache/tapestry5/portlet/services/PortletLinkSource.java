package org.apache.tapestry5.portlet.services;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.internal.structure.Page;

/**
 * Wrapper around the Tapestry default implementation of LinkSource.
 * 
 * @author ccordenier
 * @author ffacon
 * 
 */
public interface PortletLinkSource
		extends LinkSource {

	/**
	 * Wrap the original
	 * {@link LinkSource#createComponentEventLink(Page, String, String, boolean, Object...)}
	 * method.
	 */
	Link createComponentEventLinkForServletContainer(Page page, String nestedId, String eventType, boolean forForm,
			Object... eventContext);

	/**
	 * Wrap the original
	 * {@link LinkSource#createPageRenderLink(String, boolean, Object...)}
	 * method.
	 */
	Link createPageRenderLinkForServletContainer(String pageName, boolean override, Object... pageActivationContext);

}
