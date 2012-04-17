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

package org.apache.tapestry5.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventPortlet;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceServingPortlet;

import org.apache.tapestry5.ioc.def.ModuleDef;
import org.apache.tapestry5.portlet.services.RegistryWrapper;

public class ApplicationPortlet implements Portlet, ResourceServingPortlet, EventPortlet
{

    private PortletConfig portletConfig;

    private RegistryWrapper registry;

    public void init(PortletConfig portletConfig) throws PortletException
    {
        this.portletConfig = portletConfig;
        this.registry = new RegistryWrapper(portletConfig.getPortletContext());
    }

    /**
     * Overridden in subclasses to provide additional module definitions beyond
     * those normally located. This implementation returns an empty array.
     */
    protected ModuleDef[] provideExtraModuleDefs(PortletContext context)
    {
        return new ModuleDef[0];
    }

    public void processAction(ActionRequest request, ActionResponse response)
            throws PortletException, IOException
    {
        try
        {
            registry.getPortletConfigProvider().store(this.portletConfig);
            registry.getActionHandler().service(request, response);
        }
        finally
        {
            registry.cleanupThread();
        }
    }

    public void render(RenderRequest request, RenderResponse response) throws PortletException,
            IOException
    {
        try
        {
            registry.getPortletConfigProvider().store(this.portletConfig);
            registry.getRenderHandler().service(request, response);
        }
        finally
        {
            registry.cleanupThread();
        }
    }

    public void serveResource(ResourceRequest request, ResourceResponse response)
            throws PortletException, IOException
    {
        try
        {
            registry.getPortletConfigProvider().store(this.portletConfig);
            registry.getResourceHandler().service(request, response);
        }
        finally
        {
            registry.cleanupThread();
        }
    }

    public void processEvent(EventRequest request, EventResponse response) throws PortletException,
            IOException
    {
        try
        {
            registry.getPortletConfigProvider().store(this.portletConfig);
            registry.getEventHandler().service(request, response);
        }
        finally
        {
            registry.cleanupThread();
        }

    }

    /** Shuts down and discards the registry. */
    public final void destroy()
    {
    }

}
