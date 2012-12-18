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

	public SassScript process(String input, Syntax syntax) {
		return process(input, syntax, null);
	}

	public SassScript process(String input, Syntax syntax, String filename) {
		SassScript script = new SassScript(input, syntax);
		createSassFilterChain(filename).process(script, config);
		return script;
	}

	public SassScript processFile(String uri) {
		SassImporterFactory factory = SassImporterFactory.getInstance();
		SassImporter sassImporter = factory.createSassImporter(config);
		SassScript script = sassImporter.importSassFile(uri);
		createSassFilterChain(uri).process(script, config);
		return script;
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
