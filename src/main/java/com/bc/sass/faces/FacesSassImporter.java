package com.bc.sass.faces;

import com.bc.sass.AbstractSassImporter;
import com.bc.sass.SassImporter;
import com.bc.sass.SassFile;
import com.bc.sass.Syntax;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Import Ruby
 *
 * @author vvasabi
 */
public class FacesSassImporter extends AbstractSassImporter {

	public FacesSassImporter(String library) {
		super(library);
	}

	@Override
	protected InputStream getFileInputStream(String relativePath) {
		String resourcePath = "resources/" + relativePath;
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		return externalContext.getResourceAsStream(resourcePath);
	}

}
