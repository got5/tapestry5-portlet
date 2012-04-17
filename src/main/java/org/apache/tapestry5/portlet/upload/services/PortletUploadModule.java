package org.apache.tapestry5.portlet.upload.services;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.FileCleaner;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Marker;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;
import org.apache.tapestry5.portlet.annotations.Portlet;
import org.apache.tapestry5.portlet.services.PortletActionRequestFilter;
import org.apache.tapestry5.portlet.upload.internal.services.PortletMultipartDecoderFilter;
import org.apache.tapestry5.portlet.upload.internal.services.PortletMultipartDecoderImpl;
import org.apache.tapestry5.services.AliasContribution;
import org.apache.tapestry5.upload.services.MultipartDecoder;

/**
 * This module adds upload capability to your application.The default
 * tapestry-upload implementation has been extended to handle file upload in the
 * context of a portlet.
 * 
 * @author ccordenier
 */
public class PortletUploadModule
{
    private static final AtomicBoolean needToAddShutdownListener = new AtomicBoolean(true);

    @Scope(ScopeConstants.PERTHREAD)
    @Marker(Portlet.class)
    public static PortletMultipartDecoder buildPortletMultipartDecoder(
            PerthreadManager perthreadManager, RegistryShutdownHub shutdownHub, @Autobuild
            PortletMultipartDecoderImpl multipartDecoder)
    {
        // This is proabably overkill since the FileCleaner should catch
        // temporary files, but lets
        // be safe.
        perthreadManager.addThreadCleanupListener(multipartDecoder);

        if (needToAddShutdownListener.getAndSet(false))
        {
            shutdownHub.addRegistryShutdownListener(new RegistryShutdownListener()
            {
                public void registryDidShutdown()
                {
                    FileCleaner.exitWhenFinished();
                }
            });
        }

        return multipartDecoder;
    }

    public static void contributePortletActionRequestHandler(
            OrderedConfiguration<PortletActionRequestFilter> configuration,
            PortletMultipartDecoder multipartDecoder)
    {
        configuration.add("PortletMultipartFilter", new PortletMultipartDecoderFilter(
                multipartDecoder), "after:IgnoredPaths");
    }

    /**
     * Override the default MultipartDecoder to add fileupload capability in a
     * portlet context.
     * 
     * @param configuration
     * @param multiPartDecoder
     */
    public void contributeAliasOverrides(Configuration<AliasContribution> configuration, @Portlet
    final PortletMultipartDecoder multiPartDecoder)
    {
        configuration.add(AliasContribution.create(MultipartDecoder.class, multiPartDecoder));
    }
}
