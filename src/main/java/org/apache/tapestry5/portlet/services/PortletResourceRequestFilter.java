package org.apache.tapestry5.portlet.services;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

public interface PortletResourceRequestFilter {
    boolean service(ResourceRequest request, ResourceResponse response,
            PortletResourceRequestHandler servicer)
        throws IOException, PortletException;
}
