package com.bc.sass;

/**
 * @author vvasabi
 */
public final class SassConfig implements Cloneable {

	private Style style = Style.COMPRESSED;
	private String loadPath = "";
	private boolean compassEnabled;
	private String cacheLocation;

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

	public boolean isCompassEnabled() {
		return compassEnabled;
	}

	public void setCompassEnabled(boolean compassEnabled) {
		this.compassEnabled = compassEnabled;
	}

	public String getCacheLocation() {
		return cacheLocation;
	}

	public void setCacheLocation(String cacheLocation) {
		this.cacheLocation = cacheLocation;
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
