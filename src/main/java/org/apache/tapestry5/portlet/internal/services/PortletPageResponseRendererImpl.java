// Copyright 2006, 2007 The Apache Software Foundation
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

package org.apache.tapestry5.portlet.internal.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.MimeResponse;
import javax.portlet.PortalContext;
import javax.portlet.PortletRequest;

import org.apache.tapestry5.ContentType;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.dom.Attribute;
import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;
import org.apache.tapestry5.internal.services.PageContentTypeAnalyzer;
import org.apache.tapestry5.internal.services.PageMarkupRenderer;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.portlet.services.PortletRequestGlobals;
import org.apache.tapestry5.portlet.services.PortletResponseRenderer;
import org.apache.tapestry5.services.MarkupWriterFactory;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

public class PortletPageResponseRendererImpl implements PortletResponseRenderer
{

    private static final String HEADER_MARKUP = "javax.servlet.tapestry.header-markup";

    private final Logger LOGGER = org.slf4j.LoggerFactory
            .getLogger(PortletPageResponseRendererImpl.class);

    private final PortletRequestGlobals globals;

    private final PageMarkupRenderer _markupRenderer;

    private final MarkupWriterFactory _markupWriterFactory;

    private final PageContentTypeAnalyzer _pageContentTypeAnalyzer;

    private final Response _response;

    public PortletPageResponseRendererImpl(MarkupWriterFactory markupWriterFactory,
            PageMarkupRenderer markupRenderer, PageContentTypeAnalyzer pageContentTypeAnalyzer,
            Response response, PortletRequestGlobals portletGlobals)
    {
        this._markupWriterFactory = markupWriterFactory;
        this._markupRenderer = markupRenderer;
        this._pageContentTypeAnalyzer = pageContentTypeAnalyzer;
        this._response = response;
        this.globals = portletGlobals;
    }

    @Override
    public void renderPageResponse(Page page) throws IOException
    {
        assert page != null;

        Object lifecyclePhase = this.globals.getPortletRequest().getAttribute(
                PortletRequest.LIFECYCLE_PHASE);

        if (PortletRequest.ACTION_PHASE.equals(lifecyclePhase)
                || PortletRequest.EVENT_PHASE.equals(lifecyclePhase)) { throw new IllegalStateException(
                "Cannot render markup for action request"); }

        // For the moment, the content type is all that's used determine the
        // model for the markup writer.
        // It's something of a can of worms.
        ContentType contentType = this._pageContentTypeAnalyzer.findContentType(page);
        
        PrintWriter pw = this._response.getPrintWriter(contentType.toString());

        MarkupWriter writer = this._markupWriterFactory.newMarkupWriter(contentType);

        this._markupRenderer.renderPageMarkup(page, writer);

        this.write(writer, pw);

        pw.close();
        
    }

    private void write(MarkupWriter markupWriter, PrintWriter pw)
    {
        // extract the body from the DOM and use it as portlet body
        Document document = markupWriter.getDocument();

        // This is not an full HTML document, simply output it
        if (!"html".equals(document.getRootElement().getName()))
        {
            markupWriter.toMarkup(pw);
        }
        else
        {
            String HeadElementSupport = globals.getPortletRequest().getPortalContext()
                    .getProperty(PortalContext.MARKUP_HEAD_ELEMENT_SUPPORT);
            // Output Javascipt and CSS first
            if (HeadElementSupport != null)
            {
                // Add elements to head if support is enabled
                Element head = document.find("html/head");
                if (head != null)
                {
                    for (Node child : head.getChildren())
                    {
                        if (child instanceof Element)
                        {
                            Element element = (Element) child;
                            this.addElementToHead(element);
                        }
                    }
                }
            }
            else
            {
                LOGGER.warn("Your portlet context does not support adding markup in the head elements, you may encounter client side js conflicts.");
                Element head = document.find("html/head");
                List<String> existingResources = (List<String>) globals.getRenderRequest()
                        .getAttribute(HEADER_MARKUP);

                if (existingResources == null)
                {
                    existingResources = new ArrayList<String>();
                }

                if (head != null)
                {
                    for (Node child : head.getChildren())
                    {
                        if (child instanceof Element)
                        {
                            String tagName = ((Element) child).getName();
                            if ("script".equalsIgnoreCase(tagName)
                                    || "link".equalsIgnoreCase(tagName))
                            {
                                Element element = (Element) child;
                                String path = readPath(element);
                                if (existingResources.contains(path))
                                {
                                    continue;
                                }

                                element.toMarkup(pw);
                                existingResources.add(path);
                            }
                        }
                    }
                }
                
                globals.getRenderRequest().setAttribute(HEADER_MARKUP, existingResources);
                
            }

            Element body = document.find("html/body");
            if (body != null)
            {
                String childMarkup = body.getChildMarkup();
                pw.write(childMarkup);
            }

        }

        pw.flush();

    }

    /**
     * This method add a property with ${@link MimeResponse#MARKUP_HEAD_ELEMENT} id. If the
     * container support this features then all the javascript and stylesheet files will be
     * added in the document header.
     * 
     * @param element
     */
    private void addElementToHead(Element element)
    {
        MimeResponse renderResponse = (MimeResponse) globals.getPortletResponse();

        String tagName = element.getName();

        org.w3c.dom.Element headElement = null;

        String textContent = element.getChildMarkup();
        if (InternalUtils.isNonBlank(textContent))
        {
            headElement = renderResponse.createElement(tagName);
            headElement.setTextContent(textContent);
        }
        else
        {

            PortletRequest req = globals.getPortletRequest();
            List<String> markupHeadElements = (List<String>) req
                    .getAttribute(MimeResponse.MARKUP_HEAD_ELEMENT);
            // make to include script only once
            if (markupHeadElements != null)
                for (Attribute attribute : element.getAttributes())
                {
                    if (attribute.getName().equals("src") || attribute.getName().equals("href")
                            || attribute.getName().equals("name"))
                    {
                        String value = attribute.getValue();
                        for (String s : markupHeadElements)
                            if (s.contains(value))
                            {
                                LOGGER.debug("value " + s + " already defined ");
                                // avoid write the same include twice
                                return;
                            }
                    }
                }

            if ("script".equalsIgnoreCase(tagName))
            {

                headElement = renderResponse.createElement(tagName);
                headElement.setTextContent("\n");
            }
        }

        for (Attribute attribute : element.getAttributes())
        {
            String name = attribute.getName();
            String value = attribute.getValue();
            if (headElement == null)
                headElement = renderResponse.createElement(tagName);
            headElement.setAttribute(name, value);
        }

        renderResponse.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, headElement);
    }

    private String readPath(Element headerElement)
    {
        for (Attribute attribute : headerElement.getAttributes())
        {
            if (attribute.getName().equals("src") || attribute.getName().equals("href")
                    || attribute.getName().equals("name")) { return attribute.getValue(); }
        }
        return null;
    }

}
