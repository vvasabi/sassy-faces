package com.bc.sass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author vvasabi
 */
public final class SassScript implements Serializable {

	private static final long serialVersionUID = -2547727465287121467L;

	private String content;
	private Syntax syntax;
	private final List<String> files;

	public SassScript(String content, Syntax syntax) {
		this.content = content;
		this.syntax = syntax;
		this.files = new ArrayList<String>(1);
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

	public void addFile(String file) {
		files.add(file);
	}

	public void addFiles(List<String> filesToAdd) {
		files.addAll(filesToAdd);
	}

	public List<String> getFiles() {
		return Collections.unmodifiableList(files);
	}

}
