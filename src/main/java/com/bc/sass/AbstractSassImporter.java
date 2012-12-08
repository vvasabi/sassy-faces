package com.bc.sass;

import com.bc.sass.filter.ProcessFilter;
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
		String sassScriptContent = null;
		if (path.endsWith(".scss")) {
			sassScriptContent = loadSassScriptContent(path);
			if (sassScriptContent == null) {
				return null;
			}
		} else if (path.endsWith(".sass")) {
			syntax = Syntax.SASS;
			sassScriptContent = loadSassScriptContent(path);
			if (sassScriptContent == null) {
				return null;
			}
		} else {
			String relativeFilePath = getRelativeFilePath(path, syntax);
			sassScriptContent = loadSassScriptContent(relativeFilePath);
			if (sassScriptContent == null) {
				syntax = Syntax.SASS;
				relativeFilePath = getRelativeFilePath(path, syntax);
				sassScriptContent = loadSassScriptContent(relativeFilePath);
			}
			if (sassScriptContent == null) {
				return null;
			}
			path = relativeFilePath;
		}

		ProcessFilter filter = new ProcessFilter();
		filter.setLoadPath(root);
		String processed = filter.process(sassScriptContent);
		return new SassFile(path, processed, syntax);
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

	@Override
	public String importSassFile(String uri) {
		return find(uri, null).getFileContent();
	}

	public String getRoot() {
		return root;
	}

	protected abstract String loadSassScriptContent(String relativePath);

}
