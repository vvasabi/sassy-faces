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
			InputStream is = externalContext.getResourceAsStream(resourcePath);
			String content = IOUtils.toString(is);

			ELValueProcessor processor = new ELValueProcessor();
			return processor.process(content);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

}
