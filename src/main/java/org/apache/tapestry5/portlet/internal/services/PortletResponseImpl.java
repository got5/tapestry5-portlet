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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.portlet.ActionResponse;
import javax.portlet.PortletResponse;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapts {@link javax.portlet.PortletResponse} as {@link org.apache.tapestry.web.WebResponse}.
 * 
 */
public class PortletResponseImpl implements Response
{
    private final Logger _logger = LoggerFactory.getLogger(PortletResponseImpl.class);

    private final PortletResponse _response;
    private final Request _request;
    protected boolean _isCommited = false;

    public PortletResponseImpl(PortletResponse portletResponse, Request portletRequest)
    {
        assert portletResponse!=null;

        _response = portletResponse;
        _request = portletRequest;
    }

    public PrintWriter getPrintWriter(String contentType) throws IOException
    {
        _logger.error(PortletServicesMessages.unsupportedMethod("getOutputStream"));
        return null;   
    }

    public OutputStream getOutputStream(String contentType) throws IOException
    {
        _logger.error(PortletServicesMessages.unsupportedMethod("getOutputStream"));
        return null;
    }

    public void sendRedirect(String URL) throws IOException
    {
        if (_response instanceof ActionResponse)
        {
            ActionResponse liferayResponse = (ActionResponse) _response;
            liferayResponse.sendRedirect(URL);
            _isCommited = true;
            return;
        }
        _logger.error(PortletServicesMessages.unsupportedMethod("sendRedirect"));
    }

    public void sendRedirect(Link link) throws IOException
    {
        sendRedirect(link.toAbsoluteURI());
    }

    public void setStatus(int sc)
    {
        _logger.error(PortletServicesMessages.unsupportedMethod("setStatus"));
    }

    public void sendError(int statusCode, String message) throws IOException
    {
        _logger.error(PortletServicesMessages.unsupportedMethod("sendError"));
    }

    public void setContentLength(int contentLength)
    {
        _logger.error(PortletServicesMessages.unsupportedMethod("setContentLength"));
    }

    public void setDateHeader(String string, long date)
    {
        _logger.error(PortletServicesMessages.unsupportedMethod("setDateHeader"));
    }

    public void setHeader(String name, String value)
    {
        _logger.info("Response Header: " + name + " " + value + " Class: " + _response.getClass());
    }

    public void setIntHeader(String name, int value)
    {
        _logger.error(PortletServicesMessages.unsupportedMethod("setIntHeader"));
    }

    public String encodeURL(String url)
    {
        // URL must not be encoded
        return url;
    }

    public String encodeRedirectURL(String URL)
    {
        _logger.error(PortletServicesMessages.unsupportedMethod("encodeRedirectURL"));

        return URL;
    }

    public boolean isCommitted()
    {
        _logger.info("Commited: " + _isCommited);
        return _isCommited;
    }

	
    public void disableCompression() {
		// TODO Auto-generated method stub
		
	}

	public void addHeader(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

}
