package com.bc.sass.faces;

import com.bc.sass.SassProcessor;
import com.bc.sass.Syntax;
import org.apache.commons.io.IOUtils;

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

	public SassResource(Resource resource, Syntax syntax) throws IOException {
		this.wrapped = resource;
		this.rendered = render(syntax);
	}

	protected String render(Syntax syntax) throws IOException {
		String input = getResourceContent();
		SassProcessor processor = new SassProcessor(getFullName());
		processor.setSyntax(syntax);

		String library = wrapped.getLibraryName();
		if (library != null) {
			processor.setLoadPath(library);
		}
		return processor.process(processELValues(input));
	}

	private String getResourceContent() throws IOException {
		InputStream is = wrapped.getInputStream();
		try {
			return IOUtils.toString(is);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	private String getFullName() {
		if (getLibraryName() == null) {
			return getResourceName();
		}
		return getLibraryName() + "/" + getResourceName();
	}

	private String processELValues(String input) {
		ELValueProcessor processor = new ELValueProcessor();
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

	@Override
	public String getResourceName() {
		return wrapped.getResourceName();
	}

	@Override
	public String getLibraryName() {
		return wrapped.getLibraryName();
	}

}
