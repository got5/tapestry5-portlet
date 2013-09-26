package org.apache.tapestry5.portlet.internal.services;

import java.util.List;

import javax.portlet.BaseURL;
import javax.portlet.PortletResponse;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.internal.services.LinkSecurity;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.portlet.PortletConstants;
import org.apache.tapestry5.portlet.PortletUtilities;

public abstract class BasePortletLinkImpl<T extends BaseURL> implements Link
{

    private final PortletResponse response;

    private final T portletUrl;

    private final Link delegate;

    public BasePortletLinkImpl(T portletUrl, Link delegate, PortletResponse response)
    {
        super();
        this.portletUrl = portletUrl;
        this.delegate = delegate;
        this.response = response;
    }

    public List<String> getParameterNames()
    {
        return delegate.getParameterNames();
    }

    public String getParameterValue(String name)
    {
        return delegate.getParameterValue(name);
    }

    public void addParameter(String parameterName, String value)
    {
        delegate.addParameter(parameterName, value);
    }

    public String toURI()
    {
        portletUrl.setParameter(PortletConstants.PORTLET_PAGE, PortletUtilities.stripQueryString(delegate.toURI()));
        fillParameter(portletUrl);
        return appendAnchor(response.encodeURL(portletUrl.toString()));
    }

    public String toRedirectURI()
    {
        portletUrl.setParameter(PortletConstants.PORTLET_PAGE, PortletUtilities.stripQueryString(delegate.toRedirectURI()));
        fillParameter(portletUrl);
        return appendAnchor(response.encodeURL(portletUrl.toString()));
    }

    public String getAnchor()
    {
        return delegate.getAnchor();
    }

    public void setAnchor(String anchor)
    {
        delegate.setAnchor(anchor);
    }

    public String toAbsoluteURI()
    {
        portletUrl.setParameter(PortletConstants.PORTLET_PAGE, PortletUtilities.stripQueryString(delegate.toURI()));
        fillParameter(portletUrl);
        return appendAnchor(portletUrl.toString());
    }

    @Override
    public String toString()
    {
        return toURI();
    }

    private String appendAnchor(String path)
    {
        return InternalUtils.isBlank(delegate.getAnchor()) ? path : path + "#"
                + delegate.getAnchor();
    }

    protected void fillParameter(T portletUrl)
    {
        if (portletUrl == null)
            throw new IllegalArgumentException("portletUrl is null");

        for (String key : delegate.getParameterNames())
        {
            if (!key.equalsIgnoreCase("jsessionid"))
            {
                portletUrl.setParameter(key, delegate.getParameterValue(key));
            }
        }
    }

    // New in 5.2
    public Link addParameterValue(String parameterName, Object value)
    {
        return this.delegate.addParameterValue(parameterName, value);
    }

    public Link copyWithBasePath(String basePath)
    {
        return this.copyWithBasePath(basePath);
    }

    public String getBasePath()
    {
        return this.delegate.getBasePath();
    }

    public void removeParameter(String parameterName)
    {
        this.delegate.removeParameter(parameterName);

    }

    public String toAbsoluteURI(boolean secure)
    {
        return toAbsoluteURI();
    }

    //5.3
    public void setSecurity(LinkSecurity newSecurity)
    {
    	this.delegate.setSecurity(newSecurity);
    }

    public LinkSecurity getSecurity()
    {
    	return this.delegate.getSecurity();
    }
    
    //5.3.7
    public String[] getParameterValues(String parameterName)
    {
    	return this.delegate.getParameterValues(parameterName);
    }
    
}
