package com.bc.sass;

import com.bc.sass.filter.JRubyFilter;
import com.bc.sass.filter.ProcessFilter;
import com.bc.sass.filter.SassFilter;
import org.jruby.cext.JRuby;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Use JRuby and SASS gem to process SASS files.
 *
 * @author vvasabi
 */
public class SassProcessor {

	private final ProcessFilter filter;

	public SassProcessor() {
		filter = new ProcessFilter();
	}

	public SassProcessor(String filename) {
		this();
		filter.setFilename(filename);
	}

	public String getFilename() {
		return filter.getFilename();
	}

	public void setFilename(String filename) {
		filter.setFilename(filename);
	}

	public Syntax getSyntax() {
		return filter.getSyntax();
	}

	public void setSyntax(Syntax syntax) {
		filter.setSyntax(syntax);
	}

	public String getLoadPath() {
		return filter.getLoadPath();
	}

	public void setLoadPath(String loadPath) {
		filter.setLoadPath(loadPath);
	}

	public Style getStyle() {
		return filter.getStyle();
	}

	public void setStyle(Style style) {
		filter.setStyle(style);
	}

	public String process(String input) {
		return filter.process(input);
	}

	public String processFile(String uri) {
		SassImporterFactory factory = SassImporterFactory.getInstance();
		SassImporter sassImporter = factory.createSassImporter(getLoadPath());
		return sassImporter.importSassFile(uri);
	}

}
