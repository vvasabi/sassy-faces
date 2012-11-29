package com.bc.sass;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use JRuby and SASS gem to process SASS files.
 *
 * @author vvasabi
 */
public class SassProcessor {

	private static final String JRUBY_ENGINE = "jruby";
	private static final String COMPILE_SCRIPT = "compile-sass.rb";
	private static final Logger LOGGER = LoggerFactory
		.getLogger(SassProcessor.class);

	private Style style = Style.COMPRESSED;
	private Syntax syntax = Syntax.SASS;

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

	public String process(String input) {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName(JRUBY_ENGINE);
			Bindings bindings = new SimpleBindings();
			bindings.put("input", input);
			bindings.put("style", style.toString());
			bindings.put("syntax", syntax.toString());
			return engine.eval(getCompileScript(), bindings).toString();
		} catch (ScriptException exception) {
			LOGGER.error("Error rendering SASS script.", exception);
			throw new SassException("Error rendering SASS script.", exception);
		}
	}

	private Reader getCompileScript() {
		ClassLoader tcl = Thread.currentThread().getContextClassLoader();
		InputStream is = tcl.getResourceAsStream(COMPILE_SCRIPT);
		return new InputStreamReader(is);
	}

}