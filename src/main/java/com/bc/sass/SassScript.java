package com.bc.sass;

import java.io.Serializable;

/**
 * @author vvasabi
 */
public final class SassScript implements Serializable {

	private static final long serialVersionUID = -2547727465287121467L;

	private final String content;

	private final Syntax syntax;

	public SassScript(String content, Syntax syntax) {
		this.content = content;
		this.syntax = syntax;
	}

	public String getContent() {
		return content;
	}

	public Syntax getSyntax() {
		return syntax;
	}

}
