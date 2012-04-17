package org.apache.tapestry5.portlet.services;

import javax.portlet.PortletConfig;

public interface PortletConfigProvider {

    void store(PortletConfig portletConfig);

    String getPortletName();

    String getInitParameter(String name);
}
