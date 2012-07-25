package org.apache.tapestry5.portlet.pages;

import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;


public class About
{
	   @Inject
       private Cookies cookies;
       
	   @Property
	   private String nb;
	   
	   @BeginRender
       void onBeginRender() {
      	   	   nb =cookies.readCookieValue("numberOfView");
    	   	   if (nb==null) nb="1";
    	   	   else 
    	   	   {
    	   		long count;
    	   		   try
    	   		   {	
    	   			   count = Long.parseLong(nb);
    	   		   }
    	   		   catch(NumberFormatException excp)
    	   		   {
    	   			   count=0L;
    	   		   }
    	   		   count++;
    	   		   nb = Long.toString(count);
    	   	   }
               cookies.writeCookieValue("numberOfView", nb);  
       }
       
       void onActionFromResetCount()
       {
	   	   cookies.removeCookieValue("numberOfView");  
       }
}
