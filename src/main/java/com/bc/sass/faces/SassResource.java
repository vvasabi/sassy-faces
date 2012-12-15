package com.bc.sass.faces;

import com.bc.sass.SassConfig;
import com.bc.sass.SassProcessor;

import javax.faces.application.Resource;
import javax.faces.application.ResourceWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * An instance of SASS Resource.
 *
 * @author vvasabi
 */
public class SassResource extends ResourceWrapper {

	private static final String UTF8_CHARSET = "UTF-8";

	private final Resource wrapped;
	private final String rendered;
	private final SassConfig config;

	public SassResource(Resource resource, SassConfig config) {
		this.wrapped = resource;
		this.config = config;
		this.rendered = render();
	}

	protected String render() {
		SassProcessor processor = new SassProcessor();
		config.setLoadPath(getLibraryName());
		processor.setConfig(config);
		return processor.processFile(getResourceName());
	}

	@Override
	public Map<String, String> getResponseHeaders() {
		Map<String, String> responseHeaders = wrapped.getResponseHeaders();
		responseHeaders.put("Content-Type", "text/css");
		return responseHeaders;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(rendered.getBytes(UTF8_CHARSET));
	}

	@Override
	public Resource getWrapped() {
		return wrapped;
	}

	@Override
	public String getResourceName() {
		return wrapped.getResourceName();
	}

	@Override
	public String getLibraryName() {
		return wrapped.getLibraryName();
	}

}
