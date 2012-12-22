package com.bc.sass.filter;

import com.bc.sass.SassConfig;
import com.bc.sass.SassException;
import com.bc.sass.SassScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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
	public void process(SassScript script, SassConfig config,
						SassFilterChain filterChain) {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName(JRUBY_ENGINE);
			Bindings bindings = new SimpleBindings();
			bindings.put("input", script.getContent());
			bindings.put("style", config.getStyle().toString());
			bindings.put("syntax", script.getSyntax().toString());
			bindings.put("compass", config.isCompassEnabled());
			bindings.put("cache_location", config.getCacheLocation());
			bindings.put("load_paths", getLoadPaths(config));
			script.setContent(engine.eval(getCompileScript(), bindings)
					.toString());
		} catch (ScriptException exception) {
			LOGGER.error("Error rendering SASS script.", exception);
			throw new SassException("Error rendering SASS script.", exception);
		}
	}

	private List<String> getLoadPaths(SassConfig config) {
		List<String> loadPaths = new ArrayList<String>(1);
		loadPaths.add(config.getLoadPath());
		return loadPaths;
	}

	private Reader getCompileScript() {
		ClassLoader tcl = Thread.currentThread().getContextClassLoader();
		return new InputStreamReader(tcl.getResourceAsStream(COMPILE_SCRIPT));
	}

}
