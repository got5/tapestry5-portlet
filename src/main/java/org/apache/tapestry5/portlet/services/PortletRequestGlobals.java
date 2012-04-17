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

package org.apache.tapestry5.portlet.services;

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

/**
 * Stores the current Portlet request and response, for access by other
 * services.
 * 
 */
public interface PortletRequestGlobals
{

    void store(ActionRequest request, ActionResponse response);

    void store(RenderRequest request, RenderResponse response);
    
    void store(ResourceRequest request, ResourceResponse response);
    
    void store(EventRequest request, EventResponse response);
    
    PortletRequest getPortletRequest();
    
    PortletResponse getPortletResponse();

    ActionRequest getActionRequest();

    ActionResponse getActionResponse();

    RenderRequest getRenderRequest();

    RenderResponse getRenderResponse();
    
    ResourceRequest getResourceRequest();
    
    ResourceResponse getResourceResponse();

    EventRequest getEventRequest();
    
    EventResponse getEventResponse();

}
