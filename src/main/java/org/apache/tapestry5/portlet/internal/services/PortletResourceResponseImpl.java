package org.apache.tapestry5.portlet.internal.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.portlet.ResourceResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortletResourceResponseImpl extends PortletResponseImpl
{

    private final Logger _logger = LoggerFactory.getLogger(PortletRenderResponseImpl.class);

    private final ResourceResponse _resourceResponse;

    public PortletResourceResponseImpl(ResourceResponse resourceResponse)
    {
        super(resourceResponse, null);
        _resourceResponse = resourceResponse;
    }

    @Override
    public void setStatus(int sc)
    {
        _resourceResponse.setProperty(ResourceResponse.HTTP_STATUS_CODE, sc + "");
    }

    @Override
    public OutputStream getOutputStream(String contentType) throws IOException
    {
        _logger.info("getOutputStream");
        _resourceResponse.setContentType(contentType);
        _isCommited = true;
        return _resourceResponse.getPortletOutputStream();
    }

    @Override
    public PrintWriter getPrintWriter(String contentType) throws IOException
    {
        _logger.info("getPrintWriter");
        _resourceResponse.setContentType(contentType);
        _isCommited = true;
        return _resourceResponse.getWriter();
    }

}
