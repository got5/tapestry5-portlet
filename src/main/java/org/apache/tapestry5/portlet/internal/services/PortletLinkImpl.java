package org.apache.tapestry5.portlet.internal.services;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.LinkSecurity;
import org.apache.tapestry5.portlet.PortletLink;

public class PortletLinkImpl
		extends BasePortletLinkImpl<PortletURL>
		implements PortletLink {

	private WindowState windowState;

	private PortletMode mode;

	public PortletLinkImpl(PortletURL portletUrl, Link delegate, PortletResponse response) {
		super(portletUrl, delegate, response);
	}

	@Override
	public void setWindowState(WindowState windowState) {
		this.windowState = windowState;
	}

	@Override
	public void setMode(PortletMode mode) {
		this.mode = mode;
	}

	@Override
	protected final void fillParameter(PortletURL portletUrl) {
	    super.fillParameter(portletUrl);
		try {
			if (windowState != null) {
				portletUrl.setWindowState(windowState);
			}
			if (mode != null) {
				portletUrl.setPortletMode(mode);
			}
		} catch (PortletModeException pmEx) {
			throw new IllegalArgumentException(String.format("Portlet mode %s not supported", mode));
		} catch (WindowStateException wsEx) {
			throw new IllegalArgumentException(String.format("WindowState %s not supported", windowState));
		}
	}

	

	

	
}
