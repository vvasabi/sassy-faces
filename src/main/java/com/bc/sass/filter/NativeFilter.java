package com.bc.sass.filter;

import com.bc.sass.SassConfig;
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
public class NativeFilter extends AbstractSassFilter {

	public NativeFilter(SassConfig config) {
		super(config);
	}

	@Override
	public String process(String input, Syntax syntax) {
		try {
			List<String> command = new ArrayList<String>();
			command.add("sass");
			command.add("-C");
			command.add("-s");
			if (syntax == Syntax.SCSS) {
				command.add("--scss");
			}
			command.add("--style");
			command.add(getConfig().getStyle().toString());

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

}
