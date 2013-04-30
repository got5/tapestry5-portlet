package org.apache.tapestry5.portlet.services;

import javax.portlet.PortletSession;

import org.apache.tapestry5.portlet.services.PortletRequestGlobals;
import org.apache.tapestry5.services.ApplicationStateCreator;
import org.apache.tapestry5.services.ApplicationStatePersistenceStrategy;

/**
 * Persistence strategy for SessionState objects: scope Application instead of Portlet. 
 */
public class PorletSessionApplicationStatePersistenceStrategy implements ApplicationStatePersistenceStrategy {
	
	static final String PREFIX = "sso:";
	
    private final PortletRequestGlobals globals;

    public PorletSessionApplicationStatePersistenceStrategy(PortletRequestGlobals globals)
    {
        this.globals = globals;
    }
    
    protected PortletSession getSession()
    {
        return globals.getPortletRequest().getPortletSession();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> ssoClass, ApplicationStateCreator<T> creator)
    {
        return (T) getOrCreate(ssoClass, creator);
    }
    
    protected <T> Object getOrCreate(Class<T> ssoClass, ApplicationStateCreator<T> creator)
    {
    	PortletSession session = getSession();

        String key = buildKey(ssoClass);

        Object sso = session.getAttribute(key, PortletSession.APPLICATION_SCOPE);

        if (sso == null)
        {
            sso = creator.create();
            set(ssoClass, (T) sso);
        }
        
        return sso;
    }

    protected <T> String buildKey(Class<T> ssoClass)
    {
        return PREFIX + ssoClass.getName();
    }

    public <T> void set(Class<T> ssoClass, T sso)
    {
        String key = buildKey(ssoClass);
        
        getSession().setAttribute(key, sso, PortletSession.APPLICATION_SCOPE);
    }

    public <T> boolean exists(Class<T> ssoClass)
    {
        String key = buildKey(ssoClass);

        PortletSession session = getSession();

        return session != null && session.getAttribute(key, PortletSession.APPLICATION_SCOPE) != null;
    }
}
