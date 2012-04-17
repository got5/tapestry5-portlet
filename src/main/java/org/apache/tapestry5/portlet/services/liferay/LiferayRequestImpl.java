package org.apache.tapestry5.portlet.services.liferay;

import java.util.Enumeration;
import java.util.List;

import javax.portlet.PortletRequest;

import org.apache.tapestry5.portlet.internal.services.PortletRequestImpl;
import org.apache.tapestry5.services.SessionPersistedObjectAnalyzer;

import com.liferay.portal.kernel.portlet.LiferayPortletRequest;

/**
 * This class replaces the default standard implementation when the portlet is
 * deployed in a Liferay server. We need to read the Http Header from the inner
 * HttpServletRequest because Liferay does not import those in PortletRequest
 * properties (As suggested in the portlet specification JSR-286)
 * 
 * @author ccordenier
 * 
 */
public class LiferayRequestImpl
		extends PortletRequestImpl {
	private LiferayPortletRequest portletRequest;

	public LiferayRequestImpl(PortletRequest request, String pageName, SessionPersistedObjectAnalyzer analyzer) {
		super(request, pageName, analyzer);
		portletRequest = (LiferayPortletRequest) request;
	}

	@Override
	public boolean isXHR() {
		return XML_HTTP_REQUEST.equals(getHeader(REQUESTED_WITH_HEADER));
	}

	@Override
	public String getHeader(String name) {
		String headerValue = super.getHeader(name);
		return headerValue == null ? portletRequest.getHttpServletRequest().getHeader(name) : headerValue;
	}

	@Override
	public List<String> getHeaderNames() {
		List<String> result = super.getHeaderNames();
		for (Enumeration<String> e = portletRequest.getHttpServletRequest().getHeaderNames(); e.hasMoreElements();) {
			result.add(e.nextElement());
		}
		return result;
	}

}
