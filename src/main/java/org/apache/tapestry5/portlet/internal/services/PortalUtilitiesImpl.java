// Copyright 2012 The Apache Software Foundation
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
import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.portlet.internal.services.PortletRequestImpl;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SessionPersistedObjectAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PortalUtilitiesImpl implements PortalUtilities
{

    private final Logger logger = LoggerFactory.getLogger(PortalUtilitiesImpl.class);


    public PortalUtilitiesImpl()
    {
        
    }

	public Request buildPortletRequest(PortletRequest request, String pageName, SessionPersistedObjectAnalyzer analyzer)
	{
		return  new PortletRequestImpl(request,pageName, analyzer);
	}

	public HttpServletRequest getOriginalServletRequest() {
		
		return null;
	}
	
	

}
