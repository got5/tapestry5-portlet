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

package org.apache.tapestry5.portlet;

/**
 * 
 */
public final class PortletConstants
{
	
	/**
	 * Render parameter name storing the page name to be invoked upon an action
	 */
    public static final String PORTLET_PAGE = "t:pp";
	
	/**
     * Render parameter name storing the portlet mode at the time the render URL was created. This
     * is necessary for determining when the mode has changed.
     */

    public static final String PORTLET_MODE = "t:pm";

    /**
     * Render parameter name storing the window state at the time the render URL was created. Again,
     * this is necessary for determining when the window state has changed.
     */

    public static final String WINDOW_STATE = "t:ws";
    
	/**
	 * This key is used to store the last exception that has occured on an
	 * action request.
	 */
	public static final String LAST_ACTION_EXCEPTION = "LAST_ACTION_EXCEPTION";
	
	/**
	 * This key is used to define a new persistence strategy for objects annoted with
	 * SessionState. This way, scope is Application instead of Portlet.
	 */
	public static final String SESSION_STATE_APPLICATION_SCOPE = "sessionApplication";
	


	
}
