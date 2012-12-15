package com.bc.sass.filter;

import com.bc.sass.SassConfig;
import com.bc.sass.SassException;
import com.bc.sass.Syntax;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author vvasabi
 */
public class JRubyFilter implements SassFilter {

	private static final String JRUBY_ENGINE = "jruby";
	private static final String COMPILE_SCRIPT = "compile_sass.rb";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JRubyFilter.class);

	static {
		// Make sure JRuby variables are stored in ThreadLocal storage
		System.setProperty("org.jruby.embed.localcontext.scope", "threadsafe");
	}

	@Override
	public String process(String input, Syntax syntax, SassConfig config,
						  SassFilterChain filterChain) {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName(JRUBY_ENGINE);
			Bindings bindings = new SimpleBindings();
			bindings.put("input", input);
			bindings.put("style", config.getStyle().toString());
			bindings.put("syntax", syntax.toString());
			return engine.eval(getCompileScript(), bindings).toString();
		} catch (ScriptException exception) {
			LOGGER.error("Error rendering SASS script.", exception);
			throw new SassException("Error rendering SASS script.", exception);
		}
	}

	private Reader getCompileScript() {
		return getReader(COMPILE_SCRIPT);
	}

	private Reader getReader(String file) {
		ClassLoader tcl = Thread.currentThread().getContextClassLoader();
		return new InputStreamReader(tcl.getResourceAsStream(file));
	}

}
