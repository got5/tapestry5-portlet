package org.apache.tapestry5.portlet.services;

import java.io.IOException;

import org.apache.tapestry5.internal.structure.Page;

public interface PortletResponseRenderer
{
    void renderPageResponse(Page page) throws IOException;
}
