package com.bc.sass.faces;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.application.ResourceWrapper;

import org.apache.commons.io.IOUtils;

import com.bc.sass.SassProcessor;
import com.bc.sass.Syntax;

/**
 * An instance of SASS Resource.
 *
 * @author vvasabi
 */
public class SassResource extends ResourceWrapper {

	private static final String UTF8_CHARSET = "UTF-8";

	private final Resource wrapped;
	private final Syntax syntax;

	public SassResource(Resource resource, Syntax syntax) {
		this.wrapped = resource;
		this.syntax = syntax;
	}

	@Override
	public Map<String, String> getResponseHeaders() {
		Map<String, String> responseHeaders = wrapped.getResponseHeaders();
		responseHeaders.put("Content-Type", "text/css");
		return responseHeaders;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		String input = IOUtils.toString(wrapped.getInputStream());
		SassProcessor processor = new SassProcessor();
		processor.setSyntax(syntax);

		String result = processor.process(input);
		return new ByteArrayInputStream(result.getBytes(UTF8_CHARSET));
	}

	@Override
	public Resource getWrapped() {
		return wrapped;
	}

}
