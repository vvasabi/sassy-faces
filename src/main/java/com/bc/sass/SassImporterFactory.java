package com.bc.sass;

/**
 * @author vvasabi
 */
public abstract class SassImporterFactory {

	private static SassImporterFactory instance;

	public abstract SassImporter createSassImporter(String root);

	public static SassImporterFactory getInstance() {
		return instance;
	}

	public static void setInstance(SassImporterFactory instance) {
		if (SassImporterFactory.instance != null) {
			throw new RuntimeException("A SassImporterFactory instance has "
					+ "already been set.");
		}
		if (instance == null) {
			throw new IllegalArgumentException("Instance cannot be null.");
		}
		SassImporterFactory.instance = instance;
	}
}
