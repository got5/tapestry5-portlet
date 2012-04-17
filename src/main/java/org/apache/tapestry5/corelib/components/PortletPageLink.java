package org.apache.tapestry5.corelib.components;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.portlet.PortletLink;
import org.apache.tapestry5.services.PageRenderLinkSource;

/**
 * This type of link can be used to navigate inside the portlet between Tapestry
 * page. It add support for window state and portlet mode.
 * 
 * @author ccordenier
 * 
 */
public class PortletPageLink
		extends AbstractLink {
	/**
	 * The logical name of the page to link to.
	 */
	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String page;

	@Parameter(allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String windowState;

	@Parameter(allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String portletMode;

	@Inject
	private PageRenderLinkSource linkSource;

	/**
	 * If provided, this is the activation context for the target page (the
	 * information will be encoded into the URL). If not provided, then the
	 * target page will provide its own activation context.
	 */
	@Parameter
	private Object[] context;

	void beginRender(MarkupWriter writer) {

		if (isDisabled())
			return;

		Link link = context != null ? linkSource.createPageRenderLinkWithContext(page, context) : linkSource.createPageRenderLink(page);

		if (link instanceof PortletLink) {
			PortletLink portletLink = (PortletLink) link;
			portletLink.setMode(new PortletMode(portletMode));
			portletLink.setWindowState(new WindowState(windowState));
		}

		writeLink(writer, link);
	}

	void afterRender(MarkupWriter writer) {
		if (isDisabled())
			return;

		writer.end(); // <a>
	}

	String defaultWindowState() {
		return WindowState.NORMAL.toString();
	}

	String defaultPortletMode() {
		return PortletMode.VIEW.toString();
	}
}
