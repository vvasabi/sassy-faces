package com.bc.sass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
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

	private static final String JRUBY_ENGINE = "jruby";
	private static final String JAVA_IMPORTER_SCRIPT = "jruby_importer.rb";
	private static final String COMPILE_SCRIPT = "compile_sass.rb";
	private static final Logger LOGGER = LoggerFactory
		.getLogger(SassProcessor.class);

	private final String filename;
	private Style style = Style.COMPRESSED;
	private Syntax syntax = Syntax.SASS;
	private final List<String> loadPaths;

	static {
		// Make sure JRuby variables are stored in ThreadLocal storage
		System.setProperty("org.jruby.embed.localcontext.scope", "threadsafe");
	}

	public SassProcessor() {
		filename = null;
		loadPaths = new ArrayList<String>();
	}

	public SassProcessor(String filename) {
		this.filename = filename;
		loadPaths = new ArrayList<String>();
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public Syntax getSyntax() {
		return syntax;
	}

	public void setSyntax(Syntax syntax) {
		this.syntax = syntax;
	}

	public void addLoadPath(String path) {
		loadPaths.add(path);
	}

	public String process(String input) {
		try {
			Date start = new Date();
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName(JRUBY_ENGINE);
			Bindings bindings = new SimpleBindings();
			bindings.put("input", input);
			bindings.put("style", style.toString());
			bindings.put("syntax", syntax.toString());
			bindings.put("load_paths", loadPaths);
			engine.eval(getJavaImporterScript());
			String result = engine.eval(getCompileScript(), bindings)
					.toString();
			Date end = new Date();
			if (LOGGER.isDebugEnabled()) {
				long duration = end.getTime() - start.getTime();
				String name = (filename == null) ? "<filename not specified>"
						: filename;
				LOGGER.debug("Time taken to parse {}: {}s", name,
						duration / 1000.0);
			}
			return result;
		} catch (ScriptException exception) {
			LOGGER.error("Error rendering SASS script.", exception);
			throw new SassException("Error rendering SASS script.", exception);
		}
	}

	private Reader getJavaImporterScript() {
		return getReader(JAVA_IMPORTER_SCRIPT);
	}

	private Reader getCompileScript() {
		return getReader(COMPILE_SCRIPT);
	}

	private Reader getReader(String file) {
		ClassLoader tcl = Thread.currentThread().getContextClassLoader();
		InputStream is = tcl.getResourceAsStream(file);
		return new InputStreamReader(is);
	}

}
