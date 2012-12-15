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

	public static Style parse(String value) {
		if (value == null) {
			throw new NullPointerException("Style is null.");
		}
		for (Style style : Style.values()) {
			if (style.toString().equals(value.toLowerCase())) {
				return style;
			}
		}
		throw new IllegalArgumentException("Invalid style: " + value);
	}

}
