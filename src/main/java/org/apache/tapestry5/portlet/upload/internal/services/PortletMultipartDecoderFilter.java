// Copyright 2006 The Apache Software Foundation
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
package org.apache.tapestry5.portlet.upload.internal.services;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.tapestry5.portlet.services.PortletActionRequestFilter;
import org.apache.tapestry5.portlet.services.PortletActionRequestHandler;
import org.apache.tapestry5.portlet.upload.services.PortletMultipartDecoder;

/**
 * @author Raphael Jean
 * @ccordenier
 */
public class PortletMultipartDecoderFilter implements PortletActionRequestFilter
{
    private PortletMultipartDecoder decoder;

    public PortletMultipartDecoderFilter(PortletMultipartDecoder decoder)
    {
        this.decoder = decoder;
    }

    public boolean service(ActionRequest request, ActionResponse response,
            PortletActionRequestHandler servicer) throws IOException, PortletException
    {
        ActionRequest newRequest = PortletFileUpload.isMultipartContent(request) ? decoder.decode(request) : request;
        return servicer.service(newRequest, response);
    }

}
