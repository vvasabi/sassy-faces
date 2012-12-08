package com.bc.sass;

/**
 * @author vvasabi
 */
public class ClassPathSassImporterFactory extends SassImporterFactory {

	@Override
	public SassImporter createSassImporter(SassConfig config) {
		return new ClassPathSassImporter(config);
	}

}
