// Copyright 2012,2013 The Apache Software Foundation
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
package org.apache.tapestry5.portlet.internal.services;

import javax.portlet.PortletRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.SessionPersistedObjectAnalyzer;


/**
 * 
 * 
 * @author ffacon
 * 
 */
public interface PortalUtilities{;

/**
 * This method create the corresponding PortletRequest depending on the request
 * type this method may be overrided to use PortletRequest implementation for a portlet container. 
 * 
 * @param request
 *            The incoming portlet request
 * @param pageName
 *            The Tapestry pageName requested
 * @param analyzer
 *            Needed to create the subsequent tapestry portletRequest
 *            object.
 * @return
 */
public Request buildPortletRequest(PortletRequest request, String pageName, SessionPersistedObjectAnalyzer analyzer);

	
/**
 * This method create the corresponding ResourceResponse depending on the request
 * type this method may be overrided to use ResourceResponse implementation by the portlet container. 
 * 
 * @param response
 *            The outgoing portlet response
 * @return
 */
public Response buildResourceReponse(ResourceResponse response);
	
	
/**
 * Even if portletRequest provide many informations from the original HttpServletRequest, it is needed some times get direct access to 
 * the original HttpServletRequest. This can only be done with the help of utilities provided by the portlet container.  
 * 
 * 
 * @return
 */
	public HttpServletRequest getOriginalServletRequest();

	
}
