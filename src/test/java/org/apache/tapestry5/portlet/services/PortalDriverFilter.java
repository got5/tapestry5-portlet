package org.apache.tapestry5.portlet.services;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.portlet.WindowState;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pluto.container.PortletContainer;
import org.apache.pluto.container.PortletWindow;
import org.apache.pluto.driver.AttributeKeys;
import org.apache.pluto.driver.core.PortalRequestContext;
import org.apache.pluto.driver.core.PortletWindowImpl;
import org.apache.pluto.driver.services.portal.PortletWindowConfig;
import org.apache.pluto.driver.url.PortalURL;

public class PortalDriverFilter extends org.apache.pluto.driver.PortalDriverFilter
{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException
    {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse)
        {
            HttpServletRequest req = (HttpServletRequest) request;
            // Since we must support a 2.3 environment, we can't use
            // filter dispatchers. B/C of this, we make sure we haven't
            // allready processed this request. No infinite loops for us!!!!
            if (PortalRequestContext.getContext(req) == null)
            {
                boolean actionRequestProcessed = doPortletPrepare(req,
                        (HttpServletResponse) response);

                if (actionRequestProcessed) { return; }

            }

            doPortletRender(req, (HttpServletResponse) response);

            response.flushBuffer();
            return;
        }
        super.doFilter(request, response, filterChain);
    }

    public boolean doPortletRender(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {

        // Retrieve the portlet window config for the evaluated portlet ID.
        ServletContext servletContext = super.getServletContext();
        System.out.println("servletContext"+servletContext.getContextPath());
        PortletWindowConfig windowConfig = PortletWindowConfig.fromId("tapestry5-portlet.Index");

        // Retrieve the current portal URL.
        PortalRequestContext portalEnv = PortalRequestContext.getContext(request);
        PortalURL portalURL = portalEnv.getRequestedPortalURL();

        // Retrieve the portlet container from servlet context.
        PortletContainer container = (PortletContainer) servletContext
                .getAttribute(AttributeKeys.PORTLET_CONTAINER);

        // Create the portlet window to render.
        PortletWindow window = null;

        try
        {
            window = new PortletWindowImpl(container, windowConfig, portalURL);
        }
        catch (RuntimeException e) // FIXME: Prose a change to anything else, handle it.
        {
            throw new ServletException(e);
        }


        if (window != null)
        {
            // Check if someone else is maximized. If yes, don't show content.
            Map windowStates = portalURL.getWindowStates();
            for (Iterator it = windowStates.keySet().iterator(); it.hasNext();)
            {
                String windowId = (String) it.next();
                WindowState windowState = (WindowState) windowStates.get(windowId);
                if (WindowState.MAXIMIZED.equals(windowState)
                        && !window.getId().getStringId().equals(windowId)) { return false; }
            }

        }

        // Render the portlet and cache the response.
        try
        {
            if (portalURL.getResourceWindow() != null)
            {
                container.doServeResource(window, request, response);
                response.flushBuffer();
            }
            else
            {
                container.doRender(window, request, response);
                response.flushBuffer();
            }

            return true;
        }
        catch (Throwable th)
        {
            th.printStackTrace();
            return false;
        }

    }

}
