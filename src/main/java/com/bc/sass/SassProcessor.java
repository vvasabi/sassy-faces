package com.bc.sass;

import com.bc.sass.filter.ProcessFilter;

/**
 * Use JRuby and SASS gem to process SASS files.
 *
 * @author vvasabi
 */
public class SassProcessor {

	private SassConfig config = new SassConfig();

	public SassConfig getConfig() {
		return config.clone();
	}

	public void setConfig(SassConfig config) {
		this.config = config.clone();
	}

	public String process(String input, Syntax syntax) {
		return process(input, syntax, null);
	}

	public String process(String input, Syntax syntax, String filename) {
		ProcessFilter filter = new ProcessFilter(config);
		filter.setFilename(filename);
		return filter.process(input, syntax);
	}

	public String processFile(String uri) {
		SassImporterFactory factory = SassImporterFactory.getInstance();
		SassImporter sassImporter = factory.createSassImporter(config);
		Syntax syntax = determineSyntax(uri);
		String imported = sassImporter.importSassFile(uri, syntax);
		ProcessFilter filter = new ProcessFilter(config);
		filter.setFilename(uri);
		return filter.process(imported, syntax);
	}

	private Syntax determineSyntax(String uri) {
		if (uri.endsWith(Syntax.SCSS.getExtension())) {
			return Syntax.SCSS;
		}
		return Syntax.SASS;
	}

}
