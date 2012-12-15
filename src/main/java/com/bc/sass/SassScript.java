package com.bc.sass;

/**
 * @author vvasabi
 */
public final class SassScript {

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
