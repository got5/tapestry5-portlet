package org.apache.tapestry5.portlet.internal.services;

import java.io.IOException;

import javax.portlet.Event;

import org.apache.tapestry5.portlet.services.PortletRequestGlobals;
import org.apache.tapestry5.services.ComponentEventResultProcessor;

/**
 * This processor publishes an event in portlet action response.
 * 
 * @author ccordenier
 */
public class PortletEventResultProcessor implements ComponentEventResultProcessor<Event>
{

    private final PortletRequestGlobals globals;

    public PortletEventResultProcessor(PortletRequestGlobals globals)
    {
        this.globals = globals;
    }

    public void processResultValue(Event value) throws IOException
    {
        if (globals.getActionResponse() != null)
        {
            if (value.getName() != null)
            {
                globals.getActionResponse().setEvent(value.getName(), value.getValue());
            }
            else if (value.getQName() != null)
            {
                globals.getActionResponse().setEvent(value.getName(), value.getValue());
            }
            else
            {
                throw new IllegalArgumentException("Event name (or QName) is missing name");
            }
        }
    }
}
