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

package org.apache.tapestry5.portlet.bindings;

import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.internal.util.TapestryException;

/**
 * Allows access to a Portlet user attrbute.
 * 
 * 
 * @since 4.0
 */
public class UserAttributeBinding extends AbstractBinding
{
    private final String _description;
	
	private final PortletRequest _request;

    public UserAttributeBinding(String description, Location location, 
    		PortletRequest request)
    {
        super(location);

        _description = description;
        _request = request;
    }
    
    public Object get()
    {
        return getUserInfo().get(_description);
    }
    
    @Override
    public void set(Object value)
    {
        getUserInfo().put(_description, (String) value);
    }

    public boolean isInvariant()
    {
        // These can always be changed.
        return false;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getUserInfo()
    {
        Map<String, String> result 
        	= (Map<String, String>) _request.getAttribute(PortletRequest.USER_INFO);

        if (result == null)
            throw new TapestryException(BindingsMessages.noUserInfo(), getLocation(),
                    null);

        return result;
    }
}
