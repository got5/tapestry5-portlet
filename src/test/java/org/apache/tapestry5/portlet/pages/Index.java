package org.apache.tapestry5.portlet.pages;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import javax.portlet.Event;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.pluto.driver.services.container.EventImpl;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.portlet.PortalPage;
import org.apache.tapestry5.portlet.PortletRenderable;
import org.apache.tapestry5.portlet.services.PortletRequestGlobals;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.upload.services.UploadedFile;

/**
 * Start page of application tapestry5 portlet.
 */
public class Index
{

    @Inject
    @Property
    private Request request;

    @Inject
    private PortletRequestGlobals globals;

    @InjectComponent
    private Zone dateZone;

    @InjectComponent
    private Zone formResultZone;
    
    @InjectComponent
    private Zone surNameZone;

    @Property
    private String firstName;
    
    @Property
    private String surName;

    @Property
    @Persist
    private String lastName;

    @Inject
    private LinkSource linkSource;

    @Property
    private UploadedFile file;
    
    @Persist
    @Property
    private String filename;

    public String getWindowState()
    {
        return globals.getPortletRequest().getWindowState().toString();
    }

    public String getPortletMode()
    {
        return globals.getPortletRequest().getPortletMode().toString();
    }

   
    public String getLastNamefromPortletSession()
    {
    	PortletSession session = globals.getPortletRequest().getPortletSession();
    	String WindowsId = globals.getPortletRequest().getWindowID();
    	String fabricatedAttributeNameBase = "javax.portlet.p."+WindowsId+"?";
    	String tapestryAttributeName= "state:Index::lastName";
    	
    	String lastNamefromApplicationScope = (String)session.getAttribute(fabricatedAttributeNameBase+tapestryAttributeName,PortletSession.APPLICATION_SCOPE);
    	String lastNamefromPortletScope = (String)session.getAttribute(tapestryAttributeName,PortletSession.PORTLET_SCOPE);
    	
    	assert(lastNamefromApplicationScope.equalsIgnoreCase(lastNamefromPortletScope));
    	
    	return lastNamefromPortletScope; 
    }
    
    
    public String getHttpServletRequestFromRenderRequest()
    {
    	RenderRequest renderRequest = globals.getRenderRequest();
    	HttpServletRequest httpRequest = (HttpServletRequest)  renderRequest.getAttribute("javax.portlet.portletc.httpServletRequest");		
    	return httpRequest.toString();
    }
    
    public Date getCurrentTime()
    {
        return new Date();
    }

    
    public PortletRenderable onActionFromToContactPage()
    {
        return new PortletRenderable("Contact");
    }
    
    public PortalPage onActionFromNavToAbout()
    {
        return new PortalPage("/tapestry5-portlet/portal/About");
    }


    public Event onPublishEvent()
    {
        return new EventImpl(new QName("sampleEvent"), "sampleEvent");
    }

    public Object onSampleEvent(String event){
        globals.getEventResponse().setRenderParameter("lastEvent", event);
        return this;
    }
    
    public Link onActionFromRefreshWithLink()
    {
        return linkSource.createPageRenderLink("Index", false);
    }

    public Object onActionFromAjaxDateRefresh()
    {
        return dateZone.getBody();
    }

    public String getDateZoneId()
    {
        return dateZone.getClientId();
    }

    public String getFormResultZoneId()
    {
        return formResultZone.getClientId();
    }
    
    public String getSurNameZoneId()
    {
        return surNameZone.getClientId();
    }

    @OnEvent("serveResource")
    public StreamResponse onServeResource()
    {
        System.out.println("fromdownload " + globals.getPortletRequest().getWindowState());
        return new StreamResponse()
        {

            public String getContentType()
            {
                return "application/schmock";
            }

            public InputStream getStream() throws IOException
            {
                return new ByteArrayInputStream(new String("hellohello").getBytes());
            }

            public void prepareResponse(Response arg0)
            {
                arg0.setHeader("Content-Disposition", "attachment; filename=greetings.txt");
                arg0.setHeader("Expires", "0");
                arg0.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                arg0.setHeader("Pragma", "public");

            }

        };
    }

    public Object onActionFromRefreshAction()
    {
        PortletRenderable renderable = new PortletRenderable("Index");
        renderable.addRenderParameter("rp1", "Render Parameter 1");
        renderable.addRenderParameter("rp2", "Render Parameter 2");
        return renderable;
    }

    /*public Object onActionFromReDownloadResource()
    {
        System.out.println("fromredownload");
        return Contact.class;
    }*/

    @OnEvent(component = "actionException")
    void onActionFromActionException()
    {
        Integer.parseInt("NaN");
    }

    Object onSuccessFromFirstNameForm()
    {
        return formResultZone.getBody();
    }
    
    Object onSuccessFromSurNameForm()
    {
        return surNameZone.getBody();
    }

    public void onSuccessFromUploadForm() throws IOException
    {
        File copy = File.createTempFile(file.getFileName(), null);
        System.out.println(copy.getPath());
        System.out.println("filename:"+filename);
        file.write(copy);
    }
}
