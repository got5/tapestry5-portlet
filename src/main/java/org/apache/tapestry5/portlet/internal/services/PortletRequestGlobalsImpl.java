// Copyright 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.portlet.internal.services;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.tapestry5.portlet.services.PortletRequestGlobals;

/**
 * Implementation of the tapestry.portlet.PortletRequestGlobals service, which
 * uses the threaded service lifecycle model.
 * 
 * 
 * @since 4.0
 */
public class PortletRequestGlobalsImpl implements PortletRequestGlobals
{
	
	private PortletRequest _portletRequest;
	
	private PortletResponse _portletResponse;
	
	private ActionRequest _actionRequest;

    private ActionResponse _actionResponse;

    private RenderResponse _renderResponse;

    private RenderRequest _renderRequest;
    
    private ResourceRequest _resourceRequest;
    
    private ResourceResponse _resourceResponse;
    
    private EventRequest _eventRequest;
    
    private EventResponse _eventResponse;

    public void store(ActionRequest request, ActionResponse response)
    {
        _actionRequest = request;
        _portletRequest = request;
        _actionResponse = response;
        _portletResponse = response;
    }

    public void store(RenderRequest request, RenderResponse response)
    {
        _renderRequest = request;
        _portletRequest = request;
        _renderResponse = response;
        _portletResponse = response;
    }
    
    public void store(ResourceRequest request, ResourceResponse response)
    {
    	_resourceRequest = request;
    	_resourceResponse = response;
    	_portletRequest = request;
    	_portletResponse = response;
    }
    
    public void store(EventRequest request, EventResponse response)
    {
    	_eventRequest = request;
    	_eventResponse = response;
    	_portletRequest = request;
    	_portletResponse = response;
    }
    
    public PortletRequest getPortletRequest()
    {
    	return _portletRequest;
    }
    
    public PortletResponse getPortletResponse()
    {
    	return _portletResponse;
    }

    public ActionRequest getActionRequest()
    {
        return _actionRequest;
    }

    public ActionResponse getActionResponse()
    {
        return _actionResponse;
    }

    public RenderRequest getRenderRequest()
    {
        return _renderRequest;
    }

    public RenderResponse getRenderResponse()
    {
        return _renderResponse;
    }
    
    public ResourceRequest getResourceRequest(){
    	return _resourceRequest;
    }
    
    public ResourceResponse getResourceResponse(){
    	return _resourceResponse;
    }

	public EventRequest getEventRequest() {
		return _eventRequest;
	}

	public EventResponse getEventResponse() {
		return _eventResponse;
	}
    
    

}
