package com.bc.sass;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author vvasabi
 */
public abstract class AbstractSassImporter implements SassImporter {

	private static final Logger LOGGER
		= LoggerFactory.getLogger(AbstractSassImporter.class);

	private final String root;

	protected AbstractSassImporter(String root) {
		LOGGER.debug("path {}", root);
		this.root = root;
	}

	@Override
	public SassFile findRelative(String uri, String base,
								 Map<String, Object> options) {
		LOGGER.debug("findRelative {}, {}, {}", uri, base, options);
		String path = getFilePath(uri, base);
		Syntax syntax = Syntax.SCSS;
		String relativePath = getRelativeFilePath(path, syntax);
		String sassScriptContent = loadSassScriptContent(relativePath);
		if (sassScriptContent == null) {
			syntax = Syntax.SASS;
			relativePath = getRelativeFilePath(path, syntax);
			sassScriptContent = loadSassScriptContent(relativePath);
		}
		if (sassScriptContent == null) {
			return null;
		}

		return new SassFile(relativePath, sassScriptContent, syntax);
	}

	private String getRelativeFilePath(String path, Syntax syntax) {
		return path + "." + syntax.getExtension();
	}

	private String getFilePath(String uri, String base) {
		StringBuilder sb = new StringBuilder();
		if (!StringUtils.isBlank(base)) {
			sb.append(base);
			sb.append("/");
		}
		sb.append(uri);

		return sb.toString();
	}

	@Override
	public SassFile find(String uri, Map<String, Object> options) {
		LOGGER.debug("find {}, {}", uri, options);
		return findRelative(uri, root, options);
	}

	public String getRoot() {
		return root;
	}

	protected abstract String loadSassScriptContent(String relativePath);

}
