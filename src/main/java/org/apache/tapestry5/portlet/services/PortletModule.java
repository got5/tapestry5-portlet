// Copyright 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.portlet.services;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.StateAwareResponse;
import javax.servlet.http.Cookie;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.corelib.components.AjaxFormLoop;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.FormInjector;
import org.apache.tapestry5.corelib.internal.FormSupportImpl;
import org.apache.tapestry5.corelib.mixins.Autocomplete;
import org.apache.tapestry5.internal.services.CookieSink;
import org.apache.tapestry5.internal.services.CookieSource;
import org.apache.tapestry5.internal.services.DocumentLinker;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.internal.services.ObjectComponentEventResultProcessor;
import org.apache.tapestry5.internal.services.PageContentTypeAnalyzer;
import org.apache.tapestry5.internal.services.PageMarkupRenderer;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.internal.services.StreamResponseResultProcessor;
import org.apache.tapestry5.internal.services.ajax.JavaScriptSupportImpl;
import org.apache.tapestry5.internal.services.javascript.JavaScriptStackPathConstructor;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Advise;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Decorate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Marker;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.PipelineBuilder;
import org.apache.tapestry5.ioc.services.PropertyShadowBuilder;
import org.apache.tapestry5.ioc.services.StrategyBuilder;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.ioc.util.IdAllocator;
import org.apache.tapestry5.ioc.util.StrategyRegistry;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.portlet.DeclaredResourceResponseSender;
import org.apache.tapestry5.portlet.PortalPage;
import org.apache.tapestry5.portlet.PortletConstants;
import org.apache.tapestry5.portlet.PortletPageResolver;
import org.apache.tapestry5.portlet.PortletPersistenceConstants;
import org.apache.tapestry5.portlet.PortletRenderable;
import org.apache.tapestry5.portlet.PortletSymbolConstants;
import org.apache.tapestry5.portlet.PortletUtilities;
import org.apache.tapestry5.portlet.annotations.Portlet;
import org.apache.tapestry5.portlet.internal.services.PortalPageNameComponentEventResultProcessor;
import org.apache.tapestry5.portlet.internal.services.PortalUtilities;
import org.apache.tapestry5.portlet.internal.services.PortalUtilitiesImpl;
import org.apache.tapestry5.portlet.internal.services.PortletActionRenderResponseGeneratorImpl;
import org.apache.tapestry5.portlet.internal.services.PortletActionResultProcessor;
import org.apache.tapestry5.portlet.internal.services.PortletApplicationScopePersistentFieldStrategy;
import org.apache.tapestry5.portlet.internal.services.PortletClassResultProcessor;
import org.apache.tapestry5.portlet.internal.services.PortletComponentEventRequestFilter;
import org.apache.tapestry5.portlet.internal.services.PortletComponentEventRequestHandler;
import org.apache.tapestry5.portlet.internal.services.PortletComponentInstanceEventResultProcessor;
import org.apache.tapestry5.portlet.internal.services.PortletContextImpl;
import org.apache.tapestry5.portlet.internal.services.PortletEventResultProcessor;
import org.apache.tapestry5.portlet.internal.services.PortletFormSupportAdapter;
import org.apache.tapestry5.portlet.internal.services.PortletIdAllocatorFactoryImpl;
import org.apache.tapestry5.portlet.internal.services.PortletLinkSourceImpl;
import org.apache.tapestry5.portlet.internal.services.PortletPageNameComponentEventResultProcessor;
import org.apache.tapestry5.portlet.internal.services.PortletPageRenderRequestHandlerImpl;
import org.apache.tapestry5.portlet.internal.services.PortletPageResolverImpl;
import org.apache.tapestry5.portlet.internal.services.PortletPageResponseRendererImpl;
import org.apache.tapestry5.portlet.internal.services.PortletRenderRequestFilterImpl;
import org.apache.tapestry5.portlet.internal.services.PortletRenderResponseImpl;
import org.apache.tapestry5.portlet.internal.services.PortletRequestGlobalsImpl;
import org.apache.tapestry5.portlet.internal.services.PortletResourceResponseIdentifierImpl;
import org.apache.tapestry5.portlet.internal.services.PortletResourceResponseImpl;
import org.apache.tapestry5.portlet.internal.services.PortletResponseImpl;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.services.ApplicationInitializer;
import org.apache.tapestry5.services.ApplicationStatePersistenceStrategy;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentEventRequestHandler;
import org.apache.tapestry5.services.ComponentEventResultProcessor;
import org.apache.tapestry5.services.ContextProvider;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.ExceptionReporter;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.MarkupRenderer;
import org.apache.tapestry5.services.MarkupRendererFilter;
import org.apache.tapestry5.services.MarkupWriterFactory;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PartialMarkupRenderer;
import org.apache.tapestry5.services.PartialMarkupRendererFilter;
import org.apache.tapestry5.services.PersistentFieldStrategy;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestExceptionHandler;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.SessionPersistedObjectAnalyzer;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.services.pageload.ComponentRequestSelectorAnalyzer;
import org.apache.tapestry5.services.pageload.ComponentResourceLocator;
import org.slf4j.Logger;

public final class PortletModule
{

    public static void bind(ServiceBinder binder)
    {
        binder.bind(PortletRequestGlobals.class, PortletRequestGlobalsImpl.class).scope( ScopeConstants.PERTHREAD);
        binder.bind(PortletConfigProvider.class, PortletConfigProviderImpl.class).scope( ScopeConstants.PERTHREAD);
        binder.bind(PortletLinkSource.class, PortletLinkSourceImpl.class).withId("PortletLinkSource");
        binder.bind(PortletActionRenderResponseGenerator.class, PortletActionRenderResponseGeneratorImpl.class).withId("PortletActionRenderResponseGenerator");
        binder.bind(PortletPageResolver.class, PortletPageResolverImpl.class);
        binder.bind(PortletIdAllocatorFactory.class, PortletIdAllocatorFactoryImpl.class);
        binder.bind(PortletResourceResponseIdentifier.class,PortletResourceResponseIdentifierImpl.class);
        binder.bind(ComponentRequestSelectorAnalyzer.class, PortletRequestSelectorAnalyzer.class).withId("PortletRequestSelectorAnalyzer");
        binder.bind(PortalUtilities.class, PortalUtilitiesImpl.class);
		binder.bind(ApplicationStatePersistenceStrategy.class, PorletSessionApplicationStatePersistenceStrategy.class);
    }

    @Decorate(serviceInterface = ComponentResourceLocator.class)
    public static Object customComponentResourceLocator(
           ComponentResourceLocator delegate,
			@ContextProvider AssetFactory assetFactory,
			ComponentClassResolver componentClassResolver,
			@Symbol(SymbolConstants.APPLICATION_FOLDER) String applicationFolder) {

		return new PortletResourceLocator(delegate);
	}
    public PortletRequest build(PortletRequestGlobals portletGlobals,
            PropertyShadowBuilder shadowBuilder)
    {
        return shadowBuilder.build(portletGlobals, "PortletRequest", PortletRequest.class);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(PortletSymbolConstants.EXCLUDE_ASSETS, "false");
    }

    public PortletApplicationInitializer build(Logger logger,
            List<PortletApplicationInitializerFilter> configuration,
            @InjectService("ApplicationGlobals")
            final ApplicationGlobals applicationGlobals, @InjectService("ApplicationInitializer")
            final ApplicationInitializer initializer, @InjectService("PipelineBuilder")
            PipelineBuilder builder)
    {
        PortletApplicationInitializer terminator = new PortletApplicationInitializer()
        {
            public void initializeApplication(PortletContext context)
            {
                initializer.initializeApplication(new PortletContextImpl(context));
            }
        };

        return builder.build(logger, PortletApplicationInitializer.class,
                PortletApplicationInitializerFilter.class, configuration, terminator);
    }

    public PortletActionRequestHandler build(Logger logger,
            List<PortletActionRequestFilter> configuration, 
            @InjectService("PipelineBuilder")
            PipelineBuilder builder, final PortletPageResolver pageResolver,
            @InjectService("RequestGlobals")
            final RequestGlobals requestGlobals, 
            @InjectService("PortletRequestGlobals")
            final PortletRequestGlobals portletRequestGlobals, 
            @InjectService("RequestHandler")
            final RequestHandler handler,
        	final PortalUtilities portalUtil,
            @Primary
            final SessionPersistedObjectAnalyzer analyzer, 
            final Logger log)
    {

        PortletActionRequestHandler terminator = new PortletActionRequestHandler()
        {
            public boolean service(ActionRequest request, ActionResponse response)
                    throws IOException
            {
                String pageName = pageResolver.resolve(request);
                log.info("PORTLET ACTION REQUEST HANDLER for page " + pageName);

                Request portletRequest = portalUtil.buildPortletRequest(request, pageName, analyzer);
                Response portletResponse = new PortletResponseImpl(response, portletRequest);

                requestGlobals.storeRequestResponse(portletRequest, portletResponse);
                portletRequestGlobals.store(request, response);

                return handler.service(portletRequest, portletResponse);
            }

        };

        return builder.build(logger, PortletActionRequestHandler.class,
                PortletActionRequestFilter.class, configuration, terminator);
    }

    public PortletRenderRequestHandler build(List<PortletRenderRequestFilter> configuration,
            @InjectService("PipelineBuilder")
            PipelineBuilder builder, 
            final PortletPageResolver pageResolver,
            @InjectService("RequestGlobals")
            final RequestGlobals requestGlobals, 
            @InjectService("PortletRequestGlobals")
            final PortletRequestGlobals portletRequestGlobals, 
            @InjectService("RequestHandler")
            final RequestHandler handler, Logger logger, 
            @Primary
            final SessionPersistedObjectAnalyzer analyzer, 
            @Inject
            @Symbol(SymbolConstants.EXCEPTION_REPORT_PAGE)
            final String exceptionPage, 
        	final PortalUtilities portalUtil,
            final RequestPageCache pageCache, final Logger log)
    {

        PortletRenderRequestHandler terminator = new PortletRenderRequestHandler()
        {
            public boolean service(RenderRequest request, RenderResponse response)
                    throws IOException
            {
                String pageName = pageResolver.resolve(request);
                log.info("PORTLET RENDER REQUEST HANDLER for page " + pageName);

                // In case of exception page, let the page set up for the new
                // exception.
                if (request.getPortletSession().getAttribute(PortletConstants.LAST_ACTION_EXCEPTION) != null)
                {
                    pageName = exceptionPage;
                    portletRequestGlobals.store(request, response);
                    Page page = pageCache.get(exceptionPage);
                    ExceptionReporter rootComponent = (ExceptionReporter) page.getRootComponent();
                    rootComponent.reportException((Throwable) request.getPortletSession()
                            .getAttribute(PortletConstants.LAST_ACTION_EXCEPTION));
                    request.getPortletSession().removeAttribute(
                            PortletConstants.LAST_ACTION_EXCEPTION);
                }

                Request portletRequest = portalUtil.buildPortletRequest(request, pageName, analyzer);
                Response portletResponse = new PortletRenderResponseImpl(response);

                requestGlobals.storeRequestResponse(portletRequest, portletResponse);
                portletRequestGlobals.store(request, response);

                return handler.service(portletRequest, portletResponse);
            }
        };

        return builder.build(logger, PortletRenderRequestHandler.class,
                PortletRenderRequestFilter.class, configuration, terminator);
    }

    public PortletResourceRequestHandler build(List<PortletResourceRequestFilter> configuration,
            @InjectService("PipelineBuilder")
            PipelineBuilder builder, final PortletPageResolver pageResolver,
            @InjectService("RequestGlobals")
            final RequestGlobals requestGlobals, 
            @InjectService("PortletRequestGlobals")
            final PortletRequestGlobals portletRequestGlobals, 
            @InjectService("RequestHandler")
            final RequestHandler handler, 
            @Primary
            final SessionPersistedObjectAnalyzer analyzer,
        	final PortalUtilities portalUtil,
            final Logger log)
    {

        PortletResourceRequestHandler terminator = new PortletResourceRequestHandler()
        {
            public boolean service(ResourceRequest request, ResourceResponse response)
                    throws IOException, PortletException
            {
                String pageName = pageResolver.resolve(request);
                log.info("PORTLET RESSOURCES REQUEST HANDLER for page " + pageName);

                Request portletRequest = portalUtil.buildPortletRequest(request, pageName, analyzer);
                Response portletResponse = portalUtil.buildResourceReponse(response);

                requestGlobals.storeRequestResponse(portletRequest, portletResponse);
                portletRequestGlobals.store(request, response);

                return handler.service(portletRequest, portletResponse);
            }
        };

        return builder.build(log, PortletResourceRequestHandler.class,
                PortletResourceRequestFilter.class, configuration, terminator);

    }

    public PortletEventRequestHandler buildPortletEventRequestHandler(
            List<PortletEventRequestFilter> configuration, @InjectService("PipelineBuilder")
            PipelineBuilder builder, final PortletPageResolver pageResolver,
            @InjectService("RequestGlobals")
            final RequestGlobals requestGlobals, 
            @InjectService("PortletRequestGlobals")
            final PortletRequestGlobals portletRequestGlobals, 
            @InjectService("RequestHandler")
            final RequestHandler handler, 
            @Primary
            final SessionPersistedObjectAnalyzer analyzer, final RequestPageCache pageCache, 
            @Local
            final PortletLinkSource linkSource,
        	final PortalUtilities portalUtil,
            final Logger log)
    {

        PortletEventRequestHandler terminator = new PortletEventRequestHandler()
        {

            public boolean service(EventRequest request, EventResponse response)
                    throws IOException, PortletException
            {
                // Resolve against configuration or default page
                String pageName = pageResolver.resolve(null);
                log.info("PORTLET EVENT REQUEST HANDLER for page " + pageName);

                Request portletRequest = portalUtil.buildPortletRequest(request, pageName + ":"
                        + request.getEvent().getName() + "/"
                        + request.getEvent().getValue().toString(), analyzer);
                Response portletResponse = new PortletResponseImpl(response, portletRequest);

                requestGlobals.storeRequestResponse(portletRequest, portletResponse);
                portletRequestGlobals.store(request, response);

                return handler.service(portletRequest, portletResponse);
            }
        };

        return builder.build(log, PortletEventRequestHandler.class,
                PortletEventRequestFilter.class, configuration, terminator);

    }

    @Marker(
    { Portlet.class })
    public PortletResponseRenderer buildPortletResponseRenderer(
            PortletRequestGlobals portletGlobals, MarkupWriterFactory markupWriterFactory,
            PageMarkupRenderer markupRenderer, PageContentTypeAnalyzer pageContentTypeAnalyzer,
            Response response)
    {
        return new PortletPageResponseRendererImpl(markupWriterFactory, markupRenderer,
                pageContentTypeAnalyzer, response, portletGlobals);
    }

    public CookieSource buildPortletCookieSource(@InjectService("PortletRequestGlobals") final PortletRequestGlobals portletRequestGlobals) {
		return new CookieSource() {
			public Cookie[] getCookies()
            {
                return portletRequestGlobals.getPortletRequest().getCookies();
            }

		};
	}
	
    public CookieSink buildPortletCookieSink(@InjectService("PortletRequestGlobals") final PortletRequestGlobals portletRequestGlobals)
    {
        return new CookieSink()
        {

            public void addCookie(Cookie cookie)
            {
            	portletRequestGlobals.getPortletResponse().addProperty(cookie);
            }
        };
    }

    public void contributeMarkupRenderer(OrderedConfiguration<MarkupRendererFilter> configuration,
            final Environment environment, final Request request,
            final PortletRequestGlobals globals, final JavaScriptStackSource javascriptStackSource,
            final JavaScriptStackPathConstructor javascriptStackPathConstructor,
            final PortletIdAllocatorFactory iaFactory)
    {
        MarkupRendererFilter javaScriptSupport = new MarkupRendererFilter()
        {
            public void renderMarkup(MarkupWriter writer, MarkupRenderer renderer)
            {
                DocumentLinker linker = environment.peekRequired(DocumentLinker.class);

                IdAllocator idAllocator = iaFactory.getNewPortletIdAllocator();

                JavaScriptSupportImpl support = new JavaScriptSupportImpl(linker,
                        javascriptStackSource, javascriptStackPathConstructor, idAllocator, false);

                environment.push(JavaScriptSupport.class, support);

                renderer.renderMarkup(writer);

                environment.pop(JavaScriptSupport.class);

                support.commit();
            }
        };
        configuration.override("JavaScriptSupport", javaScriptSupport, "after:DocumentLinker");
    }

    public void contributePartialMarkupRenderer(
            OrderedConfiguration<PartialMarkupRendererFilter> configuration,

            @Path("${tapestry.spacer-image}")
            final Asset spacerImage,

            final SymbolSource symbolSource,

            final AssetSource assetSource,

            final Environment environment,

            final PortletIdAllocatorFactory iaFactory,

            final JavaScriptStackSource javascriptStackSource,

            final JavaScriptStackPathConstructor javascriptStackPathConstructor)
    {
        PartialMarkupRendererFilter javascriptSupport = new PartialMarkupRendererFilter()
        {
            public void renderMarkup(MarkupWriter writer, JSONObject reply,
                    PartialMarkupRenderer renderer)
            {
                String uid = Long.toHexString(System.currentTimeMillis());

                String namespace = "_" + uid;

                IdAllocator idAllocator = iaFactory.getNewPortletIdAllocator(namespace);

                DocumentLinker linker = environment.peekRequired(DocumentLinker.class);

                JavaScriptSupportImpl support = new JavaScriptSupportImpl(linker,
                        javascriptStackSource, javascriptStackPathConstructor, idAllocator, true);

                environment.push(JavaScriptSupport.class, support);

                renderer.renderMarkup(writer, reply);

                environment.pop(JavaScriptSupport.class);

                support.commit();
            }
        };

        configuration.override("JavaScriptSupport", javascriptSupport, "after:DocumentLinker");

    }

    public void contributePageRenderRequestHandler(
            OrderedConfiguration<PageRenderRequestFilter> configuration, @Portlet
            PortletPageRenderRequestHandler portletHandler, PortletRequestGlobals globals,
            Logger log)
    {
        configuration.add("Portlet", new PortletRenderRequestFilterImpl(globals, portletHandler,
                log));
    }

    public void contributeComponentEventRequestHandler(
            OrderedConfiguration<ComponentEventRequestFilter> configuration, @Portlet
            ComponentEventRequestHandler portletHandler, PortletRequestGlobals globals,
            Request request, Logger log)
    {
        configuration.add("Portlet", new PortletComponentEventRequestFilter(globals,
                portletHandler, request, log), "before:Ajax");
    }

    @Marker(
    { Portlet.class })
    public PortletPageRenderRequestHandler buildPortletPageRenderRequestHandler(
            List<PageRenderRequestFilter> configuration, @Autobuild
            PortletPageRenderRequestHandlerImpl terminator, PipelineBuilder pipelineBuilder,
            Logger log)
    {
        return pipelineBuilder.build(log, PortletPageRenderRequestHandler.class,
                PageRenderRequestFilter.class, configuration, terminator);
    }

    /**
     * Builds the action request handler for Portlet ActionRequests, based on a
     * {@linkplain org.apache.tapestry5.ioc.services.PipelineBuilder pipeline} around
     * {@link PortletComponentEventRequestHandler}. Filters on the
     * request handler are supported here as well.
     */
    @Marker(
    { Portlet.class })
    public ComponentEventRequestHandler buildPortletComponentEventRequestHandler(
            List<ComponentEventRequestFilter> configuration, @Autobuild
            PortletComponentEventRequestHandler terminator, PipelineBuilder pipelineBuilder,
            Logger logger)
    {
        return pipelineBuilder.build(logger, ComponentEventRequestHandler.class,
                ComponentEventRequestFilter.class, configuration, terminator);
    }

    /**
     * The component event result processor used for Portlet-action-oriented
     * component requests.
     */
    @Marker(Portlet.class)
    public ComponentEventResultProcessor buildPorletComponentEventResultProcessor(
            Map<Class, ComponentEventResultProcessor> configuration, StrategyBuilder strategyBuilder)
    {
        return constructComponentEventResultProcessor(configuration, strategyBuilder);
    }

    /**
     * Contribute new processor handler for portlet action requests.
     * 
     * @param processors
     */
    public void contributePorletComponentEventResultProcessor(
            MappedConfiguration<Class, ComponentEventResultProcessor> configuration,
            final PortletRequestGlobals portletGlobals)
    {
        configuration.addInstance(PortletRenderable.class, PortletActionResultProcessor.class);
        configuration.addInstance(Event.class, PortletEventResultProcessor.class);
        configuration.addInstance(StreamResponse.class, StreamResponseResultProcessor.class);
        configuration.addInstance(Class.class, PortletClassResultProcessor.class);
        configuration.addInstance(String.class, PortletPageNameComponentEventResultProcessor.class);
        configuration.addInstance(Component.class,
                PortletComponentInstanceEventResultProcessor.class);
        configuration.addInstance(PortalPage.class,
                PortalPageNameComponentEventResultProcessor.class);

        /**
         * Explicit redirect is done through URL
         */
        configuration.add(URL.class, new ComponentEventResultProcessor<URL>()
        {
            public void processResultValue(URL value) throws IOException
            {
                (portletGlobals.getActionResponse()).sendRedirect(value.toExternalForm());
            }
        });

        configuration.add(Link.class, new ComponentEventResultProcessor<Link>()
        {
            public void processResultValue(Link value) throws IOException
            {
                StateAwareResponse response = (StateAwareResponse) portletGlobals
                        .getPortletResponse();
                for (String name : value.getParameterNames())
                {
                    response.setRenderParameter(name, value.getParameterValue(name));
                }
                response.setRenderParameter(PortletConstants.PORTLET_PAGE, PortletUtilities.stripQueryString(value.toURI()));
            }
        });

    }

    private ComponentEventResultProcessor constructComponentEventResultProcessor(
            Map<Class, ComponentEventResultProcessor> configuration, StrategyBuilder strategyBuilder)
    {
        Set<Class> handledTypes = CollectionFactory.newSet(configuration.keySet());

        // A slight hack!
        configuration.put(Object.class, new ObjectComponentEventResultProcessor(handledTypes));

        StrategyRegistry<ComponentEventResultProcessor> registry = StrategyRegistry.newInstance(
                ComponentEventResultProcessor.class, configuration);

        return strategyBuilder.build(registry);
    }

    public void contributeServiceOverride(
			MappedConfiguration<Class, Object> configuration,
			@InjectService("PortletCookieSource") final CookieSource cookieSource,
			@InjectService("PortletCookieSink") final CookieSink cookieSink,
			@InjectService("PortletLinkSource") final PortletLinkSource linkSource, 
			@InjectService("PortletRequestSelectorAnalyzer") final ComponentRequestSelectorAnalyzer analyzer)

	{
		configuration.add(LinkSource.class, linkSource);
		configuration.add(CookieSource.class, cookieSource);
		configuration.add(CookieSink.class, cookieSink);
		configuration.add(ComponentRequestSelectorAnalyzer.class, analyzer);
	}

    /**
     * Intercept FormSupport push calls and adpat it to prefix generated ids with portlet window id.
     * Doing we assure that there will be no collision between control name on client side.
     * 
     * @param receiver
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    @Advise(serviceInterface = Environment.class)
    public static void adaptFormSupport(MethodAdviceReceiver receiver,
            final PortletRequestGlobals globals) throws SecurityException, NoSuchMethodException
    {

        MethodAdvice advice = new MethodAdvice()
        {
            @Override
            public void advise(Invocation invocation)
            {
                Object instance = invocation.getParameter(1);
                if (FormSupportImpl.class.equals(instance.getClass()))
                {
                    FormSupport adapter = new PortletFormSupportAdapter((FormSupport) instance,
                            globals.getPortletRequest().getWindowID()
                                    .replaceAll("[^A-za-z0-9]", ""));
                    invocation.override(1, adapter);
                }
                invocation.proceed();
            }
        };

        Method push = Environment.class.getMethod("push", Class.class, Object.class);
        receiver.adviseMethod(push, advice);
    }

    /**
     * Exception handling on action request must keep information between
     * request.
     * 
     * @param delegate
     * @param request
     * @return
     */
    @Match(value = "RequestExceptionHandler")
    public RequestExceptionHandler decorateRequestExceptionHandler(
            final RequestExceptionHandler delegate, 
            final PortletRequestGlobals globals,
            final RequestGlobals requestGlobals,   
            @Inject
            @Symbol(SymbolConstants.EXCEPTION_REPORT_PAGE)
            final String exceptionPage, final Logger log)
    {

        return new RequestExceptionHandler()
        {

            public void handleRequestException(Throwable exception) throws IOException
            {
                // Bypass exception rendering in case of action request
                if (globals.getActionRequest() != null)
                {
                    
                    PortletSession session =   globals.getPortletRequest().getPortletSession();
                    if(session!=null)
                    {
                    	if(requestGlobals.getRequest().isRequestedSessionIdValid())
                    	{
                    		log.debug("handleRequestException saved at PortletRequest->PortletSession->Attribute=LAST_ACTION_EXCEPTION");
                    		session.setAttribute(PortletConstants.LAST_ACTION_EXCEPTION, exception);
                    		return;
                    	}   
                    	else
                    	{
                    		log.debug("handleRequestException not saved at PortletRequest->PortletSession->Attribute=LAST_ACTION_EXCEPTION as RequestedSessionIdValid");
                    		log.debug("exception is "+exception);
                    		return;
                    	}
                    }
                }
                log.debug("handleRequestException redirect to delegate");
                delegate.handleRequestException(exception);
            }

        };
    }

    public static void contributePortletResourceResponseIdentifier(
            Configuration<DeclaredResourceResponseSender> configuration)
    {
        // declare core component that will return resource response form ajax
        // call
        configuration.add(new DeclaredResourceResponseSender(DateField.class.getName()));
        configuration.add(new DeclaredResourceResponseSender(FormInjector.class.getName()));
        configuration.add(new DeclaredResourceResponseSender(BeanEditForm.class.getName()));
        // ajaxFormLoop
        DeclaredResourceResponseSender ajaxFormLoop = new DeclaredResourceResponseSender(
                AjaxFormLoop.class.getName());
        ajaxFormLoop.addEvent(EventConstants.ADD_ROW);
        ajaxFormLoop.addEvent(EventConstants.REMOVE_ROW);
        ajaxFormLoop.addEvent("triggerRemoveRow"); //See AjaxFormLoop.formLoopContext for more details
        configuration.add(ajaxFormLoop);
        // declare core mixin that will return resource response form ajax call
        configuration.add(new DeclaredResourceResponseSender(Autocomplete.class.getName(), true));

        // declare tapestry-jquery component that will return resource response
        // form ajax call
        configuration.add(new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.components.AjaxUpload"));
        configuration.add(new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.components.CarouselItem"));
        configuration.add(new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.components.AjaxUpload"));
        configuration.add(new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.components.CarouselItem"));
        DeclaredResourceResponseSender datatable = new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.components.DataTable");
        datatable.addEvent("Data");
        datatable.addEvent("filterData");
        datatable.addEvent("sortData");
        configuration.add(datatable);
        configuration.add(new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.components.DialogAjaxLink"));
        configuration.add(new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.components.InPlaceEditor"));
        configuration.add(new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.components.ProgressiveDisplay"));
        configuration.add(new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.components.RangeSlider"));
        configuration.add(new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.components.Slider"));
        configuration.add(new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.components.Tabs"));
        configuration.add(new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.components.TwitterView"));
        // declare tapestry-jquery mixin that will return resource response form
        // ajax call
        configuration.add(new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.mixins.Autocomplete",true));
        configuration.add(new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.mixins.ZoneDroppable",true));
        configuration.add(new DeclaredResourceResponseSender("org.got5.tapestry5.jquery.mixins.ZoneRefresh",true));
        // for page or mixin like  org.got5.tapestry5.jquery.mixins.Bind you have to declare the full pagename
        // and the eventname that should be treat as resource URL 
        
        
        
    }
    
    /**
     * Contributes Persistent Field strategies:
     * <dl>
     * <dt>PORTLET_SESSION_APPLICATION_SCOPE
     * <dd>Values are stored in the the field's value is stored in the APPLICATION_SCOPE in  PortletSession
     * 
     * </dl>
     */
    public void contributePersistentFieldManager(MappedConfiguration<String, PersistentFieldStrategy> configuration,
    		 @InjectService("PortletRequestGlobals")
    		PortletRequestGlobals globals)
    {
        configuration.add(PortletPersistenceConstants.PORTLET_SESSION_APPLICATION_SCOPE, new PortletApplicationScopePersistentFieldStrategy(globals));
       
    }
	
	/**
     * Contribute SessionState persistence strategy. This strategy uses scope Application. 
     */
    public void contributeApplicationStatePersistenceStrategySource(
			MappedConfiguration<String, ApplicationStatePersistenceStrategy> configuration,
			@Local ApplicationStatePersistenceStrategy sessionStategy) {
		configuration.add(PortletConstants.SESSION_STATE_APPLICATION_SCOPE, sessionStategy);
	}
}
