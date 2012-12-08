package com.bc.sass.faces;

import com.bc.sass.SassConfig;
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
	public SassImporter createSassImporter(SassConfig config) {
		return new FacesSassImporter(config);
	}

}
