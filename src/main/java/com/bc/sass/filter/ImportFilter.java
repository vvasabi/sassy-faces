package com.bc.sass.filter;

import com.bc.sass.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vvasabi
 */
public class ImportFilter extends AbstractSassFilter {

	private static final Pattern IMPORT_SUB_REGEX
		= Pattern.compile("\"([^\"]+)\"");
	private static final Pattern IMPORT_REGEX
		= Pattern.compile("^@import[\\s]+\"([^\"]+)\"((,[\\s]*\"[^\"]+\")*)"
			+ "[\\s]*;?[\\s]*$", Pattern.MULTILINE);
	private static final Pattern URL_REGEX = Pattern.compile("^[^:]+://.+");
	private static final Pattern CSS_REGEX = Pattern.compile(".+\\.css$");

	public ImportFilter(SassConfig config) {
		super(config);
	}

	@Override
	public String process(String input, Syntax syntax) {
		StringBuffer sb = new StringBuffer();
		Matcher matcher = IMPORT_REGEX.matcher(input);
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

			StringBuilder result = new StringBuilder();
			for (String uri : fileUris) {
				result.append(importSassFile(uri, syntax));
			}
			matcher.appendReplacement(sb, result.toString());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	private String importSassFile(String uri, Syntax fromSyntax) {
		SassImporterFactory factory = SassImporterFactory.getInstance();
		SassImporter importer = factory.createSassImporter(getConfig());

		String result = importer.importSassFile(uri, fromSyntax);
		if (result == null) {
			throw new SassException("Unable to import file: " + uri);
		}
		return result;
	}

}