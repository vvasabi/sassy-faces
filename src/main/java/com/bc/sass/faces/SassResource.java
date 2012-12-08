package com.bc.sass.faces;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.application.ResourceWrapper;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.io.FilenameUtils;
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
	private final String rendered;

	public SassResource(Resource resource, Syntax syntax) throws IOException {
		this.wrapped = resource;
		this.rendered = render(syntax);
	}

	protected String render(Syntax syntax) throws IOException {
		String input = IOUtils.toString(wrapped.getInputStream());
		SassProcessor processor = new SassProcessor();
		processor.setSyntax(syntax);

		String library = wrapped.getLibraryName();
		if (library != null) {
			processor.addLoadPath(library);
		}
		return processor.process(input);
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

}
