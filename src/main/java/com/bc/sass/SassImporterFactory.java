package com.bc.sass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vvasabi
 */
public abstract class SassImporterFactory {

	private static final Logger LOGGER
			= LoggerFactory.getLogger(SassImporterFactory.class);

	private static SassImporterFactory instance;

	public abstract SassImporter createSassImporter(SassConfig config);

	public static SassImporterFactory getInstance() {
		return instance;
	}

	public static void setInstance(SassImporterFactory instance) {
		if (SassImporterFactory.instance != null) {
			LOGGER.warn("A SassImporterFactory instance has already been set.");
			return;
		}
		if (instance == null) {
			throw new IllegalArgumentException("Instance cannot be null.");
		}
		SassImporterFactory.instance = instance;
	}

}
