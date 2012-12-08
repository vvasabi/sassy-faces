package com.bc.sass.filter;

import com.bc.sass.SassConfig;

/**
 * @author vvasabi
 */
public abstract class AbstractSassFilter implements SassFilter {

	private final SassConfig config;

	public AbstractSassFilter(SassConfig config) {
		this.config = config.clone();
	}

	public SassConfig getConfig() {
		return config;
	}

}
