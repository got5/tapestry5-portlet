// Copyright 2012, 2013 The Apache Software Foundation
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

package org.apache.tapestry5.internal.services;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.internal.structure.ComponentPageElement;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.internal.test.InternalBaseTestCase;
import org.apache.tapestry5.portlet.PortletConstants;
import org.apache.tapestry5.portlet.PortletRenderable;
import org.apache.tapestry5.portlet.PortletUtilities;
import org.apache.tapestry5.portlet.internal.services.PortletActionResultProcessor;
import org.apache.tapestry5.portlet.internal.services.PortletClassResultProcessor;
import org.apache.tapestry5.portlet.internal.services.PortletEventResultProcessor;
import org.apache.tapestry5.portlet.internal.services.PortletRequestGlobalsImpl;
import org.apache.tapestry5.portlet.services.PortletLinkSource;
import org.apache.tapestry5.portlet.services.PortletRequestGlobals;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ComponentEventResultProcessor;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;

public class PortletResultProcessorTest extends InternalBaseTestCase
{
    @Test
    public void portlet_event_result_processor() throws IOException
    {
        String eventName = "nip";
        String eventQname = "nop";
        String eventValue = "nup";
              
        PortletRequestGlobals portletGlobals = new PortletRequestGlobalsImpl(); 
        ActionRequest actionRequest = mockActionRequest();
        ActionResponse actionResponse = mockActionResponse();
        portletGlobals.store(actionRequest, actionResponse);
     
        Event event = mockEvent();
        
        train_getEventName(event, eventName);
        train_getEventValue(event, eventValue);
        actionResponse.setEvent(eventName, eventValue);
        
        replay();

        ComponentEventResultProcessor<Event> processor = new PortletEventResultProcessor(portletGlobals);

        processor.processResultValue(event);

        verify();
    }

    @Test
    public void portlet_action_result_processor() throws IOException
    {
    	String pageName ="index";
    	Map<String, String> renderParameters = new Hashtable<String, String>();
    	
    	PortletLinkSource portletLinkSource = mockPortletLinkSource();
    	PortletRequestGlobals portletGlobals = new PortletRequestGlobalsImpl(); 
        ActionRequest actionRequest = mockActionRequest();
        ActionResponse actionResponse = mockActionResponse();
        portletGlobals.store(actionRequest, actionResponse);
        PortletRenderable renderable = new PortletRenderable(pageName);
        Link link = mockLink();
        
        Event event = mockEvent();
        expect(portletLinkSource.createPageRenderLink(pageName, false)).andReturn(link).atLeastOnce();
        expect(link.toURI()).andReturn(pageName).atLeastOnce();
        actionResponse.setRenderParameter(PortletConstants.PORTLET_PAGE,pageName);
        
        replay();

        ComponentEventResultProcessor<PortletRenderable> processor = new PortletActionResultProcessor(portletLinkSource,portletGlobals);

        processor.processResultValue(renderable);

        verify();
    }
   
}
