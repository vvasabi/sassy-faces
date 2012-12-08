package com.bc.sass;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * @author vvasabi
 */
public class ClassPathSassImporter extends AbstractSassImporter {

	public ClassPathSassImporter(String root) {
		super(root);
	}

	@Override
	protected String loadSassScriptContent(String relativePath) {
		try {
			ClassLoader tcl = Thread.currentThread().getContextClassLoader();
			return IOUtils.toString(tcl.getResourceAsStream(relativePath));
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

}
