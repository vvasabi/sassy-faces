package com.bc.sass.faces;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bc.sass.Syntax;

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

	public SassResourceHandler(ResourceHandler handler) {
		this.wrapped = handler;
	}

	@Override
	public Resource createResource(String resourceName) {
		return createResource(resourceName, null);
	}

	@Override
	public Resource createResource(String resourceName, String libraryName) {
		Resource resource = wrapped.createResource(resourceName, libraryName);
		if (resourceName.endsWith(SASS_EXTENSION)) {
			LOGGER.debug("Compile SASS file {}...", resourceName);
			return new SassResource(resource, Syntax.SASS);
		} else if (resourceName.endsWith(SCSS_EXTENSION)) {
			LOGGER.debug("Compile SCSS file {}...", resourceName);
			return new SassResource(resource, Syntax.SCSS);
		}
		return resource;
	}

	@Override
	public ResourceHandler getWrapped() {
		return wrapped;
	}

}
