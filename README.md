# Tapestry 5 Portlet Bridge Module - 5.4.0


## Changelog
- 5.4.0 : Update to Tapestry 5.4.0
- 5.4-rc-1 : Update to Tapestry 5.4-rc-1
- 5.4-beta-35 : Update to Tapestry 5.4-beta-35
- 5.4-beta-26 : Update to Tapestry 5.4-beta-26
- 5.4-beta-22 : Update to Tapestry 5.4-beta-22 
- 5.4-alpha-1-SNAPSHOT : Update to Tapestry 5.4-alpha-1 
- 5.3.8.0: Update to Tapestry 5.3.8 -> branch maint-5.3
- 5.3.7.0: removing commons-io dependency 
- 5.3.6: fix #4 Avoid clientID duplication 
- 5.3.5 : Update to Tapestry 5.3.7 
- 5.3.4 : new persistence strategy for SessionState annotation.
- 5.3.3 : Update to Tapestry 5.3.6 + Remove Liferay dependencies     
- 5.3.2 : Update To Tapestry 5.3.5 + CookieSink & CookieSource 		
- 5.3.1 : Adding templating, PortletApplicationScopePersistentFieldStrategy
- 5.3.1-SNAPSHOT : Update to Tapestry 5.3.4 
- 5.3.0-SNAPSHOT : Update to Tapestry 5.3.3
- 5.2.0-SNAPSHOT : Update to Tapestry 5.2.6 -> branch : maint-5.2

If you want to see it in action, you just need to:
- Download the sources
- Go to the repository directory
- Run "mvn jetty:run"
- Open your browser to http://localhost:8080/tapestry5-portlet/portal/Index

If you are gradle user you can try:
- gradle jettyRun


This library is based on work of Markus Feindler, Le Xuan Trun and Kristina B. Taylor 
(see http://code.google.com/p/tapestry5portlet/ for more details).

Like Felix Scheffer (http://code.google.com/p/tapestry5-portlet-support/ for more details), 
we have updated the dependencies of this library to use Tapestry 5.2.6 and upper version

This library support:
- portlet event processing (JSR 286)
- serving ajax request as portlet resource (event name that start with serve or components declared in the PortletResourceResponseIdentifier service)
- support MARKUP_HEAD_ELEMENT
- rework on IdAllocator to avoid conflict for generated id when there is more than on tapestry portlet in a page. 
- use of Apache Pluto to ease the test 
	

