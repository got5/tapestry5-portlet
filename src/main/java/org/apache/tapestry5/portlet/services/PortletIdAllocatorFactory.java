package org.apache.tapestry5.portlet.services;

import org.apache.tapestry5.ioc.util.IdAllocator;

/**
 * Allows to generate unique client id for tapestry form components.
 * 
 * @author awillemant
 */
public interface PortletIdAllocatorFactory {

    public IdAllocator getNewPortletIdAllocator();

    public IdAllocator getNewPortletIdAllocator(String namespace);

}
