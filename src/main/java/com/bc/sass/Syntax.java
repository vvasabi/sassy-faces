package com.bc.sass;

/**
 * Syntax options.
 *
 * @author vvasabi
 */
public enum Syntax {

	SASS,
	SCSS;

	public String toString() {
		return super.toString().toLowerCase();
	}

	public String getExtension() {
		return toString();
	}

}
