// Copyright 2012 The Apache Software Foundation
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

import java.util.List;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.portlet.PortletConstants;
import org.apache.tapestry5.portlet.PortletPageResolver;
import org.apache.tapestry5.portlet.services.PortletConfigProvider;
import org.apache.tapestry5.portlet.services.PortletPageResolverRule;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.slf4j.Logger;

/**
 * Implements the PortletPageResolver for a basic portlet. It runs through all
 * of the given configurations and tries to match the current request to one of
 * the configurations. Note that it does this in order of the declared
 * configurations in the project Module file, so it returns the first page name
 * that matches.
 */
public class PortletPageResolverImpl implements PortletPageResolver
{
    private final ComponentClassResolver componentClassResolver;

    private final List<PortletPageResolverRule> configuration = CollectionFactory.newList();

    private final PortletConfigProvider portletConfig;

    public PortletPageResolverImpl(
    		Logger logger,
    		List<PortletPageResolverRule> configuration,
            ComponentClassResolver componentClassResolver, PortletConfigProvider portletConfig)
    {
        this.configuration.addAll(configuration);
        this.componentClassResolver = componentClassResolver;
        this.portletConfig = portletConfig;
    }

    /**
     * Page resolution algorithm:
     * <ol>
     * <li>First we try to match one of the rule defined by the developer</li>
     * <li>If no rule match, then read the t:pp parameter. If the page path set in this request
     * parameter match the current request mode then we simply return it.</li>
     * <li>We probably have change the portlet's mode, we have to build the page name from the
     * porlet name and the current portlet mode. We always check if the correspoding exists in the
     * tapestry application.</li>
     * </ol>
     */
    @Override
    public String resolve(PortletRequest request)
    {
        if (request == null) { return portletConfig.getPortletName(); }
        
        String pageName = resolveToConfiguration(portletConfig.getPortletName(), request);

        // a page name configured by the developer supresedes any other value 
        if (pageName != null) { return pageName; }

        PortletMode portletMode = request.getPortletMode();
        pageName = (String) request.getParameter(PortletConstants.PORTLET_PAGE);
        if (pageName != null && checkPageMode(pageName, request.getContextPath(), portletMode)) { return pageName; }

        // Build name from portlet name
        pageName = portletConfig.getPortletName();

        // In case of edit mode prefix with 'preferences/'
        if (PortletMode.EDIT.equals(portletMode))
        {
            String preferencesPage = "preferences/" + pageName;

            if (this.componentClassResolver.isPageName(preferencesPage)) { return preferencesPage; }

            throw new RuntimeException(String.format("'%s' has no preferences page.", pageName));
        }

        if (PortletMode.HELP.equals(portletMode))
        {
            String helpPage = "help/" + pageName;

            if (this.componentClassResolver.isPageName(helpPage)) { return helpPage; }

            throw new RuntimeException(String.format("'%s' has no help page.", pageName));
        }

        // Default is view mode
        if (!this.componentClassResolver.isPageName(pageName)) { throw new RuntimeException(
                String.format("'%s' does not match any tapestry page.",
                        portletConfig.getPortletName())); }

        return pageName;
    }

    /**
     * Simply check that the page path match the current mode. If not then it's
     * probably that the mode has change or is wrong. In that case we should
     * redirect to the portlet home page.
     * 
     * @param pageName
     * @return
     */
    private boolean checkPageMode(String pageName, String contextPath, PortletMode mode)
    {

        if (pageName == null)
            throw new IllegalArgumentException(pageName);

        if (PortletMode.EDIT.equals(mode)) { return pageName.startsWith(contextPath
                + "/preferences/"); }

        if (PortletMode.HELP.equals(mode)) { return pageName.startsWith(contextPath + "/help/"); }

        return !pageName.startsWith(contextPath + "/preferences/")
                && !pageName.startsWith(contextPath + "/help/");
    }

    public String resolveToConfiguration(String portletName, PortletRequest request)
    {
        for (PortletPageResolverRule c : configuration)
        {
            if (c.match(portletName, request))
            {
                String pageName = c.getPageName();

                if (componentClassResolver.isPageName(pageName)) { return pageName; }
            }
        }
        return null;
    }

}
