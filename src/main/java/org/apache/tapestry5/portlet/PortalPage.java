package org.apache.tapestry5.portlet;

import java.util.Locale;

/**
 * Class used to ease the redirect to a specific PortalPage
 * 
 */
public class PortalPage {
	
	 private final String name;

	 private final Locale locale;

	 /**
	     * @param name
	     *   				canonicalized page name
	     * @param locale
	     *            		locale for page and all components
	     */
	 public PortalPage(String name, Locale locale)
	 {
		 this.name = name;
	     this.locale = locale;
	       
	  } 
	 
	 public PortalPage(String name)
	 {
		 this.name = name;
	     this.locale = null;
	       
	  } 
	
	public String getName() {
		return name;
	}

	public Locale getLocale() {
		return locale;
	}

}
