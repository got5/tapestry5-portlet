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

import javax.portlet.PortletRequest;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;

/**
 * Factory used to create {@link org.apache.tapestry5.portlet.bindings.UserAttributeBinding}s.
 * 
 * 
 * @since 4.0
 */
public class UserAttributeBindingFactory implements BindingFactory
{
    private PortletRequest _request;
    
    public UserAttributeBindingFactory(PortletRequest request)
    {
        _request = request;
    }

    public Binding newBinding(String description, ComponentResources container,
            ComponentResources component, String expression, Location location)
    {   
        return new UserAttributeBinding(description, location, _request);
    }
}
