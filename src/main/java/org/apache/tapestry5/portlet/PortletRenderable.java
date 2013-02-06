package org.apache.tapestry5.portlet;

import java.util.Hashtable;
import java.util.Map;

/**
 * 	PortletRenderable is a class that ease the communication between two portlets
 *   
 * 	@author ffacon
 */
public class PortletRenderable
{

	/**
     * The logical name of the portlet to link to.
     */
    private String pageName;

    /**
     * map of render parameters that hold the data for the targeted Portlet 
     */
    private Map<String, String> renderParameters = new Hashtable<String, String>();

    public PortletRenderable(String pageName)
    {
        this.pageName = pageName;
    }

    public void addRenderParameter(String name, String value)
    {
        renderParameters.put(name, value);
    }

    public String getPageName()
    {
        return pageName;
    }

    public void setPageName(String pageName)
    {
        this.pageName = pageName;
    }

    public Map<String, String> getRenderParameters()
    {
        return renderParameters;
    }

    public void setRenderParameters(Map<String, String> renderParameters)
    {
        this.renderParameters = renderParameters;
    }

}
