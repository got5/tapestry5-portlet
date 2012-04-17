// Copyright 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.portlet.services;

import javax.portlet.PortletRequest;

/**
 * Contribution used for resolving requests to named pages.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PortletPageResolverRule implements Comparable<PortletPageResolverRule>
{
//	private static final Logger _logger = LoggerFactory.getLogger(PortletPageResolverRule.class);
	
	private String _portletName;
    
    private String _mimeType;
	
    private String _portletMode;

    private String _windowState;

    private String _pageName;
    
    public PortletPageResolverRule(String portletName, String mimeType, 
    	String portletMode, String windowState, String pageName)
    {
    	_portletName = portletName;
    	_mimeType = mimeType;
    	_portletMode = portletMode;
    	_windowState = windowState;
    	_pageName = pageName;
    }
    
    public String getPortletName() 
    {
    	return _portletName;
    }
    
    public String getPortletMode()
    {
        return _portletMode;
    }

    public String getWindowState()
    {
        return _windowState;
    }
    
    public String getMimeType()
    {
    	return _mimeType;
    }

    public String getPageName()
    {
        return _pageName;
    }

    int sortScore()
    {
        int result = 0;
        
        if (_portletName != null)
        	result += 8;

        if (_mimeType != null)
            result += 4;

        if (_portletMode != null)
            result += 2;

        if (_windowState != null)
            result += 1;

        return result;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append(this.getClass().getName());
        builder.append("[");
        builder.append("portletName=" + _portletName);
        builder.append(",");
        builder.append("mimeType=" + _mimeType);
        builder.append(",");
        builder.append("portletMode=" + _portletMode);
        builder.append(",");
        builder.append("windowState=" + _windowState);
        builder.append(",");
        builder.append("pageName=" + _pageName);
        builder.append("]");

        return builder.toString();
    }

    public int compareTo(PortletPageResolverRule that)
    {
        int thisScore = this.sortScore();
        int otherScore = that.sortScore();

        // End result: sorted descending by specificity

        return otherScore - thisScore;
    }

    public boolean match(String portletName, PortletRequest request)
    {
//    	_logger.info(request.getResponseContentType());
    	System.out.println("CONTENT TYPE:");
    	System.out.println(request.getResponseContentType());
    	if (_portletName != null && !_portletName.equals(portletName))
    			return false;
    	
    	if (_mimeType != null && !_mimeType.equals(request.getResponseContentType()))
          return false;

      if (_portletMode != null && !_portletMode.equals(request.getPortletMode().toString()))
          return false;

      if (_windowState != null && !_windowState.equals(request.getWindowState().toString()))
          return false;

      return true;
    }
}
