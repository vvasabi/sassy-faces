package com.bc.sass.faces;

import com.bc.sass.SassConfig;
import com.bc.sass.SassProcessor;
import com.bc.sass.SassScript;
import org.apache.commons.io.IOUtils;

import javax.faces.application.Resource;
import javax.faces.application.ResourceWrapper;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static com.bc.sass.faces.SassResourceHandler.CSS_CONTENT_TYPE;

/**
 * An instance of SASS Resource.
 *
 * @author vvasabi
 */
public class SassResource extends ResourceWrapper {

	private static final String UTF8_CHARSET = "UTF-8";
	private static final String IF_MODIFIED_SINCE_HEADER = "If-Modified-Since";
	private static final String LAST_MODIFIED_HEADER = "Last-Modified";
	private static final String CONTENT_TYPE_HEADER = "Content-Type";
	private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
	private static final String[] HTTP_REQUEST_DATE_HEADERS = {
		"EEE, dd MMM yyyy HH:mm:ss zzz",
		"EEEEEE, dd-MMM-yy HH:mm:ss zzz",
		"EEE MMMM d HH:mm:ss yyyy"
	};
	private static final String HTTP_RESPONSE_DATE_HEADER =
		"EEE, dd MMM yyyy HH:mm:ss zzz";

	private final Resource wrapped;
	private final SassScript sassScript;
	private final SassConfig config;
	private final long lastModified;

	/**
	 * Create a SassResource with a generic Resource that can be wrapped and
	 * config settings for the Sass compiler.
	 *
	 * @param resource generic Resource that can be wrapped
	 * @param config settings for the Sass compiler
	 */
	public SassResource(Resource resource, SassConfig config) {
		this.wrapped = resource;
		this.config = config;
		this.sassScript = renderSassScript();
		this.lastModified = System.currentTimeMillis();
	}

	protected SassScript renderSassScript() {
		// dispatch rendering to SassProcessor
		SassProcessor processor = new SassProcessor();
		config.setLoadPath(getLibraryName());
		processor.setConfig(config);
		return processor.processFile(getResourceName());
	}

	@Override
	public Map<String, String> getResponseHeaders() {
		Map<String, String> responseHeaders = wrapped.getResponseHeaders();
		responseHeaders.put(CONTENT_TYPE_HEADER, CSS_CONTENT_TYPE);
		responseHeaders.put(LAST_MODIFIED_HEADER,
			formatResponseDateHeader(lastModified));
		return responseHeaders;
	}

	private String formatResponseDateHeader(long time) {
		// from org.apache.myfaces.shared.resource.ResourceLoaderUtils
		SimpleDateFormat format = new SimpleDateFormat(
			HTTP_RESPONSE_DATE_HEADER, Locale.US);
		format.setTimeZone(GMT);
		return format.format(new Date(time));
	}

	@Override
	public InputStream getInputStream() throws IOException {
		byte[] bytes = sassScript.getContent().getBytes(UTF8_CHARSET);
		return new ByteArrayInputStream(bytes);
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

	@Override
	public boolean userAgentNeedsUpdate(FacesContext context) {
		// from org.apache.myfaces.resource.ResourceImpl
		String cacheHeader = context.getExternalContext()
			.getRequestHeaderMap().get(IF_MODIFIED_SINCE_HEADER);
		if (cacheHeader == null) {
			return true;
		}

		long cacheTime = parseDateHeader(cacheHeader);
		return greaterThanInSeconds(lastModified, cacheTime);
	}

	private long parseDateHeader(String value) {
		// from org.apache.myfaces.resource.ResourceUtils
		Date date = null;
		for (int i = 0; (date == null) && (i < HTTP_REQUEST_DATE_HEADERS.length);
				i++) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(
					HTTP_REQUEST_DATE_HEADERS[i], Locale.US);
				format.setTimeZone(GMT);
				date = format.parse(value);
			} catch (ParseException e) {
				// NOOP
			}
		}
		return (date == null) ? 0 : date.getTime();
	}

	private boolean greaterThanInSeconds(long a, long b) {
		return a / 1000 > b;
	}

	/**
	 * Check if the underlying Sass files, including ones that are imported,
	 * have changed.
	 *
	 * @return true if the underlying Sass files has changed; false otherwise
	 */
	boolean isOutdated() {
		return lastModified < getLastModified();
	}

	private long getLastModified() {
		FacesContext context = FacesContext.getCurrentInstance();
		long resourceLastModified = lastModified;
		try {
			for (String file : sassScript.getFiles()) {
				String path = FacesSassImporter.RESOURCE_PATH_PREFIX + file;
				URL url = context.getExternalContext().getResource(path);
				long urlLastModified = getUrlLastModified(url);
				if (urlLastModified > resourceLastModified) {
					resourceLastModified = urlLastModified;
				}
			}
			return resourceLastModified;
		} catch (MalformedURLException exception) {
			throw new RuntimeException("Unable to load resource.", exception);
		} catch (IOException exception) {
			throw new RuntimeException("Unable to check resource last modified "
					+ "time.", exception);
		}
	}

	private long getUrlLastModified(URL url) throws IOException {
		// from: org.apache.myfaces.resource.ResourceUtils
		if ("file".equals(url.getProtocol())) {
			File file = new File(url.toExternalForm().substring(5));
			return file.lastModified();
		}

		URLConnection connection = url.openConnection();
		if (connection instanceof JarURLConnection) {
			try {
				return connection.getLastModified();
			} finally {
				IOUtils.closeQuietly(connection.getInputStream());
			}
		}
		return connection.getLastModified();
	}

	@Override
	public String getRequestPath() {
		String path = wrapped.getRequestPath();

		// force the invalidation of cache for browsers that do not attempt to
		// check 304 status
		return path + "&v=" + (lastModified / 1000);
	}

}
