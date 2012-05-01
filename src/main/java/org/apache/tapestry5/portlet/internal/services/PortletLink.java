package org.apache.tapestry5.portlet.internal.services;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

/**
 * Link interface with portlet specificities: window state and portlet mode. This is the bridge
 * needed between Tapestry link API and Portlet API.
 * 
 * @author ccordenier
 * @author ffacon
 */
public interface PortletLink
{

    void setWindowState(WindowState windowState);

    void setMode(PortletMode mode);

}
