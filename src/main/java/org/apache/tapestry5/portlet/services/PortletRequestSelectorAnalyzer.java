package org.apache.tapestry5.portlet.services;

import javax.portlet.WindowState;

import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.pageload.ComponentRequestSelectorAnalyzer;
import org.apache.tapestry5.services.pageload.ComponentResourceSelector;

/**
 * New Implementation of the ComponentRequestSelectorAnalyzer, in order to use
 * the WindowState when retrieving templates and bundles
 * (template_maximized.tml, template_minimized.tml)
 * 
 * @author Emmanuel DEMEY
 * 
 */
public class PortletRequestSelectorAnalyzer implements
		ComponentRequestSelectorAnalyzer {

	private ThreadLocale threadLocale;
	private PortletRequestGlobals request;

	public PortletRequestSelectorAnalyzer(ThreadLocale threadLocale,
			PortletRequestGlobals request) {

		this.threadLocale = threadLocale;
		this.request = request;
	}

	@Override
	public ComponentResourceSelector buildSelectorForRequest() {
		System.out.println("######### " + request.getPortletRequest());
		return new ComponentResourceSelector(threadLocale.getLocale())
				.withAxis(WindowState.class, request.getPortletRequest()
						.getWindowState());
	}

}
