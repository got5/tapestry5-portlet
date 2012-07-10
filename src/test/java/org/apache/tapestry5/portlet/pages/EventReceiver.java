package org.apache.tapestry5.portlet.pages;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.portlet.PortletPersistenceConstants;
import org.apache.tapestry5.portlet.services.PortletRequestGlobals;
import org.apache.tapestry5.services.Request;

public class EventReceiver
{
    @Inject
    @Property
    private Request request;

    @Inject
    private PortletRequestGlobals globals;

    @Persist(PortletPersistenceConstants.PORTLET_SESSION_APPLICATION_SCOPE)
    @Property
    private String applicationScope;
    
    @Persist
    @Property
    private String portletScope;
    
   
	public Object onSampleEvent(String event)
    {
        globals.getEventResponse().setRenderParameter("lastEvent", event);
        return this;
    }

    public String getWindowState()
    {
        return globals.getPortletRequest().getWindowState().toString();
    }

    public String getPortletMode()
    {
        return globals.getPortletRequest().getPortletMode().toString();
        
    }
}
