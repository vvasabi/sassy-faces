package com.bc.sass.faces;

import com.bc.sass.Syntax;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Resource handler for processing SASS files.
 *
 * @author vvasabi
 */
public class SassResourceHandler extends ResourceHandlerWrapper {

	private static final String SASS_EXTENSION = ".sass";
	private static final String SCSS_EXTENSION = ".scss";
	private static final Logger LOGGER
			= LoggerFactory.getLogger(SassResourceHandler.class);

	private final ResourceHandler wrapped;
	private final ConcurrentMap<String, Resource> cache;

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
		Syntax syntax = null;
		if (resourceName.endsWith(SASS_EXTENSION)) {
			syntax = Syntax.SASS;
		} else if (resourceName.endsWith(SCSS_EXTENSION)) {
			syntax = Syntax.SCSS;
		}
		if (syntax == null) {
			return resource;
		}
		return getSassResource(resource, syntax);
	}

	private Resource getSassResource(Resource resource, Syntax syntax) {
		try {
			// in dev stage don't cache resource
			FacesContext context = FacesContext.getCurrentInstance();
			if (context.isProjectStage(ProjectStage.Development)) {
				LOGGER.debug("Compile SASS file {}...",
					resource.getResourceName());
				return new SassResource(resource, syntax);
			}

			// otherwise cache it
			String resourceKey = buildResourceKey(resource);
			if (cache.containsKey(resourceKey)) {
				return cache.get(resourceKey);
			}

			return createAndCacheSassResource(resource, syntax);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	private Resource createAndCacheSassResource(Resource resource,
			Syntax syntax) throws IOException {
		LOGGER.debug("Compile SASS file {}...", resource.getResourceName());
		String resourceKey = buildResourceKey(resource);
		Resource sassResource = new SassResource(resource, syntax);
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

	@Override
	public ResourceHandler getWrapped() {
		return wrapped;
	}

}
