// Copyright 2008, 2010 The Apache Software Foundation
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

package org.apache.tapestry5.portlet;

/**
 * Constants for persistent field strategies.
 * 
 * @see org.apache.tapestry5.annotations.Persist#value()
 */
public class PortletPersistenceConstants
{
	/**
	 * A portlet can bind an object attribute into a PortletSession by name. 
	 * The PortletSession interface defines two scopes for storing objects:
	 *			- APPLICATION_SCOPE
	 * 			- PORTLET_SCOPE
	 * 
     * With @Persist(PortletSessionApplicationScope), the field's value is stored in the APPLICATION_SCOPE 
     * in the PortletSession. So value will be available to all the portlets .
     * Default @Persist storevalue in PORTLET_SCOPE
     * 
     */
    public static final String PORTLET_SESSION_APPLICATION_SCOPE = "PortletSessionApplicationScope";
}
