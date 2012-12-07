package com.bc.sass;

/**
 * @author vvasabi
 */
public final class SassFile {

	private final String filename;
	private final String fileContent;
	private final Syntax syntax;

	public SassFile(String filename, String fileContent, Syntax syntax) {
		this.filename = filename;
		this.fileContent = fileContent;
		this.syntax = syntax;
	}

	public String getFilename() {
		return filename;
	}

	public String getFileContent() {
		return fileContent;
	}

	public Syntax getSyntax() {
		return syntax;
	}

}
