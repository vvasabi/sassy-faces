package com.bc.sass.filter;

import com.bc.sass.SassConfig;
import com.bc.sass.Syntax;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * @author vvasabi
 */
public class ProcessFilter extends AbstractSassFilter {

	private static Logger LOGGER = LoggerFactory.getLogger(ProcessFilter.class);

	private String filename;

	public ProcessFilter(SassConfig config) {
		super(config);
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public String process(String input, Syntax syntax) {
		ImportFilter importFilter = new ImportFilter(getConfig());

		SassFilter filter = new NativeFilter(getConfig());
		if (!isNativeGemAvailable()) {
			LOGGER.info("Native sass gem is not available. Processing sass "
					+ "with JRuby may be a lot slower.");
			filter = new JRubyFilter(getConfig());
		}

		Date start = new Date();
		String result = filter.process(importFilter.process(input, syntax),
				syntax);
		Date end = new Date();
		if (LOGGER.isDebugEnabled()) {
			long duration = end.getTime() - start.getTime();
			String name = (filename == null) ? "<filename not specified>"
					: filename;
			LOGGER.debug("Time taken to parse file {} using {}: {}s", name,
					filter.getClass(), duration / 1000.0);
		}
		return result;
	}

	private boolean isNativeGemAvailable() {
		try {
			// Windows not supported
			if (System.getProperty("os.name").startsWith("Windows")) {
				return false;
			}

			Process process = Runtime.getRuntime().exec("command -v sass");
			return process.waitFor() == 0;
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		} catch (InterruptedException exception) {
			throw new RuntimeException(exception);
		}
	}

}