package com.bc.sass.faces;

import com.bc.sass.SassImporter;
import com.bc.sass.SassImporterFactory;

/**
 * @author vvasabi
 */
public class FacesSassImporterFactory extends SassImporterFactory {

	public FacesSassImporterFactory() {
		SassImporterFactory.setInstance(this);
	}

	@Override
	public SassImporter createSassImporter(String root) {
		return new FacesSassImporter(root);
	}

}
