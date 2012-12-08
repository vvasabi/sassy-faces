package com.bc.sass.faces;

import com.bc.sass.AbstractSassImporter;
import org.apache.commons.io.IOUtils;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;

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
	protected String loadSassScriptContent(String relativePath) {
		try {
			String resourcePath = "resources/" + relativePath;
			ExternalContext externalContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			ELValueProcessor processor = new ELValueProcessor();
			InputStream is = externalContext.getResourceAsStream(resourcePath);
			try {
				return processor.process(IOUtils.toString(is));
			} finally {
				IOUtils.closeQuietly(is);
			}
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

}
