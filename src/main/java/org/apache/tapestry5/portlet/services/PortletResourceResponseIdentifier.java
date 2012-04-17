package org.apache.tapestry5.portlet.services;

/**
 * PortletResourceResponseIdentifier service is used to register component handler that could return ajax response
 * Ajax Request should be treat as resource request. This service is used by PortletLinsSourceImpl
 * 
 * @author ffacon
 */
public interface PortletResourceResponseIdentifier {
	
	boolean isResourceResponseHandler(String containingPageName, String nestedComponentId, String eventType);

}
