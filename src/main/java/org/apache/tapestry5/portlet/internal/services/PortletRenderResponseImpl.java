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

import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper around {@link javax.portlet.RenderResponse}&nbsp;to adapt it as
 * {@link org.apache.tapestry5.portlet.internal.services.PortletResponseImpl}.
 * 
 * @since 4.0
 */
public class PortletRenderResponseImpl extends PortletResponseImpl
{
    private final Logger _logger = LoggerFactory.getLogger(PortletRenderResponseImpl.class);

    private final RenderResponse renderResponse;

    public PortletRenderResponseImpl(RenderResponse renderResponse)
    {
        super(renderResponse, null);

        this.renderResponse = renderResponse;
    }

    public void reset()
    {
        renderResponse.reset();
    }

    public PrintWriter getPrintWriter(String contentType) throws IOException
    {
        _logger.info("getPrintWriter");
        renderResponse.setContentType(contentType.toString());
        return renderResponse.getWriter();
    }

    public String getNamespace()
    {
        return renderResponse.getNamespace();
    }

    @Override
    public boolean isCommitted()
    {
        _logger.info("isCommited");
        return renderResponse.isCommitted();
    }

    @Override
    public OutputStream getOutputStream(String contentType) throws IOException
    {
        _logger.info("getOutputStream");
        renderResponse.setContentType(contentType);
        return renderResponse.getPortletOutputStream();
    }
}
