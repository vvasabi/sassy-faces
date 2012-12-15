package com.bc.sass;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vvasabi
 */
public abstract class AbstractSassImporter implements SassImporter {

	private final SassConfig config;

	protected AbstractSassImporter(SassConfig config) {
		this.config = config.clone();
	}

	@Override
	public SassScript importSassFile(String uri) {
		String path = getFilePath(uri, config.getLoadPath());
		Syntax syntax = Syntax.SCSS;
		String content = null;
		if (path.endsWith(Syntax.SCSS.getExtension())) {
			content = loadSassScriptContent(path);
		} else if (path.endsWith(Syntax.SASS.getExtension())) {
			syntax = Syntax.SASS;
			content = loadSassScriptContent(path);
		}
		if (content != null) {
			return new SassScript(content, syntax);
		}
		return findSassScript(generateSearchPaths(path));
	}

	private List<String> generateSearchPaths(String path) {
		List<String> paths = new ArrayList<String>();
		String scss = path + Syntax.SCSS.getExtension();
		String sass = path + Syntax.SASS.getExtension();
		paths.add(scss);
		paths.add(getPartialPath(scss));
		paths.add(sass);
		paths.add(getPartialPath(sass));
		return paths;
	}

	private String getPartialPath(String path) {
		return FilenameUtils.getFullPath(path) + "_"
				+ FilenameUtils.getName(path);
	}

	private SassScript findSassScript(List<String> searchPaths) {
		for (String path : searchPaths) {
			String content = loadSassScriptContent(path);
			if (content == null) {
				continue;
			}
			Syntax syntax = Syntax.SCSS;
			if (path.endsWith(Syntax.SASS.getExtension())) {
				syntax = Syntax.SASS;
			}
			return new SassScript(content, syntax);
		}
		return null;
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
