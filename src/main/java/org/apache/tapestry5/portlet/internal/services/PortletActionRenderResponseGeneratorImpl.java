package org.apache.tapestry5.portlet.internal.services;

import java.io.IOException;

import javax.portlet.ActionResponse;
import javax.portlet.StateAwareResponse;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.portlet.PortalPage;
import org.apache.tapestry5.portlet.PortletConstants;
import org.apache.tapestry5.portlet.PortletUtilities;
import org.apache.tapestry5.portlet.services.PortletActionRenderResponseGenerator;
import org.apache.tapestry5.portlet.services.PortletLinkSource;
import org.apache.tapestry5.portlet.services.PortletRequestGlobals;

/**
 * Portlet action response can only modify render parameters for the following
 * render request. This implementation simply modify the parameter that hold the
 * Tapestry page to display.
 * 
 * @author ccordenier
 * @author ffacon
 */
public class PortletActionRenderResponseGeneratorImpl implements
        PortletActionRenderResponseGenerator
{

    private final PortletLinkSource linkSource;

    private final PortletRequestGlobals portletGlobals;

    public PortletActionRenderResponseGeneratorImpl(PortletLinkSource linkSource,
            PortletRequestGlobals portletGlobals)
    {
        super();
        this.linkSource = linkSource;
        this.portletGlobals = portletGlobals;
    }

    @Override
    public void generateResponse(Page page) throws IOException
    {
        Link result = linkSource.createPageRenderLink(page.getName(), false);

        // Add render parameter in the action response
        StateAwareResponse response = (StateAwareResponse) portletGlobals.getPortletResponse();
        response.setRenderParameter(PortletConstants.PORTLET_PAGE, PortletUtilities.stripQueryString(result.toURI()));
        for (String name : result.getParameterNames())
        {
            response.setRenderParameter(name, result.getParameterValue(name));
        }
    }

    @Override
    public void generateRedirect(PortalPage page) throws IOException
    {
        if (!(portletGlobals.getPortletResponse() instanceof ActionResponse)) { 
            throw new IllegalStateException(
                String.format("%s request does not support redirect", portletGlobals
                        .getPortletResponse().getClass().getName())); 
        }
        
        portletGlobals.getActionResponse().sendRedirect(page.getName());
    }

}
