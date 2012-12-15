package com.bc.sass.filter;

import com.bc.sass.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vvasabi
 */
public class ImportFilter implements SassFilter {

	private static final String TEMP_FILE_PREFIX = "sassy-faces-";
	private static final Pattern IMPORT_SUB_REGEX
		= Pattern.compile("\"([^\"]+)\"");
	private static final Pattern IMPORT_REGEX
		= Pattern.compile("^@import[\\s]+\"([^\"]+)\"((,[\\s]*\"[^\"]+\")*)"
			+ "[\\s]*;?[\\s]*$", Pattern.MULTILINE);
	private static final Pattern URL_REGEX = Pattern.compile("^[^:]+://.+");
	private static final Pattern CSS_REGEX = Pattern.compile(".+\\.css$");

	@Override
	public String process(SassScript script, SassConfig config,
						  SassFilterChain filterChain) {
		List<File> importedFiles = new ArrayList<File>();
		StringBuffer sb = new StringBuffer();
		Matcher matcher = IMPORT_REGEX.matcher(script.getContent());
		while (matcher.find()) {
			String statement = matcher.group();
			String fileUri = matcher.group(1);
			String repeatedGroup = matcher.group(2);

			// Ignored @import statements
			if ((repeatedGroup == null)
					&& (URL_REGEX.matcher(fileUri).matches()
					|| CSS_REGEX.matcher(fileUri).matches())) {
				matcher.appendReplacement(sb, statement);
				continue;
			}

			List<String> fileUris = new ArrayList<String>();
			fileUris.add(fileUri);
			if (repeatedGroup != null) {
				Matcher subMatcher = IMPORT_SUB_REGEX.matcher(repeatedGroup);
				while (subMatcher.find()) {
					fileUris.add(subMatcher.group(1));
				}
			}

			for (String uri : fileUris) {
				SassScript imported = importSassFile(uri, config);
				File tempFile = createTemporaryFile(imported);
				importedFiles.add(tempFile);
			}

			String importStatement = createImportStatement(script.getSyntax(),
					importedFiles);
			String replace = Matcher.quoteReplacement(importStatement);
			matcher.appendReplacement(sb, replace);
		}
		matcher.appendTail(sb);

		// set load path
		if (!importedFiles.isEmpty()) {
			File tempFile = importedFiles.get(0);
			String path = FilenameUtils.getFullPath(tempFile.getAbsolutePath());
			config.setLoadPath(path);
		}

		String result = filterChain.process(new SassScript(sb.toString(),
				script.getSyntax()), config);
		deleteFiles(importedFiles);
		return result;
	}

	private String createImportStatement(Syntax syntax, List<File> files) {
		StringBuilder sb = new StringBuilder();
		sb.append("@import ");
		boolean first = true;
		for (File file : files) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append("\"");
			sb.append(FilenameUtils.removeExtension(file.getName()));
			sb.append("\"");
		}
		if (syntax == Syntax.SCSS) {
			sb.append(";");
		}
		return sb.toString();
	}

	private File createTemporaryFile(SassScript script) {
		try {
			File tempFile = File.createTempFile(TEMP_FILE_PREFIX,
					script.getSyntax().getExtension());
			FileUtils.write(tempFile, script.getContent());
			return tempFile;
		} catch (IOException exception) {
			throw new SassException("Error occurred when trying to create a "
					+ "temporary file.", exception);
		}
	}

	private void deleteFiles(List<File> tempFiles) {
		for (File file : tempFiles) {
			FileUtils.deleteQuietly(file);
		}
	}

	private SassScript importSassFile(String uri, SassConfig config) {
		SassImporterFactory factory = SassImporterFactory.getInstance();
		SassImporter importer = factory.createSassImporter(config);

		SassScript imported = importer.importSassFile(uri);
		if (imported == null) {
			throw new SassException("Unable to import file: " + uri);
		}
		return imported;
	}

}
