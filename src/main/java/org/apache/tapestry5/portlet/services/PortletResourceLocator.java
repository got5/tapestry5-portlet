package org.apache.tapestry5.portlet.services;

import java.util.List;

import javax.portlet.WindowState;

import org.apache.tapestry5.TapestryConstants;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.apache.tapestry5.model.ComponentModel;
import org.apache.tapestry5.services.pageload.ComponentResourceLocator;
import org.apache.tapestry5.services.pageload.ComponentResourceSelector;

/**
 * Decorator of the ComponentResourceLocator, in order to add the WindowState
 * property in the path of templates and bundles.
 * 
 * @author Emmanuel DEMEY
 * 
 */
public class PortletResourceLocator implements ComponentResourceLocator {

	private final ComponentResourceLocator delegate;

	public PortletResourceLocator(ComponentResourceLocator delegate) {

		this.delegate = delegate;
	}

	/**
	 * Method used to locate bundles of a component. We will construct an array of Resource, containing the bundles 
	 * for the current window_state, language and the default bundle :
	 * <ul>
	 * 		<li>component_minimized_en.properties</li>
	 *      <li>component_minimized.properties</li>
	 *      <li>component_en.properties</li>
	 *      <li>component.properties</li>
	 * </ul>
	 */
	@Override
	public List<Resource> locateMessageCatalog(Resource catalog,
			ComponentResourceSelector selector) {

		WindowState client = selector.getAxis(WindowState.class);

		if (isOverridable(client)) {

			List<Resource> ress = F
					.flow(delegate.locateMessageCatalog(
							new ClasspathResource(getResourceFileWithSuffix(
									catalog.getPath(), client)), selector))
					.concat(F.flow(delegate.locateMessageCatalog(catalog,
							selector))).toList();

			return ress;

		}
		return delegate.locateMessageCatalog(catalog, selector);
	}

	/**
	 * Locate the template file of a component/page. 
	 * If no template with the windowState was found, 
	 * the default implementation will be executed. 
	 */
	@Override
	public Resource locateTemplate(ComponentModel model,
			ComponentResourceSelector selector) {

		WindowState client = selector.getAxis(WindowState.class);

		if (isOverridable(client)) {
			Resource resource = new ClasspathResource(
					getResourceFileWithSuffix(
							model.getBaseResource().getPath(), client))
					.withExtension(TapestryConstants.TEMPLATE_EXTENSION);

			if (resource.exists()) {

				return resource.forLocale(selector.locale);
			}
		}
		return delegate.locateTemplate(model, selector);
	}

	private String getResourceFileWithSuffix(String initial, WindowState suffix) {

		int dotx = initial.lastIndexOf('.');

		if (dotx < 0)
			return initial + "_" + suffix;

		return initial.substring(0, dotx) + "_" + suffix + "."
				+ initial.substring(dotx + 1);
	}

	private boolean isOverridable(WindowState suffix) {
		return !(suffix == null || suffix.equals(WindowState.NORMAL));
	}

}
