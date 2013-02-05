// Copyright 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.portlet.internal.services;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.portlet.PortletUtilities;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import org.apache.tapestry5.services.SessionPersistedObjectAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Portlet request wrapper
 * 
 * @author ccordenier
 * @author ffacon
 */
public class PortletRequestImpl implements Request
{

    protected static final String REQUESTED_WITH_HEADER = "X-Requested-With";
    protected static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    private final Logger logger = LoggerFactory.getLogger(PortletRequestImpl.class);

    private final PortletRequest request;

    private final String pageName;

    private Session session;

    private final SessionPersistedObjectAnalyzer analyzer;

    public PortletRequestImpl(PortletRequest request, String pageName,
            SessionPersistedObjectAnalyzer analyzer)
    {
        this.request = request;
        this.pageName = PortletUtilities.stripOffContext(pageName, request.getContextPath());
        this.analyzer = analyzer;
    }
    
      public Session getSession(boolean create)
    {
        if (session != null)
            return session;

        PortletSession portletSession = request.getPortletSession(create);

        if (portletSession != null)
            session = new PortletSessionImpl(portletSession, analyzer);

        return session;
    }

    public String getContextPath()
    {
        return request.getContextPath();
    }

    public List<String> getParameterNames()
    {
        return InternalUtils.toList(request.getParameterNames());
    }

    public String getParameter(String name)
    {
        return request.getParameter(name);
    }

    public String[] getParameters(String name)
    {
        return request.getParameterValues(name);
    }

    public String getPath()
    {
    	String path = pageName.startsWith("/") ? pageName : "/" + pageName;
    	return path;
    }

    public Locale getLocale()
    {
        return request.getLocale();
    }

    public List<String> getHeaderNames()
    {
        List<String> headerNames = new ArrayList<String>();
        for (Enumeration<String> e = request.getPropertyNames(); e.hasMoreElements();)
        {
            headerNames.add(e.nextElement());
        }
        return headerNames;
    }

    public long getDateHeader(String name)
    {
        logger.error(PortletServicesMessages.unsupportedMethod("getDateHeader"));
        return -1;
    }

    public String getHeader(String name)
    {
        String result = request.getProperty(name);
        return result;
    }

    public void setEncoding(String requestEncoding)
    {
        logger.error(PortletServicesMessages.unsupportedMethod("setEncoding"));
    }

    public boolean isXHR()
    {
        return XML_HTTP_REQUEST.equals(request.getProperty(REQUESTED_WITH_HEADER));
    }

    public boolean isSecure()
    {
        return request.isSecure();
    }

    public boolean isRequestedSessionIdValid()
    {
        return request.isRequestedSessionIdValid();
    }

    public Object getAttribute(String name)
    {
        return request.getAttribute(name);
    }

    public void setAttribute(String name, Object value)
    {
        request.setAttribute(name, value);
    }

    public String getServerName()
    {
        return request.getServerName();
    }

    public String getMethod()
    {
        return "POST";
    }

    /**
     * PLT 19.3.4: getRemotePort and getLocalPort now return ‘0’ instead of null
     */
    public int getLocalPort()
    {
    	if (request instanceof HttpServletRequest) {
    	return ((HttpServletRequest) request).getLocalPort();
    	}
    	else return 0;
    	//PLT 19.3.4: getRemotePort and getLocalPort now return ‘0’ instead of null
    }

    /**
    * The following methods of the HttpServletRequest must be equivalent to the methods  5 
	* of the  PortletRequest of similar name:  getScheme, getServerName, 
    * getServerPort, getAttribute, getAttributeNames, setAttribute, 
	* removeAttribute, getLocale, getLocales, isSecure, getAuthType, 
	* getContextPath, getRemoteUser, getUserPrincipal, getRequestedSessionId, 
	* isRequestedSessionIdValid, getCookies.ccxxiii
     */
    public int getServerPort()
    {
    	if (request instanceof HttpServletRequest) {
    		return ((HttpServletRequest) request).getServerPort();
      	}
    	else return request.getServerPort();
    	
    }

    /**
    * Not allowed in a portlet.
    * 
    * @throws IllegalStateException
    *             Not allowed in a portlet.
    */
    public String getRemoteHost() 
    {
    	if (request instanceof HttpServletRequest) {
      			return ((HttpServletRequest) request).getRemoteHost();
       		}
      		throw new IllegalStateException("getRemoteHost Not allowed in a portlet");
    }

	@Override
	public boolean isSessionInvalidated() {
		// Double check to ensure that the session exists, but don't create it.
        if (session == null)
        {
            //toDo session = sessionFactory.getSession(false);
        	PortletSession portletSession = request.getPortletSession(false);
        }

        return session != null && session.isInvalidated();
	}

	@Override
	public List<String> getAttributeNames() {
		 return InternalUtils.toList(request.getAttributeNames());
	}

}
