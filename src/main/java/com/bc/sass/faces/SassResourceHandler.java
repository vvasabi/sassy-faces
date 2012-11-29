package com.bc.sass.faces;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

import com.bc.sass.Syntax;

/**
 * Resource handler for processing SASS files.
 *
 * @author vvasabi
 */
public class SassResourceHandler extends ResourceHandlerWrapper {

	private static final String SASS_EXTENSION = ".sass";
	private static final String SCSS_EXTENSION = ".scss";

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
			return new SassResource(resource, Syntax.SASS);
		} else if (resourceName.endsWith(SCSS_EXTENSION)) {
			return new SassResource(resource, Syntax.SCSS);
		}
		return resource;
	}

	@Override
	public ResourceHandler getWrapped() {
		return wrapped;
	}

}
