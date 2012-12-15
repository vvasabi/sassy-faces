package com.bc.sass;

import com.bc.sass.filter.ImportFilter;
import com.bc.sass.filter.ProcessFilter;
import com.bc.sass.filter.SassFilterChain;

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
		return createSassFilterChain(filename).process(input, syntax, config);
	}

	public String processFile(String uri) {
		SassImporterFactory factory = SassImporterFactory.getInstance();
		SassImporter sassImporter = factory.createSassImporter(config);

		Syntax syntax = determineSyntax(uri);
		String imported = sassImporter.importSassFile(uri, syntax);
		return createSassFilterChain(uri).process(imported, syntax, config);
	}

	private Syntax determineSyntax(String uri) {
		if (uri.endsWith(Syntax.SCSS.getExtension())) {
			return Syntax.SCSS;
		}
		return Syntax.SASS;
	}

	private SassFilterChain createSassFilterChain(String filename) {
		SassFilterChain filterChain = new SassFilterChain();
		ProcessFilter processFilter = new ProcessFilter();
		processFilter.setFilename(filename);
		filterChain.addFilter(processFilter);
		filterChain.addFilter(new ImportFilter());
		return filterChain;
	}

}
