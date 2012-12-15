package com.bc.sass.filter;

import com.bc.sass.SassConfig;
import com.bc.sass.SassException;
import com.bc.sass.SassScript;
import com.bc.sass.Syntax;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vvasabi
 */
public class NativeFilter implements SassFilter {

	@Override
	public String process(SassScript script, SassConfig config,
						  SassFilterChain filterChain) {
		try {
			List<String> command = new ArrayList<String>();
			command.add("sass");
			command.add("-C");
			command.add("-s");
			if (script.getSyntax() == Syntax.SCSS) {
				command.add("--scss");
			}
			command.add("--style");
			command.add(config.getStyle().toString());
			command.add("--load-path");
			command.add(config.getLoadPath());
			if (config.isCompassEnabled()) {
				command.add("--compass");
			}

			ProcessBuilder processBuilder = new ProcessBuilder(command);
			Process process = processBuilder.start();
			OutputStream os = process.getOutputStream();
			try {
				IOUtils.write(script.getContent(), os);
			} finally {
				IOUtils.closeQuietly(os);
			}

			if (process.waitFor() != 0) {
				String message = null;
				InputStream is = process.getErrorStream();
				try {
					message = IOUtils.toString(is);
				} finally {
					IOUtils.closeQuietly(is);
				}
				throw new SassException(message);
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
		} catch (InterruptedException exception) {
			throw new RuntimeException(exception);
		}
	}

}
