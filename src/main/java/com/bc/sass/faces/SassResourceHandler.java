package com.bc.sass.faces;

import com.bc.sass.SassConfig;
import com.bc.sass.SassImporterFactory;
import com.bc.sass.Style;
import com.bc.sass.Syntax;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
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
	private final ConcurrentMap<String, Long> cacheTime;

	static {
		if (SassImporterFactory.getInstance() == null) {
			SassImporterFactory.setInstance(new FacesSassImporterFactory());
		}
	}

	public SassResourceHandler(ResourceHandler handler) {
		this.wrapped = handler;
		this.cache = new ConcurrentHashMap<String, Resource>();
		this.cacheTime = new ConcurrentHashMap<String, Long>();
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
		String resourceKey = buildResourceKey(resource);

		// in dev stage we need to invalidate cached resource upon change
		FacesContext context = FacesContext.getCurrentInstance();
		Long resourceCacheTime = cacheTime.get(resourceKey);
		try {
			if (context.isProjectStage(ProjectStage.Development)
					&& (resourceCacheTime != null)
					&& (getLastModified(resource) > resourceCacheTime)) {
				LOGGER.debug("Resource change detected; reload SASS file...");
				return createAndCacheSassResource(resource);
			}
		} catch (IOException exception) {
			// NOOP
		}

		Resource cached = cache.get(resourceKey);
		return (cached == null) ? createAndCacheSassResource(resource) : cached;
	}

	private long getLastModified(Resource resource) throws IOException {
		// from: org.apache.myfaces.resource.ResourceUtils
		URL url = resource.getURL();
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

	private Resource createAndCacheSassResource(Resource resource) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Compile SASS filesss {}...", resource.getResourceName());
		}
		String resourceKey = buildResourceKey(resource);
		Resource sassResource = new SassResource(resource, loadSassConfig());
		cache.put(resourceKey, sassResource);
		cacheTime.put(resourceKey, (new Date()).getTime());
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
