package com.bc.sass;

/**
 * @author vvasabi
 */
public class ClassPathSassImporterFactory extends SassImporterFactory {

	@Override
	public SassImporter createSassImporter(String root) {
		return new ClassPathSassImporter(root);
	}

}
