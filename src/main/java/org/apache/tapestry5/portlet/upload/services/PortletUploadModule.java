package org.apache.tapestry5.portlet.upload.services;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.FileCleaningTracker;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Marker;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;
import org.apache.tapestry5.portlet.annotations.Portlet;
import org.apache.tapestry5.portlet.services.PortletActionRequestFilter;
import org.apache.tapestry5.portlet.upload.internal.services.PortletMultipartDecoderFilter;
import org.apache.tapestry5.portlet.upload.internal.services.PortletMultipartDecoderImpl;
import org.apache.tapestry5.upload.services.MultipartDecoder;

/**
 * This module adds upload capability to your application.The default
 * tapestry-upload implementation has been extended to handle file upload in the
 * context of a portlet.
 * 
 * @author ccordenier,ffacon
 */
public class PortletUploadModule
{
    private static final AtomicBoolean needToAddShutdownListener = new AtomicBoolean(true);
    private static final FileCleaningTracker fileCleaningTracker = new FileCleaningTracker();
    
    @Scope(ScopeConstants.PERTHREAD)
    @Marker(Portlet.class)
    public static PortletMultipartDecoder buildPortletMultipartDecoder(
            PerthreadManager perthreadManager, RegistryShutdownHub shutdownHub, @Autobuild
            PortletMultipartDecoderImpl multipartDecoder)
    {
        // This is probably overkill since the FileCleaner should catch
        // temporary files, but lets
        // be safe.
        perthreadManager.addThreadCleanupListener(multipartDecoder);

        if (needToAddShutdownListener.getAndSet(false))
        {
            shutdownHub.addRegistryShutdownListener(new RegistryShutdownListener()
            {
                public void registryDidShutdown()
                {
                	fileCleaningTracker.exitWhenFinished();
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
  	 public void contributeServiceOverride(
				MappedConfiguration<Class, Object> configuration,
				@Local MultipartDecoder multiPartDecoder)
  	 {
  		 configuration.add(MultipartDecoder.class, multiPartDecoder);			
  	 }

}