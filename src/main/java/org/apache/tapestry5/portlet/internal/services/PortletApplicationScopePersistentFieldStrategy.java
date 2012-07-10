// Copyright 2006, 2007 The Apache Software Foundation
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

package org.apache.tapestry5.portlet.internal.services;

import static org.apache.tapestry5.ioc.internal.util.CollectionFactory.newList;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.portlet.PortletSession;

import org.apache.tapestry5.internal.services.AbstractSessionPersistentFieldStrategy;
import org.apache.tapestry5.internal.services.PersistentFieldChangeImpl;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.portlet.PortletUtilities;
import org.apache.tapestry5.portlet.services.PortletRequestGlobals;
import org.apache.tapestry5.services.PersistentFieldChange;
import org.apache.tapestry5.services.PersistentFieldStrategy;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Session;

/**
 * A strategy for storing persistent page properties into the {@link PortletSession session}.
 * <p/>
 * Builds attribute names as: <code>pas:<em>page-name</em>:<em>component-id</em>:<em>field-name</em></code>
 */

public class PortletApplicationScopePersistentFieldStrategy implements PersistentFieldStrategy
{
	/**
     * Prefix used to identify keys stored in the session.
     */
    static final String prefix = "pas:"; 

    private final PortletRequestGlobals globals;

    public PortletApplicationScopePersistentFieldStrategy(PortletRequestGlobals globals2)
    {
        this.globals = globals2;
    }

    public final Collection<PersistentFieldChange> gatherFieldChanges(String pageName)
    {
    	PortletSession session = globals.getPortletRequest().getPortletSession();

        if (session == null) return Collections.emptyList();

        List<PersistentFieldChange> result = newList();

        String fullPrefix = prefix + pageName + ":";

        for (String name : PortletUtilities.getAttributeNames(session,PortletSession.APPLICATION_SCOPE,fullPrefix))
        {
            Object persistedValue = session.getAttribute(name,PortletSession.APPLICATION_SCOPE);

            Object applicationValue = persistedValue == null ? null : convertPersistedToApplicationValue(
                    persistedValue);

            PersistentFieldChange change = buildChange(name, applicationValue);

            result.add(change);

      
        }

        return result;
    }

    public void discardChanges(String pageName)
    {
    	PortletSession session = globals.getPortletRequest().getPortletSession();

        if (session == null) return;

        String fullPrefix = prefix + pageName + ":";

        for (String name :  PortletUtilities.getAttributeNames(session,PortletSession.APPLICATION_SCOPE,fullPrefix))
        {
            session.setAttribute(name, null,PortletSession.APPLICATION_SCOPE);
        }
    }

    

    private PersistentFieldChange buildChange(String name, Object newValue)
    {
        String[] chunks = name.split(":");

        // Will be empty string for the root component
        String componentId = chunks[2];
        String fieldName = chunks[3];

        return new PersistentFieldChangeImpl(componentId, fieldName, newValue);
    }

    public final void postChange(String pageName, String componentId, String fieldName,
                                 Object newValue)
    {
        assert InternalUtils.isNonBlank(pageName);
        assert InternalUtils.isNonBlank(fieldName);
        Object persistedValue = newValue == null ? null : convertApplicationValueToPersisted(newValue);

        StringBuilder builder = new StringBuilder(prefix);
        builder.append(pageName);
        builder.append(':');

        if (componentId != null) builder.append(componentId);

        builder.append(':');
        builder.append(fieldName);

        
        PortletSession session = globals.getPortletRequest().getPortletSession(persistedValue != null);


        // TAPESTRY-2308: The session will be false when newValue is null and the session
        // does not already exist.

        if (session != null)
        {
            session.setAttribute(builder.toString(), persistedValue,PortletSession.APPLICATION_SCOPE);
        }
    }

    /**
     * Hook that allows a value to be converted as it is written to the session. Passed the new value provided by the
     * application, returns the object to be stored in the session. This implementation simply returns the provided
     * value.
     *
     * @param newValue non-null value
     * @return persisted value
     * @see #convertPersistedToApplicationValue(Object)
     */
    protected Object convertApplicationValueToPersisted(Object newValue)
    {
        return newValue;
    }

    /**
     * Converts a persisted value stored in the session back into an application value.   This implementation returns
     * the persisted value as is.
     *
     * @param persistedValue non-null persisted value
     * @return application value
     * @see #convertPersistedToApplicationValue(Object)
     */
    protected Object convertPersistedToApplicationValue(Object persistedValue)
    {
        return persistedValue;
    }
    
    
}
