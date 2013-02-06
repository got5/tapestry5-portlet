package org.apache.tapestry5.portlet.internal.services;

import javax.portlet.PortletResponse;
import javax.portlet.ResourceURL;

import org.apache.tapestry5.Link;

/**
 * This class is used to render resource URL.
 *
 * @author ccordenier
 *
 */
public class ResourceLinkImpl
		extends BasePortletLinkImpl<ResourceURL> {

	public ResourceLinkImpl(ResourceURL portletUrl, Link delegate, PortletResponse response) {
		super(portletUrl, delegate, response);
	}

	
}
