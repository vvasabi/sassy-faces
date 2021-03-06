package com.bc.sass.filter;

import com.bc.sass.SassConfig;
import com.bc.sass.SassScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * @author vvasabi
 */
public class ProcessFilter implements SassFilter {

	private final static Logger LOGGER
			= LoggerFactory.getLogger(ProcessFilter.class);

	private String filename;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public void process(SassScript script, SassConfig config,
						SassFilterChain filterChain) {
		String filterName = NativeFilter.class.getName();
		if (isNativeGemAvailable()) {
			filterChain.addFilter(new NativeFilter());
		} else {
			LOGGER.info("Native sass gem is not available. Processing sass "
					+ "with JRuby may be a lot slower.");
			filterName = JRubyFilter.class.getName();
			filterChain.addFilter(new JRubyFilter());
		}

		Date start = new Date();
		filterChain.process(script, config);
		Date end = new Date();
		if (LOGGER.isDebugEnabled()) {
			long duration = end.getTime() - start.getTime();
			String name = (filename == null) ? "<filename not specified>"
					: filename;
			LOGGER.debug("Time taken to parse file {} using {}: {}s", name,
					filterName, duration / 1000.0);
		}
	}

	private boolean isNativeGemAvailable() {
		try {
			// Windows not supported
			if (System.getProperty("os.name").startsWith("Windows")) {
				return false;
			}

			Process process = Runtime.getRuntime().exec("which sass");
			return process.waitFor() == 0;
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		} catch (InterruptedException exception) {
			throw new RuntimeException(exception);
		}
	}

}
