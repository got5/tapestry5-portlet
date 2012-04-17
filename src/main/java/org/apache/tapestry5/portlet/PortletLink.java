package org.apache.tapestry5.portlet;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

public interface PortletLink {

	void setWindowState(WindowState windowState);
	
	void setMode(PortletMode mode);
	
}
