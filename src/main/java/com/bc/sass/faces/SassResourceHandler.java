package com.bc.sass.faces;

import com.bc.sass.SassConfig;
import com.bc.sass.SassImporterFactory;
import com.bc.sass.Style;
import com.bc.sass.Syntax;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Resource handler for processing SASS files.
 *
 * @author vvasabi
 */
public class SassResourceHandler extends ResourceHandlerWrapper {

	private static final Logger LOGGER
			= LoggerFactory.getLogger(SassResourceHandler.class);

	private static final String CONFIG_COMPASS_ENABLED
			= "com.bc.sass.faces.COMPASS_ENABLED";
	private static final String CONFIG_STYLE = "com.bc.sass.faces.STYLE";

	private final ResourceHandler wrapped;
	private final ConcurrentMap<String, Resource> cache;

	static {
		if (SassImporterFactory.getInstance() == null) {
			SassImporterFactory.setInstance(new FacesSassImporterFactory());
		}
	}

	public SassResourceHandler(ResourceHandler handler) {
		this.wrapped = handler;
		this.cache = new ConcurrentHashMap<String, Resource>();
	}

	@Override
	public Resource createResource(String resourceName) {
		return createResource(resourceName, null);
	}

	@Override
	public Resource createResource(String resourceName, String libraryName) {
		Resource resource = wrapped.createResource(resourceName, libraryName);
		if (!resourceName.endsWith(Syntax.SASS.getExtension())
				&& !resourceName.endsWith(Syntax.SCSS.getExtension())) {
			return resource;
		}
		return getSassResource(resource);
	}

	private Resource getSassResource(Resource resource) {
		// in dev stage don't cache resource
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.isProjectStage(ProjectStage.Development)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Compile SASS file {}...",
						resource.getResourceName());
			}
			return new SassResource(resource, loadSassConfig());
		}

		// otherwise cache it
		String resourceKey = buildResourceKey(resource);
		Resource cached = cache.get(resourceKey);
		if (cached != null) {
			return cached;
		}

		return createAndCacheSassResource(resource);
	}

	private Resource createAndCacheSassResource(Resource resource) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Compile SASS file {}...", resource.getResourceName());
		}
		String resourceKey = buildResourceKey(resource);
		Resource sassResource = new SassResource(resource, loadSassConfig());
		cache.put(resourceKey, sassResource);
		return sassResource;
	}

	private String buildResourceKey(Resource resource) {
		String libraryName = resource.getLibraryName();
		String resourceName = resource.getResourceName();
		if (libraryName == null) {
			return resourceName;
		}
		return libraryName + "/" + resourceName;
	}

	private SassConfig loadSassConfig() {
		SassConfig config = new SassConfig();
		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		String compass = context.getInitParameter(CONFIG_COMPASS_ENABLED);
		config.setCompassEnabled(BooleanUtils.toBoolean(compass));

		String style = context.getInitParameter(CONFIG_STYLE);
		if (style != null) {
			config.setStyle(Style.parse(style));
		}
		return config;
	}

	@Override
	public ResourceHandler getWrapped() {
		return wrapped;
	}

}
