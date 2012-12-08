package com.bc.sass;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author vvasabi
 */
public class ClassPathSassImporter extends AbstractSassImporter {

	public ClassPathSassImporter(SassConfig config) {
		super(config);
	}

	@Override
	protected String loadSassScriptContent(String relativePath) {
		try {
			ClassLoader tcl = Thread.currentThread().getContextClassLoader();
			InputStream is = tcl.getResourceAsStream(relativePath);
			if (is == null) {
				throw new RuntimeException("File not found: " + relativePath);
			}

			try {
				return IOUtils.toString(is);
			} finally {
				IOUtils.closeQuietly(is);
			}
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

}
