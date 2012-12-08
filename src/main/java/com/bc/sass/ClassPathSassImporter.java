package com.bc.sass;

import java.io.InputStream;

/**
 * @author vvasabi
 */
public class ClassPathSassImporter extends AbstractSassImporter {

	public ClassPathSassImporter(String root) {
		super(root);
	}

	@Override
	protected InputStream getFileInputStream(String relativePath) {
		ClassLoader tcl = Thread.currentThread().getContextClassLoader();
		return tcl.getResourceAsStream(relativePath);
	}

}
