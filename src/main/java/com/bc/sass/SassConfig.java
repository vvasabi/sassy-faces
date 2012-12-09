package com.bc.sass;

/**
 * @author vvasabi
 */
public final class SassConfig implements Cloneable {

	private Style style = Style.COMPRESSED;
	private String loadPath = "";

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public String getLoadPath() {
		return loadPath;
	}

	public void setLoadPath(String loadPath) {
		this.loadPath = loadPath;
	}

	@Override
	public SassConfig clone() {
		try {
			SassConfig cloned = (SassConfig) super.clone();
			cloned.setLoadPath(loadPath);
			cloned.setStyle(style);
			return cloned;
		} catch (CloneNotSupportedException exception) {
			return null;
		}
	}

}