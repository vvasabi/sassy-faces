package com.bc.sass;

/**
 * Output style options.
 *
 * @author vvasabi
 */
public enum Style {

	NESTED,
	EXPANDED,
	COMPACT,
	COMPRESSED;

	public String toString() {
		return super.toString().toLowerCase();
	}

}
