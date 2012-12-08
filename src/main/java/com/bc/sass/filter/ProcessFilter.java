package com.bc.sass.filter;

import com.bc.sass.Style;
import com.bc.sass.Syntax;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author vvasabi
 */
public class ProcessFilter implements SassFilter {

	private static Logger LOGGER = LoggerFactory.getLogger(ProcessFilter.class);

	private String filename;
	private Style style = Style.COMPRESSED;
	private Syntax syntax = Syntax.SCSS;
	private String loadPath = "";

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

	public String getLoadPath() {
		return loadPath;
	}

	public void setLoadPath(String loadPath) {
		this.loadPath = loadPath;
	}

	@Override
	public String process(String input) {
		ImportFilter importFilter = new ImportFilter();
		importFilter.setLoadPath(loadPath);
		String importFilterProcessed = importFilter.process(input);

		if (isNativeGemAvailable()) {
			return processWithNativeGem(importFilterProcessed);
		}

		LOGGER.info("Native sass gem is not available. Processing sass with "
				+ "JRuby may be a lot slower.");
		return processWithJRuby(importFilterProcessed);
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

	private String processWithNativeGem(String input) {
		try {
			List<String> command = new ArrayList<String>();
			command.add("sass");
			command.add("-C");
			command.add("-s");
			if (syntax == Syntax.SCSS) {
				command.add("--scss");
			}
			command.add("--style");
			command.add(style.toString());

			Process process = (new ProcessBuilder(command)).start();
			OutputStream os = process.getOutputStream();
			try {
				IOUtils.write(input, os);
			} finally {
				IOUtils.closeQuietly(os);
			}

			String result = null;
			InputStream is = process.getInputStream();
			try {
				result = IOUtils.toString(is);
			} finally {
				IOUtils.closeQuietly(is);
			}
			return result;
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	private String processWithJRuby(String input) {
		Date start = new Date();
		JRubyFilter filter = new JRubyFilter();
		filter.setStyle(style);
		filter.setSyntax(syntax);
		String result = filter.process(input);
		Date end = new Date();
		if (LOGGER.isDebugEnabled()) {
			long duration = end.getTime() - start.getTime();
			String name = (filename == null) ? "<filename not specified>"
					: filename;
			LOGGER.debug("Time taken to parse using JRuby {}: {}s", name,
					duration / 1000.0);
		}
		return result;
	}

}
