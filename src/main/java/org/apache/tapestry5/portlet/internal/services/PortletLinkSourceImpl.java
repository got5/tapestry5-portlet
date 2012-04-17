package org.apache.tapestry5.portlet.internal.services;

import java.util.List;

import javax.portlet.MimeResponse;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;
import javax.portlet.StateAwareResponse;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.internal.InternalComponentResources;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.internal.services.ArrayEventContext;
import org.apache.tapestry5.internal.services.PageActivationContextCollector;
import org.apache.tapestry5.internal.services.PageRenderQueue;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.internal.transform.ParameterConduit;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.portlet.services.PortletLinkSource;
import org.apache.tapestry5.portlet.services.PortletRequestGlobals;
import org.apache.tapestry5.portlet.services.PortletResourceResponseIdentifier;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.LinkCreationHub;
import org.apache.tapestry5.services.LinkCreationListener;
import org.apache.tapestry5.services.LinkCreationListener2;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.RequestGlobals;
import org.slf4j.Logger;

public class PortletLinkSourceImpl
		implements PortletLinkSource, LinkCreationHub {

	private final PageRenderQueue pageRenderQueue;

	private final PageActivationContextCollector contextCollector;

	private final ComponentEventLinkEncoder linkEncoder;

	private final List<LinkCreationListener2> listeners = CollectionFactory.newThreadSafeList();

	private final TypeCoercer typeCoercer;

	private final ComponentClassResolver resolver;

	private final PortletRequestGlobals portletGlobals;

	private final ComponentSource componentSource;
	
	private final RequestGlobals requestGlobals;
	
	private final RequestPageCache pageCache;
	
	private final PortletResourceResponseIdentifier resourceResponseIdentifier;
	
	private final Logger log;

	public PortletLinkSourceImpl(PageRenderQueue pageRenderQueue, PageActivationContextCollector contextCollector,
			TypeCoercer typeCoercer, ComponentClassResolver resolver, ComponentEventLinkEncoder linkEncoder,
			RequestGlobals requestGlobals,PortletRequestGlobals portletGlobals, RequestPageCache pageCache,
			ComponentSource componentSource,
			PortletResourceResponseIdentifier resourceResponseIdentifier,
			Logger log) {
		this.pageRenderQueue = pageRenderQueue;
		this.contextCollector = contextCollector;
		this.typeCoercer = typeCoercer;
		this.resolver = resolver;
		this.linkEncoder = linkEncoder;
		this.portletGlobals = portletGlobals;
		this.componentSource = componentSource;
		this.requestGlobals = requestGlobals;
		this.pageCache = pageCache;
		this.resourceResponseIdentifier = resourceResponseIdentifier;
		this.log = log;
	}

	public Link createComponentEventLink(Page page, String nestedId, String eventType, boolean forForm,
			Object... eventContext) {

		String baseMsg = "PortletLinkSourceImpl->createComponentEventLink page="+page+" eventType="+eventType;
		Link link = createComponentEventLinkForServletContainer(page, nestedId, eventType, forForm, eventContext);

		// If in action request then return the default generated link
		if(portletGlobals.getPortletResponse() != null && portletGlobals.getPortletResponse() instanceof StateAwareResponse) 
		{
			log.info("----------- actionRequest then return link="+link.toString() );
		    return link;
		}
		
		Component component = componentSource.getComponent(page.getName() + ":" + toBlank(nestedId));
		boolean isXHR = isXHR(page.getName(),nestedId,eventType);
		
		// If in portlet request then generate the corresponding portlet URL
		if (portletGlobals.getPortletRequest() != null) {
			// In case of action response, it's not possible to generate URL
			// from the portlet
			// action response standard API.
			if (StateAwareResponse.class.isInstance(portletGlobals.getPortletResponse())) {
				throw new IllegalStateException("URL cannot be generated from action or event request");
			}

			// If event starts with serve prefix that means we need to serve a
			// resource
			if (eventType!=null && ( isXHR || eventType.startsWith("serve")) ) 
			{
				ResourceURL resourceUrl = MimeResponse.class.cast(portletGlobals.getPortletResponse()).createResourceURL();
				Link res = new ResourceLinkImpl(resourceUrl, link, portletGlobals.getPortletResponse());
				log.info("----------- resourceRequest then return link="+res.toString() );
				return res;
			} else {
				PortletURL actionUrl = MimeResponse.class.cast(portletGlobals.getPortletResponse()).createActionURL();
				Link act=new PortletLinkImpl(actionUrl, link, portletGlobals.getPortletResponse());
				log.info("----------- actionRequest then return link="+act.toString() );
				return act;
			}
		}

		return link;
	}

	private String toBlank(String input) {
		return input == null ? "" : input;
	}

	public Link createPageRenderLink(String pageName, boolean override, Object... pageActivationContext) {

		String baseMsg = "PortletLinkSourceImpl->createPageRenderLink page="+pageName;
		Link link = createPageRenderLinkForServletContainer(pageName, override, pageActivationContext);

	      // If in action request then return the default generated link
        if(portletGlobals.getPortletResponse() != null && portletGlobals.getPortletResponse() instanceof StateAwareResponse) {
        	log.info(baseMsg + " actionRequest then return link="+link.toString() );
        	return link;
        }
		
		// If in portlet request then generate the corresponding portlet URL
		if (portletGlobals.getPortletRequest() != null) {
			// In case of action response, it's not possible to generate URL
			// from the portlet
			// action response standard API.
			if (StateAwareResponse.class.isInstance(portletGlobals.getPortletResponse())) {
				throw new IllegalStateException("URL cannot be generated from ressources or event request");
			}

			PortletURL actionUrl = MimeResponse.class.cast(portletGlobals.getPortletResponse()).createRenderURL();
			Link portletLink = new PortletLinkImpl(actionUrl, link, portletGlobals.getPortletResponse());
			log.info(baseMsg + " actionRequest then return link="+portletLink.toString() );
			return portletLink;
		}

		return link;
	}

	@Override
	public Link createComponentEventLinkForServletContainer(Page page, String nestedId, String eventType,
			boolean forForm, Object... eventContext) {
		assert page!=null;
		assert InternalUtils.isNonBlank(eventType);

		Page activePage = pageRenderQueue.getRenderingPage();

		// See TAPESTRY-2184
		if (activePage == null)
			activePage = page;

		String activePageName = activePage.getName();

		Object[] pageActivationContext = contextCollector.collectPageActivationContext(activePageName);

		ComponentEventRequestParameters parameters = new ComponentEventRequestParameters(activePageName,
				page.getName(), toBlank(nestedId), eventType,
				new ArrayEventContext(typeCoercer, pageActivationContext), new ArrayEventContext(typeCoercer,
						eventContext));

		Link link = linkEncoder.createComponentEventLink(parameters, forForm);

		for (LinkCreationListener2 listener : listeners)
            listener.createdComponentEventLink(link, parameters);

		return link;
	}

	@Override
	public Link createPageRenderLinkForServletContainer(String pageName, boolean override,
			Object... pageActivationContext) {
		// Resolve the page name to its canonical format (the best version for
		// URLs). This also
		// validates the page name.

		String canonical = resolver.canonicalizePageName(pageName);

		Object[] context = (override || pageActivationContext.length != 0) ? pageActivationContext : contextCollector
				.collectPageActivationContext(canonical);
		
		 boolean loopback = canonical.equals(requestGlobals.getActivePageName())
         && pageCache.get(pageName).hasResetListeners();

		PageRenderRequestParameters parameters = new PageRenderRequestParameters(canonical, new ArrayEventContext(
				typeCoercer, context),loopback);

		Link link = linkEncoder.createPageRenderLink(parameters);

		for (LinkCreationListener2 listener : listeners)
			listener.createdPageRenderLink(link,parameters);

		return link;
	}

	public LinkCreationHub getLinkCreationHub() {
		return this;
	}

	public void addListener(LinkCreationListener listener) {
		assert listener!=null;

		 addListener(TapestryInternalUtils.toLinkCreationListener2(listener));
	}

	public void removeListener(LinkCreationListener listener) {
		assert listener!=null;

		throw new UnsupportedOperationException("Removing listeners from LinkSource is not longer supported.");
	}

	@Override
	public void addListener(LinkCreationListener2 listener) {
		 assert listener != null;

	     listeners.add(listener);
	}

	private boolean isXHR(String containingPageName, String nestedComponentId, String eventType) {
       // get the element for which we are creating the Link to see if it's triggering a zone (i.e. is an AJAX event).
       // EventLink is using the containing page's ComponentResource to generate the url so nestedComponentId is ALWAYS null!
       if (!InternalUtils.isBlank(nestedComponentId)) {
	        //check if it's zone
	        String cptName = containingPageName + ":" + nestedComponentId;
	        Component component = this.componentSource.getComponent(cptName);
	        ComponentResources componentResources = component.getComponentResources();
	        InternalComponentResources internalRes = (InternalComponentResources) componentResources;
	        Class cpt = component.getClass();
	        String cptClassName = cpt.getName();
	       
	         	
	        componentResources.getComponentModel().getEmbeddedComponentIds();
	        boolean zoneBounded = componentResources.isBound("zone");
	        if(zoneBounded)
	        {
	        	ParameterConduit paramConduit = internalRes.getParameterConduit("zone");
	        	log.info(cptName+" "+paramConduit.toString());
	        	//Fix: make sure zone is bound to a real component       
	        	if(paramConduit.get()==null) return false;
	        	else return true;
	        	
	        }
       	}	
        return      resourceResponseIdentifier.isResourceResponseHandler(containingPageName, nestedComponentId, eventType) ;
    }
}
