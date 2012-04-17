package org.apache.tapestry5.portlet.services;

import javax.portlet.PortletConfig;

import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Scope;

@Scope(ScopeConstants.PERTHREAD)
public class PortletConfigProviderImpl implements PortletConfigProvider {

    private PortletConfig portletConfig;

    @Override
    public void store(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

    @Override
    public String getPortletName() {
        return this.portletConfig.getPortletName();
    }

    @Override
    public String getInitParameter(String name) {
        return this.portletConfig.getInitParameter(name);
    }

}
