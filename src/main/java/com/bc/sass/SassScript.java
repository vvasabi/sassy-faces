package com.bc.sass;

import java.io.Serializable;

/**
 * @author vvasabi
 */
public final class SassScript implements Serializable {

	private static final long serialVersionUID = -2547727465287121467L;

	private String content;

	private Syntax syntax;

	public SassScript(String content, Syntax syntax) {
		this.content = content;
		this.syntax = syntax;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Syntax getSyntax() {
		return syntax;
	}

	public void setSyntax(Syntax syntax) {
		this.syntax = syntax;
	}

}
