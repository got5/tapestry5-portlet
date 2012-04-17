package org.apache.tapestry5.portlet.internal.services;

import org.apache.tapestry5.ioc.util.IdAllocator;
import org.apache.tapestry5.portlet.services.PortletIdAllocatorFactory;
import org.apache.tapestry5.portlet.services.PortletRequestGlobals;

public class PortletIdAllocatorFactoryImpl
        implements PortletIdAllocatorFactory {

    private final PortletRequestGlobals requestGlobals;

    public PortletIdAllocatorFactoryImpl(PortletRequestGlobals requestGlobals) {
        super();
        this.requestGlobals = requestGlobals;
    }

    private String getPortletId() {
        return requestGlobals.getPortletRequest().getWindowID();
    }

    public IdAllocator getNewPortletIdAllocator() {
        IdAllocator retour = new IdAllocator();
        if(requestGlobals.getPortletRequest() != null) {
            return new IdAllocator(getPortletId().replaceAll("[^A-za-z0-9]", ""));
        }
        return retour;
    }

    public IdAllocator getNewPortletIdAllocator(String namespace) {
        if(requestGlobals.getPortletRequest() != null) {
            return new IdAllocator(namespace + getPortletId().replaceAll("[^A-za-z0-9]", ""));
        }
        return new IdAllocator(namespace);
    }

}
