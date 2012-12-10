package com.bc.sass;

import com.bc.sass.filter.ProcessFilter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author vvasabi
 */
public abstract class AbstractSassImporter implements SassImporter {

	private final SassConfig config;

	protected AbstractSassImporter(SassConfig config) {
		this.config = config.clone();
	}

	public SassConfig getConfig() {
		return config;
	}

	@Override
	public String importSassFile(String uri, Syntax fromSyntax) {
		String path = getFilePath(uri, config.getLoadPath());
		Syntax syntax = Syntax.SCSS;
		String sassScriptContent;
		if (path.endsWith(Syntax.SCSS.getExtension())) {
			sassScriptContent = loadSassScriptContent(path);
			if (sassScriptContent == null) {
				return null;
			}
		} else if (path.endsWith(Syntax.SASS.getExtension())) {
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
		}

		if (syntax != fromSyntax) {
			throw new SassException("Importing scripts of a different syntax "
				+ "is currently unsupported.");
		}

		return sassScriptContent;
	}

	private String getRelativeFilePath(String path, Syntax syntax) {
		return path + syntax.getExtension();
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

	protected abstract String loadSassScriptContent(String relativePath);

}
