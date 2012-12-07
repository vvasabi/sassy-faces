package com.bc.sass;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.InputStream;
import java.util.Map;

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
