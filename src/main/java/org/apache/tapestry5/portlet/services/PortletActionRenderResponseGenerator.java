package org.apache.tapestry5.portlet.services;

import java.io.IOException;

import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.portlet.PortalPage;

/**
 * {@link ActionRenderResponseGenerator}
 * 
 * This class has been created to avoid having duplicate implementation of
 * default ActionRenderResponseGenerator that causes registry startup failure.
 * 
 * @author ccordenier
 * @author ffacon
 */
public interface PortletActionRenderResponseGenerator {

	/**
	 * see {@link ActionRenderResponseGenerator#generateResponse(Page)}
	 * @param page
	 * @throws IOException
	 */
	void generateResponse(Page page) throws IOException;
	
	void generateRedirect(PortalPage page) throws IOException;

}